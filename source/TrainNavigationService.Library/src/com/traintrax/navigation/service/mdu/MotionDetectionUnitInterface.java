package com.traintrax.navigation.service.mdu;

import java.util.Collection;
import java.util.List;

import com.traintrax.navigation.service.position.AccelerometerMeasurement;
import com.traintrax.navigation.service.position.GyroscopeMeasurement;
import com.traintrax.navigation.service.position.RfidTagDetectedNotification;
import com.traintrax.navigation.service.position.Train;

/**
 * Interface facilitates communication with the Motion 
 * Detection Unit
 * @author Corey Sanders
 *
 */
public interface MotionDetectionUnitInterface {
	
	/**
	 * Retrieves a list of all of the train that have
	 * been reported on the MDU channel being used.
	 * @return a list of all of the train that have
	 * been reported on the MDU channel being used.
	 */
	Collection<Train> getAssociatedTrains();
	
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
	
	/**
	 * Assigns the callback used to notify the rest of the navigation service
	 * about internal changes in the MDU
	 * @param mduCallback Interface to use to send notifications about MDU internal changes
	 */
	void setMduCallback(MduCallbackInterface mduCallback);
}
