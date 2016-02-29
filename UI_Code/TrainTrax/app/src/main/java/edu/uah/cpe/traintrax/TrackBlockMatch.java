package edu.uah.cpe.traintrax;
import com.google.gson.annotations.SerializedName;

/**
 * Represents a single match in the TrackBlock Repository database
 * @author Corey Sanders
 *
 */
public class TrackBlockMatch {
	
	@SerializedName("blockId")
	private String blockId;
	
	@SerializedName("blockName")
	private String blockName;

	/**
	 * Constructor
	 * @param blockId Unique identifier for the track block entry
	 * @param blockName Human friendly name to describe the block
	 */
	public TrackBlockMatch(
			String blockId,
			String blockName){
		this.blockId = blockId;
		this.blockName = blockName;
	}

	/**
	 * Retrieves the unique identifier for the track block entry
	 * @return Unique identifier for the track block entry
	 */
	public String getBlockId() {
		return blockId;
	}

	/**
	 * Assigns the block ID associated with the entry
	 * @param blockId Unique identifier for the track block entry
	 */
	public void setBlockId(String blockId) {
		this.blockId = blockId;
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
