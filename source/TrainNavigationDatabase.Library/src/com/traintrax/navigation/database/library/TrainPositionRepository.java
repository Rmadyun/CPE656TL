package com.traintrax.navigation.database.library;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Class responsible for storing the history of train movement
 * along the test bed track.
 * @author Corey Sanders
 *
 */
public class TrainPositionRepository implements FilteredSearchRepositoryInterface<TrainPosition, TrainPositionSearchCriteria> {

	private static final String PositionMeasurementTable = "Train_Position";
    private static final String MeasurementIdColumn = "measurement_id";
    private static final String XColumn = "x";
    private static final String YColumn = "y";
    private static final String ZColumn = "z";
    private static final String TrainIdColumn = "train_id";
    private static final String TimestampColumn = "timestamp";
	private final GenericDatabaseInterface databaseInterface;
	
	/**
	 * Constructor
	 * @param databaseInterface Contact to the database storing the point
	 * information
	 */
	public TrainPositionRepository(GenericDatabaseInterface databaseInterface){
		this.databaseInterface = databaseInterface;
	}
	
	/**
	 * Converts a DatabaseEntry into a TrainPosition object
	 * @param databaseEntry Entry to convert
	 * @return TrainPosition object that is representative of the values
	 * assigned to the database entry. Returns null if the entry
	 * cannot be converted.
	 */
	private static TrainPosition convertToTrainPositionMeasurement(DatabaseEntry databaseEntry) {
		TrainPosition trainPosition = null;

		try {
			double x = Double.parseDouble(DatabaseEntry.findColumnValue(databaseEntry,
					XColumn));
			double y = Double.parseDouble(DatabaseEntry.findColumnValue(databaseEntry,
					YColumn));
			double z = Double.parseDouble(DatabaseEntry.findColumnValue(databaseEntry,
					ZColumn));
			String trainId = DatabaseEntry.findColumnValue(databaseEntry, TrainIdColumn);
			Calendar timestamp = SqlUtilities.parseSqlTimestamp(DatabaseEntry.findColumnValue(databaseEntry,
					TimestampColumn));

			trainPosition = new TrainPosition(trainId, x,y,z,timestamp);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return trainPosition;
	}
	
    /**
     * Converts a TrainPosition object into a DatabaseEntry object
	 * @param databaseEntry Entry to convert
	 * @return DatabaseEntry object that is representative of the values
	 * assigned to the TrainPosition object. Returns null if the entry
	 * cannot be converted.
	 */
	private static DatabaseEntry convertToDatabaseEntry(TrainPosition trainPosition) {
		DatabaseEntry databaseEntry = null;

		try {
			List<KeyValuePair> kvps = new ArrayList<KeyValuePair>();

			KeyValuePair xKvp = new KeyValuePair(XColumn, SqlUtilities.createSqlDouble(trainPosition.getX()));
			KeyValuePair yKvp = new KeyValuePair(YColumn, SqlUtilities.createSqlDouble(trainPosition.getY()));
			KeyValuePair zKvp = new KeyValuePair(ZColumn, SqlUtilities.createSqlDouble(trainPosition.getZ()));
			KeyValuePair trainIdKvp = new KeyValuePair(TrainIdColumn, SqlUtilities.createSqlString(trainPosition.getTrainId()));
			KeyValuePair timestampKvp = new KeyValuePair(TimestampColumn, SqlUtilities.createSqlTimestamp(trainPosition.getTimeAtPosition()));
			
			kvps.add(xKvp);
			kvps.add(yKvp);
			kvps.add(zKvp);
			kvps.add(trainIdKvp);
			kvps.add(timestampKvp);

			databaseEntry = new DatabaseEntry(kvps);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return databaseEntry;
	}

	@Override
	public RepositoryEntry<TrainPosition> find(String id) {
		RepositoryEntry<TrainPosition>  firstMatch = null;
		
		String queryString = "SELECT * FROM "+PositionMeasurementTable + " WHERE " + MeasurementIdColumn + "=" + id;
		
		List<DatabaseEntry> results = databaseInterface.sendQuery(queryString);
		
		if(results.size() > 0){
			DatabaseEntry databaseEntry = results.get(0);
			TrainPosition trainPosition = convertToTrainPositionMeasurement(databaseEntry);
			
			RepositoryEntry<TrainPosition> repositoryEntry = new RepositoryEntry<TrainPosition>(id, trainPosition);
			firstMatch = repositoryEntry;
		}
		
		return firstMatch;
	}

	@Override
	public RepositoryEntry<TrainPosition> add(
			TrainPosition entry) {
		DatabaseEntry databaseEntry = convertToDatabaseEntry(entry);
		int val = databaseInterface.sendAdd(PositionMeasurementTable, databaseEntry);

		String id = String.format("%d", val);
		
		RepositoryEntry<TrainPosition> repositoryEntry = new RepositoryEntry<TrainPosition>(id, entry);
		
		return repositoryEntry;
	}

	@Override
	public void remove(String id) {
		String queryString = "DELETE FROM "+PositionMeasurementTable + " WHERE " + MeasurementIdColumn + "=" + id;
		
		databaseInterface.sendDelete(queryString);
	}

	@Override
	public void update(String id, TrainPosition entry) {
		DatabaseEntry databaseEntry = convertToDatabaseEntry(entry);
		KeyValuePair primaryKey = new KeyValuePair(MeasurementIdColumn, id);
		
		databaseInterface.sendUpdate(PositionMeasurementTable, databaseEntry, primaryKey);
	}

	@Override
	public List<RepositoryEntry<TrainPosition>> findAll() {
		List<RepositoryEntry<TrainPosition> > points = new ArrayList<RepositoryEntry<TrainPosition> >();
		
		String queryString = "SELECT * FROM "+PositionMeasurementTable;
		
		List<DatabaseEntry> results = databaseInterface.sendQuery(queryString);
		
		for(DatabaseEntry databaseEntry : results){
			TrainPosition adjacentPoint = convertToTrainPositionMeasurement(databaseEntry);
			String id = DatabaseEntry.findColumnValue(databaseEntry, MeasurementIdColumn);
			
			RepositoryEntry<TrainPosition> repositoryEntry = new RepositoryEntry<TrainPosition>(id, adjacentPoint);
			
			points.add(repositoryEntry);
		}

		return points;
	}

	@Override
	public List<RepositoryEntry<TrainPosition>> find(
			TrainPositionSearchCriteria searchCriteria) {
		List<RepositoryEntry<TrainPosition>>  matches = new ArrayList<RepositoryEntry<TrainPosition>>();
		
		String queryString = "SELECT * FROM "+PositionMeasurementTable;
		String clauses = "";
		
		String x = searchCriteria.getXValue();
		if(x != null && !x.isEmpty()){
			
			if(!clauses.isEmpty()){
				clauses += " AND ";
			}
			
			clauses += XColumn + "=" + SqlUtilities.createSqlDouble(Double.parseDouble(x));
		}
		
		String y = searchCriteria.getXValue();
		if(y != null && !y.isEmpty()){
			
			if(!clauses.isEmpty()){
				clauses += " AND ";
			}
			
			clauses += YColumn + "=" + SqlUtilities.createSqlDouble(Double.parseDouble(y));
		}
		
		String z = searchCriteria.getXValue();
		if(z != null && !z.isEmpty()){
			
			if(!clauses.isEmpty()){
				clauses += " AND ";
			}
			
			clauses += ZColumn + "=" + SqlUtilities.createSqlDouble(Double.parseDouble(z));
		}
		
		String trainId = searchCriteria.getTrainId();
		if(trainId != null && !trainId.isEmpty()){
			
			if(!clauses.isEmpty()){
				clauses += " AND ";
			}
			
			clauses += TrainIdColumn + "=" + SqlUtilities.createSqlString(trainId);
		}
		
		if(!clauses.isEmpty()){
			queryString +=  " WHERE " + clauses;
		}
		
		List<DatabaseEntry> results = databaseInterface.sendQuery(queryString);
		
		for(DatabaseEntry databaseEntry : results) {

			TrainPosition trainPosition = convertToTrainPositionMeasurement(databaseEntry);
			String id = DatabaseEntry.findColumnValue(databaseEntry, MeasurementIdColumn);
			
			RepositoryEntry<TrainPosition> repositoryEntry = new RepositoryEntry<TrainPosition>(id, trainPosition);
			matches.add(repositoryEntry);
		}
				
		return matches;
	}
}
