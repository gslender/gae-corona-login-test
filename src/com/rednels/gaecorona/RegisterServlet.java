package com.rednels.gaecorona;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
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

        String plainBody = "...";
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
            Transport.send(msg);

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
	
}
