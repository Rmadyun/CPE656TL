package com.traintrax.navigation.service.testing;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import com.traintrax.navigation.service.ValueUpdate;
import com.traintrax.navigation.service.math.Matrix;
import com.traintrax.navigation.service.math.ThreeDimensionalSpaceVector;
import com.traintrax.navigation.service.math.Triplet;
import com.traintrax.navigation.service.mdu.RotationUtilities;
import com.traintrax.navigation.service.position.Acceleration;
import com.traintrax.navigation.service.position.AccelerometerMeasurement;
import com.traintrax.navigation.service.position.Coordinate;
import com.traintrax.navigation.service.position.GyroscopeMeasurement;
import com.traintrax.navigation.service.position.RfidTagDetectedNotification;
import com.traintrax.navigation.service.position.UnitConversionUtilities;
import com.traintrax.navigation.service.position.Velocity;
import com.traintrax.navigation.service.rotation.EulerAngleRotation;

/**
 * Creates simulated MDU measurements for testing
 * @author Corey Sanders
 *
 */
public class MduMeasurementGenerator {
	
	/**
	 * Generates Accelerometer measurements
	 * @param startTime Time of the first sample
	 * @param numberOfSeconds Time range for the samples that are reported
	 * @return Generated accelerometer measurements
	 * Assuming that one sample is provided per second.
	 */
	public static List<AccelerometerMeasurement> generateAccelerometerMeasurements(Calendar startTime, int numberOfSeconds) {
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
	public static List<GyroscopeMeasurement> generateGyroscopeMeasurements(EulerAngleRotation rotation, int numberOfSeconds, Calendar startTime){
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
	
	/**
	 * Calculate the motion of an object as it travels in a straight line. Note: Each reported measurement is incremented in time by a second.
	 * @param orientation Direction that the line is going from its initial position. It is the rotation along the z-axis in radians
	 * @param initialPosition Initial position of the object relative to the origin in inches
	 * @param initialSpeedInMetersPerSecond initial speed of the object in meters per second
	 * @param accelerationInMetersPerSecondSquared constant rate of acceleration of the object in meters per second squared
	 * @param numberOfSeconds Duration of seconds to generate for the sampling.
	 * @param startTime Initial time reported by the sample.
	 * @param numberOfMeasurementsBetweenRfidTagNotifications Number of measurements to report before a RFID Tag position update should be injected.
	 * @param accKineticFrictionOffset Amount to add an offset the accelerometer measurements to account for the force necessary to counter kinetic friction when the train is moving 
	 * @return New test case to use for verifying position estimation when traveling in a straight line.
	 */
	public static PositionTestCase generateStraightLine(double orientation, Coordinate initialPosition, double initialSpeedInMetersPerSecond, double accelerationInMetersPerSecondSquared, int numberOfSeconds, Calendar startTime, int numberOfMeasurementsBetweenRfidTagNotifications, double accKineticFrictionOffset){
		PositionTestCase straightLineTestCase = new PositionTestCase("Straight Line Test Case", initialPosition, new EulerAngleRotation(0,0,orientation));
		List<PositionTestSample> samples = straightLineTestCase.getSamples();
		
		Calendar timeMeasured = (Calendar) startTime.clone();
		
		EulerAngleRotation eulerAngleRotation = new EulerAngleRotation(0,0,orientation);
		Matrix rotationMatrix = RotationUtilities.createRotationMatrix(eulerAngleRotation);
		Velocity vel = new Velocity(initialSpeedInMetersPerSecond,0,0);
		ThreeDimensionalSpaceVector newVelocityVector = RotationUtilities.changeToInertialFrame(Velocity.ToThreeDimensionalSpaceVector(vel), rotationMatrix);
		
		Velocity newVelocity = new Velocity(newVelocityVector);
		double rfidTagEventCountDown = 0;
		
		double currentX = initialPosition.getX();
		double currentY = initialPosition.getY();
		
		double speedX = initialSpeedInMetersPerSecond*Math.cos(orientation);
		double speedY = initialSpeedInMetersPerSecond*Math.sin(orientation);
		
		double accOffX = accKineticFrictionOffset*Math.cos(orientation);
		double accOffY = accKineticFrictionOffset*Math.sin(orientation);
		double accX = accelerationInMetersPerSecondSquared*Math.cos(orientation);
		double accY = accelerationInMetersPerSecondSquared*Math.sin(orientation);
		
	    for(int i = 0; i < numberOfSeconds; i++){
	    	PositionTestSample positionTestSample = new PositionTestSample();
	    	GyroscopeMeasurement gyrMeasurement = new GyroscopeMeasurement(0, 0, 0, 1, timeMeasured);
	    	AccelerometerMeasurement accMeasurement = new AccelerometerMeasurement(new Acceleration(accX+accOffX, accY+accOffY, 0), 1, timeMeasured);

	    	positionTestSample.setAccelerometerMeasurement(accMeasurement);
	    	positionTestSample.setGyroscopeMeasurement(gyrMeasurement);

	    	
	    	//t=1
	    	speedX = accX*1 + speedX;
	    	currentX = (speedX*1)*UnitConversionUtilities.MetersToInches + currentX;
	    	speedY = accY*1 + speedY;
	    	currentY = (speedY*1)*UnitConversionUtilities.MetersToInches + currentY;
	    	
			ValueUpdate<Coordinate> rfidTagPosition = null;
			ValueUpdate<Coordinate> expectedPosition = new ValueUpdate<Coordinate>(new Coordinate(currentX, currentY, 0), timeMeasured);
	    	RfidTagDetectedNotification rfidTagDetectionNotification = null;
	    	
	    	if(rfidTagEventCountDown == 0)
	    	{
	    		rfidTagPosition = new ValueUpdate<Coordinate>(new Coordinate(currentX, currentY, 0), timeMeasured);
	    		rfidTagEventCountDown = numberOfMeasurementsBetweenRfidTagNotifications;
	    		System.out.printf("x: %f, y:%f, z:%f\n", rfidTagPosition.getValue().getX(), rfidTagPosition.getValue().getY(), rfidTagPosition.getValue().getZ());
	    	}
	    	else
	    	{
	    		rfidTagEventCountDown--;
	    	}
	    	
	    	positionTestSample.setRfidTagDetectedNotification(rfidTagDetectionNotification);
	    	positionTestSample.setRfidTagPosition(rfidTagPosition);
	    	positionTestSample.setExpectedPosition(expectedPosition);
	    	
/*	    	//t=1
	    	speedX = accX*1 + speedX;
	    	currentX = (speedX*1)*UnitConversionUtilities.MetersToInches + currentX;
	    	speedY = accY*1 + speedY;
	    	currentY = (speedY*1)*UnitConversionUtilities.MetersToInches + currentY; */
	    	
	    	timeMeasured = (Calendar) timeMeasured.clone();
	    	timeMeasured.add(Calendar.SECOND, 1);
	    	samples.add(positionTestSample);
	    }
	    
		
		return straightLineTestCase;
	}

}
