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
		//Credentials cred = Credentials.constructFromJson(sb.toString());

		// ensure we set the response to JSON
        response.setContentType("application/json");
//        PrintWriter out = response.getWriter();
//        LoginResponse lr = null;
//        
//        // do some credential checks to see if it matches the username/password
//		if (cred.getUsername().equals("user.name") && cred.getPassword().equals("s3cr3t")) {
//			lr = new LoginResponse(LoginResponse.Status.OK, "Login success!");
//		}
//		else {
//			lr = new LoginResponse(LoginResponse.Status.ERROR, "Login fail!");
//		}
//		
//		// finish by writing the object out as JSON
//        out.print(lr.toJson());
	}
	
}
