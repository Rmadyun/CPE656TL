import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.traintrax.navigation.database.library.AdjacentPoint;
import com.traintrax.navigation.database.library.AdjacentPointRepository;
import com.traintrax.navigation.database.library.FilteredSearchRepositoryInterface;
import com.traintrax.navigation.database.library.GenericDatabaseInterface;
import com.traintrax.navigation.database.library.MySqlDatabaseAdapter;
import com.traintrax.navigation.database.library.RepositoryEntry;
import com.traintrax.navigation.database.library.TrackBlock;
import com.traintrax.navigation.database.library.TrackBlockRepository;
import com.traintrax.navigation.database.library.TrackPoint;
import com.traintrax.navigation.database.library.TrackPointRepository;
import com.traintrax.navigation.database.library.TrackPointSearchCriteria;
import com.traintrax.navigation.database.library.TrackSwitch;
import com.traintrax.navigation.database.library.TrackSwitchRepository;

/**
 * Class is responsible for handling the importing of 
 * CSV data into the Train Navigation Database
 * @author Corey Sanders
 *
 */
public class DatabaseImportProgram {
	
	/**
	 * Entry point of the Database endpoint program
	 * @param args command line arguments passed into the program.
	 */
	public static void main(String[] args) {

		// Setup expected command line arguments
		Options commandlineOptions = new Options();

		// Options
		Option trackSwitchOption = Option.builder("s").required(true).argName("track switch csv file").hasArg().longOpt("switch-data")
				.build();
		Option trackPointOption = Option.builder("p").required(true).argName("track point csv file").hasArg().longOpt("point-data")
				.build();

		Option helpOption = Option.builder("h").required(false).longOpt("help").build();

		commandlineOptions.addOption(trackSwitchOption);
		commandlineOptions.addOption(trackPointOption);

		commandlineOptions.addOption(helpOption);

		DefaultParser defaultParser = new DefaultParser();
		Boolean showHelp = false;
		String trackSwitchCsv = "";
		String trackPointCsv = ""; 

		try {
			CommandLine parsedCommandLine = defaultParser.parse(commandlineOptions, args);

			if (parsedCommandLine.hasOption(helpOption.getOpt())) {
				showHelp = true;
			}

			trackSwitchCsv = parsedCommandLine.getOptionValue(trackSwitchOption.getOpt());
			trackPointCsv = parsedCommandLine.getOptionValue(trackPointOption.getOpt());

		} catch (MissingOptionException exception) {
			System.out.println(exception.getMessage());
			showHelp = true;
		}
		catch (ParseException exception) {
			System.out.println(exception.getMessage());
			showHelp = true;
		}

		if (showHelp) {
			// automatically generate the help statement
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("TrainNavigationService.RestService", commandlineOptions);
			return;
		}
		
		MySqlDatabaseAdapter mySqlDatabaseAdapter = new MySqlDatabaseAdapter();

		mySqlDatabaseAdapter.connect();

		importTrackMeasurements(trackPointCsv, mySqlDatabaseAdapter);
		importTrackSwitchMeasurements(trackSwitchCsv, mySqlDatabaseAdapter);		
		
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
		
		TrackBlockRepository trackBlockRepository = new TrackBlockRepository(databaseInterface);
		TrackPointRepository trackPointRepository = new TrackPointRepository(databaseInterface);
		TrackSwitchRepository trackSwitchRepository = new TrackSwitchRepository(databaseInterface);
		
		// Add track block
		String blockId = "";
		String passBlockId = "";
		String bypassBlockId = "";
		String blockName = measurement.getBlockName().trim();
		String passBlockName = measurement.getPassBlockName().trim();
		String bypassBlockName = measurement.getBypassBlockName().trim();

		RepositoryEntry<TrackBlock> trackBlockEntry = DataImportUtilities.updateOrAddEntry(new TrackBlock(blockName), trackBlockRepository);
		
		if (trackBlockEntry != null) {
			blockId = trackBlockEntry.getId();
		}

		RepositoryEntry<TrackBlock> passTrackBlockEntry = DataImportUtilities.updateOrAddEntry(new TrackBlock(passBlockName), trackBlockRepository);

		if (passTrackBlockEntry != null) {
			passBlockId = passTrackBlockEntry.getId();
		}
		
		RepositoryEntry<TrackBlock> bypassTrackBlockEntry = DataImportUtilities.updateOrAddEntry(new TrackBlock(bypassBlockName), trackBlockRepository);

		if (bypassTrackBlockEntry != null) {
			bypassBlockId = bypassTrackBlockEntry.getId();
		}

		// New Track Point
		TrackPoint updatedTrackPoint = new TrackPoint(measurement.getSwitchName(), "Switch", measurement.getxInches(),
				measurement.getyInches(), 0, blockId, "");

		RepositoryEntry<TrackPoint> trackPointEntry = DataImportUtilities.updateOrAddEntry(updatedTrackPoint, trackPointRepository);

		// New Track Switch
		TrackSwitch updatedTrackSwitch = new TrackSwitch(measurement.getSwitchName(), trackPointEntry.getId(),
				passBlockId, bypassBlockId);

		DataImportUtilities.updateOrAddEntry(updatedTrackSwitch, trackSwitchRepository);

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

	private static void saveMeasurement(TrackPointMeasurement measurement, GenericDatabaseInterface databaseInterface) {
		TrackBlockRepository trackBlockRepository = new TrackBlockRepository(databaseInterface);
		TrackPointRepository trackPointRepository = new TrackPointRepository(databaseInterface);
		
		// Add track block
		String blockId = "";
		String blockName = measurement.getBlockName().trim();

		RepositoryEntry<TrackBlock> trackBlockEntry = DataImportUtilities.updateOrAddEntry(new TrackBlock(blockName), trackBlockRepository);

		if (trackBlockEntry != null) {
			blockId = trackBlockEntry.getId();
		}

		// Add point
		TrackPoint trackPoint = new TrackPoint(measurement.getPointName(), measurement.getPointType(),
				measurement.getxInches(), measurement.getyInches(), 0, blockId, measurement.getRfidTagId());

		DataImportUtilities.updateOrAddEntry(trackPoint,  trackPointRepository);

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

					DataImportUtilities.updateOrAddEntry(adjacentPoint, adjacentPointRepository);
				}
			}
		}

	}

}
