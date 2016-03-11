package com.traintrax.navigation.service.rest.client;
import java.io.IOException;
import java.util.List;

import com.traintrax.navigation.service.TrainNavigationServiceEventSubscriber;
import com.traintrax.navigation.service.TrainNavigationServiceInterface;
import com.traintrax.navigation.service.position.Coordinate;
import com.traintrax.navigation.service.position.ValueUpdate;
import com.traintrax.navigation.service.trackswitch.SwitchState;

/**
 * Restful Web client implementation of the Train Navigation Service
 * 
 * @author Corey Sanders
 *
 */
public class RemoteTrainNavigationService
		implements TrainNavigationServiceInterface {

	/**
	 * Host name or IP of the target Restful web service
	 */
	private String hostName;

	/**
	 * IP port of the target Restful web service
	 */
	private int port;

	private final RemoteTrainPositionService trainPositionService;

	private final RemoteTrainIdentityService trainIdentityService;
	
	private final RemoteTrackSwitchService trackSwitchService;

	/**
	 * Constructor
	 */
	public RemoteTrainNavigationService() {
		hostName = "localhost";
		port = 8182;
		this.trainPositionService = new RemoteTrainPositionService();
		this.trainIdentityService = new RemoteTrainIdentityService();
		this.trackSwitchService = new RemoteTrackSwitchService();
	}


	@Override
	public ValueUpdate<Coordinate> GetLastKnownPosition(String trainIdentifier) {
		return trainPositionService.getLastKnownTrainPosition(trainIdentifier);
	}

	@Override
	public List<String> GetKnownTrainIdentifiers() {
		return trainIdentityService.getTrainIdentifiers();
	}

	@Override
	public SwitchState GetSwitchState(String switchIdentifier) {
		
		SwitchState switchState = trackSwitchService.getTrackSwitchState(switchIdentifier);
		
		return switchState;
	}

	@Override
	public void SetSwitchState(String switchIdentifier, SwitchState switchState) throws IOException {
		
		trackSwitchService.setSwitchState(switchIdentifier, switchState);
		
	}

	@Override
	public void Subscribe(TrainNavigationServiceEventSubscriber subscriber) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Unsubscribe(TrainNavigationServiceEventSubscriber subscriber) {
		// TODO Auto-generated method stub
		
	}
}
