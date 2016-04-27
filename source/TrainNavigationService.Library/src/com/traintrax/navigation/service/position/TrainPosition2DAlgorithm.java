package com.traintrax.navigation.service.position;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.traintrax.navigation.service.ValueUpdate;
import com.traintrax.navigation.service.math.ThreeDimensionalSpaceVector;
import com.traintrax.navigation.service.math.Triplet;
import com.traintrax.navigation.service.math.Tuple;
import com.traintrax.navigation.service.position.Coordinate;
import com.traintrax.navigation.service.rotation.EulerAngleRotation;

/**
 * Class is responsible for determining the position of a train based on the
 * latest measurements taken from the train.
 * 
 * @author Corey Sanders
 * 
 */
public class TrainPosition2DAlgorithm implements InertialMotionPositionAlgorithmInterface {

	/**
	 * Constant to use to correct acceleration measurements to account for the
	 * amount of force necessary to offset kinetic friction of a moving vehicle.
	 */

	// Assuming that train is initially at rest at the start of time.
	private static final double kineticFrictionOffset = 0.35;

	private final ThresholdFilter thresholdFilter = new ThresholdFilter(kineticFrictionOffset, 0);
	private final AccelerometerCalibrationFilter accCalibrationFilter = new AccelerometerCalibrationFilter();
	private final GyroscopeCalibrationFilter gyrCalibrationFilter = new GyroscopeCalibrationFilter();

	private ValueUpdate<Coordinate> lastKnownTrainPosition;
	private ValueUpdate<Velocity> lastKnownTrainVelocity;
	private ValueUpdate<Acceleration> lastKnownTrainAcceleration;
	private ValueUpdate<EulerAngleRotation> lastKnownTrainOrientation;
	private ValueUpdate<ThreeDimensionalSpaceVector> lastKnownAngularVelocity;

	private final ImuCalibrator imuCalibrator = new ImuCalibrator();

	private final DiscretePositionCalculator discretePositionCalculator = new DiscretePositionCalculator();

	private RfidTagPositionResults lastRfidTagPositionResults = null;

	// TODO: Incorporate Last IMU Position Results
	private ImuPositionResults lastImuPositionResults = null;

	// Flag that is being used to determine if the initial position has been
	// found from the
	// measurements. This is intended to determine when values reported by the
	// algorithm can
	// be trusted.
	private boolean initialPositionFound;

	/**
	 * Constructor NOTE: Assuming that the train is a rest at initialization.
	 * 
	 * @param initialTrainPosition
	 *            Initial location of the train (distance of each axes from the origin; measured in meters)
	 * @param initialTrainOrientation
	 *            Degree of rotation necessary to align the current Motion
	 *            Detection Unit body coordinate frame axes with the test bed
	 *            coordinate frame axes. (measured in radians)
	 * @param initialTrainVelocity Initial rate of movement of the train along each axes (measured in meters per second)
	 */
	public TrainPosition2DAlgorithm(Coordinate initialTrainPosition, EulerAngleRotation initialTrainOrientation, Velocity initialVelocity) {

		Calendar currentTime = Calendar.getInstance();
		lastKnownTrainPosition = new ValueUpdate<Coordinate>(initialTrainPosition, currentTime);
		lastKnownTrainOrientation = new ValueUpdate<EulerAngleRotation>(initialTrainOrientation, currentTime);
		lastKnownTrainVelocity = new ValueUpdate<Velocity>(initialVelocity, currentTime);
		lastKnownTrainAcceleration = new ValueUpdate<Acceleration>(new Acceleration(0, 0, 0), currentTime);
		lastKnownAngularVelocity = new ValueUpdate<ThreeDimensionalSpaceVector>(
				new ThreeDimensionalSpaceVector(0, 0, 0), currentTime);

		// Creating a fake measurement to initiate position calculations
		lastRfidTagPositionResults = new RfidTagPositionResults();

		lastRfidTagPositionResults.setLastKnownTrainOrientation(lastKnownTrainOrientation);
		lastRfidTagPositionResults.setLastKnownTrainVelocity(lastKnownTrainVelocity);
		lastRfidTagPositionResults.setLastKnownTrainPosition(lastKnownTrainPosition);

	}

