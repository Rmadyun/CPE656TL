package com.traintrax.navigation.database.library.unittest;
import java.util.Calendar;

import org.junit.*;

import com.traintrax.navigation.database.library.FilteredSearchRepositoryInterface;
import com.traintrax.navigation.database.library.MySqlDatabaseAdapter;
import com.traintrax.navigation.database.library.TrainPosition;
import com.traintrax.navigation.database.library.TrainPositionRepository;
import com.traintrax.navigation.database.library.TrainPositionSearchCriteria;


public class TrainPositionRepositoryTests extends FilteredSearchRepositoryInterfaceTests<TrainPosition, TrainPositionSearchCriteria> {
	
	FilteredSearchRepositoryInterface<TrainPosition, TrainPositionSearchCriteria> createRepository(){
        MySqlDatabaseAdapter mySqlDatabaseAdapter = new MySqlDatabaseAdapter();
        
        mySqlDatabaseAdapter.connect();
        
        TrainPositionRepository TrainPositionRepository = new TrainPositionRepository(mySqlDatabaseAdapter);
		
		return TrainPositionRepository;
	}
	
	TrainPosition createNewEntry(){
		TrainPosition newTrainPosition = new TrainPosition("TrainId",0,0,0, Calendar.getInstance());
		
		return newTrainPosition;
	}
	
	TrainPosition createModifiedEntry(TrainPosition originalEntry){
		TrainPosition updatedTrainPosition = new TrainPosition(originalEntry.getTrainId(),
				originalEntry.getX()+1, originalEntry.getY()+1, originalEntry.getZ()+1,
				originalEntry.getTimeAtPosition());
				
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
