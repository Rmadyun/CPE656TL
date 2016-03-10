package com.traintrax.navigation.service.mdu;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import com.traintrax.navigation.service.position.Acceleration;
import com.traintrax.navigation.service.position.Coordinate;
import com.traintrax.navigation.service.position.ValueUpdate;
import com.traintrax.navigation.service.position.Velocity;
import com.traintrax.navigation.service.rotation.EulerAngleRotation;
import com.traintrax.navigation.service.rotation.Quat4d;

/**
 * Class is responsible for determining the position of a train based on the
 * latest measurements taken from the train.
 * 
 * @author Corey Sanders
 * 
 */
public class TrainPositionAlgorithm implements InertialMotionPositionAlgorithmInterface {

	private ValueUpdate<Coordinate> lastKnownTrainPosition;
	private ValueUpdate<Velocity> lastKnownTrainVelocity;
	private ValueUpdate<Acceleration> lastKnownTrainAcceleration;
	private ValueUpdate<EulerAngleRotation> lastKnownTrainOrientation;

	/**
	 * Constructor NOTE: Assuming that the train is a rest at initialization.
	 * 
	 * @param initialTrainPosition
	 *            Initial location of the train
	 * @param initialTrainOrientation
	 *            Degree of rotation necessary to align the current Motion
	 *            Detection Unit body coordinate frame axes with the test bed
	 *            coordinate frame axes.
	 */
	public TrainPositionAlgorithm(Coordinate initialTrainPosition, EulerAngleRotation initialTrainOrientation) {

		Calendar currentTime = Calendar.getInstance();
		lastKnownTrainPosition = new ValueUpdate<Coordinate>(initialTrainPosition, currentTime);
		lastKnownTrainOrientation = new ValueUpdate<EulerAngleRotation>(initialTrainOrientation, currentTime);
		lastKnownTrainVelocity = new ValueUpdate<Velocity>(new Velocity(0, 0, 0), currentTime);
		lastKnownTrainAcceleration = new ValueUpdate<Acceleration>(new Acceleration(0, 0, 0), currentTime);
	}