	/**
	 * Determines the current position of the train
	 * 
	 * @param gyroscopeMeasurementsSinceLastUpdate
	 *            New Gyroscope measurements (rad/s)
	 * @param accelerometerMeasurementsSinceLastUpdate
	 *            New Accelerometer measurements (m/s^2)
	 * @param rfidTagDetectedEvents
	 *            New RFID tag detected events (location in meters)
	 * @return The estimated current position (in meters) and velocity (in m/s)
	 *         of the train.
	 */
	public ValueUpdate<Tuple<Coordinate, Velocity>> calculatePosition(
			List<GyroscopeMeasurement> gyroscopeMeasurementsSinceLastUpdate,
			List<AccelerometerMeasurement> accelerometerMeasurementsSinceLastUpdate,
			List<ValueUpdate<Coordinate>> rfidTagDetectedLocations) {

		if (gyroscopeMeasurementsSinceLastUpdate == null && accelerometerMeasurementsSinceLastUpdate == null
				&& rfidTagDetectedLocations == null) {
			return new ValueUpdate<Tuple<Coordinate, Velocity>>(
					new Tuple<Coordinate, Velocity>(this.lastKnownTrainPosition.getValue(),
							this.lastKnownTrainVelocity.getValue()),
					this.lastKnownTrainPosition.getTimeObserved());
		}

		// Sort gyroscope measurements in increasing order by time
		if (gyroscopeMeasurementsSinceLastUpdate != null) {
			Collections.sort(gyroscopeMeasurementsSinceLastUpdate);
		}

		// Sort accelerometer measurements in increasing order by time
		if (accelerometerMeasurementsSinceLastUpdate != null) {
			Collections.sort(accelerometerMeasurementsSinceLastUpdate);
		}

		if (imuCalibrator.isCalibrationComplete()) {
			// Filtering
			for (int i = 0; i < gyroscopeMeasurementsSinceLastUpdate.size(); i++) {

				// retrieve original value
				GyroscopeMeasurement gyroscopeMeasurement = gyroscopeMeasurementsSinceLastUpdate.get(i);

				// filter value
				gyroscopeMeasurement = gyrCalibrationFilter.filter(gyroscopeMeasurement);

				// replace with filtered value
				gyroscopeMeasurementsSinceLastUpdate.set(i, gyroscopeMeasurement);
			}

			for (int i = 0; i < accelerometerMeasurementsSinceLastUpdate.size(); i++) {

				// retrieve original value
				AccelerometerMeasurement accelerometerMeasurement = accelerometerMeasurementsSinceLastUpdate.get(i);

				// filter value
				accelerometerMeasurement = accCalibrationFilter.filter(accelerometerMeasurement);

				// replace with filtered value
				accelerometerMeasurementsSinceLastUpdate.set(i, accelerometerMeasurement);
			}
		} else if (gyroscopeMeasurementsSinceLastUpdate != null && accelerometerMeasurementsSinceLastUpdate != null) {

			// Calibration
			for (GyroscopeMeasurement m : gyroscopeMeasurementsSinceLastUpdate) {
				imuCalibrator.addMeasurement(m);
			}

			for (AccelerometerMeasurement m : accelerometerMeasurementsSinceLastUpdate) {
				imuCalibrator.addMeasurement(m);
			}

			if (imuCalibrator.isCalibrationComplete()) {
				// Assign calibration values
				accCalibrationFilter.setXOffset(imuCalibrator.getAccXOffset());
				accCalibrationFilter.setYOffset(imuCalibrator.getAccYOffset());
				accCalibrationFilter.setZOffset(imuCalibrator.getAccZOffset());
				gyrCalibrationFilter.setXOffset(imuCalibrator.getGyrXOffset());
				gyrCalibrationFilter.setYOffset(imuCalibrator.getGyrYOffset());
				gyrCalibrationFilter.setZOffset(imuCalibrator.getGyrZOffset());
			}
		}

		ValueUpdate<Coordinate> startPosition = lastKnownTrainPosition;
		ValueUpdate<Coordinate> endPosition = lastKnownTrainPosition;

		if (rfidTagDetectedLocations.size() > 0) {
			int lastIndex = rfidTagDetectedLocations.size() - 1;

			if (lastIndex == 0) {
				startPosition = this.lastRfidTagPositionResults.getLastKnownTrainPosition();
				endPosition = rfidTagDetectedLocations.get(lastIndex);
			} else {
				startPosition = rfidTagDetectedLocations.get(lastIndex - 1);
				endPosition = rfidTagDetectedLocations.get(lastIndex);
			}

			for (ValueUpdate<Coordinate> rfidTagDetectedLocation : rfidTagDetectedLocations) {
				Tuple<EulerAngleRotation, Velocity> trainStateInfo = discretePositionCalculator
						.updatePosition(rfidTagDetectedLocation);
				if (trainStateInfo != null) {

					if (!initialPositionFound) {
						// Initial Position Found

						// Reset known orientation, position, velocity

						this.lastKnownTrainPosition = rfidTagDetectedLocation;
						this.lastKnownTrainOrientation = new ValueUpdate<EulerAngleRotation>(trainStateInfo.getItem1(),
								rfidTagDetectedLocation.getTimeObserved());
						this.lastKnownTrainVelocity = new ValueUpdate<Velocity>(trainStateInfo.getItem2(),
								rfidTagDetectedLocation.getTimeObserved());
					}

					initialPositionFound = true;
				}
			}

			// Use RFID Tag Plus IMU

			// Create RFID Tag Position Results from the last info in the
			// Discrete position calculator
			// If null, use the last known position information.

			// RfidTagPositionResults rfidTagPositionResults =
			// calculationPositionFromRfidTagUpdates(startPosition,
			// endPosition, this.lastKnownTrainOrientation.getValue());
			ValueUpdate<Triplet<Coordinate, EulerAngleRotation, Velocity>> lastEstimatedPosition = this.discretePositionCalculator
					.getLastReliableTrainPositionEstimate();
			RfidTagPositionResults rfidTagPositionResults = new RfidTagPositionResults();

			if (lastEstimatedPosition == null) {
				double dt = (endPosition.getTimeObserved().getTimeInMillis()
						- lastKnownAngularVelocity.getTimeObserved().getTimeInMillis()) / 1000.0;

				double yaw = (lastKnownAngularVelocity.getValue().getZ() * dt)
						+ lastKnownTrainOrientation.getValue().getRadiansRotationAlongZAxis();

				ValueUpdate<EulerAngleRotation> finalOrientation = new ValueUpdate<EulerAngleRotation>(
						new EulerAngleRotation(0, 0, yaw), endPosition.getTimeObserved());

				if (finalOrientation.getValue().getRadiansRotationAlongZAxis() == 4) {

					System.out.println("Weird Value Found!");
				}
				ValueUpdate<Velocity> velocity = DiscretePositionCalculator.calculateVelocity(startPosition,
						endPosition, finalOrientation.getValue());

				// rfidTagPositionResults.setLastKnownTrainOrientation(lastKnownTrainOrientation);
				// rfidTagPositionResults.setLastKnownTrainPosition(endPosition);
				// rfidTagPositionResults.setLastKnownTrainVelocity(lastKnownTrainVelocity);

				// rfidTagPositionResults.setLastKnownTrainVelocity(DiscretePositionCalculator.calculateVelocity(startPosition,
				// endPosition, lastKnownTrainOrientation.getValue()));
				// rfidTagPositionResults =
				// calculationPositionFromRfidTagUpdates(startPosition,
				// endPosition, lastKnownTrainOrientation.getValue());

				rfidTagPositionResults.setLastKnownTrainOrientation(finalOrientation);
				rfidTagPositionResults.setLastKnownTrainPosition(endPosition);

				// Disabling and using the last known train velocity instead.
				// Reason: Though the RFID positioning at this point can give us
				// some
				// Truth for speed, it will not be correct if the train is
				// moving in
				// an arc or circle.
				// It'll be a chord of the distance. This is fine if we have
				// confirmed position truth between the two points, but not
				// otherwise.
				// (i.e. use RFID Tag velocity calculation iff orientation is
				// stable)
				// rfidTagPositionResults.setLastKnownTrainVelocity(velocity);

				// Recalculate velocity according the lastKnown velocity and the
				// final orientation.
				double speed = DiscretePositionCalculator.calculateSpeed(lastKnownTrainVelocity);

				double vX = speed * Math.cos(finalOrientation.getValue().getRadiansRotationAlongZAxis());
				double vY = speed * Math.sin(finalOrientation.getValue().getRadiansRotationAlongZAxis());

				if (speed > 0.05) // Minimum that the train needs to be moving
									// is 2 inches / second before we can
									// believe this.
				{

					velocity = new ValueUpdate<Velocity>(new Velocity(vX, vY, 0), endPosition.getTimeObserved());
				}

				rfidTagPositionResults.setLastKnownTrainVelocity(velocity);

				// Go ahead and assign the last known orientation so that the
				// IMU can use this guess.

				lastKnownTrainOrientation = rfidTagPositionResults.getLastKnownTrainOrientation();

			} else {
				rfidTagPositionResults.setLastKnownTrainPosition(new ValueUpdate<Coordinate>(
						lastEstimatedPosition.getValue().getItem1(), lastEstimatedPosition.getTimeObserved()));
				rfidTagPositionResults.setLastKnownTrainOrientation(new ValueUpdate<EulerAngleRotation>(
						lastEstimatedPosition.getValue().getItem2(), lastEstimatedPosition.getTimeObserved()));
				rfidTagPositionResults.setLastKnownTrainVelocity(new ValueUpdate<Velocity>(
						lastEstimatedPosition.getValue().getItem3(), lastEstimatedPosition.getTimeObserved()));
			}

			ImuPositionResults imuPositionResults = null;

			// Update the last RFID Tag position results
			lastRfidTagPositionResults = rfidTagPositionResults;

			if (imuCalibrator.isCalibrationComplete() && (accelerometerMeasurementsSinceLastUpdate.size() > 0)) {
				// Include IMU measurements

				List<GyroscopeMeasurement> postRfidTagGyroscopeMeasurements;

				// In order to prevent IMU measurements from happening on the
				// edge case of where IMU measurements occur exactly when
				// an RFID Tag event is receive, the starting point of searching
				// for points
				// will be 1 millisecond after the last RFID tag received.

				Calendar imuMeasurementStart = (Calendar) endPosition.getTimeObserved().clone();
				imuMeasurementStart.add(Calendar.MILLISECOND, 1);

				if (gyroscopeMeasurementsSinceLastUpdate.size() > 0) {
					postRfidTagGyroscopeMeasurements = selectGyroscopeMeasurementsByTime(
							gyroscopeMeasurementsSinceLastUpdate, imuMeasurementStart,
							gyroscopeMeasurementsSinceLastUpdate.get(gyroscopeMeasurementsSinceLastUpdate.size() - 1)
									.getTimeMeasured());
				} else {
					postRfidTagGyroscopeMeasurements = new LinkedList<GyroscopeMeasurement>();
				}

				List<AccelerometerMeasurement> postRfidTagAccelerometerMeasurements = selectAccelerometerMeasurementsByTime(
						accelerometerMeasurementsSinceLastUpdate, imuMeasurementStart,
						accelerometerMeasurementsSinceLastUpdate
								.get(accelerometerMeasurementsSinceLastUpdate.size() - 1).getTimeMeasured());

				List<AccelerometerMeasurement> atRfidTagAccelerometerMeasurements = selectAccelerometerMeasurementsByTime(
						accelerometerMeasurementsSinceLastUpdate, endPosition.getTimeObserved(),
						endPosition.getTimeObserved());

				// Update the last known acceleration to be the one at the RFID
				// tag measurement if available.
				for (AccelerometerMeasurement a : atRfidTagAccelerometerMeasurements) {

					AccelerometerMeasurement adjustedMeasurement = changetoInertialFrame2d(a,
							lastKnownTrainOrientation.getValue(), thresholdFilter);

					this.lastKnownTrainAcceleration = new ValueUpdate<Acceleration>(
							adjustedMeasurement.getAccelerationMeasurement(), adjustedMeasurement.getTimeMeasured());
				}

				imuPositionResults = calculatePositionFromImu(rfidTagPositionResults.getLastKnownTrainPosition(),
						rfidTagPositionResults.getLastKnownTrainOrientation(),
						rfidTagPositionResults.getLastKnownTrainVelocity(), lastKnownTrainAcceleration,
						lastKnownAngularVelocity, postRfidTagGyroscopeMeasurements,
						postRfidTagAccelerometerMeasurements);

				// Save calculated values for future calculations

				lastKnownTrainPosition = imuPositionResults.getLastKnownTrainPosition();
				lastKnownTrainOrientation = imuPositionResults.getLastKnownTrainOrientation();
				lastKnownTrainVelocity = imuPositionResults.getLastKnownTrainVelocity();
				lastKnownTrainAcceleration = imuPositionResults.getLastKnownTrainAcceleration();
				lastKnownAngularVelocity = imuPositionResults.getLastAngularVelocity();

				lastImuPositionResults = imuPositionResults;

				if (lastKnownTrainOrientation.getValue().getRadiansRotationAlongZAxis() == 5) {

					System.out.println("Weird Value Found!");
				}

			} else {
				// Use RFID tag measurements only

				if (!lastKnownTrainPosition.getValue()
						.equals(rfidTagPositionResults.getLastKnownTrainPosition().getValue())) {

					// Save calculated values for future calculations
					lastKnownTrainPosition = rfidTagPositionResults.getLastKnownTrainPosition();
					lastKnownTrainOrientation = rfidTagPositionResults.getLastKnownTrainOrientation();
					lastKnownTrainVelocity = rfidTagPositionResults.getLastKnownTrainVelocity();

					if (lastKnownTrainOrientation.getValue().getRadiansRotationAlongZAxis() == 5) {

						System.out.println("Weird Value Found!");
					}

					/*
					 * //This is good for a velocity only correction
					 * //Recommended for this to be used when RFID tag
					 * orientation information is not good enough. //Calculated
					 * velocity based on the last calculated train velocity
					 * ValueUpdate<Velocity> rfidTagVelocityUpdate =
					 * rfidTagPositionResults.getLastKnownTrainVelocity();
					 * double speed =
					 * Math.sqrt((rfidTagVelocityUpdate.getValue().
					 * getMetersPerSecondAlongXAxis()*rfidTagVelocityUpdate.
					 * getValue().getMetersPerSecondAlongXAxis()) +
					 * (rfidTagVelocityUpdate.getValue().
					 * getMetersPerSecondAlongYAxis()*rfidTagVelocityUpdate.
					 * getValue().getMetersPerSecondAlongYAxis()));
					 * 
					 * double velocityX =
					 * Math.cos(lastKnownTrainOrientation.getValue().
					 * getRadiansRotationAlongZAxis())*speed; double velocityY =
					 * Math.sin(lastKnownTrainOrientation.getValue().
					 * getRadiansRotationAlongZAxis())*speed;
					 * 
					 * lastKnownTrainVelocity = new ValueUpdate<Velocity>(new
					 * Velocity(velocityX, velocityY, 0),
					 * rfidTagVelocityUpdate.getTimeObserved());
					 */
				}
			}

		} else if (imuCalibrator.isCalibrationComplete()) {

			if (gyroscopeMeasurementsSinceLastUpdate.size() > 0) {
				System.out.println("IMU Measurements available");
			}

			if (lastKnownTrainOrientation.getValue().getRadiansRotationAlongZAxis() == 4) {

				System.out.println("Weird Value Found!");
			}

			// Use IMU data Only
			ImuPositionResults imuPositionResults = calculatePositionFromImu(lastKnownTrainPosition,
					lastKnownTrainOrientation, lastKnownTrainVelocity, lastKnownTrainAcceleration,
					lastKnownAngularVelocity, gyroscopeMeasurementsSinceLastUpdate,
					accelerometerMeasurementsSinceLastUpdate);

			// Save calculated values for future calculations
			lastKnownTrainPosition = imuPositionResults.getLastKnownTrainPosition();
			lastKnownTrainOrientation = imuPositionResults.getLastKnownTrainOrientation();
			lastKnownTrainVelocity = imuPositionResults.getLastKnownTrainVelocity();
			lastKnownTrainAcceleration = imuPositionResults.getLastKnownTrainAcceleration();
			lastKnownAngularVelocity = imuPositionResults.getLastAngularVelocity();

			lastImuPositionResults = imuPositionResults;
		} else { // imuCalibration is NOT complete

			if (accelerometerMeasurementsSinceLastUpdate.size() > 0) {
				Calendar newTimestamp = accelerometerMeasurementsSinceLastUpdate
						.get(accelerometerMeasurementsSinceLastUpdate.size() - 1).getTimeMeasured();

				// Replacing timestamp otherwise assuming that train is at rest
				// since calibration is still in progress
				lastKnownTrainPosition = new ValueUpdate<Coordinate>(lastKnownTrainPosition.getValue(), newTimestamp);
				lastKnownTrainOrientation = new ValueUpdate<EulerAngleRotation>(lastKnownTrainOrientation.getValue(),
						newTimestamp);
				lastKnownTrainVelocity = new ValueUpdate<Velocity>(lastKnownTrainVelocity.getValue(), newTimestamp);
				lastKnownTrainAcceleration = new ValueUpdate<Acceleration>(lastKnownTrainAcceleration.getValue(),
						newTimestamp);
				lastKnownAngularVelocity = new ValueUpdate<ThreeDimensionalSpaceVector>(
						lastKnownAngularVelocity.getValue(), newTimestamp);
			}
		}

		System.out.println(String.format("orientation: %f rads\n",
				lastKnownTrainOrientation.getValue().getRadiansRotationAlongZAxis()));

		return new ValueUpdate<Tuple<Coordinate, Velocity>>(
				new Tuple<Coordinate, Velocity>(lastKnownTrainPosition.getValue(), lastKnownTrainVelocity.getValue()),
				lastKnownTrainPosition.getTimeObserved());
	}

