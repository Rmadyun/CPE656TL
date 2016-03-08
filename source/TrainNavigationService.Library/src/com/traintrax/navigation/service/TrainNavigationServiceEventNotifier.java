package com.traintrax.navigation.service;

import com.traintrax.navigation.service.events.NotifierInterface;

/**
 * Class responsible for notifying Train Navigation Service subscribers of
 * Train Navigation Service events.
 * @author Corey Sanders
 */
public class TrainNavigationServiceEventNotifier implements NotifierInterface<TrainNavigationServiceEventSubscriber, TrainNavigationServiceEvent> {


	/**
	 * Notifies a subscriber about a Train Navigation Service event.
	 * @param subscriber Subscriber to send events to
	 * @param event Event to deliver to the subscriber.
	 */
	public void Notify(TrainNavigationServiceEventSubscriber subscriber, TrainNavigationServiceEvent event) {
		
		
		if(TrainPositionUpdatedEvent.class.isInstance(event))
		{
			//Train Position Updated events
		    subscriber.TrainPositionUpdated((TrainPositionUpdatedEvent) event);
		}
		
	}

}
