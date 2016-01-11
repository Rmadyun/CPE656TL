package TrainTrax;

/**
 * Helper class for frequently performed operations involving rotating
 * objects
 * @author Corey Sanders
 *
 */
public class RotationUtilities {

	/**
	 * Converts a vector from the body frame to the inertial frame
	 * @param bodyFrameVector
	 * @param bodyFrameToInertialFrameRotationMatrix
	 * @return
	 */
	public static ThreeDimensionalSpaceVector changeToInertialFrame(ThreeDimensionalSpaceVector bodyFrameVector, Matrix bodyFrameToInertialFrameRotationMatrix){
		ThreeDimensionalSpaceVector inertialFrameVector;
		Matrix accelerationVector = new Matrix(3,1);
		
		accelerationVector.setValue(0, 0,  bodyFrameVector.getX());
		accelerationVector.setValue(1, 0,  bodyFrameVector.getY());
		accelerationVector.setValue(2, 0,  bodyFrameVector.getZ());
		
		Matrix adjustedAccelerationVector = bodyFrameToInertialFrameRotationMatrix.multiply(accelerationVector).round();
		
		inertialFrameVector = new ThreeDimensionalSpaceVector(adjustedAccelerationVector.getValue(0, 0),
				adjustedAccelerationVector.getValue(1,0),
				adjustedAccelerationVector.getValue(2, 0));
		
		return inertialFrameVector;
	}
	
	/**
	 * Creates a rotation matrix representation of a rotation
	 * @param rotation Euler Angle representation of a rotation to use to create the rotation matrix.
	 * @return Converted rotation matrix representation of a rotation
	 */
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
	
}