	/**
	 * Determines the current position of the train from RFID tag measurements
	 * This implementation is assuming a 2-dimensional train track where the NED
	 * frame is used, but there are not any elevation changes on the track (i.e.
	 * the track is flat) and on a level-table so that gravity is only acting on
	 * the Z-axis.
	 * 
	 * @param initialPosition
	 *            Initial position of the train
	 * @param finalPosition
	 *            Final position of the train
	 * @return Detailed information about the position of the train
	 */
	private RfidTagPositionResults calculationPositionFromRfidTagUpdates(ValueUpdate<Coordinate> initialPosition,
			ValueUpdate<Coordinate> finalPosition, EulerAngleRotation initialOrientation) {

		RfidTagPositionResults rfidTagPositionResults = new RfidTagPositionResults();

		double dx = finalPosition.getValue().getX() - initialPosition.getValue().getX();
		double dy = finalPosition.getValue().getY() - initialPosition.getValue().getY();
		double distance = Math.sqrt(dx * dx + dy * dy);
		double dt = (finalPosition.getTimeObserved().getTimeInMillis()
				- initialPosition.getTimeObserved().getTimeInMillis()) / 1000.0;
		double yaw = initialOrientation.getRadiansRotationAlongZAxis();

		if (distance != 0) {
			// Formula from the following:
			// https://en.wikipedia.org/wiki/Unit_circle
			// http://stackoverflow.com/questions/7586063/how-to-calculate-the-angle-between-a-line-and-the-horizontal-axis
			yaw = Math.atan2(dy, dx);
		}

		ValueUpdate<EulerAngleRotation> orientationUpdate = new ValueUpdate<EulerAngleRotation>(
				new EulerAngleRotation(0, 0, yaw), finalPosition.getTimeObserved());
		ValueUpdate<Velocity> velocityUpdate = new ValueUpdate<Velocity>(new Velocity(dx / dt, dy / dt, 0),
				finalPosition.getTimeObserved());

		rfidTagPositionResults.setLastKnownTrainVelocity(velocityUpdate);
		rfidTagPositionResults.setLastKnownTrainOrientation(orientationUpdate);
		rfidTagPositionResults.setLastKnownTrainPosition(finalPosition);

		return rfidTagPositionResults;
	}

