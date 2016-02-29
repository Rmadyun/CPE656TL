package com.traintrax.navigation.service;

import com.traintrax.navigation.service.mdu.AccelerometerMeasurement;
import com.traintrax.navigation.service.mdu.GyroscopeMeasurement;
import com.traintrax.navigation.service.mdu.RfidTagDetectedNotification;
import com.traintrax.navigation.service.position.Coordinate;

/**
 * Class communicates with the Train Navigation Database
 * @author Corey Sanders
 *
 */
public class TrainNavigationDatabase implements TrainNavigationDatabaseInterface {

	/**
	 * Constructor
	 */
	public TrainNavigationDatabase(){
		
	}
	
	/**
	 * Lookup the position of an RFID Tag placed on the Positive Train
	 * Control Test Bed track.
	 * @param rfidTagIdentifier Unique identifier for the target RFID tag.
	 * @return Position on the track of the RFID tag
	 */
	public Coordinate findTrackMarkerPosition(String rfidTagIdentifier){
		
		//TODO: Implement Track Marker lookup support
		
		return new Coordinate(0, 0);
	}
	
	/**
	 * Persists an estimate of a given train's position.
	 * @param trainPosition Train position estimate to save
	 */
	public void save(TrainPositionEstimate trainPosition){
		//TODO: Implement Train position estimate save
	}
	
	/**
	 * Persists an accelerometer measurement
	 * @param measurement Measurement to save
	 */
	public void save(AccelerometerMeasurement measurement){
		//TODO: Implement Accelerometer measurement save
	}
	
	/**
	 * Persists an gyroscope measurement
	 * @param measurement Measurement to save
	 */
	public void save(GyroscopeMeasurement measurement){
		//TODO: Implement Gyroscope measurement save
	}
	
	/**
	 * Persists a RFID Tag Detected notification
	 * @param notification Notification to save
	 */
	public void save(RfidTagDetectedNotification notification){
		//TODO: Implement RFID Tag notification save
	}
	
}
