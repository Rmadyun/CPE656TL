import java.util.ArrayList;
import java.util.List;

import TrainNavigationDatabase.RepositoryEntry;
import TrainNavigationDatabase.TrackBlock;
import TrainNavigationDatabase.TrackPoint;

/**
 * Class represents matches to a track block repository search
 * @author Corey Sanders
 *
 */
public class TrackBlockSearchResults {
	
	private List<TrackBlockMatch> trackBlockMatches;
	
	/**
	 * Constructor
	 * @param matches matches in a track block repository search
	 */
	public TrackBlockSearchResults(List<RepositoryEntry<TrackBlock>> matches){
		trackBlockMatches = new ArrayList<TrackBlockMatch>();
		
		for(RepositoryEntry<TrackBlock> match : matches){
			TrackBlockMatch trackBlockMatch = new TrackBlockMatch(match.getValue().getBlockName());
			
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
