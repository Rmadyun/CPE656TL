package com.traintrax.navigation.service.mdu;

import java.util.Calendar;

/**
 * Describes an event where a device crosses a RFID Tag
 * @author death
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
