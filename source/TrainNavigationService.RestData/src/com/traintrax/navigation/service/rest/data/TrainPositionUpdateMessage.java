package com.traintrax.navigation.service.rest.data;
import java.util.Calendar;
import com.google.gson.annotations.SerializedName;

/**
 * Class represents a response from to a GetLastKnownPositionRequest
 * @author Corey Sanders
 *
 */
public class TrainPositionUpdateMessage {
	
	@SerializedName("trainId")
	private String trainId;
	
	@SerializedName("x")
	private double x;
	
	@SerializedName("y")
	private double y;
	
	@SerializedName("z")
	private double z;
	
	@SerializedName("velocityX")
	private double velocityX;
	
	@SerializedName("velocityY")
	private double velocityY;
	
	@SerializedName("velocityZ")
	private double velocityZ;
	
	@SerializedName("timeMeasured")
	private Calendar timeMeasured;

	/**
	 * Constructor
	 * @param trainId Unique ID for Train
	 * @param x Width in inches from the origin
	 * @param y Depth in inches from the origin
	 * @param z Height in inches from the origin
	 * @param velocityX Velocity in inches per second along the X-Axis of the coordinate frame.
     * @param velocityY Velocity in inches per second along the Y-Axis of the coordinate frame.
     * @param velocityZ Velocity in inches per second along the Z-Axis of the coordinate frame.
	 * @param timeMeasured Time that Train was measured to be at this position 
	 */
	public TrainPositionUpdateMessage(String trainId, double x, double y, double z,
			double velocityX, double velocityY, double velocityZ, Calendar timeMeasured) {
		super();
		this.trainId = trainId;
		this.x = x;
		this.y = y;
		this.z = z;
		this.timeMeasured = timeMeasured;
		this.velocityX = velocityX;
		this.velocityY = velocityY;
		this.velocityZ = velocityZ;
	}

	/**
	 * @return the trainId
	 */
	public String getTrainId() {
		return trainId;
	}

	/**
	 * @return the x
	 */
	public double getX() {
		return x;
	}

	/**
	 * @return the y
	 */
	public double getY() {
		return y;
	}

	/**
	 * @return the z
	 */
	public double getZ() {
		return z;
	}
	

	/**
	 * @return the velocityX
	 */
	public double getVelocityX() {
		return velocityX;
	}

	/**
	 * @return the velocityY
	 */
	public double getVelocityY() {
		return velocityY;
	}

	/**
	 * @return the velocityZ
	 */
	public double getVelocityZ() {
		return velocityZ;
	}

	/**
	 * @return the timeMeasured
	 */
	public Calendar getTimeMeasured() {
		return timeMeasured;
	}
	
}
