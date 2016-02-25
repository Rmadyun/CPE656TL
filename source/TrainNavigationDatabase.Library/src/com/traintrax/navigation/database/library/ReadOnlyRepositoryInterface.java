package com.traintrax.navigation.database.library;

import java.util.List;

/**
 * Generic interface for reading from a repository of
 * data objects
 * @author Corey Sanders
 *
 * @param <TData> Type of data being stored
 */
public interface ReadOnlyRepositoryInterface<TData> {
	
	/**
	 * Searches for a particular entry in the repository
	 * @param id Unique identifier for the entry
	 * @return Returns the requested entry or null if not found.
	 */
	RepositoryEntry<TData> find(String id);
	
	/**
	 * Returns all known entries in the repository
	 * @return All known entries in the repository
	 */
	List<RepositoryEntry<TData>> findAll();
}
