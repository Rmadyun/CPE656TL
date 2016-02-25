package edu.uah.cpe.traintrax;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
//import TrainNavigationDatabase.*;

public class TrackDiagram {
    Boolean NavData;                   // used to determine if position data exists
    private int numberOfCoordinates;  // num of coordinates for track diagram
    private List <Float> xposition; // x position of coordinate
    private List <Float> yposition; // y position of coordinate
    private int xmax;  // maximum x coordinate of track
    private int ymax;  // maximum y coordinate of track
    private int xpixel;// width in pixels of device screen
    private int ypixel; // height in pixels of device screen



    void CreateDiagramArray(int num)
    {
        xposition = new ArrayList <Float>(Arrays.asList(new Float[num]));
        yposition = new ArrayList <Float>(Arrays.asList(new Float[num]));

        //initialize all positions to zero
        for (int i = 0; i < num; i++)
        {
            xposition.set(i, 0.0f);
            yposition.set(i, 0.0f);
        }
    }

    //pulls switch data from database (or wherever and sets all values
    void SetAllDiagramData() {

        //RemoteTrackPointRepository trackPointRepository = new RemoteTrackPointRepository();

        //GetCoordinate data from somewhere
        int num_cords = 0;
        Boolean state = false;
        Float xPos = 0.0f;
        Float yPos = 0.0f;

         //for (int i = 0; i < num_cords; i++) {
            //xposition.set(i, xPos);
            //yposition.set(i, yPos);

            /* Temporary test data for track coordinates */
            //x max = 80, y max = 90
            //nexus 10 = 2500 X 1600


            xposition.set(0, 28.0f); // 28, 50
            yposition.set(0, 50.0f); // 28, 50
            xposition.set(1, 33.0f); // 33, 46
            yposition.set(1, 46.0f); // 33, 46
            xposition.set(2, 38.0f); // 38, 43
            yposition.set(2, 43.0f);; // 38, 43
        //first loop
        xposition.set(3, 31.0f);// 31, 16
        yposition.set(3, 16.0f);// 31, 16
        xposition.set(4, 25.0f); // 25, 16
        yposition.set(4, 16.0f); // 25, 16
        xposition.set(5, 20.0f); // 20, 19
        yposition.set(5, 19.0f); // 20, 19
        xposition.set(6, 16.0f); // 16, 23
        yposition.set(6, 23.0f); // 16, 23
        xposition.set(7, 14.0f); // 14, 28
        yposition.set(7, 28.0f); // 14, 28
        xposition.set(8, 14.0f); // 14, 34
        yposition.set(8, 34.0f); // 14, 34
        xposition.set(9, 17.0f); // 17, 40
        yposition.set(9, 40.0f); // 17, 40
        xposition.set(10, 20.0f); // 20, 44
        yposition.set(10, 44.0f); // 20, 44
        xposition.set(11, 23.0f); // 23, 49
        yposition.set(11, 49.0f); // 23, 49


        //second loop
        xposition.set(12, 23.0f);// 23, 53
        yposition.set(12, 53.0f);// 23, 53
        xposition.set(13, 18.0f); // 18, 56
        yposition.set(13, 56.0f); // 18, 56
        xposition.set(14, 13.0f); // 13, 60
        yposition.set(14, 60.0f); // 13, 60
        xposition.set(15, 10.0f); // 10, 65
        yposition.set(15, 65.0f); // 10, 65
        xposition.set(16, 8.0f); // 8, 70
        yposition.set(16, 70.0f); // 8, 70
        xposition.set(17, 9.0f); // 9, 73
        yposition.set(17, 73.0f); // 9, 73
        xposition.set(18, 12.0f); // 12, 81
        yposition.set(18, 81.0f); // 12, 81
        xposition.set(19, 17.0f); // 17, 85
        yposition.set(19, 85.0f); // 17, 85
        xposition.set(20, 23.0f); // 23, 86
        yposition.set(20, 86.0f); // 23, 86
        xposition.set(21, 28.0f); // 28, 86
        yposition.set(21, 86.0f); // 28, 86
        xposition.set(22, 33.0f); // 33, 83
        yposition.set(22, 83.0f); // 33, 83
        xposition.set(23, 37.0f); // 37, 78
        yposition.set(23, 78.0f);; // 37, 78



        xposition.set(24, 39.0f); // 39, 73
        yposition.set(24, 73.0f); // 39, 73
        xposition.set(25, 40.0f); // 40, 67
        yposition.set(25, 67.0f); // 40, 67
        xposition.set(26, 44.0f); // 44, 62
        yposition.set(26, 62.0f); // 44, 62
        xposition.set(27, 47.0f); // 47, 59
        yposition.set(27, 59.0f); // 47, 59
        xposition.set(28, 53.0f); // 53, 56
        yposition.set(28, 56.0f); // 53, 56
        xposition.set(29, 59.0f); // 59, 54
        yposition.set(29, 54.0f); // 59, 54
        xposition.set(30, 60.0f); // 60, 54
        yposition.set(30, 54.0f); // 60, 54
        xposition.set(31, 71.0f);; // 71, 54
        yposition.set(31, 54.0f);; // 71, 54


           // return;
        //}

    }

    int getXmax(){
        return xmax;
    }

    int getYmax(){
        return ymax;
    }

    int getXpixel(){
        return xpixel;
    }

    int getYpixel(){
        return ypixel;
    }

    int getnumCoords(){
        return numberOfCoordinates;
    }

    Float getXPosition(int index){
        return xposition.get(index);
    }

    Float getYPosition(int index){
        return yposition.get(index);
    }

    void setXPosition(int index, Float pos){
        xposition.set(index, pos);
        return;
    }

    void setYPosition(int index, Float pos){
        yposition.set(index, pos);
        return;
    }

    void setnumCoords(int num){
        numberOfCoordinates = num;
    }

    void setXmax(int value){
        xmax = value;
    }
    void setYmax(int value){
        ymax = value;
    }

    void setXpixel(int pixel){
        xpixel = pixel;
    }
    void setYpixel(int pixel){
        ypixel = pixel;
    }


    /* TBD function gets all the value of the coordinates for the track diagram
       and stores them in the ArrayLists.  The view code will use the array
        lists to draw the lines.  Will work in conjuction with the SetAllDiagramData method
         **** TBD */
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
        xmax = 0;
        ymax = 0;
        xpixel = 0;
        ypixel = 0;
        NavData = true;
    }

    public TrackDiagram(int num) {
        this.numberOfCoordinates = num;
        CreateDiagramArray(num);
        NavData = true;


        /***************temp for testing  ***********/
        SetAllDiagramData();
        xmax = 75;
        ymax = 95;
        xpixel = 1920;
        ypixel = 850;
    }


}
