package com.traintrax.navigation.service.rest.client;
import java.io.IOException;
import java.util.List;

import com.traintrax.navigation.trackswitch.SwitchState;

public class TestClient {

	public static void main(String[] args) {
		
		RemoteTrainNavigationService service = new RemoteTrainNavigationService();
		
		List<String> trainIds = service.GetKnownTrainIdentifiers();
		
		for(String entry : trainIds){
			System.out.println(entry);
		}
		
		try {
			service.SetSwitchState("1", SwitchState.ByPass);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
