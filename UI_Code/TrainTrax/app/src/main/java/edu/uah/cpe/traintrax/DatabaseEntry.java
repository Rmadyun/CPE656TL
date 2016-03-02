package edu.uah.cpe.traintrax;
import java.util.List;

/**
 * Class represents a single item that is stored in a
 * database.
 * @author Corey Sanders
 *
 */
public class DatabaseEntry {
	
	private List<KeyValuePair> columns;

	/**
	 * Constructor
	 * @param columns Collection of columns that are associated with
	 * this database entry. The keys are the column names. The values are the
	 * values associated with the respective column.
	 */
	public DatabaseEntry(List<KeyValuePair> columns){
		this.columns = columns;
	}

	/**
	 * Retrieve the collection of columns that are associated with
	 * this database entry. The keys are the column names. The values are the
	 * values associated with the respective column.
	 * @return
	 */
	public List<KeyValuePair> getColumns(){
		return columns;
	}
	
    /**
     * Searches for the value for a particular column in a database entry 
     * @param databaseEntry Entry to search in
     * @param columnName Name associated with the column
     * @return value of the column. Returns null if not found.
     */
	public static String findColumnValue(DatabaseEntry databaseEntry,
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

}
