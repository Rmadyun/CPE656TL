package com.traintrax.navigation.service.position;

/**
 * A collection of methods for frequently used
 * unit conversion operations performed by the service
 * @author Corey Sanders
 *
 */
public class UnitConversionUtilities {
	
	/**
	 * Conversion factor to convert position measurements from meters to inches
	 */
	private static final double MetersToInches = 100/2.54;
	
	/**
	 * Conversion factor to convert position measurements from inches to meters
	 */
	private static final double InchesToMeters = 2.54/100;
	
	/**
	 * Method converts coordinates expressed in meters to 
	 * coordinates expressed in inches
	 * @param coordinate Coordinate position in meters
	 * @return Coordinate position in inches
	 */
	public static Coordinate convertFromMetersToInches(Coordinate coordinate){
		
		Coordinate coordinateInInches = new Coordinate(coordinate.getX()*MetersToInches,
				coordinate.getY()*MetersToInches,
				coordinate.getZ()*MetersToInches);
		
		return coordinateInInches;
	}
	
	/**
	 * Method converts coordinates expressed in meters to 
	 * coordinates expressed in inches
	 * @param coordinate Coordinate position in meters
	 * @return Coordinate position in inches
	 */
	public static Coordinate convertFromInchesToMeters(Coordinate coordinate){
		Coordinate coordinateInMeters = new Coordinate(coordinate.getX()*InchesToMeters,
				coordinate.getY()*InchesToMeters,
				coordinate.getZ()*InchesToMeters);
		
		return coordinateInMeters;
	}

}
