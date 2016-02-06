import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single point on a train track that has been
 * measured.
 * @author Corey Sanders
 *
 */
public class TrackPointMeasurement {
	
	private double xInches;
	private double yInches;
	private String blockName;
	private String pointName;
	private String rfidTagId;
	private String pointType;
	private List<TrackPointMeasurement> adjacentPoints;
	
	/**
	 * Default Constructor
	 */
	public TrackPointMeasurement(){
		xInches = Double.NaN;
		yInches = Double.NaN;
		blockName = "";
		pointName = "";
		rfidTagId = "";
		pointType = "";
		adjacentPoints = new ArrayList<TrackPointMeasurement>();
	}
	
	/**
	 * @return the xInches
	 */
	public double getxInches() {
		return xInches;
	}

	/**
	 * @param xInches the xInches to set
	 */
	public void setxInches(double xInches) {
		this.xInches = xInches;
	}

	/**
	 * @return the yInches
	 */
	public double getyInches() {
		return yInches;
	}

	/**
	 * @param yInches the yInches to set
	 */
	public void setyInches(double yInches) {
		this.yInches = yInches;
	}

	/**
	 * @return the blockName
	 */
	public String getBlockName() {
		return blockName;
	}

	/**
	 * @param blockName the blockName to set
	 */
	public void setBlockName(String blockName) {
		this.blockName = blockName;
	}

	/**
	 * @return the pointName
	 */
	public String getPointName() {
		return pointName;
	}

	/**
	 * @param pointName the pointName to set
	 */
	public void setPointName(String pointName) {
		this.pointName = pointName;
	}

	/**
	 * @return the rfidTagId
	 */
	public String getRfidTagId() {
		return rfidTagId;
	}

	/**
	 * @param rfidTagId the rfidTagId to set
	 */
	public void setRfidTagId(String rfidTagId) {
		this.rfidTagId = rfidTagId;
	}

	/**
	 * @return the pointType
	 */
	public String getPointType() {
		return pointType;
	}

	/**
	 * @param pointType the pointType to set
	 */
	public void setPointType(String pointType) {
		this.pointType = pointType;
	}

	/**
	 * @return the adjacentPoints
	 */
	public List<TrackPointMeasurement> getAdjacentPoints() {
		return adjacentPoints;
	}

	/**
	 * @param adjacentPoints the adjacentPoints to set
	 */
	public void setAdjacentPoints(List<TrackPointMeasurement> adjacentPoints) {
		this.adjacentPoints = adjacentPoints;
	}
	
}
