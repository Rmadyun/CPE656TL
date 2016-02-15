/**
 * Represents a single match in the TrackSwitch Repository database
 * @author Corey Sanders
 *
 */
public class TrackSwitchMatch {
	
	private String name;
	private String pointId;
	private String passBlockId;
	private String bypassBlockId;
	
	/**
	 * Constructor
	 * @param pointName Human friendly name to describe the switch
	 * @param pointId ID to the track point repository entry that describes the position of the switch
	 * @param passBlockId ID of the block when the switch is in pass mode.
	 * @param bypassBlockID ID of the block when the switch is in bypass mode.
	 */
	public TrackSwitchMatch(
			String pointName,
			String pointId,
			String passBlockId,
			String bypassBlockId){
		this.name = pointName;
		this.pointId = pointId;
		this.passBlockId = passBlockId;
		this.bypassBlockId = bypassBlockId;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
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

	/**
	 * @return the passBlockId
	 */
	public String getPassBlockId() {
		return passBlockId;
	}

	/**
	 * @param passBlockId the passBlockId to set
	 */
	public void setPassBlockId(String passBlockId) {
		this.passBlockId = passBlockId;
	}

	/**
	 * @return the bypassBlockId
	 */
	public String getBypassBlockId() {
		return bypassBlockId;
	}

	/**
	 * @param bypassBlockId the bypassBlockId to set
	 */
	public void setBypassBlockId(String bypassBlockId) {
		this.bypassBlockId = bypassBlockId;
	}
}
