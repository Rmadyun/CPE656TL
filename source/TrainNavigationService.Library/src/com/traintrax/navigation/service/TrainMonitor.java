package com.traintrax.navigation.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.traintrax.navigation.service.mdu.AccelerometerMeasurement;
import com.traintrax.navigation.service.mdu.GyroscopeMeasurement;
import com.traintrax.navigation.service.mdu.MotionDetectionUnitInterface;
import com.traintrax.navigation.service.mdu.RotationUtilities;
import com.traintrax.navigation.service.position.Coordinate;
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

	private String trainId;
	private Coordinate lastKnownTrainPosition;
	private MotionDetectionUnitInterface motionDetectionUnit;

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
	 * Class represents the orientation of the train at a particular point and
	 * time in the NED inertial reference frame.
	 * 
	 * @author death
	 * 
	 */
	private class OrientationUpdate {
		public EulerAngleRotation orientation;
		public Calendar timeObserved;
	}

	/**
	 * Constructor
	 * 
	 * @param trainId
	 *            Unique identifier for target train
	 * @param initialTrainPosition
	 *            Initial location of the train
	 */
	public TrainMonitor(String trainId, Coordinate initialTrainPosition,
			MotionDetectionUnitInterface motionDetectionUnit) {

		this.trainId = trainId;
		lastKnownTrainPosition = initialTrainPosition;
		this.motionDetectionUnit = motionDetectionUnit;
	}

	/**
	 * Blocks until there is another update on the target train's position.
	 * 
	 * @return Most recent update on the position of the train.
	 */
	public Coordinate waitForNextPositionUpdate() {

		// TODO: Implement the Train position update algorithm

		return lastKnownTrainPosition;
	}

	private Coordinate calculateTrainPosition(){
		//TODO: Convert these variables into fields or methods of this class.
		OrientationUpdate lastKnownOrientation = null;
		List<GyroscopeMeasurement> gyroscopeMeasurementsSinceLastUpdate = null;
		List<AccelerometerMeasurement> accelerometerMeasurementsSinceLastUpdate = null;
		
		//TODO: Sort gyroscope measurements in increasing order by time
		//TODO: Sort accelerometer measurements in increasing order by time
		
		//Regular variables start here
		List<OrientationUpdate> orientationUpdates = convertOrientationUpdate(lastKnownOrientation, gyroscopeMeasurementsSinceLastUpdate);
		
		List<AccelerometerMeasurement> nedAccelerometerMeasurements = adjustOrientation(accelerometerMeasurementsSinceLastUpdate, orientationUpdates);
		
		//TODO: Calculate velocity
		
		
		//TODO: Calculate position
		
		//Update the lastKnown Orientation
		
		if(orientationUpdates.size() > 0){
			lastKnownOrientation = orientationUpdates.get(orientationUpdates.size()-1);
		}
		
		//TODO: Remove measurements (gyroscope, accelerometer) that were used with the update
		//TODO: Save the time used with the last measurement. 
		
		return null;
	}
	
	private List<OrientationUpdate> convertOrientationUpdate(OrientationUpdate lastKnownOrientation, List<GyroscopeMeasurement> gyroscopeMeasurements){
		List<OrientationUpdate> orientationUpdates = new ArrayList<OrientationUpdate>();
		
		OrientationUpdate currentOrientation = lastKnownOrientation;
		
		orientationUpdates.add(lastKnownOrientation);
		
		for(GyroscopeMeasurement gyroscopeMeasurement : gyroscopeMeasurements){
			Quat4d currentOrientationQuat = RotationUtilities.convertFromEulerAngleToQuaternion(currentOrientation.orientation);
		    Quat4d updatedOrientationQuat = RotationUtilities.calculateNedInertialFrameOrientation(gyroscopeMeasurement, currentOrientationQuat);
		    EulerAngleRotation updatedOrientation = RotationUtilities.convertFromQuaternionToEulerAngle(updatedOrientationQuat);
		    
			OrientationUpdate orientationUpdate = new OrientationUpdate();
			orientationUpdate.orientation = updatedOrientation;
			orientationUpdate.timeObserved = gyroscopeMeasurement.getTimeMeasured();
			orientationUpdates.add(orientationUpdate);
		}

		return orientationUpdates;
	}

	List<AccelerometerMeasurement> adjustOrientation(
			List<AccelerometerMeasurement> rawMeasurements,
			List<OrientationUpdate> orientationUpdates) {
		List<AccelerometerMeasurement> adjustedVectors = new ArrayList<AccelerometerMeasurement>();
		List<AccelerometerMeasurement> remainder = new ArrayList<AccelerometerMeasurement>();
		int first = 0;
		int last = orientationUpdates.size();
		OrientationUpdate lastKnownOrientation = null;
		OrientationUpdate finalOrientation = orientationUpdates.get(last);

		//NOTE: This implementation assumes that the orientationUpdates list is sorted in 
		//increasing order by time.e
		//Accelerometer measurements should also be sorted in increasing order by time.
		
		for (int i = first; i < last; i++) {
			OrientationUpdate orientationUpdate = orientationUpdates.get(i);
			List<AccelerometerMeasurement> toRemove = new ArrayList<AccelerometerMeasurement>();

			// Process measurements

			for (AccelerometerMeasurement measurement : rawMeasurements) {

				if (measurement.getTimeMeasured().getTimeInMillis() < lastKnownOrientation.timeObserved
						.getTimeInMillis()) {
					//Last Known
					//This should only be called for the edge case of there still being measurements
					//with the last known measurement.
					
				    AccelerometerMeasurement adjustedMeasurement = RotationUtilities.changeToInertialFrame(measurement, finalOrientation.orientation);
				    adjustedVectors.add(adjustedMeasurement);
				    
					toRemove.add(measurement);
				} else if (measurement.getTimeMeasured().getTimeInMillis() >= lastKnownOrientation.timeObserved
						.getTimeInMillis()
						&& measurement.getTimeMeasured().getTimeInMillis() < orientationUpdate.timeObserved
								.getTimeInMillis()) {
					//Last Known
					//We assume that all accelerometer measurements occur at the last known orientation
					//until we have an orientation update.
					
				    AccelerometerMeasurement adjustedMeasurement = RotationUtilities.changeToInertialFrame(measurement, finalOrientation.orientation);
				    adjustedVectors.add(adjustedMeasurement);
					
					toRemove.add(measurement);
				}
				else if(measurement.getTimeMeasured().getTimeInMillis() >= finalOrientation.timeObserved.getTimeInMillis()){
					
					//Final
				    AccelerometerMeasurement adjustedMeasurement = RotationUtilities.changeToInertialFrame(measurement, finalOrientation.orientation);
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
			//TODO: Raise some kind indexof error.
			//Something is wrong with implementation
		}

		return adjustedVectors;
	}

	private AccelerometerInertialFrameResults updateAccelerometerMeasurements(
			OrientationUpdate currentUpdate,
			List<AccelerometerMeasurement> rawMeasurements) {
		AccelerometerInertialFrameResults results = new AccelerometerInertialFrameResults();

		for (AccelerometerMeasurement measurement : rawMeasurements) {

			// We only calculate orientation with the last
			if (measurement.getTimeMeasured().getTimeInMillis() < currentUpdate.timeObserved
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
		return lastKnownTrainPosition;
	}

}
