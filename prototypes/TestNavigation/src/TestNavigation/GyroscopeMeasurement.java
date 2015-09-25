package TestNavigation;

/**
 * @author death
 *Class represents a single measurement taken from a gyroscope for
 *angular velocity expressed in Euler-Angle rotation format.
 */
public class GyroscopeMeasurement {

	private double radiansRotationPerSecondAlongXAxis;
	private double radiansRotationPerSecondAlongYAxis;
	private double radiansRotationPerSecondAlongZAxis;
	private double numberOfSecondsSinceLastMeasurement;
	
	///Constructor
	public GyroscopeMeasurement(double radiansRotationPerSecondAlongXAxis,
			double radiansRotationPerSecondAlongYAxis,
			double radiansRotationPerSecondAlongZAxis,
			double numberOfSecondsSinceLastMeasurement){
		
		this.radiansRotationPerSecondAlongXAxis = radiansRotationPerSecondAlongXAxis;
		this.radiansRotationPerSecondAlongYAxis = radiansRotationPerSecondAlongYAxis;
		this.radiansRotationPerSecondAlongZAxis = radiansRotationPerSecondAlongZAxis;
		this.numberOfSecondsSinceLastMeasurement = numberOfSecondsSinceLastMeasurement;
	}
	
	///Describes the rate of rotation from the original position
	///along the X axis of the body frame in radians
	public double getRadiansRotationPerSecondAlongXAxis(){
		return radiansRotationPerSecondAlongXAxis;
	}

	///Describes the rate of rotation from the original position
	///along the Y axis of the body frame in radians
	public double getRadiansRotationPerSecondAlongYAxis() {
		return radiansRotationPerSecondAlongYAxis;
	}
	
	///Describes the rate of rotation from the original position
	///along the Z axis of the body frame in radians
	public double getRadiansRotationPerSecondAlongZAxis() {
		return radiansRotationPerSecondAlongZAxis;
	}

	///Describes the number of seconds that have elapsed since the
	///the last reported measurement.
	public double getNumberOfSecondsSinceLastMeasurement(){
		return numberOfSecondsSinceLastMeasurement;
	}

}
