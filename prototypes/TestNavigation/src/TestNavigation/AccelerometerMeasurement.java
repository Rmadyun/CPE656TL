package TestNavigation;

/**
 * @author death
 *Class represents a single measurement taken from an accelerometer
 *that expresses the forces acting on an object.
 */
public class AccelerometerMeasurement {

	private double metersPerSecondSquaredAlongXAxis;
	private double metersPerSecondSquaredAlongYAxis;
	private double metersPerSecondSquaredAlongZAxis;
	private double numberOfSecondsSinceLastMeasurement;
	
	///Constructor
	public AccelerometerMeasurement(double metersPerSecondSquaredAlongXAxis,
			double metersPerSecondSquaredAlongYAxis,
			double metersPerSecondSquaredAlongZAxis,
			double numberOfSecondsSinceLastMeasurement){
		
		this.metersPerSecondSquaredAlongXAxis = metersPerSecondSquaredAlongXAxis;
		this.metersPerSecondSquaredAlongYAxis = metersPerSecondSquaredAlongYAxis;
		this.metersPerSecondSquaredAlongZAxis = metersPerSecondSquaredAlongZAxis;
		this.numberOfSecondsSinceLastMeasurement = numberOfSecondsSinceLastMeasurement;
	}
	
	///Describes the acceleration from the original position
	///along the X axis of the body frame in meters per second squared
	public double getMetersPerSecondSquaredAlongXAxis(){
		return metersPerSecondSquaredAlongXAxis;
	}

	///Describes the acceleration from the original position
	///along the Y axis of the body frame in meters per second squared
	public double getMetersPerSecondSquaredAlongYAxis() {
		return metersPerSecondSquaredAlongYAxis;
	}
	
	///Describes the acceleration from the original position
	///along the Z axis of the body frame in meters per second squared
	public double getMetersPerSecondSquaredAlongZAxis() {
		return metersPerSecondSquaredAlongZAxis;
	}

	///Describes the number of seconds that have elapsed since the
	///the last reported measurement.
	public double getNumberOfSecondsSinceLastMeasurement(){
		return numberOfSecondsSinceLastMeasurement;
	}

}
