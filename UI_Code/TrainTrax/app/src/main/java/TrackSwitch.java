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
    private List <Float> xposition; // x position of switch
    private List <Float> yposition; // y position of switch


    void CreateSwitchArray(int num)
    {
        passState = new ArrayList <Boolean>(Arrays.asList(new Boolean[num]));
        xposition = new ArrayList <Float>(Arrays.asList(new Float[num]));
        yposition = new ArrayList <Float>(Arrays.asList(new Float[num]));

        //initialize all states to false
        for (int i = 0; i < num_switches; i++)
        {
            passState.set(i, false);
            xposition.set(i, 0.0f);
            yposition.set(i, 0.0f);
        }
    }

    //pulls switch data from database (or wherever and sets all values
    void SetAllSwitchData() {
        Boolean state = false;
        Double xPos = 0.0;
        Double yPos = 0.0;

        //GetCoordinate data from somewhere

       /* for (int i = 0; i < num_switches; i++) {
            passState.set(i, state);
            xposition.set(i, xPos);
            yposition.set(i, yPos);
            return; */

            //test data for switches
            xposition.set(0, 28.0f); // 28, 50
            yposition.set(0, 50.0f); // 28, 50
            xposition.set(1, 31.0f);// 31, 16
            yposition.set(1, 16.0f);// 31, 16
            passState.set(1, true);
            xposition.set(2, 17.0f); // 17, 40
            yposition.set(2, 40.0f); // 17, 40
            xposition.set(3, 23.0f); // 23, 49
            yposition.set(3, 49.0f); // 23, 49
            passState.set(3, true);
            xposition.set(4, 23.0f);// 23, 53
            yposition.set(4, 53.0f);// 23, 53
            xposition.set(5, 39.0f); // 39, 73
            yposition.set(5, 73.0f); // 39, 73
            xposition.set(6, 65.0f); // 71, 54
            yposition.set(6, 54.0f); // 71, 54
            passState.set(6, true);

        //}
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
     * @param
    /* Default Constructor */
     TrackSwitch() {
        this.num_switches = 0;

    }

    TrackSwitch(int num) {
        this.num_switches = num;
        CreateSwitchArray(num);
        SetAllSwitchData();

    }
}
