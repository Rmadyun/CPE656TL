package com.traintrax.navigation.service.testdriver;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import com.traintrax.navigation.database.library.AccelerometerMeasurementRepository;
import com.traintrax.navigation.database.library.AccelerometerMeasurementSearchCriteria;
import com.traintrax.navigation.database.library.FilteredSearchRepositoryInterface;
import com.traintrax.navigation.database.library.GenericDatabaseInterface;
import com.traintrax.navigation.database.library.GyroscopeMeasurementRepository;
import com.traintrax.navigation.database.library.GyroscopeMeasurementSearchCriteria;
import com.traintrax.navigation.database.library.MySqlDatabaseAdapter;
import com.traintrax.navigation.database.library.RfidTagDetectedNotificationRepository;
import com.traintrax.navigation.database.library.RfidTagDetectedNotificationSearchCriteria;
import com.traintrax.navigation.database.library.TrackPoint;
import com.traintrax.navigation.database.library.TrackPointRepository;
import com.traintrax.navigation.database.library.TrackPointSearchCriteria;
import com.traintrax.navigation.database.library.TrackSwitch;
import com.traintrax.navigation.database.library.TrackSwitchRepository;
import com.traintrax.navigation.database.library.TrackSwitchSearchCriteria;
import com.traintrax.navigation.database.library.TrainPosition;
import com.traintrax.navigation.database.library.TrainPositionRepository;
import com.traintrax.navigation.database.library.TrainPositionSearchCriteria;
import com.traintrax.navigation.service.TestTrackSwitchController;
import com.traintrax.navigation.service.TrackSwitchControllerInterface;
import com.traintrax.navigation.service.TrainMonitor;
import com.traintrax.navigation.service.TrainMonitorInterface;
import com.traintrax.navigation.service.TrainNavigationDatabase;
import com.traintrax.navigation.service.TrainNavigationDatabaseInterface;
import com.traintrax.navigation.service.TrainNavigationService;
import com.traintrax.navigation.service.TrainNavigationServiceEvent;
import com.traintrax.navigation.service.TrainNavigationServiceEventSubscriber;
import com.traintrax.navigation.service.TrainNavigationServiceInterface;
import com.traintrax.navigation.service.ValueUpdate;
import com.traintrax.navigation.service.events.GenericPublisher;
import com.traintrax.navigation.service.events.NotifierInterface;
import com.traintrax.navigation.service.events.PublisherInterface;
import com.traintrax.navigation.service.events.TrainNavigationServiceEventNotifier;
import com.traintrax.navigation.service.math.*;
import com.traintrax.navigation.service.mdu.*;
import com.traintrax.navigation.service.position.AccelerometerMeasurement;
import com.traintrax.navigation.service.position.Coordinate;
import com.traintrax.navigation.service.position.GyroscopeMeasurement;
import com.traintrax.navigation.service.position.InertialMotionPositionAlgorithmInterface;
import com.traintrax.navigation.service.position.Train;
import com.traintrax.navigation.service.position.TrainPosition2DAlgorithm;
import com.traintrax.navigation.service.position.TrainPositionEstimate;
import com.traintrax.navigation.service.position.UnitConversionUtilities;
import com.traintrax.navigation.service.position.Velocity;
import com.traintrax.navigation.service.rotation.*;
import com.traintrax.navigation.service.testing.MduMeasurementGenerator;
import com.traintrax.navigation.service.testing.PositionTestCase;
import com.traintrax.navigation.service.testing.PositionTestCaseFileReader;
import com.traintrax.navigation.service.testing.PositionTestSample;
import com.traintrax.navigation.trackswitch.SwitchState;

import gnu.io.*;

public class TestNavigationProgram {

