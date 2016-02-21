import java.util.List;

public class Cycle {
	
	private List<Vertex> vertices;

	public Cycle(List<Vertex> vertices) {
		super();
		this.vertices = vertices;
	}
	
	

	/**
	 * @return the vertices
	 */
	public List<Vertex> getVertices() {
		return vertices;
	}



	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((vertices == null) ? 0 : vertices.hashCode());
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
		Cycle other = (Cycle) obj;
		if (vertices == null) {
			if (other.vertices != null)
				return false;
		} else{
		
			//Makes sure that both objects contain the same values, even if they are in a different order.
			//NOTE: This implementations means that two cycles that contain the same values, but have a 
			//different number of duplicates of each value still is counted as a match. Ex.
			//Cycle1: [0,0] [1,1] [1,1]
			//Cycle2: [0,0] [0,0] [1,1] 
			//Both Cycle1 and Cycle 2 would be matches
			
			//TODO: Create a Set Equals implementation that will resolve the limitation described above.
			//Since this is intended for elementary cycles in a graph, this limitation should not be an issue.
			
			return (vertices.size()== other.vertices.size())
					&&(vertices.containsAll(other.vertices));
		}
		
		return false;
	}
	
	
	
	
	
	

}
