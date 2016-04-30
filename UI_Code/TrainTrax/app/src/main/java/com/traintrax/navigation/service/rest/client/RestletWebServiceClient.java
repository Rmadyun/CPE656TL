package com.traintrax.navigation.service.rest.client;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ClientResource;

/**
 * Restlet implementation of a client to the
 * Restful webserver
 * @author Corey Sanders
 *
 */
public class RestletWebServiceClient implements RestfulWebServiceClientInterface {

	/**
	 * Sends a Restful web request to the service
	 * 
	 * @param requestUrl
	 *            Resource to request from the web service
	 * @return connection object for the request to the service.
	 */
	private RestClientInterface connectToServer(String requestUrl) {
		ClientResource clientResource = new ClientResource(requestUrl);
		RestClientInterface restClientInterface = clientResource.wrap(RestClientInterface.class);

		return restClientInterface;
	}

	/**
	 * Extracts track point search results from a web service's response to a
	 * search request
	 * 
	 * @param connectedService
	 *            Connection object used to place a request to a target service
	 * @return Track point search results associated with the request initiated
	 *         by the connection represented by the provided connection object
	 */
	private String getResults(RestClientInterface connectedService) {
		JsonRepresentation rep = connectedService.getResults();

		String jsonString = "";
		try {
			jsonString = rep.getText();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return jsonString;
	}
	
	@Override
	public String sendRequest(String requestUrl) {
		
		RestClientInterface clientInterface = connectToServer(requestUrl);
		
		String response = getResults(clientInterface);
		
		return response;
	}

	@Override
	public void post(String requestUrl, String message) {
		
		//StringRepresentation stringRep = new StringRepresentation(message);
		
		//RestClientInterface clientInterface = connectToServer(requestUrl);
		
		//clientInterface.postResults(stringRep);

		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(requestUrl);

		try {
			// Add your data
/*			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("id", "12345"));
			nameValuePairs.add(new BasicNameValuePair("stringdata", "Hi"));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs)); */

			httppost.setEntity(new StringEntity(message));

			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);

		} catch (ClientProtocolException exception) {
			exception.printStackTrace();
		} catch (IOException exception) {
			exception.printStackTrace();
		}
		
	}

}