	/**
	 * @return A HashSet containing the CommPortIdentifier for all serial ports
	 *         that are not currently being used.
	 */
	public static HashSet<CommPortIdentifier> getAvailableSerialPorts() {
		HashSet<CommPortIdentifier> h = new HashSet<CommPortIdentifier>();
		Enumeration thePorts = CommPortIdentifier.getPortIdentifiers();
		while (thePorts.hasMoreElements()) {
			CommPortIdentifier com = (CommPortIdentifier) thePorts.nextElement();
			switch (com.getPortType()) {
			default:
			case CommPortIdentifier.PORT_SERIAL:

				try {
					if (!com.getName().contains("LPT")) {
						CommPort thePort = com.open("CommUtil", 50);
						thePort.close();
						h.add(com);
					}
				} catch (PortInUseException e) {
					System.out.println("Port, " + com.getName() + ", is in use.");
				} catch (Exception e) {
					System.err.println("Failed to open port " + com.getName());
					e.printStackTrace();
				}
			}
		}
		return h;
	}

	private static void TestJmri() {
		String serialPort = "/dev/ttyUSB0";
		// String serialPort = "/dev/ttyACM0";
		// String serialPort = "COM4";
		String prefix = "L";
		int switchNumber = 43;

		TrainNavigationServiceInterface trainNavigationServiceInterface = null;

		try {
			trainNavigationServiceInterface = new TrainNavigationService();
			trainNavigationServiceInterface.SetSwitchState("SW43", SwitchState.Pass);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void TestMduMeasurementRead() {

		String trainId = "1";
		Coordinate currentPosition = new Coordinate(0, 0, 0);
		EulerAngleRotation currentOrientation = new EulerAngleRotation(0, 0, Math.PI / 4);
		Velocity currentVelocity = new Velocity(0, 0, 0);

		/*
		 * MduCommunicationChannelInterface mduCommunicationChannel = new
		 * TestMduCommunicationChannel(); MduProtocolParserInterface
		 * mduProtocolParser = new MduProtocolParser();
		 * MotionDetectionUnitInterface motionDetectionUnit = new
		 * MotionDetectionUnit(mduCommunicationChannel, mduProtocolParser);
		 */

		//Generates a test case where the train moves in a diagonal line and accelerates for 1 second,
		//then moves at a constant speed for 10 seconds
		double initialSpeedInMetersPerSecond = 1;
		double accelerationInMetersPerSecondSquared = 0;
		int numberOfSeconds = 10;
		int numSamplesBeforeTagEvent = 3;
		double kineticFrictionOffset = 0.35;
		Calendar startTime = Calendar.getInstance();
		
		//Stay still for 50 samples so that calibration can complete.
		PositionTestCase calibrationTestCase = MduMeasurementGenerator
				.generateStraightLine(currentOrientation.getRadiansRotationAlongZAxis(), currentPosition, 0,
						0, 50, startTime, Integer.MAX_VALUE,0);

		startTime.add(Calendar.SECOND,  50);

		//Accelerate the train for 1 second so that it reaches the constant speed needed
		PositionTestCase startupTestCase = MduMeasurementGenerator
				.generateStraightLine(currentOrientation.getRadiansRotationAlongZAxis(), currentPosition, 0,
						1, 1, startTime, Integer.MAX_VALUE, kineticFrictionOffset);
		PositionTestSample lastStartupSample = startupTestCase.getSamples().get(startupTestCase.getSamples().size()-1);

		startTime.add(Calendar.SECOND,  1);
		PositionTestCase testCase = MduMeasurementGenerator
				.generateStraightLine(currentOrientation.getRadiansRotationAlongZAxis(), lastStartupSample.getExpectedPosition().getValue(), initialSpeedInMetersPerSecond,
						accelerationInMetersPerSecondSquared, numberOfSeconds, startTime, numSamplesBeforeTagEvent, kineticFrictionOffset);
		
		PositionTestCase straightLineWithInitialAccTestCase = new PositionTestCase("Train straightline with initial acceleration", currentPosition, currentOrientation, currentVelocity);
		
		//straightLineWithInitialAccTestCase.appendTestCase(calibrationTestCase);
		straightLineWithInitialAccTestCase.appendTestCase(startupTestCase);
		straightLineWithInitialAccTestCase.appendTestCase(testCase);

		SimulatedMotionDetectionUnit motionDetectionUnit = new SimulatedMotionDetectionUnit();

		TrainNavigationDatabaseInterface trainNavigationDatabase;
		GenericDatabaseInterface gdi = new MySqlDatabaseAdapter();
		FilteredSearchRepositoryInterface<TrackPoint, TrackPointSearchCriteria> trackPointRepository = new TrackPointRepository(
				gdi);
		FilteredSearchRepositoryInterface<com.traintrax.navigation.database.library.AccelerometerMeasurement, AccelerometerMeasurementSearchCriteria> accelerometerMeasurementRepository = new AccelerometerMeasurementRepository(
				gdi);
		FilteredSearchRepositoryInterface<com.traintrax.navigation.database.library.RfidTagDetectedNotification, RfidTagDetectedNotificationSearchCriteria> rfidTagNotificationRepository = new RfidTagDetectedNotificationRepository(
				gdi);
		FilteredSearchRepositoryInterface<com.traintrax.navigation.database.library.GyroscopeMeasurement, GyroscopeMeasurementSearchCriteria> gyroscopeMeasurementRepository = new GyroscopeMeasurementRepository(
				gdi);
		FilteredSearchRepositoryInterface<TrainPosition, TrainPositionSearchCriteria> trainPositionRepository = new TrainPositionRepository(
				gdi);
		FilteredSearchRepositoryInterface<TrackSwitch, TrackSwitchSearchCriteria> trackSwitchRepository = new TrackSwitchRepository(
				gdi);

		trainNavigationDatabase = new TrainNavigationDatabase(trackPointRepository, accelerometerMeasurementRepository,
				gyroscopeMeasurementRepository, rfidTagNotificationRepository, trainPositionRepository, trackSwitchRepository);

		InertialMotionPositionAlgorithmInterface positionAlgorithm = new TrainPosition2DAlgorithm(currentPosition,
				currentOrientation, currentVelocity);
		
		TrackSwitchControllerInterface trainController = null;

		trainController = new TestTrackSwitchController();

		NotifierInterface<TrainNavigationServiceEventSubscriber, TrainNavigationServiceEvent> eventNotifier = new TrainNavigationServiceEventNotifier();

		PublisherInterface<TrainNavigationServiceEventSubscriber, TrainNavigationServiceEvent> eventPublisher = new GenericPublisher<TrainNavigationServiceEventSubscriber, TrainNavigationServiceEvent>(
				eventNotifier);

		TrainNavigationServiceInterface trainNavigationService = new TrainNavigationService(motionDetectionUnit,
				trainController, trainNavigationDatabase, eventPublisher, positionAlgorithm);

		// ReadTrainPositionsFromTrainNavigationService(trainNavigationService);

		String selectedTrain = "1";
		
		//Get the calibration out of the way.
		motionDetectionUnit.enqueueSamples(calibrationTestCase.getSamples());
		
		for (PositionTestSample sample : straightLineWithInitialAccTestCase.getSamples()) {
			List<PositionTestSample> tempSamples = new LinkedList<PositionTestSample>();
			tempSamples.add(sample);
			//TODO: Figure out a way to fake the tag position lookup.
			motionDetectionUnit.enqueueSamples(tempSamples);

			TrainPositionEstimate trainPositionEstimate;
			try {
				trainPositionEstimate = trainNavigationService.GetLastKnownPosition(selectedTrain);

				System.out.println(String.format("Current position of train %s: (%f, %f) at %s", selectedTrain,
						trainPositionEstimate.getPosition().getX(), trainPositionEstimate.getPosition().getY(),
						trainPositionEstimate.getTimeAtPosition().getTime()));

				Thread.sleep(1000);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private static void TestSampleRotation() {
		List<GyroscopeMeasurement> measurements = new ArrayList<GyroscopeMeasurement>();
		EulerAngleRotation initialBodyFrameOrientation = new EulerAngleRotation(0, 0, 0);
		Calendar timeMeasured = Calendar.getInstance();
		final String DefaultTrainId = "2A";

		int numberOfSamples = 100;
		double degreeChange = Math.PI / 2;
		double degreeChangePerSample = degreeChange / numberOfSamples;

		for (int i = 0; i < numberOfSamples; i++) {
			GyroscopeMeasurement measurement = new GyroscopeMeasurement(DefaultTrainId, degreeChangePerSample, 0, 0, 1, timeMeasured);

			measurements.add(measurement);
		}

		TestGyroscope testGyroscope = new TestGyroscope(measurements);
		RotationMonitor rotationMonitor = new RotationMonitor(testGyroscope, initialBodyFrameOrientation);
		rotationMonitor.AddSubscriber(new GenerationRotationChangeSubscriber() {

			@Override
			public void OrientationChanged(EulerAngleRotation newBodyFrameOrientation) {

				super.OrientationChanged(newBodyFrameOrientation);

				System.out.println(String.format("%f radians around x axis",
						newBodyFrameOrientation.getRadiansRotationAlongXAxis()));
				System.out.println(String.format("%f radians around y axis",
						newBodyFrameOrientation.getRadiansRotationAlongYAxis()));
				System.out.println(String.format("%f radians around z axis",
						newBodyFrameOrientation.getRadiansRotationAlongZAxis()));
			}

		});

		EulerAngleRotation lastReportedTotalBodyFrameRotation = null;
		for (int i = 0; i < numberOfSamples; i++) {
			lastReportedTotalBodyFrameRotation = rotationMonitor.waitForNextRotationUpdate();
		}

		boolean testPassed = true;
		double tolerance = 0.1;

		testPassed = testPassed && (lastReportedTotalBodyFrameRotation != null) && (Math
				.abs(lastReportedTotalBodyFrameRotation.getRadiansRotationAlongXAxis() - degreeChange) < tolerance);

		if (testPassed) {
			System.out.println("Test Passed!");
		} else {
			System.out.println("Test Failed!");
		}

	}

	private static void PrintRotation(EulerAngleRotation rotation) {

		System.out.println(String.format("%f radians around x axis", rotation.getRadiansRotationAlongXAxis()));
		System.out.println(String.format("%f radians around y axis", rotation.getRadiansRotationAlongYAxis()));
		System.out.println(String.format("%f radians around z axis", rotation.getRadiansRotationAlongZAxis()));

	}

	private static void TestMeasurementCsv() {

		final String filename = "/home/death/Documents/CPE656/fullRotation_Nexus_7_09_18_15.csv";
		GyroscopeReader gyroscopeReader = new GyroscopeReader(filename);
		List<GyroscopeMeasurement> measurements = gyroscopeReader.getGyroscopeMeasurements();
		// EulerAngleRotation initialBodyFrameOrientation = new
		// EulerAngleRotation(Math.PI/2,0,0);
		EulerAngleRotation initialBodyFrameOrientation = new EulerAngleRotation(0, Math.PI / 3, 0);

		final int numberOfSamples = measurements.size();
		TestGyroscope testGyroscope = new TestGyroscope(measurements);
		RotationMonitor rotationMonitor = new RotationMonitor(testGyroscope, initialBodyFrameOrientation);
		rotationMonitor.AddSubscriber(new GenerationRotationChangeSubscriber() {

			@Override
			public void OrientationChanged(EulerAngleRotation newBodyFrameOrientation) {

				super.OrientationChanged(newBodyFrameOrientation);

				PrintRotation(newBodyFrameOrientation);
			}

		});

		EulerAngleRotation lastReportedTotalBodyFrameRotation = null;
		for (int i = 0; i < numberOfSamples; i++) {
			lastReportedTotalBodyFrameRotation = rotationMonitor.waitForNextRotationUpdate();

			// Adjust rotation to inertial frame
			EulerAngleRotation inertialFrameRotation = RotationUtilities.convertRotationFromBodyFrameToNedFrame(
					lastReportedTotalBodyFrameRotation, initialBodyFrameOrientation);

			System.out.println(String.format("%f radians around Earth x axis",
					inertialFrameRotation.getRadiansRotationAlongXAxis()));
			System.out.println(String.format("%f radians around Earth y axis",
					inertialFrameRotation.getRadiansRotationAlongYAxis()));
			System.out.println(String.format("%f radians around Earth z axis",
					inertialFrameRotation.getRadiansRotationAlongZAxis()));

			Quat4d initialRotationQuaternion = RotationUtilities.convertFromEulerAngleToQuaternion(
					initialBodyFrameOrientation.getRadiansRotationAlongXAxis(),
					initialBodyFrameOrientation.getRadiansRotationAlongYAxis(),
					initialBodyFrameOrientation.getRadiansRotationAlongZAxis());

			Quat4d bodyFrameRotationQuaternion = RotationUtilities.convertFromEulerAngleToQuaternion(
					inertialFrameRotation.getRadiansRotationAlongXAxis(),
					inertialFrameRotation.getRadiansRotationAlongYAxis(),
					inertialFrameRotation.getRadiansRotationAlongZAxis());

			Quat4d compositeRotationQuaternion = initialRotationQuaternion.multiply(bodyFrameRotationQuaternion);

			System.out.println("Estimate Earth orientation by attempting to use only quaternions");

			EulerAngleRotation compositeEulerAngleRotation = RotationUtilities
					.convertFromQuaternionToEulerAngle(compositeRotationQuaternion);

			PrintRotation(compositeEulerAngleRotation);
		}

		boolean testPassed = true;
		double tolerance = 0.1;

		// testPassed = testPassed && (lastReportedTotalBodyFrameRotation !=
		// null)&&(Math.abs(lastReportedTotalBodyFrameRotation.getRadiansRotationAlongXAxis()
		// - degreeChange) < tolerance);

		if (testPassed) {
			System.out.println("Test Passed!");
		} else {
			System.out.println("Test Failed!");
		}

	}

	public static void TestQuaternionMultiplication() {
		Quat4d quat4d = new Quat4d(1, 0, 1, 0);
		Quat4d quat4d2 = new Quat4d(1, 0.5, 0.5, 0.75);
		Quat4d expectedProduct = new Quat4d(0.5, 1.25, 1.5, 0.25);
		Quat4d prod = quat4d.multiply(quat4d2);

		boolean areEqual = prod.equals(expectedProduct);

		if (areEqual)
			System.out.println("Are Equal");
		else
			System.out.println("Are NOT Equal");
	}

	private static void TestRotationOfVectorUsingEulerAngleDerivedRotationMatrix() {
		Matrix testVector = new Matrix(3, 1);
		testVector.setValue(0, 0, 0);
		testVector.setValue(1, 0, 1);
		testVector.setValue(2, 0, 0);

		Matrix rotationMatrix = RotationUtilities.createRotationMatrix(new EulerAngleRotation(Math.PI / 2, 0, 0));

		Matrix expectedRotatedVector = new Matrix(3, 1);
		expectedRotatedVector.setValue(0, 0, 0); // x
		expectedRotatedVector.setValue(1, 0, 0); // y
		expectedRotatedVector.setValue(2, 0, 1); // z

		// Assumptions:
		// Counter clockwise rotations are positive
		// Check using the NED Local Earth Reference Frame
		// Heading North is Positive
		// Heading East is Positive
		// Heading into the Earth is Positive (So going in the air is negative)

		Matrix rotatedVector = rotationMatrix.multiply(testVector).round();

		Matrix.PrintMatrix(rotatedVector);

		boolean coordinateTransformationIsCorrect = rotatedVector.round().equals(expectedRotatedVector.round());

		if (coordinateTransformationIsCorrect) {
			System.out.println("Transformation of coordinate axises are correct!");
		} else {
			System.out.println("ERROR: Transformation of coordinate axises are WRONG!");
		}
	}

	public static void Assert(boolean condition) throws Exception {
		if (!condition) {

			throw new Exception("Test Failed");
		}
	}

	public static void TestLinearInterpolation() {

		boolean testPassed = true;
		double x[] = { 0, 1 };
		double y[] = { 0, 1 };

		try {
			double interpolatedY;

			// Test Beyond Upper Bound
			interpolatedY = MathUtilities.LinearInterpolate(x, y, 2);
			Assert(interpolatedY == 2);

			// Test At Upper Bound
			interpolatedY = MathUtilities.LinearInterpolate(x, y, 1);
			Assert(interpolatedY == 1);

			// Test At Lower Bound
			interpolatedY = MathUtilities.LinearInterpolate(x, y, 0);
			Assert(interpolatedY == 0);

			// Test Below Lower Bound
			interpolatedY = MathUtilities.LinearInterpolate(x, y, -1);
			Assert(interpolatedY == -1);

			// Test Between Bounds
			interpolatedY = MathUtilities.LinearInterpolate(x, y, 0.5);
			Assert(interpolatedY == 0.5);

			// Test Single Point Interpolate
			double x1[] = { 0 };
			double y1[] = { 0 };
			interpolatedY = MathUtilities.LinearInterpolate(x1, y1, 1);
			Assert(interpolatedY == 0);
		} catch (Exception e) {
			testPassed = false;
		}

		if (testPassed) {
			System.out.println("Test Passed");
		} else {
			System.out.println("Test Failed");
		}
	}

	private static void ReadTrainPositionsFromTrainNavigationService(
			TrainNavigationServiceInterface trainNavigationService) {

		List<String> trains = null;
		try {
			trains = trainNavigationService.GetKnownTrainIdentifiers();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		System.out.println("List of known Train Ids:");

		for (String trainId : trains) {
			System.out.println(trainId);
		}
		System.out.println("");

		String selectedTrain = trains.get(0);

		// Wait 2 seconds
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		TrainPositionEstimate trainPositionEstimate = null;
		try {
			trainPositionEstimate = trainNavigationService.GetLastKnownPosition(selectedTrain);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(String.format("Current position of train %s: (%f, %f) at %s", selectedTrain,
				trainPositionEstimate.getPosition().getX(), trainPositionEstimate.getPosition().getY(),
				trainPositionEstimate.getTimeAtPosition().getTime()));

	}

	private static void TestPositionAlgorithm() {
		List<ValueUpdate<Tuple<GyroscopeMeasurement, AccelerometerMeasurement>>> imuReadings;
		List<ValueUpdate<Coordinate>> positionReadings;
		List<ValueUpdate<Tuple<Coordinate,Velocity>>> finalPositions = new LinkedList<ValueUpdate<Tuple<Coordinate, Velocity>>>();

		imuReadings = ImuMeasurementsReader
				.ReadFile("/home/death/Documents/CPE658/sample-imu-data/02-17-2016/ImuUpdates.csv");
		// imuReadings = ImuMeasurementsReader
		// .ReadFile("/home/death/Documents/CPE658/sample-imu-data/testdataandicd/Sample
		// 3 - Imu.csv");

		positionReadings = PositionMeasurementsReader
				.ReadFile("/home/death/Documents/CPE658/sample-imu-data/02-17-2016/PositionUpdates-6inches.csv");

		// positionReadings = PositionMeasurementsReader
		// .ReadFile("/home/death/Documents/CPE658/sample-imu-data/02-17-2016/PositionUpdates-12
		// inches.csv");

		// Combine all of the data so that if is entered into the algorithm in
		// the order that it would be received
		List<ValueUpdate<Object>> timeline = new LinkedList<ValueUpdate<Object>>();

		for (ValueUpdate<Tuple<GyroscopeMeasurement, AccelerometerMeasurement>> entry : imuReadings) {
			ValueUpdate<Object> valueUpdate = new ValueUpdate<Object>(entry.getValue(), entry.getTimeObserved());
			timeline.add(valueUpdate);
		}

		for (ValueUpdate<Coordinate> entry : positionReadings) {
			ValueUpdate<Object> valueUpdate = new ValueUpdate<Object>(entry.getValue(), entry.getTimeObserved());
			timeline.add(valueUpdate);
		}

		// Sort timeline
		Collections.sort(timeline);

		double inchesToMeters = 2.54 / 100;

		// Calculate Position
		// NOTE: Ball Parked an initial position based on the 02-17-16 data.
		InertialMotionPositionAlgorithmInterface positionAlgorithm = new TrainPosition2DAlgorithm(
				new Coordinate(42.5 * inchesToMeters, 61.1875 * inchesToMeters, 0),
				new EulerAngleRotation(0, 0, -Math.PI), new Velocity(0,0,0));

		try {
			FileWriter fileWriter = new FileWriter("/home/death/debug_log.txt");
			BufferedWriter bw = new BufferedWriter(fileWriter);

			for (ValueUpdate<Object> timePoint : timeline) {

				ValueUpdate<Tuple<Coordinate,Velocity>> positionUpdate = null;
				if (timePoint.getValue() instanceof Tuple<?, ?>) {
					bw.write("IMU Processed\n");
					Tuple<GyroscopeMeasurement, AccelerometerMeasurement> imuReading = (Tuple<GyroscopeMeasurement, AccelerometerMeasurement>) timePoint
							.getValue();
					GyroscopeMeasurement gyroscopeMeasurement = imuReading.getItem1();
					AccelerometerMeasurement accelerometerMeasurement = imuReading.getItem2();

					List<GyroscopeMeasurement> gyroscopeMeasurements = new LinkedList<GyroscopeMeasurement>();
					gyroscopeMeasurements.add(gyroscopeMeasurement);

					List<AccelerometerMeasurement> accelerometerMeasurements = new LinkedList<AccelerometerMeasurement>();
					accelerometerMeasurements.add(accelerometerMeasurement);

					positionUpdate = positionAlgorithm.calculatePosition(gyroscopeMeasurements,
							accelerometerMeasurements, new LinkedList<ValueUpdate<Coordinate>>());
				} else if (timePoint.getValue() instanceof Coordinate) {
					bw.write("RFID Processed\n");
					Coordinate positionReading = UnitConversionUtilities
							.convertFromInchesToMeters((Coordinate) timePoint.getValue());

					ValueUpdate<Coordinate> positionMeasurement = new ValueUpdate<Coordinate>(positionReading,
							timePoint.getTimeObserved());
					List<ValueUpdate<Coordinate>> positionMeasurements = new LinkedList<ValueUpdate<Coordinate>>();

					positionMeasurements.add(positionMeasurement);

					positionUpdate = positionAlgorithm.calculatePosition(new LinkedList<GyroscopeMeasurement>(),
							new LinkedList<AccelerometerMeasurement>(), positionMeasurements);
				}

				if (positionUpdate != null) {
					String row = String.format("%f, %f, %f, %f\n", positionUpdate.getValue().getItem1().getX(),
							positionUpdate.getValue().getItem1().getY(), positionUpdate.getValue().getItem1().getZ(),
							positionUpdate.getTimeObserved().getTimeInMillis() / 1000.0);

					bw.write(row);
					finalPositions.add(positionUpdate);
				} else {
					System.out.println("Warning: Position Update not processed\n");
				}
			}

			bw.flush();
			bw.close();
			fileWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String positionFile = "/home/death/position.csv";

		try {
			FileWriter fileWriter = new FileWriter(positionFile);
			BufferedWriter bw = new BufferedWriter(fileWriter);

			// Write position to CSV

			for (ValueUpdate<Tuple<Coordinate, Velocity>> finalPosition : finalPositions) {

				Coordinate positionInInches = UnitConversionUtilities
						.convertFromMetersToInches(finalPosition.getValue().getItem1());
				String row = String.format("%f, %f, %f, %f\n",
						finalPosition.getTimeObserved().getTimeInMillis() / 1000.0, positionInInches.getX(),
						positionInInches.getY(), positionInInches.getZ());
				bw.write(row);
			}

			bw.flush();
			bw.close();
			fileWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// ReadTrainPositionsFromTrainNavigationService();

		HashSet<CommPortIdentifier> ports = getAvailableSerialPorts();

		for (CommPortIdentifier p : ports) {
			System.out.println(p.getName());
		}

		// TestJmri();
		// TestPositionAlgorithm();

		//TestMduMeasurementRead();
		
		PositionTestCase testCase = PositionTestCaseFileReader.Read("C:\\TrainTrax\\CPE656TL-master\\prototypes\\TestNavigation\\PositionTestCaseTemplate.csv");
		
		System.out.println(testCase.getDescription());

	}

}
