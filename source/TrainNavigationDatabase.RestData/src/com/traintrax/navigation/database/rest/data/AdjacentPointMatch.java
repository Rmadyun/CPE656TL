package com.traintrax.navigation.database.rest.data;
import com.google.gson.annotations.SerializedName;

/**
 * Represents a single match in the AdjacentPoint Repository database
 * @author Corey Sanders
 *
 */
public class AdjacentPointMatch {
	
	@SerializedName("listId")
	private String listId;
	
	@SerializedName("pointId")
	private String pointId;
	
	@SerializedName("adjacentPointId")
	private String adjacentPointId;
	
	/**
	 * Constructor
	 * @param listId Unique identifier for a given adjacent point entry
	 * @param pointId ID of the point of interest
	 * @param adjacentPointId ID of a point that is adjacent to the point of interest
	 */
	public AdjacentPointMatch(
			String listId,
			String pointId,
			String adjacentPointId){
		this.listId = listId;
		this.pointId = pointId;
		this.adjacentPointId = adjacentPointId;
	}

	/**
	 * @return the listId
	 */
	public String getListId() {
		return listId;
	}

	/**
	 * @param listId the listId to set
	 */
	public void setListId(String listId) {
		this.listId = listId;
	}

	/**
	 * @return the pointId
	 */
	public String getPointId() {
		return pointId;
	}

	/**
	 * @param pointId the pointId to set
	 */
	public void setPointId(String pointId) {
		this.pointId = pointId;
	}

	/**
	 * @return the adjacentPointId
	 */
	public String getAdjacentPointId() {
		return adjacentPointId;
	}

	/**
	 * @param adjacentPointId the adjacentPointId to set
	 */
	public void setAdjacentPointId(String adjacentPointId) {
		this.adjacentPointId = adjacentPointId;
	}
	
}
