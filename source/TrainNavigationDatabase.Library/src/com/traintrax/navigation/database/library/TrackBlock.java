package com.traintrax.navigation.database.library;

/**
 * 
 * @author Corey Sanders
 *
 */
public class TrackBlock {
	
	private String blockName;
	
	/**
	 * Constructor
	 * @param blockName
	 */
	public TrackBlock(String blockName){
		this.blockName = blockName;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getBlockName(){
		return blockName;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((blockName == null) ? 0 : blockName.hashCode());
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
		TrackBlock other = (TrackBlock) obj;
		if (blockName == null) {
			if (other.blockName != null)
				return false;
		} else if (!blockName.equals(other.blockName))
			return false;
		return true;
	}

}
