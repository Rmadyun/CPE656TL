package TrainTrax;

/**
 * @author death
 *Class represents a Euler-Angle
 * representation of a rotation.
 */
public class EulerAngleRotation {

	private double radiansRotationAlongXAxis;
	private double radiansRotationAlongYAxis;
	private double radiansRotationAlongZAxis;
	
	
	/**
	 * Constructor
	 * @param radiansRotationAlongXAxis
	 * @param radiansRotationAlongYAxis
	 * @param radiansRotationAlongZAxis
	 */
	public EulerAngleRotation(double radiansRotationAlongXAxis,
			double radiansRotationAlongYAxis,
			double radiansRotationAlongZAxis){
		
		this.radiansRotationAlongXAxis = radiansRotationAlongXAxis;
		this.radiansRotationAlongYAxis = radiansRotationAlongYAxis;
		this.radiansRotationAlongZAxis = radiansRotationAlongZAxis;
	}
	
	/**
	 * Constructor
	 * @param threeDimensionalSpaceVector
	 */
	public EulerAngleRotation(
			ThreeDimensionalSpaceVector threeDimensionalSpaceVector) {
		this(threeDimensionalSpaceVector.getX(),
				threeDimensionalSpaceVector.getY(),
				threeDimensionalSpaceVector.getZ());
	}

	///Describes the degree of rotation from the original position
	///along the X axis of the reference frame in radians
	public double getRadiansRotationAlongXAxis(){
		return radiansRotationAlongXAxis;
	}

	///Describes the degree of rotation from the original position
	///along the Y axis of the reference frame in radians
	public double getRadiansRotationAlongYAxis() {
		return radiansRotationAlongYAxis;
	}
	
	///Describes the degree of rotation from the original position
	///along the Z axis of the reference frame in radians
	public double getRadiansRotationAlongZAxis() {
		return radiansRotationAlongZAxis;
	}
	


    public EulerAngleRotation round(){
    	return round(8);
    }

    public EulerAngleRotation round(int numberOfFloatingPointDigits){
    	return round(this, numberOfFloatingPointDigits);
    }

    public static EulerAngleRotation round(EulerAngleRotation eulerAngleRotation, int numberOfFloatingPointDigits){
    	return new EulerAngleRotation(Calculate.round(eulerAngleRotation.getRadiansRotationAlongXAxis(), numberOfFloatingPointDigits), 
    			Calculate.round(eulerAngleRotation.getRadiansRotationAlongYAxis(), numberOfFloatingPointDigits),
    			Calculate.round(eulerAngleRotation.getRadiansRotationAlongZAxis(), numberOfFloatingPointDigits));
    }
    
	/**
	 * Converts the class instance into a generic three-dimensional space
	 * vector to use for calculations
	 * @param eulerAngleRotation EulerAngleRotation instance to convert
	 * @return A new ThreeDimensionalSpaceVector vector that represents the EulerAngleRotation value to convert.
	 */
	public static ThreeDimensionalSpaceVector ToThreeDimensionalSpaceVector(EulerAngleRotation eulerAngleRotation){

		return new ThreeDimensionalSpaceVector(eulerAngleRotation.radiansRotationAlongXAxis,
				eulerAngleRotation.radiansRotationAlongYAxis, 
				eulerAngleRotation.radiansRotationAlongZAxis);
	}
	
	/**
	 * Converts a generic three-dimensional space vector into an 
	 * EulerAngleRotation class instance.
	 * @param threeDimensionalSpaceVector 3-D vector representation of acceleration to convert
	 * @return A new Acceleration instance that represents 3-D vector value.
	 */
	public static EulerAngleRotation FromThreeDimensionalSpaceVector(ThreeDimensionalSpaceVector threeDimensionalSpaceVector){

		return new EulerAngleRotation(threeDimensionalSpaceVector);
	}
	
}
