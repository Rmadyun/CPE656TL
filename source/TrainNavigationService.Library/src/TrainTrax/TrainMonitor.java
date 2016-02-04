package TrainTrax;

/**
 * Class is responsible for observing changes to a train that 
 * belongs to the Positive Train Control Test Bed.
 * @author Corey Sanders
 *
 */
public class TrainMonitor implements TrainMonitorInterface {
	
	private String trainId;
	private Coordinate lastKnownTrainPosition;
	
	/**
	 * Constructor
	 * @param trainId Unique identifier for target train
	 * @param initialTrainPosition Initial location of the train
	 */
	public TrainMonitor(String trainId, Coordinate initialTrainPosition){
		
		this.trainId = trainId;
		lastKnownTrainPosition = initialTrainPosition;
	}
	
<<<<<<< HEAD

=======
	/**
	 * Converts an acceleration measurement from the body frame to 
	 * Earth-fixed inertial reference frame
	 * @param measurement Measurement to convert
	 * @param bodyFrameToInertialFrameRotationMatrix Matrix to facilitate the conversion to the target reference frame.
	 * @return Converted acceleration measured relative to the new reference frame. 
	 */
	private static AccelerometerMeasurement changeToInertialFrame(AccelerometerMeasurement measurement, Matrix bodyFrameToInertialFrameRotationMatrix){
		
		ThreeDimensionalSpaceVector accelerationVector = Acceleration.ToThreeDimensionalSpaceVector(measurement.getAccelerationMeasurement());
		
		ThreeDimensionalSpaceVector inertialFrameAccelerationVector = RotationUtilities.changeToInertialFrame(
				accelerationVector, bodyFrameToInertialFrameRotationMatrix);

		AccelerometerMeasurement accelerationMeasurement = new AccelerometerMeasurement(new Acceleration(inertialFrameAccelerationVector),
				measurement.getNumberOfSecondsSinceLastMeasurement(), measurement.getTimeMeasured());
				
		return accelerationMeasurement;
	}
>>>>>>> master
	
	/**
	 * Blocks until there is another update on the target train's
	 * position.
	 * @return Most recent update on the position of the train.
	 */
	public Coordinate waitForNextPositionUpdate(){
		
		//TODO: Implement the Train position update algorithm
		
		return lastKnownTrainPosition;
	}
	
	/**
	 * Retrieves the unique identifier for the target train
	 * @return Unique identifier for the target train
	 */
	public String getTrainId(){
		return trainId;
	}
	
	/**
	 * Retrieves the last known position of the target train
	 * @return The last known position of the target train.
	 */
	public Coordinate getLastKnownPosition(){
		return lastKnownTrainPosition;
	}

}
