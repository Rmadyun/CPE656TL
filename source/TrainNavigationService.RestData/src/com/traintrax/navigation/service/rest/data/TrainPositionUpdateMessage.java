package com.traintrax.navigation.service.rest.data;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.traintrax.navigation.database.library.AdjacentPoint;
import com.traintrax.navigation.database.library.RepositoryEntry;

/**
 * Class represents a response from to a GetLastKnownPositionRequest
 * @author Corey Sanders
 *
 */
public class TrainPositionUpdateMessage {
	
	@SerializedName("train_id")
	private String trainId;
	
	@SerializedName("x")
	private double x;
	
	@SerializedName("y")
	private double y;
	
	@SerializedName("z")
	private double z;
	
	@SerializedName("time_measured")
	private Calendar timeMeasured;

	/**
	 * Constructor
	 * @param trainId Unique ID for Train
	 * @param x Width in inches from the origin
	 * @param y Depth in inches from the origin
	 * @param z Height in inches from the origin
	 * @param timeMeasured Time that Train was measured to be at this position
	 */
	public TrainPositionUpdateMessage(String trainId, double x, double y, double z, Calendar timeMeasured) {
		super();
		this.trainId = trainId;
		this.x = x;
		this.y = y;
		this.z = z;
		this.timeMeasured = timeMeasured;
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
	 * @return the timeMeasured
	 */
	public Calendar getTimeMeasured() {
		return timeMeasured;
	}
	
}
