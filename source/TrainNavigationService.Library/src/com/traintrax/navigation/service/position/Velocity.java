package com.traintrax.navigation.service.position;

import com.traintrax.navigation.service.math.ThreeDimensionalSpaceVector;

/**
 * @author Corey Sanders
 *Class represents the velocity of a moving object.
 */
public class Velocity {

	private double metersPerSecondAlongXAxis;
	private double metersPerSecondAlongYAxis;
	private double metersPerSecondAlongZAxis;
	
	/**
	 * Constructor
	 * @param metersPerSecondAlongXAxis Velocity measured from the X-axis in meters per second
	 * @param metersPerSecondAlongYAxis Velocity measured from the Y-axis in meters per second
	 * @param metersPerSecondAlongZAxis Velocity measured from the Z-axis in meters per second
	 */
	public Velocity(double metersPerSecondAlongXAxis,
			double metersPerSecondAlongYAxis,
			double metersPerSecondAlongZAxis){
		
		this.metersPerSecondAlongXAxis = metersPerSecondAlongXAxis;
		this.metersPerSecondAlongYAxis = metersPerSecondAlongYAxis;
		this.metersPerSecondAlongZAxis = metersPerSecondAlongZAxis;
	}
	
	/**
	 * Constructor
	 * @param threeDimensionalSpaceVector 3-D Space representation of the velocity vector using meters per second units.
	 */
	public Velocity(ThreeDimensionalSpaceVector threeDimensionalSpaceVector){
		
		this(threeDimensionalSpaceVector.getX(),
			threeDimensionalSpaceVector.getY(),
			threeDimensionalSpaceVector.getZ());
	}
	
	/**
	 * Describes the velocity from the original position
	 * along the X axis of the target inertial reference frame in meters per second
	 * @return Velocity measured from the X-axis in meters per second
	 */
	public double getMetersPerSecondAlongXAxis(){
		return metersPerSecondAlongXAxis;
	}

	/**
	 * Describes the velocity from the original position
	 * along the Y axis of the target inertial reference frame in meters per second
	 * @return Velocity measured from the Y-axis in meters per second
	 */
	public double getMetersPerSecondAlongYAxis() {
		return metersPerSecondAlongYAxis;
	}
		
	/**
	 * Describes the velocity from the original position
	 * along the Z axis of the target inertial reference frame in meters per second
	 * @return Velocity measured from the Z-axis in meters per second
	 */
	public double getMetersPerSecondAlongZAxis() {
		return metersPerSecondAlongZAxis;
	}
	
	/**
	 * Converts the class instance into a generic three-dimensional space
	 * vector to use for calculations
	 * @param velocity Velocity instance to convert
	 * @return A new ThreeDimensionalSpaceVector vector that represents the Velocity value to convert.
	 */
	public static ThreeDimensionalSpaceVector ToThreeDimensionalSpaceVector(Velocity velocity){

		return new ThreeDimensionalSpaceVector(velocity.metersPerSecondAlongXAxis,
				velocity.metersPerSecondAlongYAxis, 
				velocity.metersPerSecondAlongZAxis);
	}
	
	/**
	 * Converts a generic three-dimensional space vector into an 
	 * Velocity class instance.
	 * @param threeDimensionalSpaceVector 3-D vector representation of velocity to convert
	 * @return A new velocity instance that represents 3-D vector value.
	 */
	public static Velocity FromThreeDimensionalSpaceVector(ThreeDimensionalSpaceVector threeDimensionalSpaceVector){

		return new Velocity(threeDimensionalSpaceVector);
	}
}
