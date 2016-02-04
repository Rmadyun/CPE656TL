package TrainNavigationDatabase;

import java.util.List;

/**
 * Generic interface for performing a filtered search for
 * items
 * @author death
 *
 * @param <TData> Type of items that are retrieved from the search
 * @param <TSearchCriteria> Type use to describe which items can be 
 * considered matches for the search
 */
public interface FilteredSearchInterface<TData, TSearchCriteria> {

	List<TData> find(TSearchCriteria searchCriteria);
}