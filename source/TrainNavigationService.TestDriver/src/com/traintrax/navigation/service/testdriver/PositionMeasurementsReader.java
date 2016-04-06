package com.traintrax.navigation.service.testdriver;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import com.traintrax.navigation.service.ValueUpdate;
import com.traintrax.navigation.service.position.Coordinate;

/**
 * Class is responsible for importing track measurements from a CSV file.
 * 
 * @author Corey Sanders
 * 
 */
public class PositionMeasurementsReader {

	public static List<ValueUpdate<Coordinate>> ReadFile(String filename) {
		List<ValueUpdate<Coordinate>> measurements = new LinkedList<ValueUpdate<Coordinate>>();
		try {

			final int numberOfColumns = 4;
			final int timeElapsedColumnIndex = 0;
			final int positionXColumnIndex = 1;
			final int positionYColumnIndex = 2;
			final int positionZColumnIndex = 3;

			FileReader fr = new FileReader(filename);
			BufferedReader reader = new BufferedReader(fr);
			
			try {
				String currentRow = "";
				
				//Discard first row.
				try {
					currentRow = reader.readLine();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					currentRow = "";
				}

				// Read in Measurements
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

							double positionX = Double.parseDouble(segments[positionXColumnIndex]);
							double positionY = Double
									.parseDouble(segments[positionYColumnIndex]);
							
							double positionZ = Double
									.parseDouble(segments[positionZColumnIndex]);
							
							double secondsElapsed = Double
									.parseDouble(segments[timeElapsedColumnIndex]);
							

							//Assigning to epoch since we don't really are about the actual date.
							Calendar positionTime = Calendar.getInstance();
							positionTime.setTimeInMillis((long) (secondsElapsed*1000));

							//Assuming that the positions were originally in inches.
							//converting into meters
							double inchesToMeters = (2.54/100);
							
							Coordinate position = new Coordinate(positionX*inchesToMeters, positionY*inchesToMeters, positionZ*inchesToMeters);
							ValueUpdate<Coordinate> positionReading = new ValueUpdate<Coordinate>(position, positionTime);							
							
							measurements.add(positionReading);
							
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
