package com.traintrax.navigation.service;

import java.util.List;

import com.traintrax.navigation.service.mdu.AccelerometerMeasurement;
import com.traintrax.navigation.service.mdu.GyroscopeMeasurement;
import com.traintrax.navigation.service.mdu.InertialMotionPositionAlgorithmInterface;
import com.traintrax.navigation.service.mdu.MotionDetectionUnitInterface;
import com.traintrax.navigation.service.mdu.RfidTagDetectedNotification;
import com.traintrax.navigation.service.position.Coordinate;
import com.traintrax.navigation.service.position.ValueUpdate;

/**
 * Class is responsible for observing changes to a train that belongs to the
 * Positive Train Control Test Bed.
 * 
 * @author Corey Sanders
 * 
 */
public class TrainMonitor implements TrainMonitorInterface {

	private final String trainId;
	private ValueUpdate<Coordinate> lastKnownTrainPosition;
	private final InertialMotionPositionAlgorithmInterface positionAlgorithm;
	private final MotionDetectionUnitInterface motionDetectionUnit;
	
	/**
	 * Constructor
	 * NOTE: Assuming that the train is a rest at initialization.
	 * @param trainId
	 *            Unique identifier for target train
	 * @param positionAlgorithm Determines the position of the train based
	 * on inertial measurements from the the train.
	 * @param motionDetectionUnit Contact to the Motion Detection Unit
	 * mounted on the train of interest.
	 */
	public TrainMonitor(String trainId, InertialMotionPositionAlgorithmInterface positionAlgorithm,
			MotionDetectionUnitInterface motionDetectionUnit) {

		this.trainId = trainId;
		lastKnownTrainPosition = positionAlgorithm.calculatePosition(null,  null, null);
		this.positionAlgorithm = positionAlgorithm;
		this.motionDetectionUnit = motionDetectionUnit;
	}

	/**
	 * Blocks until there is another update on the target train's position.
	 * 
	 * @return Most recent update on the position of the train.
	 */
	public Coordinate waitForNextPositionUpdate() {
		List<GyroscopeMeasurement> newGyroscopeMeasurements;
		List<AccelerometerMeasurement> newAccelerometerMeasurements;
		List<RfidTagDetectedNotification> newRfidTagEvents;
		boolean positionUpdateAvailable = false;
		
		do{
			
			newGyroscopeMeasurements = motionDetectionUnit.readCollectedGyroscopeMeasurements();
			newAccelerometerMeasurements = motionDetectionUnit.readCollectedAccelerometerMeasurements();
			newRfidTagEvents = motionDetectionUnit.readCollectedRfidTagDetectionNotifications();
			
			if((newGyroscopeMeasurements.size() == 0) &&
					(newAccelerometerMeasurements.size() == 0) &&
					(newRfidTagEvents.size() == 0)){
				positionUpdateAvailable = true;
				
				//Wait if nothing is available
				try{
				    Thread.sleep(2000, 0);
				}
				catch(Exception exception){
					
				}
			}
			
		}
		while(!positionUpdateAvailable);
		
		ValueUpdate<Coordinate> latestPositionUpdate = positionAlgorithm.calculatePosition(newGyroscopeMeasurements, newAccelerometerMeasurements, null);
		
		this.lastKnownTrainPosition = latestPositionUpdate;
		
		return  latestPositionUpdate.getValue();
	}

	/**
	 * Retrieves the unique identifier for the target train
	 * 
	 * @return Unique identifier for the target train
	 */
	public String getTrainId() {
		return trainId;
	}

	/**
	 * Retrieves the last known position of the target train
	 * 
	 * @return The last known position of the target train.
	 */
	public Coordinate getLastKnownPosition() {
		return lastKnownTrainPosition.getValue();
	}

}
