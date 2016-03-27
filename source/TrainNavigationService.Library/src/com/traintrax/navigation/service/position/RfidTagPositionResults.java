package com.traintrax.navigation.service.position;

import com.traintrax.navigation.service.ValueUpdate;
import com.traintrax.navigation.service.rotation.EulerAngleRotation;

/**
 * Class describes information about the last estimate of position
 * calculated from RFID tag input alone.
 * 
 * @author Corey Sanders
 *
 */
public class RfidTagPositionResults {
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
}