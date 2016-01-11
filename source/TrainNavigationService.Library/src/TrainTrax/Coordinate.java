package TrainTrax;

/**
 * Class describes the location of an object in the world.
 * It assumes a coordinate system that uses x, y, and z where
 * x is the width from an origin point, y is the depth from an
 * origin point and z is the height from an origin point.
 * In a three-dimensional view of the world,
 * x tends to map to latitude, y tends to map to longitude, and
 * z tends to map to elevation.
 * @author Corey Sanders
 * 
 */
public class Coordinate {
	
	private double x;
	private double y;
	private double z;
	
	/**
	 * Constructor
	 * @param x Width from the origin point
	 * @param y Depth from the origin point
	 * @param z Height(Elevation) from the origin point
	 */
	public Coordinate(double x,double y, double z){
	    this.x = x;
	    this.y = y;
	    this.z = z;
	}
	
	/**
	 * Constructor
	 * @param x Width from the origin point
	 * @param y Depth from the origin point
	 * 
	 * Height is assumed to be zero.
	 */
	public Coordinate(double x,double y)  {
		this(x, y, 0);
	}
	
	/**
	 * Width from Origin
	 * @return width from Origin
	 */
	public double getX(){
		return x;
	}
	
	/**
	 * Set width from Origin
	 * @param x Width from Origin
	 */
	public void setX(double x){
		this.x = x;
	}
	
	/**
	 * Depth from Origin
	 * @return Depth from Origin
	 */
	public double getY(){
		return y;
	}
	
	/**
	 * Set depth from Origin
	 * @param y Depth from Origin
	 */
	public void setY(double y){
		this.y = y;
	}
	
	/**
	 * Height from Origin
	 * @return Height from Origin
	 */
	public double getZ(){
		return z;
	}
	
	/**
	 * Set height from Origin
	 * @param z Height from Origin
	 */
	public void setZ(double z){
		this.z = z;
	}
	
	/**
	 * Converts the class instance into a generic three-dimensional space
	 * vector to use for calculations
	 * @param coordinate Coordinate instance to convert
	 * @return A new ThreeDimensionalSpaceVector vector that represents the Coordinate value to convert.
	 */
	public static ThreeDimensionalSpaceVector ToThreeDimensionalSpaceVector(Coordinate coordinate){

		return new ThreeDimensionalSpaceVector(coordinate.x, coordinate.y, coordinate.z);
	}
}
