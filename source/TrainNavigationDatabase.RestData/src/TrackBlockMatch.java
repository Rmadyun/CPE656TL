/**
 * Represents a single match in the TrackBlock Repository database
 * @author Corey Sanders
 *
 */
public class TrackBlockMatch {
	
	private String blockName;

	/**
	 * Constructor
	 * @param blockName Human friendly name to describe the block
	 */
	public TrackBlockMatch(
			String blockName){
		this.blockName = blockName;
	}

	/**
	 * Retrieves the block name
	 * @return Human friendly name to describe the block
	 */
	public String getBlockName() {
		return blockName;
	}

	/**
	 * Assigns the block name
	 * @param blockName Human friendly name to describe the block
	 */
	public void setBlockName(String blockName) {
		this.blockName = blockName;
	}
	
}
