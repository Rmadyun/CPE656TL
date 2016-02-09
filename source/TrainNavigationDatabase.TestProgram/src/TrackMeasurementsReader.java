import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class is responsible for importing track measurements from a CSV file.
 * 
 * @author Corey Sanders
 * 
 */
public class TrackMeasurementsReader {

	public static List<TrackPointMeasurement> ReadFile(String filename) {
		List<TrackPointMeasurement> measurements = new ArrayList<TrackPointMeasurement>();
		try {

			final int numberOfColumns = 5; // Setting to 5 instead of 6 because we don't have tag data.
			final int pointNameColumnIndex = 0;
			final int xInchesColumnIndex = 1;
			final int yInchesColumnIndex = 2;
			final int blockNameColumnIndex = 3;
			final int adjacentPointNamesColumnIndex = 4;
			final int rfidTagIdColumnIndex = 5;
			List<Entry> trackMeasurementEntries = new ArrayList<Entry>();

			FileReader fr = new FileReader(filename);
			BufferedReader reader = new BufferedReader(fr);
			try {
				String currentRow = "";

				// Stage 1: Read in Measurements
				do {
					try {
						currentRow = reader.readLine();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						currentRow = "";
					}

					if (currentRow != null && !currentRow.isEmpty()) {
						String[] segments;
						segments = currentRow.split(",");

						if (segments.length >= numberOfColumns) {

							String pointName = segments[pointNameColumnIndex]
									.trim();
							double xInches = Double
									.parseDouble(segments[xInchesColumnIndex]);
							double yInches = Double
									.parseDouble(segments[yInchesColumnIndex]);
							String blockName = segments[blockNameColumnIndex]
									.trim();
							String adjacentPointNames = segments[adjacentPointNamesColumnIndex]
									.trim();
							String rfidTagId = ""; /*segments[rfidTagIdColumnIndex]
									.trim();*/

							TrackPointMeasurement measurement = new TrackPointMeasurement();
							measurement.setPointName(pointName);
							measurement.setxInches(xInches);
							measurement.setyInches(yInches);
							measurement.setBlockName(blockName);
                            measurement.setPointType("Point");
							measurement.setRfidTagId(rfidTagId);

							Entry entry = new Entry();
							entry.Measurement = measurement;
							entry.AdjacentPointNames = adjacentPointNames;

							trackMeasurementEntries.add(entry);
						}
					}
				} while (currentRow != null && !currentRow.isEmpty());

				// Stage 2: Associate related/adjacent measurements
				for (Entry entry : trackMeasurementEntries) {
					List<TrackPointMeasurement> adjacentMeasurements = getAdjacentMeasurements(
							entry.AdjacentPointNames, trackMeasurementEntries);
					TrackPointMeasurement measurement = entry.Measurement;
					measurement.setAdjacentPoints(adjacentMeasurements);

					measurements.add(measurement);
				}
			} finally {
				try {
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					fr.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return measurements;
	}

	private static List<TrackPointMeasurement> getAdjacentMeasurements(
			String adjacentPointNames, List<Entry> trackMeasurementEntries) {
		List<TrackPointMeasurement> adjacentMeasurements = new ArrayList<TrackPointMeasurement>();
		String[] segments;
		segments = adjacentPointNames.split(";");

		for (String pointName : segments) {
			String trimmedName = pointName.trim();

			if (!trimmedName.isEmpty()) {
				TrackPointMeasurement trackPointMeasurement = findMeasurementByName(
						trimmedName, trackMeasurementEntries);

				if (trackPointMeasurement != null) {
					adjacentMeasurements.add(trackPointMeasurement);
				}

			}
		}

		return adjacentMeasurements;
	}

	private static TrackPointMeasurement findMeasurementByName(
			String pointName, List<Entry> trackMeasurementEntries) {
		TrackPointMeasurement trackPointMeasurement = null;

		for (Entry entry : trackMeasurementEntries) {
			if (entry.Measurement.getPointName().equals(pointName)) {
				trackPointMeasurement = entry.Measurement;
				break;
			}
		}

		return trackPointMeasurement;
	}

}
