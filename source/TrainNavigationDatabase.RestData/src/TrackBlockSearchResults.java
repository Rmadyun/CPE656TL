import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

import TrainNavigationDatabase.RepositoryEntry;
import TrainNavigationDatabase.TrackBlock;

/**
 * Class represents matches to a track block repository search
 * @author Corey Sanders
 *
 */
public class TrackBlockSearchResults {
	
	@SerializedName("matches")
	private List<TrackBlockMatch> trackBlockMatches;
	
	/**
	 * Constructor
	 * @param matches matches in a track block repository search
	 */
	public TrackBlockSearchResults(List<RepositoryEntry<TrackBlock>> matches){
		trackBlockMatches = new ArrayList<TrackBlockMatch>();
		
		for(RepositoryEntry<TrackBlock> match : matches){
			TrackBlockMatch trackBlockMatch = new TrackBlockMatch(
					match.getId(),
					match.getValue().getBlockName());
			
			trackBlockMatches.add(trackBlockMatch); 
		}
	}
	
	/**
	 * Retrieves all of the known matches to the search
	 * @return All of the known matches to the search
	 */
	public List<TrackBlockMatch> getMatches(){
		return trackBlockMatches;
	}

}
