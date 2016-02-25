package com.traintrax.navigation.service;

/**
 * Representation of the Train Navigation Service.
 * @author Corey Sanders
 *
 */
public class TrainNavigationService implements TrainNavigationServiceInterface {
	
	/**
	 * Constructor
	 */
	public TrainNavigationService(){
		
	}
		
	public static void PrintMatrix(Matrix matrix){
		
		for(int i = 0; i < matrix.getNumberOfRows(); i++){
			String rowString = "";
			for(int j = 0; j < matrix.getNumberOfColumns(); j++){
				rowString += String.format("%f  ", matrix.getValue(i,  j));
			}
			
			System.out.println(rowString);
		}
	}
	
    /**
     * Gets the current state of a given switch
     * @param switchIdentifier Unique identifier for the switch of interest
     * @return Current state of the switch requested
     */
	public SwitchState GetSwitchState(String switchIdentifier){
	
		//TODO: Implement Switch State support
		return SwitchState.Pass;
	}
	
	/**
	 * Changes the state of a given switch
     * @param switchIdentifier Unique identifier for the switch of interest 
	 * @param switchState State requested for the switch to change
	 */
	public void SetSwitchState(String switchIdentifier, SwitchState switchState){
		
		//TODO: Implement 
	}
	
	/**
	 * Subscribe a client to listen to events from the Train Navigation service.
	 * @param subscriber Client requested to listen to service events.
	 */
	public void Subscribe(TrainNavigationServiceEventSubscriber subscriber){
		//TODO: Implement
	}
	
	/**
	 * Unsubscribe a client from listening to events
	 * @param subscriber Client requesting to stop receiving events from the service.
	 */
	public void Unsubscribe(TrainNavigationServiceEventSubscriber subscriber){
		//TODO: Implement
	}


}
