package com.traintrax.navigation.service.mdu;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Class facilitates communication with Motion Detection Unit
 * hardware
 * @author Corey Sanders
 *
 */
public class MotionDetectionUnit implements MotionDetectionUnitInterface {
	private final Queue<AccelerometerMeasurement> collectedAccelerometerMeasurements;
    private final Queue<GyroscopeMeasurement> collectedGyroscopeMeasurements;
    private final Queue<RfidTagDetectedNotification> collectedRfidTagDetectionNotifications;

	/**
	 * Constructor
	 */
	public MotionDetectionUnit(){
		collectedAccelerometerMeasurements = new ConcurrentLinkedQueue<AccelerometerMeasurement>();
		collectedGyroscopeMeasurements = new ConcurrentLinkedQueue<GyroscopeMeasurement>();
		collectedRfidTagDetectionNotifications = new ConcurrentLinkedQueue<RfidTagDetectedNotification>();
		
		//TODO: Setup support to contact MDU
		//TODO: Setup support to collect measurements
	}
	
	/**
	 * Read collected accelerometer measurements
	 * @return Collected accelerometer measurements
	 */
	public List<AccelerometerMeasurement> readCollectedAccelerometerMeasurements(){
		
		List<AccelerometerMeasurement> collected = new LinkedList<AccelerometerMeasurement>();
		AccelerometerMeasurement measurement = null;
		
		do{
		    measurement = collectedAccelerometerMeasurements.poll();
		    collected.add(measurement);
		}
		while(measurement != null);
		
		return collected;
	}
	
	/**
	 * Read collected gyroscope measurements
	 * @return Collected gyroscope measurements
	 */
	public List<GyroscopeMeasurement> readCollectedGyroscopeMeasurements(){
		List<GyroscopeMeasurement> collected = new LinkedList<GyroscopeMeasurement>();
		GyroscopeMeasurement measurement = null;
		
		do{
		    measurement = collectedGyroscopeMeasurements.poll();
		    collected.add(measurement);
		}
		while(measurement != null);
		
		return collected;
	}
	
	/**
	 * Read collected RFID tag detection notifications
	 * @return Collected RFID tag detection notifications
	 */
	public List<RfidTagDetectedNotification> readCollectedRfidTagDetectionNotifications(){
		List<RfidTagDetectedNotification> collected = new LinkedList<RfidTagDetectedNotification>();
		RfidTagDetectedNotification measurement = null;
		
		do{
		    measurement = collectedRfidTagDetectionNotifications.poll();
		    collected.add(measurement);
		}
		while(measurement != null);
		
		return collected;
	}
	
}
