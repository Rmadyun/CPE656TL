package com.traintrax.navigation.service.unittest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import com.traintrax.navigation.service.ValueUpdate;
import com.traintrax.navigation.service.math.Tuple;
import com.traintrax.navigation.service.mdu.*;
import com.traintrax.navigation.service.position.Acceleration;
import com.traintrax.navigation.service.position.AccelerometerMeasurement;
import com.traintrax.navigation.service.position.Coordinate;
import com.traintrax.navigation.service.position.GyroscopeMeasurement;
import com.traintrax.navigation.service.position.InertialMotionPositionAlgorithmInterface;
import com.traintrax.navigation.service.position.TrainPosition2DAlgorithm;
import com.traintrax.navigation.service.position.UnitConversionUtilities;
import com.traintrax.navigation.service.position.Velocity;
import com.traintrax.navigation.service.rotation.*;
import com.traintrax.navigation.service.testing.MduMeasurementGenerator;
import com.traintrax.navigation.service.testing.PositionTestCase;
import com.traintrax.navigation.service.testing.PositionTestCaseFileReader;
import com.traintrax.navigation.service.testing.PositionTestSample;

import junit.framework.Assert;

public class TrainPositionAlgorithmTests {

	private static final String DefaultTrainId = "2A";

	/**
	 * Generates Accelerometer measurements
	 * 
	 * @param startTime
	 *            Time of the first sample
	 * @param numberOfSeconds
	 *            Time range for the samples that are reported
	 * @return Generated accelerometer measurements Assuming that one sample is
	 *         provided per second.
	 */
	private static List<AccelerometerMeasurement> generateAccelerometerMeasurements(Calendar startTime,
			int numberOfSeconds) {
		List<AccelerometerMeasurement> accelerometerMeasurements = new LinkedList<AccelerometerMeasurement>();
		Calendar currentTime = (Calendar) startTime.clone();

		// Generate

		// Assuming object accelerates to 1 m/s after the first second of
		// movement
		// Measurements
		System.out.printf("Generated acc time: %d\n", currentTime.getTimeInMillis());
		accelerometerMeasurements
				.add(new AccelerometerMeasurement(DefaultTrainId, new Acceleration(0, 0, 0), 0, currentTime));

		currentTime = (Calendar) currentTime.clone();
		currentTime.add(Calendar.SECOND, 1);
		System.out.printf("Generated acc time: %d\n", currentTime.getTimeInMillis());
		accelerometerMeasurements
				.add(new AccelerometerMeasurement(DefaultTrainId, new Acceleration(1, 0, 0), 1, currentTime));

		for (int i = 0; i < numberOfSeconds - 2; i++) {
			currentTime = (Calendar) currentTime.clone();
			currentTime.add(Calendar.SECOND, 1);
			System.out.printf("Generated acc time: %d\n", currentTime.getTimeInMillis());
			accelerometerMeasurements
					.add(new AccelerometerMeasurement(DefaultTrainId, new Acceleration(0, 0, 0), 1, currentTime));
		}

		return accelerometerMeasurements;
	}

	/**
	 * Generates Gyroscope measurements
	 * 
	 * @param startTime
	 *            Time of the first sample
	 * @param numberOfSeconds
	 *            Time range for the samples that are reported
	 * @return Generated gyroscope measurements Assuming that one sample is
	 *         provided per second.
	 */
	private static List<GyroscopeMeasurement> generateGyroscopeMeasurements(EulerAngleRotation rotation,
			int numberOfSeconds, Calendar startTime) {
		List<GyroscopeMeasurement> measurements = new ArrayList<GyroscopeMeasurement>();
		EulerAngleRotation initialBodyFrameOrientation = new EulerAngleRotation(0, 0, 0);
		Calendar timeMeasured = (Calendar) startTime.clone();

		double xAngleChangePerSample = rotation.getRadiansRotationAlongXAxis() / numberOfSeconds;
		double yAngleChangePerSample = rotation.getRadiansRotationAlongYAxis() / numberOfSeconds;
		double zAngleChangePerSample = rotation.getRadiansRotationAlongZAxis() / numberOfSeconds;

		for (int i = 0; i < numberOfSeconds; i++) {

			GyroscopeMeasurement measurement = new GyroscopeMeasurement(DefaultTrainId, xAngleChangePerSample,
					yAngleChangePerSample, zAngleChangePerSample, 1, timeMeasured);

			System.out.printf("Generated gyr time: %d\n", timeMeasured.getTimeInMillis());
			timeMeasured = (Calendar) timeMeasured.clone();
			timeMeasured.add(Calendar.SECOND, 1);
			measurements.add(measurement);
		}

		return measurements;
	}

