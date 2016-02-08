import static org.junit.Assert.assertEquals;

import org.junit.*;

import TrainNavigationDatabase.FilteredSearchRepositoryInterface;
import TrainNavigationDatabase.MySqlDatabaseAdapter;
import TrainNavigationDatabase.TrackPoint;
import TrainNavigationDatabase.TrackPointRepository;
import TrainNavigationDatabase.TrackPointSearchCriteria;


public class TrackPointRepositoryTests extends FilteredSearchRepositoryInterfaceTests<TrackPoint, TrackPointSearchCriteria> {
	
	FilteredSearchRepositoryInterface<TrackPoint, TrackPointSearchCriteria> createRepository(){
        MySqlDatabaseAdapter mySqlDatabaseAdapter = new MySqlDatabaseAdapter();
        
        mySqlDatabaseAdapter.connect();
        
        TrackPointRepository trackPointRepository = new TrackPointRepository(mySqlDatabaseAdapter);
		
		return trackPointRepository;
	}
	
	TrackPoint createNewEntry(){
		TrackPoint newTrackPoint = new TrackPoint("Name", "Point", 1, 1, 1, "2", "Tag");
		
		return newTrackPoint;
	}
	
	TrackPoint createModifiedEntry(TrackPoint originalEntry){
		TrackPoint updatedTrackPoint = new TrackPoint(originalEntry.getPointName(),
				originalEntry.getType(),
				originalEntry.getX()+1,
				originalEntry.getY()+1,
				originalEntry.getZ()+1,
				originalEntry.getBlockId(),
				originalEntry.getTagName());
		
		return updatedTrackPoint;
	}
	
	@Test
	public void TestAdd(){
		FilteredSearchRepositoryInterface<TrackPoint, TrackPointSearchCriteria> repository = createRepository();
		TrackPoint newEntry = createNewEntry();
		
		TestAdd(repository, newEntry);
	}
	
	@Test
	public void TestFindWithInvalidId(){
		FilteredSearchRepositoryInterface<TrackPoint, TrackPointSearchCriteria> repository = createRepository();
		
		TestFindWithInvalidId(repository);
	}
	
	@Test
	public void TestRemoveWithInvalidId(){
		FilteredSearchRepositoryInterface<TrackPoint, TrackPointSearchCriteria> repository = createRepository();
		
		TestRemoveWithInvalidId(repository);
	}
	
	@Test
	public void TestUpdateWithInvalidId(){
		FilteredSearchRepositoryInterface<TrackPoint, TrackPointSearchCriteria> repository = createRepository();
		TrackPoint newEntry = createNewEntry();
		
		TestUpdateWithInvalidId(repository, newEntry);
	}
	
	@Test
	public void TestUpdate(){
		
		FilteredSearchRepositoryInterface<TrackPoint, TrackPointSearchCriteria> repository = createRepository();
		TrackPoint newEntry = createNewEntry();
		TrackPoint modifiedEntry = createModifiedEntry(newEntry);
		
		TestUpdate(repository, newEntry, modifiedEntry);
	}
	
	@Test
	public void TestFindById(){
		
		FilteredSearchRepositoryInterface<TrackPoint, TrackPointSearchCriteria> repository = createRepository();
		TrackPoint newEntry = createNewEntry();
		
		TestFindById(repository, newEntry);
	}
	
	@Test
	public void TestRemove(){
		
		FilteredSearchRepositoryInterface<TrackPoint, TrackPointSearchCriteria> repository = createRepository();
		TrackPoint newEntry = createNewEntry();
		
		TestRemove(repository, newEntry);
	}
	
	@Test
	public void TestFindAll(){
		
		FilteredSearchRepositoryInterface<TrackPoint, TrackPointSearchCriteria> repository = createRepository();
		TrackPoint newEntry = createNewEntry();
		
		TestFindAll(repository, newEntry);
	}
	
	//TODO: Implement tests for search criteria search. Be sure to test each individual param , then one full combined search.

}
