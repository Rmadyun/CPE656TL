package com.traintrax.navigation.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.traintrax.navigation.service.mdu.AccelerometerMeasurement;
import com.traintrax.navigation.service.mdu.GyroscopeMeasurement;
import com.traintrax.navigation.service.mdu.MotionDetectionUnitInterface;
import com.traintrax.navigation.service.mdu.RfidTagDetectedNotification;
import com.traintrax.navigation.service.mdu.RotationUtilities;
import com.traintrax.navigation.service.position.Acceleration;
import com.traintrax.navigation.service.position.Coordinate;
import com.traintrax.navigation.service.position.Velocity;
import com.traintrax.navigation.service.rotation.EulerAngleRotation;
import com.traintrax.navigation.service.rotation.Quat4d;

/**
 * Class is responsible for observing changes to a train that belongs to the
 * Positive Train Control Test Bed.
 * 
 * @author Corey Sanders
 * 
 */
public class TrainMonitor implements TrainMonitorInterface {

	private final String trainId;
	private ValueUpdate<Coordinate> lastKnownTrainPosition;
	private ValueUpdate<Velocity> lastKnownTrainVelocity;
	private ValueUpdate<Acceleration> lastKnownTrainAcceleration;
	private ValueUpdate<EulerAngleRotation> lastKnownTrainOrientation;
	private final MotionDetectionUnitInterface motionDetectionUnit;
	
	private final List<GyroscopeMeasurement> gyroscopeMeasurementsSinceLastUpdate;
	private final List<AccelerometerMeasurement> accelerometerMeasurementsSinceLastUpdate;

	/**
	 * Stores the results of processing accelerometer measurements and placing
	 * them into the correct inertial frame
	 * 
	 * @author Corey Sanders
	 * 
	 */
	private class AccelerometerInertialFrameResults {
		public List<AccelerometerMeasurement> correctedMeasurements;
		public List<AccelerometerMeasurement> remainingMeasurements;
	}

	/**
	 * Constructor
	 * NOTE: Assuming that the train is a rest at initialization.
	 * @param trainId
	 *            Unique identifier for target train
	 * @param initialTrainPosition
	 *            Initial location of the train
	 * @param initialTrainOrientation Degree of rotation necessary
	 * to align the current Motion Detection Unit body coordinate frame
	 * axes with the test bed coordinate frame axes.
	 * @param motionDetectionUnit Contact to the Motion Detection Unit
	 * mounted on the train of interest.
	 */
	public TrainMonitor(String trainId, Coordinate initialTrainPosition,
			EulerAngleRotation initialTrainOrientation,
			MotionDetectionUnitInterface motionDetectionUnit) {

		this.trainId = trainId;
		Calendar currentTime = Calendar.getInstance();
		lastKnownTrainPosition = new ValueUpdate<Coordinate>(initialTrainPosition, currentTime);
		lastKnownTrainOrientation = new ValueUpdate<EulerAngleRotation>(initialTrainOrientation, currentTime);
		lastKnownTrainVelocity = new ValueUpdate<Velocity>(new Velocity(0,0,0), currentTime);
		lastKnownTrainAcceleration = new ValueUpdate<Acceleration>(new Acceleration(0,0,0), currentTime);
		this.motionDetectionUnit = motionDetectionUnit;
		this.accelerometerMeasurementsSinceLastUpdate = new ArrayList<>();
		this.gyroscopeMeasurementsSinceLastUpdate = new ArrayList<>();
	}

	/**
	 * Blocks until there is another update on the target train's position.
	 * 
	 * @return Most recent update on the position of the train.
	 */
	public Coordinate waitForNextPositionUpdate() {
		List<GyroscopeMeasurement> newGyroscopeMeasurements;
		List<AccelerometerMeasurement> newAccelerometerMeasurements;
		List<RfidTagDetectedNotification> newRfidTagEvents;
		boolean positionUpdateAvailable = false;
		
		do{
			
			newGyroscopeMeasurements = motionDetectionUnit.readCollectedGyroscopeMeasurements();
			newAccelerometerMeasurements = motionDetectionUnit.readCollectedAccelerometerMeasurements();
			newRfidTagEvents = motionDetectionUnit.readCollectedRfidTagDetectionNotifications();
			
			if((newGyroscopeMeasurements.size() == 0) &&
					(newAccelerometerMeasurements.size() == 0) &&
					(newRfidTagEvents.size() == 0)){
				positionUpdateAvailable = true;
				
				//Wait if nothing is available
				try{
				    Thread.sleep(2000, 0);
				}
				catch(Exception exception){
					
				}
			}
			
		}
		while(!positionUpdateAvailable);
		
		//Update the measurements list.
		gyroscopeMeasurementsSinceLastUpdate.addAll(newGyroscopeMeasurements);
		accelerometerMeasurementsSinceLastUpdate.addAll(newAccelerometerMeasurements);

		return calculateTrainPosition();
	}

