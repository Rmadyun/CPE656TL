package com.traintrax.navigation.service.testdriver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import com.traintrax.navigation.service.TrackSwitchController;
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
import com.traintrax.navigation.trackswitch.SwitchState;

/**
 * Class responsible for invoking tests that require human interaction
 * 
 * @author Death
 *
 */
public class TrainNavigationServiceTestDriver {

	/**
	 * Entry point for the Test Driver Program
	 * 
	 * @param args
	 *            Command line arguments
	 */
	public static void main(String[] args) {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		MainMenu(br);

		try {
			br.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Displays and control the main menu
	 * 
	 * @param systemConsoleReader
	 *            reader for console input.
	 */
	private static void MainMenu(BufferedReader systemConsoleReader) {

		int choice = -1;

		if (systemConsoleReader != null) {
			do {

				System.out.println("Main Menu");
				System.out.println("1. Controls Switches");
				System.out.println("2. Verify Shape of Train Path");

				String inputString = "";
				try {
					inputString = systemConsoleReader.readLine();
				} catch (IOException e) {
					choice = -1; // Default to the invalid choice selection

				}
				choice = Integer.parseInt(inputString);
			} while ((choice < 1) || (choice > 2)); // input check

			if (choice == 1) {
				ControlSwitchMenu(systemConsoleReader);
			} else if (choice == 2) {
				VerifyShapeMenu(systemConsoleReader);
			}
			
			System.out.println("Existing Test Driver...");
		}
	}

	/**
	 * Displays and control verify shape menu
	 * 
	 * @param systemConsoleReader
	 *            reader for console input.
	 */
	private static void VerifyShapeMenu(BufferedReader systemConsoleReader) {
		System.out.print("Please enter the test case file you want to use: ");
		String filename = "";
		try {
			filename = systemConsoleReader.readLine();
		} catch (IOException e) {

			System.out.println("Unable to listen for input: " + e.getMessage());
		}

		if (filename.isEmpty()) {
			System.out.println("File name is empty cannot verify shape.");
		} else {
			VerifyShape(filename);
		}

	}

	/**
	 * Displays and controls the control switch menu
	 * 
	 * @param systemConsoleReader
	 */
	private static void ControlSwitchMenu(BufferedReader systemConsoleReader) {
		System.out.print("Please the COM Port for the PR3 LocoNet Programming Interface: ");
		String pr3Port = "";
		String switchNumber = "";

		try {
			pr3Port = systemConsoleReader.readLine();
		} catch (IOException e) {

			System.out.println("Unable to listen for input: " + e.getMessage());
		}

		if (pr3Port.isEmpty()) {
			System.out.println("COM Port entered is empty cannot verify switch control.");
			return;
		}

		System.out.print("Please the Switch Address that you want to control: ");

		try {
			switchNumber = systemConsoleReader.readLine();
		} catch (IOException e) {

			System.out.println("Unable to listen for input: " + e.getMessage());
		}

		if (switchNumber.isEmpty()) {
			System.out.println("COM Port entered is empty cannot verify switch control.");
			return;
		}

		ControlSwitch(systemConsoleReader, pr3Port, switchNumber);
	}

	private static void ControlSwitch(BufferedReader systemConsoleReader, String pr3Port, String switchNumber) {
		TrackSwitchController trackSwitchController = null;

		try {
			trackSwitchController = new TrackSwitchController(pr3Port, TrackSwitchController.DefaultPrefix, null);
		} catch (Exception exception) {
			System.out.println("Unable to create track switch controller for " + pr3Port);
		}

		trackSwitchController.ChangeSwitchState(switchNumber, SwitchState.Pass);
		System.out.println("Changed Switch "+switchNumber+" into Pass state");
		System.out.println("Press ENTER to continue");
		try {
			systemConsoleReader.readLine();
		} catch (IOException e) {

			System.out.println("Unable to listen for input: " + e.getMessage());
		}

		trackSwitchController.ChangeSwitchState(switchNumber, SwitchState.ByPass);
		System.out.println("Changed Switch "+switchNumber+" into Bypass state");
		
		trackSwitchController.dispose();
	}

	private static void VerifyShape(String filename) {

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
				positionTestCase.getInitialPosition(), positionTestCase.getInitialOrientation(),
				positionTestCase.getInitialVelocity());

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

				System.out.println("Path of train movement has been processed and output to:" + positionFile);

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
