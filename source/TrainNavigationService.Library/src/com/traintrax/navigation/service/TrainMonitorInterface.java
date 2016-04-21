package com.traintrax.navigation.service;

import com.traintrax.navigation.service.position.Coordinate;
import com.traintrax.navigation.service.position.TrainPositionEstimate;

/**
 * Interface is responsible for observing changes to trains that belong
 * to the Positive Train Control Test Bed.
 * @author Corey Sanders
 *
 */
public interface TrainMonitorInterface {
	
	/**
	 * Blocks until there is another update on the target train's
	 * position.
	 * @return Most recent update on the position of the train.
	 */
	TrainPositionEstimate waitForNextPositionUpdate();
	
	/**
	 * Retrieves the unique identifier for the target train
	 * @return Unique identifier for the target train
	 */
	String getTrainId();
	
	
	/**
	 * Retrieves the last known position of the target train
	 * @return The last known position of the target train.
	 */
	TrainPositionEstimate getLastKnownPosition();

}
