package com.traintrax.navigation.database.rest.service;

import org.restlet.data.Form;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import com.traintrax.navigation.database.rest.data.TrainPositionUpdateMessage;
import com.traintrax.navigation.service.TrainNavigationServiceInterface;
import com.traintrax.navigation.service.position.Coordinate;
import com.traintrax.navigation.service.position.ValueUpdate;

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

			ValueUpdate<Coordinate> trainPosition;
			if (idQuery != null && !idQuery.isEmpty()) {
				trainPosition = trainNavigationService.GetLastKnownPosition(idQuery);
			} else {

                throw new Exception("No ID specified");
			}

			TrainPositionUpdateMessage response = new TrainPositionUpdateMessage(idQuery, trainPosition.getValue().getX(),
					trainPosition.getValue().getY(), trainPosition.getValue().getZ(),
					trainPosition.getTimeObserved());
			
			
			jsonRepresentation = new JsonRepresentation(response);

		} catch (Exception e) {
			throw e;
		}

		return jsonRepresentation;
	}

}
