/**
 * Represents a single match in the TrackPoint Repository database
 * @author Corey Sanders
 *
 */
public class TrackPointMatch {
	
	private String pointName;
	private String type;
	private double x;
	private double y;
	private double z;
	private String blockId;
	private String tagName;
	
	/**
	 * Constructor
	 * @param pointName Human friendly name to describe the point
	 * @param type Describes the type of point
	 * @param x Width in inches from the origin
	 * @param y Depth in inches from the origin
	 * @param z Height in inches from the origin
	 * @param blockId Unique identifier for the block associated with the point
	 * @param tagName Identifier for the RFID tag associated with the point.
	 */
	public TrackPointMatch(
			String pointName,
			String type,
			double x,
			double y,
			double z,
			String blockId,
			String tagName){
		this.pointName = pointName;
		this.type = type;
		this.x = x;
		this.y = y;
		this.z = z;
		this.blockId = blockId;
		this.tagName = tagName;
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
