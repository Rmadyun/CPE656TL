import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import TrainNavigationDatabase.DatabaseEntry;
import TrainNavigationDatabase.GenericDatabaseInterface;
import TrainNavigationDatabase.KeyValuePair;
import TrainNavigationDatabase.RepositoryInterface;
import TrainNavigationDatabase.TrackPoint;


/**
 * Class responsible for storing information about points
 * measured along a given train track.
 * @author Corey Sanders
 *
 */
public class TrackPointRepository implements RepositoryInterface<TrackPoint> {
	
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
     * Searches for the value for a particular column in a database entry 
     * @param databaseEntry Entry to search in
     * @param columnName Name associated with the column
     * @return value of the column. Returns null if not found.
     */
	private static String findColumnValue(DatabaseEntry databaseEntry,
			String columnName) {
		String columnValue = null;

		for (KeyValuePair kvp : databaseEntry.getColumns()) {
			if (kvp.getKey().equals(columnName)) {
				columnValue = kvp.getValue();
				break;
			}
		}

		return columnValue;
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
			int pointId = Integer.parseInt(findColumnValue(databaseEntry,
					PointIdColumn));
			String pointName = findColumnValue(databaseEntry, PointNameColumn);
			String type = findColumnValue(databaseEntry, TypeColumn);
			double x = Double.parseDouble(findColumnValue(databaseEntry,
					XColumn));
			double y = Double.parseDouble(findColumnValue(databaseEntry,
					YColumn));
			double z = Double.parseDouble(findColumnValue(databaseEntry,
					ZColumn));
			String blockId = findColumnValue(databaseEntry, BlockIdColumn);
			String tagName = "";

			try {
				tagName = findColumnValue(databaseEntry, TagNameColumn);
			} catch (Exception exception) {

			}

			trackPoint = new TrackPoint(pointId, pointName, type, x, y, z,
					blockId, tagName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return trackPoint;
	}
	
	 /* Converts a TrackPoint object into a DatabaseEntry object
	 * @param databaseEntry Entry to convert
	 * @return DatabaseEntry object that is representative of the values
	 * assigned to the TrackPoint object. Returns null if the entry
	 * cannot be converted.
	 */
	private static DatabaseEntry convertToDatabaseEntry(TrackPoint trackPoint) {
		DatabaseEntry databaseEntry = null;

		try {
			List<KeyValuePair> kvps = new ArrayList<KeyValuePair>();
			KeyValuePair pointIdKvp = new KeyValuePair(PointIdColumn, String.format("%d", trackPoint.getPointId()));
			KeyValuePair pointNameKvp = new KeyValuePair(PointNameColumn, "'"+trackPoint.getPointName()+"'");
			KeyValuePair blockIdKvp = new KeyValuePair(BlockIdColumn, trackPoint.getBlockId());
			KeyValuePair typeKvp = new KeyValuePair(TypeColumn, "'"+trackPoint.getType()+"'");
			KeyValuePair xKvp = new KeyValuePair(XColumn, Double.toString(trackPoint.getX()));
			KeyValuePair yKvp = new KeyValuePair(YColumn, Double.toString(trackPoint.getY()));
			KeyValuePair zKvp = new KeyValuePair(ZColumn, Double.toString(trackPoint.getZ()));
			//KeyValuePair tagNameKvp = new KeyValuePair(TagNameColumn, trackPoint.getTagName());
			
			//Only include if the point ID has been assigned.
			if(trackPoint.getPointId() >= 0){
			    kvps.add(pointIdKvp);
			}
			kvps.add(pointNameKvp);
			kvps.add(typeKvp);
			kvps.add(blockIdKvp);
			kvps.add(xKvp);
			kvps.add(yKvp);
			kvps.add(zKvp);
			//kvps.add(tagNameKvp);

			databaseEntry = new DatabaseEntry(kvps);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return databaseEntry;
	}

	@Override
	public TrackPoint find(String id) {
		TrackPoint firstMatch = null;
		
		String queryString = "SELECT * FROM "+TrackPointTable + " WHERE " + PointIdColumn + "=" + id;
		
		List<DatabaseEntry> results = databaseInterface.sendQuery(queryString);
		
		if(results.size() > 0){
			firstMatch = convertToTrackPoint(results.get(0));
		}
		
		return firstMatch;
	}

	@Override
	public String add(TrackPoint entry) {
		
		DatabaseEntry databaseEntry = convertToDatabaseEntry(entry);
		int val = databaseInterface.sendAdd(TrackPointTable, databaseEntry);

		return String.format("%d", val);
	}

	@Override
	public void remove(String id) {
		String queryString = "DELETE FROM "+TrackPointTable + " WHERE " + PointIdColumn + "=" + id;
		
		databaseInterface.sendDelete(queryString);
	}

	@Override
	public void update(String id, TrackPoint entry) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<TrackPoint> findAll() {
		List<TrackPoint> points = new ArrayList<TrackPoint>();
		
		String queryString = "SELECT * FROM "+TrackPointTable;
		
		List<DatabaseEntry> results = databaseInterface.sendQuery(queryString);
		
		for(DatabaseEntry databaseEntry : results){
			TrackPoint trackPoint = convertToTrackPoint(databaseEntry);
			
			points.add(trackPoint);
		}
		
		return points;
	}

}
