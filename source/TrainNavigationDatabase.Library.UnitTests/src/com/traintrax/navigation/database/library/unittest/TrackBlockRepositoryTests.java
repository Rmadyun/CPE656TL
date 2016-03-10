package com.traintrax.navigation.database.library.unittest;
import org.junit.*;

import com.traintrax.navigation.database.library.FilteredSearchRepositoryInterface;
import com.traintrax.navigation.database.library.MySqlDatabaseAdapter;
import com.traintrax.navigation.database.library.TrackBlock;
import com.traintrax.navigation.database.library.TrackBlockRepository;
import com.traintrax.navigation.database.library.TrackBlockSearchCriteria;


public class TrackBlockRepositoryTests extends FilteredSearchRepositoryInterfaceTests<TrackBlock, TrackBlockSearchCriteria> {
	
	FilteredSearchRepositoryInterface<TrackBlock, TrackBlockSearchCriteria> createRepository(){
        MySqlDatabaseAdapter mySqlDatabaseAdapter = new MySqlDatabaseAdapter();
        
        mySqlDatabaseAdapter.connect();
        
        TrackBlockRepository TrackBlockRepository = new TrackBlockRepository(mySqlDatabaseAdapter);
		
		return TrackBlockRepository;
	}
	
	TrackBlock createNewEntry(){
		TrackBlock newTrackBlock = new TrackBlock("Name");
		
		return newTrackBlock;
	}
	
	TrackBlock createModifiedEntry(TrackBlock originalEntry){
		TrackBlock updatedTrackBlock = new TrackBlock(originalEntry.getBlockName()+"1");
		
		return updatedTrackBlock;
	}
	
	@Test
	public void TestAdd(){
		FilteredSearchRepositoryInterface<TrackBlock, TrackBlockSearchCriteria> repository = createRepository();
		TrackBlock newEntry = createNewEntry();
		
		TestAdd(repository, newEntry);
	}
	
	@Test
	public void TestFindWithInvalidId(){
		FilteredSearchRepositoryInterface<TrackBlock, TrackBlockSearchCriteria> repository = createRepository();
		
		TestFindWithInvalidId(repository);
	}
	
	@Test
	public void TestRemoveWithInvalidId(){
		FilteredSearchRepositoryInterface<TrackBlock, TrackBlockSearchCriteria> repository = createRepository();
		
		TestRemoveWithInvalidId(repository);
	}
	
	@Test
	public void TestUpdateWithInvalidId(){
		FilteredSearchRepositoryInterface<TrackBlock, TrackBlockSearchCriteria> repository = createRepository();
		TrackBlock newEntry = createNewEntry();
		
		TestUpdateWithInvalidId(repository, newEntry);
	}
	
	@Test
	public void TestUpdate(){
		
		FilteredSearchRepositoryInterface<TrackBlock, TrackBlockSearchCriteria> repository = createRepository();
		TrackBlock newEntry = createNewEntry();
		TrackBlock modifiedEntry = createModifiedEntry(newEntry);
		
		TestUpdate(repository, newEntry, modifiedEntry);
	}
	
	@Test
	public void TestFindById(){
		
		FilteredSearchRepositoryInterface<TrackBlock, TrackBlockSearchCriteria> repository = createRepository();
		TrackBlock newEntry = createNewEntry();
		
		TestFindById(repository, newEntry);
	}
	
	@Test
	public void TestRemove(){
		
		FilteredSearchRepositoryInterface<TrackBlock, TrackBlockSearchCriteria> repository = createRepository();
		TrackBlock newEntry = createNewEntry();
		
		TestRemove(repository, newEntry);
	}
	
	@Test
	public void TestFindAll(){
		
		FilteredSearchRepositoryInterface<TrackBlock, TrackBlockSearchCriteria> repository = createRepository();
		TrackBlock newEntry = createNewEntry();
		
		TestFindAll(repository, newEntry);
	}
	
	@Test
	public void TestFindWithEmptySearchCriteria(){
		
		FilteredSearchRepositoryInterface<TrackBlock, TrackBlockSearchCriteria> repository = createRepository();
		TrackBlock newEntry = createNewEntry();
		TrackBlockSearchCriteria searchCriteria = new TrackBlockSearchCriteria();
		
		TestFindSearchCriteria(repository, newEntry, searchCriteria);
	}
	
	@Test
	public void TestFindWithBlockNameSearchCriteria(){
		
		FilteredSearchRepositoryInterface<TrackBlock, TrackBlockSearchCriteria> repository = createRepository();
		TrackBlock newEntry = createNewEntry();
		TrackBlockSearchCriteria searchCriteria = new TrackBlockSearchCriteria();

		searchCriteria.setBlockName(newEntry.getBlockName());
		
		TestFindSearchCriteria(repository, newEntry, searchCriteria);

		String dummyFilterValue = "dummyFilterValue";
		searchCriteria.setBlockName(dummyFilterValue);
		TestFindSearchCriteriaWithNoMatches(repository, newEntry, searchCriteria);
	}
		
	@Test
	public void TestFindWithAllSearchCriteria(){
		
		FilteredSearchRepositoryInterface<TrackBlock, TrackBlockSearchCriteria> repository = createRepository();
		TrackBlock newEntry = createNewEntry();
		TrackBlockSearchCriteria searchCriteria = new TrackBlockSearchCriteria();

		searchCriteria.setBlockName(newEntry.getBlockName());
		
		TestFindSearchCriteria(repository, newEntry, searchCriteria);
		
		String dummyFilterValue = "dummyFilterValue";
		searchCriteria.setBlockName(dummyFilterValue);
		TestFindSearchCriteriaWithNoMatches(repository, newEntry, searchCriteria);
	}

}
