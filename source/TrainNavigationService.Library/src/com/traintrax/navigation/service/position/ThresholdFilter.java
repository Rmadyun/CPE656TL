package com.traintrax.navigation.service.position;

/**
 * Class is responsible for clamping 
 * a value down to a specific value until
 * a threshold value is reached
 * @author death
 *
 */
public class ThresholdFilter {
	
	boolean thresholdValueReached = false;
	private double thresholdValue;
	private double clampValue;
	
	/**
	 * Constructor
	 * @param thresholdValue Value to filter must reach for the provided value to be used.
	 * @param clampValue Value to return until the threshold value is reached
	 */
	public ThresholdFilter(double thresholdValue, double clampValue){
		this.thresholdValue = thresholdValue;
		this.clampValue = clampValue;
	}
	
	/**
	 * Performs filtering upon the provided value
	 * @param value Actual value measured
	 * @return Assigned clamp value if threshold is not reached; otherwise the value parameter minus the threshold value.
	 */
	public double filter(double value){
		
		double returnValue = clampValue;
		
		if(thresholdValueReached)
		{
			returnValue = value - thresholdValue;
		}
		else if(value >= thresholdValue){
			returnValue = value - thresholdValue;
			thresholdValueReached = true;
		}
		
		return returnValue;
	}

}
