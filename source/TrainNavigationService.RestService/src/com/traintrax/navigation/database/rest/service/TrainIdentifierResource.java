package com.traintrax.navigation.database.rest.service;
import java.util.List;

import org.restlet.data.Form;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import com.traintrax.navigation.database.rest.data.KnownTrainIdentifiersMessage;
import com.traintrax.navigation.service.TrainNavigationServiceInterface;

/**
 * Class is responsible for providing access to Track Point information
 * 
 * @author Corey Sanders
 *
 */
public class TrainIdentifierResource extends ServerResource {

	@Get
	public Representation toJson() throws ResourceException, Exception {
		Representation jsonRepresentation = null;
		
		try {
			//Grab the service
		    TrainNavigationServiceInterface trainNavigationService = TrainNavigationServiceSingleton.getInstance();	
		
		    KnownTrainIdentifiersMessage response = new KnownTrainIdentifiersMessage(trainNavigationService.GetKnownTrainIdentifiers());
			
			jsonRepresentation = new JsonRepresentation(response);
			
		} catch (Exception e) {
			throw e;
		}

		return jsonRepresentation;
	}

}
