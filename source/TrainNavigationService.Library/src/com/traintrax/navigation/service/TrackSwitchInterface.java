package com.traintrax.navigation.service;

/**
 * Interface represents a single track switch on the
 * positive train control test bed.
 * @author Corey Sanders
 *
 */
public interface TrackSwitchInterface {
	
	/**
	 * Retrieves the unique identifier for the represented switch
	 * @return Unique identifier for the represented switch
	 */
	String getSwitchId();
	
	/**
	 * Retrieves the current state of the represented switch
	 * @return Current state of the represented switch
	 */
	SwitchState getCurrentSwitchState();
	
	/**
	 * Changes the state of the represented switch
	 * @param switchState State to change the switch
	 */
	void setSwitchState(SwitchState switchState);
	
	/**
	 * Subscribes a client to receive track switch events
	 * @param subscriber Client to receive track switch events
	 */
	void Subscribe(TrackSwitchEventSubscriber subscriber);
	
	/**
	 * Unsubscribes a client from receiving track switch events
	 * @param subscriber Client receiving track switch events
	 */
	void Unsubscribe(TrackSwitchEventSubscriber subscriber);

}
