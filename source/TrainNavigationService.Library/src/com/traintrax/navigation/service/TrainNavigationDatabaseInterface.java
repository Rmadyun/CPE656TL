package com.traintrax.navigation.service;

import java.util.List;

import com.traintrax.navigation.database.library.TrackSwitch;
import com.traintrax.navigation.service.position.AccelerometerMeasurement;
import com.traintrax.navigation.service.position.Coordinate;
import com.traintrax.navigation.service.position.GyroscopeMeasurement;
import com.traintrax.navigation.service.position.RfidTagDetectedNotification;
import com.traintrax.navigation.service.position.TrainPositionEstimate;

/**
 * Interface responsible for contacting the Train
 * Navigation Database
 * @author Corey Sanders
 *
 */
public interface TrainNavigationDatabaseInterface {

	/**
	 * Lookup the position of an RFID Tag placed on the Positive Train
	 * Control Test Bed track.
	 * @param rfidTagIdentifier Unique identifier for the target RFID tag.
	 * @return Position on the track of the RFID tag (Relative position from the
	 * origin in meters)
	 */
	Coordinate findTrackMarkerPosition(String rfidTagIdentifier);
	
	/**
	 * Lists all of the known track switches on the track
	 * @return All of the known track switches on the track
	 */
	List<TrackSwitch> getTrackSwitches();

	/**
	 * Persists an estimate of a given train's position.
	 * @param trainPosition Train position estimate to save
	 */
	void save(TrainPositionEstimate trainPosition);
	
	/**
	 * Persists an accelerometer measurement
	 * @param measurement Measurement to save
	 */
	void save(AccelerometerMeasurement measurement);
	
	/**
	 * Persists an gyroscope measurement
	 * @param measurement Measurement to save
	 */
	void save(GyroscopeMeasurement measurement);
	
	/**
	 * Persists a RFID Tag Detected notification
	 * @param notification Notification to save
	 */
	void save(RfidTagDetectedNotification notification);
	
}
