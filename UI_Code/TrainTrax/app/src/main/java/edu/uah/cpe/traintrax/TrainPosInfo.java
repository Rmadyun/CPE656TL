package edu.uah.cpe.traintrax;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Acer Valued Customer on 3/20/2016.
 */
public class TrainPosInfo {

    int num_trains;
    private List<String> trainID; // name of Train
    private List<Float> xposition; // x position of train
    private List<Float> yposition; // y position of train

    void CreateTrainArray(int num)
    {
        xposition = new ArrayList <Float>(Arrays.asList(new Float[num]));
        yposition = new ArrayList <Float>(Arrays.asList(new Float[num]));
        trainID = new ArrayList <String>(Arrays.asList(new String[num]));

        num_trains = num;

    }

    void CreateTestData()
    {
    CreateTrainArray(3);
        num_trains = 3;

        setTrainID(0, "Train1");
        setTrainID(1, "Train2");
        setTrainID(2, "Train3");

        //set temp positions of train 1
        setXPosition(0, 0.0f);
        setYPosition(0, 10.0f);

        //set temp positions of train 2
        setXPosition(1, 0.0f);
        setYPosition(1, 30.0f);

        //set temp positions of train 3
        setXPosition(2, 30.0f);
        setYPosition(2, 50.0f);

    }


    Float getXPosition(int index){return xposition.get(index);}

    Float getYPosition(int index){return yposition.get(index);}

    String getTrainID(int index){return trainID.get(index);}

    int getNumTrains(){return num_trains;}

    void setXPosition(int index, Float pos){xposition.set(index, pos);
    }

    void setYPosition(int index, Float pos){yposition.set(index, pos);}

    void setTrainID(int index, String Name) {trainID.set(index, Name);}

    void addXPosition(int index, Float pos){
        xposition.add(pos);
    }

    void addYPosition(int index, Float pos){yposition.add(pos);}

    void addTrainID(int index, String Name) {trainID.add(Name);}

    void setNumTrains(int num) {num_trains = num;}

    /**
     * Constructor
     * @param
    /* Default Constructor */
    TrainPosInfo(TrackGeometry geometry) {
        this.num_trains = 0;
        CreateTestData();

    }



}
