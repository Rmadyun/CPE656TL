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

import com.traintrax.navigation.service.TrainNavigationServiceEventSubscriber;
import com.traintrax.navigation.service.TrainPositionUpdatedEvent;

public class TrainMonitorActivity extends AppCompatActivity {

    final String module = "MainActivity";
    final Context context = this;
    AppCompatActivity activity = this;
    //private ListView listView;
    //edu.uah.cpe.traintrax.TrackSwitch switchdata = new edu.uah.cpe.traintrax.TrackSwitch();

    private TrainNavigationServiceEventSubscriber trainNavigationServiceEventSubscriber = new TrainNavigationServiceEventSubscriber(){
        @Override
        public void TrainPositionUpdated(TrainPositionUpdatedEvent event) {

            final String message = "PositionUpdate: " + event.getTrainIdentifier() + " " + event.getPosition().getValue().getX();

            //Make sure to run in the UI thread since this is invoked from a timer (i.e. separate thread)
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);

                    builder.setMessage(message);
                }
            });

        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_monitor);
         Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


    public void onMonitorButtonClicked(View view) {
        //gets the coordinates for the track database in order to draw the track
        // on the screen
        //Intent i = new Intent(context, TrainHistoryActivity.class);
        //i.putExtra("courseArg", itemValue);
        //startActivityForResult(i, 1);
    }

}
