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
	public void testCalculateNedOrientation() {		

		EulerAngleRotation initialInertialFrameOrientation = new EulerAngleRotation(0, Math.PI/2, 0);
		EulerAngleRotation totalBodyFrameRotation = new EulerAngleRotation(Math.PI/4, 0, 0);
		List<GyroscopeMeasurement> gyroscopeMeasurements = generateGyroscopeMeasurements(totalBodyFrameRotation, 10);
		
		EulerAngleRotation nedOrientation = RotationUtilities.calculateNedOrientation(initialInertialFrameOrientation, gyroscopeMeasurements);
		
		//What is expected is that the rotation along the body frame x-axis gets interpreted
		//as a rotation along the NED frame z-axis since the object is rotated 90 degrees along the
		//NED Y-axis.
		assertTrue(nedOrientation.getRadiansRotationAlongZAxis() != 0);
	}

}
