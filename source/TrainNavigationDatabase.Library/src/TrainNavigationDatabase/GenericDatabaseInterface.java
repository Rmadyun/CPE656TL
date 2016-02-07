package TrainNavigationDatabase;

import java.util.List;

/**
 * Generic interface for controlling 
 * a database.
 * @author Corey Sanders
 *
 */
public interface GenericDatabaseInterface {
	
	/**
	 * Initiates a connection to the database
	 */
    void connect();
    
    /**
     * Queries the database for information
     * @param queryString SQL query string to use to request information
     * @return Collection of database entries that match the query
     */
    List<DatabaseEntry> sendQuery(String queryString);

    /**
     * Requests for an entry to be added into the database
     * @param tableName Name of the table that the entry belongs to.
     * @param databaseEntry Entry to add
     * @return ID of the key associated with the added entry.
     */
	int sendAdd(String tableName, DatabaseEntry databaseEntry);
	
	/**
	 * Requests for one or more entries to be removed from the database
	 * @param queryString SQL query to use to define the entries to remove
	 */
	void sendDelete(String queryString);

	/**
	 * Requests for an entry to be updated within the database
     * @param tableName Name of the table that the entry belongs to.
     * @param databaseEntry Entry to update
	 * @param primaryKey Column that acts as the primary key
	 * of the table.
	 */
	void sendUpdate(String tableName, DatabaseEntry databaseEntry,
			KeyValuePair primaryKey);
    
    
}
