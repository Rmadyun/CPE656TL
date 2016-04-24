package com.traintrax.navigation.service.position;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Class encapsulates all of the information being tracked about the state of a
 * given train on the test bed.
 * 
 * @author Corey Sanders
 *
 */
public class Train {

	private final String trainId;
	private final Queue<AccelerometerMeasurement> collectedAccelerometerMeasurements;
	private final Queue<GyroscopeMeasurement> collectedGyroscopeMeasurements;
	private final Queue<RfidTagDetectedNotification> collectedRfidTagDetectionNotifications;

	Calendar lastGyroscopeMeasurement = null;
	Calendar lastAccelerometerMeasurement = null;

	/**
	 * Constructor
	 * 
	 * @param trainId
	 *            Unique identifier for the train
	 */
	public Train(String trainId) {
		super();
		this.trainId = trainId;
		collectedAccelerometerMeasurements = new ConcurrentLinkedQueue<AccelerometerMeasurement>();
		collectedGyroscopeMeasurements = new ConcurrentLinkedQueue<GyroscopeMeasurement>();
		collectedRfidTagDetectionNotifications = new ConcurrentLinkedQueue<RfidTagDetectedNotification>();
	}
	
	/**
	 * Unique identifier associated with the train
	 * @return Unique identifier associated with the train
	 */
	public String getTrainId() {
		return trainId;
	}

	/**
	 * Capture an accelerometer measurement from the target train
	 * @param accelerometerMeasurement Accelerometer measurement for the target train
	 */
	public void add(AccelerometerMeasurement accelerometerMeasurement) {
		collectedAccelerometerMeasurements.add(accelerometerMeasurement);
		lastAccelerometerMeasurement = accelerometerMeasurement.getTimeMeasured();
	}

	/**
	 * Capture a gyroscope measurement from the target train
	 * @param gyroscopeMeasurement Gyroscope measurement for the target train
	 */
	public void add(GyroscopeMeasurement gyroscopeMeasurement) {
		collectedGyroscopeMeasurements.add(gyroscopeMeasurement);
		lastGyroscopeMeasurement = gyroscopeMeasurement.getTimeMeasured();
	}

	/**
	 * Capture a RFID tag detection notification from the target train
	 * @param rfidTagDetectedNotifiation RFID tag detection notification from the target train
	 */
	public void add(RfidTagDetectedNotification rfidTagDetectedNotifiation) {
		collectedRfidTagDetectionNotifications.add(rfidTagDetectedNotifiation);
	}

	/**
	 * Read collected accelerometer measurements
	 * 
	 * @return Collected accelerometer measurements
	 */
	public List<AccelerometerMeasurement> readCollectedAccelerometerMeasurements() {
		List<AccelerometerMeasurement> collected = new LinkedList<AccelerometerMeasurement>();
		AccelerometerMeasurement measurement = null;

		if (!collectedAccelerometerMeasurements.isEmpty()) {
			do {
				measurement = collectedAccelerometerMeasurements.poll();
				if (measurement != null) {
					collected.add(measurement);
				}
			} while (measurement != null);
		}

		return collected;
	}

	/**
	 * Read collected gyroscope measurements
	 * 
	 * @return Collected gyroscope measurements
	 */
	public List<GyroscopeMeasurement> readCollectedGyroscopeMeasurements() {
		List<GyroscopeMeasurement> collected = new LinkedList<GyroscopeMeasurement>();
		GyroscopeMeasurement measurement = null;

		if (!collectedGyroscopeMeasurements.isEmpty()) {
			do {
				measurement = collectedGyroscopeMeasurements.poll();
				if (measurement != null) {
					collected.add(measurement);
				}
			} while (measurement != null);
		}

		return collected;
	}

	/**
	 * Read collected RFID tag detection notifications
	 * 
	 * @return Collected RFID tag detection notifications
	 */
	public List<RfidTagDetectedNotification> readCollectedRfidTagDetectionNotifications() {
		List<RfidTagDetectedNotification> collected = new LinkedList<RfidTagDetectedNotification>();
		RfidTagDetectedNotification measurement = null;

		if (!collectedRfidTagDetectionNotifications.isEmpty()) {
			do {
				measurement = collectedRfidTagDetectionNotifications.poll();
				if (measurement != null) {
					collected.add(measurement);
				}
			} while (measurement != null);
		}

		return collected;
	}

	/**
	 * Retrieves the last gyroscope measurement assigned to the train
	 * @return the last gyroscope measurement assigned to the train
	 */
	public Calendar getLastGyroscopeMeasurement() {
		return lastGyroscopeMeasurement;
	}

	/**
	 * Retrieves the last accelerometer measurement assigned to the train
	 * @return the last accelerometer measurement assigned to the train
	 */
	public Calendar getLastAccelerometerMeasurement() {
		return lastAccelerometerMeasurement;
	}

}
