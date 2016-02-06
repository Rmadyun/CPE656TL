import java.util.List;

import TrainNavigationDatabase.*;


public class DatabaseTestProgram {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
        MySqlDatabaseAdapter mySqlDatabaseAdapter = new MySqlDatabaseAdapter();
        
        mySqlDatabaseAdapter.connect();
        
        TrackPointRepository trackPointRepository = new TrackPointRepository(mySqlDatabaseAdapter);
        
        List<RepositoryEntry<TrackPoint>> allEntries = trackPointRepository.findAll();
        RepositoryEntry<TrackPoint> secondEntry = trackPointRepository.find("2");
        
        TrackPoint newTrackPoint = new TrackPoint("Name", "Point", 1, 1, 1, "2", "Tag");
        
        RepositoryEntry<TrackPoint> addedEntry = trackPointRepository.add(newTrackPoint);
 
        trackPointRepository.remove(addedEntry.getId());
        
        importTrackMeasurements("TestTrackMeasurements.csv", mySqlDatabaseAdapter);
        
	}
	
	private static void importTrackMeasurements(String filename, GenericDatabaseInterface databaseInterface){
		List<TrackPointMeasurement> measurements = TrackMeasurementsReader.ReadFile(filename);
		
		//Save Measurements
		for(TrackPointMeasurement measurement : measurements){
			saveMeasurement(measurement, databaseInterface);
		}
		
		//Save Adjacency List
		for(TrackPointMeasurement measurement : measurements){
			saveAdjacencyList(measurement, databaseInterface);
		}
	}
	
	private static void saveMeasurement(TrackPointMeasurement measurement, GenericDatabaseInterface databaseInterface){
		TrackPointRepository trackPointRepository = new TrackPointRepository(databaseInterface);
		TrackBlockRepository trackBlockRepository = new TrackBlockRepository(databaseInterface);
		AdjacentPointRepository adjacentPointRepository = new AdjacentPointRepository(databaseInterface);
				
		//Add track block
		String blockId = "";
		String blockName = measurement.getBlockName().trim();
		if(!blockName.isEmpty()){
			TrackBlockSearchCriteria searchCriteria = new TrackBlockSearchCriteria();
			searchCriteria.setBlockName(blockName);
			List<RepositoryEntry<TrackBlock>> matches = trackBlockRepository.find(searchCriteria);
			
			//Assuming that there is only one match
			if(matches.size() > 0){
				RepositoryEntry<TrackBlock> trackBlockEntry = matches.get(0);
				
				blockId = trackBlockEntry.getId();
			}
			
		}
		
		//Add point
		TrackPoint trackPoint = new TrackPoint(measurement.getPointName(),
				measurement.getPointType(),
				measurement.getxInches(),
				measurement.getyInches(),
				0,
				blockId,
				"");
		
		RepositoryEntry<TrackPoint> addedEntry = trackPointRepository.add(trackPoint);
			
	}
	
	private static String getPointId(String pointName, FilteredSearchRepositoryInterface<TrackPoint, TrackPointSearchCriteria> trackPointRepository){
		
		String pointId = null;

		if(!pointName.isEmpty()){
			TrackPointSearchCriteria searchCriteria = new TrackPointSearchCriteria();
			searchCriteria.setName(pointName);
			List<RepositoryEntry<TrackPoint>> matches = trackPointRepository.find(searchCriteria);
			
			//Assuming that there is only one match
			if(matches.size() > 0){
				RepositoryEntry<TrackPoint> trackPointEntry = matches.get(0);
				
				pointId = trackPointEntry.getId();
			}
		}
		
		return pointId;
	}
	
	private static void saveAdjacencyList(TrackPointMeasurement measurement, GenericDatabaseInterface databaseInterface){
		TrackPointRepository trackPointRepository = new TrackPointRepository(databaseInterface);
		AdjacentPointRepository adjacentPointRepository = new AdjacentPointRepository(databaseInterface);
				
        //Find Point
		String pointId = getPointId(measurement.getPointName().trim(), trackPointRepository);
				
		if (pointId != null) {

			// Add adjacent points
			for (TrackPointMeasurement adjacentMeasurement : measurement
					.getAdjacentPoints()) {
				
				String adjacentPointId = getPointId(adjacentMeasurement.getPointName().trim(), trackPointRepository);
				
				if(adjacentPointId != null){
					AdjacentPoint adjacentPoint = new AdjacentPoint(Integer.parseInt(pointId), Integer.parseInt(adjacentPointId));
					
					adjacentPointRepository.add(adjacentPoint);
				}
			}
		}
		
	}

}
