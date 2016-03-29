package com.traintrax.navigation.database.library;


/**
 * Class describes how to filter searches
 * for RfidTagDetectionNotification objects within a collection or
 * repository.
 * @author Corey Sanders
 *
 */
public class RfidTagDetectedNotificationSearchCriteria {
	
	private String tagName;

	/**
	 * Constructor
	 * @param tagName Value associated with the RFID Tag of interest
	 */
	public RfidTagDetectedNotificationSearchCriteria(String tagName) {
		super();
		this.tagName = tagName;
	}

	/**
	 * Retrieves the value associated with the RFID Tag of interest
	 * @return Value associated with the RFID Tag of interest
	 */
	public String getTagName() {
		return tagName;
	}

	/**
	 * Assigns the value associated with the RFID Tag of interest
	 * @param Value associated with the RFID Tag of interest
	 */
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

}
