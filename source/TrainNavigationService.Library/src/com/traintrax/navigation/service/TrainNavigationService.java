package com.traintrax.navigation.service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.traintrax.navigation.service.events.PublisherInterface;
import com.traintrax.navigation.service.position.Coordinate;
import com.traintrax.navigation.service.position.ValueUpdate;
import com.traintrax.navigation.service.trackswitch.SwitchState;

/**
 * Representation of the Train Navigation Service.
 * @author Corey Sanders
 *
 */
public class TrainNavigationService implements TrainNavigationServiceInterface {
	
	private Timer timer;
	private final TrainMonitorInterface trainMonitor;
	private final TrainControllerInterface trainController;
	private final PublisherInterface<TrainNavigationServiceEventSubscriber, TrainPositionUpdatedEvent> eventPublisher;
	private static final int POLL_RATE_IN_MS = 10000;
	private final Map<String, ValueUpdate<Coordinate>> trainPositionLut = new HashMap<>();

	/**
	 * Constructor
	 * @param trainMonitor Provides train position information
	 * @param trainController Controls the train and track
	 */
	public TrainNavigationService(TrainMonitorInterface trainMonitor, TrainControllerInterface trainController, PublisherInterface<TrainNavigationServiceEventSubscriber, TrainPositionUpdatedEvent> eventPublisher){
		this.trainMonitor = trainMonitor;
		this.trainController = trainController;
		this.eventPublisher = eventPublisher;
	
		setupTimer();
	}
	
	/**
	 * Configures the timer to run for listening to train events
	 */
	private void setupTimer(){
		timer.scheduleAtFixedRate(new TimerTask() {
			  @Override
			  public void run() {
			    
				  ValueUpdate<Coordinate> positionUpdate = trainMonitor.getLastKnownPosition();
				  TrainPositionUpdatedEvent updatedEvent = new TrainPositionUpdatedEvent(trainMonitor.getTrainId(), positionUpdate);
				  
				  trainPositionLut.put(updatedEvent.getTrainIdentifier(), updatedEvent.getPosition());
				  eventPublisher.PublishEvent(updatedEvent);
			  }
			}, POLL_RATE_IN_MS, POLL_RATE_IN_MS);
	}
	
    /**
     * Gets the current state of a given switch
     * @param switchIdentifier Unique identifier for the switch of interest
     * @return Current state of the switch requested
     */
	public SwitchState GetSwitchState(String switchIdentifier){
		
		return trainController.getSwitchState(switchIdentifier);
	}
	
	/**
	 * Changes the state of a given switch
     * @param switchIdentifier Unique identifier for the switch of interest 
	 * @param switchState State requested for the switch to change
	 */
	public void SetSwitchState(String switchIdentifier, SwitchState switchState){
		
		trainController.ChangeSwitchState(switchIdentifier, switchState); 
	}
	
	/**
	 * Subscribe a client to listen to events from the Train Navigation service.
	 * @param subscriber Client requested to listen to service events.
	 */
	public void Subscribe(TrainNavigationServiceEventSubscriber subscriber){
		eventPublisher.Subscribe(subscriber);
	}
	
	/**
	 * Unsubscribe a client from listening to events
	 * @param subscriber Client requesting to stop receiving events from the service.
	 */
	public void Unsubscribe(TrainNavigationServiceEventSubscriber subscriber){
		eventPublisher.Unsubscribe(subscriber);
	}

	@Override
	public ValueUpdate<Coordinate> GetLastKnownPosition(String trainIdentifier) {
		return trainPositionLut.get(trainIdentifier);
	}

	@Override
	public List<String> GetKnownTrainIdentifiers() {
		List<String> knownTrainIdentifers = new LinkedList<String>(trainPositionLut.keySet());
		
		return knownTrainIdentifers;
	}


}
