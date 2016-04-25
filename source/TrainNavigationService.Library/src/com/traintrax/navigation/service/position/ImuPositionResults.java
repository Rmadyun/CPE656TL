package com.traintrax.navigation.service.position;

import com.traintrax.navigation.service.ValueUpdate;
import com.traintrax.navigation.service.math.ThreeDimensionalSpaceVector;
import com.traintrax.navigation.service.rotation.EulerAngleRotation;

/**
 * Class describes information about the last estimate of position
 * calculated from IMU measurements alone.
 * 
 * @author Corey Sanders
 *
 */
public class ImuPositionResults {
	private ValueUpdate<Coordinate> lastKnownTrainPosition;
	private ValueUpdate<Velocity> lastKnownTrainVelocity;
	private ValueUpdate<EulerAngleRotation> lastKnownTrainOrientation;
	private ValueUpdate<Acceleration> lastKnownTrainAcceleration;
	private ValueUpdate<ThreeDimensionalSpaceVector> lastAngularVelocity;

	/**
	 * @return the lastKnownTrainPosition
	 */
	public ValueUpdate<Coordinate> getLastKnownTrainPosition() {
		return lastKnownTrainPosition;
	}

	/**
	 * @param lastKnownTrainPosition
	 *            the lastKnownTrainPosition to set
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
	 * @param lastKnownTrainVelocity
	 *            the lastKnownTrainVelocity to set
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
	 * @param lastKnownTrainOrientation
	 *            the lastKnownTrainOrientation to set
	 */
	public void setLastKnownTrainOrientation(ValueUpdate<EulerAngleRotation> lastKnownTrainOrientation) {
		this.lastKnownTrainOrientation = lastKnownTrainOrientation;
	}

	/**
	 * Retrieves the last acceleration value measured for the train
	 * 
	 * @return Last Acceleration value measured for the train
	 */
	public ValueUpdate<Acceleration> getLastKnownTrainAcceleration() {
		return lastKnownTrainAcceleration;
	}

	/**
	 * Assigns the last acceleration value measured for the train
	 * 
	 * @param lastKnownTrainAcceleration
	 *            Last Acceleration value measured for the train
	 */
	public void setLastKnownTrainAcceleration(ValueUpdate<Acceleration> lastKnownTrainAcceleration) {
		this.lastKnownTrainAcceleration = lastKnownTrainAcceleration;
	}

	/**
	 * Retrieves the last angular velocity reported
	 * @return the last angular velocity reported 
	 */
	public ValueUpdate<ThreeDimensionalSpaceVector> getLastAngularVelocity() {
		return lastAngularVelocity;
	}

	/**
	 * Assigns the last angular velocity reported
	 * @param lastAngularVelocity the last angular velocity reported
	 */
	public void setLastAngularVelocity(ValueUpdate<ThreeDimensionalSpaceVector> lastAngularVelocity) {
		this.lastAngularVelocity = lastAngularVelocity;
	}
	
	
}

