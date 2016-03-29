package com.traintrax.navigation.database.library;


/**
 * Class describes how to filter searches
 * for RfidTagDetectionNotification objects within a collection or
 * repository.
 * @author Corey Sanders
 *
 */
public class RfidTagDetectedNotificationSearchCriteria {
	
	private String rfidTagValue;
	
	/**
	 * Default Constructor
	 */
	public RfidTagDetectedNotificationSearchCriteria(){
		this("");
	}

	/**
	 * Constructor
	 * @param rfidTagValue Value associated with the RFID Tag of interest
	 */
	public RfidTagDetectedNotificationSearchCriteria(String rfidTagValue) {
		super();
		this.rfidTagValue = rfidTagValue;
	}

	/**
	 * Retrieves the value associated with the RFID Tag of interest
	 * @return Value associated with the RFID Tag of interest
	 */
	public String getRfidTagValue() {
		return rfidTagValue;
	}

	/**
	 * Assigns the value associated with the RFID Tag of interest
	 * @param Value associated with the RFID Tag of interest
	 */
	public void setRfidTagValue(String rfidTagValue) {
		this.rfidTagValue = rfidTagValue;
	}

}
