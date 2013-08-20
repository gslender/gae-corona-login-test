package com.rednels.gaecorona;

import com.google.gson.Gson;

/**
 * Credentials class used to manage register
 * @author gslender
 *
 */
public class Registration {

	private String fullname;
	private String password;
	private String email;

	/**
	 * Credentials default no-args constructor needed for Gson
	 */
	public Registration()
	{
		
	}
	
	public Registration(String fullname, String password)
	{
	    this.setFullname(fullname);
	    this.setPassword(password);
	}
	
	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String toJson() {
    	Gson gson = new Gson();
    	String json = gson.toJson(this);
    	return json;
    }
	
    public static Registration constructFromJson(String json)
    {
    	Gson gson = new Gson();
    	return gson.fromJson(json, Registration.class);
    }
}
