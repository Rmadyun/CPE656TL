import static org.junit.Assert.assertEquals;

import org.junit.*;

import TrainNavigationDatabase.FilteredSearchRepositoryInterface;
import TrainNavigationDatabase.MySqlDatabaseAdapter;
import TrainNavigationDatabase.AdjacentPoint;
import TrainNavigationDatabase.AdjacentPointRepository;
import TrainNavigationDatabase.AdjacentPointSearchCriteria;
import TrainNavigationDatabase.TrackSwitch;
import TrainNavigationDatabase.TrackSwitchSearchCriteria;


public class AdjacentPointRepositoryTests extends FilteredSearchRepositoryInterfaceTests<AdjacentPoint, AdjacentPointSearchCriteria> {
	
	FilteredSearchRepositoryInterface<AdjacentPoint, AdjacentPointSearchCriteria> createRepository(){
        MySqlDatabaseAdapter mySqlDatabaseAdapter = new MySqlDatabaseAdapter();
        
        mySqlDatabaseAdapter.connect();
        
        AdjacentPointRepository AdjacentPointRepository = new AdjacentPointRepository(mySqlDatabaseAdapter);
		
		return AdjacentPointRepository;
	}
	
	AdjacentPoint createNewEntry(){
		AdjacentPoint newAdjacentPoint = new AdjacentPoint(1,2);
		
		return newAdjacentPoint;
	}
	
	AdjacentPoint createModifiedEntry(AdjacentPoint originalEntry){
		AdjacentPoint updatedAdjacentPoint = new AdjacentPoint(originalEntry.getAdjacenPointId()+1,
				originalEntry.getPointId());
		
		return updatedAdjacentPoint;
	}
	
	@Test
	public void TestAdd(){
		FilteredSearchRepositoryInterface<AdjacentPoint, AdjacentPointSearchCriteria> repository = createRepository();
		AdjacentPoint newEntry = createNewEntry();
		
		TestAdd(repository, newEntry);
	}
	
	@Test
	public void TestFindWithInvalidId(){
		FilteredSearchRepositoryInterface<AdjacentPoint, AdjacentPointSearchCriteria> repository = createRepository();
		
		TestFindWithInvalidId(repository);
	}
	
	@Test
	public void TestRemoveWithInvalidId(){
		FilteredSearchRepositoryInterface<AdjacentPoint, AdjacentPointSearchCriteria> repository = createRepository();
		
		TestRemoveWithInvalidId(repository);
	}
	
	@Test
	public void TestUpdateWithInvalidId(){
		FilteredSearchRepositoryInterface<AdjacentPoint, AdjacentPointSearchCriteria> repository = createRepository();
		AdjacentPoint newEntry = createNewEntry();
		
		TestUpdateWithInvalidId(repository, newEntry);
	}
	
	@Test
	public void TestUpdate(){
		
		FilteredSearchRepositoryInterface<AdjacentPoint, AdjacentPointSearchCriteria> repository = createRepository();
		AdjacentPoint newEntry = createNewEntry();
		AdjacentPoint modifiedEntry = createModifiedEntry(newEntry);
		
		TestUpdate(repository, newEntry, modifiedEntry);
	}
	
	@Test
	public void TestFindById(){
		
		FilteredSearchRepositoryInterface<AdjacentPoint, AdjacentPointSearchCriteria> repository = createRepository();
		AdjacentPoint newEntry = createNewEntry();
		
		TestFindById(repository, newEntry);
	}
	
	@Test
	public void TestRemove(){
		
		FilteredSearchRepositoryInterface<AdjacentPoint, AdjacentPointSearchCriteria> repository = createRepository();
		AdjacentPoint newEntry = createNewEntry();
		
		TestRemove(repository, newEntry);
	}
	
	@Test
	public void TestFindAll(){
		
		FilteredSearchRepositoryInterface<AdjacentPoint, AdjacentPointSearchCriteria> repository = createRepository();
		AdjacentPoint newEntry = createNewEntry();
		
		TestFindAll(repository, newEntry);
	}
	
	@Test
	public void TestFindWithEmptySearchCriteria(){
		
		FilteredSearchRepositoryInterface<AdjacentPoint, AdjacentPointSearchCriteria> repository = createRepository();
		AdjacentPoint newEntry = createNewEntry();
		AdjacentPointSearchCriteria searchCriteria = new AdjacentPointSearchCriteria();
		
		TestFindSearchCriteria(repository, newEntry, searchCriteria);
	}
	
	@Test
	public void TestFindWithAdjacentPointIdSearchCriteria(){
		
		FilteredSearchRepositoryInterface<AdjacentPoint, AdjacentPointSearchCriteria> repository = createRepository();
		AdjacentPoint newEntry = createNewEntry();
		AdjacentPointSearchCriteria searchCriteria = new AdjacentPointSearchCriteria();

		searchCriteria.setAdjacentPointId(Integer.toString(newEntry.getAdjacenPointId()));
		
		TestFindSearchCriteria(repository, newEntry, searchCriteria);

		String dummyFilterValue = "dummyFilterValue";
		searchCriteria.setAdjacentPointId(dummyFilterValue);
		TestFindSearchCriteriaWithNoMatches(repository, newEntry, searchCriteria);
	}
	
	@Test
	public void TestFindWithPointIdSearchCriteria(){
		
		FilteredSearchRepositoryInterface<AdjacentPoint, AdjacentPointSearchCriteria> repository = createRepository();
		AdjacentPoint newEntry = createNewEntry();
		AdjacentPointSearchCriteria searchCriteria = new AdjacentPointSearchCriteria();

		searchCriteria.setPointId(Integer.toString(newEntry.getPointId()));
		
		TestFindSearchCriteria(repository, newEntry, searchCriteria);
		
		String dummyFilterValue = "-1";
		searchCriteria.setPointId(dummyFilterValue);
		TestFindSearchCriteriaWithNoMatches(repository, newEntry, searchCriteria);
	}
		
	@Test
	public void TestFindWithAllSearchCriteria(){
		
		FilteredSearchRepositoryInterface<AdjacentPoint, AdjacentPointSearchCriteria> repository = createRepository();
		AdjacentPoint newEntry = createNewEntry();
		AdjacentPointSearchCriteria searchCriteria = new AdjacentPointSearchCriteria();

		searchCriteria.setPointId(Integer.toString(newEntry.getPointId()));
		searchCriteria.setAdjacentPointId(Integer.toString(newEntry.getAdjacenPointId()));
		
		TestFindSearchCriteria(repository, newEntry, searchCriteria);
		
		String dummyFilterValue = "dummyFilterValue";
		searchCriteria.setPointId(dummyFilterValue);
		searchCriteria.setAdjacentPointId(dummyFilterValue);
		TestFindSearchCriteriaWithNoMatches(repository, newEntry, searchCriteria);
	}

}
