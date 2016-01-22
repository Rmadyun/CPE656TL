package TrainTrax;

import java.util.List;

/**
 * Helper class for frequently performed operations involving rotating
 * objects
 * @author Corey Sanders
 *
 */
public class RotationUtilities {
	
	private final static double ANGULAR_RATE_THRESHOLD = 0.01;
	
	 /**
	  * Method determines the new orientation of an object after a Gyroscope measurement has been observed
	  * This assumes that everything is input according to the same inertial reference frame.
	  * @param measurement Gyroscope reading from a device
	  * @param initialOrientation Initial orientation of the device relative to its body axes.
	  * @param bias Amount offset the incoming measurement by. Assign to null if there is not any bias.
	  * @return Euler angle representation of the new orientation of the device about its body axes.
	  */
	public static EulerAngleRotation calculateOrientation(GyroscopeMeasurement measurement, EulerAngleRotation initialOrientation, GyroscopeMeasurement bias){
		EulerAngleRotation newOrientation = initialOrientation;
		
       if(measurement != null){
       	//Adjust the angular speeds to match Euler orientations
       	//NOTE: This is also assuming that the standard conventions for rotation are used.
       	//counter clockwise turns are positive.
       	
       	GyroscopeMeasurement correctedMeasurement;
       	double rotatedX, rotatedY, rotatedZ;
       	
       	if(bias == null)
       	{
           	rotatedX = measurement.getRadiansRotationPerSecondAlongXAxis();
           	rotatedY = measurement.getRadiansRotationPerSecondAlongYAxis();
           	rotatedZ = measurement.getRadiansRotationPerSecondAlongZAxis();
       	}
       	else{
       		rotatedX = (measurement.getRadiansRotationPerSecondAlongXAxis() - bias.getRadiansRotationPerSecondAlongXAxis());
       		rotatedY = (measurement.getRadiansRotationPerSecondAlongYAxis() - bias.getRadiansRotationPerSecondAlongYAxis());
       		rotatedZ = (measurement.getRadiansRotationPerSecondAlongZAxis() - bias.getRadiansRotationPerSecondAlongZAxis());	        	
       	}
       	
       	if(rotatedX < ANGULAR_RATE_THRESHOLD){
       		rotatedX = 0;
       	}
       	
       	if(rotatedY < ANGULAR_RATE_THRESHOLD){
       		rotatedY = 0;
       	}
       	
       	if(rotatedZ < ANGULAR_RATE_THRESHOLD){
       		rotatedZ = 0;
       	}
       	
       	correctedMeasurement = new GyroscopeMeasurement(rotatedX, rotatedY, rotatedZ, measurement.getNumberOfSecondsSinceLastMeasurement(), measurement.getTimeMeasured());
       	
       	//Convert measurement into a quaternion
       	Quat4d deltaQuaternion = convertToQuaternion(correctedMeasurement);
       	
    	   //Calculate the rotation
       	EulerAngleRotation deltaRotationAsEulerAngles = convertFromQuaternionToEulerAngle(deltaQuaternion);
    	   
    	   double deltaRotationAroundXAxis;
    	   double deltaRotationAroundYAxis;
    	   double deltaRotationAroundZAxis;

    	   deltaRotationAroundXAxis = deltaRotationAsEulerAngles.getRadiansRotationAlongXAxis(); 
    	   deltaRotationAroundYAxis = deltaRotationAsEulerAngles.getRadiansRotationAlongYAxis();
    	   deltaRotationAroundZAxis = deltaRotationAsEulerAngles.getRadiansRotationAlongZAxis();
    	   
    	   //Assuming that the effect of the angular velocity of the Earth's rotation is negligible.
    	   double xAxisRotation = initialOrientation.getRadiansRotationAlongXAxis();
    	   double yAxisRotation = initialOrientation.getRadiansRotationAlongYAxis();
    	   double zAxisRotation = initialOrientation.getRadiansRotationAlongZAxis();

    	   xAxisRotation += deltaRotationAroundXAxis;
    	   yAxisRotation += deltaRotationAroundYAxis;
    	   zAxisRotation += deltaRotationAroundZAxis;
    	   
    	   newOrientation = new EulerAngleRotation(xAxisRotation, yAxisRotation, zAxisRotation);
       }		
		
       return newOrientation;
	}
	
