package com.traintrax.navigation.service.rest.data;

import com.google.gson.annotations.SerializedName;

/**
 * Describes the mode of operation for a given switch.
 * @author Corey Sanders
 *
 */
public class TrackSwitchStateMessage {
	
	@SerializedName("switchIdentifier")
	private String switchIdentifier;
	
	@SerializedName("switchState")
	private String switchState;
	
	/**
	 * Constructor
	 * @param switchIdentifier Unique ID for the switch
	 * @param switchState State of the Switch
	 */
	public TrackSwitchStateMessage(String switchIdentifier, String switchState) {
		super();
		this.switchIdentifier = switchIdentifier;
		this.switchState = switchState;
	}

	/**
	 * Unique ID for the switch.
	 * @return the switchIdentifier
	 */
	public String getSwitchIdentifier() {
		return switchIdentifier;
	}

	/**
	 * Retrieve the state of the switch
	 * @return the state of the switch
	 */
	public String getSwitchState() {
		return switchState;
	}
	
}
