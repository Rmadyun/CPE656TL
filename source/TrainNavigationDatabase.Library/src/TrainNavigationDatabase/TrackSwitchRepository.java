package TrainNavigationDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Class stores information about all of known track switches on the
 * supported train tracks.
 * 
 * @author Corey Sanders
 *
 */
public class TrackSwitchRepository implements FilteredSearchRepositoryInterface<TrackSwitch, TrackSwitchSearchCriteria>  {
	private static final String TrackSwitchTable = "Track_Switch";
    private static final String SwitchIdColumn = "switch_id";
    private static final String SwitchNameColumn = "switch_name";
    private static final String PointIdColumn = "point_id";
    private static final String PassBlockIdColumn = "pass_block_id";
    private static final String BypassBlockIdColumn = "bypass_block_id";
	private final GenericDatabaseInterface databaseInterface;
	
	/**
	 * Constructor
	 * @param databaseInterface Contact to the database storing the point
	 * information
	 */
	public TrackSwitchRepository(GenericDatabaseInterface databaseInterface){
		this.databaseInterface = databaseInterface;
	}
	
	/**
	 * Converts a DatabaseEntry into a TrackSwitch object
	 * @param databaseEntry Entry to convert
	 * @return TrackSwitch object that is representative of the values
	 * assigned to the database entry. Returns null if the entry
	 * cannot be converted.
	 */
	private static TrackSwitch convertToTrackSwitch(DatabaseEntry databaseEntry) {
		TrackSwitch trackSwitch = null;

		try {
			String switchName = DatabaseEntry.findColumnValue(databaseEntry, SwitchNameColumn);
			String pointId = DatabaseEntry.findColumnValue(databaseEntry, PointIdColumn);
			String passBlockId = DatabaseEntry.findColumnValue(databaseEntry, PassBlockIdColumn);
			String bypassBlockId = DatabaseEntry.findColumnValue(databaseEntry, BypassBlockIdColumn);

			trackSwitch = new TrackSwitch(switchName, pointId, passBlockId, bypassBlockId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return trackSwitch;
	}
	
    /**
     * Converts a TrackSwitch object into a DatabaseEntry object
	 * @param databaseEntry Entry to convert
	 * @return DatabaseEntry object that is representative of the values
	 * assigned to the TrackSwitch object. Returns null if the entry
	 * cannot be converted.
	 */
	private static DatabaseEntry convertToDatabaseEntry(TrackSwitch trackSwitch) {
		DatabaseEntry databaseEntry = null;

		try {
			List<KeyValuePair> kvps = new ArrayList<KeyValuePair>();

			KeyValuePair switchNameKvp = new KeyValuePair(SwitchNameColumn, "'"+trackSwitch.getSwitchName()+"'");
			KeyValuePair pointIdKvp = new KeyValuePair(PointIdColumn, trackSwitch.getPointId());
			KeyValuePair passBlockIdKvp = new KeyValuePair(PassBlockIdColumn, trackSwitch.getPassBlockId());
			KeyValuePair bypassBlockIdKvp = new KeyValuePair(BypassBlockIdColumn, trackSwitch.getBypassBlockId());
			
			kvps.add(switchNameKvp);
			kvps.add(pointIdKvp);
			kvps.add(passBlockIdKvp);
			kvps.add(bypassBlockIdKvp);
			
			databaseEntry = new DatabaseEntry(kvps);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return databaseEntry;
	}

	@Override
	public RepositoryEntry<TrackSwitch> find(String id) {
		RepositoryEntry<TrackSwitch>  firstMatch = null;
		
		String queryString = "SELECT * FROM "+TrackSwitchTable + " WHERE " + SwitchIdColumn + "=" + id;
		
		List<DatabaseEntry> results = databaseInterface.sendQuery(queryString);
		
		if(results.size() > 0){
			DatabaseEntry databaseEntry = results.get(0);
			TrackSwitch trackSwitch = convertToTrackSwitch(databaseEntry);
			
			RepositoryEntry<TrackSwitch> repositoryEntry = new RepositoryEntry<TrackSwitch>(id, trackSwitch);
			firstMatch = repositoryEntry;
		}
		
		return firstMatch;
	}

	@Override
	public RepositoryEntry<TrackSwitch> add(TrackSwitch entry) {
		DatabaseEntry databaseEntry = convertToDatabaseEntry(entry);
		int val = databaseInterface.sendAdd(TrackSwitchTable, databaseEntry);

		String id = String.format("%d", val);
		
		RepositoryEntry<TrackSwitch> repositoryEntry = new RepositoryEntry<TrackSwitch>(id, entry);
		
		return repositoryEntry;
	}

	@Override
	public void remove(String id) {
		String queryString = "DELETE FROM "+ TrackSwitchTable + " WHERE " + SwitchIdColumn + "=" + id;
		
		databaseInterface.sendDelete(queryString);
	}

	@Override
	public void update(String id, TrackSwitch entry) {
		DatabaseEntry databaseEntry = convertToDatabaseEntry(entry);
		KeyValuePair primaryKey = new KeyValuePair(SwitchIdColumn, id);
		
		databaseInterface.sendUpdate(TrackSwitchTable, databaseEntry, primaryKey);
		
	}

	@Override
	public List<RepositoryEntry<TrackSwitch>> findAll() {
		List<RepositoryEntry<TrackSwitch> > points = new ArrayList<RepositoryEntry<TrackSwitch> >();
		
		String queryString = "SELECT * FROM "+TrackSwitchTable;
		
		List<DatabaseEntry> results = databaseInterface.sendQuery(queryString);
		
		for(DatabaseEntry databaseEntry : results){
			TrackSwitch trackSwitch = convertToTrackSwitch(databaseEntry);
			String id = DatabaseEntry.findColumnValue(databaseEntry, SwitchIdColumn);
			
			RepositoryEntry<TrackSwitch> repositoryEntry = new RepositoryEntry<TrackSwitch>(id, trackSwitch);
			
			points.add(repositoryEntry);
		}

		return points;
	}

	@Override
	public List<RepositoryEntry<TrackSwitch>> find(
			TrackSwitchSearchCriteria searchCriteria) {
		List<RepositoryEntry<TrackSwitch>>  matches = new ArrayList<RepositoryEntry<TrackSwitch>>();
		
		String queryString = "SELECT * FROM "+TrackSwitchTable;
		String clauses = "";
		
		String switchName = searchCriteria.getSwitchName();
		if(switchName != null && switchName.isEmpty()){
			
			clauses += SwitchNameColumn + "=" + switchName;
		}
		
		String pointId = searchCriteria.getPointId();
		if(pointId != null && pointId.isEmpty()){
			
			clauses += PointIdColumn + "=" + pointId;
		}
		
		String passBlockId = searchCriteria.getPassBlockId();
		if(passBlockId != null && passBlockId.isEmpty()){
			
			clauses += PassBlockIdColumn + "=" + passBlockId;
		}
		
		String byPassBlockId = searchCriteria.getBypassBlockId();
		if(byPassBlockId != null && byPassBlockId.isEmpty()){
			
			clauses += BypassBlockIdColumn + "=" + byPassBlockId;
		}
		
		if(!clauses.isEmpty()){
			queryString +=  " WHERE " + clauses;
		}
		
		List<DatabaseEntry> results = databaseInterface.sendQuery(queryString);
		
		for(DatabaseEntry databaseEntry : results) {

			TrackSwitch trackSwitch = convertToTrackSwitch(databaseEntry);
			String id = DatabaseEntry.findColumnValue(databaseEntry, SwitchIdColumn);
			
			RepositoryEntry<TrackSwitch> repositoryEntry = new RepositoryEntry<TrackSwitch>(id, trackSwitch);
			matches.add(repositoryEntry);
		}
				
		return matches;
	}

}
