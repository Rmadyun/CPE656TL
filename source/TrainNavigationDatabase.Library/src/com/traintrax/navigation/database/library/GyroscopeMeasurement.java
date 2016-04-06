package com.traintrax.navigation.database.library;

import java.util.Calendar;

/**
 * Represents a single collected gyroscope measurement
 * @author Corey Sanders
 *
 */
public class GyroscopeMeasurement implements Comparable<GyroscopeMeasurement> {
	private double radiansRotationPerSecondAlongXAxis;
	private double radiansRotationPerSecondAlongYAxis;
	private double radiansRotationPerSecondAlongZAxis;
	private Calendar timeMeasured;
	
	/**
	 * Constructor
	 * @param radiansRotationPerSecondAlongXAxis Measurement of angular velocity along the X-Axis in radians per second
	 * @param radiansRotationPerSecondAlongYAxis Measurement of angular velocity along the Y-Axis in radians per second
	 * @param radiansRotationPerSecondAlongZAxis Measurement of angular velocity along the Z-Axis in radians per second
	 * @param timeMeasured Time that the measurement was made
	 */
	public GyroscopeMeasurement(double radiansRotationPerSecondAlongXAxis,
			double radiansRotationPerSecondAlongYAxis,
			double radiansRotationPerSecondAlongZAxis,
			Calendar timeMeasured){
		
		this.radiansRotationPerSecondAlongXAxis = radiansRotationPerSecondAlongXAxis;
		this.radiansRotationPerSecondAlongYAxis = radiansRotationPerSecondAlongYAxis;
		this.radiansRotationPerSecondAlongZAxis = radiansRotationPerSecondAlongZAxis;
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

	@Override
	public int compareTo(GyroscopeMeasurement otherMeasurement) {
		
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
		temp = Double.doubleToLongBits(radiansRotationPerSecondAlongXAxis);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(radiansRotationPerSecondAlongYAxis);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(radiansRotationPerSecondAlongZAxis);
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
		if (!(obj instanceof GyroscopeMeasurement))
			return false;
		GyroscopeMeasurement other = (GyroscopeMeasurement) obj;
		if (Double.doubleToLongBits(radiansRotationPerSecondAlongXAxis) != Double
				.doubleToLongBits(other.radiansRotationPerSecondAlongXAxis))
			return false;
		if (Double.doubleToLongBits(radiansRotationPerSecondAlongYAxis) != Double
				.doubleToLongBits(other.radiansRotationPerSecondAlongYAxis))
			return false;
		if (Double.doubleToLongBits(radiansRotationPerSecondAlongZAxis) != Double
				.doubleToLongBits(other.radiansRotationPerSecondAlongZAxis))
			return false;
		if (timeMeasured == null) {
			if (other.timeMeasured != null)
				return false;
		} else if (Math.abs(timeMeasured.getTimeInMillis() - other.timeMeasured.getTimeInMillis()) > 1000) //they are not equal if they are more than 500 ms difference between each other.
			return false;
		return true;
	}
}
