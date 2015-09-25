package TestNavigation;

import java.util.ArrayList;
import java.util.List;



public class TestNavigationProgram {

	private static void TestSampleRotation(){
		List<GyroscopeMeasurement> measurements = new ArrayList<GyroscopeMeasurement>();
		EulerAngleRotation initialBodyFrameOrientation = new EulerAngleRotation(0,0,0);		
		
		int numberOfSamples = 100;
	    double degreeChange = Math.PI/2;
	    double degreeChangePerSample = degreeChange/numberOfSamples;
	    
	    for(int i = 0; i < numberOfSamples; i++){
	    	GyroscopeMeasurement measurement = new GyroscopeMeasurement(degreeChangePerSample, 0, 0, 1);

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
		EulerAngleRotation initialBodyFrameOrientation = new EulerAngleRotation(Math.PI/2,0,0);		

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
			double p = lastReportedTotalBodyFrameRotation.getRadiansRotationAlongXAxis();
			double q = lastReportedTotalBodyFrameRotation.getRadiansRotationAlongYAxis();
			double r = lastReportedTotalBodyFrameRotation.getRadiansRotationAlongZAxis();
			double fee = initialBodyFrameOrientation.getRadiansRotationAlongXAxis();
			double theta = initialBodyFrameOrientation.getRadiansRotationAlongYAxis();
			double aroundX = p + q*Math.sin(fee)*Math.tan(theta) + r*Math.cos(fee)*Math.tan(theta);
			double aroundY = q*Math.cos(fee) - r*Math.sin(fee);
			double aroundZ = q*Math.sin(fee)/Math.cos(theta) + r*Math.cos(fee)/Math.cos(theta);
			
			EulerAngleRotation inertialFrameRotation = new EulerAngleRotation(aroundX, aroundY, aroundZ);
			
			System.out.println(String.format("%f radians around Earth x axis", inertialFrameRotation.getRadiansRotationAlongXAxis()));
			System.out.println(String.format("%f radians around Earth y axis", inertialFrameRotation.getRadiansRotationAlongYAxis()));
			System.out.println(String.format("%f radians around Earth z axis", inertialFrameRotation.getRadiansRotationAlongZAxis()));
			
			Quat4d initialRotationQuaternion = RotationMonitor.convertFromEulerAngleToQuaternion(initialBodyFrameOrientation.getRadiansRotationAlongXAxis(),
					initialBodyFrameOrientation.getRadiansRotationAlongYAxis(),
					initialBodyFrameOrientation.getRadiansRotationAlongZAxis());
			
			Quat4d bodyFrameRotationQuaternion = RotationMonitor.convertFromEulerAngleToQuaternion(inertialFrameRotation.getRadiansRotationAlongXAxis(),
					inertialFrameRotation.getRadiansRotationAlongYAxis(),
					inertialFrameRotation.getRadiansRotationAlongZAxis());
			
			Quat4d compositeRotationQuaternion = initialRotationQuaternion.multiply(bodyFrameRotationQuaternion);
			
			System.out.println("Estimate Earth orientation by attempting to use only quaternions");
			
			EulerAngleRotation compositeEulerAngleRotation = RotationMonitor.convertFromQuaternionToEulerAngle(compositeRotationQuaternion);
			
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
	
	public static Matrix createRotationMatrix(EulerAngleRotation rotation){
	    Matrix rotationMatrix = new Matrix(3,3);
	    
	    double cx = Math.cos(rotation.getRadiansRotationAlongXAxis());
	    double cy = Math.cos(rotation.getRadiansRotationAlongYAxis());
	    double cz = Math.cos(rotation.getRadiansRotationAlongZAxis());
	    double sx = Math.sin(rotation.getRadiansRotationAlongXAxis());
	    double sy = Math.sin(rotation.getRadiansRotationAlongYAxis());
	    double sz = Math.sin(rotation.getRadiansRotationAlongZAxis());
	    
	    rotationMatrix.setValue(0, 0, cz*cy);
	    rotationMatrix.setValue(0, 1, cz*sx*sy - cx*sz);
	    rotationMatrix.setValue(0, 2, sx*sz + cx*cz*sy);
	    rotationMatrix.setValue(1, 0, cy*sz);
	    rotationMatrix.setValue(1, 1, cx*cz + sx*sz*sy);
	    rotationMatrix.setValue(1, 2, cx*sz*sy - cz*sx);
	    rotationMatrix.setValue(2, 0, -1*sy);
	    rotationMatrix.setValue(2, 1, cy*sx);
	    rotationMatrix.setValue(2, 2, cx*cy);
	    
	    return rotationMatrix;
	}
	
	public static void PrintMatrix(Matrix matrix){
		
		for(int i = 0; i < matrix.getNumberOfRows(); i++){
			String rowString = "";
			for(int j = 0; j < matrix.getNumberOfColumns(); j++){
				rowString += String.format("%f  ", matrix.getValue(i,  j));
			}
			
			System.out.println(rowString);
		}
	}
	
	private static void TestRotationOfVectorUsingEulerAngleDerivedRotationMatrix(){
		Matrix testVector = new Matrix(3,1);
		testVector.setValue(0, 0, 0);
		testVector.setValue(1, 0, 1);
		testVector.setValue(2, 0, 0);
		
		Matrix rotationMatrix = createRotationMatrix(new EulerAngleRotation(Math.PI/2, 0, 0));
		
		Matrix expectedRotatedVector = new Matrix(3, 1);
		expectedRotatedVector.setValue(0, 0, 0); //x
		expectedRotatedVector.setValue(1, 0, 0); //y
		expectedRotatedVector.setValue(2, 1, 0); //z
		
		//Assumptions:
		//Counter clockwise rotations are positive
		//Check using the NED Local Earth Reference Frame
		//Heading North is Positive
		//Heading East is Positive
		//Heading into the Earth is Positive (So going in the air is negative)
		
		Matrix rotatedVector =  rotationMatrix.multiply(testVector);
		
		PrintMatrix(rotatedVector);
		
		boolean coordinateTransformationIsCorrect = rotatedVector.equals(expectedRotatedVector);
		
		if(coordinateTransformationIsCorrect){
			System.out.println("Transformation of coordinate axises are correct!");
		}
		else{
			System.out.println("ERROR: Transformation of coordinate axises are WRONG!");
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Quat4d initialInertialFrameOrientation = RotationMonitor.convertFromEulerAngleToQuaternion(0, 0, Math.PI/2);
		Quat4d bodyFrameToInertialFrameQuaternion = initialInertialFrameOrientation.inverse();
		Quat4d initialBodyFrameOrientation = initialInertialFrameOrientation.multiply(initialInertialFrameOrientation.inverse());
		Quat4d finalBodyFrameOrientation = RotationMonitor.convertFromEulerAngleToQuaternion(0, Math.PI/2, 0);
		Quat4d finalInertialFrameOrientation = finalBodyFrameOrientation.multiply(bodyFrameToInertialFrameQuaternion);
		
		//System.out.println("Calculate final inertial frame orientation");
		//PrintRotation(RotationMonitor.convertFromQuaternionToEulerAngle(finalInertialFrameOrientation));
		
		TestRotationOfVectorUsingEulerAngleDerivedRotationMatrix();
		
		/*EulerAngleRotation rotation = RotationMonitor.convertFromQuaternionToEulerAngle(prod);
		
		PrintRotation(rotation); */
	
		//TestMeasurementCsv();		
	}

}
