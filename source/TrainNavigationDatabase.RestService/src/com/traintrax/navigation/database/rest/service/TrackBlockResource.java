package com.traintrax.navigation.database.rest.service;
import java.util.List;

import org.restlet.data.Form;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import com.traintrax.navigation.database.library.FilteredSearchRepositoryInterface;
import com.traintrax.navigation.database.library.RepositoryEntry;
import com.traintrax.navigation.database.library.TrackBlock;
import com.traintrax.navigation.database.library.TrackBlockSearchCriteria;
import com.traintrax.navigation.database.rest.data.TrackBlockSearchResults;

/**
 * Class is responsible for providing access to Track Block information
 * 
 * @author Corey Sanders
 *
 */
public class TrackBlockResource extends ServerResource {

	// Search criteria
	private static final String blockNameQueryParameter = "name";
	
	private List<RepositoryEntry<TrackBlock>> findPoints(TrackBlockSearchCriteria searchCriteria)
	{
		FilteredSearchRepositoryInterface<TrackBlock, TrackBlockSearchCriteria> trackBlockRepository;
		trackBlockRepository = TrainDatabaseRepositoryFactory.getInstance().createTrackBlockRepository();
		List<RepositoryEntry<TrackBlock>> matches;
		
		matches = trackBlockRepository.find(searchCriteria);
		
		return matches;
	}

	@Get
	public Representation toJson() throws ResourceException, Exception {
		Representation jsonRepresentation = null;
		
		try {
			// Extract parameters from the request
			Form query = getQuery();
			String blockNameQuery = query.getValues(blockNameQueryParameter);
			
			TrackBlockSearchCriteria searchCriteria = new TrackBlockSearchCriteria();
			
			searchCriteria.setBlockName(blockNameQuery);
			
			//Grab values
			
			List<RepositoryEntry<TrackBlock>> matches = findPoints(searchCriteria);
			
			TrackBlockSearchResults trackBlockMatches = new TrackBlockSearchResults(matches);
			jsonRepresentation = new JsonRepresentation(trackBlockMatches);
			
		} catch (Exception e) {
			throw e;
		}

		return jsonRepresentation;
	}

}
