import java.util.List;

import TrainNavigationDatabase.*;


public class DatabaseTestProgram {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
        MySqlDatabaseAdapter mySqlDatabaseAdapter = new MySqlDatabaseAdapter();
        
        mySqlDatabaseAdapter.connect();
        
        TrackPointRepository trackPointRepository = new TrackPointRepository(mySqlDatabaseAdapter);
        
        List<RepositoryEntry<TrackPoint>> allEntries = trackPointRepository.findAll();
        RepositoryEntry<TrackPoint> secondEntry = trackPointRepository.find("2");
        
        TrackPoint newTrackPoint = new TrackPoint("Name", "Point", 1, 1, 1, "2", "Tag");
        
        RepositoryEntry<TrackPoint> addedEntry = trackPointRepository.add(newTrackPoint);
 
        trackPointRepository.remove(addedEntry.getId());
        
	}

}
