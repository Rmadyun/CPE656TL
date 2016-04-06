package com.traintrax.navigation.database.library;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Class responsible for storing information about
 * collected measurements from gyroscopes.
 * @author Corey Sanders
 *
 */
public class GyroscopeMeasurementRepository implements FilteredSearchRepositoryInterface<GyroscopeMeasurement, GyroscopeMeasurementSearchCriteria>  {

	private static final String GyrMeasurementTable = "Raw_Gyroscope";
    private static final String MeasurementIdColumn = "measurement_id";
    private static final String XColumn = "x";
    private static final String YColumn = "y";
    private static final String ZColumn = "z";
    private static final String TimestampColumn = "timestamp";
	private final GenericDatabaseInterface databaseInterface;
	
	/**
	 * Constructor
	 * @param databaseInterface Contact to the database storing the point
	 * information
	 */
	public GyroscopeMeasurementRepository(GenericDatabaseInterface databaseInterface){
		this.databaseInterface = databaseInterface;
	}
	
	/**
	 * Converts a DatabaseEntry into a GyroscopeMeasurement object
	 * @param databaseEntry Entry to convert
	 * @return GyroscopeMeasurement object that is representative of the values
	 * assigned to the database entry. Returns null if the entry
	 * cannot be converted.
	 */
	private static GyroscopeMeasurement convertToGyroscopeMeasurement(DatabaseEntry databaseEntry) {
		GyroscopeMeasurement gyroscopeMeasurement = null;

		try {
			double x = Double.parseDouble(DatabaseEntry.findColumnValue(databaseEntry,
					XColumn));
			double y = Double.parseDouble(DatabaseEntry.findColumnValue(databaseEntry,
					YColumn));
			double z = Double.parseDouble(DatabaseEntry.findColumnValue(databaseEntry,
					ZColumn));
			Calendar timestamp = SqlUtilities.parseSqlTimestamp(DatabaseEntry.findColumnValue(databaseEntry,
					TimestampColumn));

			gyroscopeMeasurement = new GyroscopeMeasurement(x,y,z,timestamp);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return gyroscopeMeasurement;
	}
	
    /**
     * Converts a GyroscopeMeasurement object into a DatabaseEntry object
	 * @param databaseEntry Entry to convert
	 * @return DatabaseEntry object that is representative of the values
	 * assigned to the GyroscopeMeasurement object. Returns null if the entry
	 * cannot be converted.
	 */
	private static DatabaseEntry convertToDatabaseEntry(GyroscopeMeasurement gyroscopeMeasurement) {
		DatabaseEntry databaseEntry = null;

		try {
			List<KeyValuePair> kvps = new ArrayList<KeyValuePair>();

			KeyValuePair xKvp = new KeyValuePair(XColumn, SqlUtilities.createSqlDouble(gyroscopeMeasurement.getRadiansRotationPerSecondAlongXAxis()));
			KeyValuePair yKvp = new KeyValuePair(YColumn, SqlUtilities.createSqlDouble(gyroscopeMeasurement.getRadiansRotationPerSecondAlongYAxis()));
			KeyValuePair zKvp = new KeyValuePair(ZColumn, SqlUtilities.createSqlDouble(gyroscopeMeasurement.getRadiansRotationPerSecondAlongZAxis()));
			KeyValuePair timestampKvp = new KeyValuePair(TimestampColumn, SqlUtilities.createSqlTimestamp(gyroscopeMeasurement.getTimeMeasured()));
			
			kvps.add(xKvp);
			kvps.add(yKvp);
			kvps.add(zKvp);
			kvps.add(timestampKvp);

			databaseEntry = new DatabaseEntry(kvps);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return databaseEntry;
	}

	@Override
	public RepositoryEntry<GyroscopeMeasurement> find(String id) {
		RepositoryEntry<GyroscopeMeasurement>  firstMatch = null;
		
		String queryString = "SELECT * FROM "+GyrMeasurementTable + " WHERE " + MeasurementIdColumn + "=" + id;
		
		List<DatabaseEntry> results = databaseInterface.sendQuery(queryString);
		
		if(results.size() > 0){
			DatabaseEntry databaseEntry = results.get(0);
			GyroscopeMeasurement rfidTagDetectedNotification = convertToGyroscopeMeasurement(databaseEntry);
			
			RepositoryEntry<GyroscopeMeasurement> repositoryEntry = new RepositoryEntry<GyroscopeMeasurement>(id, rfidTagDetectedNotification);
			firstMatch = repositoryEntry;
		}
		
		return firstMatch;
	}

	@Override
	public RepositoryEntry<GyroscopeMeasurement> add(
			GyroscopeMeasurement entry) {
		DatabaseEntry databaseEntry = convertToDatabaseEntry(entry);
		int val = databaseInterface.sendAdd(GyrMeasurementTable, databaseEntry);

		String id = String.format("%d", val);
		
		RepositoryEntry<GyroscopeMeasurement> repositoryEntry = new RepositoryEntry<GyroscopeMeasurement>(id, entry);
		
		return repositoryEntry;
	}

	@Override
	public void remove(String id) {
		String queryString = "DELETE FROM "+GyrMeasurementTable + " WHERE " + MeasurementIdColumn + "=" + id;
		
		databaseInterface.sendDelete(queryString);
	}

	@Override
	public void update(String id, GyroscopeMeasurement entry) {
		DatabaseEntry databaseEntry = convertToDatabaseEntry(entry);
		KeyValuePair primaryKey = new KeyValuePair(MeasurementIdColumn, id);
		
		databaseInterface.sendUpdate(GyrMeasurementTable, databaseEntry, primaryKey);
	}

	@Override
	public List<RepositoryEntry<GyroscopeMeasurement>> findAll() {
		List<RepositoryEntry<GyroscopeMeasurement> > points = new ArrayList<RepositoryEntry<GyroscopeMeasurement> >();
		
		String queryString = "SELECT * FROM "+GyrMeasurementTable;
		
		List<DatabaseEntry> results = databaseInterface.sendQuery(queryString);
		
		for(DatabaseEntry databaseEntry : results){
			GyroscopeMeasurement adjacentPoint = convertToGyroscopeMeasurement(databaseEntry);
			String id = DatabaseEntry.findColumnValue(databaseEntry, MeasurementIdColumn);
			
			RepositoryEntry<GyroscopeMeasurement> repositoryEntry = new RepositoryEntry<GyroscopeMeasurement>(id, adjacentPoint);
			
			points.add(repositoryEntry);
		}

		return points;
	}

	@Override
	public List<RepositoryEntry<GyroscopeMeasurement>> find(
			GyroscopeMeasurementSearchCriteria searchCriteria) {
		List<RepositoryEntry<GyroscopeMeasurement>>  matches = new ArrayList<RepositoryEntry<GyroscopeMeasurement>>();
		
		String queryString = "SELECT * FROM "+GyrMeasurementTable;
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
		
		if(!clauses.isEmpty()){
			queryString +=  " WHERE " + clauses;
		}
		
		List<DatabaseEntry> results = databaseInterface.sendQuery(queryString);
		
		for(DatabaseEntry databaseEntry : results) {

			GyroscopeMeasurement adjacentPoint = convertToGyroscopeMeasurement(databaseEntry);
			String id = DatabaseEntry.findColumnValue(databaseEntry, MeasurementIdColumn);
			
			RepositoryEntry<GyroscopeMeasurement> repositoryEntry = new RepositoryEntry<GyroscopeMeasurement>(id, adjacentPoint);
			matches.add(repositoryEntry);
		}
				
		return matches;
	}
	
}
