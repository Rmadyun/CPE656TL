import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.ClientResource;

import com.google.gson.Gson;

import TrainNavigationDatabase.FilteredSearchReadOnlyRepositoryInterface;
import TrainNavigationDatabase.RepositoryEntry;
import TrainNavigationDatabase.TrackBlock;
import TrainNavigationDatabase.TrackBlockSearchCriteria;

/**
 * Restful Web client implementation of the Track Block Repository
 * 
 * @author Corey Sanders
 *
 */
public class RemoteTrackBlockRepository
		implements FilteredSearchReadOnlyRepositoryInterface<TrackBlock, TrackBlockSearchCriteria> {
	/**
	 * Represents the location where requests to the Track Block repository can
	 * be sent.
	 */
	private static final String trackBlockRepositoryPath = "/TrackBlocks";

	// Search criteria
	private static final String blockNameQueryParameter = "name";

	/**
	 * Creates the URL used for Restful web requests to a remote Rest service
	 * 
	 * @param host
	 *            Host name or IP of the target server
	 * @param port
	 *            IP port of the target server
	 * @return URL used for Restful web requests
	 */
	private static String createTrackPointRequestUrl(String host, int port) {
		StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append("http://");
		stringBuilder.append(host);
		stringBuilder.append(":");
		stringBuilder.append(port);
		stringBuilder.append(trackBlockRepositoryPath);

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
	private static String createTrackBlockRequestUrl(String host, int port, int pointId) {
		String baseUrl = createTrackPointRequestUrl(host, port);

		String finalUrl = baseUrl + "?id=" + Integer.toString(pointId);

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
	private static String createTrackBlockRequestUrl(String host, int port, TrackBlockSearchCriteria searchCriteria) {
		String baseUrl = createTrackPointRequestUrl(host, port);

		String finalUrl = baseUrl;
		String queries = "";

		if (searchCriteria != null) {
			String blockName = searchCriteria.getBlockName();
			if (blockName != null && !blockName.isEmpty()) {
				
				if(!queries.isEmpty()){
					queries+="&";
				}
				
				queries += blockNameQueryParameter + "=" + blockName;
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
	public RemoteTrackBlockRepository() {

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
	 * Extracts track block search results from a web service's response to a
	 * search request
	 * 
	 * @param connectedService
	 *            Connection object used to place a request to a target service
	 * @return Track block search results associated with the request initiated
	 *         by the connection represented by the provided connection object
	 */
	private TrackBlockSearchResults getResults(RestClientInterface connectedService) {
		TrackBlockSearchResults response = null;
		JsonRepresentation rep = connectedService.getResults();

		String jsonString;
		try {
			jsonString = rep.getText();
			Gson gson = new Gson();
			response = gson.fromJson(jsonString, TrackBlockSearchResults.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return response;
	}

	/**
	 * Converts a track block match into a track repository entry
	 * 
	 * @param trackBlockMatch
	 *            Track block match from Restful web service query to convert
	 * @return Repository Entry representation of the track block
	 */
	private RepositoryEntry<TrackBlock> convertToRepositoryEntry(TrackBlockMatch trackBlockMatch) {

		TrackBlock trackBlock = new TrackBlock(trackBlockMatch.getBlockName());
		RepositoryEntry<TrackBlock> repositoryEntry = new RepositoryEntry<TrackBlock>(trackBlockMatch.getBlockId(),
				trackBlock);

		return repositoryEntry;
	}

	@Override
	public RepositoryEntry<TrackBlock> find(String id) {

		String requestUrl = createTrackBlockRequestUrl(hostName, port, Integer.parseInt(id));

		RestClientInterface restClientInterface = connectToServer(requestUrl);

		TrackBlockSearchResults results = getResults(restClientInterface);
		RepositoryEntry<TrackBlock> match = null;
		
		if(results.getMatches().size() > 0){
			
			match = convertToRepositoryEntry(results.getMatches().get(0));
		}

		return match;
	}

	@Override
	public List<RepositoryEntry<TrackBlock>> findAll() {
		
		return find(new TrackBlockSearchCriteria());
	}

	@Override
	public List<RepositoryEntry<TrackBlock>> find(TrackBlockSearchCriteria searchCriteria) {
		String requestUrl = createTrackBlockRequestUrl(hostName, port, searchCriteria);

		RestClientInterface restClientInterface = connectToServer(requestUrl);

		TrackBlockSearchResults results = getResults(restClientInterface);
		List<RepositoryEntry<TrackBlock>> matches = new ArrayList<RepositoryEntry<TrackBlock>>();

        for(TrackBlockMatch trackBlockMatch : results.getMatches()){
        	matches.add(convertToRepositoryEntry(trackBlockMatch));
        }

		return matches;
	}

}
