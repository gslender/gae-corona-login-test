package com.rednels.gaecorona;

import com.google.gson.Gson;

/**
 * LoginResponse class used for login success/failure
 * @author gslender
 *
 */
public class LoginResponse {

	public enum Status {
	
		ERROR, OK
	}

	private Status status;
	private String message;

	/**
	 * LoginResponse default no-args constructor needed for Gson
	 */
	public LoginResponse()
	{
		
	}
	
	public LoginResponse(Status status, String message)
	{
	    this.status = status;
	    this.message = message;
	}
	
	public String toJson() {
    	Gson gson = new Gson();
    	String json = gson.toJson(this);
    	return json;
    }
	
    public static LoginResponse constructFromJson(String json)
    {
    	Gson gson = new Gson();
    	return gson.fromJson(json, LoginResponse.class);
    }
}
