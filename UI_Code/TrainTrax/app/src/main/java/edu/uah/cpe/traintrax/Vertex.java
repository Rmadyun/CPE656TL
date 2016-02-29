package edu.uah.cpe.traintrax;

import java.util.ArrayList;
import java.util.List;

/**
 * Information about a given corner of a graph
 * @author Corey Sanders
 *
 */
public class Vertex {
	
	private Coordinate position;
	
	private List<Vertex> adjacentVertices;
	
	public Vertex(Coordinate position){
		this.position = position;
		adjacentVertices = new ArrayList<Vertex>();
	}

	public Coordinate getPosition() {
		return position;
	}

	public void setPosition(Coordinate position) {
		this.position = position;
	}

	public List<Vertex> getAdjacentVertices() {
		return adjacentVertices;
	}

	public void setAdjacentVertices(List<Vertex> adjacentVertices) {
		this.adjacentVertices = adjacentVertices;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((position == null) ? 0 : position.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vertex other = (Vertex) obj;
		if (position == null) {
			if (other.position != null)
				return false;
		} else if (!position.equals(other.position))
			return false;
		return true;
	}

	
}
