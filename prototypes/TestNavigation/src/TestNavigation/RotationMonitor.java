package TestNavigation;

///Class is responsible for monitoring changes
///in the orientation of an object.
public class RotationMonitor {
	
	private final static double ANGULAR_RATE_THRESHOLD = 0.01;
	
	private IGyroscope gyroscope;
	private EulerAngleRotation initialBodyFrameOrientation;
	private EulerAngleRotation currentBodyFrameOrientation;
	private IRotationChangeSubscriber subscriber = null;
	private GyroscopeMeasurement measurementBias;
	
	///Constructor
	public RotationMonitor(IGyroscope gyroscope, 
			EulerAngleRotation initialBodyFrameOrientation){
		this.gyroscope = gyroscope;
		this.initialBodyFrameOrientation = initialBodyFrameOrientation;
		this.currentBodyFrameOrientation = initialBodyFrameOrientation;
	}
	
	public void AddSubscriber(IRotationChangeSubscriber subscriber){
		this.subscriber = subscriber;
	}
	
	///Converts a gyroscope measurement into a quaternion
	///Courtesy of http://developer.android.com/guide/topics/sensors/sensors_motion.html
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
	
	/*
	 * Method determines the new orientation of an object after a Gyroscope measurement has been observed
	 */
	private static EulerAngleRotation calculateOrientation(GyroscopeMeasurement measurement, EulerAngleRotation initialOrientation, GyroscopeMeasurement bias){
		EulerAngleRotation newOrientation = initialOrientation;
		
        if(measurement != null){
        	//Adjust the angular speeds to match Euler orientations
        	//NOTE: This is also assuming that the standard conventions for rotation are used.
        	//counter clockwise turns are positive.
        	
        	//Test quaternion
        	Quat4d testQuaternion = new Quat4d();
        	testQuaternion.w = 0.7071;
        	testQuaternion.x = 0.7071;
        	
        /*	@SuppressWarnings("unused")
			EulerAngleRotation testRotation = convertFromQuaternionToEulerAngle(testQuaternion); */
        	
        	GyroscopeMeasurement correctedMeasurement;
        	
        	/*if(bias == null)
        	{
        		correctedMeasurement = measurement;
        	}
        	else{
        		double rotatedX = (measurement.getRadiansRotationPerSecondAlongXAxis() - bias.getRadiansRotationPerSecondAlongXAxis());
        		double rotatedY = (measurement.getRadiansRotationPerSecondAlongYAxis() - bias.getRadiansRotationPerSecondAlongYAxis());
        		double rotatedZ = (measurement.getRadiansRotationPerSecondAlongZAxis() - bias.getRadiansRotationPerSecondAlongZAxis());
        		
        	    correctedMeasurement = new GyroscopeMeasurement(rotatedX, rotatedY, rotatedZ, measurement.getNumberOfSecondsSinceLastMeasurement());	        	
        	}*/
        	
        	double rotatedX = measurement.getRadiansRotationPerSecondAlongXAxis();
        	double rotatedY = measurement.getRadiansRotationPerSecondAlongYAxis();
        	double rotatedZ = measurement.getRadiansRotationPerSecondAlongZAxis();
        	
        	if(rotatedX < ANGULAR_RATE_THRESHOLD){
        		rotatedX = 0;
        	}
        	
        	if(rotatedY < ANGULAR_RATE_THRESHOLD){
        		rotatedY = 0;
        	}
        	
        	if(rotatedZ < ANGULAR_RATE_THRESHOLD){
        		rotatedZ = 0;
        	}
        	
        	correctedMeasurement = new GyroscopeMeasurement(rotatedX, rotatedY, rotatedZ, measurement.getNumberOfSecondsSinceLastMeasurement());
        	
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
	
	public EulerAngleRotation waitForNextRotationUpdate() {

		GyroscopeMeasurement gyroscopeMeasurement = gyroscope.getNextMeasurement();

		if(gyroscopeMeasurement != null){
			//Calculate the rotation
			
			if(measurementBias == null)
			{
				measurementBias = gyroscopeMeasurement;
			}

			currentBodyFrameOrientation = calculateOrientation(gyroscopeMeasurement, currentBodyFrameOrientation, measurementBias);

			if(subscriber != null){

				subscriber.OrientationChanged(currentBodyFrameOrientation);
			}
		}

		return currentBodyFrameOrientation;
	}
	
	EulerAngleRotation getCurrentBodyFrameOrientation() {
		
		return currentBodyFrameOrientation;
	}

}
