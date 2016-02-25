package com.traintrax.navigation.database.library;

/**
 * Interface for a data repository that also has filtered searches.
 * @author Corey Sanders
 * This interface was created to simplify testing of all of the implemented repositories
 * in the database since they are planned to all include this capability
 * @param <TData> Type of items that are retrieved from the search
 * @param <TSearchCriteria> Type use to describe which items can be 
 * considered matches for the search
 */
public interface FilteredSearchRepositoryInterface<TData,TSearchCriteria> extends RepositoryInterface<TData>, FilteredSearchInterface<RepositoryEntry<TData>, TSearchCriteria>{
}
