package edu.uah.cpe.traintrax;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + adjacentPointId;
		result = prime * result + pointId;
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
		AdjacentPoint other = (AdjacentPoint) obj;
		if (adjacentPointId != other.adjacentPointId)
			return false;
		if (pointId != other.pointId)
			return false;
		return true;
	}

	
}
