package edu.uah.cpe.traintrax;
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

}
