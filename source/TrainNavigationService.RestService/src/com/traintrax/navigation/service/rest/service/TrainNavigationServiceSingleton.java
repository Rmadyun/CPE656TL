package com.traintrax.navigation.service.rest.service;

import com.traintrax.navigation.service.TrackSwitchController;
import com.traintrax.navigation.service.TrainNavigationDatabaseInterface;
import com.traintrax.navigation.service.TrainNavigationService;
import com.traintrax.navigation.service.TrainNavigationServiceInterface;
import com.traintrax.navigation.service.mdu.MduProtocolParser;
import com.traintrax.navigation.service.mdu.MotionDetectionUnit;
import com.traintrax.navigation.service.mdu.SerialPortMduCommunicationChannel;
import com.traintrax.navigation.service.testing.TrainNavigationServiceBuilder;

/**
 * Class is responsible for creating a single instance of the train navigation
 * service for Restful services to use.
 * 
 * @author Corey Sanders
 *
 */
public class TrainNavigationServiceSingleton {

	// Configuration for the service.
	private static TrainNavigationServiceConfiguration serviceConfiguration;

	// Singleton instance
	private static TrainNavigationServiceInterface instance;

	/**
	 * Default Constructor
	 */
	private TrainNavigationServiceSingleton() {
	}

	/**
	 * Retrieves the current configuration for the Train Navigation Service
	 * 
	 * @return current configuration for the Train Navigation Service.
	 */
	public TrainNavigationServiceConfiguration getServiceConfiguration() {
		return serviceConfiguration;
	}

	/**
	 * Completes initialization of the instances for use.
	 * 
	 * @param serviceConfiguration
	 *            Configuration to use to initialize the navigation service.
	 * @throws Exception
	 *             Reports if initialization of the service fails
	 */
	public static void initialize(TrainNavigationServiceConfiguration serviceConfiguration) throws Exception {
		TrainNavigationServiceSingleton.serviceConfiguration = serviceConfiguration;

		if (!serviceConfiguration.isMduDisabled() && !serviceConfiguration.isPr3Disabled()) {
			instance = new TrainNavigationService(serviceConfiguration.getMduSerialPort(),
					serviceConfiguration.getPr3SerialPort(), serviceConfiguration.getDbHost(),
					serviceConfiguration.getDbPort(), serviceConfiguration.getDbName(),
					serviceConfiguration.getDbUsername(), serviceConfiguration.getDbPassword());
		} else {
			TrainNavigationServiceBuilder builder = TrainNavigationServiceBuilder.getBuilder();

			builder.setTrainNavigationDatabase(serviceConfiguration.getDbHost(), serviceConfiguration.getDbPort(), serviceConfiguration.getDbName(), serviceConfiguration.getDbUsername(), serviceConfiguration.getDbPassword());
			
			TrainNavigationDatabaseInterface trainNavigationDatabase = builder.getTrainNavigationDatabase();			

			if (!serviceConfiguration.isMduDisabled()) {
				MotionDetectionUnit motionDetectionUnit = new MotionDetectionUnit(
						new SerialPortMduCommunicationChannel(serviceConfiguration.getMduSerialPort()),
						new MduProtocolParser());

				builder.setMotionDetectionUnitInterface(motionDetectionUnit);
			}

			if (!serviceConfiguration.isPr3Disabled()) {
				TrackSwitchController trackSwitchController = new TrackSwitchController(serviceConfiguration.getPr3SerialPort(),
						TrackSwitchController.DefaultPrefix, trainNavigationDatabase);

				builder.setTrainController(trackSwitchController);

			}

			instance = builder.build();

		}

	}

	/**
	 * Initializes singleton with a specific instance
	 * 
	 * @param instance
	 *            Instance to assign to the singleton. This method is intended
	 *            to be used to help with debugging.
	 */
	public static void initialize(TrainNavigationServiceInterface trainNavigationServiceInterface) {
		instance = trainNavigationServiceInterface;
	}

	/**
	 * Retrieves the singleton instance of the service to use for creating
	 * repositories
	 * 
	 * @return Singleton instance of the service to use for creating
	 *         repositories
	 */
	public static TrainNavigationServiceInterface getInstance() {
		return instance;
	}

}
