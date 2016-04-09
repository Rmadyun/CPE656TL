package edu.uah.cpe.traintrax;

import com.traintrax.navigation.database.library.AdjacentPoint;
import com.traintrax.navigation.database.library.RepositoryEntry;
import com.traintrax.navigation.database.library.TrackBlock;
import com.traintrax.navigation.database.library.TrackPoint;
import com.traintrax.navigation.database.library.TrackSwitch;

import java.util.List;

/**
 * Class that defines all of the information known about the
 * shape of a given Positive Train Control Test Bed
 * @author Corey Sanders
 */
public class TrackGeometry {

    private final List<RepositoryEntry<TrackSwitch>> switches;
    private List<RepositoryEntry<TrackBlock>> trackBlocks;
    private List<RepositoryEntry<TrackPoint>> trackPoints;
    private List<RepositoryEntry<AdjacentPoint>> adjacentPoints;

    /**
     * Constructor
     * @param trackBlocks A list of all of the track blocks (monitorable partitions of the track) of the track.
     * @param trackPoints A list of specific positions on the train track
     * @param adjacentPoints A list of objects describing which points are adjacent
     * to other points on the track
     */
    public TrackGeometry(List<RepositoryEntry<TrackBlock>> trackBlocks, List<RepositoryEntry<TrackPoint>> trackPoints, List<RepositoryEntry<AdjacentPoint>> adjacentPoints, List<RepositoryEntry<TrackSwitch>> switches) {
        this.trackBlocks = trackBlocks;
        this.trackPoints = trackPoints;
        this.adjacentPoints = adjacentPoints;
        this.switches = switches;
    }

    /**
     * Retrieves the list of track blocks that compose the track.
     * Track Blocks are monitorable partitions of the track.
     * @return A list of all of the track blocks of the track.
     */
    public List<RepositoryEntry<TrackBlock>> getTrackBlocks() {
        return trackBlocks;
    }

    /**
     * Retrieves a list of specific positions on the train track
     * @return a list of specific positions on the train track
     */
    public List<RepositoryEntry<TrackPoint>> getTrackPoints() {
        return trackPoints;
    }

    /**
     * Retrieves a list of objects describing which points are in close
     * proximity to other points
     * @return A list of objects describing which points are adjacent
     * to other points on the track.
     */
    public List<RepositoryEntry<AdjacentPoint>> getAdjacentPoints() {
        return adjacentPoints;
    }


    /**
     * Retrieves a list of all switches on the test bed
     * @return A list of all switches on the test bed
     */
    public List<RepositoryEntry<TrackSwitch>> getSwitches() {
        return switches;
    }
}
