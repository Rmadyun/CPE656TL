package TrainTrax;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;



public class TestNavigationProgram {

	private static void TestSampleRotation(){
		List<GyroscopeMeasurement> measurements = new ArrayList<GyroscopeMeasurement>();
		EulerAngleRotation initialBodyFrameOrientation = new EulerAngleRotation(0,0,0);	
		Calendar timeMeasured = Calendar.getInstance();
		
		int numberOfSamples = 100;
	    double degreeChange = Math.PI/2;
	    double degreeChangePerSample = degreeChange/numberOfSamples;
	    
	    for(int i = 0; i < numberOfSamples; i++){
	    	GyroscopeMeasurement measurement = new GyroscopeMeasurement(degreeChangePerSample, 0, 0, 1, timeMeasured);

	    	measurements.add(measurement);
	    }

		TestGyroscope testGyroscope = new TestGyroscope(measurements);
		RotationMonitor rotationMonitor = new RotationMonitor(testGyroscope, initialBodyFrameOrientation);
		rotationMonitor.AddSubscriber(new GenerationRotationChangeSubscriber(){

			@Override
			public void OrientationChanged(
					EulerAngleRotation newBodyFrameOrientation) {
				
				super.OrientationChanged(newBodyFrameOrientation);
				
				System.out.println(String.format("%f radians around x axis", newBodyFrameOrientation.getRadiansRotationAlongXAxis()));
				System.out.println(String.format("%f radians around y axis", newBodyFrameOrientation.getRadiansRotationAlongYAxis()));
				System.out.println(String.format("%f radians around z axis", newBodyFrameOrientation.getRadiansRotationAlongZAxis()));
			}
			
		});
		
		EulerAngleRotation lastReportedTotalBodyFrameRotation = null;
		for(int i = 0; i < numberOfSamples; i++){
			lastReportedTotalBodyFrameRotation = rotationMonitor.waitForNextRotationUpdate();
		}
		
		boolean testPassed = true;
		double tolerance = 0.1;
		
		testPassed = testPassed && (lastReportedTotalBodyFrameRotation != null)&&(Math.abs(lastReportedTotalBodyFrameRotation.getRadiansRotationAlongXAxis() - degreeChange) < tolerance);
		
		if(testPassed)
		{
			System.out.println("Test Passed!");
		}
		else{
			System.out.println("Test Failed!");
		}

	}
	
	private static void PrintRotation(EulerAngleRotation rotation){

		System.out.println(String.format("%f radians around x axis", rotation.getRadiansRotationAlongXAxis()));
		System.out.println(String.format("%f radians around y axis", rotation.getRadiansRotationAlongYAxis()));
		System.out.println(String.format("%f radians around z axis", rotation.getRadiansRotationAlongZAxis()));
		
	}
	
	private static void TestMeasurementCsv(){

		final String filename = "/home/death/Documents/CPE656/fullRotation_Nexus_7_09_18_15.csv";
		GyroscopeReader gyroscopeReader = new GyroscopeReader(filename);
		List<GyroscopeMeasurement> measurements = gyroscopeReader.getGyroscopeMeasurements();
		//EulerAngleRotation initialBodyFrameOrientation = new EulerAngleRotation(Math.PI/2,0,0);		
		EulerAngleRotation initialBodyFrameOrientation = new EulerAngleRotation(0,Math.PI/3, 0);

		final int numberOfSamples = measurements.size();
		TestGyroscope testGyroscope = new TestGyroscope(measurements);
		RotationMonitor rotationMonitor = new RotationMonitor(testGyroscope, initialBodyFrameOrientation);
		rotationMonitor.AddSubscriber(new GenerationRotationChangeSubscriber(){

			@Override
			public void OrientationChanged(
					EulerAngleRotation newBodyFrameOrientation) {
				
				super.OrientationChanged(newBodyFrameOrientation);
				
				PrintRotation(newBodyFrameOrientation);
			}
			
		});
		
		EulerAngleRotation lastReportedTotalBodyFrameRotation = null;
		for(int i = 0; i < numberOfSamples; i++){
			lastReportedTotalBodyFrameRotation = rotationMonitor.waitForNextRotationUpdate();
			
			//Adjust rotation to inertial frame
			EulerAngleRotation inertialFrameRotation = RotationUtilities.convertRotationFromBodyFrameToNedFrame(lastReportedTotalBodyFrameRotation, initialBodyFrameOrientation);
			
			System.out.println(String.format("%f radians around Earth x axis", inertialFrameRotation.getRadiansRotationAlongXAxis()));
			System.out.println(String.format("%f radians around Earth y axis", inertialFrameRotation.getRadiansRotationAlongYAxis()));
			System.out.println(String.format("%f radians around Earth z axis", inertialFrameRotation.getRadiansRotationAlongZAxis()));
			
			Quat4d initialRotationQuaternion = RotationUtilities.convertFromEulerAngleToQuaternion(initialBodyFrameOrientation.getRadiansRotationAlongXAxis(),
					initialBodyFrameOrientation.getRadiansRotationAlongYAxis(),
					initialBodyFrameOrientation.getRadiansRotationAlongZAxis());
			
			Quat4d bodyFrameRotationQuaternion = RotationUtilities.convertFromEulerAngleToQuaternion(inertialFrameRotation.getRadiansRotationAlongXAxis(),
					inertialFrameRotation.getRadiansRotationAlongYAxis(),
					inertialFrameRotation.getRadiansRotationAlongZAxis());
			
			Quat4d compositeRotationQuaternion = initialRotationQuaternion.multiply(bodyFrameRotationQuaternion);
			
			System.out.println("Estimate Earth orientation by attempting to use only quaternions");
			
			EulerAngleRotation compositeEulerAngleRotation = RotationUtilities.convertFromQuaternionToEulerAngle(compositeRotationQuaternion);
			
			PrintRotation(compositeEulerAngleRotation);
		}
		
		boolean testPassed = true;
		double tolerance = 0.1;
		
		
		//testPassed = testPassed && (lastReportedTotalBodyFrameRotation != null)&&(Math.abs(lastReportedTotalBodyFrameRotation.getRadiansRotationAlongXAxis() - degreeChange) < tolerance);
		
		if(testPassed)
		{
			System.out.println("Test Passed!");
		}
		else{
			System.out.println("Test Failed!");
		}

	}
	
