package com.traintrax.navigation.database.rest.client;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.restlet.engine.Engine;

import org.restlet.engine.connector.HttpClientHelper;
import org.restlet.ext.jackson.JacksonConverter;
import org.restlet.ext.json.JsonConverter;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.ClientResource;

/**
 * Restlet implementation of a client to the
 * Restful webserver
 * @author Corey Sanders
 *
 */
public class RestletWebServiceClient implements RestfulWebServiceClientInterface {
	
	/**
	 * Default Constructor
	 */
	public RestletWebServiceClient(){
		
		//Make sure that on Android the system is configured
		//to handle serialization/deserialization and HTTP requests.
		
		Engine.getInstance().getRegisteredConverters().clear();
		Engine.getInstance().getRegisteredConverters().add(new JacksonConverter());
		Engine.getInstance().getRegisteredConverters().add(new JsonConverter());
		Engine.getInstance().getRegisteredClients().clear();
		Engine.getInstance().getRegisteredClients().add(new HttpClientHelper(null));
	}
	

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
		/*String response = "";

		//NOTE: UrlConnection is needed for 6.0 (AP23) and up.

		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httppost = new HttpGet(requestUrl);

		try {
			HttpResponse result = httpclient.execute(httppost);

			HttpEntity resultEntity = result.getEntity();

			response = resultEntity.getContent().toString();
		} catch (ClientProtocolException e) {

		} catch (IOException e) {

		}*/
		
		return response;
	}

}
