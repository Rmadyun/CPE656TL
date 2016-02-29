package edu.uah.cpe.traintrax;
import java.util.ArrayList;
import java.util.List;

/* import com.traintrax.navigation.database.library.FilteredSearchReadOnlyRepositoryInterface;
import com.traintrax.navigation.database.library.RepositoryEntry;
import com.traintrax.navigation.database.library.TrackBlock;
import com.traintrax.navigation.database.library.TrackBlockSearchCriteria;
import com.traintrax.navigation.database.rest.data.TrackBlockMatch;
import com.traintrax.navigation.database.rest.data.TrackBlockSearchResults; */

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

	private final RestfulWebServiceClientInterface webServiceClient;

	private final MessageDeserializerInterface<TrackBlockSearchResults> messageDeserializer;

	/**
	 * Constructor
	 */
	public RemoteTrackBlockRepository() {
		this(new RestletWebServiceClient(), new JsonRepositoryMessageDeserializer<TrackBlockSearchResults>(TrackBlockSearchResults.class));
	}

	/**
	 * Constructor
	 */
	public RemoteTrackBlockRepository(RestfulWebServiceClientInterface webServiceClient, MessageDeserializerInterface<TrackBlockSearchResults> messageDeserializer) {

		hostName = "localhost";
		port = 8182;
		this.webServiceClient = webServiceClient;
		this.messageDeserializer = messageDeserializer;
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
		String response = webServiceClient.sendRequest(requestUrl);
		TrackBlockSearchResults results = messageDeserializer.deserialize(response);

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
		String response = webServiceClient.sendRequest(requestUrl);
		TrackBlockSearchResults results = messageDeserializer.deserialize(response);


		List<RepositoryEntry<TrackBlock>> matches = new ArrayList<RepositoryEntry<TrackBlock>>();

        for(TrackBlockMatch trackBlockMatch : results.getMatches()){
        	matches.add(convertToRepositoryEntry(trackBlockMatch));
        }

		return matches;
	}

}
