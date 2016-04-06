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

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((timeAtPosition == null) ? 0 : timeAtPosition.hashCode());
		result = prime * result + ((trainId == null) ? 0 : trainId.hashCode());
		long temp;
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(z);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof TrainPosition))
			return false;
		TrainPosition other = (TrainPosition) obj;
		if (timeAtPosition == null) {
			if (other.timeAtPosition != null)
				return false;
		} else if (Math.abs(timeAtPosition.getTimeInMillis() - other.timeAtPosition.getTimeInMillis()) > 1000) //they are not equal if they are more than 1 sec difference between each other.
			return false;
		if (trainId == null) {
			if (other.trainId != null)
				return false;
		} else if (!trainId.equals(other.trainId))
			return false;
		if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
			return false;
		if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y))
			return false;
		if (Double.doubleToLongBits(z) != Double.doubleToLongBits(other.z))
			return false;
		return true;
	}
	
	
	
}
