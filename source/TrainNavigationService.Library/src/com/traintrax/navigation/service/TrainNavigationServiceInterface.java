package com.traintrax.navigation.service;

/**
 * Standard interface for interacting with the Train Navigation Service
 * @author Corey Sanders
 *
 */
public interface TrainNavigationServiceInterface {

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
