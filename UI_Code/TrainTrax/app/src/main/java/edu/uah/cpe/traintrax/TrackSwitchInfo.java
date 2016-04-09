package edu.uah.cpe.traintrax;

import com.traintrax.navigation.database.library.RepositoryEntry;
import com.traintrax.navigation.database.library.TrackPoint;
import com.traintrax.navigation.database.library.TrackSwitch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Acer Valued Customer on 1/31/2016.
 */
public class TrackSwitchInfo {
    private int num_switches; // num of switches
    private List <Boolean> passState; // state of switch
    private List <Float> xposition; // x position of switch
    private List <Float> yposition; // y position of switch


    void CreateSwitchArray(int num)
    {
        passState = new ArrayList <Boolean>(Arrays.asList(new Boolean[num]));
        xposition = new ArrayList <Float>(Arrays.asList(new Float[num]));
        yposition = new ArrayList <Float>(Arrays.asList(new Float[num]));

        //initialize all states to false
        for (int i = 0; i < num; i++)
        {
            passState.set(i, false);
            xposition.set(i, 0.0f);
            yposition.set(i, 0.0f);
        }
    }

    //pulls switch data from database (or wherever and sets all values
    void SetAllSwitchData(TrackGeometry trackGeometry) {

        //get the track points really need to only do this once
        // (being done in track switch and track diagram class )
        List<RepositoryEntry<TrackPoint>> trackPoints = trackGeometry.getTrackPoints();
        List<RepositoryEntry<TrackSwitch>> trackSwitches = trackGeometry.getSwitches();

        int index = 0;

        num_switches = trackSwitches.size();
        CreateSwitchArray(num_switches);

        //Create track switches
        for(RepositoryEntry<TrackSwitch> entry : trackSwitches) {
            TrackSwitch trackSwitch = entry.getValue();
            String pointId = trackSwitch.getPointId();
            TrackPoint point = findPoint(trackPoints, pointId);

            double xcord = point.getX();
            double ycord = point.getY();

            trackSwitch.getBypassBlockId();
            trackSwitch.getPassBlockId();

            setXPosition(index, ((float) xcord));
            setYPosition(index, ((float) ycord));

            //TODO Need to set state info in here

            index++;
            }
        }

    int getNum_switches(){
        return num_switches;
    }

    Boolean getPassState(int index){
        return passState.get(index);
    }

    Float getXPosition(int index){
        return xposition.get(index);
    }

    Float getYPosition(int index){
        return yposition.get(index);
    }

    void setPassState(int index, Boolean state){
        passState.set(index, state);
    }

    void setXPosition(int index, Float pos){
        xposition.set(index, pos);
    }

    void setYPosition(int index, Float pos){
        yposition.set(index, pos);
    }

    void setNum_switches(int num){
        num_switches = num;
    }




    /**
     * Constructor
     * @param trackGeometry Collected information about the track
    /* Default Constructor */
     TrackSwitchInfo(TrackGeometry trackGeometry) {
        this.num_switches = 0;
         SetAllSwitchData(trackGeometry);

    }


    private static TrackPoint findPoint(List<RepositoryEntry<TrackPoint>> trackPoints, String id){
        TrackPoint trackPoint = null;

        for(RepositoryEntry<TrackPoint> entry : trackPoints){
            if(entry.getId().equals(id))
            {
                trackPoint = entry.getValue();
                break;
            }
        }

        return trackPoint;
    }
}
