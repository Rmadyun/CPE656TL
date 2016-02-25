package com.traintrax.navigation.database.library;

/**
 * Class describes a single point located on a train track
 * @author Corey Sanders
 *
 */
public class TrackPoint {
	
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
	public TrackPoint(
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

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((blockId == null) ? 0 : blockId.hashCode());
		result = prime * result
				+ ((pointName == null) ? 0 : pointName.hashCode());
		result = prime * result + ((tagName == null) ? 0 : tagName.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		long temp;
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(z);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TrackPoint other = (TrackPoint) obj;
		if (blockId == null) {
			if (other.blockId != null)
				return false;
		} else if (!blockId.equals(other.blockId))
			return false;
		if (pointName == null) {
			if (other.pointName != null)
				return false;
		} else if (!pointName.equals(other.pointName))
			return false;
		if (tagName == null) {
			if (other.tagName != null)
				return false;
		} else if (!tagName.equals(other.tagName))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(	other.type))
			return false;
		if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
			return false;
		if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y))
			return false;
		if (Double.doubleToLongBits(z) != Double.doubleToLongBits(other.z))
			return false;
		return true;
	}

	
}
