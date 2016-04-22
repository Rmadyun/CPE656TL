package edu.uah.cpe.traintrax;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;


import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.os.Handler;

import com.traintrax.navigation.service.TrainNavigationServiceEventSubscriber;
import com.traintrax.navigation.service.TrainNavigationServiceInterface;
import com.traintrax.navigation.service.TrainPositionUpdatedEvent;
import com.traintrax.navigation.service.ValueUpdate;
import com.traintrax.navigation.service.position.TrainPositionEstimate;
import com.traintrax.navigation.service.rest.client.RemoteTrainNavigationService;


public class TrainMonitorActivity extends AppCompatActivity {


    final String module = "MainActivity";
    final Context context = this;
    AppCompatActivity activity = this;
    final Handler myHandler = new Handler(); //used to update GUI
    private MonitorView myView;

    private TrainNavigationServiceEventSubscriber trainNavigationServiceEventSubscriber = new TrainNavigationServiceEventSubscriber() {
        @Override
        public void TrainPositionUpdated(TrainPositionUpdatedEvent event) {

            //Method handles notification that a new train position update has been received.
            //NOTE: This is presently implemented by a periodic poll of train position from a timer.
            TrackSwitchInfo Switch = SharedObjectSingleton.getInstance().getTrackSwitchInfo();
            TrainPosInfo TrainPos = SharedObjectSingleton.getInstance().getTrainPosInfo();

            //TODO: Only update the information for the train whose position information was reported here

            // this block get position updates from train navigation service every second

            //get updated list of train identifiers
               /* List <String> trains = NavService.GetKnownTrainIdentifiers();

                //loop through the train position list to see if there are any new identifiers
                int num_trains = myView.TrainPos.getNumTrains();

                //check to see if there are any IDs that need to be added from TrainNavigation
                //Service to the TrainPosInfo
                for (int i = 0; i < trains.size(); i++)
                {
                    //get train ID from navigation service
                    String trainID = trains.get(i);
                    int found = 0;

                 //loop through all of the IDs stored in TrainPosInfo to see if the ID
                 // in the train navigation service exists
                    for (int j = 0; j < num_trains; j++)
                    {
                        String trainID2 = myView.TrainPos.getTrainID(j);
                        if (trainID.equals(trainID2)) {
                        found = 1;
                        break;
                    }

                        if (j == num_trains && found == 0)
                        {
                            //need to add this trainID
                            //add a new train to the TrainPosInfo class
                            myView.TrainPos.addTrainID(i, trainID);
                            myView.TrainPos.addXPosition(i, 0.0f);
                            myView.TrainPos.addYPosition(i, 0.0f);
                            myView.TrainPos.setNumTrains(num_trains + 1);
                        }
                    }
                }

                //update the rest of the train positions
                UpdateTrainPositions(trains); */


            //test data the above code is meant to work with the actual navigation service remove #TODO
            Boolean passTest = Boolean.FALSE;
            passTest = Switch.getPassState(0);

            if (passTest == true)
                Switch.setPassState(0, Boolean.FALSE);
            else
                Switch.setPassState(0, Boolean.TRUE);

            //test data to move train x position
            Float tempPos = myView.TrainPos.getXPosition(0);
            if (tempPos >= 0 && tempPos < 210)
                TrainPos.setXPosition(0, tempPos + 3.0f);
            else if (tempPos >= 210)
                TrainPos.setXPosition(0, 0.0f);

            //test data to move train x position
            tempPos = TrainPos.getXPosition(1);
            if (tempPos >= 0 && tempPos < 210)
                TrainPos.setXPosition(1, tempPos + 5.0f);
            else if (tempPos >= 210)
                TrainPos.setXPosition(1, 0.0f);

            //test data to move train x position
            tempPos = TrainPos.getXPosition(2);
            if (tempPos >= 0 && tempPos < 210)
                TrainPos.setXPosition(2, tempPos + 10.0f);
            else if (tempPos >= 210)
                TrainPos.setXPosition(2, 0.0f);


            final String message = "PositionUpdate: " + event.getTrainIdentifier() + " " + event.getPosition().getPosition().getX();

            //Make sure to run in the UI thread since this is invoked from a timer (i.e. separate thread)
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //Add any code here that needs to make changes to the UI

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);

                    builder.setMessage(message);

                    //Trigger the display to be referenced for the train monitor view
                    myView.invalidate();
                }
            });

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_monitor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        myView = (MonitorView) findViewById(R.id.myview);

        TrainNavigationServiceInterface trainNavigationService = SharedObjectSingleton.getInstance().getTrainNavigationServiceInterface();

        if (trainNavigationService != null) {
            trainNavigationService.Subscribe(trainNavigationServiceEventSubscriber);
        }

      /*  setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();


            }
        }); */

        //get initial list of train identifiers create array
/*        List<String> trains = NavService.GetKnownTrainIdentifiers();
        myView.TrainPos.CreateTrainArray(trains.size());

        //get positions for each train
         for (int i = 0; i < trains.size(); i++)
        {
            String trainID = trains.get(i);
            //get position
           ValueUpdate Coord = NavService.GetLastKnownPosition(trainID);

            //needs to be corrected #TODO
            //ValueUpdate<Coordinate> value = Coord.getValue();
            //Float x_pos =  value.getX();
            //Float y_pos =  value.getY();

            Float x_pos = 0.0f;
            Float y_pos = 0.0f;

            //set the values on the TrainPosInfo
            myView.TrainPos.setTrainID(i, trainID);
            myView.TrainPos.setXPosition(i, x_pos);
            myView.TrainPos.setYPosition(i, y_pos);
        } */
    }


    public void onMonitorButtonClicked(View view) {
        //gets the coordinates for the track database in order to draw the track
        // on the screen
        //Intent i = new Intent(context, TrainHistoryActivity.class);
        //i.putExtra("courseArg", itemValue);
        //startActivityForResult(i, 1);
    }

    public void UpdateTrainPositions(List<String> trains) {

        //loop through the train position list to see if there are any new identifiers

        TrainNavigationServiceInterface NavService = SharedObjectSingleton.getInstance().getTrainNavigationServiceInterface();

        //update trains
        for (int i = 0; i < trains.size(); i++) {
            String trainID = trains.get(i);

            Float x_pos = 0.0f;
            Float y_pos = 0.0f;

            //#TODO Provide better error handling if retrieving the position fails.

            //get position
            try {
                TrainPositionEstimate Coord = NavService.GetLastKnownPosition(trainID);

                x_pos = Float.valueOf((float) Coord.getPosition().getX());
                y_pos = Float.valueOf((float) Coord.getPosition().getY());
            } catch (Exception e) {
                e.printStackTrace();
            }

            myView.TrainPos.setTrainID(i, trainID);
            myView.TrainPos.setXPosition(i, x_pos);
            myView.TrainPos.setYPosition(i, y_pos);

        }
    }
}



