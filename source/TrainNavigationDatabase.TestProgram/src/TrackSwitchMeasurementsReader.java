import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class is responsible for importing track switch measurements from a CSV file.
 * 
 * @author Corey Sanders
 * 
 */
public class TrackSwitchMeasurementsReader {

	public static List<TrackSwitchMeasurement> ReadFile(String filename) {
		List<TrackSwitchMeasurement> measurements = new ArrayList<TrackSwitchMeasurement>();
		try {

			final int numberOfColumns = 6; 
			final int switchNameColumnIndex = 0;
			final int xInchesColumnIndex = 1;
			final int yInchesColumnIndex = 2;
			final int blockNameColumnIndex = 3;
			final int passBlockColumnIndex = 4;
			final int bypassBlockColumnIndex = 5;

			FileReader fr = new FileReader(filename);
			BufferedReader reader = new BufferedReader(fr);
			try {
				String currentRow = "";
				
				//Discard first row
				try {
					currentRow = reader.readLine();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					currentRow = "";
				}

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

							String switchName = segments[switchNameColumnIndex]
									.trim();
							double xInches = Double
									.parseDouble(segments[xInchesColumnIndex]);
							double yInches = Double
									.parseDouble(segments[yInchesColumnIndex]);
							String blockName = segments[blockNameColumnIndex]
									.trim();
							String passBlockName = segments[passBlockColumnIndex]
									.trim();
							String bypassBlockName = segments[bypassBlockColumnIndex]
									.trim();

							TrackSwitchMeasurement measurement = new TrackSwitchMeasurement();
							measurement.setSwitchName(switchName);
							measurement.setxInches(xInches);
							measurement.setyInches(yInches);
							measurement.setBlockName(blockName);
                            measurement.setPassBlockName(passBlockName);
							measurement.setBypassBlockName(bypassBlockName);

							measurements.add(measurement);
						}
					}
				} while (currentRow != null && !currentRow.isEmpty());
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

}
