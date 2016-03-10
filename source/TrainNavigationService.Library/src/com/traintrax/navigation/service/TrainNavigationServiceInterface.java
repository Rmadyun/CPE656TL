package com.traintrax.navigation.service;

import java.util.List;

import com.traintrax.navigation.service.position.Coordinate;
import com.traintrax.navigation.service.position.ValueUpdate;
import com.traintrax.navigation.service.trackswitch.SwitchState;

/**
 * Standard interface for interacting with the Train Navigation Service
 * @author Corey Sanders
 *
 */
public interface TrainNavigationServiceInterface {
	
	/**
	 * Retrieves the last known position of a given train
	 * @param trainIdentifier Unique ID for the train that we want to have the position for.
	 * @return the last known position of a given train.
	 */
	ValueUpdate<Coordinate> GetLastKnownPosition(String trainIdentifier);
	
	/**
	 * Retrieves the unique ID associated with each train known by the service.
	 * @return List of unique IDs. One for each train known by the service.
	 */
	List<String> GetKnownTrainIdentifiers();

    /**
     * Gets the current state of a given switch
     * @param switchIdentifier Unique identifier for the switch of interest
     * @return Current state of the switch requested
     */
	SwitchState GetSwitchState(String switchIdentifier);
	
	/**
	 * Changes the state of a given switch
     * @param switchIdentifier Unique identifier for the switch of interest 
	 * @param switchState State requested for the switch to change
	 */
	void SetSwitchState(String switchIdentifier, SwitchState switchState);
	
	/**
	 * Subscribe a client to listen to events from the Train Navigation service.
	 * @param subscriber Client requested to listen to service events.
	 */
	void Subscribe(TrainNavigationServiceEventSubscriber subscriber);
	
	/**
	 * Unsubscribe a client from listening to events
	 * @param subscriber Client requesting to stop receiving events from the service.
	 */
	void Unsubscribe(TrainNavigationServiceEventSubscriber subscriber);
}
