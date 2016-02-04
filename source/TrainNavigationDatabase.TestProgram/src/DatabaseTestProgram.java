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
        
        List<TrackPoint> allEntries = trackPointRepository.findAll();
        TrackPoint secondEntry = trackPointRepository.find("2");
        
        TrackPoint newTrackPoint = new TrackPoint(-1, "Name", "Point", 1, 1, 1, "2", "Tag");
        
        String assignedId = trackPointRepository.add(newTrackPoint);
 
        trackPointRepository.remove(assignedId);
        
	}

}
