import TrainNavigationDatabase.FilteredSearchRepositoryInterface;
import TrainNavigationDatabase.GenericDatabaseInterface;
import TrainNavigationDatabase.MySqlDatabaseAdapter;
import TrainNavigationDatabase.TrackPoint;
import TrainNavigationDatabase.TrackPointRepository;
import TrainNavigationDatabase.TrackPointSearchCriteria;

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
	 * Retrieves the singleton instance of the factory to use for creating 
	 * repositories
	 * @return Singleton instance of the factory to use for creating 
	 * repositories
	 */
	public static TrainDatabaseRepositoryFactory getInstance(){
		return instance;
	}

}
