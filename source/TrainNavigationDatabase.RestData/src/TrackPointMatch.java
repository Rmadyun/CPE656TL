import com.google.gson.annotations.SerializedName;

/**
 * Represents a single match in the TrackPoint Repository database
 * @author Corey Sanders
 *
 */
public class TrackPointMatch {
	
	@SerializedName("pointId")
	private String pointId;
	
	@SerializedName("pointName")
	private String pointName;
	
	@SerializedName("type")
	private String type;
	
	@SerializedName("x")
	private double x;
	
	@SerializedName("y")
	private double y;
	
	@SerializedName("z")
	private double z;
	
	@SerializedName("blockId")
	private String blockId;
	
	@SerializedName("tagName")
	private String tagName;
	
	/**
	 * Constructor
	 * @param pointId Unique identifier for a given track point entry
	 * @param pointName Human friendly name to describe the point
	 * @param type Describes the type of point
	 * @param x Width in inches from the origin
	 * @param y Depth in inches from the origin
	 * @param z Height in inches from the origin
	 * @param blockId Unique identifier for the block associated with the point
	 * @param tagName Identifier for the RFID tag associated with the point.
	 */
	public TrackPointMatch(
			String pointId,
			String pointName,
			String type,
			double x,
			double y,
			double z,
			String blockId,
			String tagName){
		this.pointId = pointId;
		this.pointName = pointName;
		this.type = type;
		this.x = x;
		this.y = y;
		this.z = z;
		this.blockId = blockId;
		this.tagName = tagName;
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

	public String getPointName(){
		return pointName;
	}
	
	public String getType(){
		return type;
	}
	
	public double getX(){
		return x;
	}
	
	public double getY(){
		return y;
	}
	
	public double getZ(){
		return z;
	}
	
	public String getBlockId(){
		return blockId;
	}
	
	public String getTagName(){
		return tagName;
	}

}
