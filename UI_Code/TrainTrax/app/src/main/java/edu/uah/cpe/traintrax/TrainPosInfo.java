package edu.uah.cpe.traintrax;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Class stores all of the latest information about the
 * position of trains on the track.
 * This class exists so that drawing the trains on the view can be decoupled from
 * the models used to report the train position.
 * It also allows the work to retrieve the train position updates to be in a separate
 * thread.
 */
public class TrainPosInfo {


    //TODO: Make this thread-safe
    private List<TrainInfo> trainList; // Collection of information about the known trains

    /**
     * Default Constructor
     */
    public TrainPosInfo(){
        trainList = new LinkedList<TrainInfo>();

        //CreateTestData();
    }

    /**
     * Create test trains to use for simulating train movement without
     * a train navigation service
     */
    private void CreateTestData() {

        //set temp positions of train 1
        trainList.add(new TrainInfo("Train1", 0.0f, 10.0f));

        //set temp positions of train 2
        trainList.add(new TrainInfo("Train2", 0.0f, 30.0f));

        //set temp positions of train 3
        trainList.add(new TrainInfo("Train3", 30.0f, 50.0f));
    }

    /**
     * Updates a train to be tracked. If it does not exist, then it adds it.
     * @param trainID ID mapped to the train
     * @param xposition X position of the train
     * @param yposition Y position of the train
     */
    public void addOrUpdateTrain(String trainID, Float xposition, Float yposition){

        TrainInfo trainInfo = find(trainID);

        if(trainInfo == null){
            //Create a new entry
            trainInfo = new TrainInfo(trainID, xposition, yposition);

            trainList.add(trainInfo);
        }
        else{

            // take the difference of the last position and current position
            Float Xvelocity =  xposition - trainInfo.getXposition();
            Float Yvelocity =  yposition - trainInfo.getYposition();

            //Update the existing velocity entry
            trainInfo.setXvelocity(Xvelocity);
            trainInfo.setYvelocity(Yvelocity);

            //Update the existing entry
            trainInfo.setXposition(xposition);
            trainInfo.setYposition(yposition);
        }
    }

    /**
     * Searches for an existing train being monitored
     * @param trainID ID mapped to the train
     * @return Information about the train being tracked. Returns null if no matches are found.
     */
    public TrainInfo find(String trainID){

        TrainInfo firstMatch = null;

        for(TrainInfo trainInfo : trainList){

            if(trainInfo.getTrainID() == trainID){
                firstMatch = trainInfo;
                break;
            }
        }

        return firstMatch;
    }

    /**
     * Retrieves the current list of train known and their positions
     * @return the current list of known trains and their positions
     */
    public List<TrainInfo> getTrains() {
        return trainList;
    }
}
