package com.traintrax.navigation.service.rest.data;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Class represents matches to a track block repository search
 * @author Corey Sanders
 *
 */
public class KnownTrainIdentifiersMessage {
	
	@SerializedName("trainIdentifiers")
	@Expose
	private List<TrainIdentifier> trainIdentifiers;
	
	/**
	 * Constructor
	 * @param trainIdentifiers All of the IDs of trains known on the track
	 */
	public KnownTrainIdentifiersMessage(Collection<TrainIdentifier> trainIdentifiers){
		this.trainIdentifiers = new ArrayList<>(trainIdentifiers);
	}
	
	/**
	 * Retrieves all of the IDs of all of the known trains on the track.
	 * @return All of the IDs of trains known on the track
	 */
	public List<TrainIdentifier> getTrainIdentifiers(){
		return trainIdentifiers;
	}

}
