import static org.junit.Assert.assertEquals;

import org.junit.*;

import TrainNavigationDatabase.FilteredSearchRepositoryInterface;
import TrainNavigationDatabase.MySqlDatabaseAdapter;
import TrainNavigationDatabase.TrackPoint;
import TrainNavigationDatabase.TrackPointSearchCriteria;
import TrainNavigationDatabase.TrackSwitch;
import TrainNavigationDatabase.TrackSwitchRepository;
import TrainNavigationDatabase.TrackSwitchSearchCriteria;


public class TrackSwitchRepositoryTests extends FilteredSearchRepositoryInterfaceTests<TrackSwitch, TrackSwitchSearchCriteria> {
	
	FilteredSearchRepositoryInterface<TrackSwitch, TrackSwitchSearchCriteria> createRepository(){
        MySqlDatabaseAdapter mySqlDatabaseAdapter = new MySqlDatabaseAdapter();
        
        mySqlDatabaseAdapter.connect();
        
        TrackSwitchRepository TrackSwitchRepository = new TrackSwitchRepository(mySqlDatabaseAdapter);
		
		return TrackSwitchRepository;
	}
	
	TrackSwitch createNewEntry(){
		TrackSwitch newTrackSwitch = new TrackSwitch("Name", "1", "2", "3");
		
		return newTrackSwitch;
	}
	
	TrackSwitch createModifiedEntry(TrackSwitch originalEntry){
		TrackSwitch updatedTrackSwitch = new TrackSwitch(originalEntry.getSwitchName()+"1",
				originalEntry.getPointId(),
				originalEntry.getPassBlockId(),
				originalEntry.getBypassBlockId());
		
		return updatedTrackSwitch;
	}
	
	@Test
	public void TestAdd(){
		FilteredSearchRepositoryInterface<TrackSwitch, TrackSwitchSearchCriteria> repository = createRepository();
		TrackSwitch newEntry = createNewEntry();
		
		TestAdd(repository, newEntry);
	}
	
	@Test
	public void TestFindWithInvalidId(){
		FilteredSearchRepositoryInterface<TrackSwitch, TrackSwitchSearchCriteria> repository = createRepository();
		
		TestFindWithInvalidId(repository);
	}
	
	@Test
	public void TestRemoveWithInvalidId(){
		FilteredSearchRepositoryInterface<TrackSwitch, TrackSwitchSearchCriteria> repository = createRepository();
		
		TestRemoveWithInvalidId(repository);
	}
	
	@Test
	public void TestUpdateWithInvalidId(){
		FilteredSearchRepositoryInterface<TrackSwitch, TrackSwitchSearchCriteria> repository = createRepository();
		TrackSwitch newEntry = createNewEntry();
		
		TestUpdateWithInvalidId(repository, newEntry);
	}
	
	@Test
	public void TestUpdate(){
		
		FilteredSearchRepositoryInterface<TrackSwitch, TrackSwitchSearchCriteria> repository = createRepository();
		TrackSwitch newEntry = createNewEntry();
		TrackSwitch modifiedEntry = createModifiedEntry(newEntry);
		
		TestUpdate(repository, newEntry, modifiedEntry);
	}
	
	@Test
	public void TestFindById(){
		
		FilteredSearchRepositoryInterface<TrackSwitch, TrackSwitchSearchCriteria> repository = createRepository();
		TrackSwitch newEntry = createNewEntry();
		
		TestFindById(repository, newEntry);
	}
	
	@Test
	public void TestRemove(){
		
		FilteredSearchRepositoryInterface<TrackSwitch, TrackSwitchSearchCriteria> repository = createRepository();
		TrackSwitch newEntry = createNewEntry();
		
		TestRemove(repository, newEntry);
	}
	
	@Test
	public void TestFindAll(){
		
		FilteredSearchRepositoryInterface<TrackSwitch, TrackSwitchSearchCriteria> repository = createRepository();
		TrackSwitch newEntry = createNewEntry();
		
		TestFindAll(repository, newEntry);
	}
	
	@Test
	public void TestFindWithEmptySearchCriteria(){
		
		FilteredSearchRepositoryInterface<TrackSwitch, TrackSwitchSearchCriteria> repository = createRepository();
		TrackSwitch newEntry = createNewEntry();
		TrackSwitchSearchCriteria searchCriteria = new TrackSwitchSearchCriteria();
		
		TestFindSearchCriteria(repository, newEntry, searchCriteria);
	}
	
