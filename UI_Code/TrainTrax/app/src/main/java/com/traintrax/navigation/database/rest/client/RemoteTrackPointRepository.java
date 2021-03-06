package com.traintrax.navigation.database.rest.client;
import java.util.ArrayList;
import java.util.List;

import com.traintrax.navigation.database.library.FilteredSearchReadOnlyRepositoryInterface;
import com.traintrax.navigation.database.library.RepositoryEntry;
import com.traintrax.navigation.database.library.TrackPoint;
import com.traintrax.navigation.database.library.TrackPointSearchCriteria;
import com.traintrax.navigation.database.rest.data.TrackPointMatch;
import com.traintrax.navigation.database.rest.data.TrackPointSearchResults;

/**
 * Restful Web client implementation of the Track Point Repository
 * 
 * @author Corey Sanders
 *
 */
public class RemoteTrackPointRepository
		implements FilteredSearchReadOnlyRepositoryInterface<TrackPoint, TrackPointSearchCriteria> {
	
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

	private final RestfulWebServiceClientInterface webServiceClient;

	private final MessageDeserializerInterface<TrackPointSearchResults> messageDeserializer;
	
	/**
	 * Default constructor.
	 * Defaulting to use Restlet as the web service client and JSON for the
	 * serialization format.
	 */
	public RemoteTrackPointRepository() {
	    this(new RestletWebServiceClient(), new JsonRepositoryMessageDeserializer<TrackPointSearchResults>(TrackPointSearchResults.class));
	}

	/**
	 * Constructor.
	 * Defaulting to use Restlet as the web service client and JSON for the
	 * serialization format.
	 * @param hostName Network name of the machine hosting the target repository
	 * @param port Network port associated with the target repository
	 */
	public RemoteTrackPointRepository(String hostName, int port) {
	    this(hostName, port, new RestletWebServiceClient(), new JsonRepositoryMessageDeserializer<TrackPointSearchResults>(TrackPointSearchResults.class));
	}
	
	/**
	 * Constructor
	 */
	public RemoteTrackPointRepository(String hostName, int port, RestfulWebServiceClientInterface webServiceClient, MessageDeserializerInterface<TrackPointSearchResults> messageDeserializer) {

		this.hostName = hostName;
		this.port = port;
		this.webServiceClient = webServiceClient;
		this.messageDeserializer = messageDeserializer;
	}
	
	/**
	 * Constructor
	 */
	public RemoteTrackPointRepository(RestfulWebServiceClientInterface webServiceClient, MessageDeserializerInterface<TrackPointSearchResults> messageDeserializer) {

		this.hostName = DefaultHostName;
		this.port = DefaultPort;
		this.webServiceClient = webServiceClient;
		this.messageDeserializer = messageDeserializer;
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

		String response = webServiceClient.sendRequest(requestUrl);

		TrackPointSearchResults results = messageDeserializer.deserialize(response);
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

		String response = webServiceClient.sendRequest(requestUrl);

		TrackPointSearchResults results = messageDeserializer.deserialize(response);
		List<RepositoryEntry<TrackPoint>> matches = new ArrayList<RepositoryEntry<TrackPoint>>();

        for(TrackPointMatch trackPointMatch : results.getMatches()){
        	matches.add(convertToRepositoryEntry(trackPointMatch));
        }

		return matches;
	}

}
