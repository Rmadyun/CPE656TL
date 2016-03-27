package com.traintrax.navigation.service.mdu;

/**
 * Class is responsible for determining calibration values for a given IMU
 * device
 * 
 * @author Corey Sanders This calibrator assumes that the values provided to it
 *         are from an object that is stationary. It is also assuming that
 *         gravity is the only think acting on it.
 */
public class ImuCalibrator {
	private double accXOffset;
	private double accYOffset;
	private double accZOffset;
	private double gyrXOffset;
	private double gyrYOffset;
	private double gyrZOffset;

	private double accXTotal;
	private double accYTotal;
	private double accZTotal;
	private double gyrXTotal;
	private double gyrYTotal;
	private double gyrZTotal;

	private int numberOfAccMeasurements;
	private int numberOfGyrMeasurements;

	private static final int MaximumNumberOfAccMeasurements = 40;
	private static final int MaximumNumberOfGyrMeasurements = 40;

	/**
	 * Constructor
	 */
	public ImuCalibrator() {
		accXOffset = 0;
		accYOffset = 0;
		accZOffset = 0;
		accXTotal = 0;
		accYTotal = 0;
		accZTotal = 0;
		gyrXTotal = 0;
		gyrYTotal = 0;
		gyrZTotal = 0;
		numberOfAccMeasurements = 0;
		numberOfGyrMeasurements = 0;
	}
	
	/**
	 * Adds a measurement to include in calibration
	 * @param measurement Accelerometer measurement to include
	 */
	public void addMeasurement(AccelerometerMeasurement measurement){
		
		if(numberOfAccMeasurements < MaximumNumberOfAccMeasurements)
		{
			accXTotal += measurement.getAccelerationMeasurement().getMetersPerSecondSquaredAlongXAxis();
			accYTotal += measurement.getAccelerationMeasurement().getMetersPerSecondSquaredAlongYAxis();
			accZTotal += measurement.getAccelerationMeasurement().getMetersPerSecondSquaredAlongZAxis();

			numberOfAccMeasurements++;
			
			accXOffset = (accXTotal / numberOfAccMeasurements);
			accYOffset = (accYTotal / numberOfAccMeasurements);
			accZOffset = (accZTotal / numberOfAccMeasurements);
		}
	}
	
	/**
	 * Adds a measurement to include in calibration
	 * @param measurement Accelerometer measurement to include
	 */
	public void addMeasurement(GyroscopeMeasurement measurement){
		
		if(numberOfGyrMeasurements < MaximumNumberOfGyrMeasurements)
		{
			gyrXTotal += measurement.getRadiansRotationPerSecondAlongXAxis();
			gyrYTotal += measurement.getRadiansRotationPerSecondAlongYAxis();
			gyrZTotal += measurement.getRadiansRotationPerSecondAlongZAxis();

			numberOfGyrMeasurements++;
			
			gyrXOffset = (gyrXTotal / numberOfGyrMeasurements);
			gyrYOffset = (gyrYTotal / numberOfGyrMeasurements);
			gyrZOffset = (gyrZTotal / numberOfGyrMeasurements);
		}
	}
	
	/**
	 * Method reports whether all of the samples necessary to complete calibration have been
	 * collected
	 * @return Returns true if calibration is complete; otherwise it returns false.
	 */
	public boolean isCalibrationComplete(){
		return (numberOfAccMeasurements == MaximumNumberOfAccMeasurements)
				&&(numberOfGyrMeasurements == MaximumNumberOfGyrMeasurements);
	}

	/**
	 * Retrieves the offset needed to correct the accelerometer
	 * X-axis.
	 * @return Offset to correct the accelerometer X axis
	 */
	public double getAccXOffset() {
		return accXOffset;
	}

	/**
	 * Retrieves the offset needed to correct the accelerometer
	 * Y-axis.
	 * @return Offset to correct the accelerometer Y axis
	 */
	public double getAccYOffset() {
		return accYOffset;
	}

	/**
	 * Retrieves the offset needed to correct the accelerometer
	 * Z-axis.
	 * @return Offset to correct the accelerometer Z axis
	 */
	public double getAccZOffset() {
		return accZOffset;
	}

	/**
	 * Retrieves the offset needed to correct the gyroscope
	 * X-axis.
	 * @return Offset to correct the gyroscope X axis
	 */
	public double getGyrXOffset() {
		return gyrXOffset;
	}

	/**
	 * Retrieves the offset needed to correct the gyroscope
	 * Y-axis.
	 * @return Offset to correct the gyroscope Y axis
	 */
	public double getGyrYOffset() {
		return gyrYOffset;
	}

	/**
	 * Retrieves the offset needed to correct the gyroscope
	 * Z-axis.
	 * @return Offset to correct the gyroscope Z axis
	 */
	public double getGyrZOffset() {
		return gyrZOffset;
	}	
}
