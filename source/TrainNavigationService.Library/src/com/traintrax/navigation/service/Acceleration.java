package com.traintrax.navigation.service;

/**
 * @author Corey Sanders
 *Class represents a single measurement taken from an accelerometer
 *that expresses the forces acting on an object.
 */
public class Acceleration {

	private double metersPerSecondSquaredAlongXAxis;
	private double metersPerSecondSquaredAlongYAxis;
	private double metersPerSecondSquaredAlongZAxis;
	
	/**
	 * Constructor
	 * @param metersPerSecondSquaredAlongXAxis Acceleration measured from the X-axis in meters per second squared
	 * @param metersPerSecondSquaredAlongYAxis Acceleration measured from the Y-axis in meters per second squared
	 * @param metersPerSecondSquaredAlongZAxis Acceleration measured from the Z-axis in meters per second squared
	 */
	public Acceleration(double metersPerSecondSquaredAlongXAxis,
			double metersPerSecondSquaredAlongYAxis,
			double metersPerSecondSquaredAlongZAxis){
		
		this.metersPerSecondSquaredAlongXAxis = metersPerSecondSquaredAlongXAxis;
		this.metersPerSecondSquaredAlongYAxis = metersPerSecondSquaredAlongYAxis;
		this.metersPerSecondSquaredAlongZAxis = metersPerSecondSquaredAlongZAxis;
	}
	
	/**
	 * Constructor
	 * @param threeDimensionalSpaceVector 3-D Space representation of the acceleration vector using meters per second squared units.
	 */
	public Acceleration(ThreeDimensionalSpaceVector threeDimensionalSpaceVector){
		
		this(threeDimensionalSpaceVector.getX(),
			threeDimensionalSpaceVector.getY(),
			threeDimensionalSpaceVector.getZ());
	}
	
	/**
	 * Describes the acceleration from the original position
	 * along the X axis of the target inertial reference frame in meters per second squared
	 * @return Acceleration measured from the X-axis in meters per second squared
	 */
	public double getMetersPerSecondSquaredAlongXAxis(){
		return metersPerSecondSquaredAlongXAxis;
	}

	/**
	 * Describes the acceleration from the original position
	 * along the Y axis of the target inertial reference frame in meters per second squared
	 * @return Acceleration measured from the Y-axis in meters per second squared
	 */
	public double getMetersPerSecondSquaredAlongYAxis() {
		return metersPerSecondSquaredAlongYAxis;
	}
		
	/**
	 * Describes the acceleration from the original position
	 * along the Z axis of the target inertial reference frame in meters per second squared
	 * @return Acceleration measured from the Z-axis in meters per second squared
	 */
	public double getMetersPerSecondSquaredAlongZAxis() {
		return metersPerSecondSquaredAlongZAxis;
	}
	
	/**
	 * Converts the class instance into a generic three-dimensional space
	 * vector to use for calculations
	 * @param acceleration Acceleration instance to convert
	 * @return A new ThreeDimensionalSpaceVector vector that represents the Acceleration value to convert.
	 */
	public static ThreeDimensionalSpaceVector ToThreeDimensionalSpaceVector(Acceleration acceleration){

		return new ThreeDimensionalSpaceVector(acceleration.metersPerSecondSquaredAlongXAxis,
				acceleration.metersPerSecondSquaredAlongYAxis, 
				acceleration.metersPerSecondSquaredAlongZAxis);
	}
	
	/**
	 * Converts a generic three-dimensional space vector into an 
	 * Acceleration class instance.
	 * @param threeDimensionalSpaceVector 3-D vector representation of acceleration to convert
	 * @return A new Acceleration instance that represents 3-D vector value.
	 */
	public static Acceleration FromThreeDimensionalSpaceVector(ThreeDimensionalSpaceVector threeDimensionalSpaceVector){

		return new Acceleration(threeDimensionalSpaceVector);
	}
}
