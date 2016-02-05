package TrainNavigationDatabase;

/**
 * Class describes how to filter searches
 * for AdjacentPoint objects within a collection or
 * repository.
 * @author Corey Sanders
 *
 */
public class AdjacentPointSearchCriteria {

	private int pointId;
	private int adjacentPointId;

	/**
	 * Constructor
	 * @param pointId
	 * @param adjacentPointId
	 */
	public AdjacentPointSearchCriteria(int pointId, int adjacentPointId){
		this.pointId = pointId;
		this.adjacentPointId = adjacentPointId;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getPointId(){
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
	public int getAdjacenPointId(){
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
