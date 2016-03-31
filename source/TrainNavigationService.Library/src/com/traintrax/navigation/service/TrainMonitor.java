package com.traintrax.navigation.service;

import java.util.LinkedList;
import java.util.List;

import com.traintrax.navigation.service.mdu.AccelerometerMeasurement;
import com.traintrax.navigation.service.mdu.GyroscopeMeasurement;
import com.traintrax.navigation.service.mdu.InertialMotionPositionAlgorithmInterface;
import com.traintrax.navigation.service.mdu.MotionDetectionUnitInterface;
import com.traintrax.navigation.service.mdu.RfidTagDetectedNotification;
import com.traintrax.navigation.service.position.Coordinate;
import com.traintrax.navigation.service.position.UnitConversionUtilities;

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
	private final TrainNavigationDatabaseInterface trainNavigationDatabase;
	
	/**
	 * Constructor
	 * NOTE: Assuming that the train is a rest at initialization.
	 * @param trainId
	 *            Unique identifier for target train
	 * @param positionAlgorithm Determines the position of the train based
	 * on inertial measurements from the the train.
	 * @param motionDetectionUnit Contact to the Motion Detection Unit
	 * mounted on the train of interest.
	 * @param trainNavigationDatabase Contact to the Train Navigation Database
	 */
	public TrainMonitor(String trainId, InertialMotionPositionAlgorithmInterface positionAlgorithm,
			MotionDetectionUnitInterface motionDetectionUnit,
			TrainNavigationDatabaseInterface trainNavigationDatabase) {

		this.trainId = trainId;
		lastKnownTrainPosition = positionAlgorithm.calculatePosition(null,  null, null);
		this.positionAlgorithm = positionAlgorithm;
		this.motionDetectionUnit = motionDetectionUnit;
		this.trainNavigationDatabase = trainNavigationDatabase;
	}

	/**
	 * Blocks until there is another update on the target train's position.
	 * 
	 * @return Most recent update on the position of the train.
	 */
	public ValueUpdate<Coordinate> waitForNextPositionUpdate() {
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
				//Wait if nothing is available
				try{
				    Thread.sleep(2000, 0);
				}
				catch(Exception exception){
					
				}
			}
			else{
				positionUpdateAvailable = true;
			}
			
		}
		while(!positionUpdateAvailable);
		
		List<ValueUpdate<Coordinate>> positionUpdates = new LinkedList<ValueUpdate<Coordinate>>();
		
		//Lookup RFID Tag position
		for(RfidTagDetectedNotification notification : newRfidTagEvents){
		
			Coordinate tagPosition = trainNavigationDatabase.findTrackMarkerPosition(notification.getRfidTagValue());
			
			if(tagPosition != null){
				ValueUpdate<Coordinate> positionUpdate = new ValueUpdate<>(tagPosition, notification.getTimeDetected());
				
				positionUpdates.add(positionUpdate);
			}
		}
				
		ValueUpdate<Coordinate> latestPositionUpdate = positionAlgorithm.calculatePosition(newGyroscopeMeasurements, newAccelerometerMeasurements, positionUpdates);
		
		//Convert position from meters to inches
		latestPositionUpdate = new ValueUpdate<Coordinate>(UnitConversionUtilities.convertFromMetersToInches(latestPositionUpdate.getValue()), latestPositionUpdate.getTimeObserved());
		
		this.lastKnownTrainPosition = latestPositionUpdate;
		
		//Save collected information
		trainNavigationDatabase.save(new TrainPositionEstimate(latestPositionUpdate.getValue(), latestPositionUpdate.getTimeObserved(), trainId));
		
		for(GyroscopeMeasurement measurement : newGyroscopeMeasurements){
			trainNavigationDatabase.save(measurement);
		}
		
		for(AccelerometerMeasurement measurement : newAccelerometerMeasurements){
			trainNavigationDatabase.save(measurement);
		}
		
		for(RfidTagDetectedNotification notification : newRfidTagEvents){
			trainNavigationDatabase.save(notification);
		}
		
		return  latestPositionUpdate;
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
	public ValueUpdate<Coordinate> getLastKnownPosition() {
		return lastKnownTrainPosition;
	}

}
