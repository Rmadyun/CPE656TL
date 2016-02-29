package edu.uah.cpe.traintrax;

/**
 * Class describes how to filter searches
 * for TrackBlock objects within a collection or
 * repository.
 * @author Corey Sanders
 *
 */
public class TrackBlockSearchCriteria {

	private String blockName;
	
	/**
	 * Default Constructor
	 */
	public TrackBlockSearchCriteria(){
		this("");
	}
	
	/**
	 * Constructor
	 * @param name
	 * @param type
	 * @param blockId
	 * @param tagName
	 */
	public TrackBlockSearchCriteria(String blockName){
		this.blockName = blockName;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getBlockName(){
		return blockName;
	}
	
	/**
	 * 
	 * @param blockName
	 */
	public void setBlockName(String blockName){
		this.blockName = blockName;
	}
	
}
