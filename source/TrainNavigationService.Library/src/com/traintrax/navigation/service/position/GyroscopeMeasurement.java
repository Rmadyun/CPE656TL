package com.traintrax.navigation.service.position;

import java.util.Calendar;

/**
 * @author Corey Sanders
 *Class represents a single measurement taken from a gyroscope for
 *angular velocity expressed in Euler-Angle rotation format.
 */
public class GyroscopeMeasurement implements Comparable<GyroscopeMeasurement> {

	private double radiansRotationPerSecondAlongXAxis;
	private double radiansRotationPerSecondAlongYAxis;
	private double radiansRotationPerSecondAlongZAxis;
	private double numberOfSecondsSinceLastMeasurement;
	private Calendar timeMeasured;
	
	/**
	 * Constructor
	 * @param radiansRotationPerSecondAlongXAxis Measurement of angular velocity along the X-Axis in radians per second
	 * @param radiansRotationPerSecondAlongYAxis Measurement of angular velocity along the Y-Axis in radians per second
	 * @param radiansRotationPerSecondAlongZAxis Measurement of angular velocity along the Z-Axis in radians per second
	 * @param numberOfSecondsSinceLastMeasurement Number of seconds that elapsed between this measurement and the previous measurement
	 * @param timeMeasured Time that the measurement was made
	 */
	public GyroscopeMeasurement(double radiansRotationPerSecondAlongXAxis,
			double radiansRotationPerSecondAlongYAxis,
			double radiansRotationPerSecondAlongZAxis,
			double numberOfSecondsSinceLastMeasurement,
			Calendar timeMeasured){
		
		this.radiansRotationPerSecondAlongXAxis = radiansRotationPerSecondAlongXAxis;
		this.radiansRotationPerSecondAlongYAxis = radiansRotationPerSecondAlongYAxis;
		this.radiansRotationPerSecondAlongZAxis = radiansRotationPerSecondAlongZAxis;
		this.numberOfSecondsSinceLastMeasurement = numberOfSecondsSinceLastMeasurement;
		this.timeMeasured = timeMeasured;
		
		//TODO: Create an AngularVelocity vector class 
		//TODO: Replace measurements with the AngularVelocity vector
		//TODO: Update helper utility functions to use the
		//Angular Velocity class instead of directly the Gyroscope
		//measurement.
	}
	
	/**
	 * Describes the rate of rotation from the original position
	 * along the X axis of the target inertial reference frame in
	 * radians per second.
	 * @return Rate of rotation along the X-Axis in radians per second.
	 */
	public double getRadiansRotationPerSecondAlongXAxis(){
		return radiansRotationPerSecondAlongXAxis;
	}

	/**
	 * Describes the rate of rotation from the original position
	 * along the Y axis of the target inertial reference frame in
	 * radians per second.
	 * @return Rate of rotation along the Y-Axis in radians per second.
	 */	public double getRadiansRotationPerSecondAlongYAxis() {
		return radiansRotationPerSecondAlongYAxis;
	}
	
    /**
	 * Describes the rate of rotation from the original position
	 * along the Z axis of the target inertial reference frame in
	 * radians per second.
	 * @return Rate of rotation along the Z-Axis in radians per second.
	 */
	public double getRadiansRotationPerSecondAlongZAxis() {
		return radiansRotationPerSecondAlongZAxis;
	}

	/**
	 * Describes the time that the gyroscope measurement was taken
	 * @return Time that measurement was taken
	 */
	public Calendar getTimeMeasured(){
		return timeMeasured;
	}
	

	/**
	 * Describes the number of seconds that have elapsed since the
	 * the last reported measurement.
	 * @return Number of seconds that have elapsed since the
	 * the last reported measurement.
	 */
	public double getNumberOfSecondsSinceLastMeasurement(){
		return numberOfSecondsSinceLastMeasurement;
	}

	@Override
	public int compareTo(GyroscopeMeasurement otherMeasurement) {
		
		return Long.compare(this.timeMeasured.getTimeInMillis(), otherMeasurement.timeMeasured.getTimeInMillis());
	}

}
