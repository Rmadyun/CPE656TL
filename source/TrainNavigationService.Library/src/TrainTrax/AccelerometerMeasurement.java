package TrainTrax;

import java.util.Calendar;

/**
 * @author death
 *Class represents a single measurement taken from an accelerometer
 *that expresses the forces acting on an object.
 */
public class AccelerometerMeasurement {

	private Acceleration accelerationMeasurement;
	private double numberOfSecondsSinceLastMeasurement;
	private Calendar timeMeasured;
	
	/**
	 * Constructor
	 * @param accelerationMeasurement Measurement of acceleration from the accelerometer
	 * @param numberOfSecondsSinceLastMeasurement Number of seconds that elapsed between this measurement and the previous measurement 
	 * @param timeMeasured Time that accelerometer measurement was taken.
	 */
	public AccelerometerMeasurement(Acceleration accelerationMeasurement,
			double numberOfSecondsSinceLastMeasurement,
			Calendar timeMeasured){
		
		this.accelerationMeasurement = accelerationMeasurement;
		this.numberOfSecondsSinceLastMeasurement = numberOfSecondsSinceLastMeasurement;
		this.timeMeasured = timeMeasured;
	}
		

    /**
     * Measurement of acceleration from the accelerometer
     * @return Measurement of acceleration from the accelerometer
     */
	public Acceleration getAccelerationMeasurement() {
		return accelerationMeasurement;
	}

	/**
	 * Describes the time that the accelerometer measurement was taken
	 * @return Time that measurement was taken
	 */
	public Calendar getTimeMeasured(){
		return timeMeasured;
	}
	
	/**
	 * Describes the number of seconds that have elapsed since the
	 * the last reported measurement.
	 * @return Number of seconds that have elapsed since the
	 * the last reported measurement.
	 */
	public double getNumberOfSecondsSinceLastMeasurement(){
		return numberOfSecondsSinceLastMeasurement;
	}

}
