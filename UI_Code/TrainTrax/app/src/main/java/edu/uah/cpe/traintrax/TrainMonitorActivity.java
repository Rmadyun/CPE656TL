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

    private static final int periodBetweenPositionRefreshInMilliseconds = 100;
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

            //Update TrainPosInfo with the latest information for this train.
            TrainPos.addOrUpdateTrain(event.getPosition().getTrainId(), Float.valueOf((float) event.getPosition().getPosition().getX()),
                    Float.valueOf((float) event.getPosition().getPosition().getY()));

/* Test CODE
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
                TrainPos.setXPosition(2, 0.0f); */


            //Make sure to run in the UI thread since this is invoked from a timer (i.e. separate thread)
            /*activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //Add any code here that needs to make changes to the UI


                    //Trigger the display to be referenced for the train monitor view
                    myView.invalidate();
                }
            }); */

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

        final Runnable r = new Runnable() {
            public void run() {
                myView.invalidate();

                myHandler.postDelayed(this, periodBetweenPositionRefreshInMilliseconds);
            }
        };

        //update every second
        myHandler.postDelayed(r, periodBetweenPositionRefreshInMilliseconds);

      /*  setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();


            }
        }); */
    }


    public void onMonitorButtonClicked(View view) {
        //gets the coordinates for the track database in order to draw the track
        // on the screen
        //Intent i = new Intent(context, TrainHistoryActivity.class);
        //i.putExtra("courseArg", itemValue);
        //startActivityForResult(i, 1);
    }

}



