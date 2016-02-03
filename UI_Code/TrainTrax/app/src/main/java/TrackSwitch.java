package edu.uah.cpe.traintrax;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Acer Valued Customer on 1/31/2016.
 */
public class TrackSwitch {
    private int num_switches; // num of switches
    private List <Boolean> passState; // state of switch
    private List <Double> xposition; // x position of switch
    private List <Double> yposition; // y position of switch


    void CreateSwitchArray(int num)
    {
        passState = new ArrayList <Boolean>(Arrays.asList(new Boolean[num]));

        //initalize all states to false
        for (int i = 0; i < passState.size(); i++)
        {
            passState.set(i, false);
        }
    }
    int getNum_switches(){
        return num_switches;
    }

    //pulls switch data from database (or wherever and sets all values
    void SetAllSwitchData() {
        int num_switch = 0;
        Boolean state = false;
        Double xPos = 0.0;
        Double yPos = 0.0;

        //GetCoordinate data from somewhere

        for (int i = 0; i < num_switches; i++) {
            passState.set(i, state);
            xposition.set(i, xPos);
            yposition.set(i, yPos);
            return;
        }
    }

   Boolean getPassState(int index){
        return passState.get(index);
    }

    double getXPosition(int index){
        return xposition.get(index);
    }

    double getYPosition(int index){
        return yposition.get(index);
    }

    void setPassState(int index, Boolean state){
        passState.set(index, state);
        return;
    }

    void setXPosition(int index, double pos){
        xposition.set(index, pos);
        return;
    }

    void setYPosition(int index, double pos){
        yposition.set(index, pos);
        return;
    }




    /**
     * Constructor
     * @param
    /* Default Constructor */
     TrackSwitch() {
        this.num_switches = 0;

    }

    TrackSwitch(int num) {
        this.num_switches = num;
        CreateSwitchArray(num);

    }
}
