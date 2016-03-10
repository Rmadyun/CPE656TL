package com.traintrax.navigation.database.library;

/**
 * Generic interface for interacting with a repository of
 * data objects
 * @author Corey Sanders
 *
 * @param <TData> Type of data being stored
 */
public interface RepositoryInterface<TData> extends ReadOnlyRepositoryInterface<TData> {
	
	/**
	 * Adds a new entry into the repository
	 * @param entry Entry that needs to be added
	 * @return Unique identifier for the entry
	 */
	RepositoryEntry<TData> add(TData entry);
	
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
	
}
