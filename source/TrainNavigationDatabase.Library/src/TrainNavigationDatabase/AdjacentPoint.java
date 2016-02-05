package TrainNavigationDatabase;

/**
 * 
 * @author Corey Sanders
 *
 */
public class AdjacentPoint {
	
	private int pointId;
	private int adjacentPointId;
	
	/**
	 * Constructor
	 * @param pointId
	 * @param adjacentPointId
	 */
	public AdjacentPoint(int pointId, int adjacentPointId){
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
	 * 
	 * @return
	 */
	public int getAdjacenPointId(){
		return adjacentPointId;
	}

}
