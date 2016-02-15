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
import TrainNavigationDatabase.TrackSwitch;
import TrainNavigationDatabase.TrackSwitchSearchCriteria;

/**
 * Class is responsible for providing access to Track Point information
 * 
 * @author Corey Sanders
 *
 */
public class TrackSwitchResource extends ServerResource {

	// Search criteria
	private static final String nameQueryParameter = "name";
	private static final String pointIdQueryParame = "point_id";
	private static final String passBlockIdQueryParameter = "pass_block_id";
	private static final String bypassBlockIdQueryParameter = "bypass_block_id";

	//@Get
	public String toString() {
		return "hello, world";
	}
	
	private List<RepositoryEntry<TrackSwitch>> findPoints(TrackSwitchSearchCriteria searchCriteria)
	{
		FilteredSearchRepositoryInterface<TrackSwitch, TrackSwitchSearchCriteria> trackSwitchRepository;
		trackSwitchRepository = TrainDatabaseRepositoryFactory.getInstance().createTrackSwitchRepository();
		List<RepositoryEntry<TrackSwitch>> matches;
		
		matches = trackSwitchRepository.find(searchCriteria);
		
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
			String pointIdQuery = query.getValues(pointIdQueryParame);
			String passBlockIdQuery = query.getValues(passBlockIdQueryParameter);
			String bypassBlockIdQuery = query.getValues(bypassBlockIdQueryParameter);
			
			TrackSwitchSearchCriteria searchCriteria = new TrackSwitchSearchCriteria();
			
			searchCriteria.setSwitchName(nameQuery);
			searchCriteria.setPointId(pointIdQuery);
			searchCriteria.setPassBlockId(passBlockIdQuery);
			searchCriteria.setBypassBlockId(bypassBlockIdQuery);
			
			//Grab values
			
			List<RepositoryEntry<TrackSwitch>> matches = findPoints(searchCriteria);
			
			TrackSwitchSearchResults trackSwitchMatches = new TrackSwitchSearchResults(matches);
			jsonRepresentation = new JsonRepresentation(trackSwitchMatches);
			
		} catch (Exception e) {
			throw e;
		}

		return jsonRepresentation;
	}

}
