/**
 * Represents a single line to draw onto a canvas
 * @author Corey Sanders
 *
 */
public class Line {
	
	private Coordinate EndOne;
	private Coordinate EndTwo;
	
	public Line(){
		this(new Coordinate(), new Coordinate());
	}
	
	public Line(Coordinate endpointOne, Coordinate endpointTwo){
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

	
}
