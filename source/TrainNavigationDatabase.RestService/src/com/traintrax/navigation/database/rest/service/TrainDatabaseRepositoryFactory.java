package com.traintrax.navigation.database.rest.service;
import com.traintrax.navigation.database.library.AdjacentPoint;
import com.traintrax.navigation.database.library.AdjacentPointRepository;
import com.traintrax.navigation.database.library.AdjacentPointSearchCriteria;
import com.traintrax.navigation.database.library.FilteredSearchRepositoryInterface;
import com.traintrax.navigation.database.library.GenericDatabaseInterface;
import com.traintrax.navigation.database.library.MySqlDatabaseAdapter;
import com.traintrax.navigation.database.library.TrackBlock;
import com.traintrax.navigation.database.library.TrackBlockRepository;
import com.traintrax.navigation.database.library.TrackBlockSearchCriteria;
import com.traintrax.navigation.database.library.TrackPoint;
import com.traintrax.navigation.database.library.TrackPointRepository;
import com.traintrax.navigation.database.library.TrackPointSearchCriteria;
import com.traintrax.navigation.database.library.TrackSwitch;
import com.traintrax.navigation.database.library.TrackSwitchRepository;
import com.traintrax.navigation.database.library.TrackSwitchSearchCriteria;

/**
 * Class is responsible for creating instances of
 * Train Navigation Database repositories
 * @author Corey Sanders
 *
 */
public class TrainDatabaseRepositoryFactory {
	
	//Singleton instance
	private static final TrainDatabaseRepositoryFactory instance = new TrainDatabaseRepositoryFactory();
	
	private GenericDatabaseInterface databaseInterface;
	private TrainNavigationDatabaseConfiguration dbConfiguration;
		

	/**
	 * Default Constructor
	 */
	private TrainDatabaseRepositoryFactory(){
		
	}
		
	/**
	 * Creates a new track point repository instance
	 * @return A new track point repository instance
	 */
	public FilteredSearchRepositoryInterface<TrackPoint, TrackPointSearchCriteria> createTrackPointRepository(){
		
		FilteredSearchRepositoryInterface<TrackPoint, TrackPointSearchCriteria> trackPointRepository = new TrackPointRepository(databaseInterface);
		
		return trackPointRepository;
	}
	
	/**
	 * Creates a new track switch repository instance
	 * @return A new track switch repository instance
	 */
	public FilteredSearchRepositoryInterface<TrackSwitch, TrackSwitchSearchCriteria> createTrackSwitchRepository(){		
		FilteredSearchRepositoryInterface<TrackSwitch, TrackSwitchSearchCriteria> trackSwitchRepository = new TrackSwitchRepository(databaseInterface);
		
		return trackSwitchRepository;
	}
	
	/**
	 * Creates a new adjacent point repository instance
	 * @return A new adjacent point repository instance
	 */
	public FilteredSearchRepositoryInterface<AdjacentPoint, AdjacentPointSearchCriteria> createAdjacentPointRepository(){		
		FilteredSearchRepositoryInterface<AdjacentPoint, AdjacentPointSearchCriteria> adjacentPointRepository = new AdjacentPointRepository(databaseInterface);
		
		return adjacentPointRepository;
	}
	
	/**
	 * Creates a new track block repository instance
	 * @return A new track block repository instance
	 */
	public FilteredSearchRepositoryInterface<TrackBlock, TrackBlockSearchCriteria> createTrackBlockRepository(){		
		FilteredSearchRepositoryInterface<TrackBlock, TrackBlockSearchCriteria> trackBlockRepository = new TrackBlockRepository(databaseInterface);
		
		return trackBlockRepository;
	}
	
	/**
	 * Retrieves the current configuration used to setup database access
	 * @return Configuration to use to setup database access
	 */
	public TrainNavigationDatabaseConfiguration getDbConfiguration() {
		return dbConfiguration;
	}

	/**
	 * Completes initialization of the factory for use.
	 * @param dbConfiguration Configuration to use to setup database access
	 */
	public void initialize(TrainNavigationDatabaseConfiguration dbConfiguration) {
		this.dbConfiguration = dbConfiguration;
		databaseInterface = new MySqlDatabaseAdapter(dbConfiguration.getDbUsername(), dbConfiguration.getDbPassword(),
				dbConfiguration.getDbName(), dbConfiguration.getDbHost(), dbConfiguration.getDbPort());
		databaseInterface.connect();
	}
	
	/**
	 * Retrieves the singleton instance of the factory to use for creating 
	 * repositories
	 * @return Singleton instance of the factory to use for creating 
	 * repositories
	 */
	public static TrainDatabaseRepositoryFactory getInstance(){
		return instance;
	}

}
