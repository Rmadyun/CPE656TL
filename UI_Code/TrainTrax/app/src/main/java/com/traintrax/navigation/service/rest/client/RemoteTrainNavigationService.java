package com.traintrax.navigation.service.rest.client;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.traintrax.navigation.service.*;
import com.traintrax.navigation.service.events.*;
import com.traintrax.navigation.service.position.*;
import com.traintrax.navigation.trackswitch.*;

/**
 * Restful Web client implementation of the Train Navigation Service
 *
 * @author Corey Sanders
 */
public class RemoteTrainNavigationService implements TrainNavigationServiceInterface {

    private final RemoteTrainPositionService trainPositionService;

    private final RemoteTrainIdentityService trainIdentityService;

    private final RemoteTrackSwitchService trackSwitchService;

    private final Timer timer;
    private final PublisherInterface<TrainNavigationServiceEventSubscriber, TrainNavigationServiceEvent> eventPublisher;
    private static final int POLL_RATE_IN_MS = 1000; //assuming a low latency network


    /**
     * Constructor
     *
     * @param hostName Network address of the remote service being contacted
     * @param port     Network port of the remote service being contacted
     */
    public RemoteTrainNavigationService(String hostName, Integer port) {
        this.trainPositionService = new RemoteTrainPositionService(hostName, port);
        this.trainIdentityService = new RemoteTrainIdentityService(hostName, port);
        this.trackSwitchService = new RemoteTrackSwitchService(hostName, port);

        NotifierInterface<TrainNavigationServiceEventSubscriber, TrainNavigationServiceEvent> eventNotifier = new TrainNavigationServiceEventNotifier();
        PublisherInterface<TrainNavigationServiceEventSubscriber, TrainNavigationServiceEvent> eventPublisher = new GenericPublisher<TrainNavigationServiceEventSubscriber, TrainNavigationServiceEvent>(
                eventNotifier);

        this.eventPublisher = eventPublisher;

        this.timer = new Timer();
        setupTimer();

    }

    /**
     * Configures the timer to run for listening to train events
     */
    private void setupTimer() {

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                ReadTrainPositions();
            }
        }, 0, POLL_RATE_IN_MS);

    }

    /**
     * Method is responsible for reading the latest train position information and firing events
     */
    private void ReadTrainPositions() {
        List<String> trainIds = new ArrayList<String>();

        try {
            trainIds = trainIdentityService.getTrainIdentifiers();
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        for (String trainId : trainIds) {

            try {
                TrainPositionEstimate positionUpdate = trainPositionService.getLastKnownTrainPosition(trainId);
                TrainPositionUpdatedEvent updatedEvent = new TrainPositionUpdatedEvent(trainId, positionUpdate);
                eventPublisher.PublishEvent(updatedEvent);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    @Override
    public TrainPositionEstimate GetLastKnownPosition(String trainIdentifier) {
        return trainPositionService.getLastKnownTrainPosition(trainIdentifier);
    }

    @Override
    public List<String> GetKnownTrainIdentifiers() {
        return trainIdentityService.getTrainIdentifiers();
    }

    @Override
    public SwitchState GetSwitchState(String switchIdentifier) {

        SwitchState switchState = trackSwitchService.getTrackSwitchState(switchIdentifier);

        return switchState;
    }

    @Override
    public void SetSwitchState(String switchIdentifier, SwitchState switchState) throws IOException {

        trackSwitchService.setSwitchState(switchIdentifier, switchState);
    }

    @Override
    public void Subscribe(TrainNavigationServiceEventSubscriber subscriber) {
        eventPublisher.Subscribe(subscriber);
    }

    @Override
    public void Unsubscribe(TrainNavigationServiceEventSubscriber subscriber) {
        eventPublisher.Unsubscribe(subscriber);
    }
}
