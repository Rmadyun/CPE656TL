package com.traintrax.navigation.service.position;

import com.traintrax.navigation.service.ValueUpdate;
import com.traintrax.navigation.service.math.Triplet;
import com.traintrax.navigation.service.math.Tuple;
import com.traintrax.navigation.service.rotation.EulerAngleRotation;

/**
 * Class is responsible for determining the current state of a given train based
 * solely on the inputs from position truth provided. NOTE: This is assuming
 * only 2D movement along the X & Y Axes.
 * 
 * @author Corey Sanders This calibrator assumes that the values provided to it
 *         are from an object that is stationary. It is also assuming that
 *         gravity is the only think acting on it.
 */
public class DiscretePositionCalculator {
	private ValueUpdate<Triplet<Coordinate, EulerAngleRotation, Velocity>> lastReliableTrainPositionEstimate;
	private ValueUpdate<Coordinate> previousPosition;
	private EulerAngleRotation previousOrientationEstimate;

	/**
	 * Constructor
	 */
	public DiscretePositionCalculator() {
		lastReliableTrainPositionEstimate = null;
		previousPosition = null;
		previousOrientationEstimate = null;
	}

	/**
	 * Calculates detailed information about the state of the train
	 * 
	 * @param currentPosition
	 *            Confirmed current position of the train
	 * @return Detailed information about the current position of the object.
	 *         This is null if it cannot be determined.
	 */
	public Tuple<EulerAngleRotation, Velocity> updatePosition(ValueUpdate<Coordinate> currentPosition) {
		Tuple<EulerAngleRotation, Velocity> additionalTrainStateInfo = null;

		if(previousPosition == null) {
			previousPosition = currentPosition;
		}
		else if (previousPosition != null) {
			ValueUpdate<EulerAngleRotation> newOrientationEstimate = calculateOrientation(previousPosition, currentPosition);
			final double tolerance = 0.05; // 5 % tolerance

			if (previousOrientationEstimate != null && newOrientationEstimate != null) {

				//Make sure that the orientation is stable enough for us to 
				//assume that the train is at the calculated orientation
				double error = (newOrientationEstimate.getValue().getRadiansRotationAlongZAxis()
						- previousOrientationEstimate.getRadiansRotationAlongZAxis());

				error /= previousOrientationEstimate.getRadiansRotationAlongZAxis();

				error = Math.abs(error);

				if (error < tolerance) {
					
					//Average out the difference between the points to reduce the worst-case error.
					ValueUpdate<EulerAngleRotation> temp = newOrientationEstimate;
					
					newOrientationEstimate = new ValueUpdate<EulerAngleRotation>(
							new EulerAngleRotation((previousOrientationEstimate.getRadiansRotationAlongXAxis()+newOrientationEstimate.getValue().getRadiansRotationAlongXAxis())/2,
									(previousOrientationEstimate.getRadiansRotationAlongYAxis()+newOrientationEstimate.getValue().getRadiansRotationAlongYAxis())/2,
									(previousOrientationEstimate.getRadiansRotationAlongZAxis()+newOrientationEstimate.getValue().getRadiansRotationAlongZAxis())/2),
							temp.getTimeObserved());

					ValueUpdate<Velocity> velocity = calculateVelocity(previousPosition, currentPosition,
							newOrientationEstimate.getValue());
					additionalTrainStateInfo = new Tuple<EulerAngleRotation, Velocity>(newOrientationEstimate.getValue(),
							velocity.getValue());
					
					lastReliableTrainPositionEstimate = new ValueUpdate<Triplet<Coordinate, EulerAngleRotation, Velocity>> (new Triplet<Coordinate, EulerAngleRotation, Velocity>(currentPosition.getValue(), newOrientationEstimate.getValue(), velocity.getValue()), currentPosition.getTimeObserved());
				}
			}

			// Update the previous orientation estimate
			if(newOrientationEstimate != null)
			{
			    previousOrientationEstimate = newOrientationEstimate.getValue();
			}
			
			//always update the previous position even if there is not change
			//so that the timing used in the calculation is correct
			previousPosition = currentPosition;
		}

		return additionalTrainStateInfo;
	}

	/**
	 * Determines the current orientation of the train from RFID tag
	 * measurements This implementation is assuming a 2-dimensional train track
	 * where the NED frame is used, but there are not any elevation changes on
	 * the track (i.e. the track is flat) and on a level-table so that gravity
	 * is only acting on the Z-axis.
	 * 
	 * @param initialPosition
	 *            Initial position of the train
	 * @param finalPosition
	 *            Final position of the train
	 * @return Calculated orientation of the train. If it cannot be determined,
	 *         then null is returned.
	 */
	private ValueUpdate<EulerAngleRotation> calculateOrientation(ValueUpdate<Coordinate> initialPosition,
			ValueUpdate<Coordinate> finalPosition) {

		double dx = finalPosition.getValue().getX() - initialPosition.getValue().getX();
		double dy = finalPosition.getValue().getY() - initialPosition.getValue().getY();
		double distance = Math.sqrt(dx * dx + dy * dy);

		ValueUpdate<EulerAngleRotation> estimatedOrientation = null;

		if (distance != 0) {
			// Formula from the following:
			// https://en.wikipedia.org/wiki/Unit_circle
			// http://stackoverflow.com/questions/7586063/how-to-calculate-the-angle-between-a-line-and-the-horizontal-axis
			double yaw = Math.atan2(dy, dx);

			estimatedOrientation = new ValueUpdate<EulerAngleRotation>(new EulerAngleRotation(0, 0, yaw),
					finalPosition.getTimeObserved());
		}

		return estimatedOrientation;
	}

	/**
	 * Determines the current velocity of the train from RFID tag measurements
	 * This implementation is assuming a 2-dimensional train track where the NED
	 * frame is used, but there are not any elevation changes on the track (i.e.
	 * the track is flat) and on a level-table so that gravity is only acting on
	 * the Z-axis.
	 * 
	 * @param initialPosition
	 *            Initial position of the train
	 * @param finalPosition
	 *            Final position of the train
	 * @param finalOrientation
	 *            Final orientation of the train when at the final position
	 * @return An estimate on the velocity of the train.
	 */
	private ValueUpdate<Velocity> calculateVelocity(ValueUpdate<Coordinate> initialPosition,
			ValueUpdate<Coordinate> finalPosition, EulerAngleRotation finalOrientation) {

		double dx = finalPosition.getValue().getX() - initialPosition.getValue().getX();
		double dy = finalPosition.getValue().getY() - initialPosition.getValue().getY();
		double distance = Math.sqrt(dx * dx + dy * dy);
		double dt = (finalPosition.getTimeObserved().getTimeInMillis()
				- initialPosition.getTimeObserved().getTimeInMillis()) / 1000.0;

		double dVx = (distance / dt) * Math.cos(finalOrientation.getRadiansRotationAlongZAxis());
		double dVy = (distance / dt) * Math.sin(finalOrientation.getRadiansRotationAlongZAxis());

		ValueUpdate<Velocity> velocityUpdate = new ValueUpdate<Velocity>(new Velocity(dVx, dVy, 0),
				finalPosition.getTimeObserved());

		return velocityUpdate;
	}

	/**
	 * Returns the last information that was able to be reliably calculated about the position / state of the target object
	 * @return the last information that was able to be reliably calculated about the position / state of the target object
	 * This includes in order: position (meters), orientation (radians), velocity (meters per second)
	 */
	public ValueUpdate<Triplet<Coordinate, EulerAngleRotation, Velocity>> getLastReliableTrainPositionEstimate() {
		return lastReliableTrainPositionEstimate;
	}
	
}
