package com.traintrax.navigation.service.rest.client;
import java.io.IOException;

import org.restlet.ext.json.JsonRepresentation;

import com.google.gson.Gson;
import com.traintrax.navigation.service.rest.data.KnownTrainIdentifiersMessage;
import com.traintrax.navigation.service.rest.data.TrackSwitchStateMessage;
import com.traintrax.navigation.trackswitch.SwitchState;

/**
 * Restful Web client to the Train Navigation Service to retrieve information
 * about the switches on the track
 * 
 * @author Corey Sanders
 *
 */
 class RemoteTrackSwitchService {
	/**
	 * Represents the location where requests for track switch information
	 * be sent.
	 */
	private static final String trackSwitchStatePath = "/TrackSwitchState";


	/**
	 * Creates the URL used for Restful web requests to a remote Rest service
	 * 
	 * @param host
	 *            Host name or IP of the target server
	 * @param port
	 *            IP port of the target server
	 * @return URL used for Restful web requests
	 */
	private static String createTrackSwitchStateRequestUrl(String host, int port) {
		StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append("http://");
		stringBuilder.append(host);
		stringBuilder.append(":");
		stringBuilder.append(port);
		stringBuilder.append(trackSwitchStatePath);

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
	private static String createTrackSwitchStateRequestUrl(String host, int port, String switchNumber) {
		String baseUrl = createTrackSwitchStateRequestUrl(host, port);

		String finalUrl = baseUrl + "?id=" + switchNumber;

		return finalUrl;
	}

	private final RestfulWebServiceClientInterface webServiceClient;

	private final MessageDeserializerInterface<TrackSwitchStateMessage> messageDeserializer;

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
	public RemoteTrackSwitchService(String hostName, int port) {

		this(hostName, port, new RestletWebServiceClient(), new JsonMessageDeserializer<TrackSwitchStateMessage>(TrackSwitchStateMessage.class));
	}

	/**
	 * Constructor
	 * @param hostName Network address to use to contact service
	 * @param port Network port to use to contact service
	 * @param webServiceClient Contact to the remote service
	 * @param messageDeserializer Decodes messages received from the service
	 */
	public RemoteTrackSwitchService(String hostName, int port, RestfulWebServiceClientInterface webServiceClient, MessageDeserializerInterface<TrackSwitchStateMessage> messageDeserializer) {

		this.hostName = hostName;
		this.port = port;
		this.webServiceClient = webServiceClient;
		this.messageDeserializer = messageDeserializer;
	}

	/**
	 * Retrieves the current state of the track switch
	 * @param id Unique ID of track switch of interest
	 * @return The current state of the track switch
	 */
	public SwitchState getTrackSwitchState(String id) {

		String requestUrl = createTrackSwitchStateRequestUrl(hostName, port, id);
		
		String response = webServiceClient.sendRequest(requestUrl);
		TrackSwitchStateMessage results = messageDeserializer.deserialize(response);
		
		return SwitchState.valueOf(results.getSwitchState());
	}
	
	/**
	 * Retrieves the last known position of a given train
	 * @param id Unique ID of the train we want the position for
	 * @return The last known position of a given train.
	 * @throws IOException Throws error of encoding string fails.
	 */
	public void setSwitchState(String id, SwitchState switchState) throws IOException {

		String requestUrl = createTrackSwitchStateRequestUrl(hostName, port, id);
		
		TrackSwitchStateMessage  trackSwitchStateMessage = new TrackSwitchStateMessage(id, switchState.toString());

		Gson gson = new Gson();
		String message = gson.toJson(trackSwitchStateMessage);

		webServiceClient.post(requestUrl, message);
	}
}
