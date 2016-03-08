package com.traintrax.navigation.service;

import java.util.HashMap;
import java.util.Map;

import com.traintrax.navigation.service.trackswitch.SwitchState;

/**
 * Class facilitates communication with switch controllers
 * on the Positive Train Control Test Bed
 * @author death
 *
 */
public class TrainController implements TrainControllerInterface {
	private Map<String,SwitchState> switchStateLut = new HashMap<String,SwitchState>();
	
	
	/**
	 * Requests that a switches' state be changed.
	 * @param switchIdentifier Unique identifier for the desired switch to change
	 * @param switchState State to change the targeted switch
	 */
	public void ChangeSwitchState(String switchIdentifier, SwitchState switchState){
		//TODO: Implement switch control
		
		switchStateLut.put(switchIdentifier, switchState);
	}

	/**
	 * Retrieves the last known state of a given switch
	 * @param switchIdentifier Unique ID for the switch of interest
	 * @return Current state of the desired switch
	 */
	public SwitchState getSwitchState(String switchIdentifier) {
		
		return switchStateLut.get(switchIdentifier);
	}

}
