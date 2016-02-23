import java.util.List;

import TrainNavigationDatabase.FilteredSearchReadOnlyRepositoryInterface;
import TrainNavigationDatabase.RepositoryEntry;
import TrainNavigationDatabase.TrackPoint;
import TrainNavigationDatabase.TrackPointSearchCriteria;

/**
 * The purpose of this class is so that test implementations of repositories my be run to verify
 * that they work correctly
 * @author Corey Sanders
 *
 */
public class TestProgramForTestInterfaces {

	/**
	 * Main Entry Point for test program
	 * @param args Program Arguments
	 */
	public static void main(String[] args) {
		
		FilteredSearchReadOnlyRepositoryInterface<TrackPoint, TrackPointSearchCriteria> repo = new TestTrackPointRepository();
		
		List<RepositoryEntry<TrackPoint>> allEntries = repo.findAll();
		
		for(RepositoryEntry<TrackPoint> entry : allEntries){
			System.out.println(entry.getValue().getPointName());
		}
	}

}
