package com.traintrax.navigation.service.testing;

import java.util.LinkedList;
import java.util.List;

import com.traintrax.navigation.service.position.Coordinate;
import com.traintrax.navigation.service.position.Velocity;
import com.traintrax.navigation.service.rotation.EulerAngleRotation;

/**
 * Class represents a dataset that can used for 
 * testing a train's position in a given scenario.
 * @author Corey Sanders
 *
 */
public class PositionTestCase {
	
	private String description;
	private List<PositionTestSample> samples;
	private Coordinate initialPosition;
	private EulerAngleRotation initialOrientation;
	private Velocity initialVelocity;
	
	
	/**
	 * Constructor
	 * @param description Human readable description of the test scenario where
	 * the samples were collected
	 * @param initialPosition initial position of the object of interest
	 * @param initialOrientation initial orientation of the object of interest
	 * @param initialVelocity the initial rate of movement of the object of interest
	 */
	public PositionTestCase(String description, Coordinate initialPosition, EulerAngleRotation initialOrientation, Velocity initialVelocity) {
		this(description, initialPosition, initialOrientation, initialVelocity, new LinkedList<PositionTestSample>());
	}
	
	
	/**
	 * Constructor
	 * @param description Human readable description of the test scenario where
	 * the samples were collected
	 * @param initialPosition initial position of the object of interest
	 * @param initialOrientation initial orientation of the object of interest
	 * @param initialVelocity the initial rate of movement of the object of interest
	 * @param samples Collection of points sampled during the test case scenario 
	 */
	public PositionTestCase(String description, Coordinate initialPosition, EulerAngleRotation initialOrientation, Velocity initialVelocity, List<PositionTestSample> samples) {
		super();
		this.description = description;
		this.initialPosition = initialPosition;
		this.initialOrientation = initialOrientation;
		this.initialVelocity = initialVelocity;
		this.samples = samples;
	}

	/**
	 * Retrieves description of the test scenario where
	 * the samples were collected.
	 * @return Human readable description of the test scenario where
	 * the samples were collected
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Retrieves the collection of points sampled during the test case scenario
	 * @return the collection of points sampled during the test case scenario
	 */
	public List<PositionTestSample> getSamples() {
		return samples;
	}
	
	
	/**
	 * Retrieves the initial position of the object of interest
	 * @return the initial position of the object of interest
	 */
	public Coordinate getInitialPosition() {
		return initialPosition;
	}


	/**
	 * Assigns the initial position of the object of interest
	 * @param initialPosition the initial position of the object of interest
	 */
	public void setInitialPosition(Coordinate initialPosition) {
		this.initialPosition = initialPosition;
	}


	/**
	 * Retrieves the initial orientation of the object of interest
	 * @return the initial orientation of the object of interest
	 */
	public EulerAngleRotation getInitialOrientation() {
		return initialOrientation;
	}


	/**
	 * Assigns initial orientation of the object of interest
	 * @param initialOrientation the initial orientation of the object of interest
	 */
	public void setInitialOrientation(EulerAngleRotation initialOrientation) {
		this.initialOrientation = initialOrientation;
	}


	/**
	 * Append another test case to this one.
	 * @param testCase Test Case to append
	 */
	public void appendTestCase(PositionTestCase testCase)
	{
		samples.addAll(samples.size(), testCase.samples);
	}


	/**
	 * Retrieves the initial rate of movement of the object of interest
	 * @return the initial rate of movement of the object of interest
	 */
	public Velocity getInitialVelocity() {

		return initialVelocity;
	}


	/**
	 * Assigns the initial rate of movement of the object of interest
	 * @param initialVelocity the initial rate of movement of the object of interest
	 */
	public void setInitialVelocity(Velocity initialVelocity) {
		this.initialVelocity = initialVelocity;
	}
	
	
}