	public static void TestQuaternionMultiplication(){
		Quat4d quat4d = new Quat4d(1,0,1,0);
		Quat4d quat4d2 = new Quat4d(1,0.5,0.5, 0.75);
		Quat4d expectedProduct = new Quat4d(0.5, 1.25, 1.5, 0.25);
		Quat4d prod = quat4d.multiply(quat4d2);
		
		boolean areEqual = prod.equals(expectedProduct);
		
		if(areEqual)
			System.out.println("Are Equal");
		else
			System.out.println("Are NOT Equal");		
	}
	
	
	private static void TestRotationOfVectorUsingEulerAngleDerivedRotationMatrix(){
		Matrix testVector = new Matrix(3,1);
		testVector.setValue(0, 0, 0);
		testVector.setValue(1, 0, 1);
		testVector.setValue(2, 0, 0);
		
		Matrix rotationMatrix = RotationUtilities.createRotationMatrix(new EulerAngleRotation(Math.PI/2, 0, 0));
		
		Matrix expectedRotatedVector = new Matrix(3, 1);
		expectedRotatedVector.setValue(0, 0, 0); //x
		expectedRotatedVector.setValue(1, 0, 0); //y
		expectedRotatedVector.setValue(2, 0, 1); //z
		
		//Assumptions:
		//Counter clockwise rotations are positive
		//Check using the NED Local Earth Reference Frame
		//Heading North is Positive
		//Heading East is Positive
		//Heading into the Earth is Positive (So going in the airTestMeasurementCsv is negative)
		
		Matrix rotatedVector =  rotationMatrix.multiply(testVector).round();
		
		NavigationEngine.PrintMatrix(rotatedVector);
		
		boolean coordinateTransformationIsCorrect = rotatedVector.round().equals(expectedRotatedVector.round());
		
		if(coordinateTransformationIsCorrect){
			System.out.println("Transformation of coordinate axises are correct!");
		}
		else{
			System.out.println("ERROR: Transformation of coordinate axises are WRONG!");
		}
	}
	
	public static void Assert(boolean condition) throws Exception{
		if(!condition){
			
			throw new Exception("Test Failed");
		}
	}
	
	public static void TestLinearInterpolation(){
		
		boolean testPassed = true;
		double x[] = {0, 1};
		double y[] = {0, 1};
		
		try{
			double interpolatedY;
			
			//Test Beyond Upper Bound
			interpolatedY = MathUtilities.LinearInterpolate(x, y,2);
			Assert(interpolatedY == 2);
			
			//Test At Upper Bound
			interpolatedY = MathUtilities.LinearInterpolate(x, y,1);
			Assert(interpolatedY == 1);
			
			//Test At Lower Bound
			interpolatedY = MathUtilities.LinearInterpolate(x, y,0);
			Assert(interpolatedY == 0);
			
			//Test Below Lower Bound
			interpolatedY = MathUtilities.LinearInterpolate(x, y,-1);
			Assert(interpolatedY == -1);
			
			//Test Between Bounds
			interpolatedY = MathUtilities.LinearInterpolate(x, y,0.5);
			Assert(interpolatedY == 0.5);
			
			//Test Single Point Interpolate
			double x1[] = {0};
			double y1[] = {0};
			interpolatedY = MathUtilities.LinearInterpolate(x1, y1, 1);
			Assert(interpolatedY == 0);
		}
		catch (Exception e){
			testPassed = false;
		}
		
		
		if(testPassed){
			System.out.println("Test Passed");
		}
		else{
			System.out.println("Test Failed");
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Quat4d initialInertialFrameOrientation = RotationUtilities.convertFromEulerAngleToQuaternion(0, 0, Math.PI/2);
		Quat4d bodyFrameToInertialFrameQuaternion = initialInertialFrameOrientation.inverse();
		Quat4d initialBodyFrameOrientation = initialInertialFrameOrientation.multiply(initialInertialFrameOrientation.inverse());
		Quat4d finalBodyFrameOrientation = RotationUtilities.convertFromEulerAngleToQuaternion(0, Math.PI/2, 0);
		Quat4d finalInertialFrameOrientation = finalBodyFrameOrientation.multiply(bodyFrameToInertialFrameQuaternion);
		
		//System.out.println("Calculate final inertial frame orientation");
		//PrintRotation(RotationMonitor.convertFromQuaternionToEulerAngle(finalInertialFrameOrientation));
		
		//TestRotationOfVectorUsingEulerAngleDerivedRotationMatrix();
		//TestLinearInterpolation();
		
		TestMeasurementCsv();
		
		/*EulerAngleRotation rotation = RotationMonitor.convertFromQuaternionToEulerAngle(prod);
		
		PrintRotation(rotation); */
	
		//TestMeasurementCsv();		
	}

}
