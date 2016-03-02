package edu.uah.cpe.traintrax;
/**
 * Class describes how to filter searches
 * for AdjacentPoint objects within a collection or
 * repository.
 * @author Corey Sanders
 *
 */
public class AdjacentPointSearchCriteria {

	private String pointId;
	private String adjacentPointId;

	/**
	 * Constructor
	 * @param pointId
	 * @param adjacentPointId
	 */
	public AdjacentPointSearchCriteria(String pointId, String adjacentPointId){
		this.pointId = pointId;
		this.adjacentPointId = adjacentPointId;
	}
	
	/**
	 * Default constructor
	 */
	public AdjacentPointSearchCriteria() {
        this("", "");
	}

	/**
	 * 
	 * @return
	 */
	public String getPointId(){
		return pointId;
	}
	
	/**
	 * @param pointId 
	 */
	public void setPointId(String pointId) {
		this.pointId = pointId;
	}

	
	/**
	 * 
	 * @return
	 */
	public String getAdjacentPointId(){
		return adjacentPointId;
	}
	
	/**
	 * 
	 * @param adjacentPointId
	 */
	public void setAdjacentPointId(String adjacentPointId) {
		this.adjacentPointId = adjacentPointId;
	}

	
}
