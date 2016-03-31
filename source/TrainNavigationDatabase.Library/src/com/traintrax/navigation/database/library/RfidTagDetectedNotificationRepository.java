package com.traintrax.navigation.database.library;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Class responsible for storing all of the known
 * RFID Tag Detected events reported to the system.
 * @author Corey Sanders
 *
 */
public class RfidTagDetectedNotificationRepository implements FilteredSearchRepositoryInterface<RfidTagDetectedNotification, RfidTagDetectedNotificationSearchCriteria>  {
	
	private static final String RfidTagDetectedEventTable = "Raw_RfidTagReader";
    private static final String EventIdColumn = "measurement_id";
    private static final String RfidTagValueColumn = "rfid_tag";
    private static final String TimestampColumn = "timestamp";
	private final GenericDatabaseInterface databaseInterface;
	
	/**
	 * Constructor
	 * @param databaseInterface Contact to the database storing the point
	 * information
	 */
	public RfidTagDetectedNotificationRepository(GenericDatabaseInterface databaseInterface){
		this.databaseInterface = databaseInterface;
	}
	
	/**
	 * Converts a DatabaseEntry into a RfidTagDetectedNotification object
	 * @param databaseEntry Entry to convert
	 * @return RfidTagDetectedNotification object that is representative of the values
	 * assigned to the database entry. Returns null if the entry
	 * cannot be converted.
	 */
	private static RfidTagDetectedNotification convertToRfidTagDetectedNotification(DatabaseEntry databaseEntry) {
		RfidTagDetectedNotification rfidTagDetectedNotification = null;

		try {
			String rfidTagValue = DatabaseEntry.findColumnValue(databaseEntry,
					RfidTagValueColumn);
			Calendar timestamp = SqlUtilities.parseSqlTimestamp(DatabaseEntry.findColumnValue(databaseEntry,
					TimestampColumn));

			rfidTagDetectedNotification = new RfidTagDetectedNotification(rfidTagValue, timestamp);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return rfidTagDetectedNotification;
	}
	
    /**
     * Converts a RfidTagDetectedNotification object into a DatabaseEntry object
	 * @param databaseEntry Entry to convert
	 * @return DatabaseEntry object that is representative of the values
	 * assigned to the RfidTagDetectedNotification object. Returns null if the entry
	 * cannot be converted.
	 */
	private static DatabaseEntry convertToDatabaseEntry(RfidTagDetectedNotification adjacentPoint) {
		DatabaseEntry databaseEntry = null;

		try {
			List<KeyValuePair> kvps = new ArrayList<KeyValuePair>();

			KeyValuePair rfidTagValueKvp = new KeyValuePair(RfidTagValueColumn, SqlUtilities.createSqlString(adjacentPoint.getRfidTagValue()));
			KeyValuePair timestampKvp = new KeyValuePair(TimestampColumn, SqlUtilities.createSqlTimestamp(adjacentPoint.getTimeDetected()));
			
			kvps.add(rfidTagValueKvp);
			kvps.add(timestampKvp);

			databaseEntry = new DatabaseEntry(kvps);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return databaseEntry;
	}

	@Override
	public RepositoryEntry<RfidTagDetectedNotification> find(String id) {
		RepositoryEntry<RfidTagDetectedNotification>  firstMatch = null;
		
		String queryString = "SELECT * FROM "+RfidTagDetectedEventTable + " WHERE " + EventIdColumn + "=" + id;
		
		List<DatabaseEntry> results = databaseInterface.sendQuery(queryString);
		
		if(results.size() > 0){
			DatabaseEntry databaseEntry = results.get(0);
			RfidTagDetectedNotification rfidTagDetectedNotification = convertToRfidTagDetectedNotification(databaseEntry);
			
			RepositoryEntry<RfidTagDetectedNotification> repositoryEntry = new RepositoryEntry<RfidTagDetectedNotification>(id, rfidTagDetectedNotification);
			firstMatch = repositoryEntry;
		}
		
		return firstMatch;
	}

	@Override
	public RepositoryEntry<RfidTagDetectedNotification> add(
			RfidTagDetectedNotification entry) {
		DatabaseEntry databaseEntry = convertToDatabaseEntry(entry);
		int val = databaseInterface.sendAdd(RfidTagDetectedEventTable, databaseEntry);

		String id = String.format("%d", val);
		
		RepositoryEntry<RfidTagDetectedNotification> repositoryEntry = new RepositoryEntry<RfidTagDetectedNotification>(id, entry);
		
		return repositoryEntry;
	}

	@Override
	public void remove(String id) {
		String queryString = "DELETE FROM "+RfidTagDetectedEventTable + " WHERE " + EventIdColumn + "=" + id;
		
		databaseInterface.sendDelete(queryString);
	}

	@Override
	public void update(String id, RfidTagDetectedNotification entry) {
		DatabaseEntry databaseEntry = convertToDatabaseEntry(entry);
		KeyValuePair primaryKey = new KeyValuePair(EventIdColumn, id);
		
		databaseInterface.sendUpdate(RfidTagDetectedEventTable, databaseEntry, primaryKey);
	}

	@Override
	public List<RepositoryEntry<RfidTagDetectedNotification>> findAll() {
		List<RepositoryEntry<RfidTagDetectedNotification> > points = new ArrayList<RepositoryEntry<RfidTagDetectedNotification> >();
		
		String queryString = "SELECT * FROM "+RfidTagDetectedEventTable;
		
		List<DatabaseEntry> results = databaseInterface.sendQuery(queryString);
		
		for(DatabaseEntry databaseEntry : results){
			RfidTagDetectedNotification adjacentPoint = convertToRfidTagDetectedNotification(databaseEntry);
			String id = DatabaseEntry.findColumnValue(databaseEntry, EventIdColumn);
			
			RepositoryEntry<RfidTagDetectedNotification> repositoryEntry = new RepositoryEntry<RfidTagDetectedNotification>(id, adjacentPoint);
			
			points.add(repositoryEntry);
		}

		return points;
	}

	@Override
	public List<RepositoryEntry<RfidTagDetectedNotification>> find(
			RfidTagDetectedNotificationSearchCriteria searchCriteria) {
		List<RepositoryEntry<RfidTagDetectedNotification>>  matches = new ArrayList<RepositoryEntry<RfidTagDetectedNotification>>();
		
		String queryString = "SELECT * FROM "+RfidTagDetectedEventTable;
		String clauses = "";
		
		String rfidTagValue = searchCriteria.getRfidTagValue();
		if(rfidTagValue != null && !rfidTagValue.isEmpty()){
			
			if(!clauses.isEmpty()){
				clauses += " AND ";
			}
			
			clauses += RfidTagValueColumn + "=" + SqlUtilities.createSqlString(rfidTagValue);
		}
		
		if(!clauses.isEmpty()){
			queryString +=  " WHERE " + clauses;
		}
		
		List<DatabaseEntry> results = databaseInterface.sendQuery(queryString);
		
		for(DatabaseEntry databaseEntry : results) {

			RfidTagDetectedNotification adjacentPoint = convertToRfidTagDetectedNotification(databaseEntry);
			String id = DatabaseEntry.findColumnValue(databaseEntry, EventIdColumn);
			
			RepositoryEntry<RfidTagDetectedNotification> repositoryEntry = new RepositoryEntry<RfidTagDetectedNotification>(id, adjacentPoint);
			matches.add(repositoryEntry);
		}
				
		return matches;
	}

}
