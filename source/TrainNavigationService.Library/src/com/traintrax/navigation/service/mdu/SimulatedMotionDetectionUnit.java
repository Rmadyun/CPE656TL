package com.traintrax.navigation.service.mdu;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import com.traintrax.navigation.service.testing.PositionTestSample;

/**
 * Class facilitates simulates collaboration with a Motion Detection Unit
 * 
 * @author Corey Sanders
 *
 */
public class SimulatedMotionDetectionUnit implements MotionDetectionUnitInterface {
	private final Lock queueLock = new ReentrantLock();
	private final Condition allMeasurementsRead = queueLock.newCondition();
	private final Condition newSample = queueLock.newCondition();
	private final Queue<AccelerometerMeasurement> collectedAccelerometerMeasurements;
	private final Queue<GyroscopeMeasurement> collectedGyroscopeMeasurements;
	private final Queue<RfidTagDetectedNotification> collectedRfidTagDetectionNotifications;

	private Boolean accDone = true;
	private Boolean gyrDone = true;
	private Boolean rfDone = true;
	private Boolean sampleReady = false;

	/**
	 * Constructor
	 */
	public SimulatedMotionDetectionUnit() {
		collectedAccelerometerMeasurements = new ConcurrentLinkedQueue<AccelerometerMeasurement>();
		collectedGyroscopeMeasurements = new ConcurrentLinkedQueue<GyroscopeMeasurement>();
		collectedRfidTagDetectionNotifications = new ConcurrentLinkedQueue<RfidTagDetectedNotification>();
	}

	/**
	 * Method is used to inject measurements into the simulated MDU so that they
	 * can be read by the Train Navigation Service. Method is setup to
	 * atomically enqueue all of the measurements in the sample. Assign null to
	 * measurements that do not have a value to include.
	 * 
	 * @param positionTestSamples
	 *            samples to enqueue to be read from the service.
	 */
	public void enqueueSamples(List<PositionTestSample> positionTestSamples) {

		queueLock.lock();
		try {
			while (!accDone && !gyrDone && !rfDone)

				try {
					allMeasurementsRead.await();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			for (PositionTestSample positionTestSample : positionTestSamples) {
				if (positionTestSample.getAccelerometerMeasurement() != null) {
					// Add Accelerometer Measurement
					collectedAccelerometerMeasurements.add(positionTestSample.getAccelerometerMeasurement());
				}

				if (positionTestSample.getGyroscopeMeasurement() != null) {
					// Add Gyroscope Measurement
					collectedGyroscopeMeasurements.add(positionTestSample.getGyroscopeMeasurement());
				}

				if (positionTestSample.getRfidTagDetectedNotification() != null) {
					// Add RFID Tag Detected Notification
					collectedRfidTagDetectionNotifications.add(positionTestSample.getRfidTagDetectedNotification());
				}
			}

			accDone = false;
			gyrDone = false;
			rfDone = false;
			sampleReady = true;
			newSample.signalAll();
		} finally {
			queueLock.unlock();
		}

	}

	/**
	 * 
	 * /** Read collected accelerometer measurements
	 * 
	 * @return Collected accelerometer measurements
	 */
	public List<AccelerometerMeasurement> readCollectedAccelerometerMeasurements() {

		List<AccelerometerMeasurement> collected = new LinkedList<AccelerometerMeasurement>();
		AccelerometerMeasurement measurement = null;

		queueLock.lock();

		try {
			while (!sampleReady)
				try {
					newSample.await();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			if (!collectedAccelerometerMeasurements.isEmpty()) {
				do {
					measurement = collectedAccelerometerMeasurements.poll();
					if (measurement != null) {
						collected.add(measurement);
					}
				} while (measurement != null);
			}
			accDone = true;
			allMeasurementsRead.signal();
		} finally {
			queueLock.unlock();
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

		queueLock.lock();
		try {
			while (!sampleReady)
				try {
					newSample.await();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			if (!collectedGyroscopeMeasurements.isEmpty()) {
				do {
					measurement = collectedGyroscopeMeasurements.poll();
					if (measurement != null) {
						collected.add(measurement);
					}
				} while (measurement != null);
			}
			gyrDone = true;
			allMeasurementsRead.signal();
		} finally {
			queueLock.unlock();
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

		queueLock.lock();
		try {
			while (!sampleReady)
				try {
					newSample.await();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			if (!collectedRfidTagDetectionNotifications.isEmpty()) {
				do {
					measurement = collectedRfidTagDetectionNotifications.poll();
					if (measurement != null) {
						collected.add(measurement);
					}
				} while (measurement != null);
			}

			rfDone = true;
			allMeasurementsRead.signal();

		} finally {
			queueLock.unlock();
		}

		return collected;
	}

}
