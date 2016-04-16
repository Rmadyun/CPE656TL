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
	
	private static final String DEFAULT_MDU_SERIAL_PORT="/dev/null";
	private static final String DEFAULT_PR3_SERIAL_PORT="/dev/null"; 
	
	
	//Singleton instance
	private static final TrainNavigationServiceInterface instance = new TrainNavigationService(DEFAULT_MDU_SERIAL_PORT, DEFAULT_PR3_SERIAL_PORT);
	

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
