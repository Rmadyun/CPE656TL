package TrainNavigationDatabase;

import java.util.List;

/**
 * Generic interface for interacting with a repository of
 * data objects
 * @author Corey Sanders
 *
 * @param <TData> Type of data being stored
 */
public interface RepositoryInterface<TData> {
	
	/**
	 * Searches for a particular entry in the repository
	 * @param id Unique identifier for the entry
	 * @return Returns the requested entry or null if not found.
	 */
	TData find(String id);
	
	/**
	 * Adds a new entry into the repository
	 * @param entry Entry that needs to be added
	 * @return Unique identifier for the entry
	 */
	String add(TData entry);
	
	/**
	 * Removes an entry from the repository
	 * @param id Unique identifier for the entry
	 */
	void remove(String id);
	
	/**
	 * Updates an existing entry in the repository
	 * @param id Unique identifier for the entry
	 * @param entry Entry to update
	 */
	void update(String id, TData entry);
	
	/**
	 * Returns all known entries in the repository
	 * @return All known entries in the repository
	 */
	List<TData> findAll();
}
