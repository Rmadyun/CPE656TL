package com.traintrax.navigation.service.rest.client;
import java.util.LinkedList;
import java.util.List;

import com.traintrax.navigation.service.rest.data.KnownTrainIdentifiersMessage;
import com.traintrax.navigation.service.rest.data.TrainIdentifier;

/**
 * Restful Web client to the Train Navigation Service to retrieve information
 * about the identity of known trains.
 * 
 * @author Corey Sanders
 *
 */
 class RemoteTrainIdentityService {
	/**
	 * Represents the location where requests for train identity information
	 * be sent.
	 */
	private static final String trainIdentityPath = "/TrainIdentifiers";


	/**
	 * Creates the URL used for Restful web requests to a remote Rest service
	 * 
	 * @param host
	 *            Host name or IP of the target server
	 * @param port
	 *            IP port of the target server
	 * @return URL used for Restful web requests
	 */
	private static String createTrainIdentifierRequestUrl(String host, int port) {
		StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append("http://");
		stringBuilder.append(host);
		stringBuilder.append(":");
		stringBuilder.append(port);
		stringBuilder.append(trainIdentityPath);

		return stringBuilder.toString();
	}

	private final RestfulWebServiceClientInterface webServiceClient;

	private final MessageDeserializerInterface<KnownTrainIdentifiersMessage> messageDeserializer;

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
	 * @param hostName Network address to use to contact service
	 * @param port Network port to use to contact service
	 */
	public RemoteTrainIdentityService(String hostName, int port) {

		this(hostName, port, new RestletWebServiceClient(), new JsonMessageDeserializer<KnownTrainIdentifiersMessage>(KnownTrainIdentifiersMessage.class));

	}

	/**
	 * Constructor
	 * @param webServiceClient Contact to the remote service
	 * @param messageDeserializer Decodes messages received from the service
	 */
	public RemoteTrainIdentityService(String hostName, int port, RestfulWebServiceClientInterface webServiceClient, MessageDeserializerInterface<KnownTrainIdentifiersMessage> messageDeserializer) {
		this.hostName = hostName;
		this.port = port;
		this.webServiceClient = webServiceClient;
		this.messageDeserializer = messageDeserializer;
	}

	/**
	 * Retrieves IDs for all known train that belong to the rail system
	 * @return The list of all known train that belong to the rail system.
	 */
	public List<String> getTrainIdentifiers() {

		String requestUrl = createTrainIdentifierRequestUrl(hostName, port);
		
		String response = webServiceClient.sendRequest(requestUrl);
		KnownTrainIdentifiersMessage results = messageDeserializer.deserialize(response);


		List<String> trainIds = new LinkedList<String>();
		
		for(TrainIdentifier trainIdentifier : results.getTrainIdentifiers()){
			trainIds.add(trainIdentifier.getTrainIdentifier());
		}
		
		return trainIds;
	}
}
