package com.traintrax.navigation.service;

import java.util.Calendar;

import com.traintrax.navigation.service.position.Coordinate;

/**
 * Describes an estimate of a train's position at a given point in 
 * time.
 * @author Corey Sanders
 *
 */
public class TrainPositionEstimate {

	private Coordinate position;
	private Calendar timeAtPosition;
	private String trainId;

	/**
	 * Constructor
	 * @param position Position of estimated for train 
	 * @param timeAtPosition Point in time where train is believed to be at the reported position
	 * @param trainId Unique identifier for the train of interest
	 */
	public TrainPositionEstimate(Coordinate position,
			Calendar timeAtPosition,
			String trainId){
		this.position = position;
		this.timeAtPosition = timeAtPosition;
		this.trainId = trainId;
	}
	
	/**
	 * Position of estimated for train
	 * @return Position of estimated for train
	 */
	public Coordinate getPosition(){
		return position;
	}
	
	/**
	 * Point in time where train is believed to be at the reported position
	 * @return Point in time where train is believed to be at the reported position
	 */
	public Calendar getTimeAtPosition(){
		return timeAtPosition;
	}
	
	/**
	 * Unique identifier for the train of interest
	 * @return Unique identifier for the train of interest
	 */
	public String getTrainId(){
		return trainId;
	}
	
}
