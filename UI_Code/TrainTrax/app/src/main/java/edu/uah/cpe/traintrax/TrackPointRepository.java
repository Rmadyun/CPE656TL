package edu.uah.cpe.traintrax;
import java.util.ArrayList;
import java.util.List;

import com.traintrax.navigation.database.library.DatabaseEntry;
import com.traintrax.navigation.database.library.GenericDatabaseInterface;
import com.traintrax.navigation.database.library.KeyValuePair;
import com.traintrax.navigation.database.library.TrackPoint;


/**
 * Class responsible for storing information about points
 * measured along a given train track.
 * @author Corey Sanders
 *
 */
public class TrackPointRepository implements FilteredSearchRepositoryInterface<TrackPoint, TrackPointSearchCriteria> {
	
	private static final String TrackPointTable = "Track_Point";
    private static final String PointIdColumn = "point_id";
    private static final String PointNameColumn = "point_name";
    private static final String TypeColumn = "type";
    private static final String XColumn = "x";
    private static final String YColumn = "y";
    private static final String ZColumn = "z";
    private static final String BlockIdColumn = "block_id";
    private static final String TagNameColumn = "tag_name";
	private final GenericDatabaseInterface databaseInterface;
	
	/**
	 * Constructor
	 * @param databaseInterface Contact to the database storing the point
	 * information
	 */
	public TrackPointRepository(GenericDatabaseInterface databaseInterface){
		this.databaseInterface = databaseInterface;
	}
	
