package com.traintrax.navigation.database.library.unittest;
import static org.junit.Assert.assertEquals;

import org.junit.*;

import com.traintrax.navigation.database.library.FilteredSearchRepositoryInterface;
import com.traintrax.navigation.database.library.GyroscopeMeasurement;
import com.traintrax.navigation.database.library.GyroscopeMeasurementRepository;
import com.traintrax.navigation.database.library.GyroscopeMeasurementSearchCriteria;
import com.traintrax.navigation.database.library.MySqlDatabaseAdapter;


public class GyroscopeMeasurementRepositoryTests extends FilteredSearchRepositoryInterfaceTests<GyroscopeMeasurement, GyroscopeMeasurementSearchCriteria> {
	
	FilteredSearchRepositoryInterface<GyroscopeMeasurement, GyroscopeMeasurementSearchCriteria> createRepository(){
        MySqlDatabaseAdapter mySqlDatabaseAdapter = new MySqlDatabaseAdapter();
        
        mySqlDatabaseAdapter.connect();
        
        GyroscopeMeasurementRepository GyroscopeMeasurementRepository = new GyroscopeMeasurementRepository();
		
		return GyroscopeMeasurementRepository;
	}
	
	GyroscopeMeasurement createNewEntry(){
		GyroscopeMeasurement newGyroscopeMeasurement = new GyroscopeMeasurement();
		
		return newGyroscopeMeasurement;
	}
	
	GyroscopeMeasurement createModifiedEntry(GyroscopeMeasurement originalEntry){
		GyroscopeMeasurement updatedGyroscopeMeasurement = new GyroscopeMeasurement();
		
		//TODO: Modify value
		
		return updatedGyroscopeMeasurement;
	}
	
	@Test
	public void TestAdd(){
		FilteredSearchRepositoryInterface<GyroscopeMeasurement, GyroscopeMeasurementSearchCriteria> repository = createRepository();
		GyroscopeMeasurement newEntry = createNewEntry();
		
		TestAdd(repository, newEntry);
	}
	
	@Test
	public void TestFindWithInvalidId(){
		FilteredSearchRepositoryInterface<GyroscopeMeasurement, GyroscopeMeasurementSearchCriteria> repository = createRepository();
		
		TestFindWithInvalidId(repository);
	}
	
	@Test
	public void TestRemoveWithInvalidId(){
		FilteredSearchRepositoryInterface<GyroscopeMeasurement, GyroscopeMeasurementSearchCriteria> repository = createRepository();
		
		TestRemoveWithInvalidId(repository);
	}
	
	@Test
	public void TestUpdateWithInvalidId(){
		FilteredSearchRepositoryInterface<GyroscopeMeasurement, GyroscopeMeasurementSearchCriteria> repository = createRepository();
		GyroscopeMeasurement newEntry = createNewEntry();
		
		TestUpdateWithInvalidId(repository, newEntry);
	}
	
	@Test
	public void TestUpdate(){
		
		FilteredSearchRepositoryInterface<GyroscopeMeasurement, GyroscopeMeasurementSearchCriteria> repository = createRepository();
		GyroscopeMeasurement newEntry = createNewEntry();
		GyroscopeMeasurement modifiedEntry = createModifiedEntry(newEntry);
		
		TestUpdate(repository, newEntry, modifiedEntry);
	}
	
	@Test
	public void TestFindById(){
		
		FilteredSearchRepositoryInterface<GyroscopeMeasurement, GyroscopeMeasurementSearchCriteria> repository = createRepository();
		GyroscopeMeasurement newEntry = createNewEntry();
		
		TestFindById(repository, newEntry);
	}
	
	@Test
	public void TestRemove(){
		
		FilteredSearchRepositoryInterface<GyroscopeMeasurement, GyroscopeMeasurementSearchCriteria> repository = createRepository();
		GyroscopeMeasurement newEntry = createNewEntry();
		
		TestRemove(repository, newEntry);
	}
	
	@Test
	public void TestFindAll(){
		
		FilteredSearchRepositoryInterface<GyroscopeMeasurement, GyroscopeMeasurementSearchCriteria> repository = createRepository();
		GyroscopeMeasurement newEntry = createNewEntry();
		
		TestFindAll(repository, newEntry);
	}
	
	//TODO: Implement tests for search criteria search. Be sure to test each individual param , then one full combined search.

}
