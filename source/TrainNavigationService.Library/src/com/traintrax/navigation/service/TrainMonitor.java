package com.traintrax.navigation.service;

import java.util.LinkedList;
import java.util.List;

import com.traintrax.navigation.service.position.AccelerometerMeasurement;
import com.traintrax.navigation.service.position.Coordinate;
import com.traintrax.navigation.service.position.GyroscopeMeasurement;
import com.traintrax.navigation.service.position.InertialMotionPositionAlgorithmInterface;
import com.traintrax.navigation.service.position.RfidTagDetectedNotification;
import com.traintrax.navigation.service.position.Train;
import com.traintrax.navigation.service.position.TrainPositionEstimate;
import com.traintrax.navigation.service.position.UnitConversionUtilities;
import com.traintrax.navigation.service.position.Velocity;
import com.traintrax.navigation.service.math.*;

/**
 * Class is responsible for observing changes to a train that belongs to the
 * Positive Train Control Test Bed.
 * 
 * @author Corey Sanders
 * 
 */
public class TrainMonitor implements TrainMonitorInterface {

	private TrainPositionEstimate lastKnownTrainPosition;
	private final InertialMotionPositionAlgorithmInterface positionAlgorithm;
	private final Train train; // The latest info about the train being
								// monitored
	private final TrainNavigationDatabaseInterface trainNavigationDatabase;
	private final boolean useRfidTagsOnly;

	/**
	 * Constructor
	 * 
	 * @param train
	 *            Provides measurements from the train
	 * @param positionAlgorithm
	 *            Calculates train position from measurements
	 * @param trainNavigationDatabase
	 *            Stores position and measurements. Provides RFID tag position
	 *            information NOTE: Assuming that the train is a rest at
	 *            initialization.
	 * @param useRfidTagsOnly indicates that IMU measurements should be ignored and only
	 * RFID tags should be used.
	 */
	public TrainMonitor(Train train, InertialMotionPositionAlgorithmInterface positionAlgorithm,
			TrainNavigationDatabaseInterface trainNavigationDatabase, boolean useRfidTagsOnly) {
		this.train = train;
		this.positionAlgorithm = positionAlgorithm;
		this.trainNavigationDatabase = trainNavigationDatabase;
		this.useRfidTagsOnly = useRfidTagsOnly;
		lastKnownTrainPosition = calculatePosition(null, null, null);
	}

	/**
	 * Method determines an estimate on the position of the train based on IMU
	 * measurements
	 * 
	 * @param gyroscopeMeasurementsSinceLastUpdate
	 *            New Gyroscope measurements from the object (rad/s)
	 * @param accelerometerMeasurementsSinceLastUpdate
	 *            New Accelerometer measurements from the object (m/s^2)
	 * @param positionUpdates
	 *            Reported updates in the position of the object. This should be
	 *            the distance from the origin in inches (in inches)
	 * @return Calculated position of the train
	 */
	private TrainPositionEstimate calculatePosition(List<GyroscopeMeasurement> gyroscopeMeasurementsSinceLastUpdate,
			List<AccelerometerMeasurement> accelerometerMeasurementsSinceLastUpdate,
			List<ValueUpdate<Coordinate>> positionUpdate) {

		ValueUpdate<Tuple<Coordinate, Velocity>> latestPositionUpdate = positionAlgorithm.calculatePosition(
				gyroscopeMeasurementsSinceLastUpdate, accelerometerMeasurementsSinceLastUpdate, positionUpdate);

		Coordinate positionInInches = UnitConversionUtilities
				.convertFromMetersToInches(latestPositionUpdate.getValue().getItem1());
		// NOTE: a three dimensional space vector allows a generic way to
		// express any vector including velocity.
		// The velocity class was not used here because it is specifically
		// intended to store measurements in meters per second right now.
		// An improvement would be to extend the class so that it can return its
		// values with other units than meters/per second.
		ThreeDimensionalSpaceVector velocityInInches = UnitConversionUtilities.convertFromMetersToInches(
				Velocity.ToThreeDimensionalSpaceVector(latestPositionUpdate.getValue().getItem2()));

		return new TrainPositionEstimate(positionInInches, velocityInInches, latestPositionUpdate.getTimeObserved(),
				train.getTrainId());
	}

	/**
	 * Tries to retrieve another update on the target train's position.
	 * 
	 * @return Most recent update on the position of the train if successful;
	 *         Otherwise returns null.
	 */
	public TrainPositionEstimate tryFetchNextPositionUpdate() {
		List<GyroscopeMeasurement> newGyroscopeMeasurements;
		List<AccelerometerMeasurement> newAccelerometerMeasurements;
		List<RfidTagDetectedNotification> newRfidTagEvents;
		boolean positionUpdateAvailable = false;

		newGyroscopeMeasurements = train.readCollectedGyroscopeMeasurements();
		newAccelerometerMeasurements = train.readCollectedAccelerometerMeasurements();
		newRfidTagEvents = train.readCollectedRfidTagDetectionNotifications();

		if ((newGyroscopeMeasurements.size() == 0) && (newAccelerometerMeasurements.size() == 0)
				&& (newRfidTagEvents.size() == 0)) {
			// Wait if nothing is available
			positionUpdateAvailable = false;
		} else {
			positionUpdateAvailable = true;
		}

		TrainPositionEstimate latestPositionUpdate = null;

		if (positionUpdateAvailable) {

			List<ValueUpdate<Coordinate>> positionUpdates = new LinkedList<ValueUpdate<Coordinate>>();

			// Lookup RFID Tag position
			for (RfidTagDetectedNotification notification : newRfidTagEvents) {

				Coordinate tagPosition = trainNavigationDatabase
						.findTrackMarkerPosition(notification.getRfidTagValue());

				if (tagPosition != null) {
					ValueUpdate<Coordinate> positionUpdate = new ValueUpdate<>(tagPosition,
							notification.getTimeDetected());

					positionUpdates.add(positionUpdate);
				}
			}

			if(useRfidTagsOnly)
			{
			    latestPositionUpdate = calculatePosition(new LinkedList<GyroscopeMeasurement>(),
			    		new LinkedList<AccelerometerMeasurement>(),
					positionUpdates);
			}
			else{
			    latestPositionUpdate = calculatePosition(newGyroscopeMeasurements, newAccelerometerMeasurements,
					positionUpdates);
			}

			if (positionAlgorithm.isInitialPositionFound()) {

				this.lastKnownTrainPosition = latestPositionUpdate;
				// Save collected information
				trainNavigationDatabase.save(latestPositionUpdate);
			} else {
				// Don't report position yet.
				latestPositionUpdate = null;
			}

			for (GyroscopeMeasurement measurement : newGyroscopeMeasurements) {
				trainNavigationDatabase.save(measurement);
			}

			for (AccelerometerMeasurement measurement : newAccelerometerMeasurements) {
				trainNavigationDatabase.save(measurement);
			}

			for (RfidTagDetectedNotification notification : newRfidTagEvents) {
				trainNavigationDatabase.save(notification);
			}
		}

		return latestPositionUpdate;
	}

	/**
	 * Retrieves the unique identifier for the target train
	 * 
	 * @return Unique identifier for the target train
	 */
	public String getTrainId() {
		return train.getTrainId();
	}

	/**
	 * Retrieves the last known position of the target train
	 * 
	 * @return The last known position of the target train.
	 */
	public TrainPositionEstimate getLastKnownPosition() {
		return lastKnownTrainPosition;
	}

}