	/**
	 * Method selects a subset of measurements based on time.
	 * 
	 * @param measurements
	 *            List of measurements that have been pre-sorted by time to
	 *            search through to find the desired measurements.
	 * @param startTime
	 *            Beginning of the time range to select measurements
	 * @param endTime
	 *            End of the time range to select measurements
	 * @return Subset of measurements that match the time range requested sorted
	 *         by time.
	 */
	private List<AccelerometerMeasurement> selectAccelerometerMeasurementsByTime(
			List<AccelerometerMeasurement> measurements, Calendar startTime, Calendar endTime) {

		List<AccelerometerMeasurement> selectedMeasurements = new LinkedList<AccelerometerMeasurement>();

		for (AccelerometerMeasurement measurement : measurements) {

			if (measurement.getTimeMeasured().getTimeInMillis() >= startTime.getTimeInMillis()
					&& measurement.getTimeMeasured().getTimeInMillis() <= endTime.getTimeInMillis()) {

				selectedMeasurements.add(measurement);
			}
		}

		return selectedMeasurements;
	}

	/**
	 * Method selects a subset of measurements based on time.
	 * 
	 * @param measurements
	 *            List of measurements that have been pre-sorted by time to
	 *            search through to find the desired measurements.
	 * @param startTime
	 *            Beginning of the time range to select measurements
	 * @param endTime
	 *            End of the time range to select measurements
	 * @return Subset of measurements that match the time range requested sorted
	 *         by time.
	 */
	private List<GyroscopeMeasurement> selectGyroscopeMeasurementsByTime(List<GyroscopeMeasurement> measurements,
			Calendar startTime, Calendar endTime) {

		List<GyroscopeMeasurement> selectedMeasurements = new LinkedList<GyroscopeMeasurement>();

		for (GyroscopeMeasurement measurement : measurements) {

			if (measurement.getTimeMeasured().getTimeInMillis() >= startTime.getTimeInMillis()
					&& measurement.getTimeMeasured().getTimeInMillis() <= endTime.getTimeInMillis()) {

				selectedMeasurements.add(measurement);
			}
		}

		return selectedMeasurements;
	}

