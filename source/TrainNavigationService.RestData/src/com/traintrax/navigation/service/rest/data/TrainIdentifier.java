package com.traintrax.navigation.service.rest.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Class represents matches to a track block repository search
 * @author Corey Sanders
 *
 */
public class TrainIdentifier {
	
	@SerializedName("trainIdentifier")
	@Expose
	private String trainIdentifier;
	
	/**
	 * Constructor
	 * @param trainIdentifier Unique ID associated with the train of interest
	 */
	public TrainIdentifier(String trainIdentifier){
		this.trainIdentifier = trainIdentifier;
	}
	
	/**
	 * Retrieves the train ID
	 * @return Unique ID associated with the train of interest
	 */
	public String getTrainIdentifier(){
		return trainIdentifier;
	}

}
