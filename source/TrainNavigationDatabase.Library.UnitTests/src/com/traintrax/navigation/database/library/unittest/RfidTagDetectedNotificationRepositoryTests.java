package com.traintrax.navigation.database.library.unittest;
import java.util.Calendar;

import org.junit.*;

import com.traintrax.navigation.database.library.FilteredSearchRepositoryInterface;
import com.traintrax.navigation.database.library.MySqlDatabaseAdapter;
import com.traintrax.navigation.database.library.RfidTagDetectedNotification;
import com.traintrax.navigation.database.library.RfidTagDetectedNotificationRepository;
import com.traintrax.navigation.database.library.RfidTagDetectedNotificationSearchCriteria;
import com.traintrax.navigation.database.library.TrackPoint;
import com.traintrax.navigation.database.library.TrackPointSearchCriteria;


public class RfidTagDetectedNotificationRepositoryTests extends FilteredSearchRepositoryInterfaceTests<RfidTagDetectedNotification, RfidTagDetectedNotificationSearchCriteria> {
	
	FilteredSearchRepositoryInterface<RfidTagDetectedNotification, RfidTagDetectedNotificationSearchCriteria> createRepository(){
        MySqlDatabaseAdapter mySqlDatabaseAdapter = new MySqlDatabaseAdapter();
        
        mySqlDatabaseAdapter.connect();
        
        RfidTagDetectedNotificationRepository RfidTagDetectedNotificationRepository = new RfidTagDetectedNotificationRepository(mySqlDatabaseAdapter);
		
		return RfidTagDetectedNotificationRepository;
	}
	
	RfidTagDetectedNotification createNewEntry(){
		RfidTagDetectedNotification newRfidTagDetectedNotification = new RfidTagDetectedNotification("RFIDTagID", Calendar.getInstance());
		
		return newRfidTagDetectedNotification;
	}
	
	RfidTagDetectedNotification createModifiedEntry(RfidTagDetectedNotification originalEntry){
		Calendar time = (Calendar) originalEntry.getTimeDetected().clone();
		time.add(Calendar.SECOND, 1);
		
		RfidTagDetectedNotification updatedRfidTagDetectedNotification = new RfidTagDetectedNotification(originalEntry.getRfidTagValue(),
				time);
			
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
	
	@Test
	public void TestFindWithRfidTagValueSearchCriteria(){
		
		FilteredSearchRepositoryInterface<RfidTagDetectedNotification, RfidTagDetectedNotificationSearchCriteria> repository = createRepository();
		RfidTagDetectedNotification newEntry = createNewEntry();
		RfidTagDetectedNotificationSearchCriteria searchCriteria = new RfidTagDetectedNotificationSearchCriteria();

		searchCriteria.setRfidTagValue(newEntry.getRfidTagValue());
		
		TestFindSearchCriteria(repository, newEntry, searchCriteria);
		
		String dummyFilterValue = "dummyFilterValue";
		searchCriteria.setRfidTagValue(dummyFilterValue);
		TestFindSearchCriteriaWithNoMatches(repository, newEntry, searchCriteria);
	}
	
	@Test
	public void TestFindWithAllSearchCriteria(){
		
		FilteredSearchRepositoryInterface<RfidTagDetectedNotification, RfidTagDetectedNotificationSearchCriteria> repository = createRepository();
		RfidTagDetectedNotification newEntry = createNewEntry();
		RfidTagDetectedNotificationSearchCriteria searchCriteria = new RfidTagDetectedNotificationSearchCriteria();

		searchCriteria.setRfidTagValue(newEntry.getRfidTagValue());

		//Add other fields here
		
		
		TestFindSearchCriteria(repository, newEntry, searchCriteria);
		
		String dummyFilterValue = "dummyFilterValue";
		searchCriteria.setRfidTagValue(dummyFilterValue);
        //Add other fields here
		
		
		TestFindSearchCriteriaWithNoMatches(repository, newEntry, searchCriteria);
	}

}
