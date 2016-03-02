package edu.uah.cpe.traintrax;
import java.util.List;

/* import com.traintrax.navigation.database.library.RepositoryEntry;
import com.traintrax.navigation.database.library.TrackBlock; */

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
