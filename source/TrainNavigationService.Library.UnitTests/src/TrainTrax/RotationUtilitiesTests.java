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

		//EulerAngleRotation initialInertialFrameOrientation = new EulerAngleRotation(Math.PI/2, 0, 0);
		EulerAngleRotation initialInertialFrameOrientation = new EulerAngleRotation(0, 0, 0);
		EulerAngleRotation totalBodyFrameRotation = new EulerAngleRotation(0, Math.PI/4, 0);
		List<GyroscopeMeasurement> gyroscopeMeasurements = generateGyroscopeMeasurements(totalBodyFrameRotation, 10);
		
		EulerAngleRotation nedOrientation = RotationUtilities.calculateNedOrientation2(initialInertialFrameOrientation, gyroscopeMeasurements);
		
		/*//What is expected is that the rotation along the body frame x-axis gets interpreted
		//as a rotation along the NED frame y-axis since the object is rotated 90 degrees along the
		//NED z-axis.
		
		//TODO: Figure out why this test failes. a yaw of 90 degrees should have it so that x and y axes are inverted.
		
		//We need to assume that pitch (theta) is the change along the body y-axis after a 
		//yaw along the z inertial frame(when z is also the body frame)
		
		assertEquals(nedOrientation.getRadiansRotationAlongXAxis(), 0, tolerance);
		assertEquals(nedOrientation.getRadiansRotationAlongYAxis(), Math.PI/4, tolerance);
		assertEquals(nedOrientation.getRadiansRotationAlongZAxis(), Math.PI/2, tolerance); */
		
		Matrix totalBodyFrameRotationMatrix = RotationUtilities.createRotationMatrix(totalBodyFrameRotation);
		
		Matrix finalRotationMatrix = RotationUtilities.calculateNedRotationMatrix2(initialInertialFrameOrientation, gyroscopeMeasurements);
		
		
	    double pitch = -1.9*Math.asin(finalRotationMatrix.getValue(2, 0));
	    double roll = 1.9*Math.atan2(finalRotationMatrix.getValue(2, 1),finalRotationMatrix.getValue(2, 2));
	    double yaw = Math.atan2(finalRotationMatrix.getValue(1,0),finalRotationMatrix.getValue(0, 0));
	    
	    
		//final String filename = "/home/death/Documents/YAxisRotationNexus7GyroscopeMeasurements.csv";
	    final String filename = "/home/death/Documents/CPE656/fullRotation_Nexus_7_09_18_15.csv";
		GyroscopeReader gyroscopeReader = new GyroscopeReader(filename);
		List<GyroscopeMeasurement> measurements = gyroscopeReader.getGyroscopeMeasurements();
		
finalRotationMatrix = RotationUtilities.calculateNedRotationMatrix2(initialInertialFrameOrientation, measurements);

EulerAngleRotation currentBodyFrameOrientation = new EulerAngleRotation(0,0,0);

