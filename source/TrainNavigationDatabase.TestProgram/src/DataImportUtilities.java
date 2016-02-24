import java.util.List;

import TrainNavigationDatabase.AdjacentPoint;
import TrainNavigationDatabase.AdjacentPointSearchCriteria;
import TrainNavigationDatabase.FilteredSearchRepositoryInterface;
import TrainNavigationDatabase.RepositoryEntry;
import TrainNavigationDatabase.TrackBlock;
import TrainNavigationDatabase.TrackBlockSearchCriteria;
import TrainNavigationDatabase.TrackPoint;
import TrainNavigationDatabase.TrackPointSearchCriteria;
import TrainNavigationDatabase.TrackSwitch;
import TrainNavigationDatabase.TrackSwitchSearchCriteria;

/**
 * Utility class for frequently used methods for
 * importing information into a Train Navigation Database
 * repository
 * @author Corey Sanders
 *
 */
public class DataImportUtilities {
	
	/**
	 * Updates an existing entry in a repository. If an entry does not exist, then
	 * a new entry is added to the repository
	 * @param trackSwitch Item to add or update
	 * @param trackSwitchRepository Location to store latest version of the entry
	 * @return Repository entry for the item that was added or updated.
	 */
	public static RepositoryEntry<TrackSwitch> updateOrAddEntry(TrackSwitch trackSwitch,
			FilteredSearchRepositoryInterface<TrackSwitch, TrackSwitchSearchCriteria> trackSwitchRepository) {
		RepositoryEntry<TrackSwitch> entry = null;

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
	

	/**
	 * Updates an existing entry in a repository. If an entry does not exist, then
	 * a new entry is added to the repository
	 * @param trackPoint Item to add or update
	 * @param trackPointRepository Location to store latest version of the entry
	 * @return Repository entry for the item that was added or updated.
	 */
	public static RepositoryEntry<TrackPoint> updateOrAddEntry(TrackPoint trackPoint,
			FilteredSearchRepositoryInterface<TrackPoint, TrackPointSearchCriteria> trackPointRepository) {
		RepositoryEntry<TrackPoint> entry = null;

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
	
	/**
	 * Updates an existing entry in a repository. If an entry does not exist, then
	 * a new entry is added to the repository
	 * @param adjacentPoint Item to add or update
	 * @param adjacentPointRepository Location to store latest version of the entry
	 * @return Repository entry for the item that was added or updated.
	 */
	public static RepositoryEntry<AdjacentPoint> updateOrAddEntry(AdjacentPoint adjacentPoint,
			FilteredSearchRepositoryInterface<AdjacentPoint, AdjacentPointSearchCriteria> adjacentPointRepository) {
		RepositoryEntry<AdjacentPoint> entry = null;

		// Find AdjacentPoint
		AdjacentPointSearchCriteria adjacentPointSearchCriteria = new AdjacentPointSearchCriteria();
		adjacentPointSearchCriteria.setPointId(Integer.toString(adjacentPoint.getPointId()));
		adjacentPointSearchCriteria.setAdjacentPointId(Integer.toString(adjacentPoint.getAdjacenPointId()));
		List<RepositoryEntry<AdjacentPoint>> results = adjacentPointRepository.find(adjacentPointSearchCriteria);

		if (results.isEmpty()) {
			// Add entry
			entry = adjacentPointRepository.add(adjacentPoint);
		} else {
			// Update Entry
			// Grab only first entry
			RepositoryEntry<AdjacentPoint> selectedEntry = results.get(0);

			adjacentPointRepository.update(selectedEntry.getId(), adjacentPoint);

			entry = new RepositoryEntry<AdjacentPoint>(selectedEntry.getId(), adjacentPoint);
		}

		return entry;
	}
	
	/**
	 * Updates an existing entry in a repository. If an entry does not exist, then
	 * a new entry is added to the repository
	 * @param trackBlock Item to add or update
	 * @param trackBlockRepository Location to store latest version of the entry
	 * @return Repository entry for the item that was added or updated.
	 */
	public static RepositoryEntry<TrackBlock> updateOrAddEntry(TrackBlock trackBlock,
			FilteredSearchRepositoryInterface<TrackBlock, TrackBlockSearchCriteria> trackBlockRepository) {
		RepositoryEntry<TrackBlock> entry = null;

		// Find TrackPoint
		TrackBlockSearchCriteria trackBlockSearchCriteria = new TrackBlockSearchCriteria();
		trackBlockSearchCriteria.setBlockName(trackBlock.getBlockName());
		List<RepositoryEntry<TrackBlock>> results = trackBlockRepository.find(trackBlockSearchCriteria);

		if (results.isEmpty()) {
			// Add entry
			entry = trackBlockRepository.add(trackBlock);
		} else {
			// Update Entry
			// Grab only first entry
			RepositoryEntry<TrackBlock> selectedEntry = results.get(0);

			trackBlockRepository.update(selectedEntry.getId(), trackBlock);

			entry = new RepositoryEntry<TrackBlock>(selectedEntry.getId(), trackBlock);
		}

		return entry;
	}

}
