package com.traintrax.navigation.service.mdu;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedList;
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
	private Coordinate initialTrainPosition;
	private EulerAngleRotation initialTrainOrientation;
	private boolean firstMeasurementReceived = false;

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
		this.initialTrainPosition = initialTrainPosition;
		this.initialTrainOrientation = initialTrainOrientation;
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
			
			if(!firstMeasurementReceived){
				AccelerometerMeasurement accelerometerMeasurement = accelerometerMeasurementsSinceLastUpdate.get(0);
				Calendar currentTime = accelerometerMeasurement.getTimeMeasured();
				lastProcessedAcceleration = new ValueUpdate<Acceleration>(accelerometerMeasurement.getAccelerationMeasurement(), accelerometerMeasurement.getTimeMeasured());
				lastKnownTrainPosition = new ValueUpdate<Coordinate>(initialTrainPosition, currentTime);
				lastKnownTrainOrientation = new ValueUpdate<EulerAngleRotation>(initialTrainOrientation, currentTime);
				lastKnownTrainVelocity = new ValueUpdate<Velocity>(new Velocity(0, 0, 0), currentTime);
				
				firstMeasurementReceived = true;
			}

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
	
	/**
	 * Class describes information about the last 
	 * estimate of position calculated from RFID tag input alone.
	 * @author Corey Sanders
	 *
	 */
	private class RfidTagPositionResults{
		private ValueUpdate<Coordinate> lastKnownTrainPosition;
		private ValueUpdate<Velocity> lastKnownTrainVelocity;
		private ValueUpdate<EulerAngleRotation> lastKnownTrainOrientation;
		
		/**
		 * @return the lastKnownTrainPosition
		 */
		public ValueUpdate<Coordinate> getLastKnownTrainPosition() {
			return lastKnownTrainPosition;
		}
		/**
		 * @param lastKnownTrainPosition the lastKnownTrainPosition to set
		 */
		public void setLastKnownTrainPosition(ValueUpdate<Coordinate> lastKnownTrainPosition) {
			this.lastKnownTrainPosition = lastKnownTrainPosition;
		}
		/**
		 * @return the lastKnownTrainVelocity
		 */
		public ValueUpdate<Velocity> getLastKnownTrainVelocity() {
			return lastKnownTrainVelocity;
		}
		/**
		 * @param lastKnownTrainVelocity the lastKnownTrainVelocity to set
		 */
		public void setLastKnownTrainVelocity(ValueUpdate<Velocity> lastKnownTrainVelocity) {
			this.lastKnownTrainVelocity = lastKnownTrainVelocity;
		}
		
		/**
		 * @return the lastKnownTrainOrientation
		 */
		public ValueUpdate<EulerAngleRotation> getLastKnownTrainOrientation() {
			return lastKnownTrainOrientation;
		}
		/**
		 * @param lastKnownTrainOrientation the lastKnownTrainOrientation to set
		 */
		public void setLastKnownTrainOrientation(ValueUpdate<EulerAngleRotation> lastKnownTrainOrientation) {
			this.lastKnownTrainOrientation = lastKnownTrainOrientation;
		}
	}
	
	/**
	 * Determines the current position of the train from RFID tag measurements
	 * This implementation is assuming a 2-dimensional train track where the
	 * NED frame is used, but there are not any elevation changes on the track (i.e. the track is flat)
	 * and on a level-table so that gravity is only acting on the Z-axis.
	 * @param initialPosition Initial position of the train
	 * @param finalPosition Final position of the train
	 * @return Detailed information about the position of the train
	 */
	private RfidTagPositionResults calculationPositionFromRfidTagUpdates(ValueUpdate<Coordinate> initialPosition, 
			ValueUpdate<Coordinate> finalPosition){
		
		RfidTagPositionResults rfidTagPositionResults = new RfidTagPositionResults();
		
        double dx = finalPosition.getValue().getX() - initialPosition.getValue().getX();
        double dy = finalPosition.getValue().getY() - initialPosition.getValue().getY();
        double distance = Math.sqrt(dx*dx + dy*dy);
		double dt = (finalPosition.getTimeObserved().getTimeInMillis()
				- initialPosition.getTimeObserved().getTimeInMillis()) / 1000.0;
        double yaw = 0;
		
        if(distance != 0)
        {
            yaw = Math.acos(dx/distance);
            if(dy < 0)
            {
                yaw = yaw * -1;
            }
        }
        
        ValueUpdate<EulerAngleRotation> orientationUpdate = new ValueUpdate<EulerAngleRotation>(new EulerAngleRotation(0,0, yaw), finalPosition.getTimeObserved());
        ValueUpdate<Velocity> velocityUpdate = new ValueUpdate<Velocity>(new Velocity(dx/dt, dy/dt, 0), finalPosition.getTimeObserved());
        
		rfidTagPositionResults.setLastKnownTrainVelocity(velocityUpdate);
		rfidTagPositionResults.setLastKnownTrainOrientation(orientationUpdate);
		rfidTagPositionResults.setLastKnownTrainPosition(finalPosition);
		
		return rfidTagPositionResults;
	}
	
	/**
	 * Class describes information about the last 
	 * estimate of position calculated from IMU measurements alone.
	 * @author Corey Sanders
	 *
	 */
	private class ImuPositionResults{
		private ValueUpdate<Coordinate> lastKnownTrainPosition;
		private ValueUpdate<Velocity> lastKnownTrainVelocity;
		private ValueUpdate<EulerAngleRotation> lastKnownTrainOrientation;
		private ValueUpdate<Acceleration> lastKnownTrainAcceleration;
		
		/**
		 * @return the lastKnownTrainPosition
		 */
		public ValueUpdate<Coordinate> getLastKnownTrainPosition() {
			return lastKnownTrainPosition;
		}
		
		/**
		 * @param lastKnownTrainPosition the lastKnownTrainPosition to set
		 */
		public void setLastKnownTrainPosition(ValueUpdate<Coordinate> lastKnownTrainPosition) {
			this.lastKnownTrainPosition = lastKnownTrainPosition;
		}
		
		/**
		 * @return the lastKnownTrainVelocity
		 */
		public ValueUpdate<Velocity> getLastKnownTrainVelocity() {
			return lastKnownTrainVelocity;
		}
		/**
		 * @param lastKnownTrainVelocity the lastKnownTrainVelocity to set
		 */
		public void setLastKnownTrainVelocity(ValueUpdate<Velocity> lastKnownTrainVelocity) {
			this.lastKnownTrainVelocity = lastKnownTrainVelocity;
		}

		/**
		 * @return the lastKnownTrainOrientation
		 */
		public ValueUpdate<EulerAngleRotation> getLastKnownTrainOrientation() {
			return lastKnownTrainOrientation;
		}
		
		/**
		 * @param lastKnownTrainOrientation the lastKnownTrainOrientation to set
		 */
		public void setLastKnownTrainOrientation(ValueUpdate<EulerAngleRotation> lastKnownTrainOrientation) {
			this.lastKnownTrainOrientation = lastKnownTrainOrientation;
		}
		
		/**
		 * Retrieves the last acceleration value measured for the train
		 * @return Last Acceleration value measured for the train
		 */
		public ValueUpdate<Acceleration> getLastKnownTrainAcceleration() {
			return lastKnownTrainAcceleration;
		}
		
		/**
		 * Assigns the last acceleration value measured for the train
		 * @param lastKnownTrainAcceleration Last Acceleration value measured for the train
		 */
		public void setLastKnownTrainAcceleration(ValueUpdate<Acceleration> lastKnownTrainAcceleration) {
			this.lastKnownTrainAcceleration = lastKnownTrainAcceleration;
		}
		
		
	}
	
	/**
	 * Method selects a subset of measurements based on time.
	 * @param measurements List of measurements that have been pre-sorted by time to search through to find the desired measurements.
	 * @param startTime Beginning of the time range to select measurements
	 * @param endTime End of the time range to select measurements
	 * @return Subset of measurements that match the time range requested sorted by time.
	 */
	private List<AccelerometerMeasurement> selectAccelerometerMeasurementsByTime(List<AccelerometerMeasurement> measurements, Calendar startTime, Calendar endTime){
		
		List<AccelerometerMeasurement> selectedMeasurements = new LinkedList<AccelerometerMeasurement>();
		
		for(AccelerometerMeasurement measurement: measurements){
			
			if(measurement.getTimeMeasured().getTimeInMillis() >= startTime.getTimeInMillis() 
					&& measurement.getTimeMeasured().getTimeInMillis() <= endTime.getTimeInMillis()){
			
				selectedMeasurements.add(measurement);
			}
		}
		
		return selectedMeasurements;
	}
	
	/**
	 * Method selects a subset of measurements based on time.
	 * @param measurements List of measurements that have been pre-sorted by time to search through to find the desired measurements.
	 * @param startTime Beginning of the time range to select measurements
	 * @param endTime End of the time range to select measurements
	 * @return Subset of measurements that match the time range requested sorted by time.
	 */
	private List<GyroscopeMeasurement> selectGyroscopeMeasurementsByTime(List<GyroscopeMeasurement> measurements, Calendar startTime, Calendar endTime){
		
		List<GyroscopeMeasurement> selectedMeasurements = new LinkedList<GyroscopeMeasurement>();
		
		for(GyroscopeMeasurement measurement: measurements){
			
			if(measurement.getTimeMeasured().getTimeInMillis() >= startTime.getTimeInMillis() 
					&& measurement.getTimeMeasured().getTimeInMillis() <= endTime.getTimeInMillis()){
			
				selectedMeasurements.add(measurement);
			}
		}
		
		return selectedMeasurements;
	}

	/**
	 * Determines the current position of the train
	 * 
	 * @param initialPosition Initial position of the train
	 * @param initialOrientation Initial orientation of the train
	 * @param initialVelocity Initial velocity of the train
	 * @param lastProcessedAcceleration Last measured acceleration of the train (NED frame)
	 * 
	 * @param gyroscopeMeasurementsSinceLastUpdate
	 *            New Gyroscope measurements
	 * @param accelerometerMeasurementsSinceLastUpdate
	 *            New Accelerometer measurements
	 * @param rfidTagDetectedEvents
	 *            New RFID tag detected events
	 * @return The estimated current position of the train.
	 */
	private ImuPositionResults calculatePositionFromImu(ValueUpdate<Coordinate> initialPosition, ValueUpdate<EulerAngleRotation> initialOrientation, ValueUpdate<Velocity> initialVelocity,	
			ValueUpdate<Acceleration> lastProcessedAcceleration, List<GyroscopeMeasurement> gyroscopeMeasurementsSinceLastUpdate,
			List<AccelerometerMeasurement> accelerometerMeasurementsSinceLastUpdate) {
		
		ImuPositionResults positionResults = new ImuPositionResults();
		
		//TODO: Remove all dependencies of fields in the algorithm
		//Make them arguments instead.
		
		//TODO: Partition IMU Measurements based on RFID Tag time
		//Use only the most recent Time post RFID Tag measurements for
		//IMU position calculations.
		//.) Add code to write to CSV all interim measurements
		//.) -> This will be done by calling each measurement (IMU/ACC+GYR or RFID Tag)
		//.) update one-at-a-time with a separate calculate position call.
		//.) Use RFID Tag derived orientation and velocity as inputs
		//.) for IMU calculation.
		
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
			orientationUpdates = convertOrientationUpdate(initialOrientation,
					gyroscopeMeasurementsSinceLastUpdate);
		}
		
		if (accelerometerMeasurementsSinceLastUpdate != null) {
			List<AccelerometerMeasurement> testbedCoordinateFrameAccelerometerMeasurements = adjustOrientation(
					accelerometerMeasurementsSinceLastUpdate, initialOrientation, orientationUpdates);
			
			if(lastProcessedAcceleration == null && testbedCoordinateFrameAccelerometerMeasurements.size() > 0){
				
				AccelerometerMeasurement accelerometerMeasurement = testbedCoordinateFrameAccelerometerMeasurements.get(0);
				Calendar currentTime = accelerometerMeasurement.getTimeMeasured();
				lastProcessedAcceleration = new ValueUpdate<Acceleration>(accelerometerMeasurement.getAccelerationMeasurement(), accelerometerMeasurement.getTimeMeasured());
				initialPosition = new ValueUpdate<Coordinate>(initialPosition.getValue(), currentTime);
				initialOrientation = new ValueUpdate<EulerAngleRotation>(initialOrientation.getValue(), currentTime);
				initialVelocity = new ValueUpdate<Velocity>(new Velocity(0, 0, 0), currentTime);
			}

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
			positionResults.setLastKnownTrainOrientation(orientationUpdates.get(orientationUpdates.size() - 1));
		}
		else{
			positionResults.setLastKnownTrainOrientation(initialOrientation);
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
