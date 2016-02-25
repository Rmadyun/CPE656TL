package com.traintrax.navigation.gui.models;
import java.util.ArrayList;
import java.util.List;

import com.traintrax.navigation.database.library.AdjacentPoint;
import com.traintrax.navigation.database.library.RepositoryEntry;
import com.traintrax.navigation.database.library.TrackBlock;
import com.traintrax.navigation.database.library.TrackPoint;
import com.traintrax.navigation.database.rest.test.TestAdjacentPointRepository;
import com.traintrax.navigation.database.rest.test.TestTrackBlockRepository;
import com.traintrax.navigation.database.rest.test.TestTrackPointRepository;

import cycles_johnson_meyer.ElementaryCyclesSearch;

public class TestModels {
	
	/**
	 * This method is a helper method to check whether the current algorithms being used
	 * to navigate the graph correctly report all of the information necessary to draw an
	 * arbitrary graph in its entirety.
	 */
	private static void TestPathDetection(){
		List<Coordinate> pointSequence = new ArrayList<Coordinate>();

		//Test Sequence of points with loops and additional edges
		pointSequence.add(new Coordinate(0, 1));
		pointSequence.add(new Coordinate(1, 1));
		pointSequence.add(new Coordinate(1, 0));
		pointSequence.add(new Coordinate(0, 0));
		pointSequence.add(new Coordinate(0, -1));
		pointSequence.add(new Coordinate(-1, -1));
		pointSequence.add(new Coordinate(-1, 0));
		pointSequence.add(new Coordinate(0, 0));
		
		//Uncomment to make test sequence include just two loops
		//pointSequence.add(new Coordinate(0, 1));


		List<Vertex> vertices = generateVertices(pointSequence);
		Vertex selectedVertex = vertices.get(0);
		List<Vertex> availableVertices = new ArrayList<Vertex>(vertices);
		List<Polygon> loops = new ArrayList<Polygon>();

		// Polygon.TraverseGraph(selectedVertex, vertices,
		// verticesTravelSequence, loops);

		//Create the input necessary for the elementary cycles search
		boolean[][] adjMatrix = generateAdjacencyMatrix(vertices);
		Object[] nodes = vertices.toArray();
		
		//Perform the elementary cycles search
		ElementaryCyclesSearch ecs = new ElementaryCyclesSearch(adjMatrix, nodes);
		List cycles = ecs.getElementaryCycles();

		List<Cycle> loopsToDraw = new ArrayList<Cycle>();
		
		List<Graph> shapesToDraw = new ArrayList<>();

		//Process the elementary cycles detected to only include
		//Unique loops that are not simply a line.
		for (int i = 0; i < cycles.size(); i++) {
			List cycle = (List) cycles.get(i);

			if (cycle.size() > 2) { // Ignore the trivial cycles
				List<Vertex> cycleVertices = new ArrayList<Vertex>();
				for (int j = 0; j < cycle.size(); j++) {
					Vertex node = (Vertex) cycle.get(j);

					cycleVertices.add(node);
				}

				Cycle currentCycle = new Cycle(cycleVertices);

				if (!loopsToDraw.contains(currentCycle)) {
					loopsToDraw.add(currentCycle);

					shapesToDraw.add(new Graph(currentCycle.getVertices()));
					
					for (Vertex vertex : currentCycle.getVertices()) {
						if(availableVertices.contains(vertex)){
							availableVertices.remove(vertex);
						}
						
						//System.out.print(vertex.getPosition() + " ");
					}
					//System.out.print("\n");
				}
			}
		}
		
		//Find the remaining edges that are not part of any loop, and
		//express paths to traverse them all.
		Graph.CreateAcyclicSubGraphs(availableVertices, shapesToDraw);
		
		//shapesToDraw.clear();
		
		//Graph.createPaths(new Graph(vertices), shapesToDraw);
		
		//Report all of the edges that have been accounted for in order
		//to render the graph.
		System.out.println("Polygons: \n");
		for(Graph polygon : shapesToDraw){
			System.out.println("---------Start of Polygon--------");
			for (Edge edge : polygon.getEdges()) {
				System.out.print(edge.getEndOne() + " + " + edge.getEndTwo());
				System.out.print("\n");
			}
			System.out.println("---------End of Polygon--------");
			
		}
		
		System.out.println("Processing Finished");
	}

