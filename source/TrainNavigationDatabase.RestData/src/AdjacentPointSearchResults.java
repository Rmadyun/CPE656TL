import java.util.ArrayList;
import java.util.List;

import TrainNavigationDatabase.AdjacentPoint;
import TrainNavigationDatabase.RepositoryEntry;

/**
 * Class represents matches to an adjacent point repository search
 * @author Corey Sanders
 *
 */
public class AdjacentPointSearchResults {
	
	private List<AdjacentPointMatch> adjacentPointMatches;
	
	/**
	 * Constructor
	 * @param matches matches in a adjacent point repository search
	 */
	public AdjacentPointSearchResults(List<RepositoryEntry<AdjacentPoint>> matches){
		adjacentPointMatches = new ArrayList<AdjacentPointMatch>();
		
		for(RepositoryEntry<AdjacentPoint> match : matches){
			AdjacentPointMatch trackPointMatch = new AdjacentPointMatch(
					Integer.toString(match.getValue().getPointId()),
					Integer.toString(match.getValue().getAdjacenPointId()));
			
			adjacentPointMatches.add(trackPointMatch); 
		}
	}
	
	/**
	 * Retrieves all of the known matches to the search
	 * @return All of the known matches to the search
	 */
	public List<AdjacentPointMatch> getMatches(){
		return adjacentPointMatches;
	}

}
