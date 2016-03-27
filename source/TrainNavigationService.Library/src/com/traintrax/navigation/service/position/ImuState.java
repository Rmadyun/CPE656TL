package com.traintrax.navigation.service.position;

import java.util.Calendar;

import com.traintrax.navigation.service.ValueUpdate;
import com.traintrax.navigation.service.mdu.AccelerometerMeasurement;
import com.traintrax.navigation.service.rotation.EulerAngleRotation;

/**
 * The state of an object as determined from IMU measurements
 * @author death
 *
 */
public class ImuState implements Comparable<ImuState> {
	
	private final double numberOfSecondsSinceLastUpdate;
	private final Calendar timeMeasured;
	private final Acceleration correctedAcceleration;
	private final EulerAngleRotation correctedOrientation;
	
	public ImuState(AccelerometerMeasurement accelerometerMeasurement, ValueUpdate<EulerAngleRotation> orientationUpdate){
		timeMeasured = (Calendar) accelerometerMeasurement.getTimeMeasured().clone();
		numberOfSecondsSinceLastUpdate = accelerometerMeasurement.getNumberOfSecondsSinceLastMeasurement();
		correctedAcceleration = accelerometerMeasurement.getAccelerationMeasurement();
		correctedOrientation = orientationUpdate.getValue();
	}
	
	public ImuState(double numberOfSecondsSinceLastUpdate, Calendar timeMeasured,
			Acceleration correctedAcceleration, EulerAngleRotation correctedOrientation) {
		super();
		this.numberOfSecondsSinceLastUpdate = numberOfSecondsSinceLastUpdate;
		this.timeMeasured = timeMeasured;
		this.correctedAcceleration = correctedAcceleration;
		this.correctedOrientation = correctedOrientation;
	}
	/**
	 * @return the numberOfSecondsSinceLastUpdate
	 */
	public double getNumberOfSecondsSinceLastUpdate() {
		return numberOfSecondsSinceLastUpdate;
	}
	/**
	 * @return the timeMeasured
	 */
	public Calendar getTimeMeasured() {
		return timeMeasured;
	}
	/**
	 * @return the correctedAcceleration
	 */
	public Acceleration getCorrectedAcceleration() {
		return correctedAcceleration;
	}
	/**
	 * @return the correctedOrientation
	 */
	public EulerAngleRotation getCorrectedOrientation() {
		return correctedOrientation;
	}
	
	@Override
	public int compareTo(ImuState otherMeasurement) {
		
	    return Long.compare(this.timeMeasured.getTimeInMillis(), otherMeasurement.timeMeasured.getTimeInMillis());
	}
}
