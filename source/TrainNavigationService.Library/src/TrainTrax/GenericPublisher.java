package TrainTrax;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Corey Sanders
 *  Generic class for publishing events
 * @param <TSubscriber> type of subscriber
 * @param <TEvent> type of events to communicate with a subscriber
 */
public class GenericPublisher<TSubscriber, TEvent> implements PublisherInterface<TSubscriber, TEvent> {

	private final List<TSubscriber> subscribers;
	private final NotifierInterface<TSubscriber, TEvent> notifier;
	
	/**
	 * Constructor
	 * @param notifier Send notifications about events to given subscriber
	 */
	public GenericPublisher(NotifierInterface<TSubscriber, TEvent> notifier){
		
		this.notifier = notifier;
		subscribers = new ArrayList<TSubscriber>();
	}
	
    /**
     * Adds a subscriber to receive events
     * @param subscriber New subscriber for events from this publisher
     */
	public void Subscribe(TSubscriber subscriber){
		subscribers.add(subscriber);
	}
	
	/**
	 * Removes a subscriber from receiving events.
	 * @param subscriber Existing subscriber to remove from receiving
	 * this publisher's events.
	 */
	public void Unsubscribe(TSubscriber subscriber){
		subscribers.remove(subscriber);
	}
	
	/**
	 * Sends an event to all subscribers
	 * @param event Event to publish
	 */
	public void PublishEvent(TEvent event){
		
		for(TSubscriber subscriber : subscribers){
			notifier.Notify(subscriber, event);
		}
		
	}
	
}
