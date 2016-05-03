package com.traintrax.navigation.service.position;

import java.util.Calendar;

/**
 * @author death
 *Class represents a single measurement taken from an accelerometer
 *that expresses the forces acting on an object.
 */
public class AccelerometerMeasurement implements Comparable<AccelerometerMeasurement> {

	private String trainId;
	private Acceleration accelerationMeasurement;
	private double numberOfSecondsSinceLastMeasurement;
	private Calendar timeMeasured;
	
	/**
	 * Constructor
	 * @param trainId Unique ID for train measurement came from
	 * @param accelerationMeasurement Measurement of acceleration from the accelerometer
	 * @param numberOfSecondsSinceLastMeasurement Number of seconds that elapsed between this measurement and the previous measurement 
	 * @param timeMeasured Time that accelerometer measurement was taken.
	 */
	public AccelerometerMeasurement(String trainId, Acceleration accelerationMeasurement,
			double numberOfSecondsSinceLastMeasurement,
			Calendar timeMeasured){
		
		this.trainId = trainId;
		this.accelerationMeasurement = accelerationMeasurement;
		this.numberOfSecondsSinceLastMeasurement = numberOfSecondsSinceLastMeasurement;
		this.timeMeasured = timeMeasured;
	}
		
    /**
     * Measurement of acceleration from the accelerometer
     * @return Measurement of acceleration from the accelerometer
     */
	public Acceleration getAcceleration() {
		return accelerationMeasurement;
	}

	/**
	 * Describes the time that the accelerometer measurement was taken
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
	public int compareTo(AccelerometerMeasurement otherMeasurement) {
		
	    return Long.compare(this.timeMeasured.getTimeInMillis(), otherMeasurement.timeMeasured.getTimeInMillis());
	}

	/**
	 * Retrieves the unique ID for train measurement came from
	 * @return Unique ID for train measurement came from
	 */
	public String getTrainId() {
		return trainId;
	}	
	
}
