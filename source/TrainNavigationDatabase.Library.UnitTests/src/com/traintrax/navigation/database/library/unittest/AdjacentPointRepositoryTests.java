package com.traintrax.navigation.database.library.unittest;
import org.junit.*;

import com.traintrax.navigation.database.library.AdjacentPoint;
import com.traintrax.navigation.database.library.AdjacentPointRepository;
import com.traintrax.navigation.database.library.AdjacentPointSearchCriteria;
import com.traintrax.navigation.database.library.FilteredSearchRepositoryInterface;
import com.traintrax.navigation.database.library.MySqlDatabaseAdapter;

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
		AdjacentPoint updatedAdjacentPoint = new AdjacentPoint(originalEntry.getAdjacentPointId()+1,
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

		searchCriteria.setAdjacentPointId(Integer.toString(newEntry.getAdjacentPointId()));
		
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
		searchCriteria.setAdjacentPointId(Integer.toString(newEntry.getAdjacentPointId()));
		
		TestFindSearchCriteria(repository, newEntry, searchCriteria);
		
		String dummyFilterValue = "dummyFilterValue";
		searchCriteria.setPointId(dummyFilterValue);
		searchCriteria.setAdjacentPointId(dummyFilterValue);
		TestFindSearchCriteriaWithNoMatches(repository, newEntry, searchCriteria);
	}

}
