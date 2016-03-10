package com.traintrax.navigation.database.library;

import java.util.Calendar;


/**
 * Represents a single collected accelerometer measurement
 * @author Corey Sanders
 *
 */
public class AccelerometerMeasurement implements Comparable<AccelerometerMeasurement> {
	private Calendar timeMeasured;
	private double metersPerSecondSquaredAlongXAxis;
	private double metersPerSecondSquaredAlongYAxis;
	private double metersPerSecondSquaredAlongZAxis;
	
	/**
	 * Constructor
	 * @param metersPerSecondSquaredAlongXAxis Acceleration measured from the X-axis in meters per second squared
	 * @param metersPerSecondSquaredAlongYAxis Acceleration measured from the Y-axis in meters per second squared
	 * @param metersPerSecondSquaredAlongZAxis Acceleration measured from the Z-axis in meters per second squared 
	 * @param timeMeasured Time that accelerometer measurement was taken.
	 */
	public AccelerometerMeasurement(double metersPerSecondSquaredAlongXAxis,
			double metersPerSecondSquaredAlongYAxis,
			double metersPerSecondSquaredAlongZAxis,
			Calendar timeMeasured) {
		
		this.metersPerSecondSquaredAlongXAxis = metersPerSecondSquaredAlongXAxis;
		this.metersPerSecondSquaredAlongYAxis = metersPerSecondSquaredAlongYAxis;
		this.metersPerSecondSquaredAlongZAxis = metersPerSecondSquaredAlongZAxis;
		this.timeMeasured = timeMeasured;
	}
	/**
	 * Describes the acceleration from the original position
	 * along the X axis of the target inertial reference frame in meters per second squared
	 * @return Acceleration measured from the X-axis in meters per second squared
	 */
	public double getMetersPerSecondSquaredAlongXAxis(){
		return metersPerSecondSquaredAlongXAxis;
	}

	/**
	 * Describes the acceleration from the original position
	 * along the Y axis of the target inertial reference frame in meters per second squared
	 * @return Acceleration measured from the Y-axis in meters per second squared
	 */
	public double getMetersPerSecondSquaredAlongYAxis() {
		return metersPerSecondSquaredAlongYAxis;
	}
		
	/**
	 * Describes the acceleration from the original position
	 * along the Z axis of the target inertial reference frame in meters per second squared
	 * @return Acceleration measured from the Z-axis in meters per second squared
	 */
	public double getMetersPerSecondSquaredAlongZAxis() {
		return metersPerSecondSquaredAlongZAxis;
	}

	/**
	 * Describes the time that the accelerometer measurement was taken
	 * @return Time that measurement was taken
	 */
	public Calendar getTimeMeasured(){
		return timeMeasured;
	}

	@Override
	public int compareTo(AccelerometerMeasurement otherMeasurement) {
		
	    return Long.compare(this.timeMeasured.getTimeInMillis(), otherMeasurement.timeMeasured.getTimeInMillis());
	}

}
