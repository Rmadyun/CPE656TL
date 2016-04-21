package com.traintrax.navigation.service;

import com.traintrax.navigation.service.position.Coordinate;
import com.traintrax.navigation.service.position.TrainPositionEstimate;

/**
 * Event notifies that the new information about the position of a train has been determined
 * @author Corey Sanders
 *
 */
public class TrainPositionUpdatedEvent extends TrainNavigationServiceEvent {

	private final String trainIdentifier;
	private final TrainPositionEstimate position;
	
	/**
	 * Constructor 
	 * @param trainIdentifier Unique identifier for the train
	 * @param positionUpdate Details on the new data on the position of the train
	 */
	public TrainPositionUpdatedEvent(String trainIdentifier, TrainPositionEstimate positionUpdate) {
		super();
		this.trainIdentifier = trainIdentifier;
		this.position = positionUpdate;
	}
	/**
	 * Retrieves the Train ID
	 * @return a Unique identifier for a train
	 */
	public String getTrainIdentifier() {
		return trainIdentifier;
	}
	/**
	 * Retrieves the train position estimate recorded in this event
	 * @return the train position estimate recorded in this event
	 */
	public TrainPositionEstimate getPosition() {
		return position;
	}
	
}
