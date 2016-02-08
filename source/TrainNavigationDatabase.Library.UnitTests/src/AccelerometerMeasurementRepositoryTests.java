import static org.junit.Assert.assertEquals;

import org.junit.*;

import TrainNavigationDatabase.FilteredSearchRepositoryInterface;
import TrainNavigationDatabase.MySqlDatabaseAdapter;
import TrainNavigationDatabase.AccelerometerMeasurement;
import TrainNavigationDatabase.AccelerometerMeasurementRepository;
import TrainNavigationDatabase.AccelerometerMeasurementSearchCriteria;


public class AccelerometerMeasurementRepositoryTests extends FilteredSearchRepositoryInterfaceTests<AccelerometerMeasurement, AccelerometerMeasurementSearchCriteria> {
	
	FilteredSearchRepositoryInterface<AccelerometerMeasurement, AccelerometerMeasurementSearchCriteria> createRepository(){
        MySqlDatabaseAdapter mySqlDatabaseAdapter = new MySqlDatabaseAdapter();
        
        mySqlDatabaseAdapter.connect();
        
        AccelerometerMeasurementRepository AccelerometerMeasurementRepository = new AccelerometerMeasurementRepository();
		
		return AccelerometerMeasurementRepository;
	}
	
	AccelerometerMeasurement createNewEntry(){
		AccelerometerMeasurement newAccelerometerMeasurement = new AccelerometerMeasurement();
		
		return newAccelerometerMeasurement;
	}
	
	AccelerometerMeasurement createModifiedEntry(AccelerometerMeasurement originalEntry){
		AccelerometerMeasurement updatedAccelerometerMeasurement = new AccelerometerMeasurement();
		
		//TODO: Modify value
		
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
