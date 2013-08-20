package com.rednels.gaecorona;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.*;


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
