import java.util.ArrayList;
import java.util.List;

import TrainNavigationDatabase.RepositoryEntry;
import TrainNavigationDatabase.TrackPoint;

/**
 * Class represents matches to a track point repository search
 * @author Corey Sanders
 *
 */
public class TrackPointMatches {
	
	private List<TrackPointMatch> trackPointMatches;
	
	/**
	 * Constructor
	 * @param matches matches in a track point repository search
	 */
	public TrackPointMatches(List<RepositoryEntry<TrackPoint>> matches){
		trackPointMatches = new ArrayList<TrackPointMatch>();
		
		for(RepositoryEntry<TrackPoint> match : matches){
			TrackPointMatch trackPointMatch = new TrackPointMatch(match.getValue().getPointName(),
					match.getValue().getType(),
					match.getValue().getX(),
					match.getValue().getY(),
					match.getValue().getZ(),
					match.getValue().getBlockId(),
					match.getValue().getTagName());
			
			trackPointMatches.add(trackPointMatch);
		}
	}
	
	/**
	 * Retrieves all of the known matches to the search
	 * @return All of the known matches to the search
	 */
	public List<TrackPointMatch> getMatches(){
		return trackPointMatches;
	}

}
