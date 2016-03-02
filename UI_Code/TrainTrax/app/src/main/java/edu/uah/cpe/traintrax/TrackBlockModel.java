package edu.uah.cpe.traintrax;

import java.util.ArrayList;
import java.util.List;

/**
 * Class represents a partition of track that can be used to determine the position of 
 * a given train on the track.
 * @author Corey Sanders
 *
 */
public class TrackBlockModel {

	private List<Vertex> points = new ArrayList<>();
	private List<Vertex> boundaryPoints = new ArrayList<>();
	private List<TrackBlockModel> adjacentBlocks = new ArrayList<>();
	private String blockName;
	
	public TrackBlockModel(String blockName){
		this.blockName = blockName;
	}
	
	
	/**
	 * @return the blockName
	 */
	public String getBlockName() {
		return blockName;
	}


	/**
	 * @return the points
	 */
	public List<Vertex> getPoints() {
		return points;
	}
	/**
	 * @return the adjacentBlocks
	 */
	public List<TrackBlockModel> getAdjacentBlocks() {
		return adjacentBlocks;
	}
	
	
	
	
}
