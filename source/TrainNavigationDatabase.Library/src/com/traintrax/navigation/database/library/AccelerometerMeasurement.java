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
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(metersPerSecondSquaredAlongXAxis);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(metersPerSecondSquaredAlongYAxis);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(metersPerSecondSquaredAlongZAxis);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((timeMeasured == null) ? 0 : timeMeasured.hashCode());
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AccelerometerMeasurement))
			return false;
		AccelerometerMeasurement other = (AccelerometerMeasurement) obj;
		if (Double.doubleToLongBits(metersPerSecondSquaredAlongXAxis) != Double
				.doubleToLongBits(other.metersPerSecondSquaredAlongXAxis))
			return false;
		if (Double.doubleToLongBits(metersPerSecondSquaredAlongYAxis) != Double
				.doubleToLongBits(other.metersPerSecondSquaredAlongYAxis))
			return false;
		if (Double.doubleToLongBits(metersPerSecondSquaredAlongZAxis) != Double
				.doubleToLongBits(other.metersPerSecondSquaredAlongZAxis))
			return false;
		if (timeMeasured == null) {
			if (other.timeMeasured != null)
				return false;
		} else if (Math.abs(timeMeasured.getTimeInMillis() - other.timeMeasured.getTimeInMillis()) > 1000) //they are not equal if they are more than 500 ms difference between each other.
			return false;
		return true;
	}
	
	

}
