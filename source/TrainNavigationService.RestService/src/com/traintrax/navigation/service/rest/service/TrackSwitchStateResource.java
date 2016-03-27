package com.traintrax.navigation.service.rest.service;

import org.restlet.data.Form;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import com.google.gson.Gson;
import com.traintrax.navigation.service.TrainNavigationServiceInterface;
import com.traintrax.navigation.service.rest.data.TrackSwitchStateMessage;
import com.traintrax.navigation.trackswitch.SwitchState;

/**
 * Class is responsible for providing access to Track Switch information
 * 
 * @author Corey Sanders
 *
 */
public class TrackSwitchStateResource extends ServerResource {

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

			SwitchState trackSwitchState;
			if (idQuery != null && !idQuery.isEmpty()) {
				trackSwitchState = trainNavigationService.GetSwitchState(idQuery);
			} else {

                throw new Exception("No ID specified");
			}

			
			TrackSwitchStateMessage response = new TrackSwitchStateMessage(idQuery, trackSwitchState.toString());
			
			
			jsonRepresentation = new JsonRepresentation(response);

		} catch (Exception e) {
			throw e;
		}

		return jsonRepresentation;
	}
	
	@Post
	public void acceptJsonRepresentation(JsonRepresentation entity) throws Exception{
		TrackSwitchStateMessage response = null;
		String jsonString = entity.getText();
		Gson gson = new Gson();
		response = gson.fromJson(jsonString, TrackSwitchStateMessage.class);
		
	    TrainNavigationServiceInterface trainNavigationService = TrainNavigationServiceSingleton.getInstance();
	    
	    trainNavigationService.SetSwitchState(response.getSwitchIdentifier(), SwitchState.valueOf(response.getSwitchState()));
	    
	    System.out.println(String.format("Switch state for %s changed to %s", response.getSwitchIdentifier(), response.getSwitchState()));
	}

}