	/**
	 * Determines the current position of the train
	 * 
	 * @param gyroscopeMeasurementsSinceLastUpdate
	 *            New Gyroscope measurements
	 * @param accelerometerMeasurementsSinceLastUpdate
	 *            New Accelerometer measurements
	 * @param rfidTagDetectedEvents
	 *            New RFID tag detected events
	 * @return The estimated current position of the train.
	 */
	public ValueUpdate<Coordinate> calculatePosition(List<GyroscopeMeasurement> gyroscopeMeasurementsSinceLastUpdate,
			List<AccelerometerMeasurement> accelerometerMeasurementsSinceLastUpdate,
			List<ValueUpdate<Coordinate>> rfidTagDetectedLocations) {
		
		
		
		//Sort gyroscope measurements in increasing order by time
		if(gyroscopeMeasurementsSinceLastUpdate != null)
		{
		    Collections.sort(gyroscopeMeasurementsSinceLastUpdate);
		}
		
		//Sort accelerometer measurements in increasing order by time
		if(accelerometerMeasurementsSinceLastUpdate != null)
		{
		   Collections.sort(accelerometerMeasurementsSinceLastUpdate);
		}
		
		// TODO: Filter gyroscope values
		// TODO: Filter accelerometer values

		// Regular variables start here
		List<ValueUpdate<EulerAngleRotation>> orientationUpdates;

		if (gyroscopeMeasurementsSinceLastUpdate == null) {
			orientationUpdates = new ArrayList<ValueUpdate<EulerAngleRotation>>();

		} else {
			orientationUpdates = convertOrientationUpdate(lastKnownTrainOrientation,
					gyroscopeMeasurementsSinceLastUpdate);
		}

		ValueUpdate<Coordinate> initialPosition = lastKnownTrainPosition;
		ValueUpdate<Velocity> initialVelocity = lastKnownTrainVelocity;
		ValueUpdate<Acceleration> lastProcessedAcceleration = lastKnownTrainAcceleration;

		if (accelerometerMeasurementsSinceLastUpdate != null) {
			List<AccelerometerMeasurement> testbedCoordinateFrameAccelerometerMeasurements = adjustOrientation(
					accelerometerMeasurementsSinceLastUpdate, lastKnownTrainOrientation, orientationUpdates);
			for (AccelerometerMeasurement measurement : testbedCoordinateFrameAccelerometerMeasurements) {

				// Calculate velocity
				double velocityX = 0;
				double velocityY = 0;
				double velocityZ = 0;
				double deltaTimeInSeconds = (measurement.getTimeMeasured().getTimeInMillis()
						- lastProcessedAcceleration.getTimeObserved().getTimeInMillis()) / 1000.0;

				velocityX = measurement.getAccelerationMeasurement().getMetersPerSecondSquaredAlongXAxis()
						* deltaTimeInSeconds + initialVelocity.getValue().getMetersPerSecondAlongXAxis();
				velocityY = measurement.getAccelerationMeasurement().getMetersPerSecondSquaredAlongYAxis()
						* deltaTimeInSeconds + initialVelocity.getValue().getMetersPerSecondAlongYAxis();
				velocityZ = measurement.getAccelerationMeasurement().getMetersPerSecondSquaredAlongZAxis()
						* deltaTimeInSeconds + initialVelocity.getValue().getMetersPerSecondAlongZAxis();

				// Update the final velocity
				ValueUpdate<Velocity> finalVelocity = new ValueUpdate<Velocity>(
						new Velocity(velocityX, velocityY, velocityZ), measurement.getTimeMeasured());

				// Calculate position
				double displacementX = 0;
				double displacementY = 0;
				double displacementZ = 0;

				displacementX = finalVelocity.getValue().getMetersPerSecondAlongXAxis() * deltaTimeInSeconds
						+ initialPosition.getValue().getX();
				displacementY = finalVelocity.getValue().getMetersPerSecondAlongYAxis() * deltaTimeInSeconds
						+ initialPosition.getValue().getY();
				displacementZ = finalVelocity.getValue().getMetersPerSecondAlongZAxis() * deltaTimeInSeconds
						+ initialPosition.getValue().getZ();

				ValueUpdate<Coordinate> finalPosition = new ValueUpdate<Coordinate>(
						new Coordinate(displacementX, displacementY, displacementZ), measurement.getTimeMeasured());

				// Update the initialPosition
				initialPosition = finalPosition;

				// Update the initial velocity
				initialVelocity = finalVelocity;

				// Update the last
				// processeaccelerometerMeasurementsSinceLastUpdated
				// acceleration
				lastProcessedAcceleration = new ValueUpdate<Acceleration>(measurement.getAccelerationMeasurement(),
						measurement.getTimeMeasured());
			}
		}

		// Update the last known orientation

		if (orientationUpdates.size() > 0) {
			lastKnownTrainOrientation = orientationUpdates.get(orientationUpdates.size() - 1);
		}

		// Update last known position
		lastKnownTrainPosition = initialPosition;

		// Update last known velocity
		lastKnownTrainVelocity = initialVelocity;

		// Update last known acceleration
		lastKnownTrainAcceleration = lastProcessedAcceleration;

		return lastKnownTrainPosition;
	}

	private List<ValueUpdate<EulerAngleRotation>> convertOrientationUpdate(
			ValueUpdate<EulerAngleRotation> lastKnownOrientation, List<GyroscopeMeasurement> gyroscopeMeasurements) {
		List<ValueUpdate<EulerAngleRotation>> orientationUpdates = new ArrayList<ValueUpdate<EulerAngleRotation>>();

		ValueUpdate<EulerAngleRotation> currentOrientation = lastKnownOrientation;

		orientationUpdates.add(lastKnownOrientation);

		for (GyroscopeMeasurement gyroscopeMeasurement : gyroscopeMeasurements) {
			Quat4d currentOrientationQuat = RotationUtilities
					.convertFromEulerAngleToQuaternion(currentOrientation.getValue());
			Quat4d updatedOrientationQuat = RotationUtilities.calculateNedInertialFrameOrientation(gyroscopeMeasurement,
					currentOrientationQuat);
			EulerAngleRotation updatedOrientation = RotationUtilities
					.convertFromQuaternionToEulerAngle(updatedOrientationQuat);

			ValueUpdate<EulerAngleRotation> orientationUpdate = new ValueUpdate<EulerAngleRotation>(updatedOrientation,
					gyroscopeMeasurement.getTimeMeasured());
			orientationUpdates.add(orientationUpdate);
		}

		return orientationUpdates;
	}

