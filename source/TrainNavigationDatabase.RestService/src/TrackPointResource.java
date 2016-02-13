import java.util.List;

import org.restlet.data.Form;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import TrainNavigationDatabase.FilteredSearchRepositoryInterface;
import TrainNavigationDatabase.RepositoryEntry;
import TrainNavigationDatabase.TrackPoint;
import TrainNavigationDatabase.TrackPointSearchCriteria;

/**
 * Class is responsible for providing access to Track Point information
 * 
 * @author Corey Sanders
 *
 */
public class TrackPointResource extends ServerResource {

	// Search criteria
	private static final String nameQueryParameter = "name";
	private static final String typeQueryParameter = "type";
	private static final String blockIdQueryParameter = "block_id";
	private static final String tagNameQueryParameter = "tag_name";

	//@Get
	public String toString() {
		return "hello, world";
	}
	
	private List<RepositoryEntry<TrackPoint>> findPoints(TrackPointSearchCriteria searchCriteria)
	{
		FilteredSearchRepositoryInterface<TrackPoint, TrackPointSearchCriteria> trackPointRepository;
		trackPointRepository = TrainDatabaseRepositoryFactory.getInstance().createTrackPointRepository();
		List<RepositoryEntry<TrackPoint>> matches;
		
		matches = trackPointRepository.find(searchCriteria);
		
		return matches;
	}

	//@Get("json")
	@Get
	public Representation toJson() throws ResourceException, Exception {
		Representation jsonRepresentation = null;
		
		try {
			// Extract parameters from the request
			Form query = getQuery();
			String nameQuery = query.getValues(nameQueryParameter);
			String typeQuery = query.getValues(typeQueryParameter);
			String blockIdQuery = query.getValues(blockIdQueryParameter);
			String tagNameQuery = query.getValues(tagNameQueryParameter);
			
			TrackPointSearchCriteria searchCriteria = new TrackPointSearchCriteria();
			
			searchCriteria.setName(nameQuery);
			searchCriteria.setType(typeQuery);
			searchCriteria.setBlockId(blockIdQuery);
			searchCriteria.setTagName(tagNameQuery);
			
			//Grab values
			
			List<RepositoryEntry<TrackPoint>> matches = findPoints(searchCriteria);
			
			TrackPointMatches trackPointMatches = new TrackPointMatches(matches);
			jsonRepresentation = new JsonRepresentation(trackPointMatches);
			
		} catch (Exception e) {
			throw e;
		}

		return jsonRepresentation;
	}

}