	public static void main(String[] args) {

		
        TestTrackBlockRepository trackBlockRepository = new TestTrackBlockRepository();
        TestTrackPointRepository trackPointRepository = new TestTrackPointRepository();
        TestAdjacentPointRepository adjacentPointRepository = new TestAdjacentPointRepository();

        List<RepositoryEntry<TrackBlock>> trackBlocks = trackBlockRepository.findAll();
        List<RepositoryEntry<TrackPoint>> trackPoints = trackPointRepository.findAll();
        List<RepositoryEntry<AdjacentPoint>> adjacentPoints = adjacentPointRepository.findAll();

        Track trainTrack = Track.createTrack(trackBlocks, trackPoints, adjacentPoints);

        List<Polygon> shapes = trainTrack.getShapes();
        
		System.out.println("Polygons: \n");
		for(Polygon shape : shapes){
			System.out.println("---------Start of Polygon--------");
			for (Edge edge : shape.getEdges()) {
				System.out.print(edge.getEndOne() + " + " + edge.getEndTwo());
				System.out.print("\n");
			}
			System.out.println("---------End of Polygon--------");
			
		}
		
		System.out.println("Processing Finished");

	}
	


	static boolean[][] generateAdjacencyMatrix(List<Vertex> vertices) {
		int num_vertices = vertices.size();
		boolean adjMatrix[][] = new boolean[num_vertices][num_vertices];

		// Using the assumption that the default value for a 'boolean' is false.

		for (int i = 0; i < num_vertices; i++) {
			Vertex selectedVertex = vertices.get(i);
			for (Vertex adjacentVertex : selectedVertex.getAdjacentVertices()) {
				int adjacentVertexIndex = vertices.indexOf(adjacentVertex);

				adjMatrix[i][adjacentVertexIndex] = true;
			}
		}

		return adjMatrix;

	}

	/**
	 * Searched for a vertex in an list
	 * 
	 * @param coordinate
	 *            location of the vertex we want
	 * @param vertices
	 *            List of vertices to search through
	 * @return Returns the vertex match. If none are found, return null.
	 */
	private static Vertex findVertex(Coordinate coordinate, List<Vertex> vertices) {
		Vertex vertexEntry = null;

		if (coordinate != null) {
			Vertex tempVertex = new Vertex(coordinate);

			if (vertices.contains(tempVertex)) {
				vertexEntry = vertices.get(vertices.indexOf(tempVertex));
			}
		}

		return vertexEntry;
	}

	private static List<Vertex> generateVertices(List<Coordinate> pointSequence) {
		List<Vertex> vertices = new ArrayList<Vertex>();

		int numberOfPoints = pointSequence.size();

		for (int i = 0; i < numberOfPoints; i++) {
			Coordinate point = pointSequence.get(i);
			Coordinate previousPoint = null;
			Coordinate nextPoint = null;

			if (i > 0) {
				// behind
				previousPoint = pointSequence.get(i - 1);
			}

			if (i < (numberOfPoints - 1)) {
				// ahead
				nextPoint = pointSequence.get(i + 1);
			}

			Vertex pointVertex = findVertex(point, vertices);
			Vertex nextPointVertex = findVertex(nextPoint, vertices);
			Vertex previousPointVertex = findVertex(previousPoint, vertices);

			// Create a new vertices where needed
			if (pointVertex == null) {
				pointVertex = new Vertex(point);

				// Add to the vertices list
				vertices.add(pointVertex);
			}

			if (nextPointVertex == null && nextPoint != null) {
				nextPointVertex = new Vertex(nextPoint);

				// Add to the vertices list
				vertices.add(nextPointVertex);
			}

			if (previousPointVertex == null && previousPoint != null) {
				previousPointVertex = new Vertex(previousPoint);

				// Add to the vertices list
				vertices.add(previousPointVertex);

				System.out.println("Error: A previous vertex point was not already created in the"
						+ "when it was time to process the current vertex. ");
			}

			// Update adjacent relationships
			if (previousPointVertex != null) {
				pointVertex.getAdjacentVertices().add(previousPointVertex);
			}

			if (nextPointVertex != null) {
				pointVertex.getAdjacentVertices().add(nextPointVertex);
			}
		}

		return vertices;

	}

}