	private Coordinate calculateTrainPosition(){
		List<GyroscopeMeasurement> gyroscopeMeasurementSnapshot = new ArrayList<>(gyroscopeMeasurementsSinceLastUpdate);
		List<AccelerometerMeasurement> accelerometerMeasurementSnapshot = new ArrayList<>(accelerometerMeasurementsSinceLastUpdate);
		
		//TODO: Sort gyroscope measurements in increasing order by time
		//TODO: Sort accelerometer measurements in increasing order by time
		
		//Regular variables start here
		List<ValueUpdate<EulerAngleRotation>> orientationUpdates = convertOrientationUpdate(lastKnownTrainOrientation, gyroscopeMeasurementsSinceLastUpdate);
		
		List<AccelerometerMeasurement> testbedCoordinateFrameAccelerometerMeasurements = adjustOrientation(accelerometerMeasurementsSinceLastUpdate, orientationUpdates);
		
		//TODO: Filter gyroscope values
		//TODO: Filter accelerometer values

		ValueUpdate<Coordinate> initialPosition = lastKnownTrainPosition;
		ValueUpdate<Velocity> initialVelocity = lastKnownTrainVelocity;
		ValueUpdate<Acceleration> lastProcessedAcceleration = lastKnownTrainAcceleration;
		for(AccelerometerMeasurement measurement : testbedCoordinateFrameAccelerometerMeasurements){

			//Calculate velocity
			double velocityX = 0;
			double velocityY = 0;
			double velocityZ = 0;
			double deltaTimeInSeconds = (measurement.getTimeMeasured().getTimeInMillis() - lastProcessedAcceleration.getTimeObserved().getTimeInMillis())/1000.0;
			
			velocityX = measurement.getAccelerationMeasurement().getMetersPerSecondSquaredAlongXAxis()*deltaTimeInSeconds +
					initialVelocity.getValue().getMetersPerSecondAlongXAxis();
			velocityY = measurement.getAccelerationMeasurement().getMetersPerSecondSquaredAlongYAxis()*deltaTimeInSeconds +
					initialVelocity.getValue().getMetersPerSecondAlongYAxis();
			velocityZ = measurement.getAccelerationMeasurement().getMetersPerSecondSquaredAlongZAxis()*deltaTimeInSeconds +
					initialVelocity.getValue().getMetersPerSecondAlongZAxis();

			//Update the final velocity
			ValueUpdate<Velocity> finalVelocity = new ValueUpdate<Velocity>(new Velocity(velocityX, velocityY, velocityZ),
					measurement.getTimeMeasured());
			
			//Calculate position
			double displacementX = 0;
			double displacementY = 0;
			double displacementZ = 0;
			
			displacementX = finalVelocity.getValue().getMetersPerSecondAlongXAxis()*deltaTimeInSeconds + initialPosition.getValue().getX();
			displacementY = finalVelocity.getValue().getMetersPerSecondAlongYAxis()*deltaTimeInSeconds + initialPosition.getValue().getY();
			displacementZ = finalVelocity.getValue().getMetersPerSecondAlongZAxis()*deltaTimeInSeconds + initialPosition.getValue().getZ();

			ValueUpdate<Coordinate> finalPosition = new ValueUpdate<Coordinate>(new Coordinate(displacementX, displacementY, displacementZ),
					measurement.getTimeMeasured());
			
			//Update the initialPosition
			initialPosition = finalPosition;
			
			//Update the initial velocity
			initialVelocity = finalVelocity;
			
			//Update the last processed acceleration
			lastProcessedAcceleration = new ValueUpdate<Acceleration>(measurement.getAccelerationMeasurement(), measurement.getTimeMeasured());
		}
		
		//Update the last known orientation
		
		if(orientationUpdates.size() > 0){
			lastKnownTrainOrientation = orientationUpdates.get(orientationUpdates.size()-1);
		}
		
		//Update last known position
		lastKnownTrainPosition = initialPosition;
		
		//Update last known velocity
		lastKnownTrainVelocity = initialVelocity;
		
		//Update last known acceleration
		lastKnownTrainAcceleration = lastProcessedAcceleration;
		
		//Remove measurements (gyroscope, accelerometer) that were used with the update
		accelerometerMeasurementsSinceLastUpdate.removeAll(accelerometerMeasurementSnapshot);
		gyroscopeMeasurementsSinceLastUpdate.removeAll(gyroscopeMeasurementSnapshot);
		
		return lastKnownTrainPosition.getValue();
	}
	
