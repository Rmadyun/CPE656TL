package com.traintrax.navigation.service.position;

import com.traintrax.navigation.service.math.ThreeDimensionalSpaceVector;

/**
 * A collection of methods for frequently used unit conversion operations
 * performed by the service
 * 
 * @author Corey Sanders
 *
 */
public class UnitConversionUtilities {

	/**
	 * Conversion factor to convert position measurements from meters to inches
	 */
	public static final double MetersToInches = 100 / 2.54;

	/**
	 * Conversion factor to convert position measurements from inches to meters
	 */
	public static final double InchesToMeters = 2.54 / 100;

	/**
	 * Method converts coordinates expressed in meters to coordinates expressed
	 * in inches
	 * 
	 * @param coordinate
	 *            Coordinate position in meters
	 * @return Coordinate position in inches
	 */
	public static Coordinate convertFromMetersToInches(Coordinate coordinate) {

		Coordinate coordinateInInches = new Coordinate(coordinate.getX() * MetersToInches,
				coordinate.getY() * MetersToInches, coordinate.getZ() * MetersToInches);

		return coordinateInInches;
	}

	/**
	 * Method converts coordinates expressed in inches to coordinates expressed
	 * in meters
	 * 
	 * @param coordinate
	 *            Coordinate position in inches
	 * @return Coordinate position in meters
	 */
	public static Coordinate convertFromInchesToMeters(Coordinate coordinate) {
		Coordinate coordinateInMeters = new Coordinate(coordinate.getX() * InchesToMeters,
				coordinate.getY() * InchesToMeters, coordinate.getZ() * InchesToMeters);

		return coordinateInMeters;
	}

	/**
	 * Method converts vector measurements expressed in meters to inches
	 * 
	 * @param vector Vector measurement in meters
	 * @return Vector measurement in inches
	 */
	public static ThreeDimensionalSpaceVector convertFromMetersToInches(ThreeDimensionalSpaceVector velocity) {
		ThreeDimensionalSpaceVector velocityInInches = new ThreeDimensionalSpaceVector(velocity.getX() * MetersToInches,
				velocity.getY() * MetersToInches,
				velocity.getZ() * MetersToInches);

		return velocityInInches;
	}
	
	/**
	 * Method converts vector measurements expressed in inches to measurements expressed
	 * in meters
	 * 
	 * @param vector Vector position in inches
	 * @return Vector measurement in meters
	 */
	public static ThreeDimensionalSpaceVector convertFromInchesToMeters(ThreeDimensionalSpaceVector coordinate) {
		ThreeDimensionalSpaceVector coordinateInMeters = new ThreeDimensionalSpaceVector(coordinate.getX() * InchesToMeters,
				coordinate.getY() * InchesToMeters, coordinate.getZ() * InchesToMeters);

		return coordinateInMeters;
	}

}
