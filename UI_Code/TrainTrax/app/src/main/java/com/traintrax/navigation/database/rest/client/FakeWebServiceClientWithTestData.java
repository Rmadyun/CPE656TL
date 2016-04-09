package com.traintrax.navigation.database.rest.client;
/**
 * Fake implementation of a web service client to use for testing
 * @author Corey Sanders
 *
 */
public class FakeWebServiceClientWithTestData implements RestfulWebServiceClientInterface {
	
	private final String defaultResponseString;

	/**
	 * Constructor
	 * @param defaultResponseString String to return for each request made.
	 */
	public FakeWebServiceClientWithTestData(String defaultResponseString){
		this.defaultResponseString = defaultResponseString;
	}

	@Override
	public String sendRequest(String requestUrl) {
		
		return defaultResponseString;
	}

}
