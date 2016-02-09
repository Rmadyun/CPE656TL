package TrainNavigationDatabase;

import java.util.ArrayList;
import java.util.List;

public class TrackBlockRepository implements FilteredSearchRepositoryInterface<TrackBlock, TrackBlockSearchCriteria>  {
	private static final String TrackBlockTable = "Track_Block";
    private static final String BlockIdColumn = "block_id";
    private static final String BlockNameColumn = "block_name";
	private final GenericDatabaseInterface databaseInterface;
	
	/**
	 * Constructor
	 * @param databaseInterface Contact to the database storing the point
	 * information
	 */
	public TrackBlockRepository(GenericDatabaseInterface databaseInterface){
		this.databaseInterface = databaseInterface;
	}
	
	/**
	 * Converts a DatabaseEntry into a TrackBlock object
	 * @param databaseEntry Entry to convert
	 * @return TrackBlock object that is representative of the values
	 * assigned to the database entry. Returns null if the entry
	 * cannot be converted.
	 */
	private static TrackBlock convertToTrackBlock(DatabaseEntry databaseEntry) {
		TrackBlock trackBlock = null;

		try {
			String blockName = DatabaseEntry.findColumnValue(databaseEntry, BlockNameColumn);

			trackBlock = new TrackBlock(blockName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return trackBlock;
	}
	
	/** 
	 * Converts a TrackBlock object into a DatabaseEntry object
	 * @param databaseEntry Entry to convert
	 * @return DatabaseEntry object that is representative of the values
	 * assigned to the TrackBlock object. Returns null if the entry
	 * cannot be converted.
	 */
	private static DatabaseEntry convertToDatabaseEntry(TrackBlock trackBlock) {
		DatabaseEntry databaseEntry = null;

		try {
			List<KeyValuePair> kvps = new ArrayList<KeyValuePair>();
			KeyValuePair blockNameKvp = new KeyValuePair(BlockNameColumn, "'"+trackBlock.getBlockName()+"'");

			kvps.add(blockNameKvp);

			databaseEntry = new DatabaseEntry(kvps);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return databaseEntry;
	}
    
	@Override
	public RepositoryEntry<TrackBlock> find(String id) {
		RepositoryEntry<TrackBlock>  firstMatch = null;
		
		String queryString = "SELECT * FROM "+TrackBlockTable + " WHERE " + BlockIdColumn + "=" + id;
		
		List<DatabaseEntry> results = databaseInterface.sendQuery(queryString);
		
		if(results.size() > 0){
			DatabaseEntry databaseEntry = results.get(0);
			TrackBlock trackBlock = convertToTrackBlock(databaseEntry);
			
			RepositoryEntry<TrackBlock> repositoryEntry = new RepositoryEntry<TrackBlock>(id, trackBlock);
			firstMatch = repositoryEntry;
		}
		
		return firstMatch;
	}

	@Override
	public RepositoryEntry<TrackBlock> add(TrackBlock entry) {
		DatabaseEntry databaseEntry = convertToDatabaseEntry(entry);
		int val = databaseInterface.sendAdd(TrackBlockTable, databaseEntry);

		String id = String.format("%d", val);
		
		RepositoryEntry<TrackBlock> repositoryEntry = new RepositoryEntry<TrackBlock>(id, entry);
		
		return repositoryEntry;
	}

	@Override
	public void remove(String id) {
		String queryString = "DELETE FROM "+TrackBlockTable + " WHERE " + BlockIdColumn + "=" + id;
		
		databaseInterface.sendDelete(queryString);
		
	}

	@Override
	public void update(String id, TrackBlock entry) {
		DatabaseEntry databaseEntry = convertToDatabaseEntry(entry);
		KeyValuePair primaryKey = new KeyValuePair(BlockIdColumn, id);
		
		databaseInterface.sendUpdate(TrackBlockTable, databaseEntry, primaryKey);
		
	}

	@Override
	public List<RepositoryEntry<TrackBlock>> findAll() {
		List<RepositoryEntry<TrackBlock> > points = new ArrayList<RepositoryEntry<TrackBlock> >();
		
		String queryString = "SELECT * FROM "+TrackBlockTable;
		
		List<DatabaseEntry> results = databaseInterface.sendQuery(queryString);
		
		for(DatabaseEntry databaseEntry : results){
			TrackBlock trackBlock = convertToTrackBlock(databaseEntry);
			String id = DatabaseEntry.findColumnValue(databaseEntry, BlockIdColumn);
			
			RepositoryEntry<TrackBlock> repositoryEntry = new RepositoryEntry<TrackBlock>(id, trackBlock);
			
			points.add(repositoryEntry);
		}

		return points;
	}

	@Override
	public List<RepositoryEntry<TrackBlock>> find(
			TrackBlockSearchCriteria searchCriteria) {
		List<RepositoryEntry<TrackBlock>>  matches = new ArrayList<RepositoryEntry<TrackBlock>>();
		
		String queryString = "SELECT * FROM "+TrackBlockTable;
		String clauses = "";
		
		String blockName = searchCriteria.getBlockName();
		if(blockName!= null && !blockName.isEmpty()){
			
			clauses += BlockNameColumn + "=" + blockName;
		}
		
		if(!clauses.isEmpty()){
			queryString +=  " WHERE " + clauses;
		}
		
		List<DatabaseEntry> results = databaseInterface.sendQuery(queryString);
		
		for(DatabaseEntry databaseEntry : results) {

			TrackBlock trackPoint = convertToTrackBlock(databaseEntry);
			String id = DatabaseEntry.findColumnValue(databaseEntry, BlockIdColumn);
			
			RepositoryEntry<TrackBlock> repositoryEntry = new RepositoryEntry<TrackBlock>(id, trackPoint);
			matches.add(repositoryEntry);
		}
				
		return matches;
	}



}
