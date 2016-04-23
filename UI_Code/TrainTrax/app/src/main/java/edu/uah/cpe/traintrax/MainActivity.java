package edu.uah.cpe.traintrax;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.traintrax.navigation.service.rest.client.RemoteTrainNavigationService;

import java.util.HashSet;
import java.util.Set;

public class MainActivity extends ActionBarActivity {

    public static final String PREFS_NAME = "MyPrefsFile";

    final String module = "MainActivity";
    final Context context = this;
    final MainActivity activity = this;
    MainSettings mySettings = new MainSettings();  //class for accessing settings

    String hostName = "10.0.2.2";
    Integer trainNavigationDatabasePort = 8182;
    Integer trainNavigationServicePort = 8183;

    private RetrieveTrackGeometryTask retrieveTrackGeometryTask = new RetrieveTrackGeometryTask() {
        @Override
        protected void onPostExecute(TrackGeometry trackGeometry) {
            super.onPostExecute(trackGeometry);
            SharedObjectSingleton.getInstance().setTrackDiagram(new TrackDiagram(trackGeometry));
            SharedObjectSingleton.getInstance().setTrackSwitchInfo(new TrackSwitchInfo(trackGeometry));
            SharedObjectSingleton.getInstance().setTrainPosInfo(new TrainPosInfo());

            //Find the main diagram view associated with the view
            View diagramView = activity.findViewById(R.id.myview);

            if (diagramView != null) {

                //Force redrawing of the diagram now that we have retrieved
                //track geometry information

                diagramView.invalidate();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Restore preferences
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();

        //Retrieve the values from Preference file
        Set<String> set = settings.getStringSet("listkey", new HashSet<String>());

        int portnum = settings.getInt("portKey", 0);
        if (portnum != 0) {
            //set the port number to the value saved in preferences if it's been set
            mySettings.setPortNum(portnum);
            trainNavigationDatabasePort = portnum;
        }

        String host = settings.getString("hostKey", null);
        if (host != null) {
            mySettings.setHostName(host);
            //set the hostName to the value saved in preferences if it's been set
            hostName = host;
        }

        //Set the instance to the Train Navigation Service that will be used throughout the
        //application
        SharedObjectSingleton.getInstance().setTrainNavigationServiceInterface(new RemoteTrainNavigationService(hostName, 8183));

        //Launches a task in a background thread to read all of the point and information about the
        //track from the Train Navigation Database so that the GUI is not locked while reading happens.
        //(p.s. Android throws exception if you don't do this)
        retrieveTrackGeometryTask.execute(hostName, trainNavigationDatabasePort.toString());
    }

    @Override

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //Save Preferences Selected
        if (id == R.id.action_save) {

            //TODO: Reiniitalize the Nav Sevice and Nav Database references here so that
            //data from the specified locaton is used.

            savePreferences();
            Toast.makeText(this, "AsITravel Preference File Saved.", Toast.LENGTH_SHORT).show();
            return super.onOptionsItemSelected(item);

        }


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        mySettings.setportFlag(false);

        //Edit Search Interval Selected
        if (id == R.id.action_portnum) {
            String sport = String.valueOf(mySettings.getPortNum());
            alertDialogBuilder.setTitle("Current portNum is " + sport);
            alertDialogBuilder.setMessage("Enter portNum:");
            mySettings.setportFlag(true);
        }

        //Edit Search Interval Selected
        if (id == R.id.action_ipaddress) {
            String sipaddr = String.valueOf(mySettings.getHostName());
            alertDialogBuilder.setTitle("Current IP Address is set to " + sipaddr);
            alertDialogBuilder.setMessage("Enter IP Address:");
        }


        //continue building dialog based on selected option
        final EditText input = new EditText(context);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        alertDialogBuilder.setView(input);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Set",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String textvalue = input.getText().toString();
                        if (mySettings.getportFlag()) {
                            int portNum = Integer.valueOf(textvalue);
                            mySettings.setPortNum(portNum);
                        } else {
                            String hostName = textvalue;
                            mySettings.setHostName(hostName);
                        }


                    }
                });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();


        return super.onOptionsItemSelected(item);
    }

    public void savePreferences() {
        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();

        //Set the values
        editor.putInt("portKey", mySettings.getPortNum());
        editor.putString("hostKey", mySettings.getHostName());
        editor.commit();

    }

    public void onMonitorButtonClicked(View view) {
        //gets the coordinates for the track database in order to draw the track
        // on the screen
        Intent i = new Intent(context, TrainMonitorActivity.class);
        //i.putExtra("courseArg", itemValue);
        startActivityForResult(i, 1);
    }
}
