package edu.uah.cpe.traintrax;
import java.util.ArrayList;

public class TrackDiagram {

    /* private variable for number of people to split the bill with */
    private int numberOfCoordinates;
    //drawRect(float left, float top, float right, float bottom, Paint paint)
    private ArrayList recLeftCoordinate = new ArrayList();
    private ArrayList recTopCoordinate = new ArrayList ();
    private ArrayList rectRightCoordinate = new ArrayList ();
    private ArrayList rectBottomCoordinate = new ArrayList();


    /* function gets all the value of the coordinates for the track diagram
       and stores them in the ArrayLists.  The view code will use the array
        lists to draw rectangles on the view */
    public void getCoordinates(){
        return;
    }



    /**
     * Constructor
     * @param


    /* Default Constructor */
    public TrackDiagram() {
        this.numberOfCoordinates = 0;
    }

}
