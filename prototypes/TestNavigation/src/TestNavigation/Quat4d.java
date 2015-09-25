package TestNavigation;

///Class represents a Quaternion
///this is used for rotation calculations
///Name was inspired by http://www.euclideanspace.com/maths/geometry/rotations/conversions/quaternionToEuler/
public class Quat4d {
	
	public Quat4d(double w, double x, double y, double z){
		this.w = w;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Quat4d(){
		this.w = 0;
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}

	
	///Real / scalar component of the quaternion
	public double w;
	
	///First imaginary component
	public double x;
	
	///Second imaginary component
	public double y;
	
	///Third imaginary component
	public double z;
	
	public Quat4d add(Quat4d quaternion){
		return add(this, quaternion);
	}
	
	public static Quat4d add(Quat4d firstQuaternion, Quat4d secondQuaternion){
		Quat4d sum = new Quat4d();
		
		sum.w = firstQuaternion.w + secondQuaternion.w;
		sum.x = firstQuaternion.x + secondQuaternion.x;
		sum.y = firstQuaternion.y + secondQuaternion.y;
		sum.z = firstQuaternion.z + secondQuaternion.z;
		
		return sum;
	}
	
	public Quat4d conjugate(){
		return conjugate(this);
	}
	
	public static Quat4d conjugate(Quat4d quaternion){
		Quat4d conjugateQuaternion = new Quat4d(quaternion.w, -1*quaternion.x,
				-1*quaternion.y, -1*quaternion.z);

		return conjugateQuaternion;
	}
	
	public double norm(){
		return norm(this);
	}
	
	public static double norm(Quat4d quaternion){
		double wSquared = quaternion.w*quaternion.w;
		double xSquared = quaternion.x*quaternion.x;
		double ySquared = quaternion.y*quaternion.y;
		double zSquared = quaternion.z*quaternion.z;
		double quaternionNorm = Math.sqrt(wSquared + xSquared + ySquared + zSquared);
		
		return quaternionNorm;
	}
	
	public Quat4d multiply(Quat4d quaternion){
		return multiply(this, quaternion);
	}
	
	public static Quat4d multiply(Quat4d firstQuaternion, Quat4d secondQuaternion){
		Quat4d productQuaternion = new Quat4d();

		productQuaternion.w = (firstQuaternion.w*secondQuaternion.w) - (firstQuaternion.x*secondQuaternion.x) - (firstQuaternion.y * secondQuaternion.y) - (firstQuaternion.z * secondQuaternion.z);
		productQuaternion.x = (firstQuaternion.w*secondQuaternion.x) + (firstQuaternion.x*secondQuaternion.w) + (firstQuaternion.y * secondQuaternion.z) - (firstQuaternion.z * secondQuaternion.y);
		productQuaternion.y = (firstQuaternion.w*secondQuaternion.y) - (firstQuaternion.x*secondQuaternion.z) + (firstQuaternion.y * secondQuaternion.w) + (firstQuaternion.z * secondQuaternion.x);
		productQuaternion.z = (firstQuaternion.w*secondQuaternion.z) + (firstQuaternion.x*secondQuaternion.y) - (firstQuaternion.y * secondQuaternion.x) + (firstQuaternion.z * secondQuaternion.w);
		
		
		return productQuaternion;
	}
	
	public Quat4d multiply(double scalar){
		
		return multiply(this, scalar);
	}
	
	public static Quat4d multiply(Quat4d quaternion, double scalar){
		Quat4d productQuaternion = new Quat4d();
		
		productQuaternion.w = quaternion.w*scalar;
		productQuaternion.x = quaternion.x*scalar;
		productQuaternion.y = quaternion.y*scalar;
		productQuaternion.z = quaternion.z*scalar;
		
		return productQuaternion;
	}
	
	public Quat4d inverse(){
		return inverse(this);
	}
	
	public static Quat4d inverse(Quat4d quaternion){
		Quat4d inverseQuaternion;
		
		double norm = quaternion.norm();
		double normSquared = norm * norm;
		
		inverseQuaternion = quaternion.conjugate();
		inverseQuaternion = inverseQuaternion.multiply(1/normSquared);
		
		return inverseQuaternion;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(w);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(z);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Quat4d other = (Quat4d) obj;
		if (Double.doubleToLongBits(w) != Double.doubleToLongBits(other.w))
			return false;
		if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
			return false;
		if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y))
			return false;
		if (Double.doubleToLongBits(z) != Double.doubleToLongBits(other.z))
			return false;
		return true;
	}

}
