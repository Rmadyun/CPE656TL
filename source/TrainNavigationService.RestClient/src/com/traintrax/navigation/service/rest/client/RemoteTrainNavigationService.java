package com.traintrax.navigation.service.rest.client;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.traintrax.navigation.service.TrainNavigationServiceEvent;
import com.traintrax.navigation.service.TrainNavigationServiceEventNotifier;
import com.traintrax.navigation.service.TrainNavigationServiceEventSubscriber;
import com.traintrax.navigation.service.TrainNavigationServiceInterface;
import com.traintrax.navigation.service.TrainPositionUpdatedEvent;
import com.traintrax.navigation.service.events.GenericPublisher;
import com.traintrax.navigation.service.events.NotifierInterface;
import com.traintrax.navigation.service.events.PublisherInterface;
import com.traintrax.navigation.service.position.Coordinate;
import com.traintrax.navigation.service.position.ValueUpdate;
import com.traintrax.navigation.service.trackswitch.SwitchState;

/**
 * Restful Web client implementation of the Train Navigation Service
 * 
 * @author Corey Sanders
 *
 */
public class RemoteTrainNavigationService implements TrainNavigationServiceInterface {

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

	private Timer timer = new Timer();
	private final PublisherInterface<TrainNavigationServiceEventSubscriber, TrainNavigationServiceEvent> eventPublisher;
	private static final int POLL_RATE_IN_MS = 10000;

	/**
	 * Configures the timer to run for listening to train events
	 */
	private void setupTimer() {
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {

				List<String> trainIds = trainIdentityService.getTrainIdentifiers();

				for (String trainId : trainIds) {

					ValueUpdate<Coordinate> positionUpdate = trainPositionService.getLastKnownTrainPosition(trainId);
					TrainPositionUpdatedEvent updatedEvent = new TrainPositionUpdatedEvent(trainId, positionUpdate);
					eventPublisher.PublishEvent(updatedEvent);
				}

			}
		}, 0, POLL_RATE_IN_MS);
	}

	/**
	 * Constructor
	 */
	public RemoteTrainNavigationService() {
		hostName = "localhost";
		port = 8182;
		this.trainPositionService = new RemoteTrainPositionService();
		this.trainIdentityService = new RemoteTrainIdentityService();
		this.trackSwitchService = new RemoteTrackSwitchService();

		NotifierInterface<TrainNavigationServiceEventSubscriber, TrainNavigationServiceEvent> eventNotifier = new TrainNavigationServiceEventNotifier();
		PublisherInterface<TrainNavigationServiceEventSubscriber, TrainNavigationServiceEvent> eventPublisher = new GenericPublisher<TrainNavigationServiceEventSubscriber, TrainNavigationServiceEvent>(
				eventNotifier);

		this.eventPublisher = eventPublisher;

		setupTimer();
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
		eventPublisher.Subscribe(subscriber);
	}

	@Override
	public void Unsubscribe(TrainNavigationServiceEventSubscriber subscriber) {
		eventPublisher.Unsubscribe(subscriber);
	}
}
