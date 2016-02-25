package com.traintrax.navigation.database.rest.data;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.traintrax.navigation.database.library.RepositoryEntry;
import com.traintrax.navigation.database.library.TrackSwitch;

/**
 * Class represents matches to a track point repository search
 * @author Corey Sanders
 *
 */
public class TrackSwitchSearchResults {
	
	@SerializedName("matches")
	private List<TrackSwitchMatch> trackSwitchMatches;
	
	/**
	 * Constructor
	 * @param matches matches in a track switch repository search
	 */
	public TrackSwitchSearchResults(List<RepositoryEntry<TrackSwitch>> matches){
		trackSwitchMatches = new ArrayList<TrackSwitchMatch>();
		
		for(RepositoryEntry<TrackSwitch> match : matches){
			TrackSwitchMatch trackSwitchMatch = new TrackSwitchMatch(
					match.getId(),
					match.getValue().getSwitchName(),
					match.getValue().getPointId(),
					match.getValue().getPassBlockId(),
					match.getValue().getBypassBlockId());
			
			trackSwitchMatches.add(trackSwitchMatch); 
		}
	}
	
	/**
	 * Retrieves all of the known matches to the search
	 * @return All of the known matches to the search
	 */
	public List<TrackSwitchMatch> getMatches(){
		return trackSwitchMatches;
	}

}
