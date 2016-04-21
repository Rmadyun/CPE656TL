package com.traintrax.navigation.service.mdu;

import java.util.List;

import com.traintrax.navigation.service.position.AccelerometerMeasurement;
import com.traintrax.navigation.service.position.GyroscopeMeasurement;
import com.traintrax.navigation.service.position.RfidTagDetectedNotification;

/**
 * Interface facilitates communication with the Motion 
 * Detection Unit
 * @author Corey Sanders
 *
 */
public interface MotionDetectionUnitInterface {
    
	/**
	 * Read collected accelerometer measurements
	 * @return Collected accelerometer measurements
	 */
	List<AccelerometerMeasurement> readCollectedAccelerometerMeasurements();
	
	/**
	 * Read collected gyroscope measurements
	 * @return Collected gyroscope measurements
	 */
	List<GyroscopeMeasurement> readCollectedGyroscopeMeasurements();
	
	/**
	 * Read collected RFID tag detection notifications
	 * @return Collected RFID tag detection notifications
	 */
	List<RfidTagDetectedNotification> readCollectedRfidTagDetectionNotifications();
}
