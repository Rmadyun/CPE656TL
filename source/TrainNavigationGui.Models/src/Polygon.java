import java.util.ArrayList;
import java.util.List;

/**
 * Represents a generic multi-sided shape that can be rendered
 * 
 * @author death
 *
 */
public class Polygon {
	/**
	 * A sequence of vertices to follow to create a Polygon.
	 */
	private List<Vertex> vertices;

	public Polygon(List<Vertex> vertices) {
		this.vertices = vertices;
	}

	/**
	 * @return the vertices
	 */
	public List<Vertex> getVertices() {
		return vertices;
	}

	/**
	 * 
	 * @return
	 */
	public List<Edge> getEdges() {
		return Graph.CalculateEdges(vertices);
	}

}
