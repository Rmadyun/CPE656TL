import java.util.ArrayList;
import java.util.List;

/**
 * Class represents a generic graph.
 * 
 * @author Corey Sanders
 *
 */
public class Graph {
	// Adjacency List
	private List<Vertex> vertices;

	// Edges that belong to the graph
	private List<Edge> edges;

	/**
	 * Constructor
	 * 
	 * @param vertices
	 *            All of the vertices that belong to the graph. This needs to be
	 *            pre-populated with adjacent point information
	 */
	public Graph(List<Vertex> vertices) {
		super();
		this.vertices = vertices;
		this.edges = CalculateEdges(vertices);
	}

	/**
	 * @return the vertices
	 */
	public List<Vertex> getVertices() {
		return vertices;
	}

	/**
	 * @return the edges
	 */
	public List<Edge> getEdges() {
		return edges;
	}

	/**
	 * Calculates all of the edges associated with the collection of vertices.
	 * 
	 * @param vertices
	 *            Collection of vertices with information about adjacent
	 *            vertices pre-populated
	 * @return All of the edges associated with the provided collection of
	 *         vertices.
	 */
	public static List<Edge> CalculateEdges(List<Vertex> vertices) {
		List<Edge> edges = new ArrayList<Edge>();

		// Find all of the unique edges in the graph.
		for (Vertex vertex : vertices) {
			for (Vertex adjacentVertex : vertex.getAdjacentVertices()) {

				// Makes sure that vertices are within the set
				if (vertices.contains(adjacentVertex)) {

					Edge edge = new Edge(vertex.getPosition(), adjacentVertex.getPosition());

					if (!edges.contains(edge)) {
						edges.add(edge);
					}
				}
			}
		}

		return edges;
	}

	/**
	 * Method creates acyclic sub graphs out of unused vertices of the graph.
	 * 
	 * @param availableVertices
	 *            Available vertices to use to create subgraphs
	 * @param subGraphs
	 *            List of subGraphs that have been completed
	 */
	public static void CreateAcyclicSubGraphs(List<Vertex> availableVertices, List<Graph> subGraphs) {

		while (availableVertices.size() > 0) {
			// Find an acyclic portion of the graph. (This is necessary to
			// facilitate creating a complete
			// drawing of the graph.
			List<Vertex> subGraph = CreateAcyclicSubGraph(availableVertices.get(0), availableVertices);

			// Attach sub graph to the graph
			if (subGraph.size() == 1) {
				Vertex selectedVertex = subGraph.get(0);

				// Attach all of the vertices that do not belong to the subgraph
				for (Vertex adjacentVertex : selectedVertex.getAdjacentVertices()) {
					if (!subGraph.contains(adjacentVertex)) {
						subGraph.add(adjacentVertex);
					}
				}

				subGraphs.add(new Graph(subGraph));
			} else if (subGraph.size() > 1) {

				Vertex firstVertex = subGraph.get(0);
				Vertex lastVertex = subGraph.get(subGraph.size() - 1);

				// Attach all of the vertices that do not belong to the subgraph
				for (Vertex adjacentVertex : firstVertex.getAdjacentVertices()) {
					if (!subGraph.contains(adjacentVertex)) {
						subGraph.add(adjacentVertex);
					}
				}

				// Attach all of the vertices that do not belong to the subgraph
				for (Vertex adjacentVertex : lastVertex.getAdjacentVertices()) {
					if (!subGraph.contains(adjacentVertex)) {
						subGraph.add(adjacentVertex);
					}
				}

				subGraphs.add(new Graph(subGraph));
			}
		}

	}

	/**
	 * Creates an acyclic subgraph from the remaining unused vertices in the
	 * graph.
	 * 
	 * @param availableVertices
	 *            Unused vertices. This is updated as the acyclic graph is made
	 *            to eliminate used vertices
	 * @return a subgraph composed of previously unused vertices.
	 */
	public static List<Vertex> CreateAcyclicSubGraph(Vertex startingVertex, List<Vertex> availableVertices) {
		List<Vertex> graph = new ArrayList<Vertex>();

		if (availableVertices.size() > 0) {

			graph.add(startingVertex);
			availableVertices.remove(startingVertex);

			for (Vertex adjacentVertex : startingVertex.getAdjacentVertices()) {
				if (availableVertices.contains(adjacentVertex)) {

					List<Vertex> subGraph = CreateAcyclicSubGraph(adjacentVertex, availableVertices);

					graph.addAll(subGraph);
				}
			}
		}

		return graph;
	}

	/* Ignore, not functional
	public static void createPaths(Graph graph, List<Graph> paths) {

		List<Vertex> availableVertices = new ArrayList<>(graph.getVertices());

		for (Vertex vertex : graph.getVertices()) {
			if (availableVertices.isEmpty()) {
				break;
			} else {
				if (availableVertices.contains(vertex)) {
					List<Vertex> pathVertices = createPath(vertex, availableVertices);
					Graph pathGraph = new Graph(pathVertices);
					paths.add(pathGraph);
				}
			}
		}

	}

	public static List<Vertex> createPath(Vertex startingVertex, List<Vertex> availableVertices) {
		List<Vertex> path = new ArrayList<Vertex>();

		path.add(startingVertex);

		// Perform DFS to create a path as long as there are reachable available
		// vertices.

		if (!availableVertices.isEmpty()) {

			for (Vertex adjacentVertex : startingVertex.getAdjacentVertices()) {

				if (availableVertices.contains(adjacentVertex)) {
					availableVertices.remove(adjacentVertex);
					List<Vertex> subPath = createPath(adjacentVertex, availableVertices);

					path.addAll(subPath);

					break;
				}
			}
		}

		return path;
	} */

}
