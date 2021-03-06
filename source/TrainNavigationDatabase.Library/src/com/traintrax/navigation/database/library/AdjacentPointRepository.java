package com.traintrax.navigation.database.library;

import java.util.ArrayList;
import java.util.List;

public class AdjacentPointRepository implements FilteredSearchRepositoryInterface<AdjacentPoint, AdjacentPointSearchCriteria>  {
	private static final String AdjacentPointTable = "Adjacent_Points";
    private static final String ListIdColumn = "list_id";
    private static final String PointIdColumn = "point_id";
    private static final String AdjacentPointIdColumn = "adjacent_point_id";
	private final GenericDatabaseInterface databaseInterface;
	
	/**
	 * Constructor
	 * @param databaseInterface Contact to the database storing the point
	 * information
	 */
	public AdjacentPointRepository(GenericDatabaseInterface databaseInterface){
		this.databaseInterface = databaseInterface;
	}
	
	/**
	 * Converts a DatabaseEntry into a AdjacentPoint object
	 * @param databaseEntry Entry to convert
	 * @return AdjacentPoint object that is representative of the values
	 * assigned to the database entry. Returns null if the entry
	 * cannot be converted.
	 */
	private static AdjacentPoint convertToAdjacentPoint(DatabaseEntry databaseEntry) {
		AdjacentPoint adjacentPoint = null;

		try {
			int pointId = Integer.parseInt(DatabaseEntry.findColumnValue(databaseEntry,
					PointIdColumn));
			int adjacentPointId = Integer.parseInt(DatabaseEntry.findColumnValue(databaseEntry,
					AdjacentPointIdColumn));

			adjacentPoint = new AdjacentPoint(pointId, adjacentPointId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return adjacentPoint;
	}
	
    /**
     * Converts a AdjacentPoint object into a DatabaseEntry object
	 * @param databaseEntry Entry to convert
	 * @return DatabaseEntry object that is representative of the values
	 * assigned to the AdjacentPoint object. Returns null if the entry
	 * cannot be converted.
	 */
	private static DatabaseEntry convertToDatabaseEntry(AdjacentPoint adjacentPoint) {
		DatabaseEntry databaseEntry = null;

		try {
			List<KeyValuePair> kvps = new ArrayList<KeyValuePair>();

			KeyValuePair pointIdKvp = new KeyValuePair(PointIdColumn, SqlUtilities.createSqlInt(adjacentPoint.getPointId()));
			KeyValuePair adjacentPointIdKvp = new KeyValuePair(AdjacentPointIdColumn, SqlUtilities.createSqlInt(adjacentPoint.getAdjacentPointId()));
			
			kvps.add(pointIdKvp);
			kvps.add(adjacentPointIdKvp);

			databaseEntry = new DatabaseEntry(kvps);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return databaseEntry;
	}
    
	@Override
	public RepositoryEntry<AdjacentPoint> find(String id) {
		RepositoryEntry<AdjacentPoint>  firstMatch = null;
		
		String queryString = "SELECT * FROM "+AdjacentPointTable + " WHERE " + ListIdColumn + "=" + id;
		
		List<DatabaseEntry> results = databaseInterface.sendQuery(queryString);
		
		if(results.size() > 0){
			DatabaseEntry databaseEntry = results.get(0);
			AdjacentPoint adjacentPoint = convertToAdjacentPoint(databaseEntry);
			
			RepositoryEntry<AdjacentPoint> repositoryEntry = new RepositoryEntry<AdjacentPoint>(id, adjacentPoint);
			firstMatch = repositoryEntry;
		}
		
		return firstMatch;
	}

	@Override
	public RepositoryEntry<AdjacentPoint> add(AdjacentPoint entry) {
		DatabaseEntry databaseEntry = convertToDatabaseEntry(entry);
		int val = databaseInterface.sendAdd(AdjacentPointTable, databaseEntry);

		String id = String.format("%d", val);
		
		RepositoryEntry<AdjacentPoint> repositoryEntry = new RepositoryEntry<AdjacentPoint>(id, entry);
		
		return repositoryEntry;
	}

	@Override
	public void remove(String id) {
		String queryString = "DELETE FROM "+AdjacentPointTable + " WHERE " + ListIdColumn + "=" + id;
		
		databaseInterface.sendDelete(queryString);
	}

	@Override
	public void update(String id, AdjacentPoint entry) {
		DatabaseEntry databaseEntry = convertToDatabaseEntry(entry);
		KeyValuePair primaryKey = new KeyValuePair(ListIdColumn, id);
		
		databaseInterface.sendUpdate(AdjacentPointTable, databaseEntry, primaryKey);
		
	}

	@Override
	public List<RepositoryEntry<AdjacentPoint>> findAll() {
		List<RepositoryEntry<AdjacentPoint> > points = new ArrayList<RepositoryEntry<AdjacentPoint> >();
		
		String queryString = "SELECT * FROM "+AdjacentPointTable;
		
		List<DatabaseEntry> results = databaseInterface.sendQuery(queryString);
		
		for(DatabaseEntry databaseEntry : results){
			AdjacentPoint adjacentPoint = convertToAdjacentPoint(databaseEntry);
			String id = DatabaseEntry.findColumnValue(databaseEntry, ListIdColumn);
			
			RepositoryEntry<AdjacentPoint> repositoryEntry = new RepositoryEntry<AdjacentPoint>(id, adjacentPoint);
			
			points.add(repositoryEntry);
		}

		return points;
	}

	@Override
	public List<RepositoryEntry<AdjacentPoint>> find(
			AdjacentPointSearchCriteria searchCriteria) {
		List<RepositoryEntry<AdjacentPoint>>  matches = new ArrayList<RepositoryEntry<AdjacentPoint>>();
		
		String queryString = "SELECT * FROM "+AdjacentPointTable;
		String clauses = "";
		
		String pointId = searchCriteria.getPointId();
		if(pointId != null && !pointId.isEmpty()){
			
			if(!clauses.isEmpty()){
				clauses += " AND ";
			}
			
			clauses += PointIdColumn + "=" + SqlUtilities.createSqlInt(pointId);
		}
		
		String adjacentPointId = searchCriteria.getAdjacentPointId();
		if(adjacentPointId != null && !adjacentPointId.isEmpty()){
			if(!clauses.isEmpty()){
				clauses += " AND ";
			}
			
			clauses += AdjacentPointIdColumn + "=" + SqlUtilities.createSqlInt(adjacentPointId);
		}
		
		if(!clauses.isEmpty()){
			queryString +=  " WHERE " + clauses;
		}
		
		List<DatabaseEntry> results = databaseInterface.sendQuery(queryString);
		
		for(DatabaseEntry databaseEntry : results) {

			AdjacentPoint adjacentPoint = convertToAdjacentPoint(databaseEntry);
			String id = DatabaseEntry.findColumnValue(databaseEntry, ListIdColumn);
			
			RepositoryEntry<AdjacentPoint> repositoryEntry = new RepositoryEntry<AdjacentPoint>(id, adjacentPoint);
			matches.add(repositoryEntry);
		}
				
		return matches;
	}

}