	///Converts Quaternions into Euler angles for the standard Earth reference frame.
	///Courtesy of http://www.euclideanspace.com/maths/geometry/rotations/conversions/quaternionToEuler/
	public final static EulerAngleRotation convertFromQuaternionToEulerAngle(Quat4d q1) {
		EulerAngleRotation eulerAngleRotation = null;
		
	    double sqw = q1.w*q1.w;
	    double sqx = q1.x*q1.x;
	    double sqy = q1.y*q1.y;
	    double sqz = q1.z*q1.z;
		double unit = sqx + sqy + sqz + sqw; // if normalized is one, otherwise is correction factor
		double test = q1.x*q1.y + q1.z*q1.w;
		double heading, attitude, bank;
		
		if (test > 0.499*unit) { // singularity at north pole
			heading = 2 * Math.atan2(q1.x,q1.w);
			attitude = Math.PI/2;
			bank = 0;
			
		}
		else if (test < -0.499*unit) { // singularity at south pole
			heading = -2 * Math.atan2(q1.x,q1.w);
			attitude = -Math.PI/2;
			bank = 0;
		}
		else {
	    heading = Math.atan2(2*q1.y*q1.w-2*q1.x*q1.z , sqx - sqy - sqz + sqw);
		attitude = Math.asin(2*test/unit);
		bank = Math.atan2(2*q1.x*q1.w-2*q1.y*q1.z , -sqx + sqy - sqz + sqw);
		}
		
		eulerAngleRotation = new EulerAngleRotation(bank, heading, attitude);
		
		return eulerAngleRotation;
	}
	
	 //convert from Euler Angle NED to quaternion
	 public static final Quat4d convertFromEulerAngleToQuaternion( double bank, double heading, double attitude) {
		    // Assuming the angles are in radians.
		    double c1 = Math.cos(heading);
		    double s1 = Math.sin(heading);
		    double c2 = Math.cos(attitude);
		    double s2 = Math.sin(attitude);
		    double c3 = Math.cos(bank);
		    double s3 = Math.sin(bank);
		    double w = Math.sqrt(1.0 + c1 * c2 + c1*c3 - s1 * s2 * s3 + c2*c3) / 2.0;
		    double w4 = (4.0 * w);
		    double x = (c2 * s3 + c1 * s3 + s1 * s2 * c3) / w4 ;
		    double y = (s1 * c2 + s1 * c3 + c1 * s2 * s3) / w4 ;
		    double z = (-s1 * s3 + c1 * s2 * c3 +s2) / w4 ;
		    
		    return new Quat4d(w,x,y,z);
		  }
	 
	 /**
	  * Converts a measurement of rotation along the object's body frame into
	  * the North, East, Down (NED) Earth-fixed inertial reference frame.
	  * Formal expressed from:
	  * http://www.chrobotics.com/library/understanding-euler-angsin pi/2les
	  * @param currentBodyFrameOrientation
	  * @param initialInertialFrameOrientation The initial orientation of the device body frame coordinate axes relative to the
	  * NED inertial frame axes.
	  * @return Euler Angle representation of the rotation of the target object along the NED inertial reference frame.
	 * @throws IllegalArgumentException Throws exception if the rotation of the device include rotation in the gimbal lock range:
	 * 90 degrees pitch (rotation along the y-axis). 
	  */
	 public static EulerAngleRotation convertRotationFromBodyFrameToNedFrame(EulerAngleRotation currentBodyFrameOrientation, EulerAngleRotation initialInertialFrameOrientation) throws IllegalArgumentException{
		 
		    //Adjust rotation to inertial frame
			double p = currentBodyFrameOrientation.getRadiansRotationAlongXAxis();
			double q = currentBodyFrameOrientation.getRadiansRotationAlongYAxis();
			double r = currentBodyFrameOrientation.getRadiansRotationAlongZAxis();
			double fee = initialInertialFrameOrientation.getRadiansRotationAlongXAxis();
			double theta = initialInertialFrameOrientation.getRadiansRotationAlongYAxis();
			double aroundX = p + q*Math.sin(fee)*Math.tan(theta) + r*Math.cos(fee)*Math.tan(theta);
			double aroundY = q*Math.cos(fee) - r*Math.sin(fee);
			double aroundZ = q*Math.sin(fee)/Math.cos(theta) + r*Math.cos(fee)/Math.cos(theta);
			
		    double tolerance = 0.1;
		    double gimbalLockMin = (Math.PI/2) - tolerance;
		    double gimbalLockMax = (Math.PI/2) + tolerance;
			 
		     if(theta <= gimbalLockMax && theta >= gimbalLockMin){
		    	 throw new IllegalArgumentException("Orientation of the device is in the gimbal lock range: 90 degrees around y-axis");
		     }
			
			EulerAngleRotation currentInertialFrameRotation = new EulerAngleRotation(aroundX, aroundY, aroundZ);
			
			return currentInertialFrameRotation;
	 }
	 

