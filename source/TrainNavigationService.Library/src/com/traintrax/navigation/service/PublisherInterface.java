package com.traintrax.navigation.service;

/**
 * @author death
 * Common interface to use for publishing events.
 * @param <TSubscriber> type of subscriber
 * @param <TEvent> type of events to communicate with a subscriber 
 */
public interface PublisherInterface<TSubscriber, TEvent> {

    /**
     * Adds a subscriber to receive events
     * @param subscriber New subscriber for events from this publisher
     */
	public void Subscribe(TSubscriber subscriber);
	
	/**
	 * Removes a subscriber from receiving events.
	 * @param subscriber Existing subscriber to remove from receiving
	 * this publisher's events.
	 */
	public void Unsubscribe(TSubscriber subscriber);
	
	/**
	 * Sends an event to all subscribers
	 * @param event Event to publish
	 */
	public void PublishEvent(TEvent event);
	
}
