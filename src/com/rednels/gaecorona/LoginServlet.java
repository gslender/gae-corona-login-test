package com.rednels.gaecorona;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.http.*;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery.TooManyResultsException;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;


@SuppressWarnings("serial")
public class LoginServlet extends HttpServlet {
	
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
		Credentials cred = Credentials.constructFromJson(sb.toString());

		// ensure we set the response to JSON
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        WebServiceResponse wsr = null;
        
        // do some credential checks to see if it matches the username/password
		Filter usernameEqualFilter = new FilterPredicate("username",FilterOperator.EQUAL,cred.getUsername());
        Query q = new Query("Users").setFilter(usernameEqualFilter);
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		try {
			Entity users = datastore.prepare(q).asSingleEntity();
			
			if (users != null) {
				String password = (String) users.getProperty("password");

				if (cred.getPassword().equals(password)) {
					users.setProperty("lastlogin", new Date());
					datastore.put(users);
					wsr = new WebServiceResponse(WebServiceResponse.Status.OK, "Login Success!");
					// finish by writing the object out as JSON
			        out.print(wsr.toJson());
			        return;
				}				
			}
		} catch (TooManyResultsException e) {
		}
		wsr = new WebServiceResponse(WebServiceResponse.Status.ERROR, "Login fail!");
		// finish by writing the object out as JSON
        out.print(wsr.toJson());	
	}
}