	/**
	 * Determines the current position of the train
	 * 
	 * @param initialPosition
	 *            Initial position of the train
	 * @param initialOrientation
	 *            Initial orientation of the train
	 * @param initialVelocity
	 *            Initial velocity of the train
	 * @param lastProcessedAcceleration
	 *            Last measured acceleration of the train (NED frame)
	 * 
	 * @param gyroscopeMeasurementsSinceLastUpdate
	 *            New Gyroscope measurements
	 * @param accelerometerMeasurementsSinceLastUpdate
	 *            New Accelerometer measurements
	 * @return The estimated current position of the train.
	 */
	private ImuPositionResults calculatePositionFromImu(ValueUpdate<Coordinate> initialPosition,
			ValueUpdate<EulerAngleRotation> initialOrientation, ValueUpdate<Velocity> initialVelocity,
			ValueUpdate<Acceleration> lastProcessedAcceleration,
			ValueUpdate<ThreeDimensionalSpaceVector> lastProcessedAngularVelocity,
			List<GyroscopeMeasurement> gyroscopeMeasurementsSinceLastUpdate,
			List<AccelerometerMeasurement> accelerometerMeasurementsSinceLastUpdate) {

		ImuPositionResults positionResults = new ImuPositionResults();

		// Sort gyroscope measurements in increasing order by time
		if (gyroscopeMeasurementsSinceLastUpdate != null) {
			Collections.sort(gyroscopeMeasurementsSinceLastUpdate);
		}

		// Sort accelerometer measurements in increasing order by time
		if (accelerometerMeasurementsSinceLastUpdate != null) {
			Collections.sort(accelerometerMeasurementsSinceLastUpdate);
		}

		// TODO: Filter gyroscope values
		// TODO: Filter accelerometer values

		// Regular variables start here
		List<ValueUpdate<EulerAngleRotation>> orientationUpdates;

		if (gyroscopeMeasurementsSinceLastUpdate == null) {
			orientationUpdates = new ArrayList<ValueUpdate<EulerAngleRotation>>();

		} else {
			orientationUpdates = convertOrientationUpdate(initialOrientation, gyroscopeMeasurementsSinceLastUpdate);
		}

		if (accelerometerMeasurementsSinceLastUpdate != null) {
			List<ImuState> testbedCoordinateFrameAccelerometerMeasurements = adjustOrientation(
					accelerometerMeasurementsSinceLastUpdate, initialOrientation, orientationUpdates);

			if (lastProcessedAcceleration == null && testbedCoordinateFrameAccelerometerMeasurements.size() > 0) {

				ImuState imuState = testbedCoordinateFrameAccelerometerMeasurements.get(0);
				Calendar currentTime = imuState.getTimeMeasured();
				lastProcessedAcceleration = new ValueUpdate<Acceleration>(imuState.getCorrectedAcceleration(),
						imuState.getTimeMeasured());
				initialPosition = new ValueUpdate<Coordinate>(initialPosition.getValue(), currentTime);
				initialOrientation = new ValueUpdate<EulerAngleRotation>(initialOrientation.getValue(), currentTime);
				initialVelocity = new ValueUpdate<Velocity>(new Velocity(0, 0, 0), currentTime);
			}

			for (ImuState measurement : testbedCoordinateFrameAccelerometerMeasurements) {

				// Calculate velocity
				double velocityX = 0;
				double velocityY = 0;
				double velocityZ = 0;
				double deltaTimeInSeconds = measurement.getNumberOfSecondsSinceLastUpdate();

				// Correct Velocity with the current orientation
				double speed = Math.sqrt((initialVelocity.getValue().getMetersPerSecondAlongXAxis()
						* initialVelocity.getValue().getMetersPerSecondAlongXAxis())
						+ (initialVelocity.getValue().getMetersPerSecondAlongYAxis()
								* initialVelocity.getValue().getMetersPerSecondAlongYAxis()));

				velocityX = Math.cos(measurement.getCorrectedOrientation().getRadiansRotationAlongZAxis()) * speed;
				velocityY = Math.sin(measurement.getCorrectedOrientation().getRadiansRotationAlongZAxis()) * speed;
				// velocityX =
				// initialVelocity.getValue().getMetersPerSecondAlongXAxis();
				// velocityY =
				// initialVelocity.getValue().getMetersPerSecondAlongYAxis();

				velocityX += (measurement.getCorrectedAcceleration().getMetersPerSecondSquaredAlongXAxis()
						* deltaTimeInSeconds);
				velocityY += (measurement.getCorrectedAcceleration().getMetersPerSecondSquaredAlongYAxis()
						* deltaTimeInSeconds);
				velocityZ += (measurement.getCorrectedAcceleration().getMetersPerSecondSquaredAlongZAxis()
						* deltaTimeInSeconds);

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

				// Update the last processed acceleration measurement
				lastProcessedAcceleration = new ValueUpdate<Acceleration>(measurement.getCorrectedAcceleration(),
						measurement.getTimeMeasured());

			}
		}

		// Update the last known orientation

		if (orientationUpdates.size() > 0) {
			positionResults.setLastKnownTrainOrientation(orientationUpdates.get(orientationUpdates.size() - 1));
		} else {
			positionResults.setLastKnownTrainOrientation(initialOrientation);
		}

		if (gyroscopeMeasurementsSinceLastUpdate.size() > 0) {
			GyroscopeMeasurement gyroscopeMeasurement = gyroscopeMeasurementsSinceLastUpdate
					.get(gyroscopeMeasurementsSinceLastUpdate.size() - 1);

			ValueUpdate<ThreeDimensionalSpaceVector> angularVelocity = new ValueUpdate<ThreeDimensionalSpaceVector>(
					new ThreeDimensionalSpaceVector(gyroscopeMeasurement.getRadiansRotationPerSecondAlongXAxis(),
							gyroscopeMeasurement.getRadiansRotationPerSecondAlongYAxis(),
							gyroscopeMeasurement.getRadiansRotationPerSecondAlongZAxis()),
					gyroscopeMeasurement.getTimeMeasured());

			positionResults.setLastAngularVelocity(angularVelocity);
		} else {
			positionResults.setLastAngularVelocity(lastProcessedAngularVelocity);
		}

		// Update last known position
		positionResults.setLastKnownTrainPosition(initialPosition);

		// Update last known velocity
		positionResults.setLastKnownTrainVelocity(initialVelocity);

		// Update last known acceleration
		positionResults.setLastKnownTrainAcceleration(lastProcessedAcceleration);

		return positionResults;
	}

