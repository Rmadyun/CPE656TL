import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.ClientResource;

import com.google.gson.Gson;

import TrainNavigationDatabase.AdjacentPoint;
import TrainNavigationDatabase.AdjacentPointSearchCriteria;
import TrainNavigationDatabase.FilteredSearchReadOnlyRepositoryInterface;
import TrainNavigationDatabase.RepositoryEntry;

/**
 * Restful Web client implementation of the Adjacent Point Repository
 * 
 * @author Corey Sanders
 *
 */
public class RemoteAdjacentPointRepository
		implements FilteredSearchReadOnlyRepositoryInterface<AdjacentPoint, AdjacentPointSearchCriteria> {
	/**
	 * Represents the location where requests to the Adjacent Point repository can
	 * be sent.
	 */
	private static final String adjacentPointRepositoryPath = "/AdjacentPoints";

	// Search criteria
	private static final String pointIdQueryParameter = "point_id";
	private static final String adjacentPointIdQueryParameter = "adjacent_point_id";

	/**
	 * Creates the URL used for Restful web requests to a remote Rest service
	 * 
	 * @param host
	 *            Host name or IP of the target server
	 * @param port
	 *            IP port of the target server
	 * @return URL used for Restful web requests
	 */
	private static String createAdjacentPointRequestUrl(String host, int port) {
		StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append("http://");
		stringBuilder.append(host);
		stringBuilder.append(":");
		stringBuilder.append(port);
		stringBuilder.append(adjacentPointRepositoryPath);

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
	private static String createAdjacentPointRequestUrl(String host, int port, int pointId) {
		String baseUrl = createAdjacentPointRequestUrl(host, port);

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
	private static String createAdjacentPointRequestUrl(String host, int port, AdjacentPointSearchCriteria searchCriteria) {
		String baseUrl = createAdjacentPointRequestUrl(host, port);

		String finalUrl = baseUrl;
		String queries = "";

		if (searchCriteria != null) {
			String pointId = searchCriteria.getPointId();
			if (pointId != null && !pointId.isEmpty()) {
				
				if(!queries.isEmpty()){
					queries+="&";
				}
				
				queries += pointIdQueryParameter + "=" + pointId;
			}

			String adjacentPointId = searchCriteria.getAdjacentPointId();
			if (adjacentPointId != null && !adjacentPointId.isEmpty()) {
				
				if(!queries.isEmpty()){
					queries+="&";
				}
				
				queries += adjacentPointIdQueryParameter + "=" + adjacentPointId;
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
	public RemoteAdjacentPointRepository() {

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
	 * Extracts adjacent point search results from a web service's response to a
	 * search request
	 * 
	 * @param connectedService
	 *            Connection object used to place a request to a target service
	 * @return Adjacent point search results associated with the request initiated
	 *         by the connection represented by the provided connection object
	 */
	private AdjacentPointSearchResults getResults(RestClientInterface connectedService) {
		AdjacentPointSearchResults response = null;
		JsonRepresentation rep = connectedService.getResults();

		String jsonString;
		try {
			jsonString = rep.getText();
			Gson gson = new Gson();
			response = gson.fromJson(jsonString, AdjacentPointSearchResults.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return response;
	}

	/**
	 * Converts an adjacent point match into a track repository entry
	 * 
	 * @param adjacentPointMatch
	 *            Adjacent point match from Restful web service query to convert
	 * @return Repository Entry representation of the adjacent point
	 */
	private RepositoryEntry<AdjacentPoint> convertToRepositoryEntry(AdjacentPointMatch adjacentPointMatch) {

		AdjacentPoint adjacentPoint = new AdjacentPoint(
				Integer.parseInt(adjacentPointMatch.getPointId()),
				Integer.parseInt(adjacentPointMatch.getAdjacentPointId()));
		RepositoryEntry<AdjacentPoint> repositoryEntry = new RepositoryEntry<AdjacentPoint>(adjacentPointMatch.getPointId(),
				adjacentPoint);

		return repositoryEntry;
	}

	@Override
	public RepositoryEntry<AdjacentPoint> find(String id) {

		String requestUrl = createAdjacentPointRequestUrl(hostName, port, Integer.parseInt(id));

		RestClientInterface restClientInterface = connectToServer(requestUrl);

		AdjacentPointSearchResults results = getResults(restClientInterface);
		RepositoryEntry<AdjacentPoint> match = null;
		
		if(results.getMatches().size() > 0){
			
			match = convertToRepositoryEntry(results.getMatches().get(0));
		}

		return match;
	}

	@Override
	public List<RepositoryEntry<AdjacentPoint>> findAll() {
		
		return find(new AdjacentPointSearchCriteria());
	}

	@Override
	public List<RepositoryEntry<AdjacentPoint>> find(AdjacentPointSearchCriteria searchCriteria) {
		String requestUrl = createAdjacentPointRequestUrl(hostName, port, searchCriteria);

		RestClientInterface restClientInterface = connectToServer(requestUrl);

		AdjacentPointSearchResults results = getResults(restClientInterface);
		List<RepositoryEntry<AdjacentPoint>> matches = new ArrayList<RepositoryEntry<AdjacentPoint>>();

        for(AdjacentPointMatch adjacentPointMatch : results.getMatches()){
        	matches.add(convertToRepositoryEntry(adjacentPointMatch));
        }

		return matches;
	}

}
