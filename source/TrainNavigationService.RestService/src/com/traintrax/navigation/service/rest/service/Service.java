package com.traintrax.navigation.service.rest.service;
import org.restlet.Component;
import org.restlet.data.Protocol;

/**
 * Class responsible for launching the Restful Service
 * of the Train Navigation Database
 * @author Corey Sanders
 *
 */
public class Service {

	/**
	 * Main function invoked when the service launches.
	 * @param args
	 * @throws Exception throws an exception if starting the web service fails.
	 */
	public static void main(String[] args) throws Exception {
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
