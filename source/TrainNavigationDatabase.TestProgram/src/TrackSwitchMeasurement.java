/**
 * Represents a single point on a train track that has been
 * measured.
 * @author Corey Sanders
 *
 */
public class TrackSwitchMeasurement {
	
	private double xInches;
	private double yInches;
	private String blockName;
	private String switchName;
    private String passBlockName;
    private String bypassBlockName;
	
	/**
	 * Default Constructor
	 */
	public TrackSwitchMeasurement(){
		xInches = Double.NaN;
		yInches = Double.NaN;
		blockName = "";
		switchName = "";
		passBlockName = "";
		bypassBlockName = "";
	}
	
	/**
	 * @return the xInches
	 */
	public double getxInches() {
		return xInches;
	}

	/**
	 * @param xInches the xInches to set
	 */
	public void setxInches(double xInches) {
		this.xInches = xInches;
	}

	/**
	 * @return the yInches
	 */
	public double getyInches() {
		return yInches;
	}

	/**
	 * @param yInches the yInches to set
	 */
	public void setyInches(double yInches) {
		this.yInches = yInches;
	}

	/**
	 * @return the blockName
	 */
	public String getBlockName() {
		return blockName;
	}

	/**
	 * @param blockName the blockName to set
	 */
	public void setBlockName(String blockName) {
		this.blockName = blockName;
	}

	/**
	 * @return the switchName
	 */
	public String getSwitchName() {
		return switchName;
	}

	/**
	 * @param switchName the switchName to set
	 */
	public void setSwitchName(String switchName) {
		this.switchName = switchName;
	}

	/**
	 * @return the passBlockName
	 */
	public String getPassBlockName() {
		return passBlockName;
	}

	/**
	 * @param passBlockName the passBlockName to set
	 */
	public void setPassBlockName(String passBlockName) {
		this.passBlockName = passBlockName;
	}

	/**
	 * @return the bypassBlockName
	 */
	public String getBypassBlockName() {
		return bypassBlockName;
	}

	/**
	 * @param bypassBlockName the bypassBlockName to set
	 */
	public void setBypassBlockName(String bypassBlockName) {
		this.bypassBlockName = bypassBlockName;
	}


	
}
