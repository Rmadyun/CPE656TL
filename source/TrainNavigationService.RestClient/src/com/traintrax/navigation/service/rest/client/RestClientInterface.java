package com.traintrax.navigation.service.rest.client;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;

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
	
	/**
	 * Posts the Generic representation of results
	 * @param representation Encoding of the results
	 */
	@Post
	void postResults(Representation representation);

}
