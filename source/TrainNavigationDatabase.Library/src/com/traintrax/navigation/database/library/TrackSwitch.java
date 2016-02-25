package com.traintrax.navigation.database.library;

/**
 * 
 * @author Corey Sanders
 *
 */
public class TrackSwitch {
	
	private String switchName;
	private String pointId;
	private String passBlockId;
	private String bypassBlockId;
	
	/**
	 * Constructor
	 * @param switchName
	 * @param pointId
	 * @param passBlockId
	 * @param bypassBlockId
	 */
	public TrackSwitch(String switchName, String pointId, String passBlockId, String bypassBlockId){
	    this.switchName = switchName;
	    this.pointId = pointId;
	    this.passBlockId = passBlockId;
	    this.bypassBlockId = bypassBlockId;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getSwitchName(){
		return switchName;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getPointId(){
		return pointId;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getPassBlockId(){
		return passBlockId;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getBypassBlockId(){
		return bypassBlockId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((bypassBlockId == null) ? 0 : bypassBlockId.hashCode());
		result = prime * result
				+ ((passBlockId == null) ? 0 : passBlockId.hashCode());
		result = prime * result + ((pointId == null) ? 0 : pointId.hashCode());
		result = prime * result
				+ ((switchName == null) ? 0 : switchName.hashCode());
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
		TrackSwitch other = (TrackSwitch) obj;
		if (bypassBlockId == null) {
			if (other.bypassBlockId != null)
				return false;
		} else if (!bypassBlockId.equals(other.bypassBlockId))
			return false;
		if (passBlockId == null) {
			if (other.passBlockId != null)
				return false;
		} else if (!passBlockId.equals(other.passBlockId))
			return false;
		if (pointId == null) {
			if (other.pointId != null)
				return false;
		} else if (!pointId.equals(other.pointId))
			return false;
		if (switchName == null) {
			if (other.switchName != null)
				return false;
		} else if (!switchName.equals(other.switchName))
			return false;
		return true;
	}

}
