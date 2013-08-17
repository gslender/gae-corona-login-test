package com.rednels.gaecorona;

import com.google.gson.Gson;

/**
 * Credentials class used to manage login
 * @author gslender
 *
 */
public class Credentials {

	private String username;
	private String password;

	/**
	 * Credentials default no-args constructor needed for Gson
	 */
	public Credentials()
	{
		
	}
	
	public Credentials(String username, String password)
	{
	    this.setUsername(username);
	    this.setPassword(password);
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String toJson() {
    	Gson gson = new Gson();
    	String json = gson.toJson(this);
    	return json;
    }
	
    public static Credentials constructFromJson(String json)
    {
    	Gson gson = new Gson();
    	return gson.fromJson(json, Credentials.class);
    }
}
