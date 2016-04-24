package com.traintrax.navigation.service.rest.service;
import java.util.Calendar;

import org.restlet.Component;
import org.restlet.data.Protocol;

import com.traintrax.navigation.service.TrainNavigationServiceInterface;
import com.traintrax.navigation.service.mdu.MduProtocolParser;
import com.traintrax.navigation.service.mdu.MotionDetectionUnit;
import com.traintrax.navigation.service.mdu.SerialPortMduCommunicationChannel;
import com.traintrax.navigation.service.mdu.SimulatedMotionDetectionUnit;
import com.traintrax.navigation.service.position.Coordinate;
import com.traintrax.navigation.service.rotation.EulerAngleRotation;
import com.traintrax.navigation.service.testing.MduMeasurementGenerator;
import com.traintrax.navigation.service.testing.PositionTestCase;
import com.traintrax.navigation.service.testing.TrainNavigationServiceBuilder;

/**
 * Class responsible for launching a Debug Implementation of 
 * the Restful Service of the Train Navigation Service.
 * This is intended to assist with integration between clients of the 
 * service.
 * @author Corey Sanders
 *
 */
public class DebugService {
	
	
	/**
	 * Method is responsible for create a train Navigation service instance with
	 * a mock track switch controller and a real motion detection unit.
	 * @return new Train Navigation Service instance
	 * @throws Exception Reports any failure to create the Train Navigation Service instance
	 */
	private static TrainNavigationServiceInterface createTrainNavigationServiceWithFakeSwitchControllerAndRealMdu() throws Exception{
		TrainNavigationServiceBuilder builder = TrainNavigationServiceBuilder.getBuilder();
		
		MotionDetectionUnit motionDetectionUnit = new MotionDetectionUnit(new SerialPortMduCommunicationChannel("/dev/ttyUSB0"), new MduProtocolParser());
		
		builder.setMotionDetectionUnitInterface(motionDetectionUnit);
		
		TrainNavigationServiceInterface  trainNavigationServiceInterface = builder.build();
		
		return trainNavigationServiceInterface;
	}

	/**
	 * Main function invoked when the service launches.
	 * @param args
	 * @throws Exception throws an exception if starting the web service fails.
	 */
	public static void main(String[] args) throws Exception {
		
		//Create a train navigation service instance with all simulated dependencies
		TrainNavigationServiceBuilder builder = TrainNavigationServiceBuilder.getBuilder();
		SimulatedMotionDetectionUnit simulatedMotionDetectionUnit = new SimulatedMotionDetectionUnit();
		
		builder.setMotionDetectionUnitInterface(simulatedMotionDetectionUnit);
		
		TrainNavigationServiceInterface  trainNavigationServiceInterface = builder.build();
		
		//Generates a test case where the train moves in a diagonal line and accelerates for 1 second,
		//then moves at a constant speed for 10 seconds
		
		Coordinate currentPosition = new Coordinate(0, 0, 0);
		EulerAngleRotation currentOrientation = new EulerAngleRotation(0, 0, Math.PI / 4);
		double initialSpeedInMetersPerSecond = 1;
		double accelerationInMetersPerSecondSquared = 0;
		int numberOfSeconds = 100;
		int numSamplesBeforeTagEvent = 1;
		double kineticFrictionOffset = 0.35;
		Calendar startTime = Calendar.getInstance();
		
		PositionTestCase positionTestCase = MduMeasurementGenerator.generateStraightLine(currentOrientation.getRadiansRotationAlongZAxis(), currentPosition, initialSpeedInMetersPerSecond, accelerationInMetersPerSecondSquared, numberOfSeconds, startTime, numSamplesBeforeTagEvent, kineticFrictionOffset);

		//Add samples to report information
		//simulatedMotionDetectionUnit.enqueueSamples(positionTestCase.getSamples());
		
		//Initialize the service for use
		TrainNavigationServiceSingleton.initialize(trainNavigationServiceInterface);
		
		//Initializes the Service
		 // Create a new Restlet component and add a HTTP server connector to it
	    Component component = new Component();
	    component.getServers().add(Protocol.HTTP, 8183);

	    // Then attach it to the local host
	    component.getDefaultHost().attach("/TrainPosition", TrainPositionResource.class);
	    component.getDefaultHost().attach("/TrainIdentifiers", TrainIdentifierResource.class);
	    component.getDefaultHost().attach("/TrackSwitchState", TrackSwitchStateResource.class);

	    // Now, let's start the component!
	    // Note that the HTTP server connector is also automatically started.
	    component.start();
	}

}
