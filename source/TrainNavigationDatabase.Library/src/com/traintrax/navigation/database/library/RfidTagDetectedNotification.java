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

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((rfidTagValue == null) ? 0 : rfidTagValue.hashCode());
		result = prime * result + ((timeDetected == null) ? 0 : timeDetected.hashCode());
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
		if (!(obj instanceof RfidTagDetectedNotification))
			return false;
		RfidTagDetectedNotification other = (RfidTagDetectedNotification) obj;
		if (rfidTagValue == null) {
			if (other.rfidTagValue != null)
				return false;
		} else if (!rfidTagValue.equals(other.rfidTagValue))
			return false;
		if (timeDetected == null) {
			if (other.timeDetected != null)
				return false;
		} else if (Math.abs(timeDetected.getTimeInMillis() - other.timeDetected.getTimeInMillis()) > 1000) //they are not equal if they are more than 500 ms difference between each other.
			return false;
		return true;
	}
	
	

}
