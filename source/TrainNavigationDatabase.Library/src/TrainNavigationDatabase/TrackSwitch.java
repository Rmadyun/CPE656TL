package TrainNavigationDatabase;

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

}
