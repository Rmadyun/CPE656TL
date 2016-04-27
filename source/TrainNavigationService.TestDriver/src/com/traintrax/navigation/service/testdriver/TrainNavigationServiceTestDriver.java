package com.traintrax.navigation.service.testdriver;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import com.traintrax.navigation.service.ValueUpdate;
import com.traintrax.navigation.service.math.Tuple;
import com.traintrax.navigation.service.position.AccelerometerMeasurement;
import com.traintrax.navigation.service.position.Coordinate;
import com.traintrax.navigation.service.position.GyroscopeMeasurement;
import com.traintrax.navigation.service.position.InertialMotionPositionAlgorithmInterface;
import com.traintrax.navigation.service.position.TrainPosition2DAlgorithm;
import com.traintrax.navigation.service.position.UnitConversionUtilities;
import com.traintrax.navigation.service.position.Velocity;
import com.traintrax.navigation.service.testing.PositionTestCase;
import com.traintrax.navigation.service.testing.PositionTestCaseFileReader;
import com.traintrax.navigation.service.testing.PositionTestSample;

import gnu.io.CommPortIdentifier;

/**
 * Class responsible for invoking tests that require human interaction
 * @author Death
 *
 */
public class TrainNavigationServiceTestDriver {
	
	
	/**
	 * Entry point for the Test Driver Program
	 * @param args Command line arguments
	 */
	public static void main(String[] args) {

		MainMenu();

		try {
			System.in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void MainMenu(){
		System.out.println("Main Menu");
		System.out.println("1. Controls Switches");
		System.out.println("2. Verify Shape of Train Path");
		
	}
	
	
	private static void VerifyShape(String filename){
		
		PositionTestCase testCase = PositionTestCaseFileReader.Read(filename);
		
		OutputTrainPath(testCase, "C:\\TrainTrax\\CPE656TL-master\\test\\position.csv");
		
	}

	/**
	 * Calculates the position of the train writes the output to a CSV file
	 */
	private static void OutputTrainPath(PositionTestCase positionTestCase, String positionFile) {
		double tolerance = 0.05;
		List<ValueUpdate<Tuple<GyroscopeMeasurement, AccelerometerMeasurement>>> imuReadings;
		List<ValueUpdate<Coordinate>> positionReadings;
		List<ValueUpdate<Coordinate>> finalPositions = new LinkedList<ValueUpdate<Coordinate>>();
		
		FileWriter fileWriter = null;
		BufferedWriter bw = null;
		try {
			fileWriter = new FileWriter(positionFile);
			bw = new BufferedWriter(fileWriter);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		 

		// Calculate Position
		// NOTE: Ball Parked an initial position based on the 02-17-16 data.
		InertialMotionPositionAlgorithmInterface positionAlgorithm = new TrainPosition2DAlgorithm(
				positionTestCase.getInitialPosition(), positionTestCase.getInitialOrientation(), positionTestCase.getInitialVelocity());

		for (PositionTestSample sample : positionTestCase.getSamples()) {

			GyroscopeMeasurement gyroscopeMeasurement = sample.getGyroscopeMeasurement();
			AccelerometerMeasurement accelerometerMeasurement = sample.getAccelerometerMeasurement();
			ValueUpdate<Coordinate> positionUpdate = sample.getRfidTagPosition();
			ValueUpdate<Coordinate> expectedPositionUpdate = sample.getExpectedPosition();

			List<GyroscopeMeasurement> gyroscopeMeasurements = new LinkedList<GyroscopeMeasurement>();

			if (gyroscopeMeasurement != null) {
				gyroscopeMeasurements.add(gyroscopeMeasurement);
			}

			List<AccelerometerMeasurement> accelerometerMeasurements = new LinkedList<AccelerometerMeasurement>();

			if (accelerometerMeasurement != null) {
				accelerometerMeasurements.add(accelerometerMeasurement);
			}

			List<ValueUpdate<Coordinate>> positionUpdates = new LinkedList<ValueUpdate<Coordinate>>();

			if (positionUpdate != null) {
				positionUpdates.add(positionUpdate);
			}

			ValueUpdate<Tuple<Coordinate, Velocity>> actualPositionUpdate = positionAlgorithm
					.calculatePosition(gyroscopeMeasurements, accelerometerMeasurements, positionUpdates);

			try {

				// Write position to CSV

				Coordinate positionInInches = UnitConversionUtilities
						.convertFromMetersToInches(actualPositionUpdate.getValue().getItem1());
				String row = String.format("%f, %f, %f, %f\n",
						actualPositionUpdate.getTimeObserved().getTimeInMillis() / 1000.0, positionInInches.getX(),
						positionInInches.getY(), positionInInches.getZ());
				bw.write(row);

				bw.flush();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} // end for

		try {
			bw.close();
			fileWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
