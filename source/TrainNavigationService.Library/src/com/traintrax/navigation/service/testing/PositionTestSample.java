package com.traintrax.navigation.service.testing;

import com.traintrax.navigation.service.ValueUpdate;
import com.traintrax.navigation.service.position.AccelerometerMeasurement;
import com.traintrax.navigation.service.position.Coordinate;
import com.traintrax.navigation.service.position.GyroscopeMeasurement;
import com.traintrax.navigation.service.position.RfidTagDetectedNotification;

/**
 * Class is responsible for represent information collected
 * for a single point in time about the position of an object.
 * @author Corey Sanders
 *
 */
public class PositionTestSample {
	private AccelerometerMeasurement accelerometerMeasurement;
	private GyroscopeMeasurement gyroscopeMeasurement;
	private RfidTagDetectedNotification rfidTagDetectedNotification;
	private ValueUpdate<Coordinate> rfidTagPosition;
	private ValueUpdate<Coordinate> expectedPosition;
	
	/**
	 * Default Constructor
	 */
	public PositionTestSample(){
		this.accelerometerMeasurement = null;
		this.gyroscopeMeasurement = null;
		this.rfidTagDetectedNotification = null;
		this.rfidTagPosition = null;
		this.expectedPosition = null;
	}
	
	
	/**
	 * Retrieves the accelerometer measurement associated with the sampling
	 * @return the accelerometer measurement associated with the sampling
	 */
	public AccelerometerMeasurement getAccelerometerMeasurement() {
		return accelerometerMeasurement;
	}
	
	/**
	 * Assigns the accelerometer measurement associated with the sampling
	 * @param accelerometerMeasurement the accelerometer measurement associated with the sampling
	 */
	public void setAccelerometerMeasurement(AccelerometerMeasurement accelerometerMeasurement) {
		this.accelerometerMeasurement = accelerometerMeasurement;
	}
	
	/**
	 * Retrieves the gyroscope measurement associated with the sampling
	 * @return the gyroscope measurement associated with the sampling
	 */
	public GyroscopeMeasurement getGyroscopeMeasurement() {
		return gyroscopeMeasurement;
	}
	
	/**
	 * Assigns the gyroscope measurement associated with the sampling
	 * @param gyroscopeMeasurement the gyroscope measurement associated with the sampling
	 */
	public void setGyroscopeMeasurement(GyroscopeMeasurement gyroscopeMeasurement) {
		this.gyroscopeMeasurement = gyroscopeMeasurement;
	}
	
	/**
	 * Retrieves the RFID Tag Detected Notification given with the sampling
	 * @return the the RFID Tag Detected Notification given with the sampling
	 */
	public RfidTagDetectedNotification getRfidTagDetectedNotification() {
		return rfidTagDetectedNotification;
	}
	
	/**
	 * Assigns the RFID Tag Detected Notification given with the sampling
	 * @param rfidTagDetectedNotification the RFID Tag Detected Notification given with the sampling
	 */
	public void setRfidTagDetectedNotification(RfidTagDetectedNotification rfidTagDetectedNotification) {
		this.rfidTagDetectedNotification = rfidTagDetectedNotification;
	}
	
	/**
	 * Retrieves the corresponding position reported for a RFID Tag Detected
	 * notification given with the sampling.
	 * @return the corresponding position reported for a RFID Tag Detected
	 * notification given with the sampling.
	 */
	public ValueUpdate<Coordinate> getRfidTagPosition() {
		return rfidTagPosition;
	}
	
	/**
	 * Assigns the position just detected from passing a RFID Tag
	 * @param rfidTagPosition the corresponding position reported for a RFID Tag Detected
	 * notification given with the sampling.
	 */
	public void setRfidTagPosition(ValueUpdate<Coordinate> rfidTagPosition) {
		this.rfidTagPosition = rfidTagPosition;
	}
	
	/**
	 * Retrieves the expected position of the object at the time of this sampling
	 * @return the expected position of the object at the time of this sampling
	 */
	public ValueUpdate<Coordinate> getExpectedPosition() {
		return expectedPosition;
	}
	
	/**
	 * Assigns the expected position of the object at the time of this sampling
	 * @param expectedPosition the expected position of the object at the time of this sampling
	 */
	public void setExpectedPosition(ValueUpdate<Coordinate> expectedPosition) {
		this.expectedPosition = expectedPosition;
	}
	
	
	

}
