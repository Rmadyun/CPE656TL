package TestNavigation;



public class NavigationEngine {
	
	private Matrix rotationMatrix;
	
	
	public NavigationEngine(){
		
	}
	
	public static AccelerometerMeasurement changeToInertialFrame(AccelerometerMeasurement measurement, Matrix bodyFrameToInertialFrameRotationMatrix){
		AccelerometerMeasurement acceleration;
		Matrix accelerationVector = new Matrix(3,1);
		
		accelerationVector.setValue(0, 0,  measurement.getMetersPerSecondSquaredAlongXAxis());
		accelerationVector.setValue(1, 0,  measurement.getMetersPerSecondSquaredAlongYAxis());
		accelerationVector.setValue(2, 0,  measurement.getMetersPerSecondSquaredAlongZAxis());
		
		Matrix adjustedAccelerationVector = bodyFrameToInertialFrameRotationMatrix.multiply(accelerationVector).round();
		
		acceleration = new AccelerometerMeasurement(adjustedAccelerationVector.getValue(0, 0),
				adjustedAccelerationVector.getValue(1,0),
				adjustedAccelerationVector.getValue(2, 0),
				measurement.getNumberOfSecondsSinceLastMeasurement());
		
		
		return acceleration;
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

	
};