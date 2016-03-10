package com.traintrax.navigation.database.library;

import java.util.Calendar;


/**
 * Represents the position of a train on the test bed at
 * a given point in time
 * @author Corey Sanders
 *
 */
public class TrainPosition {

	private String trainId;
	private double x;
	private double y;
	private double z;
	private Calendar timeAtPosition;

	/**
	 * Constructor
	 * @param trainId Unique ID for the target train
	 * @param x Width in inches from the origin
	 * @param y Depth in inches from the origin
	 * @param z Height in inches from the origin
	 * @param timeAtPosition Date and time for the reported position.
	 */
	public TrainPosition(String trainId, double x, double y, double z, Calendar timeAtPosition) {
		this.trainId = trainId;
		this.x = x;
		this.y = y;
		this.z = z;
		this.timeAtPosition = timeAtPosition;
	}

	/**
	 * Retrieves the Unique ID for the target train
	 * @return Unique ID for the target train
	 */
	public String getTrainId() {
		return trainId;
	}

	/**
	 * Retrieves the Width in inches from the origin
	 * @return Width in inches from the origin
	 */
	public double getX() {
		return x;
	}

	/**
	 * Retrieves the Depth in inches from the origin
	 * @return Depth in inches from the origin
	 */
	public double getY() {
		return y;
	}

	/**
	 * Retrieves the Height in inches from the origin
	 * @return Height in inches from the origin
	 */
	public double getZ() {
		return z;
	}

	/**
	 * Retrieves the Date and time for the reported position.
	 * @return Date and time for the reported position.
	 */
	public Calendar getTimeAtPosition() {
		return timeAtPosition;
	}
	
}