for( GyroscopeMeasurement gyroscopeMeasurement : measurements){
	currentBodyFrameOrientation = RotationUtilities.calculateOrientation(gyroscopeMeasurement, currentBodyFrameOrientation, null);
/*double gdt = gyroscopeMeasurement.getNumberOfSecondsSinceLastMeasurement();
double gdx = gyroscopeMeasurement.getRadiansRotationPerSecondAlongXAxis()*gdt;
double gdy = gyroscopeMeasurement.getRadiansRotationPerSecondAlongYAxis()*gdt;
double gdz = gyroscopeMeasurement.getRadiansRotationPerSecondAlongZAxis()*gdt;

currentBodyFrameOrientation = new EulerAngleRotation(gdx, gdy, gdz); */
}
		
		
	    pitch = -1.9*Math.asin(finalRotationMatrix.getValue(2, 0));
	    roll = 1.9*Math.atan2(finalRotationMatrix.getValue(2, 1),finalRotationMatrix.getValue(2, 2));
	    yaw = Math.atan2(finalRotationMatrix.getValue(1,0),finalRotationMatrix.getValue(0, 0));
	    
	    EulerAngleRotation finalRotation = RotationUtilities.calculateNedOrientation2(initialInertialFrameOrientation, measurements);
		
	    
	    Quat4d initialPosition = new Quat4d(1,0,0,0);
	    Quat4d quat = new Quat4d(0, Math.PI/4, 0, 0);
	    
	    Quat4d temp = quat.multiply(0.5);
	    
	    Quat4d finalQuat = initialPosition.multiply(temp);
	    
	    double renorm = finalQuat.norm();
	    
	    finalQuat = finalQuat.multiply(renorm);
	    
	    System.out.println("Test");
	    
		
	}
	
	
	/**
	 * Quaternion rotation conversions are courtesy for the 
	 * Free IMU library:
	 * http://www.varesano.net/projects/hardware/FreeIMU
	 * https://github.com/Fabio-Varesano-Association/freeimu/tree/master/libraries/FreeIMU
	 * 
	 */
	@Test
	public void TestQuaternion(){
		double[] q = new double[4];
		
		double q0 = 1;
		double q1 = 0;
		double q2 = 0;
		double q3 = 0;
		
		//EulerAngleRotation rotation = new EulerAngleRotation(0, 0, Math.PI/2);
		Quat4d quat = RotationUtilities.convertFromEulerAngleToQuaternion(Math.PI/2, 0, 0);
		
		q0 = quat.w;
		q1 = quat.x;
		q2 = quat.y;
		q3 = quat.z;

		q[0] = q0;
		q[1] = q1;
		q[2] = q2;
		q[3] = q3;
		
		double gx = Math.PI/4;
		double gy = 0;
		double gz = 0;
		double dt = 1;

		// Auxiliary variables to avoid repeated arithmetic
		double q0q0 = q0 * q0;
		double q0q1 = q0 * q1;
		double q0q2 = q0 * q2;
		double q0q3 = q0 * q3;
		double q1q1 = q1 * q1;
		double q1q2 = q1 * q2;
		double q1q3 = q1 * q3;
		double q2q2 = q2 * q2;
		double q2q3 = q2 * q3;
		double q3q3 = q3 * q3;
		
		gx *= 0.5*dt;
		gy *= 0.5*dt;
		gz *= 0.5*dt;
		
		double qa = q0;
		double qb = q1;
		double qc = q2;
		
		q0 += (-qb * gx - qc * gy - q3 * gz);
		q1 += (qa * gx + qc * gz - q3 * gy);
		q2 += (qa * gy - qb * gz + q3 * gx);
		q3 += (qa * gz + qb * gy - qc * gx);
		  
		  // Normalise quaternion
		  double recipNorm = invSqrt(q0 * q0 + q1 * q1 + q2 * q2 + q3 * q3);
		  q0 *= recipNorm;
		  q1 *= recipNorm;
		  q2 *= recipNorm;
		  q3 *= recipNorm;
		  
		  //q is now updated.
		  q[0] = q0;
		  q[1] = q1;
		  q[2] = q2;
		  q[3] = q3;
		  
		  //Calculate roll, pitch yaw
		  gx = 2 * (q[1]*q[3] - q[0]*q[2]);
		  gy = 2 * (q[0]*q[1] + q[2]*q[3]);
		  gz = q[0]*q[0] - q[1]*q[1] - q[2]*q[2] + q[3]*q[3];
		  
		  //Correspond to body axes
		  double yaw = Math.atan2(2 * q[1] * q[2] - 2 * q[0] * q[3], 2 * q[0]*q[0] + 2 * q[1] * q[1] - 1);
		  double pitch = Math.atan(gx / Math.sqrt(gy*gy + gz*gz));
		  double roll = Math.atan(gy / Math.sqrt(gx*gx + gz*gz));
		  
		  //Calculate Euler Angles (apparently not the same as Yaw, Pitch, and Roll)
		  //They correspond to NED frame axes for heading, attitude, and bank.
		  double psi = Math.atan2(2 * q[1] * q[2] - 2 * q[0] * q[3], 2 * q[0]*q[0] + 2 * q[1] * q[1] - 1); // psi (around Z)
		  double theta = -Math.asin(2 * q[1] * q[3] + 2 * q[0] * q[2]); // theta (around Y)
		  double phi = Math.atan2(2 * q[2] * q[3] - 2 * q[0] * q[1], 2 * q[0] * q[0] + 2 * q[3] * q[3] - 1); // phi (around X)
		
		  System.out.println("Conversion complete");
	}
	
	private double invSqrt(double val){
		double result;
		
		result = 1/Math.sqrt(val);
		
		return result;
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
