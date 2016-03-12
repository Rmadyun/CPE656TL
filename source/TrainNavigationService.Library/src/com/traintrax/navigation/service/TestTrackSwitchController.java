package com.traintrax.navigation.service;
import java.util.HashMap;
import java.util.Map;

import com.traintrax.navigation.service.TrackSwitchControllerInterface;
import com.traintrax.navigation.service.trackswitch.SwitchState;

public class TestTrackSwitchController implements TrackSwitchControllerInterface {

	private Map<String,SwitchState> switchStateLut = new HashMap<String,SwitchState>();
	
	
	@Override
	public void ChangeSwitchState(String switchIdentifier, SwitchState switchState) {
		
		switchStateLut.put(switchIdentifier, switchState);
	}

	@Override
	public SwitchState getSwitchState(String switchIdentifier) {

		SwitchState switchState = SwitchState.ByPass;
		if(switchStateLut.containsKey(switchIdentifier))
		{
			switchState = switchStateLut.get(switchIdentifier);
		}
		
		return switchState;
	}

}
