package TrainTrax;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

public class RotationUtilitiesTests {
	
	private static List<GyroscopeMeasurement> generateGyroscopeMeasurements(EulerAngleRotation rotation, int numberOfSamples){
		List<GyroscopeMeasurement> measurements = new ArrayList<GyroscopeMeasurement>();
		EulerAngleRotation initialBodyFrameOrientation = new EulerAngleRotation(0,0,0);
		Calendar timeMeasured = Calendar.getInstance();

	    double xAngleChangePerSample = rotation.getRadiansRotationAlongXAxis()/numberOfSamples;
	    double yAngleChangePerSample = rotation.getRadiansRotationAlongYAxis()/numberOfSamples;
	    double zAngleChangePerSample = rotation.getRadiansRotationAlongZAxis()/numberOfSamples;
	    
	    for(int i = 0; i < numberOfSamples; i++){

	    	GyroscopeMeasurement measurement = new GyroscopeMeasurement(xAngleChangePerSample, yAngleChangePerSample, zAngleChangePerSample, 1, timeMeasured);

	    	timeMeasured.add(Calendar.SECOND, 1);
	    	measurements.add(measurement);
	    }
	    
	    return measurements;
	}

	@Test
	public void testCalculateNedOrientationWhenRotationAlongXAxis() {		
        double tolerance = 0.05;

		EulerAngleRotation initialInertialFrameOrientation = new EulerAngleRotation(Math.PI/2, 0, 0);
		EulerAngleRotation totalBodyFrameRotation = new EulerAngleRotation(0, Math.PI/4, 0);
		List<GyroscopeMeasurement> gyroscopeMeasurements = generateGyroscopeMeasurements(totalBodyFrameRotation, 10);
		
		EulerAngleRotation nedOrientation = RotationUtilities.calculateNedOrientation(initialInertialFrameOrientation, gyroscopeMeasurements);
		
		//What is expected is that the rotation along the body frame y-axis gets interpreted
		//as a rotation along the NED frame z-axis since the object is rotated 90 degrees along the
		//NED X-axis.
		
		assertEquals(nedOrientation.getRadiansRotationAlongXAxis(), Math.PI/2, tolerance);
		assertEquals(nedOrientation.getRadiansRotationAlongYAxis(), 0, tolerance);
		assertEquals(nedOrientation.getRadiansRotationAlongZAxis(), Math.PI/4, tolerance);
	}
	
	@Test
	public void testCalculateNedOrientationWhenRotationAlongYAxis() {		
        double tolerance = 0.05;

		EulerAngleRotation initialInertialFrameOrientation = new EulerAngleRotation(0, Math.PI, 0);
		EulerAngleRotation totalBodyFrameRotation = new EulerAngleRotation(0, Math.PI/4, 0);
		List<GyroscopeMeasurement> gyroscopeMeasurements = generateGyroscopeMeasurements(totalBodyFrameRotation, 10);
		
		EulerAngleRotation nedOrientation = RotationUtilities.calculateNedOrientation(initialInertialFrameOrientation, gyroscopeMeasurements);
		
		//What is expected is that the rotation along the body frame y-axis gets interpreted
		//as a rotation along the NED frame z-axis since the object is rotated 90 degrees along the
		//NED X-axis.
		
		assertEquals(nedOrientation.getRadiansRotationAlongXAxis(), 0, tolerance);
		assertEquals(nedOrientation.getRadiansRotationAlongZAxis(), 0, tolerance);
		assertEquals(nedOrientation.getRadiansRotationAlongYAxis(), -Math.PI/4, tolerance);
	}
	
	@Test
	public void testCalculateNedOrientationWhenRotationAlongZAxis() {		
        double tolerance = 0.05;

		EulerAngleRotation initialInertialFrameOrientation = new EulerAngleRotation(0, 0, Math.PI/2);
		EulerAngleRotation totalBodyFrameRotation = new EulerAngleRotation(Math.PI/4, 0, 0);
		List<GyroscopeMeasurement> gyroscopeMeasurements = generateGyroscopeMeasurements(totalBodyFrameRotation, 10);
		
		EulerAngleRotation nedOrientation = RotationUtilities.calculateNedOrientation(initialInertialFrameOrientation, gyroscopeMeasurements);
		
		//What is expected is that the rotation along the body frame x-axis gets interpreted
		//as a rotation along the NED frame y-axis since the object is rotated 90 degrees along the
		//NED z-axis.
		
		//TODO: Figure out why this test failes. a yaw of 90 degrees should have it so that x and y axes are inverted.
		
		//We need to assume that pitch (theta) is the change along the body y-axis after a 
		//yaw along the z inertial frame(when z is also the body frame)
		
		assertEquals(nedOrientation.getRadiansRotationAlongXAxis(), 0, tolerance);
		assertEquals(nedOrientation.getRadiansRotationAlongYAxis(), Math.PI/4, tolerance);
		assertEquals(nedOrientation.getRadiansRotationAlongZAxis(), Math.PI/2, tolerance);
	}
	
	@Test
	public void testCalculateNedOrientationWhenGimbalLockPresent() {		
        EulerAngleRotation initialInertialFrameOrientation = new EulerAngleRotation(0, Math.PI/2, 0);
		EulerAngleRotation totalBodyFrameRotation = new EulerAngleRotation(0, Math.PI/4, 0);
		List<GyroscopeMeasurement> gyroscopeMeasurements = generateGyroscopeMeasurements(totalBodyFrameRotation, 10);
		

		//Test gimbal lock scenario
		
		try{
			RotationUtilities.calculateNedOrientation(initialInertialFrameOrientation, gyroscopeMeasurements);
			fail("Exception not thrown calculating orientation in gimbal lock region.");
		}
		catch(IllegalArgumentException exception){
			
		}
		
		
	}

}
