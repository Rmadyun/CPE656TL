package com.traintrax.navigation.database.library.unittest;
import static org.junit.Assert.*;

import java.util.List;

import com.traintrax.navigation.database.library.FilteredSearchRepositoryInterface;
import com.traintrax.navigation.database.library.RepositoryEntry;

public abstract class FilteredSearchRepositoryInterfaceTests<TData, TSearchCriteria> {

	
	public void TestFindWithInvalidId(FilteredSearchRepositoryInterface<TData, TSearchCriteria> repository){
		
		String dummyId = "-1";
		
		RepositoryEntry<TData> repositoryEntry = repository.find(dummyId);
		
		assertEquals(null, repositoryEntry);
	}
	
	public void TestRemoveWithInvalidId(FilteredSearchRepositoryInterface<TData, TSearchCriteria> repository){
		
		String dummyId = "-1";
			
		//We expect it to silently ignore the error and not update anything
		//This fails if an exception is thrown.
		repository.remove(dummyId);
	}
	
	public void TestUpdateWithInvalidId(FilteredSearchRepositoryInterface<TData, TSearchCriteria> repository, TData newEntry){
		
		String dummyId = "-1";

		//We expect it to silently ignore the error and not update anything
		//This fails if an exception is thrown.
		repository.update(dummyId, newEntry);
	}
	
	public void TestAdd(FilteredSearchRepositoryInterface<TData, TSearchCriteria> repository, TData newEntry){
		
		RepositoryEntry<TData> repositoryEntry = repository.add(newEntry);
		
		assertEquals(newEntry, repositoryEntry.getValue());
		
		//cleanup
		repository.remove(repositoryEntry.getId());
	}
	
	public void TestUpdate(FilteredSearchRepositoryInterface<TData, TSearchCriteria> repository, TData newEntry, TData updatedEntry){
		
		RepositoryEntry<TData> repositoryEntry = repository.add(newEntry);
		
		assertEquals(newEntry, repositoryEntry.getValue());
		//cleanup
		
		repository.update(repositoryEntry.getId(), updatedEntry);
		
		RepositoryEntry<TData> foundEntry = repository.find(repositoryEntry.getId());
		
		assertEquals(updatedEntry, foundEntry.getValue());
		
		//cleanup
		repository.remove(repositoryEntry.getId());
	}
	
	public void TestFindById(FilteredSearchRepositoryInterface<TData, TSearchCriteria> repository, TData newEntry){
		
		RepositoryEntry<TData> repositoryEntry = repository.add(newEntry);
				
		RepositoryEntry<TData> foundEntry = repository.find(repositoryEntry.getId());
		
		assertEquals(newEntry, foundEntry.getValue());
		
		//cleanup
		repository.remove(repositoryEntry.getId());
	}
	
	public void TestRemove(FilteredSearchRepositoryInterface<TData, TSearchCriteria> repository, TData newEntry){
		
		RepositoryEntry<TData> repositoryEntry = repository.add(newEntry);
		
		repository.remove(repositoryEntry.getId());
				
		RepositoryEntry<TData> foundEntry = repository.find(repositoryEntry.getId());
		
		assertEquals(null, foundEntry);
	}
	
	public void TestFindAll(FilteredSearchRepositoryInterface<TData, TSearchCriteria> repository, TData newEntry){
		
		RepositoryEntry<TData> repositoryEntry = repository.add(newEntry);
		
		List<RepositoryEntry<TData>> allItems = repository.findAll();
		
		assertTrue(allItems.contains(repositoryEntry));
		
		repository.remove(repositoryEntry.getId());
	}
	
	public void TestFindSearchCriteria(FilteredSearchRepositoryInterface<TData, TSearchCriteria> repository, TData newEntry, TSearchCriteria searchCriteria){
		
		RepositoryEntry<TData> repositoryEntry = repository.add(newEntry);
				
		List<RepositoryEntry<TData>> matches = repository.find(searchCriteria);

		boolean matchFound = false;
		for(RepositoryEntry<TData> match: matches){
			if(match.getValue().equals(newEntry)){
				matchFound = true;
				break;
			}
		}
		assertTrue(matchFound);
		
		//cleanup
		repository.remove(repositoryEntry.getId());
	}
	
	public void TestFindSearchCriteriaWithNoMatches(FilteredSearchRepositoryInterface<TData, TSearchCriteria> repository, TData newEntry, TSearchCriteria searchCriteria){
		
		RepositoryEntry<TData> repositoryEntry = repository.add(newEntry);
				
		List<RepositoryEntry<TData>> matches = repository.find(searchCriteria);

		boolean matchFound = false;
		for(RepositoryEntry<TData> match: matches){
			if(match.getValue().equals(newEntry)){
				matchFound = true;
				break;
			}
		}
		assertFalse(matchFound);
		
		//cleanup
		repository.remove(repositoryEntry.getId());
	}
}
