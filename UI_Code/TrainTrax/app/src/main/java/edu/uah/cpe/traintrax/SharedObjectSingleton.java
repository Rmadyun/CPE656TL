package edu.uah.cpe.traintrax;

import com.traintrax.navigation.service.TrainNavigationServiceInterface;

/**
 * Class responsible for providing access to models that are shared between multiple
 * views of an application.
 * @author Corey Sanders
 */
public class SharedObjectSingleton {

    private static SharedObjectSingleton instance = new SharedObjectSingleton();

    private TrackDiagram trackDiagram;
    private TrackSwitchInfo trackSwitchInfo;
    private TrainPosInfo trainPosInfo;
    private TrainNavigationServiceInterface trainNavigationServiceInterface;

    /**
     * Default Constructor
     * prevents other instances
     */
    protected SharedObjectSingleton(){
        trackDiagram = null;
    }

    /**
     * Retrieves the single instance of this class
     * @return Single instance of this class.
     */
    public static SharedObjectSingleton getInstance(){
        return instance;
    }

    /**
     * Retrieves the information needed to draw the track diagram
     * @return The information needed to draw the track diagram
     */
    public TrackDiagram getTrackDiagram() {
        return trackDiagram;
    }

    /**
     * Assigns the information needed to draw the track diagram
     * @param trackDiagram The information needed to draw the track diagram
     */
    public void setTrackDiagram(TrackDiagram trackDiagram) {
        this.trackDiagram = trackDiagram;
    }

    /**
     * Retrieves information about all of the switches on the track
     * @return Information about all of the switches on the track
     */
    public TrackSwitchInfo getTrackSwitchInfo() {
        return trackSwitchInfo;
    }

    /**
     * Assigns a collection of all of the information about switches on the track
     * @param trackSwitchInfo Information about all of the switches on the track
     */
    public void setTrackSwitchInfo(TrackSwitchInfo trackSwitchInfo) {
        this.trackSwitchInfo = trackSwitchInfo;
    }

    public TrainPosInfo getTrainPosInfo() {
        return trainPosInfo;
    }

    /**
     * Assigns a collection of all of the information about train positions on the track
     * @param trainPosInfo Information about all of the trains on the track
     */
    public void setTrainPosInfo(TrainPosInfo trainPosInfo) {
        this.trainPosInfo = trainPosInfo;
    }

    /**
     * Retrieves the interface for communicating with the train navigation service
     * @return interface for contacting the train navigation service
     */
    public TrainNavigationServiceInterface getTrainNavigationServiceInterface() {
        return trainNavigationServiceInterface;
    }

    /**
     * Assigns the interface for contacting the train navigation service
     * @param trainNavigationServiceInterface interface to use for contacting the train navigation service
     */
    public void setTrainNavigationServiceInterface(TrainNavigationServiceInterface trainNavigationServiceInterface) {
        this.trainNavigationServiceInterface = trainNavigationServiceInterface;
    }
}
