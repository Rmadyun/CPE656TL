package com.traintrax.navigation.service.position;

/**
 * Class is responsible for adjust raw gyroscope
 * measurements according to specified calibration values.
 * @author Corey Sanders
 *
 */
public class GyroscopeCalibrationFilter {

	private double xOffset;
	private double yOffset;
	private double zOffset;
	
	/**
	 * Constructor
	 */
	public GyroscopeCalibrationFilter(){
		
		xOffset = 0;
		yOffset = 0;
		zOffset = 0;
	}
	
	/**
	 * Performs filtering upon the provided value
	 * @param value Actual value measured
	 * @return Values adjusted for the calibration values
	 */
	public GyroscopeMeasurement filter(GyroscopeMeasurement value){
		
		GyroscopeMeasurement gyroscopeMeasurement = new GyroscopeMeasurement(value.getRadiansRotationPerSecondAlongXAxis() - xOffset,
				value.getRadiansRotationPerSecondAlongYAxis() - yOffset,
				value.getRadiansRotationPerSecondAlongZAxis() - zOffset,
				value.getNumberOfSecondsSinceLastMeasurement(),
				value.getTimeMeasured());
				
		return gyroscopeMeasurement;
	}

	/**
	 * Retrieves the amount to offset measurements along the X-Axis of the device
	 * @return Amount to offset measurements along the X-Axis of the device
	 */
	public double getXOffset() {
		return xOffset;
	}

	/**
	 * Assigns the amount to offset measurements along the X-Axis of the device
	 * @param xOffset Amount to offset measurements along the X-Axis of the device
	 */
	public void setXOffset(double xOffset) {
		this.xOffset = xOffset;
	}

	/**
	 * Retrieves the amount to offset measurements along the Y-Axis of the device
	 * @return Amount to offset measurements along the Y-Axis of the device
	 */
	public double getYOffset() {
		return yOffset;
	}

	/**
	 * Assigns the amount to offset measurements along the Y-Axis of the device
	 * @param yOffset Amount to offset measurements along the Y-Axis of the device
	 */
	public void setYOffset(double yOffset) {
		this.yOffset = yOffset;
	}

	/**
	 * Retrieves the amount to offset measurements along the Z-Axis of the device
	 * @return Amount to offset measurements along the Z-Axis of the device
	 */
	public double getZOffset() {
		return zOffset;
	}

	/**
	 * Assigns the amount to offset measurements along the X-Axis of the device
	 * @param zOffset Amount to offset measurements along the X-Axis of the device
	 */
	public void setZOffset(double zOffset) {
		this.zOffset = zOffset;
	}
	
}
