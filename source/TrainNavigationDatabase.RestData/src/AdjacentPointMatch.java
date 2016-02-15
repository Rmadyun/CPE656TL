/**
 * Represents a single match in the AdjacentPoint Repository database
 * @author Corey Sanders
 *
 */
public class AdjacentPointMatch {
	
	private String pointId;
	private String adjacentPointId;
	
	/**
	 * Constructor
	 * @param pointId ID of the point of interest
	 * @param adjacentPointId ID of a point that is adjacent to the point of interest
	 */
	public AdjacentPointMatch(
			String pointId,
			String adjacentPointId){
		
		this.pointId = pointId;
		this.adjacentPointId = adjacentPointId;
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
