package com.traintrax.navigation.service;

import java.util.ArrayList;
import java.util.List;

/**
 * Class facilitates communication with Motion Detection Unit
 * hardware
 * @author Corey Sanders
 *
 */
public class MotionDetectionUnit implements MotionDetectionUnitInterface {
    private final List<AccelerometerMeasurement> collectedAccelerometerMeasurements;
    private final List<GyroscopeMeasurement> collectedGyroscopeMeasurements;
    private final List<RfidTagDetectedNotification> collectedRfidTagDetectionNotifications;
	
	
	/**
	 * Constructor
	 */
	public MotionDetectionUnit(){
		collectedAccelerometerMeasurements = new ArrayList<AccelerometerMeasurement>();
		collectedGyroscopeMeasurements = new ArrayList<GyroscopeMeasurement>();
		collectedRfidTagDetectionNotifications = new ArrayList<RfidTagDetectedNotification>();
		
		//TODO: Implement support contact the Motion Detection Unit.
		//TODO: Implement support collect measurements
	}

	/**
	 * Read collected accelerometer measurements
	 * @return Collected accelerometer measurements
	 */
	public List<AccelerometerMeasurement> readCollectedAccelerometerMeasurements(){
		return collectedAccelerometerMeasurements;
	}
	
	/**
	 * Read collected gyroscope measurements
	 * @return Collected gyroscope measurements
	 */
	public List<GyroscopeMeasurement> readCollectedGyroscopeMeasurements(){
		return collectedGyroscopeMeasurements;
	}
	
	/**
	 * Read collected RFID tag detection notifications
	 * @return Collected RFID tag detection notifications
	 */
	public List<RfidTagDetectedNotification> readCollectedRfidTagDetectionNotifications(){
		return collectedRfidTagDetectionNotifications;
	}
	
}
