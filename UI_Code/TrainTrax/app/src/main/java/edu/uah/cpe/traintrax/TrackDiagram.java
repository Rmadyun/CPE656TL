package edu.uah.cpe.traintrax;

import com.traintrax.navigation.database.library.AdjacentPoint;
import com.traintrax.navigation.database.library.RepositoryEntry;
import com.traintrax.navigation.database.library.TrackBlock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.traintrax.navigation.database.library.*;
import com.traintrax.navigation.database.rest.client.RemoteAdjacentPointRepository;
import com.traintrax.navigation.database.rest.client.RemoteTrackBlockRepository;
import com.traintrax.navigation.database.rest.client.RemoteTrackPointRepository;

public class TrackDiagram {
    List<Polygon> shapes;
    int num_shapes;
    List<ShapeCoordinate> coordinates;
    private int xmax;  // maximum x coordinate of track
    private int ymax;  // maximum y coordinate of track
    private int xpixel;// width in pixels of device screen
    private int ypixel; // height in pixels of device screen
    Boolean NavData;                   // used to determine if position data exists

    int getNumShapes() {
        return num_shapes;
    }


    void setNumShapes(int num) {
        num_shapes = num;
    }

    //pulls switch data from database (or wherever and sets all values
    void SetAllDiagramData(TrackGeometry trackGeometry) {

        //GetCoordinate data from somewhere
        int num_cords = 0;
        Float xPos = 0.0f;
        Float yPos = 0.0f;


        try {

            List<RepositoryEntry<TrackBlock>> trackBlocks = trackGeometry.getTrackBlocks();
            List<RepositoryEntry<TrackPoint>> trackPoints = trackGeometry.getTrackPoints();
            List<RepositoryEntry<AdjacentPoint>> adjacentPoints = trackGeometry.getAdjacentPoints();

            Track trainTrack = Track.createTrack(trackBlocks, trackPoints, adjacentPoints);

            shapes = trainTrack.getShapes();

            num_shapes = shapes.size();
            coordinates = new ArrayList<ShapeCoordinate>(Arrays.asList(new ShapeCoordinate[num_shapes]));


            int shapecount = 0;
            for (Polygon shape : shapes) {
                List<Vertex> points = shape.getVertices();
                ShapeCoordinate temp_coord = new ShapeCoordinate();
                num_cords = points.size();
                temp_coord.numberOfCoordinates = num_cords;
                temp_coord.xposition = new ArrayList<Float>(Arrays.asList(new Float[num_cords]));
                temp_coord.yposition = new ArrayList<Float>(Arrays.asList(new Float[num_cords]));
                temp_coord.trackBlockName = new ArrayList<String>(Arrays.asList(new String[num_cords]));


                for (int i = 0; i < num_cords; i++) {
                    Vertex point = points.get(i);
                    Coordinate position = point.getPosition();
                    float xcord = (float) position.getX();
                    float ycord = (float) position.getY();
                    temp_coord.xposition.set(i, xcord);
                    temp_coord.yposition.set(i, ycord);

                    TrackBlockModel temptrackblock;

                    int size = trainTrack.getTrackBlockModels().size();
                    for (int j = 0; j < size; j++) {
                        temptrackblock = trainTrack.getTrackBlockModels().get(j);
                        List<Vertex> blockpoints = temptrackblock.getPoints();

                        for (int k = 0; k < blockpoints.size(); k++) {
                            Vertex tempvertex = blockpoints.get(k);
                            Coordinate tempcoord = tempvertex.getPosition();

                            double tempx = tempcoord.getX();
                            double tempy = tempcoord.getY();

                            if (tempx == position.getX() && tempy == position.getY())
                                temp_coord.trackBlockName.set(i, temptrackblock.getBlockName());

                        }
                    }
                }

                //get max x from collection

                float xmax_f = Collections.max(temp_coord.xposition);
                float ymax_f = Collections.max(temp_coord.yposition);

                if (xmax_f > xmax)
                    xmax = (int) xmax_f;


                if (ymax_f > ymax)
                    ymax = (int) ymax_f;


                // TODO temp until all the track data is here
                xmax = 210;
                ymax = 95;


                coordinates.set(shapecount, temp_coord);
                shapecount++;
            }
        } catch (Exception exception) {
            exception.printStackTrace();

        }
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

    /**
     * Constructor
     *
     * @param trackGeometry A description of all of the information known about the
     *                      Positive Train control test bed
     */
    public TrackDiagram(TrackGeometry trackGeometry) {
        this.num_shapes = 0;
        xmax = 0;
        ymax = 0;
        NavData = false;

        //DEBUG: This test implementation will use test data for the track
        //if track geometry data is not provided
        if(trackGeometry == null){
            TestTrackBlockRepository trackBlockRepository = new TestTrackBlockRepository();
            TestTrackPointRepository trackPointRepository = new TestTrackPointRepository();
            TestAdjacentPointRepository adjacentPointRepository = new TestAdjacentPointRepository();
            TestTrackSwitchRepository trackSwitchRepository = new TestTrackSwitchRepository();

            List<RepositoryEntry<TrackBlock>> trackBlocks = trackBlockRepository.findAll();
            List<RepositoryEntry<TrackPoint>> trackPoints = trackPointRepository.findAll();
            List<RepositoryEntry<AdjacentPoint>> adjacentPoints = adjacentPointRepository.findAll();
            List<RepositoryEntry<TrackSwitch>> trackSwitches = trackSwitchRepository.findAll();

            //Load all of the test data to define the shape (i.e. geometry) of the track
            trackGeometry = new TrackGeometry(trackBlocks, trackPoints, adjacentPoints, trackSwitches);

        }

        //Set diagram data if we were able to read data from
        //the track geometry database
        if (trackGeometry != null) {
            NavData = true;
            SetAllDiagramData(trackGeometry);
        }
        //xmax = 210;
        //ymax = 95;
        //xpixel = 1920;
        //ypixel = 850;
    }

    public class ShapeCoordinate {
        private int numberOfCoordinates;  // num of coordinates for track diagram
        private List<Float> xposition; // x position of coordinate
        private List<Float> yposition; // y position of coordinate
        private List<String> trackBlockName; // Track Block assigned to this coordinate

        int getnumCoords() {
            return numberOfCoordinates;
        }

        Float getXPosition(int index) {
            return xposition.get(index);
        }

        Float getYPosition(int index) {
            return yposition.get(index);
        }

        String getTrackBlockName(int index) {
            return trackBlockName.get(index);
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

        void setTrackBlockId(int index, String name) {
            trackBlockName.set(index, name);
        }

    }
}
