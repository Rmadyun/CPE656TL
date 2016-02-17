import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.ClientResource;

import com.google.gson.Gson;

import TrainNavigationDatabase.FilteredSearchReadOnlyRepositoryInterface;
import TrainNavigationDatabase.RepositoryEntry;
import TrainNavigationDatabase.TrackSwitch;
import TrainNavigationDatabase.TrackSwitchSearchCriteria;

/**
 * Restful Web client implementation of the Track Switch Repository
 * 
 * @author Corey Sanders
 *
 */
public class RemoteTrackSwitchRepository
		implements FilteredSearchReadOnlyRepositoryInterface<TrackSwitch, TrackSwitchSearchCriteria> {
	/**
	 * Represents the location where requests to the Track Switch repository can
	 * be sent.
	 */
	private static final String trackSwitchRepositoryPath = "/TrackSwitches";

	// Search criteria
	private static final String nameQueryParameter = "name";
	private static final String pointIdQueryParameter = "point_id";
	private static final String passBlockIdQueryParameter = "pass_block_id";
	private static final String bypassBlockIdQueryParameter = "bypass_block_id";

	/**
	 * Creates the URL used for Restful web requests to a remote Rest service
	 * 
	 * @param host
	 *            Host name or IP of the target server
	 * @param port
	 *            IP port of the target server
	 * @return URL used for Restful web requests
	 */
	private static String createTrackSwitchRequestUrl(String host, int port) {
		StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append("http://");
		stringBuilder.append(host);
		stringBuilder.append(":");
		stringBuilder.append(port);
		stringBuilder.append(trackSwitchRepositoryPath);

		return stringBuilder.toString();
	}

	/**
	 * Creates the URL used for Restful web requests to a remote Rest service
	 * 
	 * @param host
	 *            Host name or IP of the target server
	 * @param port
	 *            IP port of the target server
	 * @return URL used for Restful web requests
	 */
	private static String createTrackSwitchRequestUrl(String host, int port, int switchId) {
		String baseUrl = createTrackSwitchRequestUrl(host, port);

		String finalUrl = baseUrl + "?id=" + Integer.toString(switchId);

		return finalUrl;
	}

	/**
	 * Creates the URL used for Restful web requests to a remote Rest service
	 * 
	 * @param host
	 *            Host name or IP of the target server
	 * @param port
	 *            IP port of the target server
	 * @return URL used for Restful web requests
	 */
	private static String createTrackSwitchRequestUrl(String host, int port, TrackSwitchSearchCriteria searchCriteria) {
		String baseUrl = createTrackSwitchRequestUrl(host, port);

		String finalUrl = baseUrl;
		String queries = "";

		if (searchCriteria != null) {
			String name = searchCriteria.getSwitchName();
			if (name != null && !name.isEmpty()) {
				
				if(!queries.isEmpty()){
					queries+="&";
				}
				
				queries += nameQueryParameter + "=" + name;
			}

			String pointId = searchCriteria.getPointId();
			if (pointId != null && !pointId.isEmpty()) {
				
				if(!queries.isEmpty()){
					queries+="&";
				}
				
				queries += pointIdQueryParameter + "=" + pointId;
			}

			String passBlockId = searchCriteria.getPassBlockId();
			if (passBlockId != null && !passBlockId.isEmpty()) {
				
				if(!queries.isEmpty()){
					queries+="&";
				}
				
				queries += passBlockIdQueryParameter + "=" + passBlockId;
			}

			String bypassBlockId = searchCriteria.getBypassBlockId();
			if (bypassBlockId != null && !bypassBlockId.isEmpty()) {
				
				if(!queries.isEmpty()){
					queries+="&";
				}
				
				queries += bypassBlockIdQueryParameter + "=" + bypassBlockId;
			}
		}

		if (!queries.isEmpty()) {
			finalUrl += "?" + queries;
		}

		return finalUrl;
	}

	/**
	 * Host name or IP of the target Restful web service
	 */
	private String hostName;

	/**
	 * IP port of the target Restful web service
	 */
	private int port;

	/**
	 * Constructor
	 */
	public RemoteTrackSwitchRepository() {

		hostName = "localhost";
		port = 8182;
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
	 * Extracts track switch search results from a web service's response to a
	 * search request
	 * 
	 * @param connectedService
	 *            Connection object used to place a request to a target service
	 * @return Track switch search results associated with the request initiated
	 *         by the connection represented by the provided connection object
	 */
	private TrackSwitchSearchResults getResults(RestClientInterface connectedService) {
		TrackSwitchSearchResults response = null;
		JsonRepresentation rep = connectedService.getResults();

		String jsonString;
		try {
			jsonString = rep.getText();
			Gson gson = new Gson();
			response = gson.fromJson(jsonString, TrackSwitchSearchResults.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return response;
	}

	/**
	 * Converts a track switch match into a track repository entry
	 * 
	 * @param trackSwitchMatch
	 *            Track switch match from Restful web service query to convert
	 * @return Repository Entry representation of the track switch
	 */
	private RepositoryEntry<TrackSwitch> convertToRepositoryEntry(TrackSwitchMatch trackSwitchMatch) {

		TrackSwitch trackSwitch = new TrackSwitch(trackSwitchMatch.getName(), trackSwitchMatch.getPointId(),
				trackSwitchMatch.getPassBlockId(), trackSwitchMatch.getBypassBlockId());
		RepositoryEntry<TrackSwitch> repositoryEntry = new RepositoryEntry<TrackSwitch>(trackSwitchMatch.getPointId(),
				trackSwitch);

		return repositoryEntry;
	}

	@Override
	public RepositoryEntry<TrackSwitch> find(String id) {

		String requestUrl = createTrackSwitchRequestUrl(hostName, port, Integer.parseInt(id));

		RestClientInterface restClientInterface = connectToServer(requestUrl);

		TrackSwitchSearchResults results = getResults(restClientInterface);
		RepositoryEntry<TrackSwitch> match = null;
		
		if(results.getMatches().size() > 0){
			
			match = convertToRepositoryEntry(results.getMatches().get(0));
		}

		return match;
	}

	@Override
	public List<RepositoryEntry<TrackSwitch>> findAll() {
		
		return find(new TrackSwitchSearchCriteria());
	}

	@Override
	public List<RepositoryEntry<TrackSwitch>> find(TrackSwitchSearchCriteria searchCriteria) {
		String requestUrl = createTrackSwitchRequestUrl(hostName, port, searchCriteria);

		RestClientInterface restClientInterface = connectToServer(requestUrl);

		TrackSwitchSearchResults results = getResults(restClientInterface);
		List<RepositoryEntry<TrackSwitch>> matches = new ArrayList<RepositoryEntry<TrackSwitch>>();

        for(TrackSwitchMatch trackSwitchMatch : results.getMatches()){
        	matches.add(convertToRepositoryEntry(trackSwitchMatch));
        }

		return matches;
	}

}
