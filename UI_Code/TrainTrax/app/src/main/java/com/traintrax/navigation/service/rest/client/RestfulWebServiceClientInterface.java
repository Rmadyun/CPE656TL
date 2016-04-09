package com.traintrax.navigation.service.rest.client;
/**
 * Interfaces used to contact a web server
 * @author Corey Sanders
 *
 */
public interface RestfulWebServiceClientInterface {
	
	/**
	 * Send request to server
	 * @param requestUrl URL describing the information requested
	 * @return Serialized response to the request.
	 */
	String sendRequest(String requestUrl);
	
	/**
	 * Submit information to the server
	 * @param requestUrl URL describing the information requested 
	 * @param message Serialized version of the message.
	 */
	void post(String requestUrl, String message);

}
