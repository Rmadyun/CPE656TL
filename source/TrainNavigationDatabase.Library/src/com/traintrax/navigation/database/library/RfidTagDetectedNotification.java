package com.traintrax.navigation.database.library;

import java.util.Calendar;

/**
 * Represents an event where a RFID Tag was detected
 * @Corey Sanders
 *
 */
public class RfidTagDetectedNotification {
	private String rfidTagValue;
	private Calendar timeDetected;
	
	/**
	 * Constructor
	 * @param rfidTagValue Value of the RFID Tag that crossed the device.
	 * @param timeDetected The time that the RFID Tag crossing the device was 
	 * detected.
	 */
	public RfidTagDetectedNotification(String rfidTagValue, Calendar timeDetected){
		this.rfidTagValue = rfidTagValue;
		this.timeDetected = timeDetected;
	}
	
	/**
	 * Retrieves the value of the RFID Tag that crossed the
	 * device
	 * @return Value of the RFID Tag that crossed the device.
	 */
	public String getRfidTagValue(){
		return rfidTagValue;
	}
	
	/**
	 * Retrieves the time that the RFID Tag crossing the device
	 * was detected.
	 * @return The time that the RFID Tag crossing the device was 
	 * detected.
	 */
	public Calendar getTimeDetected(){
		return timeDetected;
	}

}
