package com.traintrax.navigation.database.rest.service;
import java.util.List;

import org.restlet.data.Form;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import com.traintrax.navigation.database.library.AdjacentPoint;
import com.traintrax.navigation.database.library.AdjacentPointSearchCriteria;
import com.traintrax.navigation.database.library.FilteredSearchRepositoryInterface;
import com.traintrax.navigation.database.library.RepositoryEntry;
import com.traintrax.navigation.database.rest.data.AdjacentPointSearchResults;

/**
 * Class is responsible for providing access to Track Point information
 * 
 * @author Corey Sanders
 *
 */
public class AdjacentPointResource extends ServerResource {

	// Search criteria
	private static final String pointIdQueryParameter = "point_id";
	private static final String adjacentPointIdQueryParameter = "adjacent_point_id";
	
	private List<RepositoryEntry<AdjacentPoint>> findPoints(AdjacentPointSearchCriteria searchCriteria)
	{
		FilteredSearchRepositoryInterface<AdjacentPoint, AdjacentPointSearchCriteria> adjacentPointRepository;
		adjacentPointRepository = TrainDatabaseRepositoryFactory.getInstance().createAdjacentPointRepository();
		List<RepositoryEntry<AdjacentPoint>> matches;
		
		matches = adjacentPointRepository.find(searchCriteria);
		
		return matches;
	}

	@Get
	public Representation toJson() throws ResourceException, Exception {
		Representation jsonRepresentation = null;
		
		try {
			// Extract parameters from the request
			Form query = getQuery();
			String pointIdQuery = query.getValues(pointIdQueryParameter);
			String adjacentPointIdQuery = query.getValues(adjacentPointIdQueryParameter);
			
			AdjacentPointSearchCriteria searchCriteria = new AdjacentPointSearchCriteria();
			
			searchCriteria.setPointId(pointIdQuery);
			searchCriteria.setAdjacentPointId(adjacentPointIdQuery);
			
			//Grab values
			
			List<RepositoryEntry<AdjacentPoint>> matches = findPoints(searchCriteria);
			
			AdjacentPointSearchResults adjacentPointMatches = new AdjacentPointSearchResults(matches);
			jsonRepresentation = new JsonRepresentation(adjacentPointMatches);
			
		} catch (Exception e) {
			throw e;
		}

		return jsonRepresentation;
	}

}
