package com.traintrax.navigation.service.mdu;

import java.util.List;

import com.traintrax.navigation.service.ValueUpdate;
import com.traintrax.navigation.service.position.Coordinate;

/**
 * Generic interface for an object that determines the
 * position of an object using inertial motion measurements
 * @author Corey Sanders
 * 
 */
public interface InertialMotionPositionAlgorithmInterface {
	
	/**
	 * Determines the current position of the object.
	 * Assign null or an empty list for parameters that do not have any updated values.
	 * @param gyroscopeMeasurementsSinceLastUpdate New Gyroscope measurements from the object
	 * @param accelerometerMeasurementsSinceLastUpdate New Accelerometer measurements from the object
	 * @param positionUpdates Reported updates in the position of the object. This should be
	 * the distance from the origin in meters
	 * @return The estimated current position of the object.
	 */
	public ValueUpdate<Coordinate> calculatePosition(
			List<GyroscopeMeasurement> gyroscopeMeasurementsSinceLastUpdate,
			List<AccelerometerMeasurement> accelerometerMeasurementsSinceLastUpdate,
			List<ValueUpdate<Coordinate>> positionUpdates);

}
