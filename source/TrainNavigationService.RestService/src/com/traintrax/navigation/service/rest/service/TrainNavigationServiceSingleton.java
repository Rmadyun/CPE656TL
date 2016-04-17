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
	
	//Configuration for the service.
	private static TrainNavigationServiceConfiguration serviceConfiguration;
	
	//Singleton instance
	private static TrainNavigationServiceInterface instance;
	
	/**
	 * Default Constructor
	 */
	private TrainNavigationServiceSingleton(){
	}
	
	/**
	 * Retrieves the current configuration for the Train Navigation Service
	 * @return current configuration for the Train Navigation Service.
	 */
	public TrainNavigationServiceConfiguration getServiceConfiguration() {
		return serviceConfiguration;
	}

	/**
	 * Completes initialization of the instances for use.
	 * @param serviceConfiguration Configuration to use to initialize the navigation service.
	 * @throws Exception Reports if initialization of the service fails
	 */
	public static void initialize(TrainNavigationServiceConfiguration serviceConfiguration) throws Exception {
		TrainNavigationServiceSingleton.serviceConfiguration = serviceConfiguration;
		instance = new TrainNavigationService(serviceConfiguration.getMduSerialPort(), serviceConfiguration.getPr3SerialPort(),
				serviceConfiguration.getDbHost(), serviceConfiguration.getDbPort(), serviceConfiguration.getDbName(),
				serviceConfiguration.getDbUsername(), serviceConfiguration.getDbPassword());
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
