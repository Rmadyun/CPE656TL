package TrainNavigationDatabase;

/**
 * Interface for a read-only data repository that also has filtered searches.
 * @author Corey Sanders
 * This interface was created to simplify testing of all of the implemented repositories
 * in the database since they are planned to all include this capability
 * @param <TData> Type of items that are retrieved from the search
 * @param <TSearchCriteria> Type use to describe which items can be 
 * considered matches for the search
 */
public interface FilteredSearchReadOnlyRepositoryInterface<TData,TSearchCriteria> extends ReadOnlyRepositoryInterface<TData>, FilteredSearchInterface<RepositoryEntry<TData>, TSearchCriteria>{
}
