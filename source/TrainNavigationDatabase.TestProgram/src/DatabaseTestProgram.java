import java.util.Calendar;
import java.util.List;

import com.traintrax.navigation.database.library.*;

public class DatabaseTestProgram {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MySqlDatabaseAdapter mySqlDatabaseAdapter = new MySqlDatabaseAdapter();

		mySqlDatabaseAdapter.connect();
		
		RfidTagDetectedNotificationRepository rfidTagRepo = new RfidTagDetectedNotificationRepository(mySqlDatabaseAdapter);
		
		rfidTagRepo.add(new RfidTagDetectedNotification("00:00:00:00:01", Calendar.getInstance()));
		
		rfidTagRepo.find("1");
	}
	
	private static void adjacencyCheck(GenericDatabaseInterface databaseInterface){
		AdjacentPointRepository adjacentPointRepository = new AdjacentPointRepository(databaseInterface);
		TrackPointRepository trackPointRepository = new TrackPointRepository(databaseInterface);
		
		List<RepositoryEntry<TrackPoint>> points = trackPointRepository.findAll();
		List<RepositoryEntry<AdjacentPoint>> adjacentPoints = adjacentPointRepository.findAll();
		AdjacentPointSearchCriteria adjacentPointSearchCriteria = new AdjacentPointSearchCriteria();
		
		for(RepositoryEntry<AdjacentPoint> point : adjacentPoints){

			Integer pointId = point.getValue().getPointId();
			Integer adjacentPointId = point.getValue().getAdjacentPointId();
			adjacentPointSearchCriteria.setPointId(adjacentPointId.toString());
			List<RepositoryEntry<AdjacentPoint>> matches = adjacentPointRepository.find(adjacentPointSearchCriteria);
			boolean matchFound = false;
			for(RepositoryEntry<AdjacentPoint> matchedPoint : matches)
			{
				if(matchedPoint.getValue().getAdjacentPointId() == point.getValue().getPointId()){
					matchFound = true;
				}
			}
			
			if(!matchFound)
			{
				RepositoryEntry<TrackPoint> pointMatch = trackPointRepository.find(pointId.toString());
				RepositoryEntry<TrackPoint> adjacentPointMatch = trackPointRepository.find(adjacentPointId.toString());

				System.out.printf("Link between %s and %s is NOT bidirectional.", pointMatch.getValue().getPointName(), adjacentPointMatch.getValue().getPointName());				
			}
		}
		
	}
}
