package com.traintrax.navigation.service.testdriver;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import com.traintrax.navigation.service.mdu.GyroscopeMeasurement;
import com.traintrax.navigation.service.position.Acceleration;
import com.traintrax.navigation.service.Tuple;
import com.traintrax.navigation.service.ValueUpdate;
import com.traintrax.navigation.service.mdu.AccelerometerMeasurement;

/**
 * Class is responsible for importing track measurements from a CSV file.
 * 
 * @author Corey Sanders
 * 
 */
public class ImuMeasurementsReader {

	public static List<ValueUpdate<Tuple<GyroscopeMeasurement, AccelerometerMeasurement>>> ReadFile(String filename) {
		List<ValueUpdate<Tuple<GyroscopeMeasurement, AccelerometerMeasurement>>> measurements = new LinkedList<ValueUpdate<Tuple<GyroscopeMeasurement, AccelerometerMeasurement>>>();
		try {

			final int numberOfColumns = 7;
			final int timeElapsedColumnIndex = 0;
			final int gyroscopeXColumnIndex = 1;
			final int gyroscopeYColumnIndex = 2;
			final int gyroscopeZColumnIndex = 3;
			final int accelerometerXColumnIndex = 4;
			final int accelerometerYColumnIndex = 5;
			final int accelerometerZColumnIndex = 6;

			FileReader fr = new FileReader(filename);
			BufferedReader reader = new BufferedReader(fr);
			GyroscopeMeasurement lastGyroscopeMeasurement = null;
			AccelerometerMeasurement lastAccelerometerMeasurement = null;
			
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

							double gyroscopeX = Double.parseDouble(segments[gyroscopeXColumnIndex]);
							double gyroscopeY = Double
									.parseDouble(segments[gyroscopeYColumnIndex]);
							
							double gyroscopeZ = Double
									.parseDouble(segments[gyroscopeZColumnIndex]);
							
							double accelerometerX = Double.parseDouble(segments[accelerometerXColumnIndex]);
							double accelerometerY = Double
									.parseDouble(segments[accelerometerYColumnIndex]);
							
							double accelerometerZ = Double
									.parseDouble(segments[accelerometerZColumnIndex]);

							double secondsElapsed = Double
									.parseDouble(segments[timeElapsedColumnIndex]);
							

							//Assigning to epoch since we don't really are about the actual date.
							Calendar gyroscopeTime = Calendar.getInstance();
							gyroscopeTime.setTimeInMillis((long) (secondsElapsed*1000));
							
							double deltaGyroscopeTimeInSeconds = 0;
							if(lastGyroscopeMeasurement != null)
							{
								deltaGyroscopeTimeInSeconds = (gyroscopeTime.getTimeInMillis()-lastGyroscopeMeasurement.getTimeMeasured().getTimeInMillis()) / 1000.0; 
							}
							
							GyroscopeMeasurement gyroscopeMeasurement = new GyroscopeMeasurement(gyroscopeX,
									gyroscopeY, gyroscopeZ, deltaGyroscopeTimeInSeconds, gyroscopeTime);

							lastGyroscopeMeasurement = gyroscopeMeasurement;
							
							
							//Assigning to epoch since we don't really are about the actual date.
							Calendar accelerometerTime = Calendar.getInstance();
							accelerometerTime.setTimeInMillis((long) (secondsElapsed*1000));
							
							double deltaAccelerometerTimeInSeconds = 0;
							if(lastAccelerometerMeasurement != null)
							{
								deltaAccelerometerTimeInSeconds = (accelerometerTime.getTimeInMillis() - lastAccelerometerMeasurement.getTimeMeasured().getTimeInMillis()) / 1000.0; 
							}
							
							AccelerometerMeasurement accelerometerMeasurement = new AccelerometerMeasurement(new Acceleration(accelerometerX, accelerometerY, accelerometerZ), deltaAccelerometerTimeInSeconds, accelerometerTime);

							lastAccelerometerMeasurement = accelerometerMeasurement;
							
							Tuple<GyroscopeMeasurement, AccelerometerMeasurement> imuTuple = new Tuple<GyroscopeMeasurement, AccelerometerMeasurement>(gyroscopeMeasurement, accelerometerMeasurement);
							ValueUpdate<Tuple<GyroscopeMeasurement, AccelerometerMeasurement>> imuReading = new ValueUpdate<Tuple<GyroscopeMeasurement, AccelerometerMeasurement>>(imuTuple, accelerometerTime);							
							
							measurements.add(imuReading);
							
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
