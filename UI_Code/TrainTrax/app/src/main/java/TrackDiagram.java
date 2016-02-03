package edu.uah.cpe.traintrax;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TrackDiagram {
    Boolean NavData;                   // used to determine if position data exists
    private int numberOfCoordinates;  // num of coordinates for track diagram
    private List <Double> xposition; // x position of coordinate
    private List <Double> yposition; // y position of coordinate

/*****************************TBD********************************************************/
    /* still need to determine if we are going to use the rectangles or lines to draw diagram */
    //drawRect(float left, float top, float right, float bottom, Paint paint)
    private ArrayList recLeftCoordinate = new ArrayList();
    private ArrayList recTopCoordinate = new ArrayList ();
    private ArrayList rectRightCoordinate = new ArrayList ();
    private ArrayList rectBottomCoordinate = new ArrayList();

    void CreateDiagramArray(int num)
    {
        xposition = new ArrayList <Double>(Arrays.asList(new Double[num]));
        yposition = new ArrayList <Double>(Arrays.asList(new Double[num]));

        //initialize all positions to zero
        for (int i = 0; i < num; i++)
        {
            xposition.set(i, 0.0);
            yposition.set(i, 0.0);
        }
    }

    //pulls switch data from database (or wherever and sets all values
    void SetAllDiagramData() {

        //GetCoordinate data from somewhere
        int num_cords = 0;
        Boolean state = false;
        Double xPos = 0.0;
        Double yPos = 0.0;

        for (int i = 0; i < num_cords; i++) {
            xposition.set(i, xPos);
            yposition.set(i, yPos);
            return;
        }

        //set num of coordinates and flag for NavData existing
        NavData = true;
        numberOfCoordinates = 0;
    }

    int getnumCoords(){
        return numberOfCoordinates;
    }

    double getXPosition(int index){
        return xposition.get(index);
    }

    double getYPosition(int index){
        return yposition.get(index);
    }

    void setXPosition(int index, double pos){
        xposition.set(index, pos);
        return;
    }

    void setYPosition(int index, double pos){
        yposition.set(index, pos);
        return;
    }

    void setnumCoords(int num){
        numberOfCoordinates = num;
    }

    /* function gets all the value of the coordinates for the track diagram
       and stores them in the ArrayLists.  The view code will use the array
        lists to draw rectangles on the view **** TBD */
    public void getCoordinates(){
        return;
    }

    Boolean GetNavData (){
       return NavData;
    }

    void SetNavData (Boolean flag){
        NavData = flag;
    }


    /**
     * Constructor
     * @param


    /* Default Constructor */
    public TrackDiagram() {
        this.numberOfCoordinates = 0;
        NavData = false;
    }

    public TrackDiagram(int num) {
        this.numberOfCoordinates = num;
        CreateDiagramArray(num);
        NavData = true;
    }


}
