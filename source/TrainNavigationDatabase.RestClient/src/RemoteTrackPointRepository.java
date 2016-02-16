import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.ClientResource;

import com.google.gson.Gson;

import TrainNavigationDatabase.FilteredSearchReadOnlyRepositoryInterface;
import TrainNavigationDatabase.RepositoryEntry;
import TrainNavigationDatabase.TrackPoint;
import TrainNavigationDatabase.TrackPointSearchCriteria;

/**
 * Restful Web client implementation of the Track Point Repository
 * 
 * @author Corey Sanders
 *
 */
public class RemoteTrackPointRepository
		implements FilteredSearchReadOnlyRepositoryInterface<TrackPoint, TrackPointSearchCriteria> {
	/**
	 * Represents the location where requests to the Track Point repository can
	 * be sent.
	 */
	private static final String trackPointRepositoryPath = "/TrackPoints";

	// Search criteria
	private static final String nameQueryParameter = "name";
	private static final String typeQueryParameter = "type";
	private static final String blockIdQueryParameter = "block_id";
	private static final String tagNameQueryParameter = "tag_name";

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
		stringBuilder.append(trackPointRepositoryPath);

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
	private static String createTrackPointRequestUrl(String host, int port, int pointId) {
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
	private static String createTrackPointRequestUrl(String host, int port, TrackPointSearchCriteria searchCriteria) {
		String baseUrl = createTrackPointRequestUrl(host, port);

		String finalUrl = baseUrl;
		String queries = "";

		if (searchCriteria != null) {
			String name = searchCriteria.getName();
			if (name != null && !name.isEmpty()) {
				
				if(!queries.isEmpty()){
					queries+="&";
				}
				
				queries += nameQueryParameter + "=" + name;
			}

			String type = searchCriteria.getType();
			if (type != null && !type.isEmpty()) {
				
				if(!queries.isEmpty()){
					queries+="&";
				}
				
				queries += typeQueryParameter + "=" + type;
			}

			String blockId = searchCriteria.getBlockId();
			if (blockId != null && !blockId.isEmpty()) {
				
				if(!queries.isEmpty()){
					queries+="&";
				}
				
				queries += blockIdQueryParameter + "=" + blockId;
			}

			String tagName = searchCriteria.getTagName();
			if (tagName != null && !tagName.isEmpty()) {
				
				if(!queries.isEmpty()){
					queries+="&";
				}
				
				queries += tagNameQueryParameter + "=" + tagName;
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
	public RemoteTrackPointRepository() {

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
	 * Extracts track point search results from a web service's response to a
	 * search request
	 * 
	 * @param connectedService
	 *            Connection object used to place a request to a target service
	 * @return Track point search results associated with the request initiated
	 *         by the connection represented by the provided connection object
	 */
	private TrackPointSearchResults getResults(RestClientInterface connectedService) {
		TrackPointSearchResults response = null;
		JsonRepresentation rep = connectedService.getResults();

		String jsonString;
		try {
			jsonString = rep.getText();
			Gson gson = new Gson();
			response = gson.fromJson(jsonString, TrackPointSearchResults.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return response;
	}

	/**
	 * Converts a track point match into a track repository entry
	 * 
	 * @param trackPointMatch
	 *            Track point match from Restful web service query to convert
	 * @return Repository Entry representation of the track point
	 */
	private RepositoryEntry<TrackPoint> convertToRepositoryEntry(TrackPointMatch trackPointMatch) {

		TrackPoint trackPoint = new TrackPoint(trackPointMatch.getPointName(), trackPointMatch.getType(),
				trackPointMatch.getX(), trackPointMatch.getY(), trackPointMatch.getZ(), trackPointMatch.getBlockId(),
				trackPointMatch.getTagName());
		RepositoryEntry<TrackPoint> repositoryEntry = new RepositoryEntry<TrackPoint>(trackPointMatch.getPointId(),
				trackPoint);

		return repositoryEntry;
	}

	@Override
	public RepositoryEntry<TrackPoint> find(String id) {

		String requestUrl = createTrackPointRequestUrl(hostName, port, Integer.parseInt(id));

		RestClientInterface restClientInterface = connectToServer(requestUrl);

		TrackPointSearchResults results = getResults(restClientInterface);
		RepositoryEntry<TrackPoint> match = null;
		
		if(results.getMatches().size() > 0){
			
			match = convertToRepositoryEntry(results.getMatches().get(0));
		}

		return match;
	}

	@Override
	public List<RepositoryEntry<TrackPoint>> findAll() {
		
		return find(new TrackPointSearchCriteria());
	}

	@Override
	public List<RepositoryEntry<TrackPoint>> find(TrackPointSearchCriteria searchCriteria) {
		String requestUrl = createTrackPointRequestUrl(hostName, port, searchCriteria);

		RestClientInterface restClientInterface = connectToServer(requestUrl);

		TrackPointSearchResults results = getResults(restClientInterface);
		List<RepositoryEntry<TrackPoint>> matches = new ArrayList<RepositoryEntry<TrackPoint>>();

        for(TrackPointMatch trackPointMatch : results.getMatches()){
        	matches.add(convertToRepositoryEntry(trackPointMatch));
        }

		return matches;
	}

}
