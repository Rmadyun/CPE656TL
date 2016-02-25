package com.traintrax.navigation.database.rest.test;
import java.util.List;

import com.traintrax.navigation.database.library.FilteredSearchReadOnlyRepositoryInterface;
import com.traintrax.navigation.database.library.RepositoryEntry;
import com.traintrax.navigation.database.library.TrackPoint;
import com.traintrax.navigation.database.library.TrackPointSearchCriteria;

/**
 * The purpose of this class is so that test implementations of repositories my be run to verify
 * that they work correctly
 * @author Corey Sanders
 *
 */
public class TestProgramForTestInterfaces {

	/**
	 * Main Entry Point for test program
	 * @param args Program Arguments
	 */
	public static void main(String[] args) {
		
		FilteredSearchReadOnlyRepositoryInterface<TrackPoint, TrackPointSearchCriteria> repo = new TestTrackPointRepository();
		
		List<RepositoryEntry<TrackPoint>> allEntries = repo.findAll();
		
		for(RepositoryEntry<TrackPoint> entry : allEntries){
			System.out.println(entry.getValue().getPointName());
		}
	}

}
