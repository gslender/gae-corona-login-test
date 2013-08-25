package com.rednels.gaecorona;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Properties;

import javax.servlet.http.*;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.PreparedQuery.TooManyResultsException;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.CompositeFilter;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;

@SuppressWarnings("serial")
public class RegisterServlet extends HttpServlet {

	public static String REGISTER_SECRET = "s3cr3t";
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		boolean failed = false;
		// first thing is to get requests content
		StringBuffer sb = new StringBuffer();
		BufferedReader reader = request.getReader();
		String line = reader.readLine();
		try {
			while (line != null) {
				// grab lines and pump them into a string buffer
		    	sb.append(line);
		    	line = reader.readLine();
		    }
		} catch (Exception e) { }		

		// now use the Gson methods to convert the received JSON into an expected Object
		Registration registrationObj = Registration.constructFromJson(sb.toString());

		// ensure we set the response to JSON
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        WebServiceResponse wsr = new WebServiceResponse(WebServiceResponse.Status.ERROR, "Registration fail!");
        
        String activationCode = toHashString(registrationObj.getEmail()+REGISTER_SECRET);
        String link = getHostURLString(request) + "/register?activation="+activationCode;
        //TODO need to find existing Users and deny creating new Registrations if email is registered already

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

		Filter activationEqualFilter = new FilterPredicate("username",FilterOperator.EQUAL,registrationObj.getEmail());
        Query q = new Query("Users").setFilter(activationEqualFilter);
        Entity user = datastore.prepare(q).asSingleEntity();
        
        
        
        Entity registrations = new Entity("Registrations");
        registrations.setProperty("activation", activationCode);
        registrations.setProperty("username", registrationObj.getEmail());
        registrations.setProperty("fullname", registrationObj.getFullname());
        registrations.setProperty("password", registrationObj.getPassword());
        registrations.setProperty("created", new Date());

        datastore.put(registrations);
        
        String plainBody = "Dear "+registrationObj.getFullname()+", please confirm registration by following this link " + link;
    	String htmlBody = "<html>"+plainBody+"<html>";        // ...

        System.out.println(plainBody);
        
        try {

            Multipart mp = new MimeMultipart();
            // Unformatted text version
            final MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText(plainBody);
            mp.addBodyPart(textPart);
            // HTML version
            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(htmlBody, "text/html");
            mp.addBodyPart(htmlPart);
            
            Properties props = new Properties();
            Session session = Session.getDefaultInstance(props, null);
            
            Message msg = new MimeMessage(session);
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(registrationObj.getEmail(), registrationObj.getFullname()));
            msg.setFrom(new InternetAddress("admin@example.com", "Example.com Admin"));            
            msg.setSubject("Your account has been activated");
            msg.setContent(mp);
            
//            msg.writeTo(System.out);
            Transport.send(msg);

        } catch (AddressException e) {
        	failed = true;
        } catch (MessagingException e) {
        	failed = true;
        }
        
        if (!failed) wsr = new WebServiceResponse(WebServiceResponse.Status.OK, "Registration Sent");
		// finish by writing the object out as JSON
        out.print(wsr.toJson());
	}
	

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

		boolean success = false;
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();        
        writer.println("<html>");
        writer.println("<head>");
        writer.println("<title>User Registration</title>");
        writer.println("</head>");
        writer.println("<body bgcolor=white>");
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        String activationCode = request.getParameter("activation");
        System.out.println(activationCode);
        if (activationCode == null) activationCode = "null";

		Filter activationEqualFilter = new FilterPredicate("activation",FilterOperator.EQUAL,activationCode);
        Query q = new Query("Registrations").setFilter(activationEqualFilter).addSort("created", SortDirection.ASCENDING);
        PreparedQuery pq = datastore.prepare(q);
        
		try {
			Iterable<Entity> registrations =  pq.asIterable();
			for (Entity registration : registrations) {
				if (!success) {
					String username = (String) registration.getProperty("username");
					String fullname = (String) registration.getProperty("fullname");
					String password = (String) registration.getProperty("password");
					
			        Entity users = new Entity("Users");
			        users.setProperty("username", username);
			        users.setProperty("fullname", fullname);
			        users.setProperty("password", password);
					users.setProperty("lastlogin", "never");
			        users.setProperty("created", new Date());
			        datastore.put(users);
			        writer.println("Hi "+fullname+", you may now continue using your" +
			        		" email address as your username and the password you" +
			        		" submited when registering.");
			        success = true;
				}
				System.out.println("deleted "+registration.getKey());
		        datastore.delete(registration.getKey());
			} 
	        
		} catch (TooManyResultsException  e) {
			System.out.println(e);
	        success = false;
		}

		if (!success) {
			writer.println("Activation code expired or invalid.");
		}

        writer.println("</body>");
        writer.println("</html>");
		
	}
	
	public String getHostURLString(HttpServletRequest request) {
		int port = request.getServerPort();

		if (request.getScheme().equals("http") && port == 80) {
		    port = -1;
		} else if (request.getScheme().equals("https") && port == 443) {
		    port = -1;
		}

		try {
			URL serverURL = new URL(request.getScheme(), request.getServerName(), port, "");
			return serverURL.toString();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String toHashString(String str)
    { 
        byte byteData[] = {};
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
	        md.update(str.getBytes());
	        byteData = md.digest();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return "token error";
		} 
 
        //convert the byte to hex format method 1
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
         sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
 
        return sb.toString();
    }
}
