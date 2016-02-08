import static org.junit.Assert.assertEquals;

import org.junit.*;

import TrainNavigationDatabase.FilteredSearchRepositoryInterface;
import TrainNavigationDatabase.MySqlDatabaseAdapter;
import TrainNavigationDatabase.TrainPosition;
import TrainNavigationDatabase.TrainPositionRepository;
import TrainNavigationDatabase.TrainPositionSearchCriteria;


public class TrainPositionRepositoryTests extends FilteredSearchRepositoryInterfaceTests<TrainPosition, TrainPositionSearchCriteria> {
	
	FilteredSearchRepositoryInterface<TrainPosition, TrainPositionSearchCriteria> createRepository(){
        MySqlDatabaseAdapter mySqlDatabaseAdapter = new MySqlDatabaseAdapter();
        
        mySqlDatabaseAdapter.connect();
        
        TrainPositionRepository TrainPositionRepository = new TrainPositionRepository();
		
		return TrainPositionRepository;
	}
	
	TrainPosition createNewEntry(){
		TrainPosition newTrainPosition = new TrainPosition();
		
		return newTrainPosition;
	}
	
	TrainPosition createModifiedEntry(TrainPosition originalEntry){
		TrainPosition updatedTrainPosition = new TrainPosition();
		
		//TODO: Modify value
		
		return updatedTrainPosition;
	}
	
	@Test
	public void TestAdd(){
		FilteredSearchRepositoryInterface<TrainPosition, TrainPositionSearchCriteria> repository = createRepository();
		TrainPosition newEntry = createNewEntry();
		
		TestAdd(repository, newEntry);
	}
	
	@Test
	public void TestFindWithInvalidId(){
		FilteredSearchRepositoryInterface<TrainPosition, TrainPositionSearchCriteria> repository = createRepository();
		
		TestFindWithInvalidId(repository);
	}
	
	@Test
	public void TestRemoveWithInvalidId(){
		FilteredSearchRepositoryInterface<TrainPosition, TrainPositionSearchCriteria> repository = createRepository();
		
		TestRemoveWithInvalidId(repository);
	}
	
	@Test
	public void TestUpdateWithInvalidId(){
		FilteredSearchRepositoryInterface<TrainPosition, TrainPositionSearchCriteria> repository = createRepository();
		TrainPosition newEntry = createNewEntry();
		
		TestUpdateWithInvalidId(repository, newEntry);
	}
	
	@Test
	public void TestUpdate(){
		
		FilteredSearchRepositoryInterface<TrainPosition, TrainPositionSearchCriteria> repository = createRepository();
		TrainPosition newEntry = createNewEntry();
		TrainPosition modifiedEntry = createModifiedEntry(newEntry);
		
		TestUpdate(repository, newEntry, modifiedEntry);
	}
	
	@Test
	public void TestFindById(){
		
		FilteredSearchRepositoryInterface<TrainPosition, TrainPositionSearchCriteria> repository = createRepository();
		TrainPosition newEntry = createNewEntry();
		
		TestFindById(repository, newEntry);
	}
	
	@Test
	public void TestRemove(){
		
		FilteredSearchRepositoryInterface<TrainPosition, TrainPositionSearchCriteria> repository = createRepository();
		TrainPosition newEntry = createNewEntry();
		
		TestRemove(repository, newEntry);
	}
	
	@Test
	public void TestFindAll(){
		
		FilteredSearchRepositoryInterface<TrainPosition, TrainPositionSearchCriteria> repository = createRepository();
		TrainPosition newEntry = createNewEntry();
		
		TestFindAll(repository, newEntry);
	}
	
	//TODO: Implement tests for search criteria search. Be sure to test each individual param , then one full combined search.

}
