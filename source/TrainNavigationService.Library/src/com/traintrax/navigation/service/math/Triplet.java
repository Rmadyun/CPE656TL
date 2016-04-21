package com.traintrax.navigation.service.math;

/**
 * Class that encapsulates a generic association between three objects.
 * @author Corey Sanders
 *
 * @param <TItem1> The type of the first item the tuple
 * @param <TItem2> The type of the second item in the tuple
 * @param <TItem3> The type of the third item in the tuple 
 */
public class Triplet<TItem1, TItem2, TItem3> {
	
	private TItem1 item1;
	private TItem2 item2;
	private TItem3 item3;
	
	
	/**
	 * Constructor
	 * @param item1 first item in the tuple
	 * @param item2 second item in the tuple
	 * @param item2 second item in the tuple 
	 */
	public Triplet(TItem1 item1, TItem2 item2, TItem3 item3) {
		super();
		this.item1 = item1;
		this.item2 = item2;
		this.item3 = item3;
	}
	
	/**
	 * Retrieves the first item in the tuple
	 * @return First item in the tuple
	 */
	public TItem1 getItem1() {
		return item1;
	}

	/**
	 * Retrieves the second item in the tuple
	 * @return Second item in the tuple
	 */
	public TItem2 getItem2() {
		return item2;
	}
	
	/**
	 * Retrieves the third item in the tuple
	 * @return Third item in the tuple
	 */
	public TItem3 getItem3() {
		return item3;
	}
	
}
