package com.traintrax.navigation.database.rest.client;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.Get;

/**
 * Generic interface for a REST API client
 * for the Train Navigation database repositories
 * @author Corey Sanders
 *
 */
public interface RestClientInterface {
	
	/**
	 * Retrieves the JSON representation of
	 * search results
	 * @return JSON representation of search results.
	 */
	@Get
	JsonRepresentation getResults();

}
