package com.traintrax.navigation.service.rest.service;

import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import com.google.gson.Gson;
import com.traintrax.navigation.service.TrainNavigationServiceInterface;
import com.traintrax.navigation.service.position.TrainPositionEstimate;
import com.traintrax.navigation.service.rest.data.TrainPositionUpdateMessage;

/**
 * Class is responsible for providing access to Track Point information
 * 
 * @author Corey Sanders
 *
 */
public class TrainPositionResource extends ServerResource {

	// Search criteria
	private static final String idQueryParameter = "id";


	@Get
	public Representation toJson() throws ResourceException, Exception {
		Representation jsonRepresentation = null;

		try {
			//Grab the service
		    TrainNavigationServiceInterface trainNavigationService = TrainNavigationServiceSingleton.getInstance();
		    
			// Extract parameters from the request
			Form query = getQuery();

			String idQuery = query.getValues(idQueryParameter);

			TrainPositionEstimate trainPosition;
			if (idQuery != null && !idQuery.isEmpty()) {
				trainPosition = trainNavigationService.GetLastKnownPosition(idQuery);
			} else {

                throw new Exception("No ID specified");
			}

			TrainPositionUpdateMessage response = new TrainPositionUpdateMessage(idQuery, trainPosition.getPosition().getX(),
					trainPosition.getPosition().getY(), trainPosition.getPosition().getZ(),
					trainPosition.getVelocity().getX(), trainPosition.getVelocity().getY(), trainPosition.getVelocity().getZ(),
					trainPosition.getTimeAtPosition());
			
			Gson gson = new Gson();
			
			String serializedString = gson.toJson(response);
			
			//jsonRepresentation = new JsonRepresentation(response);
			
			jsonRepresentation = new StringRepresentation(serializedString);

		} catch (Exception e) {
			throw e;
		}

		return jsonRepresentation;
	}

}
