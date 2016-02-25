package com.traintrax.navigation.database.library;

/**
 * Class describes how to filter searches
 * for TrackSwitch objects within a collection or
 * repository.
 * @author Corey Sanders
 *
 */
public class TrackSwitchSearchCriteria {
	
	private String switchName;
	private String pointId;
	private String passBlockId;
	private String bypassBlockId;
	
	/**
	 * Default Constructor
	 */
	public TrackSwitchSearchCriteria(){
		this("", "", "", "");
	}
	
	/**
	 * Constructor
	 * @param switchName
	 * @param pointId
	 * @param passBlockId
	 * @param bypassBlockId
	 */
	public TrackSwitchSearchCriteria(String switchName, String pointId, String passBlockId, String bypassBlockId){
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
	 * @param switchName
	 */
	public void setSwitchName(String switchName){
		this.switchName = switchName;
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
	 * @param pointId
	 */
	public void setPointId(String pointId){
		this.pointId = pointId;
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
	 * @param passBlockId
	 */
	public void setPassBlockId(String passBlockId){
		this.passBlockId = passBlockId;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getBypassBlockId(){
		return bypassBlockId;
	}
	
	/**
	 * 
	 * @param bypassBlockId
	 */
	public void setBypassBlockId(String bypassBlockId){
		this.bypassBlockId = bypassBlockId;
	}
	
}
