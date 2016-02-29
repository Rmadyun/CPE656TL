package edu.uah.cpe.traintrax;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
//import TrainNavigationDatabase.*;


public class TrackDiagram
{
    List <Polygon> shapes;
    int num_shapes;
    List <ShapeCoordinate> coordinates;
    private int xmax;  // maximum x coordinate of track
    private int ymax;  // maximum y coordinate of track
    private int xpixel;// width in pixels of device screen
    private int ypixel; // height in pixels of device screen
    Boolean NavData;                   // used to determine if position data exists

    /*void CreateDiagramArray(int num)
    {
        xposition = new ArrayList <Float>(Arrays.asList(new Float[num]));
        yposition = new ArrayList <Float>(Arrays.asList(new Float[num]));

        //initialize all positions to zero
        for (int i = 0; i < num; i++)
        {
            xposition.set(i, 0.0f);
            yposition.set(i, 0.0f);
        }
    } */

    int getNumShapes()
    {
        return num_shapes;
    }


    void setNumShapes(int num)
    {
        num_shapes = num;
    }

    //pulls switch data from database (or wherever and sets all values
    void SetAllDiagramData() {

        //GetCoordinate data from somewhere
        int num_cords = 0;
        Boolean state = false;
        Float xPos = 0.0f;
        Float yPos = 0.0f;

        TestTrackBlockRepository trackBlockRepository = new TestTrackBlockRepository();
        TestTrackPointRepository trackPointRepository = new TestTrackPointRepository();
        TestAdjacentPointRepository adjacentPointRepository = new TestAdjacentPointRepository();

        List<RepositoryEntry<TrackBlock>> trackBlocks = trackBlockRepository.findAll();
        List<RepositoryEntry<TrackPoint>> trackPoints = trackPointRepository.findAll();
        List<RepositoryEntry<AdjacentPoint>> adjacentPoints = adjacentPointRepository.findAll();

        Track trainTrack = Track.createTrack(trackBlocks, trackPoints, adjacentPoints);

        shapes = trainTrack.getShapes();

        num_shapes = shapes.size();
        coordinates = new ArrayList <ShapeCoordinate>(Arrays.asList(new ShapeCoordinate[num_shapes]));

        int shapecount = 0;
        for (Polygon shape : shapes) {
            List<Vertex> points = shape.getVertices();
            ShapeCoordinate temp_coord = new ShapeCoordinate();
            num_cords =  points.size();
            temp_coord.numberOfCoordinates = num_cords;
            temp_coord.xposition = new ArrayList <Float>(Arrays.asList(new Float[num_cords]));
            temp_coord.yposition = new ArrayList <Float>(Arrays.asList(new Float[num_cords]));

            //CreateDiagramArray(num_cords);

            for (int i = 0; i < num_cords; i++) {
                Vertex point = points.get(i);
                Coordinate position = point.getPosition();
                float xcord = (float) position.getX();
                float ycord = (float) position.getY();;
                temp_coord.xposition.set(i, xcord);
                temp_coord.yposition.set(i, ycord);
            }

            coordinates.set(shapecount, temp_coord);
            shapecount++;

            // return;
            //}

        }
        NavData = true;
    }

    int getXmax() {
        return xmax;
    }

    int getYmax() {
        return ymax;
    }

    int getXpixel() {
        return xpixel;
    }

    int getYpixel() {
        return ypixel;
    }

    void setXmax(int value) {
        xmax = value;
    }

    void setYmax(int value) {
        ymax = value;
    }

    void setXpixel(int pixel) {
        xpixel = pixel;
    }

    void setYpixel(int pixel) {
        ypixel = pixel;
    }

    Boolean GetNavData() {
        return NavData;
    }

    void SetNavData(Boolean flag) {
        NavData = flag;
    }


    /**
     * Constructor
     *
     * @param /* Default Constructor
     */
    public TrackDiagram() {
        this.num_shapes = 0;
        SetAllDiagramData();
        xmax = 210;
        ymax = 95;
        xpixel = 1920;
        ypixel = 850;
        //NavData = true;
    }

    public class ShapeCoordinate {
        private int numberOfCoordinates;  // num of coordinates for track diagram
        private List <Float> xposition; // x position of coordinate
        private List <Float> yposition; // y position of coordinate

        int getnumCoords()
        {
        return numberOfCoordinates;
        }

        Float getXPosition(int index) {
            return xposition.get(index);
        }

        Float getYPosition(int index) {
        return yposition.get(index);
        }

        void setXPosition(int index, Float pos) {
        xposition.set(index, pos);
        return;
        }

        void setYPosition(int index, Float pos) {
        yposition.set(index, pos);
        return;
        }

        void setnumCoords(int num) {
        numberOfCoordinates = num;
        }

    }
}
