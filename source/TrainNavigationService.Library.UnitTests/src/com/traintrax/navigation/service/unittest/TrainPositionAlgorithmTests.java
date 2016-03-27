package com.traintrax.navigation.service.unittest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import com.traintrax.navigation.service.ValueUpdate;
import com.traintrax.navigation.service.mdu.*;
import com.traintrax.navigation.service.position.Acceleration;
import com.traintrax.navigation.service.position.Coordinate;
import com.traintrax.navigation.service.rotation.*;

public class TrainPositionAlgorithmTests {

	/**
	 * Generates Accelerometer measurements
	 * @param startTime Time of the first sample
	 * @param numberOfSeconds Time range for the samples that are reported
	 * @return Generated accelerometer measurements
	 * Assuming that one sample is provided per second.
	 */
	private static List<AccelerometerMeasurement> generateAccelerometerMeasurements(Calendar startTime, int numberOfSeconds) {
		List<AccelerometerMeasurement> accelerometerMeasurements = new LinkedList<AccelerometerMeasurement>();
		Calendar currentTime = (Calendar) startTime.clone();

		// Generate

		// Assuming object accelerates to 1 m/s after the first second of
		// movement
		// Measurements
		System.out.printf("Generated acc time: %d\n", currentTime.getTimeInMillis());
		accelerometerMeasurements.add(new AccelerometerMeasurement(new Acceleration(0, 0, 0), 0, currentTime));

		currentTime = (Calendar) currentTime.clone();
		currentTime.add(Calendar.SECOND, 1);
		System.out.printf("Generated acc time: %d\n", currentTime.getTimeInMillis());
		accelerometerMeasurements.add(new AccelerometerMeasurement(new Acceleration(1, 0, 0), 1, currentTime));

		for (int i = 0; i < numberOfSeconds - 2; i++) {
			currentTime = (Calendar) currentTime.clone();
			currentTime.add(Calendar.SECOND, 1);
			System.out.printf("Generated acc time: %d\n", currentTime.getTimeInMillis());
			accelerometerMeasurements.add(new AccelerometerMeasurement(new Acceleration(0, 0, 0), 1, currentTime));
		}

		return accelerometerMeasurements;
	}
	
	/**
	 * Generates Gyroscope measurements
	 * @param startTime Time of the first sample
	 * @param numberOfSeconds Time range for the samples that are reported
	 * @return Generated gyroscope measurements
	 * Assuming that one sample is provided per second.
	 */
	private static List<GyroscopeMeasurement> generateGyroscopeMeasurements(EulerAngleRotation rotation, int numberOfSeconds, Calendar startTime){
		List<GyroscopeMeasurement> measurements = new ArrayList<GyroscopeMeasurement>();
		EulerAngleRotation initialBodyFrameOrientation = new EulerAngleRotation(0,0,0);
		Calendar timeMeasured = (Calendar) startTime.clone();

	    double xAngleChangePerSample = rotation.getRadiansRotationAlongXAxis()/numberOfSeconds;
	    double yAngleChangePerSample = rotation.getRadiansRotationAlongYAxis()/numberOfSeconds;
	    double zAngleChangePerSample = rotation.getRadiansRotationAlongZAxis()/numberOfSeconds;
	    
	    for(int i = 0; i < numberOfSeconds; i++){

	    	GyroscopeMeasurement measurement = new GyroscopeMeasurement(xAngleChangePerSample, yAngleChangePerSample, zAngleChangePerSample, 1, timeMeasured);

	    	System.out.printf("Generated gyr time: %d\n", timeMeasured.getTimeInMillis());
	    	timeMeasured = (Calendar) timeMeasured.clone();
	    	timeMeasured.add(Calendar.SECOND, 1);
	    	measurements.add(measurement);
	    }
	    
	    return measurements;
	}

	@Test
	public void testCalculatePositionWithStraightLineAlongTestBedCoordinateFrameX() {
		double tolerance = 0.05;
		EulerAngleRotation initialOrientation = new EulerAngleRotation(0, 0, 0);
		Coordinate initialPosition = new Coordinate(0, 0, 0); // Assuming starting from origin.
		InertialMotionPositionAlgorithmInterface uut = new TrainPosition2DAlgorithm(initialPosition, initialOrientation);
		Calendar startTime = Calendar.getInstance();
		int numberOfSeconds = 10;
		List<AccelerometerMeasurement> accelerometerMeasurements = generateAccelerometerMeasurements(startTime, numberOfSeconds);
		List<GyroscopeMeasurement> gyroscopeMeasurements = generateGyroscopeMeasurements(new EulerAngleRotation(0,0,0), numberOfSeconds, startTime);

		ValueUpdate<Coordinate> currentPosition = uut.calculatePosition(gyroscopeMeasurements, accelerometerMeasurements, null);
		Calendar finalTime = (Calendar) startTime.clone();
		finalTime.add(Calendar.SECOND, numberOfSeconds-1);
		
		//TODO: The list after adjusting the orientation of the acceleration vectors needs to be sorted in order
		//for this to be correct.
		//http://stackoverflow.com/questions/16252269/how-to-sort-a-list-arraylist-in-java
		
		
		assertEquals(currentPosition.getTimeObserved().getTimeInMillis(), finalTime.getTimeInMillis(), tolerance);
		assertEquals(currentPosition.getValue().getX(), 9, tolerance);
		assertEquals(currentPosition.getValue().getY(), 0, tolerance);
		assertEquals(currentPosition.getValue().getZ(), 0, tolerance);
	}

}
