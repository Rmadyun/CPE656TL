package com.traintrax.navigation.database.library;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Class responsible for storing collected measurements
 * from accelerometers
 * @author Corey Sanders
 *
 */
public class AccelerometerMeasurementRepository implements FilteredSearchRepositoryInterface<AccelerometerMeasurement, AccelerometerMeasurementSearchCriteria>  {

	private static final String AccMeasurementTable = "Raw_Accelerometer";
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
	public AccelerometerMeasurementRepository(GenericDatabaseInterface databaseInterface){
		this.databaseInterface = databaseInterface;
	}
	
	/**
	 * Converts a DatabaseEntry into a AccelerometerMeasurement object
	 * @param databaseEntry Entry to convert
	 * @return AccelerometerMeasurement object that is representative of the values
	 * assigned to the database entry. Returns null if the entry
	 * cannot be converted.
	 */
	private static AccelerometerMeasurement convertToAccelerometerMeasurement(DatabaseEntry databaseEntry) {
		AccelerometerMeasurement accelerometerMeasurement = null;

		try {
			double x = Double.parseDouble(DatabaseEntry.findColumnValue(databaseEntry,
					XColumn));
			double y = Double.parseDouble(DatabaseEntry.findColumnValue(databaseEntry,
					YColumn));
			double z = Double.parseDouble(DatabaseEntry.findColumnValue(databaseEntry,
					ZColumn));
			Calendar timestamp = SqlUtilities.parseSqlTimestamp(DatabaseEntry.findColumnValue(databaseEntry,
					TimestampColumn));

			accelerometerMeasurement = new AccelerometerMeasurement(x,y,z,timestamp);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return accelerometerMeasurement;
	}
	
    /**
     * Converts a AccelerometerMeasurement object into a DatabaseEntry object
	 * @param databaseEntry Entry to convert
	 * @return DatabaseEntry object that is representative of the values
	 * assigned to the AccelerometerMeasurement object. Returns null if the entry
	 * cannot be converted.
	 */
	private static DatabaseEntry convertToDatabaseEntry(AccelerometerMeasurement accelerometerMeasurement) {
		DatabaseEntry databaseEntry = null;

		try {
			List<KeyValuePair> kvps = new ArrayList<KeyValuePair>();

			KeyValuePair xKvp = new KeyValuePair(XColumn, SqlUtilities.createSqlDouble(accelerometerMeasurement.getMetersPerSecondSquaredAlongXAxis()));
			KeyValuePair yKvp = new KeyValuePair(YColumn, SqlUtilities.createSqlDouble(accelerometerMeasurement.getMetersPerSecondSquaredAlongYAxis()));
			KeyValuePair zKvp = new KeyValuePair(ZColumn, SqlUtilities.createSqlDouble(accelerometerMeasurement.getMetersPerSecondSquaredAlongZAxis()));
			KeyValuePair timestampKvp = new KeyValuePair(TimestampColumn, SqlUtilities.createSqlTimestamp(accelerometerMeasurement.getTimeMeasured()));
			
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
	public RepositoryEntry<AccelerometerMeasurement> find(String id) {
		RepositoryEntry<AccelerometerMeasurement>  firstMatch = null;
		
		String queryString = "SELECT * FROM "+AccMeasurementTable + " WHERE " + MeasurementIdColumn + "=" + id;
		
		List<DatabaseEntry> results = databaseInterface.sendQuery(queryString);
		
		if(results.size() > 0){
			DatabaseEntry databaseEntry = results.get(0);
			AccelerometerMeasurement rfidTagDetectedNotification = convertToAccelerometerMeasurement(databaseEntry);
			
			RepositoryEntry<AccelerometerMeasurement> repositoryEntry = new RepositoryEntry<AccelerometerMeasurement>(id, rfidTagDetectedNotification);
			firstMatch = repositoryEntry;
		}
		
		return firstMatch;
	}

	@Override
	public RepositoryEntry<AccelerometerMeasurement> add(
			AccelerometerMeasurement entry) {
		DatabaseEntry databaseEntry = convertToDatabaseEntry(entry);
		int val = databaseInterface.sendAdd(AccMeasurementTable, databaseEntry);

		String id = String.format("%d", val);
		
		RepositoryEntry<AccelerometerMeasurement> repositoryEntry = new RepositoryEntry<AccelerometerMeasurement>(id, entry);
		
		return repositoryEntry;
	}

	@Override
	public void remove(String id) {
		String queryString = "DELETE FROM "+AccMeasurementTable + " WHERE " + MeasurementIdColumn + "=" + id;
		
		databaseInterface.sendDelete(queryString);
	}

	@Override
	public void update(String id, AccelerometerMeasurement entry) {
		DatabaseEntry databaseEntry = convertToDatabaseEntry(entry);
		KeyValuePair primaryKey = new KeyValuePair(MeasurementIdColumn, id);
		
		databaseInterface.sendUpdate(AccMeasurementTable, databaseEntry, primaryKey);
	}

	@Override
	public List<RepositoryEntry<AccelerometerMeasurement>> findAll() {
		List<RepositoryEntry<AccelerometerMeasurement> > points = new ArrayList<RepositoryEntry<AccelerometerMeasurement> >();
		
		String queryString = "SELECT * FROM "+AccMeasurementTable;
		
		List<DatabaseEntry> results = databaseInterface.sendQuery(queryString);
		
		for(DatabaseEntry databaseEntry : results){
			AccelerometerMeasurement adjacentPoint = convertToAccelerometerMeasurement(databaseEntry);
			String id = DatabaseEntry.findColumnValue(databaseEntry, MeasurementIdColumn);
			
			RepositoryEntry<AccelerometerMeasurement> repositoryEntry = new RepositoryEntry<AccelerometerMeasurement>(id, adjacentPoint);
			
			points.add(repositoryEntry);
		}

		return points;
	}

	@Override
	public List<RepositoryEntry<AccelerometerMeasurement>> find(
			AccelerometerMeasurementSearchCriteria searchCriteria) {
		List<RepositoryEntry<AccelerometerMeasurement>>  matches = new ArrayList<RepositoryEntry<AccelerometerMeasurement>>();
		
		String queryString = "SELECT * FROM "+AccMeasurementTable;
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

			AccelerometerMeasurement adjacentPoint = convertToAccelerometerMeasurement(databaseEntry);
			String id = DatabaseEntry.findColumnValue(databaseEntry, MeasurementIdColumn);
			
			RepositoryEntry<AccelerometerMeasurement> repositoryEntry = new RepositoryEntry<AccelerometerMeasurement>(id, adjacentPoint);
			matches.add(repositoryEntry);
		}
				
		return matches;
	}
	
}
