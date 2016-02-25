package com.traintrax.navigation.gui.models;
/**
 * Represents a single line to draw onto a canvas
 * @author Corey Sanders
 * 
 */
public class Edge {
	
	//TODO: Use Vertex class instead of Coordinate class.
	private Coordinate EndOne;
	private Coordinate EndTwo;
	
	public Edge(){
		this(new Coordinate(), new Coordinate());
	}
	
	public Edge(Coordinate endpointOne, Coordinate endpointTwo){
		this.EndOne = endpointOne;
		this.EndTwo = endpointTwo;
	}
	
	public Coordinate getEndOne() {
		return EndOne;
	}
	public void setEndOne(Coordinate endOne) {
		EndOne = endOne;
	}
	public Coordinate getEndTwo() {
		return EndTwo;
	}
	public void setEndTwo(Coordinate endTwo) {
		EndTwo = endTwo;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((EndOne == null) ? 0 : EndOne.hashCode());
		result = prime * result + ((EndTwo == null) ? 0 : EndTwo.hashCode());
		return result;
	}
	
	private boolean EndsMatch(Coordinate firstEnd, Coordinate secondEnd){
		boolean endsMatch = false;
		
		if(firstEnd == secondEnd){
			endsMatch = true;
		}
		else if(firstEnd == null || secondEnd == null){
			endsMatch = false;
		}
		else if(firstEnd.equals(secondEnd)&&secondEnd.equals(firstEnd)){
			endsMatch = true;
		}
		
		return endsMatch;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		
		boolean isEqual = false;
		if (this == obj)
		{
			isEqual = true;
		}
		else if(obj == null)
		{
			isEqual = false;
		}
		else if(getClass() != obj.getClass()){
			isEqual = false;
		}
		else {
			Edge other = (Edge) obj;
			
			if(EndsMatch(this.EndOne, other.EndOne) && EndsMatch(this.EndTwo, other.EndTwo)){
				isEqual = true;
			}
			else if(EndsMatch(this.EndTwo, other.EndOne) && EndsMatch(this.EndOne, other.EndTwo)){
				isEqual = true;
			}
			else{
				isEqual = false;
			}
			
		}
		
		return isEqual;
	}

	
	
}