	private List<ValueUpdate<EulerAngleRotation>> convertOrientationUpdate(
			ValueUpdate<EulerAngleRotation> lastKnownOrientation, List<GyroscopeMeasurement> gyroscopeMeasurements) {
		List<ValueUpdate<EulerAngleRotation>> orientationUpdates = new ArrayList<ValueUpdate<EulerAngleRotation>>();
		orientationUpdates.add(lastKnownOrientation);

		// Assuming that the table is flat and all of gravity the NED, axis.
		// Assuming that the body frame z-axis matches the NED frame z-axis.
		double netDeltaYaw = 0;

		for (GyroscopeMeasurement gyroscopeMeasurement : gyroscopeMeasurements) {
			double dt = gyroscopeMeasurement.getNumberOfSecondsSinceLastMeasurement();

			double deltaYaw = (gyroscopeMeasurement.getRadiansRotationPerSecondAlongZAxis()) * dt;

			netDeltaYaw += deltaYaw;
			EulerAngleRotation updatedOrientation = new EulerAngleRotation(0, 0,
					lastKnownOrientation.getValue().getRadiansRotationAlongZAxis() + netDeltaYaw);

			ValueUpdate<EulerAngleRotation> orientationUpdate = new ValueUpdate<EulerAngleRotation>(updatedOrientation,
					gyroscopeMeasurement.getTimeMeasured());
			orientationUpdates.add(orientationUpdate);
		}

		return orientationUpdates;
	}

