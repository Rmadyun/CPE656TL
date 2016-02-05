package TrainNavigationDatabase;

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

}
