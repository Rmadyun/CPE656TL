import java.util.List;

import TrainNavigationDatabase.RepositoryEntry;
import TrainNavigationDatabase.TrackPoint;

public class TestClient {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		RemoteTrackPointRepository repo = new RemoteTrackPointRepository();
		
		List<RepositoryEntry<TrackPoint>> allEntries = repo.findAll();
		
		for(RepositoryEntry<TrackPoint> entry : allEntries){
			System.out.println(entry.getValue().getPointName());
		}
		

	}

}
