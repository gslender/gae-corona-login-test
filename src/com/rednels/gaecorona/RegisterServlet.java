package com.rednels.gaecorona;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

@SuppressWarnings("serial")
public class RegisterServlet extends HttpServlet {

	public static String REGISTER_SECRET = "s3cr3t";
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
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
		Registration reg = Registration.constructFromJson(sb.toString());

		// ensure we set the response to JSON
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        WebServiceResponse wsr = null;
        
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        
        String link = getHostURLString(request) + "/register?activation="+toHashString(reg.getEmail()+REGISTER_SECRET);
        System.out.println(link);

        String plainBody = "Dear "+reg.getFullname()+", please confirm registration by following this link " + link;
    	String htmlBody = "<html>";        // ...

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
            
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("admin@example.com", "Example.com Admin"));            
            msg.setSubject("Your Example.com account has been activated");
            msg.setContent(mp);
            
            msg.writeTo(System.out);
//            Transport.send(msg);

        } catch (AddressException e) {
            // ...
        } catch (MessagingException e) {
            // ...
        }
        // send an email to the user about how to register
//		if (cred.getUsername().equals("user.name") && cred.getPassword().equals("s3cr3t")) {
//			wsr = new WebServiceResponse(WebServiceResponse.Status.OK, "Login success!");
//		}
//		else {
//			wsr = new WebServiceResponse(WebServiceResponse.Status.ERROR, "Login fail!");
//		}
//		
//		// finish by writing the object out as JSON
//        out.print(wsr.toJson());
	}
	

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
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
