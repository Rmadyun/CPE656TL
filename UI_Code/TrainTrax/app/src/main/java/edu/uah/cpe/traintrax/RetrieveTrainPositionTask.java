package edu.uah.cpe.traintrax;

import android.os.AsyncTask;

import com.traintrax.navigation.database.library.AdjacentPoint;
import com.traintrax.navigation.database.library.RepositoryEntry;
import com.traintrax.navigation.database.library.TrackBlock;
import com.traintrax.navigation.database.library.TrackPoint;
import com.traintrax.navigation.database.library.TrackSwitch;
import com.traintrax.navigation.database.rest.client.RemoteAdjacentPointRepository;
import com.traintrax.navigation.database.rest.client.RemoteTrackBlockRepository;
import com.traintrax.navigation.database.rest.client.RemoteTrackPointRepository;
import com.traintrax.navigation.database.rest.client.RemoteTrackSwitchRepository;

import java.util.List;

/**
 * Created by death on 4/8/16.
 */
public class RetrieveTrainPositionTask extends AsyncTask<String,Void,TrackGeometry> {
    @Override
    protected TrackGeometry doInBackground(String... params) {

        String hostName = params[0];
        int port = Integer.parseInt(params[1]);
        TrackGeometry trackGeometry = null;


        try {
            RemoteTrackBlockRepository trackBlockRepository = new RemoteTrackBlockRepository(hostName, port);
            RemoteTrackPointRepository trackPointRepository = new RemoteTrackPointRepository(hostName, port);
            RemoteAdjacentPointRepository adjacentPointRepository = new RemoteAdjacentPointRepository(hostName, port);
            RemoteTrackSwitchRepository trackSwitchRepository = new RemoteTrackSwitchRepository(hostName, port);


            List<RepositoryEntry<TrackBlock>> trackBlocks = trackBlockRepository.findAll();
            List<RepositoryEntry<TrackPoint>> trackPoints = trackPointRepository.findAll();
            List<RepositoryEntry<AdjacentPoint>> adjacentPoints = adjacentPointRepository.findAll();
            List<RepositoryEntry<TrackSwitch>> switches = trackSwitchRepository.findAll();


            trackGeometry = new TrackGeometry(trackBlocks, trackPoints, adjacentPoints, switches);
        }
        catch(Exception exception){

            exception.printStackTrace();
        }

        return trackGeometry;
    }

}
