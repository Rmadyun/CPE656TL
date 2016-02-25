package com.traintrax.navigation.database.library.unittest;
import static org.junit.Assert.assertEquals;

import org.junit.*;

import com.traintrax.navigation.database.library.FilteredSearchRepositoryInterface;
import com.traintrax.navigation.database.library.MySqlDatabaseAdapter;
import com.traintrax.navigation.database.library.RfidTagDetectedNotification;
import com.traintrax.navigation.database.library.RfidTagDetectedNotificationRepository;
import com.traintrax.navigation.database.library.RfidTagDetectedNotificationSearchCriteria;


public class RfidTagDetectedNotificationRepositoryTests extends FilteredSearchRepositoryInterfaceTests<RfidTagDetectedNotification, RfidTagDetectedNotificationSearchCriteria> {
	
	FilteredSearchRepositoryInterface<RfidTagDetectedNotification, RfidTagDetectedNotificationSearchCriteria> createRepository(){
        MySqlDatabaseAdapter mySqlDatabaseAdapter = new MySqlDatabaseAdapter();
        
        mySqlDatabaseAdapter.connect();
        
        RfidTagDetectedNotificationRepository RfidTagDetectedNotificationRepository = new RfidTagDetectedNotificationRepository();
		
		return RfidTagDetectedNotificationRepository;
	}
	
	RfidTagDetectedNotification createNewEntry(){
		RfidTagDetectedNotification newRfidTagDetectedNotification = new RfidTagDetectedNotification();
		
		return newRfidTagDetectedNotification;
	}
	
	RfidTagDetectedNotification createModifiedEntry(RfidTagDetectedNotification originalEntry){
		RfidTagDetectedNotification updatedRfidTagDetectedNotification = new RfidTagDetectedNotification();
		
		//TODO: Modify value
		
		return updatedRfidTagDetectedNotification;
	}
	
	@Test
	public void TestAdd(){
		FilteredSearchRepositoryInterface<RfidTagDetectedNotification, RfidTagDetectedNotificationSearchCriteria> repository = createRepository();
		RfidTagDetectedNotification newEntry = createNewEntry();
		
		TestAdd(repository, newEntry);
	}
	
	@Test
	public void TestFindWithInvalidId(){
		FilteredSearchRepositoryInterface<RfidTagDetectedNotification, RfidTagDetectedNotificationSearchCriteria> repository = createRepository();
		
		TestFindWithInvalidId(repository);
	}
	
	@Test
	public void TestRemoveWithInvalidId(){
		FilteredSearchRepositoryInterface<RfidTagDetectedNotification, RfidTagDetectedNotificationSearchCriteria> repository = createRepository();
		
		TestRemoveWithInvalidId(repository);
	}
	
	@Test
	public void TestUpdateWithInvalidId(){
		FilteredSearchRepositoryInterface<RfidTagDetectedNotification, RfidTagDetectedNotificationSearchCriteria> repository = createRepository();
		RfidTagDetectedNotification newEntry = createNewEntry();
		
		TestUpdateWithInvalidId(repository, newEntry);
	}
	
	@Test
	public void TestUpdate(){
		
		FilteredSearchRepositoryInterface<RfidTagDetectedNotification, RfidTagDetectedNotificationSearchCriteria> repository = createRepository();
		RfidTagDetectedNotification newEntry = createNewEntry();
		RfidTagDetectedNotification modifiedEntry = createModifiedEntry(newEntry);
		
		TestUpdate(repository, newEntry, modifiedEntry);
	}
	
	@Test
	public void TestFindById(){
		
		FilteredSearchRepositoryInterface<RfidTagDetectedNotification, RfidTagDetectedNotificationSearchCriteria> repository = createRepository();
		RfidTagDetectedNotification newEntry = createNewEntry();
		
		TestFindById(repository, newEntry);
	}
	
	@Test
	public void TestRemove(){
		
		FilteredSearchRepositoryInterface<RfidTagDetectedNotification, RfidTagDetectedNotificationSearchCriteria> repository = createRepository();
		RfidTagDetectedNotification newEntry = createNewEntry();
		
		TestRemove(repository, newEntry);
	}
	
	@Test
	public void TestFindAll(){
		
		FilteredSearchRepositoryInterface<RfidTagDetectedNotification, RfidTagDetectedNotificationSearchCriteria> repository = createRepository();
		RfidTagDetectedNotification newEntry = createNewEntry();
		
		TestFindAll(repository, newEntry);
	}
	
	//TODO: Implement tests for search criteria search. Be sure to test each individual param , then one full combined search.

}
