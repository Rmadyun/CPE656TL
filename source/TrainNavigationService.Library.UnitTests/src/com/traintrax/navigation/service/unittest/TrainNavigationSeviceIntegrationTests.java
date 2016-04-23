package com.traintrax.navigation.service.unittest;

import java.util.List;

import org.junit.Test;

import com.traintrax.navigation.service.TrainNavigationService;
import com.traintrax.navigation.service.TrainNavigationServiceInterface;
import com.traintrax.navigation.service.position.TrainPositionEstimate;

import junit.framework.Assert;

public class TrainNavigationSeviceIntegrationTests {
	
	/**
	 * Method creates a train navigation service to use for testing
	 * @return A train navigation service instance for testing
	 * @throws Exception 
	 */
	private TrainNavigationServiceInterface CreateTrainNavigationService() throws Exception{
		return new TrainNavigationService();
	}

	@Test
	public void testPositionPoll() throws Exception {
		
		TrainNavigationServiceInterface trainNavigationService = CreateTrainNavigationService();
		
		List<String> trainIds = trainNavigationService.GetKnownTrainIdentifiers();
		
		for(String trainId : trainIds){
			TrainPositionEstimate trainPosition = trainNavigationService.GetLastKnownPosition(trainId);
			
			Assert.assertFalse(trainPosition == null);
		}
		
	}
}
