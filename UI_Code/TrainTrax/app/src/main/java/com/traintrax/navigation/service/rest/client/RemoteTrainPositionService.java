package com.traintrax.navigation.service.rest.client;

import com.traintrax.navigation.service.ValueUpdate;
import com.traintrax.navigation.service.position.Coordinate;
import com.traintrax.navigation.service.rest.data.KnownTrainIdentifiersMessage;
import com.traintrax.navigation.service.rest.data.TrainPositionUpdateMessage;

/**
 * Restful Web client to the Train Navigation Service to retrieve information
 * about the positions of known trains.
 * 
 * @author Corey Sanders
 *
 */
 class RemoteTrainPositionService {
	/**
	 * Represents the location where requests for train position information
	 * be sent.
	 */
	private static final String trainPositionPath = "/TrainPosition";


	/**
	 * Creates the URL used for Restful web requests to a remote Rest service
	 * 
	 * @param host
	 *            Host name or IP of the target server
	 * @param port
	 *            IP port of the target server
	 * @return URL used for Restful web requests
	 */
	private static String createTrainPositionRequestUrl(String host, int port) {
		StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append("http://");
		stringBuilder.append(host);
		stringBuilder.append(":");
		stringBuilder.append(port);
		stringBuilder.append(trainPositionPath);

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
	private static String createTrainPositionRequestUrl(String host, int port, int trainId) {
		String baseUrl = createTrainPositionRequestUrl(host, port);

		String finalUrl = baseUrl + "?id=" + Integer.toString(trainId);

		return finalUrl;
	}

	private final RestfulWebServiceClientInterface webServiceClient;

	private final MessageDeserializerInterface<TrainPositionUpdateMessage> messageDeserializer;

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
	public RemoteTrainPositionService(String hostName, int port) {

		this(hostName, port, new RestletWebServiceClient(), new JsonMessageDeserializer<TrainPositionUpdateMessage>(TrainPositionUpdateMessage.class));

	}

	/**
	 * Constructor
	 * @param hostName Network address to use to contact service
	 * @param port Network port to use to contact service
	 * @param webServiceClient Contact to the remote service
	 * @param messageDeserializer Decodes messages received from the service
	 */
	public RemoteTrainPositionService(String hostName, int port, RestfulWebServiceClientInterface webServiceClient, MessageDeserializerInterface<TrainPositionUpdateMessage> messageDeserializer) {

		hostName = "localhost";
		port = 8182;
		this.webServiceClient = webServiceClient;
		this.messageDeserializer = messageDeserializer;
	}

	/**
	 * Converts an adjacent point match into a track repository entry
	 * 
	 * @param trainPositionMessage
	 *            Adjacent point match from Restful web service query to convert
	 * @return Repository Entry representation of the adjacent point
	 */
	private ValueUpdate<Coordinate> convertToPositionUpdate(TrainPositionUpdateMessage trainPositionMessage) {

		Coordinate location = new Coordinate(trainPositionMessage.getX(), trainPositionMessage.getY(), trainPositionMessage.getZ());
		
		ValueUpdate<Coordinate> positionUpdate = new ValueUpdate<Coordinate>(location, trainPositionMessage.getTimeMeasured());

		return positionUpdate;
	}

	/**
	 * Retrieves the last known position of a given train
	 * @param id Unique ID of the train we want the position for
	 * @return The last known position of a given train.
	 */
	public ValueUpdate<Coordinate> getLastKnownTrainPosition(String id) {

		String requestUrl = createTrainPositionRequestUrl(hostName, port, Integer.parseInt(id));
		
		ValueUpdate<Coordinate> match = null;
		String response = webServiceClient.sendRequest(requestUrl);
		TrainPositionUpdateMessage results = messageDeserializer.deserialize(response);

	    match = convertToPositionUpdate(results);

		return match;
	}
}