	private List<ValueUpdate<EulerAngleRotation>> convertOrientationUpdate(ValueUpdate<EulerAngleRotation> lastKnownOrientation, List<GyroscopeMeasurement> gyroscopeMeasurements){
		List<ValueUpdate<EulerAngleRotation>> orientationUpdates = new ArrayList<ValueUpdate<EulerAngleRotation>>();
		
		ValueUpdate<EulerAngleRotation> currentOrientation = lastKnownOrientation;
		
		orientationUpdates.add(lastKnownOrientation);
		
		for(GyroscopeMeasurement gyroscopeMeasurement : gyroscopeMeasurements){
			Quat4d currentOrientationQuat = RotationUtilities.convertFromEulerAngleToQuaternion(currentOrientation.getValue());
		    Quat4d updatedOrientationQuat = RotationUtilities.calculateNedInertialFrameOrientation(gyroscopeMeasurement, currentOrientationQuat);
		    EulerAngleRotation updatedOrientation = RotationUtilities.convertFromQuaternionToEulerAngle(updatedOrientationQuat);
		    
		    ValueUpdate<EulerAngleRotation> orientationUpdate = new ValueUpdate<EulerAngleRotation>(updatedOrientation, gyroscopeMeasurement.getTimeMeasured());
			orientationUpdates.add(orientationUpdate);
		}

		return orientationUpdates;
	}

	List<AccelerometerMeasurement> adjustOrientation(
			List<AccelerometerMeasurement> rawMeasurements,
			List<ValueUpdate<EulerAngleRotation>> orientationUpdates) {
		List<AccelerometerMeasurement> adjustedVectors = new ArrayList<AccelerometerMeasurement>();
		List<AccelerometerMeasurement> remainder = new ArrayList<AccelerometerMeasurement>();
		int first = 0;
		int last = orientationUpdates.size();
		ValueUpdate<EulerAngleRotation> lastKnownOrientation = null;
		ValueUpdate<EulerAngleRotation> finalOrientation = orientationUpdates.get(last);

		//NOTE: This implementation assumes that the orientationUpdates list is sorted in 
		//increasing order by time.e
		//Accelerometer measurements should also be sorted in increasing order by time.
		
		for (int i = first; i < last; i++) {
			ValueUpdate<EulerAngleRotation> orientationUpdate = orientationUpdates.get(i);
			List<AccelerometerMeasurement> toRemove = new ArrayList<AccelerometerMeasurement>();

			// Process measurements

			for (AccelerometerMeasurement measurement : rawMeasurements) {

				if (measurement.getTimeMeasured().getTimeInMillis() < lastKnownOrientation.getTimeObserved()
						.getTimeInMillis()) {
					//Last Known
					//This should only be called for the edge case of there still being measurements
					//with the last known measurement.
					
				    AccelerometerMeasurement adjustedMeasurement = RotationUtilities.changeToInertialFrame(measurement, finalOrientation.getValue());
				    adjustedVectors.add(adjustedMeasurement);
				    
					toRemove.add(measurement);
				} else if (measurement.getTimeMeasured().getTimeInMillis() >= lastKnownOrientation.getTimeObserved()
						.getTimeInMillis()
						&& measurement.getTimeMeasured().getTimeInMillis() < orientationUpdate.getTimeObserved()
								.getTimeInMillis()) {
					//Last Known
					//We assume that all accelerometer measurements occur at the last known orientation
					//until we have an orientation update.
					
				    AccelerometerMeasurement adjustedMeasurement = RotationUtilities.changeToInertialFrame(measurement, finalOrientation.getValue());
				    adjustedVectors.add(adjustedMeasurement);
					
					toRemove.add(measurement);
				}
				else if(measurement.getTimeMeasured().getTimeInMillis() >= finalOrientation.getTimeObserved().getTimeInMillis()){
					
					//Final
				    AccelerometerMeasurement adjustedMeasurement = RotationUtilities.changeToInertialFrame(measurement, finalOrientation.getValue());
				    adjustedVectors.add(adjustedMeasurement);
					toRemove.add(measurement);
				}
			}

			// Remove measurements that have been processed
			for (AccelerometerMeasurement measurement : toRemove) {

				adjustedVectors.remove(measurement);
			}
			
			//Update the Last Known orientation
			lastKnownOrientation = orientationUpdate;

		}
		
		if(adjustedVectors.size() != rawMeasurements.size()){
			//TODO: Raise some kind index of error.
			//Something is wrong with implementation
		}

		return adjustedVectors;
	}

	private AccelerometerInertialFrameResults updateAccelerometerMeasurements(
			ValueUpdate<EulerAngleRotation> currentUpdate,
			List<AccelerometerMeasurement> rawMeasurements) {
		AccelerometerInertialFrameResults results = new AccelerometerInertialFrameResults();

		for (AccelerometerMeasurement measurement : rawMeasurements) {

			// We only calculate orientation with lastKnownTrainPosition.getValue()the last
			if (measurement.getTimeMeasured().getTimeInMillis() < currentUpdate.getTimeObserved()
					.getTimeInMillis()) {

			}
		}

		return results;
	}

	/**
	 * Retrieves the unique identifier for the target train
	 * 
	 * @return Unique identifier for the target train
	 */
	public String getTrainId() {
		return trainId;
	}

	/**
	 * Retrieves the last known position of the target train
	 * 
	 * @return The last known position of the target train.
	 */
	public Coordinate getLastKnownPosition() {
		return lastKnownTrainPosition.getValue();
	}

}
