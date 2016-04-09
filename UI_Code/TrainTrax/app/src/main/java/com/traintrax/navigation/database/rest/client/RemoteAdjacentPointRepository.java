package com.traintrax.navigation.database.rest.client;
import java.util.ArrayList;
import java.util.List;

import com.traintrax.navigation.database.library.AdjacentPoint;
import com.traintrax.navigation.database.library.AdjacentPointSearchCriteria;
import com.traintrax.navigation.database.library.FilteredSearchReadOnlyRepositoryInterface;
import com.traintrax.navigation.database.library.RepositoryEntry;
import com.traintrax.navigation.database.rest.data.AdjacentPointMatch;
import com.traintrax.navigation.database.rest.data.AdjacentPointSearchResults;

/**
 * Restful Web client implementation of the Adjacent Point Repository
 * 
 * @author Corey Sanders
 *
 */
public class RemoteAdjacentPointRepository
		implements FilteredSearchReadOnlyRepositoryInterface<AdjacentPoint, AdjacentPointSearchCriteria> {
	
	//Configuration Defaults
	/**
	 * The Default Host Name to contact a local instance of the
	 * repository
	 */
	public static final String DefaultHostName = "localhost";
	
	/**
	 * Default network port to use to contact a repository
	 */
	public static int DefaultPort = 8182;
	
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

	private final RestfulWebServiceClientInterface webServiceClient;

	private final MessageDeserializerInterface<AdjacentPointSearchResults> messageDeserializer;


	/**
	 * Host name or IP of the target Restful web service
	 */
	private String hostName;

	/**
	 * IP port of the target Restful web service
	 */
	private int port;

	/**
	 * Default constructor.
	 * Defaulting to use Restlet as the web service client and JSON for the
	 * serialization format.
	 */
	public RemoteAdjacentPointRepository() {
	    this(new RestletWebServiceClient(), new JsonRepositoryMessageDeserializer<AdjacentPointSearchResults>(AdjacentPointSearchResults.class));
	}

	/**
	 * Constructor.
	 * Defaulting to use Restlet as the web service client and JSON for the
	 * serialization format.
	 * @param hostName Network name of the machine hosting the target repository
	 * @param port Network port associated with the target repository
	 */
	public RemoteAdjacentPointRepository(String hostName, int port) {
	    this(hostName, port, new RestletWebServiceClient(), new JsonRepositoryMessageDeserializer<AdjacentPointSearchResults>(AdjacentPointSearchResults.class));
	}
	
	/**
	 * Constructor
	 */
	public RemoteAdjacentPointRepository(String hostName, int port, RestfulWebServiceClientInterface webServiceClient, MessageDeserializerInterface<AdjacentPointSearchResults> messageDeserializer) {

		this.hostName = hostName;
		this.port = port;
		this.webServiceClient = webServiceClient;
		this.messageDeserializer = messageDeserializer;
	}
	
	/**
	 * Constructor
	 */
	public RemoteAdjacentPointRepository(RestfulWebServiceClientInterface webServiceClient, MessageDeserializerInterface<AdjacentPointSearchResults> messageDeserializer) {

		this.hostName = DefaultHostName;
		this.port = DefaultPort;
		this.webServiceClient = webServiceClient;
		this.messageDeserializer = messageDeserializer;
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
		
		RepositoryEntry<AdjacentPoint> match = null;
		String response = webServiceClient.sendRequest(requestUrl);
		AdjacentPointSearchResults results = messageDeserializer.deserialize(response);

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
		String response = webServiceClient.sendRequest(requestUrl);
		AdjacentPointSearchResults results = messageDeserializer.deserialize(response);


		List<RepositoryEntry<AdjacentPoint>> matches = new ArrayList<RepositoryEntry<AdjacentPoint>>();

        for(AdjacentPointMatch adjacentPointMatch : results.getMatches()){
        	matches.add(convertToRepositoryEntry(adjacentPointMatch));
        }

		return matches;
	}

}
