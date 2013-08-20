package com.rednels.gaecorona;

import com.google.gson.Gson;

/**
 * LoginResponse class used for login success/failure
 * @author gslender
 *
 */
public class WebServiceResponse {

	public enum Status {
	
		ERROR, OK
	}

	private Status status;
	private String message;

	/**
	 * LoginResponse default no-args constructor needed for Gson
	 */
	public WebServiceResponse()
	{
		
	}
	
	public WebServiceResponse(Status status, String message)
	{
	    this.status = status;
	    this.message = message;
	}
	
	public String toJson() {
    	Gson gson = new Gson();
    	String json = gson.toJson(this);
    	return json;
    }
	
    public static WebServiceResponse constructFromJson(String json)
    {
    	Gson gson = new Gson();
    	return gson.fromJson(json, WebServiceResponse.class);
    }
}
