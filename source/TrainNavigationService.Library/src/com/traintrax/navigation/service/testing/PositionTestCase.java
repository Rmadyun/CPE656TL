package com.traintrax.navigation.service.testing;

import java.util.LinkedList;
import java.util.List;

/**
 * Class represents a dataset that can used for 
 * testing a train's position in a given scenario.
 * @author Corey Sanders
 *
 */
public class PositionTestCase {
	
	private String description;
	private List<PositionTestSample> samples;
	
	/**
	 * Constructor
	 * @param description Human readable description of the test scenario where
	 * the samples were collected
	 */
	public PositionTestCase(String description) {
		super();
		this.description = description;
		this.samples = new LinkedList<PositionTestSample>();
	}
	
	
	/**
	 * Constructor
	 * @param description Human readable description of the test scenario where
	 * the samples were collected
	 * @param samples Collection of points sampled during the test case scenario
	 */
	public PositionTestCase(String description, List<PositionTestSample> samples) {
		super();
		this.description = description;
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
	 * Append another test case to this one.
	 * @param testCase Test Case to append
	 */
	public void appendTestCase(PositionTestCase testCase)
	{
		samples.addAll(samples.size(), testCase.samples);
	}
	
}
