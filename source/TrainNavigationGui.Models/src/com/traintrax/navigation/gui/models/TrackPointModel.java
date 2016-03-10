package com.traintrax.navigation.gui.models;

/**
 * GUI-related model of points on the track.
 * @author Corey Sanders
 */
public class TrackPointModel {
	
	private String name;
	private String type;
	private Vertex position;
	
	/**
	 * Constructor
	 * @param name Name describing the point
	 * @param type Description of the type of point
	 * @param position location of the point
	 */
	public TrackPointModel(String name, String type, Vertex position) {
		super();
		this.name = name;
		this.type = type;
		this.position = position;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @return the position
	 */
	public Vertex getPosition() {
		return position;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	
	
	
	

}