	/**
	 * Method is responsible for adjusting accelerometer measurements based on
	 * the orientation data that is available for the the body coordinate
	 * relative to the NED coordinate frame assuming that the object is flat on
	 * a surface and the body z-axis is always aligned with the NED z-axis.
	 * 
	 * @param measurement
	 *            Accelerometer measurement to convert
	 * @param orientation
	 *            Orientation of the measurement at the point of interest
	 * @param frictionThresholdFilter
	 *            Filter used to correct for friction. Assign null if not
	 *            desired
	 * @return corrected accelerometer measurement for the NED coordinate frame
	 */
	private static AccelerometerMeasurement changetoInertialFrame2d(AccelerometerMeasurement measurement,
			EulerAngleRotation orientation, ThresholdFilter frictionThresholdFilter) {
		AccelerometerMeasurement inertialFrameMeasurement;

		double adjustedX = (measurement.getAccelerationMeasurement().getMetersPerSecondSquaredAlongXAxis());
		double adjustedY = (measurement.getAccelerationMeasurement().getMetersPerSecondSquaredAlongYAxis());
		double sqX = adjustedX * adjustedX;
		double sqY = adjustedY * adjustedY;
		double scalarAcceleration = Math.sqrt(sqX + sqY);

		if (frictionThresholdFilter != null) {
			scalarAcceleration = frictionThresholdFilter.filter(scalarAcceleration);
		}

		double accX = scalarAcceleration * Math.cos(orientation.getRadiansRotationAlongZAxis());
		double accY = scalarAcceleration * Math.sin(orientation.getRadiansRotationAlongZAxis());
		double accZ = 0;

		Acceleration acceleration = new Acceleration(accX, accY, accZ);
		inertialFrameMeasurement = new AccelerometerMeasurement(measurement.getTrainId(), acceleration,
				measurement.getNumberOfSecondsSinceLastMeasurement(), measurement.getTimeMeasured());

		return inertialFrameMeasurement;
	}

