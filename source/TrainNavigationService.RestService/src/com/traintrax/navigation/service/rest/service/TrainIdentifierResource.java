package com.traintrax.navigation.service.rest.service;
import java.util.LinkedList;
import java.util.List;

import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import com.traintrax.navigation.service.TrainNavigationServiceInterface;
import com.traintrax.navigation.service.rest.data.KnownTrainIdentifiersMessage;
import com.traintrax.navigation.service.rest.data.TrainIdentifier;

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
		
		    List<String> trainIds = trainNavigationService.GetKnownTrainIdentifiers();
		    
		    List<TrainIdentifier> trainIdentifiers = new LinkedList<TrainIdentifier>();
		    
		    for(String trainId : trainIds){
		    	TrainIdentifier trainIdentifier = new TrainIdentifier(trainId);
		    	
		    	trainIdentifiers.add(trainIdentifier);
		    }
		    
		    KnownTrainIdentifiersMessage response = new KnownTrainIdentifiersMessage(trainIdentifiers);
			
			jsonRepresentation = new JsonRepresentation(response);
			
		} catch (Exception e) {
			throw e;
		}

		return jsonRepresentation;
	}

}
