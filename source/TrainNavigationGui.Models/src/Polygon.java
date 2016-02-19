import java.util.ArrayList;
import java.util.List;

/**
 * Represents a generic multi-sided shape that can be rendered
 * @author death
 *
 */
public class Polygon {
	/**
	 * A sequence of vertices to follow to create a Polygon.
	 */
	private List<Vertex> vertices;
	
	public Polygon(List<Vertex> vertices){
		this.vertices = vertices;
	}
	
	public static List<Line> GenerateLineSequence(Polygon polygon){
		List<Line> lineSequence = new ArrayList<Line>();
		
		//TODO: Create line
		
		return lineSequence;
	}
	
	public static void TraverseGraph(Vertex selectedVertex, List<Vertex> availableVertices, List<Vertex> verticesTravelSequence, List<Polygon> loops){
		//Use Depth-First-Search (TFS) to traverse the graph and discover the sequence to draw the graph.
		//List<Vertex> currentLoop
		
		//TODO: Get loop detection added
		//TODO: Track loops that are detected.
		
		//Vertex selectedVertex = null;
		
		//Make sure that we are not finished
		if(availableVertices.size() > 0 && availableVertices.contains(selectedVertex)){
			//selectedVertex = availableVertices.get(0); //grab the first one
			
			//Mark the selected vertex as traveled.
			verticesTravelSequence.add(selectedVertex);
			
			//Remove the selected vertex as available
			availableVertices.remove(selectedVertex);
			
			//Find the next vertex to select.
			for(Vertex adjacentVertex : selectedVertex.getAdjacentVertices()){
				
				//Make sure that we have never traveled the path before
				if(availableVertices.contains(adjacentVertex)){
				     TraverseGraph(adjacentVertex, availableVertices, verticesTravelSequence, loops);
				}
				else{
					//TODO: Record loops
					System.out.println("loop detected\n");
				}
				
			}
		}
		
	}

}
