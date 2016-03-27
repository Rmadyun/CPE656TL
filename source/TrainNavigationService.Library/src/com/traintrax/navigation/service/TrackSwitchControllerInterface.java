package com.traintrax.navigation.service;

import com.traintrax.navigation.trackswitch.SwitchState;

/**
 * Interface facilitates communication with switch controllers
 * on the Positive Train Control Test Bed
 * @author death
 *
 */
public interface TrackSwitchControllerInterface {

	/**
	 * Requests that a switches' state be changed.
	 * @param switchIdentifier Unique identifier for the desired switch to change
	 * @param switchState State to change the targeted switch
	 */
	void ChangeSwitchState(String switchIdentifier, SwitchState switchState);

	/**
	 * Retrieves the last known state of a given switch
	 * @param switchIdentifier Unique ID for the switch of interest
	 * @return Current state of the desired switch
	 */
	SwitchState getSwitchState(String switchIdentifier);
}
