package com.traintrax.navigation.database.library.unittest;
import java.util.Calendar;

import org.junit.*;

import com.traintrax.navigation.database.library.AccelerometerMeasurement;
import com.traintrax.navigation.database.library.AccelerometerMeasurementRepository;
import com.traintrax.navigation.database.library.AccelerometerMeasurementSearchCriteria;
import com.traintrax.navigation.database.library.FilteredSearchRepositoryInterface;
import com.traintrax.navigation.database.library.MySqlDatabaseAdapter;


public class AccelerometerMeasurementRepositoryTests extends FilteredSearchRepositoryInterfaceTests<AccelerometerMeasurement, AccelerometerMeasurementSearchCriteria> {
	
	FilteredSearchRepositoryInterface<AccelerometerMeasurement, AccelerometerMeasurementSearchCriteria> createRepository(){
        MySqlDatabaseAdapter mySqlDatabaseAdapter = new MySqlDatabaseAdapter();
        
        mySqlDatabaseAdapter.connect();
        
        AccelerometerMeasurementRepository AccelerometerMeasurementRepository = new AccelerometerMeasurementRepository();
		
		return AccelerometerMeasurementRepository;
	}
	
	AccelerometerMeasurement createNewEntry(){
		AccelerometerMeasurement newAccelerometerMeasurement = new AccelerometerMeasurement(1,1,1, Calendar.getInstance());
		
		return newAccelerometerMeasurement;
	}
	
	AccelerometerMeasurement createModifiedEntry(AccelerometerMeasurement originalEntry){
		AccelerometerMeasurement updatedAccelerometerMeasurement = new AccelerometerMeasurement(originalEntry.getMetersPerSecondSquaredAlongXAxis()+1,
				originalEntry.getMetersPerSecondSquaredAlongYAxis()+1,
				originalEntry.getMetersPerSecondSquaredAlongZAxis()+1,
				originalEntry.getTimeMeasured());
		
		return updatedAccelerometerMeasurement;
	}
	
	@Test
	public void TestAdd(){
		FilteredSearchRepositoryInterface<AccelerometerMeasurement, AccelerometerMeasurementSearchCriteria> repository = createRepository();
		AccelerometerMeasurement newEntry = createNewEntry();
		
		TestAdd(repository, newEntry);
	}
	
	@Test
	public void TestFindWithInvalidId(){
		FilteredSearchRepositoryInterface<AccelerometerMeasurement, AccelerometerMeasurementSearchCriteria> repository = createRepository();
		
		TestFindWithInvalidId(repository);
	}
	
	@Test
	public void TestRemoveWithInvalidId(){
		FilteredSearchRepositoryInterface<AccelerometerMeasurement, AccelerometerMeasurementSearchCriteria> repository = createRepository();
		
		TestRemoveWithInvalidId(repository);
	}
	
	@Test
	public void TestUpdateWithInvalidId(){
		FilteredSearchRepositoryInterface<AccelerometerMeasurement, AccelerometerMeasurementSearchCriteria> repository = createRepository();
		AccelerometerMeasurement newEntry = createNewEntry();
		
		TestUpdateWithInvalidId(repository, newEntry);
	}
	
	@Test
	public void TestUpdate(){
		
		FilteredSearchRepositoryInterface<AccelerometerMeasurement, AccelerometerMeasurementSearchCriteria> repository = createRepository();
		AccelerometerMeasurement newEntry = createNewEntry();
		AccelerometerMeasurement modifiedEntry = createModifiedEntry(newEntry);
		
		TestUpdate(repository, newEntry, modifiedEntry);
	}
	
	@Test
	public void TestFindById(){
		
		FilteredSearchRepositoryInterface<AccelerometerMeasurement, AccelerometerMeasurementSearchCriteria> repository = createRepository();
		AccelerometerMeasurement newEntry = createNewEntry();
		
		TestFindById(repository, newEntry);
	}
	
	@Test
	public void TestRemove(){
		
		FilteredSearchRepositoryInterface<AccelerometerMeasurement, AccelerometerMeasurementSearchCriteria> repository = createRepository();
		AccelerometerMeasurement newEntry = createNewEntry();
		
		TestRemove(repository, newEntry);
	}
	
	@Test
	public void TestFindAll(){
		
		FilteredSearchRepositoryInterface<AccelerometerMeasurement, AccelerometerMeasurementSearchCriteria> repository = createRepository();
		AccelerometerMeasurement newEntry = createNewEntry();
		
		TestFindAll(repository, newEntry);
	}
	
	//TODO: Implement tests for search criteria search. Be sure to test each individual param , then one full combined search.

}