	List<AccelerometerMeasurement> adjustOrientation(List<AccelerometerMeasurement> rawMeasurements,
			ValueUpdate<EulerAngleRotation> lastKnownOrientation,
			List<ValueUpdate<EulerAngleRotation>> orientationUpdates) {
		List<AccelerometerMeasurement> remainingMeasurements = new ArrayList<AccelerometerMeasurement>(rawMeasurements);
		List<AccelerometerMeasurement> adjustedVectors = new ArrayList<AccelerometerMeasurement>();
		int first = 0;
		int last = orientationUpdates.size();

		ValueUpdate<EulerAngleRotation> finalOrientation;
		
		if(orientationUpdates.isEmpty())
		{
			finalOrientation = lastKnownOrientation;
		}
		else{
			finalOrientation = orientationUpdates.get(last-1);
		}

		// NOTE: This implementation assumes that the orientationUpdates list is
		// sorted in
		// increasing order by time.e
		// Accelerometer measurements should also be sorted in increasing order
		// by time.

		for (int i = first; i < last; i++) {
			ValueUpdate<EulerAngleRotation> orientationUpdate = orientationUpdates.get(i);
			List<AccelerometerMeasurement> toRemove = new ArrayList<AccelerometerMeasurement>();

			// Process measurements

			for (AccelerometerMeasurement measurement : remainingMeasurements) {

				if (measurement.getTimeMeasured().getTimeInMillis() < lastKnownOrientation.getTimeObserved()
						.getTimeInMillis()) {
					// Last Known
					// This should only be called for the edge case of there
					// still being measurements
					// with the last known measurement.

					AccelerometerMeasurement adjustedMeasurement = RotationUtilities.changeToInertialFrame(measurement,
							finalOrientation.getValue());
					adjustedVectors.add(adjustedMeasurement);

					toRemove.add(measurement);
				} else if (measurement.getTimeMeasured().getTimeInMillis() >= lastKnownOrientation.getTimeObserved()
						.getTimeInMillis()
						&& measurement.getTimeMeasured().getTimeInMillis() < orientationUpdate.getTimeObserved()
								.getTimeInMillis()) {
					// Last Known
					// We assume that all accelerometer measurements occur at
					// the last known orientation
					// until we have an orientation update.

					AccelerometerMeasurement adjustedMeasurement = RotationUtilities.changeToInertialFrame(measurement,
							finalOrientation.getValue());
					adjustedVectors.add(adjustedMeasurement);

					toRemove.add(measurement);
				} else if (measurement.getTimeMeasured().getTimeInMillis() >= finalOrientation.getTimeObserved()
						.getTimeInMillis()) {

					// Final
					AccelerometerMeasurement adjustedMeasurement = RotationUtilities.changeToInertialFrame(measurement,
							finalOrientation.getValue());
					adjustedVectors.add(adjustedMeasurement);
					toRemove.add(measurement);
				}
			}

			// Remove measurements that have been processed
			for (AccelerometerMeasurement measurement : toRemove) {

				remainingMeasurements.remove(measurement);
			}

			// Update the Last Known orientation
			lastKnownOrientation = orientationUpdate;

		}

		if (adjustedVectors.size() != rawMeasurements.size()) {
			// TODO: Raise some kind index of error.
			// Something is wrong with implementation
		}
		
		Collections.sort(adjustedVectors);

		return adjustedVectors;
	}

}