	@Test
	public void TestFindWithSwitchNameSearchCriteria(){
		
		FilteredSearchRepositoryInterface<TrackSwitch, TrackSwitchSearchCriteria> repository = createRepository();
		TrackSwitch newEntry = createNewEntry();
		TrackSwitchSearchCriteria searchCriteria = new TrackSwitchSearchCriteria();

		searchCriteria.setSwitchName(newEntry.getSwitchName());
		
		TestFindSearchCriteria(repository, newEntry, searchCriteria);

		String dummyFilterValue = "dummyFilterValue";
		searchCriteria.setSwitchName(dummyFilterValue);
		TestFindSearchCriteriaWithNoMatches(repository, newEntry, searchCriteria);
	}
	
	@Test
	public void TestFindWithPointIdSearchCriteria(){
		
		FilteredSearchRepositoryInterface<TrackSwitch, TrackSwitchSearchCriteria> repository = createRepository();
		TrackSwitch newEntry = createNewEntry();
		TrackSwitchSearchCriteria searchCriteria = new TrackSwitchSearchCriteria();

		searchCriteria.setPointId(newEntry.getPointId());
		
		TestFindSearchCriteria(repository, newEntry, searchCriteria);
		
		String dummyFilterValue = "-1";
		searchCriteria.setPointId(dummyFilterValue);
		TestFindSearchCriteriaWithNoMatches(repository, newEntry, searchCriteria);
	}
	
	@Test
	public void TestFindWithPassBlockIdSearchCriteria(){
		
		FilteredSearchRepositoryInterface<TrackSwitch, TrackSwitchSearchCriteria> repository = createRepository();
		TrackSwitch newEntry = createNewEntry();
		TrackSwitchSearchCriteria searchCriteria = new TrackSwitchSearchCriteria();

		searchCriteria.setPassBlockId(newEntry.getPassBlockId());
		
		TestFindSearchCriteria(repository, newEntry, searchCriteria);
		
		String dummyFilterValue = "dummyFilterValue";
		searchCriteria.setPassBlockId(dummyFilterValue);
		TestFindSearchCriteriaWithNoMatches(repository, newEntry, searchCriteria);
	}
	
	@Test
	public void TestFindWithBypassBlockIdSearchCriteria(){
		
		FilteredSearchRepositoryInterface<TrackSwitch, TrackSwitchSearchCriteria> repository = createRepository();
		TrackSwitch newEntry = createNewEntry();
		TrackSwitchSearchCriteria searchCriteria = new TrackSwitchSearchCriteria();

		searchCriteria.setBypassBlockId(newEntry.getBypassBlockId());
		
		TestFindSearchCriteria(repository, newEntry, searchCriteria);
		
		String dummyFilterValue = "dummyFilterValue";
		searchCriteria.setBypassBlockId(dummyFilterValue);
		TestFindSearchCriteriaWithNoMatches(repository, newEntry, searchCriteria);
	}
	
	@Test
	public void TestFindWithAllSearchCriteria(){
		
		FilteredSearchRepositoryInterface<TrackSwitch, TrackSwitchSearchCriteria> repository = createRepository();
		TrackSwitch newEntry = createNewEntry();
		TrackSwitchSearchCriteria searchCriteria = new TrackSwitchSearchCriteria();

		searchCriteria.setSwitchName(newEntry.getSwitchName());
		searchCriteria.setPointId(newEntry.getPointId());
		searchCriteria.setPassBlockId(newEntry.getPassBlockId());
		searchCriteria.setBypassBlockId(newEntry.getBypassBlockId());
		
		TestFindSearchCriteria(repository, newEntry, searchCriteria);
		
		String dummyFilterValue = "dummyFilterValue";
		searchCriteria.setSwitchName(dummyFilterValue);
		searchCriteria.setPointId(dummyFilterValue);
		searchCriteria.setPassBlockId(dummyFilterValue);
		searchCriteria.setBypassBlockId(dummyFilterValue);
		TestFindSearchCriteriaWithNoMatches(repository, newEntry, searchCriteria);
	}

}
