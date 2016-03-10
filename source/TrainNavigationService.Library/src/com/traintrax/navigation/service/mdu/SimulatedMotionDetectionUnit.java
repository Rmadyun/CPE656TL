package com.traintrax.navigation.service.mdu;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.traintrax.navigation.service.position.Acceleration;

/**
 * Class facilitates simulates collaboration with a Motion Detection Unit
 * 
 * @author Corey Sanders
 *
 */
public class SimulatedMotionDetectionUnit implements MotionDetectionUnitInterface {
	private final Queue<AccelerometerMeasurement> collectedAccelerometerMeasurements;
	private final Queue<GyroscopeMeasurement> collectedGyroscopeMeasurements;
	private final Queue<RfidTagDetectedNotification> collectedRfidTagDetectionNotifications;

	/**
	 * Constructor
	 */
	public SimulatedMotionDetectionUnit() {
		collectedAccelerometerMeasurements = new ConcurrentLinkedQueue<AccelerometerMeasurement>();
		collectedGyroscopeMeasurements = new ConcurrentLinkedQueue<GyroscopeMeasurement>();
		collectedRfidTagDetectionNotifications = new ConcurrentLinkedQueue<RfidTagDetectedNotification>();

		generateSamples();
	}

	/**
	 * Creates new samples to be reported
	 */
	private void generateSamples() {

		// Create sample data
		Calendar baseTime = Calendar.getInstance();
		collectedAccelerometerMeasurements
				.add(new AccelerometerMeasurement(new Acceleration(1.0, 1.0, 1.0), 0.0, baseTime));

		baseTime = (Calendar) baseTime.clone();
		baseTime.add(Calendar.SECOND, 1);
		collectedAccelerometerMeasurements
				.add(new AccelerometerMeasurement(new Acceleration(1.0, 1.0, 1.0), 0.0, baseTime));

		baseTime = (Calendar) baseTime.clone();
		baseTime.add(Calendar.SECOND, 1);
		collectedAccelerometerMeasurements
				.add(new AccelerometerMeasurement(new Acceleration(1.0, 1.0, 1.0), 0.0, baseTime));

		baseTime = (Calendar) baseTime.clone();
		baseTime.add(Calendar.SECOND, 1);
		collectedAccelerometerMeasurements
				.add(new AccelerometerMeasurement(new Acceleration(1.0, 1.0, 1.0), 0.0, baseTime));

		baseTime = (Calendar) baseTime.clone();
		baseTime.add(Calendar.SECOND, 1);
		collectedAccelerometerMeasurements
				.add(new AccelerometerMeasurement(new Acceleration(1.0, 1.0, 1.0), 0.0, baseTime));
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

}
