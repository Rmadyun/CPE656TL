package edu.uah.cpe.traintrax;


import com.traintrax.navigation.database.library.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Class represents the positive train control test bed track that
 * a given set of trains moves around.
 * @author Corey Sanders
 *
 */
public class Track {
	
	private List<TrackBlockModel> trackBlockModels;
	
	/**
	 * Constructor
	 * @param trackBlockModels Track blocks that belong to the track.
	 */
	public Track(Collection<TrackBlockModel> trackBlockModels) {
		super();
		this.trackBlockModels = new ArrayList<TrackBlockModel>(trackBlockModels);
	}


	private static TrackPoint findPoint(List<RepositoryEntry<TrackPoint>> trackPoints, String id){
		TrackPoint trackPoint = null;
		
		for(RepositoryEntry<TrackPoint> entry : trackPoints){
			if(entry.getId().equals(id))
			{
				trackPoint = entry.getValue();
				break;
			}
		}
		
		return trackPoint;
	}

	/**
	 * Method creates a train track model from Train Navigation Database models
	 * @param trackBlocks Blocks that belong to the track
	 * @param trackPoints Points that belong to the track
	 * @param adjacentPointsList Description of which points are neighbors to each other.
	 * @return Returns a train track model representative of the information provided.
	 */
    public static Track createTrack(List<RepositoryEntry<TrackBlock>> trackBlocks, List<RepositoryEntry<TrackPoint>> trackPoints, List<RepositoryEntry<AdjacentPoint>> adjacentPointsList){
    	Track track = null;
    	HashMap<String, Vertex> vertexLut = new HashMap<String, Vertex>();
    	HashMap<String, TrackBlockModel> blockLut = new HashMap<String, TrackBlockModel>();
    	
    	//Create track blocks
    	for(RepositoryEntry<TrackBlock> entry : trackBlocks){
    		TrackBlock trackBlock = entry.getValue();
    		
    		//Save block in LUT for quick lookup
    		blockLut.put(entry.getId(), new TrackBlockModel(trackBlock.getBlockName()));
    	}
    	
    	//Create vertices
    	for(RepositoryEntry<TrackPoint> entry : trackPoints){
    		TrackPoint trackPoint = entry.getValue();
    		Vertex vertex = new Vertex(new Coordinate(trackPoint.getX(), trackPoint.getY()));
    		
    		//Save vertex in LUT for quick lookup
    		vertexLut.put(entry.getId(), vertex);
    		
    		//Connect vertex to block model
    		
    		TrackBlockModel trackBlockModel = blockLut.get(trackPoint.getBlockId());
    		
    		trackBlockModel.getPoints().add(vertex);
    	}
    	
    	//Associate vertices
    	for(RepositoryEntry<AdjacentPoint> entry : adjacentPointsList){
    		String pointId = Integer.toString(entry.getValue().getPointId());
    		String adjacentPointId = Integer.toString(entry.getValue().getAdjacenPointId());
    		Vertex pointVertex = vertexLut.get(pointId);
    		Vertex adjacentPointVertex = vertexLut.get(adjacentPointId);
    		
    		pointVertex.getAdjacentVertices().add(adjacentPointVertex);
    		
    		//Associate blocks
    		
    		TrackPoint point = findPoint(trackPoints, pointId);
    		TrackPoint adjacentPoint = findPoint(trackPoints, adjacentPointId);
    		
    		if(!point.getBlockId().equals(adjacentPoint.getBlockId())){
    			//Boundary points detected
    			
    			TrackBlockModel pointBlockModel = blockLut.get(point.getBlockId());
    			TrackBlockModel adjacentPointBlockModel = blockLut.get(adjacentPoint.getBlockId());
    			
    			//Make association between the blocks
    			pointBlockModel.getAdjacentBlocks().add(adjacentPointBlockModel);
    			adjacentPointBlockModel.getAdjacentBlocks().add(pointBlockModel);
    		}
    	}
    	
    	track = new Track(blockLut.values());
    	
    	
    	return track;
    }
	
	
	/**
	 * Grabs a sequence of shapes that can be drawn to
	 * represent the track.
	 * @return A sequence of shapes that can be drawn to
	 * represent the track.
	 */
	public List<Polygon> getShapes(){
		
		List<Vertex> allVertices = new ArrayList<Vertex>();
		List<Polygon> shapes = new ArrayList<Polygon>();
		
		for(TrackBlockModel trackBlock : trackBlockModels){
			
			allVertices.addAll(trackBlock.getPoints());
		}
		
		List<Edge> edges = Graph.CalculateEdges(allVertices);
		
		for(Edge edge : edges){
			List<Vertex> points = new ArrayList<Vertex>();
			
			
			Vertex endOneVertex = new Vertex(edge.getEndOne());
			Vertex endTwoVertex = new Vertex(edge.getEndTwo());
			points.add(endOneVertex);
			points.add(endTwoVertex);
			
			//Associate the points on the line
			endOneVertex.getAdjacentVertices().add(endTwoVertex);
			endTwoVertex.getAdjacentVertices().add(endOneVertex);
			
			//NOTE: We have lost all of the adjacency information in the transformation
			//other than the line association
			//TODO: Fix this by using Vertex class instead of Coordinate
			shapes.add(new Polygon(points));
		}
		
		return shapes;
	}

}