	private List<ImuState> adjustOrientation(List<AccelerometerMeasurement> rawMeasurements,
			ValueUpdate<EulerAngleRotation> lastKnownOrientation,
			List<ValueUpdate<EulerAngleRotation>> orientationUpdates) {
		List<AccelerometerMeasurement> remainingMeasurements = new LinkedList<AccelerometerMeasurement>(
				rawMeasurements);
		List<ImuState> adjustedVectors = new LinkedList<ImuState>();
		int first = 0;
		int last = orientationUpdates.size();

		ValueUpdate<EulerAngleRotation> finalOrientation;

		if (orientationUpdates.isEmpty()) {
			finalOrientation = lastKnownOrientation;
		} else {
			finalOrientation = orientationUpdates.get(last - 1);
		}

		// NOTE: This implementation assumes that the orientationUpdates list is
		// sorted in
		// increasing order by time.e
		// Accelerometer measurements should also be sorted in increasing order
		// by time.

		for (int i = first; i < last; i++) {
			ValueUpdate<EulerAngleRotation> orientationUpdate = orientationUpdates.get(i);
			List<AccelerometerMeasurement> toRemove = new LinkedList<AccelerometerMeasurement>();

			// Process measurements

			for (AccelerometerMeasurement measurement : remainingMeasurements) {

				if (measurement.getTimeMeasured().getTimeInMillis() < lastKnownOrientation.getTimeObserved()
						.getTimeInMillis()) {
					// Last Known
					// This should only be called for the edge case of there
					// still being measurements
					// with the last known measurement.

					AccelerometerMeasurement adjustedMeasurement = changetoInertialFrame2d(measurement,
							finalOrientation.getValue(), thresholdFilter);
					adjustedVectors.add(new ImuState(adjustedMeasurement, finalOrientation));

					toRemove.add(measurement);
				} else if (measurement.getTimeMeasured().getTimeInMillis() >= lastKnownOrientation.getTimeObserved()
						.getTimeInMillis()
						&& measurement.getTimeMeasured().getTimeInMillis() < orientationUpdate.getTimeObserved()
								.getTimeInMillis()) {
					// Last Known
					// We assume that all accelerometer measurements occur at
					// the last known orientation
					// until we have an orientation update.

					AccelerometerMeasurement adjustedMeasurement = changetoInertialFrame2d(measurement,
							finalOrientation.getValue(), thresholdFilter);
					adjustedVectors.add(new ImuState(adjustedMeasurement, finalOrientation));

					toRemove.add(measurement);
				} else if (measurement.getTimeMeasured().getTimeInMillis() >= finalOrientation.getTimeObserved()
						.getTimeInMillis()) {

					// Final
					AccelerometerMeasurement adjustedMeasurement = changetoInertialFrame2d(measurement,
							finalOrientation.getValue(), thresholdFilter);
					adjustedVectors.add(new ImuState(adjustedMeasurement, finalOrientation));
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

	/**
	 * Indicates if the algorithm as determined a reliable starting point to
	 * calculate train position
	 * 
	 * @return Returns if a reliable starting point has been found; Otherwise
	 *         returns false.
	 */
	public boolean isInitialPositionFound() {
		return initialPositionFound;
	}
}
