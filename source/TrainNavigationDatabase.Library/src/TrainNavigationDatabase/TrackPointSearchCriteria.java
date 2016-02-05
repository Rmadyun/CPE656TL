package TrainNavigationDatabase;

/**
 * Class describes how to filter searches
 * for TrackPoint objects within a collection or
 * repository.
 * @author Corey Sanders
 *
 */
public class TrackPointSearchCriteria {

	private String name;
	private String type;
	private String blockId;
	private String tagName;
	
	/**
	 * Default Constructor
	 */
	public TrackPointSearchCriteria(){
		this("", "", "", "");
	}
	
	/**
	 * Constructor
	 * @param name
	 * @param type
	 * @param blockId
	 * @param tagName
	 */
	public TrackPointSearchCriteria(String name, String type, String blockId, String tagName){
		this.name = name;
		this.type = type;
		this.blockId = blockId;
		this.tagName = tagName;
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getType(){
		return type;
	}
	
	public void setType(String type){
		this.type = type;
	}
	
	public String getBlockId(){
		return blockId;
	}
	
	public void setBlockId(String blockId){
		this.blockId = blockId;
	}
	
	public String getTagName(){
		return tagName;
	}
	
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	
}
