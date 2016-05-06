package edu.uah.cpe.traintrax;

/**
 * Class represents the result of executing a RetrieveTrackGeometry task.
 */
public class RetrieveTrackGeometryTaskResult {
    private TrackGeometry trackGeometry;
    private String failureMessage;

    /**
     * Constructor
     * @param trackGeometry the track geometry data obtained. Returns null if retrieving data failed.
     * @param failureMessage detailed information about why the switch request failed. Assign to an empty string if successful.
     */
    public RetrieveTrackGeometryTaskResult(TrackGeometry trackGeometry, String failureMessage) {
        this.trackGeometry = trackGeometry;
        this.failureMessage = failureMessage;
    }

    /**
     * Retrieves the track geometry data obtained
     * @return the track geometry data obtained. Returns null if retrieving data failed.
     */
    public TrackGeometry getTrackGeometry() {
        return trackGeometry;
    }

    /**
     * Retrieves detailed information about why the switch request failed. Assign to an empty string if successful.
     * @return detailed information about why the switch request failed. Assign to an empty string if successful.
     */
    public String getFailureMessage() {
        return failureMessage;
    }
}
