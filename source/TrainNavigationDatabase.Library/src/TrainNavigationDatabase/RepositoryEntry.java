package TrainNavigationDatabase;
/**
 * Represents a single item that is stored in a repository
 * @author Corey Sanders
 *
 * @param <TData> Type of data associated stored in the repository
 */
public class RepositoryEntry<TData> {

	private TData value;
	private String id;
	
	/**
	 * Constructor
	 * @param id Unique identifier for the entry
	 * @param value Value associated with the entry
	 */
	public RepositoryEntry(String id, TData value){
		this.id = id;
		this.value = value;
	}
	
	
	/**
	 * Retrieve the unique identifier for the entry
	 * @return ID assigned to the entry
	 */
	public String getId(){
		return id;
	}
	
	/**
	 * Retrieve the value associated with the entry
	 * @return value associated with the entry 
	 */
	public TData getValue() {
		return value;
	}
	
}
