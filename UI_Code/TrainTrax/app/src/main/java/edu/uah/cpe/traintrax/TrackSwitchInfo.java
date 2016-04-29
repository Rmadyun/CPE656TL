package edu.uah.cpe.traintrax;

import com.traintrax.navigation.database.library.RepositoryEntry;
import com.traintrax.navigation.database.library.TrackBlock;
import com.traintrax.navigation.database.library.TrackPoint;
import com.traintrax.navigation.database.library.TrackSwitch;
import com.traintrax.navigation.trackswitch.SwitchState;

import android.util.DisplayMetrics;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Acer Valued Customer on 1/31/2016.
 */
public class TrackSwitchInfo {
    private int num_switches; // num of switches
    private List <String> switchName; // name of switch
    private List <Boolean> passState; // state of switch
    private List <String> switchBlockName; //name of track block of switch
    private List <String> passBlockName; // name of pass track block
    private List <String > bypassBlockName; // name of bypass track block
    private List <Float> xposition; // x position of switch
    private List <Float> yposition; // y position of switch


    void CreateSwitchArray(int num)
    {
        passState = new ArrayList <Boolean>(Arrays.asList(new Boolean[num]));
        xposition = new ArrayList <Float>(Arrays.asList(new Float[num]));
        yposition = new ArrayList <Float>(Arrays.asList(new Float[num]));


        switchBlockName = new ArrayList <String>(Arrays.asList(new String[num]));
        passBlockName = new ArrayList <String>(Arrays.asList(new String[num]));
        bypassBlockName = new ArrayList <String>(Arrays.asList(new String[num]));
        switchName = new ArrayList <String>(Arrays.asList(new String[num]));

        //initialize all states to false
        for (int i = 0; i < num; i++)
        {
            passState.set(i, true);
            xposition.set(i, 0.0f);
            yposition.set(i, 0.0f);
        }
    }

    //pulls switch data from database (or wherever and sets all values
    void SetAllSwitchData(TrackGeometry trackGeometry) {

        //get the track points really need to only do this once
        // (being done in track switch and track diagram class )
        TestTrackPointRepository trackPointRepository = new TestTrackPointRepository();
        List<RepositoryEntry<TrackPoint>> trackPoints = trackPointRepository.findAll();

        TestTrackSwitchRepository switchRepository = new TestTrackSwitchRepository();
        List<RepositoryEntry<TrackSwitch>> trackSwitches = switchRepository.findAll();

        TestTrackBlockRepository trackBlockRepository = new TestTrackBlockRepository();
        List<RepositoryEntry<TrackBlock>> trackBlocks = trackBlockRepository.findAll();

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

            //get the ID for the pass switch track block, track block and bypass block for this switch
            String passId = trackSwitch.getPassBlockId();
            String bypassId = trackSwitch.getBypassBlockId();
            String tmpName = trackSwitch.getSwitchName();

            //set name of Switch
            switchName.set(index, tmpName);

            //set x and y position of switch
            setXPosition(index, ((float) xcord));
            setYPosition(index, ((float) ycord));

            //get switch block

         //   HashMap<String, TrackBlockModel> blockLut = new HashMap<String, TrackBlockModel>();

            //Create track blocks
            for(RepositoryEntry<TrackBlock> entry2 : trackBlocks){
                TrackBlock trackBlock = entry2.getValue();

                    if (entry2.getId().equals(point.getBlockId())  )

                        switchBlockName.set(index, trackBlock.getBlockName());

                if (entry2.getId().equals(passId)  )

                    passBlockName.set(index, trackBlock.getBlockName());

                if (entry2.getId().equals(bypassId)  )

                    bypassBlockName.set(index, trackBlock.getBlockName());


                //Save block in LUT for quick lookup
                    //blockLut.put(entry.getId(), new TrackBlockModel(trackBlock.getBlockName()));
            }


            index++;
        }



    }

    int getNum_switches(){
        return num_switches;
    }

    String getswitchName(int index){return switchName.get(index);}

    String getswitchBlockName(int index){return switchBlockName.get(index);}

    String getPassBlockName(int index){return passBlockName.get(index);}

    String getBypassBlockName(int index){return bypassBlockName.get(index);}

    Boolean getPassState(int index){return passState.get(index);}

    Float getXPosition(int index){return xposition.get(index);}

    Float getYPosition(int index){
        return yposition.get(index);
    }

    void setPassState(int index, Boolean state){
        passState.set(index, state);
    }

    void setXPosition(int index, Float pos){
        xposition.set(index, pos);
    }

    void setYPosition(int index, Float pos){yposition.set(index, pos);
    }

    void setPassBlockName(int index, String name){passBlockName.set(index, name);
    }

    void setBypassBlockName(int index, String name){bypassBlockName.set(index, name);
    }

    void setSwitchBlockName(int index, String name){switchBlockName.set(index, name);
    }

    void setSwitchName(int index, String name){switchName.set(index, name);
    }
    void setNum_switches(int num){
        num_switches = num;
    }

    /**
     * Change the value of a particular switch based on the switch value assigned to it.
     * @param switchName Unique identifier for the switch
     * @param switchState State to change the switch
     */
    void setSwitchState(String switchName, SwitchState switchState){

        //Find the switch and update its state

        for(int i = 0; i < this.switchName.size(); i++){
            String selectedSwitchName = this.switchName.get(i);
            if(selectedSwitchName.equalsIgnoreCase(switchName)){
                passState.set(i, (switchState == SwitchState.Pass));
                break;
            }
        }
    }

    /**
     * Constructor
     * @param
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
