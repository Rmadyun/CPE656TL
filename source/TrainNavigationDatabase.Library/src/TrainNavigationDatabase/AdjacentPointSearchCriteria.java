package TrainNavigationDatabase;

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
	 * 
	 * @return
	 */
	public String getPointId(){
		return pointId;
	}
	
	/**
	 * @param pointId 
	 */
	public void setPointId(int pointId) {
		this.pointId = pointId;
	}

	
	/**
	 * 
	 * @return
	 */
	public String getAdjacenPointId(){
		return adjacentPointId;
	}
	
	/**
	 * 
	 * @param adjacentPointId
	 */
	public void setAdjacentPointId(int adjacentPointId) {
		this.adjacentPointId = adjacentPointId;
	}

	
}
