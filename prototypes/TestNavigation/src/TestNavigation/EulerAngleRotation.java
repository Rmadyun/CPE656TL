package TestNavigation;

/**
 * @author death
 *Class represents a Euler-Angle
 * representation of a rotation.
 */
public class EulerAngleRotation {

	private double radiansRotationAlongXAxis;
	private double radiansRotationAlongYAxis;
	private double radiansRotationAlongZAxis;
	
	///Constructor
	public EulerAngleRotation(double radiansRotationAlongXAxis,
			double radiansRotationAlongYAxis,
			double radiansRotationAlongZAxis){
		
		this.radiansRotationAlongXAxis = radiansRotationAlongXAxis;
		this.radiansRotationAlongYAxis = radiansRotationAlongYAxis;
		this.radiansRotationAlongZAxis = radiansRotationAlongZAxis;
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
	
private static double round(double initialValue, int numberOfFloatingPointDigits){
		
		int divisor = 10 *numberOfFloatingPointDigits;
		
		int dividend = (int) (initialValue * divisor);
		
		return ((double)dividend)/((double) divisor);
		
	}

    public EulerAngleRotation round(){
    	return round(8);
    }

    public EulerAngleRotation round(int numberOfFloatingPointDigits){
    	return round(this, numberOfFloatingPointDigits);
    }

    public static EulerAngleRotation round(EulerAngleRotation eulerAngleRotation, int numberOfFloatingPointDigits){
    	return new EulerAngleRotation(round(eulerAngleRotation.getRadiansRotationAlongXAxis(), numberOfFloatingPointDigits), 
    			round(eulerAngleRotation.getRadiansRotationAlongYAxis(), numberOfFloatingPointDigits),
    			round(eulerAngleRotation.getRadiansRotationAlongZAxis(), numberOfFloatingPointDigits));
    }
	
}
