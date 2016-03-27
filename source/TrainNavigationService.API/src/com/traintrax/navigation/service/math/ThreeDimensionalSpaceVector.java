package com.traintrax.navigation.service.math;

/**
 * Generic vector to represent the characteristic of an 
 * object in 3-dimensional space.
 * @author Corey Sanders
 *
 */
public class ThreeDimensionalSpaceVector {
	
	private double x;
	private double y;
	private double z;
	
	/**
	 * Constructor
	 * @param x Value along the X-Axis
	 * @param y Value along the Y-Axis
	 * @param z Value along the Z-Axis
	 */
	public ThreeDimensionalSpaceVector(double x,double y, double z){
	    this.x = x;
	    this.y = y;
	    this.z = z;
	}
	
	/**
	 * Constructor
	 * @param x Value along the X-Axis
	 * @param y Value along the Y-Axis
	 * 
	 * Height is assumed to be zero.
	 */
	public ThreeDimensionalSpaceVector(double x,double y)  {
		this(x, y, 0);
	}
	
	/**
	 * Value along the X-Axis
	 * @return value along the X-Axis
	 */
	public double getX(){
		return x;
	}
	
	/**
	 * Set value along the X-Axis
	 * @param x Value along the X-Axis
	 */
	public void setX(double x){
		this.x = x;
	}
	
	/**
	 * Depth from Origin
	 * @return Depth from Origin
	 */
	public double getY(){
		return y;
	}
	
	/**
	 * Value along the Y-Axis
	 * @param y Value along the Y-Axis
	 */
	public void setY(double y){
		this.y = y;
	}
	
	/**
	 * Value along the Z-Axis
	 * @return Value along the Z-Axis
	 */
	public double getZ(){
		return z;
	}
	
	/**
	 * Set the value along the Z-Axis
	 * @param z Value along the Z-Axis
	 */
	public void setZ(double z){
		this.z = z;
	}

}
