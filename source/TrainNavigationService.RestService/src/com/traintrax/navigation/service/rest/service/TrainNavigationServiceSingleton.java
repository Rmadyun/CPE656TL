package com.traintrax.navigation.service.rest.service;

import com.traintrax.navigation.service.TrainNavigationService;
import com.traintrax.navigation.service.TrainNavigationServiceInterface;

/**
 * Class is responsible for creating a single instance of the train navigation service for
 * Restful services to use.
 * @author Corey Sanders
 *
 */
public class TrainNavigationServiceSingleton {
	
	//Singleton instance
	private static final TrainNavigationServiceInterface instance = new TrainNavigationService();
	

	/**
	 * Constructor
	 */
	private TrainNavigationServiceSingleton(){
	}
			
	/**
	 * Retrieves the singleton instance of the service to use for creating 
	 * repositories
	 * @return Singleton instance of the service to use for creating 
	 * repositories
	 */
	public static TrainNavigationServiceInterface getInstance(){
		return instance;
	}

}
