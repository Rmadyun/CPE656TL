package TrainTrax;


/**
 * @author Corey Sanders
 * TrainTrax interface for sending asynchronous notifications to
 * objects
 * @param TSubscriber type of subscriber to send notifications to
 * @param TEvent type of events to support sending notifications for
 */
public interface NotifierInterface<TSubscriber, TEvent> {
	
	/**
	 * Send a notification to a subscriber about an event.
	 * @param subscriber Subscriber to issue a notification to
	 * @param event Event of interest.
	 */
	void Notify(TSubscriber subscriber, TEvent event);
}
