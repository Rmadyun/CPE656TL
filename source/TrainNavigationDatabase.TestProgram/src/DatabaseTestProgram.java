import java.util.List;

import TrainNavigationDatabase.*;

public class DatabaseTestProgram {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MySqlDatabaseAdapter mySqlDatabaseAdapter = new MySqlDatabaseAdapter();

		mySqlDatabaseAdapter.connect();

		importTrackMeasurements("/home/death/Documents/CPE658/TestTrackMeasurements.csv", mySqlDatabaseAdapter);

	}

	private static void importTrackSwitchMeasurements(String filename, GenericDatabaseInterface databaseInterface) {
		List<TrackSwitchMeasurement> switchMeasurements = TrackSwitchMeasurementsReader.ReadFile(filename);

		// Save measurements

		for (TrackSwitchMeasurement measurement : switchMeasurements) {
			saveMeasurement(measurement, databaseInterface);
		}
	}

	private static void saveMeasurement(TrackSwitchMeasurement measurement,
			GenericDatabaseInterface databaseInterface) {
		
		// Add track block
		String blockId = "";
		String passBlockId = "";
		String bypassBlockId = "";
		String blockName = measurement.getBlockName().trim();
		String passBlockName = measurement.getPassBlockName().trim();
		String bypassBlockName = measurement.getBypassBlockName().trim();

		RepositoryEntry<TrackBlock> trackBlockEntry = getOrAddTrackBlock(blockName, databaseInterface);

		if (trackBlockEntry != null) {
			blockId = trackBlockEntry.getId();
		}

		RepositoryEntry<TrackBlock> passTrackBlockEntry = getOrAddTrackBlock(passBlockName, databaseInterface);

		if (passTrackBlockEntry != null) {
			passBlockId = passTrackBlockEntry.getId();
		}

		RepositoryEntry<TrackBlock> bypassTrackBlockEntry = getOrAddTrackBlock(bypassBlockName, databaseInterface);

		if (bypassTrackBlockEntry != null) {
			bypassBlockId = bypassTrackBlockEntry.getId();
		}

		// New Track Point
		TrackPoint updatedTrackPoint = new TrackPoint(measurement.getSwitchName(), "Switch", measurement.getxInches(),
				measurement.getyInches(), 0, blockId, "");

		RepositoryEntry<TrackPoint> trackPointEntry = updateOrAddEntry(updatedTrackPoint, databaseInterface);

		// New Track Switch
		TrackSwitch updatedTrackSwitch = new TrackSwitch(measurement.getSwitchName(), trackPointEntry.getId(),
				passBlockId, bypassBlockId);

		updateOrAddEntry(updatedTrackSwitch, databaseInterface);

	}

	private static RepositoryEntry<TrackSwitch> updateOrAddEntry(TrackSwitch trackSwitch,
			GenericDatabaseInterface databaseInterface) {
		RepositoryEntry<TrackSwitch> entry = null;
		TrackSwitchRepository trackSwitchRepository = new TrackSwitchRepository(databaseInterface);

		// Find TrackSwitch
		TrackSwitchSearchCriteria trackSwitchSearchCriteria = new TrackSwitchSearchCriteria();
		trackSwitchSearchCriteria.setSwitchName(trackSwitch.getSwitchName());
		List<RepositoryEntry<TrackSwitch>> results = trackSwitchRepository.find(trackSwitchSearchCriteria);

		if (results.isEmpty()) {
			// Add entry
			entry = trackSwitchRepository.add(trackSwitch);
		} else {
			// Update Entry
			// Grab only first entry
			RepositoryEntry<TrackSwitch> selectedEntry = results.get(0);

			trackSwitchRepository.update(selectedEntry.getId(), trackSwitch);

			entry = new RepositoryEntry<TrackSwitch>(selectedEntry.getId(), trackSwitch);
		}

		return entry;
	}

	private static RepositoryEntry<TrackPoint> updateOrAddEntry(TrackPoint trackPoint,
			GenericDatabaseInterface databaseInterface) {
		RepositoryEntry<TrackPoint> entry = null;
		TrackPointRepository trackPointRepository = new TrackPointRepository(databaseInterface);

		// Find TrackPoint
		TrackPointSearchCriteria trackPointSearchCriteria = new TrackPointSearchCriteria();
		trackPointSearchCriteria.setType(trackPoint.getType());
		trackPointSearchCriteria.setName(trackPoint.getPointName());
		List<RepositoryEntry<TrackPoint>> results = trackPointRepository.find(trackPointSearchCriteria);

		if (results.isEmpty()) {
			// Add entry
			entry = trackPointRepository.add(trackPoint);
		} else {
			// Update Entry
			// Grab only first entry
			RepositoryEntry<TrackPoint> selectedEntry = results.get(0);

			trackPointRepository.update(selectedEntry.getId(), trackPoint);

			entry = new RepositoryEntry<TrackPoint>(selectedEntry.getId(), trackPoint);
		}

		return entry;
	}

	private static void importTrackMeasurements(String filename, GenericDatabaseInterface databaseInterface) {
		List<TrackPointMeasurement> measurements = TrackMeasurementsReader.ReadFile(filename);

		// Save Measurements
		for (TrackPointMeasurement measurement : measurements) {
			saveMeasurement(measurement, databaseInterface);
		}

		// Save Adjacency List
		for (TrackPointMeasurement measurement : measurements) {
			saveAdjacencyList(measurement, databaseInterface);
		}
	}

	private static RepositoryEntry<TrackBlock> getOrAddTrackBlock(String blockName,
			GenericDatabaseInterface databaseInterface) {
		TrackBlockRepository trackBlockRepository = new TrackBlockRepository(databaseInterface);
		RepositoryEntry<TrackBlock> trackBlockEntry = null;

		if (!blockName.isEmpty()) {
			// Search for an existing entry

			TrackBlockSearchCriteria searchCriteria = new TrackBlockSearchCriteria();
			searchCriteria.setBlockName(blockName);
			List<RepositoryEntry<TrackBlock>> matches = trackBlockRepository.find(searchCriteria);

			// Assuming that there is only one match
			if (matches.size() > 0) {
				trackBlockEntry = matches.get(0);
			} else {
				// Create new entry
				TrackBlock trackBlock = new TrackBlock(blockName);

				trackBlockEntry = trackBlockRepository.add(trackBlock);
			}
		}

		return trackBlockEntry;
	}

	private static void saveMeasurement(TrackPointMeasurement measurement, GenericDatabaseInterface databaseInterface) {
		TrackPointRepository trackPointRepository = new TrackPointRepository(databaseInterface);
		AdjacentPointRepository adjacentPointRepository = new AdjacentPointRepository(databaseInterface);

		// Add track block
		String blockId = "";
		String blockName = measurement.getBlockName().trim();

		RepositoryEntry<TrackBlock> trackBlockEntry = getOrAddTrackBlock(blockName, databaseInterface);

		if (trackBlockEntry != null) {
			blockId = trackBlockEntry.getId();
		}

		// Add point
		TrackPoint trackPoint = new TrackPoint(measurement.getPointName(), measurement.getPointType(),
				measurement.getxInches(), measurement.getyInches(), 0, blockId, "");

		RepositoryEntry<TrackPoint> addedEntry = trackPointRepository.add(trackPoint);

	}

	private static String getPointId(String pointName,
			FilteredSearchRepositoryInterface<TrackPoint, TrackPointSearchCriteria> trackPointRepository) {

		String pointId = null;

		if (!pointName.isEmpty()) {
			TrackPointSearchCriteria searchCriteria = new TrackPointSearchCriteria();
			searchCriteria.setName(pointName);
			List<RepositoryEntry<TrackPoint>> matches = trackPointRepository.find(searchCriteria);

			// Assuming that there is only one match
			if (matches.size() > 0) {
				RepositoryEntry<TrackPoint> trackPointEntry = matches.get(0);

				pointId = trackPointEntry.getId();
			}
		}

		return pointId;
	}

	private static void saveAdjacencyList(TrackPointMeasurement measurement,
			GenericDatabaseInterface databaseInterface) {
		TrackPointRepository trackPointRepository = new TrackPointRepository(databaseInterface);
		AdjacentPointRepository adjacentPointRepository = new AdjacentPointRepository(databaseInterface);

		// Find Point
		String pointId = getPointId(measurement.getPointName().trim(), trackPointRepository);

		if (pointId != null) {

			// Add adjacent points
			for (TrackPointMeasurement adjacentMeasurement : measurement.getAdjacentPoints()) {

				String adjacentPointId = getPointId(adjacentMeasurement.getPointName().trim(), trackPointRepository);

				if (adjacentPointId != null) {
					AdjacentPoint adjacentPoint = new AdjacentPoint(Integer.parseInt(pointId),
							Integer.parseInt(adjacentPointId));

					adjacentPointRepository.add(adjacentPoint);
				}
			}
		}

	}

}