	 /**
	  * Converts a gyroscope measurement into a quaternion representation of the change
	  * in orientation along the measured gyroscope's body frame coordinate system.
	  * Courtesy of http://developer.android.com/guide/topics/sensors/sensors_motion.html
	  * @param measurement Measurement from a given gyroscope to convert
	  * @return Quaternion representation of the change in orientation along the body frame
	  * coordinate system of the gyroscope involved in the measurement.
	  */
		public final static Quat4d convertToQuaternion(GyroscopeMeasurement measurement){
			
			// Axis of the rotation sample, not normalized yet (Axis' are relative to the device reference frame).
		    double axisX = measurement.getRadiansRotationPerSecondAlongXAxis();
		    double axisY = measurement.getRadiansRotationPerSecondAlongYAxis();
		    double axisZ = measurement.getRadiansRotationPerSecondAlongZAxis();
		    double EPSILON = 0; //do adjust the EPSILON to match the actual margin of error

		    // Calculate the angular speed of the sample
		    double omegaMagnitude = Math.sqrt(axisX*axisX + axisY*axisY + axisZ*axisZ);

		    // Normalize the rotation vector if it's big enough to get the axis
		    // (that is, EPSILON should represent your maximum allowable margin of error)
		    if (omegaMagnitude > EPSILON) {
		      axisX /= omegaMagnitude;
		      axisY /= omegaMagnitude;
		      axisZ /= omegaMagnitude;
		    }

		    // Integrate around this axis with the angular speed by the timestep
		    // in order to get a delta rotation from this sample over the timestep
		    // We will convert this axis-angle representation of the delta rotation
		    // into a quaternion before turning it into the rotation matrix.
		    double dT = measurement.getNumberOfSecondsSinceLastMeasurement();
		    double thetaOverTwo = omegaMagnitude * dT / 2.0f;
		    double sinThetaOverTwo = Math.sin(thetaOverTwo);
		    double cosThetaOverTwo = Math.cos(thetaOverTwo);
		    
		    Quat4d quaternion = new Quat4d();
		    quaternion.x = sinThetaOverTwo * axisX;
		    quaternion.y = sinThetaOverTwo * axisY;
		    quaternion.z = sinThetaOverTwo * axisZ;
		    quaternion.w = cosThetaOverTwo;
		    
		    return quaternion;
		}