	/**
	 * Converts a DatabaseEntry into a TrackPoint object
	 * @param databaseEntry Entry to convert
	 * @return TrackPoint object that is representative of the values
	 * assigned to the database entry. Returns null if the entry
	 * cannot be converted.
	 */
	private static TrackPoint convertToTrackPoint(DatabaseEntry databaseEntry) {
		TrackPoint trackPoint = null;

		try {
			String pointName = DatabaseEntry.findColumnValue(databaseEntry, PointNameColumn);
			String type = DatabaseEntry.findColumnValue(databaseEntry, TypeColumn);
			double x = Double.parseDouble(DatabaseEntry.findColumnValue(databaseEntry,
					XColumn));
			double y = Double.parseDouble(DatabaseEntry.findColumnValue(databaseEntry,
					YColumn));
			double z = Double.parseDouble(DatabaseEntry.findColumnValue(databaseEntry,
					ZColumn));
			String blockId = DatabaseEntry.findColumnValue(databaseEntry, BlockIdColumn);
			String tagName = "";

			try {
				tagName = DatabaseEntry.findColumnValue(databaseEntry, TagNameColumn);
			} catch (Exception exception) {

			}

			trackPoint = new TrackPoint(pointName, type, x, y, z,
					blockId, tagName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return trackPoint;
	}
	
    /**
     * Converts a TrackPoint object into a DatabaseEntry object
	 * @param databaseEntry Entry to convert
	 * @return DatabaseEntry object that is representative of the values
	 * assigned to the TrackPoint object. Returns null if the entry
	 * cannot be converted.
	 */
	private static DatabaseEntry convertToDatabaseEntry(TrackPoint trackPoint) {
		DatabaseEntry databaseEntry = null;

		try {
			List<KeyValuePair> kvps = new ArrayList<KeyValuePair>();
			KeyValuePair pointNameKvp = new KeyValuePair(PointNameColumn, SqlUtilities.createSqlString(trackPoint.getPointName()));
			KeyValuePair blockIdKvp = new KeyValuePair(BlockIdColumn, SqlUtilities.createSqlInt(trackPoint.getBlockId()));
			KeyValuePair typeKvp = new KeyValuePair(TypeColumn, SqlUtilities.createSqlString(trackPoint.getType()));
			KeyValuePair xKvp = new KeyValuePair(XColumn, SqlUtilities.createSqlDouble(trackPoint.getX()));
			KeyValuePair yKvp = new KeyValuePair(YColumn, SqlUtilities.createSqlDouble(trackPoint.getY()));
			KeyValuePair zKvp = new KeyValuePair(ZColumn, SqlUtilities.createSqlDouble(trackPoint.getZ()));
			KeyValuePair tagNameKvp = new KeyValuePair(TagNameColumn, SqlUtilities.createSqlString(trackPoint.getTagName()));
			
			kvps.add(pointNameKvp);
			kvps.add(typeKvp);
			kvps.add(blockIdKvp);
			kvps.add(xKvp);
			kvps.add(yKvp);
			kvps.add(zKvp);
			kvps.add(tagNameKvp);

			databaseEntry = new DatabaseEntry(kvps);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return databaseEntry;
	}

	@Override
	public RepositoryEntry<TrackPoint>  find(String id) {
		RepositoryEntry<TrackPoint>  firstMatch = null;
		
		String queryString = "SELECT * FROM "+TrackPointTable + " WHERE " + PointIdColumn + "=" + id;
		
		List<DatabaseEntry> results = databaseInterface.sendQuery(queryString);
		
		if(results.size() > 0){
			DatabaseEntry databaseEntry = results.get(0);
			TrackPoint trackPoint = convertToTrackPoint(databaseEntry);
			
			RepositoryEntry<TrackPoint> repositoryEntry = new RepositoryEntry<TrackPoint>(id, trackPoint);
			firstMatch = repositoryEntry;
		}
		
		return firstMatch;
	}

	@Override
	public RepositoryEntry<TrackPoint> add(TrackPoint entry) {
		
		DatabaseEntry databaseEntry = convertToDatabaseEntry(entry);
		int val = databaseInterface.sendAdd(TrackPointTable, databaseEntry);

		String id = String.format("%d", val);
		
		RepositoryEntry<TrackPoint> repositoryEntry = new RepositoryEntry<TrackPoint>(id, entry);
		
		return repositoryEntry;
	}

	@Override
	public void remove(String id) {
		String queryString = "DELETE FROM "+TrackPointTable + " WHERE " + PointIdColumn + "=" + id;
		
		databaseInterface.sendDelete(queryString);
	}

	@Override
	public void update(String id, TrackPoint entry) {
		DatabaseEntry databaseEntry = convertToDatabaseEntry(entry);
		KeyValuePair primaryKey = new KeyValuePair(PointIdColumn, id);
		
		databaseInterface.sendUpdate(TrackPointTable, databaseEntry, primaryKey);		
	}

	@Override
	public List<RepositoryEntry<TrackPoint> > findAll() {
		List<RepositoryEntry<TrackPoint> > points = new ArrayList<RepositoryEntry<TrackPoint> >();
		
		String queryString = "SELECT * FROM "+TrackPointTable;
		
		List<DatabaseEntry> results = databaseInterface.sendQuery(queryString);
		
		for(DatabaseEntry databaseEntry : results){
			TrackPoint trackPoint = convertToTrackPoint(databaseEntry);
			String id = DatabaseEntry.findColumnValue(databaseEntry, PointIdColumn);
			
			RepositoryEntry<TrackPoint> repositoryEntry = new RepositoryEntry<TrackPoint>(id, trackPoint);
			
			points.add(repositoryEntry);
		}

		return points;
	}

	@Override
	public List<RepositoryEntry<TrackPoint>> find(TrackPointSearchCriteria searchCriteria) {
		
		List<RepositoryEntry<TrackPoint>>  matches = new ArrayList<RepositoryEntry<TrackPoint>>();
		
		String queryString = "SELECT * FROM "+TrackPointTable;
		String clauses = "";
		
		String name = searchCriteria.getName();
		if(name != null && !name.isEmpty()){
			
			if(!clauses.isEmpty()){
				clauses += " AND ";
			}
			
			clauses += PointNameColumn + "=" + SqlUtilities.createSqlString(name);
		}
		
		String blockId = searchCriteria.getBlockId();
		if(blockId != null && !blockId.isEmpty()){
			
			if(!clauses.isEmpty()){
				clauses += " AND ";
			}
			
			clauses += BlockIdColumn + "=" + SqlUtilities.createSqlString(blockId);
		}
		
		String type = searchCriteria.getType();
		if(type != null && !type.isEmpty()){
			if(!clauses.isEmpty()){
				clauses += " AND ";
			}
			
			clauses += TypeColumn + "=" + SqlUtilities.createSqlString(type);
		}
		
		String tagName = searchCriteria.getTagName();
		if(tagName != null && !tagName.isEmpty()){
			if(!clauses.isEmpty()){
				clauses += " AND ";
			}
			
			clauses += TagNameColumn + "=" + SqlUtilities.createSqlString(tagName);
		}

		if(!clauses.isEmpty()){
			queryString +=  " WHERE " + clauses;
		}
		
		List<DatabaseEntry> results = databaseInterface.sendQuery(queryString);
		
		for(DatabaseEntry databaseEntry : results) {

			TrackPoint trackPoint = convertToTrackPoint(databaseEntry);
			String id = DatabaseEntry.findColumnValue(databaseEntry, PointIdColumn);
			
			RepositoryEntry<TrackPoint> repositoryEntry = new RepositoryEntry<TrackPoint>(id, trackPoint);
			matches.add(repositoryEntry);
		}
				
		return matches;
	}

}
