import TrainNavigationDatabase.AdjacentPoint;
import TrainNavigationDatabase.AdjacentPointRepository;
import TrainNavigationDatabase.AdjacentPointSearchCriteria;
import TrainNavigationDatabase.FilteredSearchRepositoryInterface;
import TrainNavigationDatabase.GenericDatabaseInterface;
import TrainNavigationDatabase.MySqlDatabaseAdapter;
import TrainNavigationDatabase.TrackBlock;
import TrainNavigationDatabase.TrackBlockRepository;
import TrainNavigationDatabase.TrackBlockSearchCriteria;
import TrainNavigationDatabase.TrackPoint;
import TrainNavigationDatabase.TrackPointRepository;
import TrainNavigationDatabase.TrackPointSearchCriteria;
import TrainNavigationDatabase.TrackSwitch;
import TrainNavigationDatabase.TrackSwitchRepository;
import TrainNavigationDatabase.TrackSwitchSearchCriteria;

/**
 * Class is responsible for creating instances of
 * Train Navigation Database repositories
 * @author Corey Sanders
 *
 */
public class TrainDatabaseRepositoryFactory {
	
	//Singleton instance
	private static final TrainDatabaseRepositoryFactory instance = new TrainDatabaseRepositoryFactory();
	
	private final GenericDatabaseInterface databaseInterface;
	private Boolean isConnected = false;
	
	/**
	 * Constructor
	 */
	private TrainDatabaseRepositoryFactory(){
		databaseInterface = new MySqlDatabaseAdapter();
	}
	
	/**
	 * Makes sure that the database has been connected to complete initialization
	 */
	private void connectionCheck(){
		if(!isConnected){
			databaseInterface.connect();
			isConnected = true;
		}
	}
	
	/**
	 * Creates a new track point repository instance
	 * @return A new track point repository instance
	 */
	public FilteredSearchRepositoryInterface<TrackPoint, TrackPointSearchCriteria> createTrackPointRepository(){
		connectionCheck();
		
		FilteredSearchRepositoryInterface<TrackPoint, TrackPointSearchCriteria> trackPointRepository = new TrackPointRepository(databaseInterface);
		
		return trackPointRepository;
	}
	
	/**
	 * Creates a new track switch repository instance
	 * @return A new track switch repository instance
	 */
	public FilteredSearchRepositoryInterface<TrackSwitch, TrackSwitchSearchCriteria> createTrackSwitchRepository(){
		connectionCheck();
		
		FilteredSearchRepositoryInterface<TrackSwitch, TrackSwitchSearchCriteria> trackSwitchRepository = new TrackSwitchRepository(databaseInterface);
		
		return trackSwitchRepository;
	}
	
	/**
	 * Creates a new adjacent point repository instance
	 * @return A new adjacent point repository instance
	 */
	public FilteredSearchRepositoryInterface<AdjacentPoint, AdjacentPointSearchCriteria> createAdjacentPointRepository(){
		connectionCheck();
		
		FilteredSearchRepositoryInterface<AdjacentPoint, AdjacentPointSearchCriteria> adjacentPointRepository = new AdjacentPointRepository(databaseInterface);
		
		return adjacentPointRepository;
	}
	
	/**
	 * Creates a new track block repository instance
	 * @return A new track block repository instance
	 */
	public FilteredSearchRepositoryInterface<TrackBlock, TrackBlockSearchCriteria> createTrackBlockRepository(){
		connectionCheck();
		
		FilteredSearchRepositoryInterface<TrackBlock, TrackBlockSearchCriteria> trackBlockRepository = new TrackBlockRepository(databaseInterface);
		
		return trackBlockRepository;
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
