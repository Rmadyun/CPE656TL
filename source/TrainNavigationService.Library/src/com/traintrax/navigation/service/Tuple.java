package com.traintrax.navigation.service;

/**
 * Class that encapsulates a generic association between two objects.
 * @author Corey Sanders
 *
 * @param <TItem1> The type of the first item the tuple
 * @param <TItem2> The type of the second item in the tuple
 */
public class Tuple<TItem1, TItem2> {
	
	private TItem1 item1;
	private TItem2 item2;
	
	
	/**
	 * Constructor
	 * @param item1 first item in the tuple
	 * @param item2 second item in the tuple
	 */
	public Tuple(TItem1 item1, TItem2 item2) {
		super();
		this.item1 = item1;
		this.item2 = item2;
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
	
}
