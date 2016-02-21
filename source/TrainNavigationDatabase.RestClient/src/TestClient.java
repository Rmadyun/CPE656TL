import java.util.List;

import TrainNavigationDatabase.RepositoryEntry;
import TrainNavigationDatabase.TrackBlock;

public class TestClient {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		RemoteTrackBlockRepository repo = new RemoteTrackBlockRepository();
		
		List<RepositoryEntry<TrackBlock>> allEntries = repo.findAll();
		
		for(RepositoryEntry<TrackBlock> entry : allEntries){
			System.out.println(entry.getValue().getBlockName());
		}
		

	}

}
