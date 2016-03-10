package com.traintrax.navigation.gui.models;
import java.util.ArrayList;
import java.util.List;

/**
 * Class represents a partition of track that can be used to determine the position of 
 * a given train on the track.
 * @author Corey Sanders
 *
 */
public class TrackBlockModel {

	private List<TrackPointModel> points = new ArrayList<>();
	private List<TrackBlockModel> adjacentBlocks = new ArrayList<>();
	private String blockName;
	
	/**
	 * Constructor
	 * @param blockName Human-friendly name for the track block.
	 */
	public TrackBlockModel(String blockName) {
		super();
		this.blockName = blockName;
	}

	/**
	 * Constructor
	 * @param points Points that belong to the track block
	 * @param adjacentBlocks Blocks that are adjacent to the the track block
	 * @param blockName Human-friendly name for the track block.
	 */
	public TrackBlockModel(List<TrackPointModel> points, List<TrackBlockModel> adjacentBlocks, String blockName) {
		super();
		this.points = points;
		this.adjacentBlocks = adjacentBlocks;
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
	public List<TrackPointModel> getPoints() {
		return points;
	}
	/**
	 * @return the adjacentBlocks
	 */
	public List<TrackBlockModel> getAdjacentBlocks() {
		return adjacentBlocks;
	}

}