	/*
	 * @Test public void
	 * testCalculatePositionWithStraightLineAlongTestBedCoordinateFrameX() {
	 * 
	 * double tolerance = 0.05; EulerAngleRotation initialOrientation = new
	 * EulerAngleRotation(0, 0, 0); Coordinate initialPosition = new
	 * Coordinate(0, 0, 0); // Assuming starting from origin.
	 * InertialMotionPositionAlgorithmInterface uut = new
	 * TrainPosition2DAlgorithm(initialPosition, initialOrientation); Calendar
	 * startTime = Calendar.getInstance(); int numberOfSeconds = 10;
	 * List<AccelerometerMeasurement> accelerometerMeasurements =
	 * generateAccelerometerMeasurements(startTime, numberOfSeconds);
	 * List<GyroscopeMeasurement> gyroscopeMeasurements =
	 * generateGyroscopeMeasurements(new EulerAngleRotation(0,0,0),
	 * numberOfSeconds, startTime);
	 * 
	 * ValueUpdate<Coordinate> currentPosition =
	 * uut.calculatePosition(gyroscopeMeasurements, accelerometerMeasurements,
	 * null); Calendar finalTime = (Calendar) startTime.clone();
	 * finalTime.add(Calendar.SECOND, numberOfSeconds-1);
	 * 
	 * //TODO: The list after adjusting the orientation of the acceleration
	 * vectors needs to be sorted in order //for this to be correct.
	 * //http://stackoverflow.com/questions/16252269/how-to-sort-a-list-
	 * arraylist-in-java
	 * 
	 * 
	 * assertEquals(currentPosition.getTimeObserved().getTimeInMillis(),
	 * finalTime.getTimeInMillis(), tolerance);
	 * assertEquals(currentPosition.getValue().getX(), 9, tolerance);
	 * assertEquals(currentPosition.getValue().getY(), 0, tolerance);
	 * assertEquals(currentPosition.getValue().getZ(), 0, tolerance);
	 * 
	 * }
	 */
	private PositionTestCase generateTestCaseForStraightLineAt45DegreeAngle() {
		Coordinate currentPosition = new Coordinate(0, 0, 0);
		EulerAngleRotation currentOrientation = new EulerAngleRotation(0, 0, Math.PI / 4);
		Velocity currentVelocity = new Velocity(0, 0, 0);

		// Generates a test case where the train moves in a diagonal line and
		// accelerates for 1 second,
		// then moves at a constant speed for 10 seconds
		double initialSpeedInMetersPerSecond = 1;
		double accelerationInMetersPerSecondSquared = 0;
		int numberOfSeconds = 10;
		int numSamplesBeforeTagEvent = 3;
		double kineticFrictionOffset = 0.35;
		Calendar startTime = Calendar.getInstance();

		// Stay still for 50 samples so that calibration can complete.
		PositionTestCase calibrationTestCase = MduMeasurementGenerator.generateStraightLine(
				currentOrientation.getRadiansRotationAlongZAxis(), currentPosition, 0, 0, 50, startTime,
				Integer.MAX_VALUE, 0);

		startTime.add(Calendar.SECOND, 50);

		// Accelerate the train for 1 second so that it reaches the constant
		// speed needed
		PositionTestCase startupTestCase = MduMeasurementGenerator.generateStraightLine(
				currentOrientation.getRadiansRotationAlongZAxis(), currentPosition, 0, 1, 1, startTime,
				Integer.MAX_VALUE, kineticFrictionOffset);
		PositionTestSample lastStartupSample = startupTestCase.getSamples()
				.get(startupTestCase.getSamples().size() - 1);

		startTime.add(Calendar.SECOND, 1);
		PositionTestCase testCase = MduMeasurementGenerator.generateStraightLine(
				currentOrientation.getRadiansRotationAlongZAxis(), lastStartupSample.getExpectedPosition().getValue(),
				initialSpeedInMetersPerSecond, accelerationInMetersPerSecondSquared, numberOfSeconds, startTime,
				numSamplesBeforeTagEvent, kineticFrictionOffset);

		PositionTestCase straightLineWithInitialAccTestCase = new PositionTestCase(
				"Train straightline with initial acceleration", currentPosition, currentOrientation, currentVelocity);

		straightLineWithInitialAccTestCase.appendTestCase(calibrationTestCase);
		straightLineWithInitialAccTestCase.appendTestCase(startupTestCase);
		straightLineWithInitialAccTestCase.appendTestCase(testCase);

		return straightLineWithInitialAccTestCase;
	}

