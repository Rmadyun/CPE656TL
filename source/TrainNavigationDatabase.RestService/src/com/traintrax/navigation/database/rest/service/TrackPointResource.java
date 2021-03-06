package com.traintrax.navigation.database.rest.service;
import java.util.ArrayList;
import java.util.List;

import org.restlet.data.Form;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import com.traintrax.navigation.database.library.FilteredSearchRepositoryInterface;
import com.traintrax.navigation.database.library.RepositoryEntry;
import com.traintrax.navigation.database.library.TrackPoint;
import com.traintrax.navigation.database.library.TrackPointSearchCriteria;
import com.traintrax.navigation.database.rest.data.TrackPointSearchResults;

/**
 * Class is responsible for providing access to Track Point information
 * 
 * @author Corey Sanders
 *
 */
public class TrackPointResource extends ServerResource {

	// Search criteria
	private static final String idQueryParameter = "id";
	private static final String nameQueryParameter = "name";
	private static final String typeQueryParameter = "type";
	private static final String blockIdQueryParameter = "block_id";
	private static final String tagNameQueryParameter = "tag_name";

	private List<RepositoryEntry<TrackPoint>> findPoints(TrackPointSearchCriteria searchCriteria) {
		FilteredSearchRepositoryInterface<TrackPoint, TrackPointSearchCriteria> trackPointRepository;
		trackPointRepository = TrainDatabaseRepositoryFactory.getInstance().createTrackPointRepository();
		List<RepositoryEntry<TrackPoint>> matches;

		matches = trackPointRepository.find(searchCriteria);

		return matches;
	}

	private List<RepositoryEntry<TrackPoint>> findPoints(String id) {
		FilteredSearchRepositoryInterface<TrackPoint, TrackPointSearchCriteria> trackPointRepository;
		trackPointRepository = TrainDatabaseRepositoryFactory.getInstance().createTrackPointRepository();
		List<RepositoryEntry<TrackPoint>> matches = new ArrayList<RepositoryEntry<TrackPoint>>();

		RepositoryEntry<TrackPoint> entry = trackPointRepository.find(id);

		matches.add(entry);

		return matches;
	}

	@Get
	public Representation toJson() throws ResourceException, Exception {
		Representation jsonRepresentation = null;

		try {
			// Extract parameters from the request
			Form query = getQuery();

			String idQuery = query.getValues(idQueryParameter);
			String nameQuery = query.getValues(nameQueryParameter);
			String typeQuery = query.getValues(typeQueryParameter);
			String blockIdQuery = query.getValues(blockIdQueryParameter);
			String tagNameQuery = query.getValues(tagNameQueryParameter);
			List<RepositoryEntry<TrackPoint>> matches;

			if (idQuery != null && !idQuery.isEmpty()) {
				matches = findPoints(idQuery);
			} else {

				TrackPointSearchCriteria searchCriteria = new TrackPointSearchCriteria();

				searchCriteria.setName(nameQuery);
				searchCriteria.setType(typeQuery);
				searchCriteria.setBlockId(blockIdQuery);
				searchCriteria.setTagName(tagNameQuery);

				// Grab values

				matches = findPoints(searchCriteria);
			}

			TrackPointSearchResults trackPointMatches = new TrackPointSearchResults(matches);
			jsonRepresentation = new JsonRepresentation(trackPointMatches);

		} catch (Exception e) {
			throw e;
		}

		return jsonRepresentation;
	}

}