	/**
	 * Converts a vector from the body frame to an inertial frame
	 * @param bodyFrameVector Vector representation of a measurement taken relative to the body-frame of the object measured
	 * @param bodyFrameToInertialFrameRotationMatrix Rotation matrix to use to convert <code>bodyFrameVector</code> to the
	 * from the body-frame of the object measured to the target inertial reference frame.
	 * @return Vector representation of a measurement relative to a new inertial reference frame.
	 */
	public static ThreeDimensionalSpaceVector changeToInertialFrame(ThreeDimensionalSpaceVector bodyFrameVector, Matrix bodyFrameToInertialFrameRotationMatrix){
		ThreeDimensionalSpaceVector inertialFrameVector;
		Matrix bodyFrameMatrix = new Matrix(3,1);
		
		//Convert body frame vector into a matrix
		bodyFrameMatrix.setValue(0, 0,  bodyFrameVector.getX());
		bodyFrameMatrix.setValue(1, 0,  bodyFrameVector.getY());
		bodyFrameMatrix.setValue(2, 0,  bodyFrameVector.getZ());
		
		//Perform conversion
		Matrix inertialFrameMatrixVector = bodyFrameToInertialFrameRotationMatrix.multiply(bodyFrameMatrix).round();
		
		//Convert inertial frame matrix into a vector
		inertialFrameVector = new ThreeDimensionalSpaceVector(inertialFrameMatrixVector.getValue(0, 0),
				inertialFrameMatrixVector.getValue(1,0),
				inertialFrameMatrixVector.getValue(2, 0));
		
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
	
	/**
	 * Calculates the orientation of an object related to the North-East-Down (NED) Earth-fixed inertial reference frame
	 * @param initialInertialFrameOrientation The initial orientation of the device body frame coordinate axes relative to the
	 * NED inertial frame axes.
	 * @param measurements Collection of measurements from a gyroscope attached to the target object
	 * @return Euler angle representation of the orientation of an object relative to the NED inertial reference frame
	 * @throws IllegalArgumentException  Throws exception if NED transformation cannot be performed.
	 */
	public static EulerAngleRotation calculateNedOrientation(EulerAngleRotation initialInertialFrameOrientation, List<GyroscopeMeasurement> measurements) throws IllegalArgumentException{
		EulerAngleRotation nedOrientation = null;
		//Assuming that the initial body frame orientation is aligned with the body frame axes.
		EulerAngleRotation initialBodyFrameOrientation =  new EulerAngleRotation(0, 0, 0);
		EulerAngleRotation currentBodyFrameOrientation = initialBodyFrameOrientation;
		
		//Update the body frame orientation
		for( GyroscopeMeasurement gyroscopeMeasurement : measurements){
			currentBodyFrameOrientation = calculateOrientation(gyroscopeMeasurement, currentBodyFrameOrientation, null);
		}
		
		//Calculate the NED Earth-fixed inertial reference frame representation of the orientation
		
		nedOrientation = convertRotationFromBodyFrameToNedFrame(currentBodyFrameOrientation, initialInertialFrameOrientation);
		
		//adds in the inertial frame initial orientation
		
		nedOrientation = new EulerAngleRotation(nedOrientation.getRadiansRotationAlongXAxis() + initialInertialFrameOrientation.getRadiansRotationAlongXAxis(),
				nedOrientation.getRadiansRotationAlongYAxis() + initialInertialFrameOrientation.getRadiansRotationAlongYAxis(),
				nedOrientation.getRadiansRotationAlongZAxis() + initialInertialFrameOrientation.getRadiansRotationAlongZAxis());
		
		return nedOrientation;
	}
	
	/**
	 * Converts an acceleration measurement from the body frame to Earth-fixed inertial reference frame
	 * @param measurement Measurement to convert
	 * @param inertialFrameOrientation Euler angle representation of the orientation of the device measured relative to
	 * to an Earth-fixed inertial reference frame.
	 * @return Converted acceleration measured relative to the new reference frame. 
	 */
	public static AccelerometerMeasurement changeToInertialFrame(AccelerometerMeasurement measurement, EulerAngleRotation inertialFrameOrientation){
		
		Matrix bodyFrameToInertialFrameRotationMatrix = createRotationMatrix(inertialFrameOrientation);
		
		return changeToInertialFrame(measurement, bodyFrameToInertialFrameRotationMatrix);
	}
	
	
	/**
	 * Converts an acceleration measurement from the body frame to Earth-fixed inertial reference frame
	 * @param measurement Measurement to convert
	 * @param bodyFrameToInertialFrameRotationMatrix Matrix to facilitate the conversion to the target reference frame.
	 * @return Converted acceleration measured relative to the new reference frame. 
	 */
	public static AccelerometerMeasurement changeToInertialFrame(AccelerometerMeasurement measurement, Matrix bodyFrameToInertialFrameRotationMatrix){
		
		ThreeDimensionalSpaceVector accelerationVector = Acceleration.ToThreeDimensionalSpaceVector(measurement.getAccelerationMeasurement());
		
		ThreeDimensionalSpaceVector inertialFrameAccelerationVector = changeToInertialFrame(
				accelerationVector, bodyFrameToInertialFrameRotationMatrix);

		AccelerometerMeasurement accelerationMeasurement = new AccelerometerMeasurement(new Acceleration(inertialFrameAccelerationVector),
				measurement.getNumberOfSecondsSinceLastMeasurement(), measurement.getTimeMeasured());
				
		return accelerationMeasurement;
	}
	
	
	
}
