package edu.uah.cpe.traintrax;
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
	 * @param value Value associated with the entryo
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


	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RepositoryEntry other = (RepositoryEntry) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
	
}
