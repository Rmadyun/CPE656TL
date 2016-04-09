package com.traintrax.navigation.database.rest.client;
import java.util.ArrayList;
import java.util.List;

import com.traintrax.navigation.database.library.FilteredSearchReadOnlyRepositoryInterface;
import com.traintrax.navigation.database.library.RepositoryEntry;
import com.traintrax.navigation.database.library.TrackSwitch;
import com.traintrax.navigation.database.library.TrackSwitchSearchCriteria;
import com.traintrax.navigation.database.rest.data.TrackSwitchMatch;
import com.traintrax.navigation.database.rest.data.TrackSwitchSearchResults;

/**
 * Restful Web client implementation of the Track Switch Repository
 * 
 * @author Corey Sanders
 *
 */
public class RemoteTrackSwitchRepository
		implements FilteredSearchReadOnlyRepositoryInterface<TrackSwitch, TrackSwitchSearchCriteria> {
	
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
	
	private final RestfulWebServiceClientInterface webServiceClient;

	private final MessageDeserializerInterface<TrackSwitchSearchResults> messageDeserializer;

	/**
	 * Default constructor.
	 * Defaulting to use Restlet as the web service client and JSON for the
	 * serialization format.
	 */
	public RemoteTrackSwitchRepository() {
	    this(new RestletWebServiceClient(), new JsonRepositoryMessageDeserializer<TrackSwitchSearchResults>(TrackSwitchSearchResults.class));
	}

	/**
	 * Constructor.
	 * Defaulting to use Restlet as the web service client and JSON for the
	 * serialization format.
	 * @param hostName Network name of the machine hosting the target repository
	 * @param port Network port associated with the target repository
	 */
	public RemoteTrackSwitchRepository(String hostName, int port) {
	    this(hostName, port, new RestletWebServiceClient(), new JsonRepositoryMessageDeserializer<TrackSwitchSearchResults>(TrackSwitchSearchResults.class));
	}
	
	/**
	 * Constructor
	 */
	public RemoteTrackSwitchRepository(String hostName, int port, RestfulWebServiceClientInterface webServiceClient, MessageDeserializerInterface<TrackSwitchSearchResults> messageDeserializer) {

		this.hostName = hostName;
		this.port = port;
		this.webServiceClient = webServiceClient;
		this.messageDeserializer = messageDeserializer;
	}
	
	/**
	 * Constructor
	 */
	public RemoteTrackSwitchRepository(RestfulWebServiceClientInterface webServiceClient, MessageDeserializerInterface<TrackSwitchSearchResults> messageDeserializer) {

		this.hostName = DefaultHostName;
		this.port = DefaultPort;
		this.webServiceClient = webServiceClient;
		this.messageDeserializer = messageDeserializer;
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

		String response = webServiceClient.sendRequest(requestUrl);

		TrackSwitchSearchResults results = messageDeserializer.deserialize(response);
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

		String response = webServiceClient.sendRequest(requestUrl);

		TrackSwitchSearchResults results = messageDeserializer.deserialize(response);
		List<RepositoryEntry<TrackSwitch>> matches = new ArrayList<RepositoryEntry<TrackSwitch>>();

        for(TrackSwitchMatch trackSwitchMatch : results.getMatches()){
        	matches.add(convertToRepositoryEntry(trackSwitchMatch));
        }

		return matches;
	}

}