	private PositionTestCase generateTestCaseForCircleAtOneRadianPerSecond() {
		Coordinate currentPosition = new Coordinate(0, 0, 0);
		EulerAngleRotation currentOrientation = new EulerAngleRotation(0, 0, 0);
		Velocity currentVelocity = new Velocity(0, 0, 0);

		// Generates a test case where the train moves in a diagonal line and
		// accelerates for 1 second,
		// then moves at a constant speed for 10 seconds
		double initialSpeedInMetersPerSecond = 1;
		double accelerationInMetersPerSecondSquared = 0;
		int numberOfSeconds = 10;
		int numSamplesBeforeTagEvent = 3;
		double kineticFrictionOffset = 0.35;
		Calendar startTime = Calendar.getInstance();
		double angularSpeedInRadiansPerSecond = 1;

		// Stay still for 50 samples so that calibration can complete.
		PositionTestCase calibrationTestCase = MduMeasurementGenerator.generateStraightLine(
				currentOrientation.getRadiansRotationAlongZAxis(), currentPosition, 0, 0, 50, startTime,
				Integer.MAX_VALUE, 0);

		startTime.add(Calendar.SECOND, 50);

		// Accelerate the train for 1 second so that it reaches the constant
		// speed needed
		PositionTestCase startupTestCase = MduMeasurementGenerator.generateStraightLine(
				currentOrientation.getRadiansRotationAlongZAxis(), currentPosition, 0, 1, 1, startTime,
				Integer.MAX_VALUE, kineticFrictionOffset);
		PositionTestSample lastStartupSample = startupTestCase.getSamples()
				.get(startupTestCase.getSamples().size() - 1);

		startTime.add(Calendar.SECOND, 1);
		PositionTestCase testCase = MduMeasurementGenerator.generateCircle(
				currentOrientation.getRadiansRotationAlongZAxis(), lastStartupSample.getExpectedPosition().getValue(),
				initialSpeedInMetersPerSecond, accelerationInMetersPerSecondSquared, numberOfSeconds, startTime,
				numSamplesBeforeTagEvent, kineticFrictionOffset, angularSpeedInRadiansPerSecond);

		PositionTestCase circleAtOneRadianPerSecondTestCase = new PositionTestCase(
				"Train straightline with initial acceleration", currentPosition, currentOrientation, currentVelocity);

		circleAtOneRadianPerSecondTestCase.appendTestCase(calibrationTestCase);
		circleAtOneRadianPerSecondTestCase.appendTestCase(startupTestCase);
		circleAtOneRadianPerSecondTestCase.appendTestCase(testCase);

		return circleAtOneRadianPerSecondTestCase;
	}

	@Test
	public void testCalculatePositionWithStraightLineAt45DegreeAngle() {
		PositionTestCase testCase = generateTestCaseForStraightLineAt45DegreeAngle();

		TestPositionAlgorithm(testCase);
	}

	@Test
	public void testCalculatePositionWithCircle() {
		PositionTestCase testCase = generateTestCaseForCircleAtOneRadianPerSecond();

		TestPositionAlgorithm(testCase);
	}

	@Test
	public void testCalculatePositionWithActualData() {
		PositionTestCase testCase = PositionTestCaseFileReader.Read("C:\\TrainTrax\\CPE656TL-master\\test\\TrainSample.csv");

		TestPositionAlgorithm(testCase);
	}

	/**
	 * Conducts test for a particular test case
	 * 
	 * @param positionTestCase
	 *            Test case to execute
	 */
	private void TestPositionAlgorithm(PositionTestCase positionTestCase) {
		double tolerance = 0.05;
		List<ValueUpdate<Tuple<GyroscopeMeasurement, AccelerometerMeasurement>>> imuReadings;
		List<ValueUpdate<Coordinate>> positionReadings;
		List<ValueUpdate<Coordinate>> finalPositions = new LinkedList<ValueUpdate<Coordinate>>();

		String positionFile = "C:\\TrainTrax\\CPE656TL-master\\test\\position.csv";
		
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

			if (expectedPositionUpdate != null) {

				if (actualPositionUpdate.getValue().getItem1().getX() > 0
						|| actualPositionUpdate.getValue().getItem1().getY() > 0) {

					System.out.println(String.format("Expected position of train %s: (%f, %f) at %s", "1",
							expectedPositionUpdate.getValue().getX(), expectedPositionUpdate.getValue().getY(),
							expectedPositionUpdate.getTimeObserved().getTime()));

					System.out.println(String.format("Actual position of train %s: (%f, %f) at %s", "1",
							actualPositionUpdate.getValue().getItem1().getX(),
							actualPositionUpdate.getValue().getItem1().getY(),
							actualPositionUpdate.getTimeObserved().getTime()));

				}

				assertEquals(actualPositionUpdate.getTimeObserved().getTimeInMillis(),
						expectedPositionUpdate.getTimeObserved().getTimeInMillis(), tolerance);

				assertEquals(actualPositionUpdate.getValue().getItem1().getX(),
						expectedPositionUpdate.getValue().getX(), tolerance);

				assertEquals(actualPositionUpdate.getValue().getItem1().getY(),
						expectedPositionUpdate.getValue().getY(), tolerance);

				assertEquals(actualPositionUpdate.getValue().getItem1().getZ(),
						expectedPositionUpdate.getValue().getZ(), tolerance);

			}

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
