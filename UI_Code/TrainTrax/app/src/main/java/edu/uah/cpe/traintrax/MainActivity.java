package edu.uah.cpe.traintrax;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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
    private static final String NavDbPortKey = "navDbPortKey";
    private static final String NavSvcPortKey = "navSvcPortKey";
    private static final String HostnameKey = "hostnameKey";

    final String module = "MainActivity";
    final Context context = this;
    final MainActivity activity = this;
    MainSettings settings = new MainSettings();  //class for accessing settings

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
        int portnum = settings.getInt(NavDbPortKey, 0);
        if (portnum != 0) {
            //set the port number to the value saved in preferences if it's been set
            this.settings.setDatabasePortNumber(portnum);
        }

        portnum = settings.getInt(NavSvcPortKey, 0);
        if (portnum != 0) {
            //set the port number to the value saved in preferences if it's been set
            this.settings.setNavigationServicePortNumber(portnum);
        }

        String host = settings.getString(HostnameKey, null);
        if (host != null) {
            this.settings.setHostName(host);
            //set the hostName to the value saved in preferences if it's been set
        }

        //Set the instance to the Train Navigation Service that will be used throughout the
        //application
        SharedObjectSingleton.getInstance().setTrainNavigationServiceInterface(new RemoteTrainNavigationService(this.settings.getHostName(), this.settings.getNavigationServicePortNumber()));

        //Launches a task in a background thread to read all of the point and information about the
        //track from the Train Navigation Database so that the GUI is not locked while reading happens.
        //(p.s. Android throws exception if you don't do this)
        retrieveTrackGeometryTask.execute(this.settings.getHostName(), Integer.toString(this.settings.getDatabasePortNumber()));
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

            savePreferences();
            Toast.makeText(this, "AsITravel Preference File Saved.", Toast.LENGTH_SHORT).show();

            //TODO: Reiniitalize the Nav Sevice and Nav Database references here so that
            //data from the specified location is used.


            return super.onOptionsItemSelected(item);

        }

        //Generate the settings window prompt

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        //Click listener for the set button of any setting.
        DialogInterface.OnClickListener setButtonClickListener = null;

        //Field used to receive input for a setting
        final EditText input = new EditText(context);

        alertDialogBuilder.setView(input);

        //Edit Database Service Port Number
        if (id == R.id.action_nav_db_port) {
            input.setInputType(InputType.TYPE_CLASS_NUMBER);
            String sport = String.valueOf(settings.getDatabasePortNumber());
            alertDialogBuilder.setTitle("Current database service port # is " + sport);
            alertDialogBuilder.setMessage("Enter Database Service Port #:");
            setButtonClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String textvalue = input.getText().toString();

                    int portNum = Integer.valueOf(textvalue);
                    settings.setDatabasePortNumber(portNum);
                }
            };
        }

        //Edit Navigation Service Port Number
        if (id == R.id.action_nav_svc_port) {
            input.setInputType(InputType.TYPE_CLASS_NUMBER);
            String sport = String.valueOf(settings.getNavigationServicePortNumber());
            alertDialogBuilder.setTitle("Current navigation service port # is " + sport);
            alertDialogBuilder.setMessage("Enter Navigation Service Port #:");
            setButtonClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String textvalue = input.getText().toString();

                    int portNum = Integer.valueOf(textvalue);
                    settings.setNavigationServicePortNumber(portNum);
                }
            };
        }

        //Edit IP Address
        if (id == R.id.action_ipaddress) {
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            String sipaddr = String.valueOf(settings.getHostName());
            alertDialogBuilder.setTitle("Current IP Address is set to " + sipaddr);
            alertDialogBuilder.setMessage("Enter IP Address:");
            setButtonClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String textvalue = input.getText().toString();
                    String hostName = textvalue;
                    settings.setHostName(hostName);
                }
            };
        }

        //continue building dialog based on selected option
        alertDialogBuilder.setCancelable(false);

        //Don't add set button if it is an unsupported button
        if(setButtonClickListener != null) {
            alertDialogBuilder.setPositiveButton("Set",
                    setButtonClickListener);
        }

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
        editor.putInt(NavDbPortKey, this.settings.getDatabasePortNumber());
        editor.putInt(NavSvcPortKey, this.settings.getNavigationServicePortNumber());
        editor.putString(HostnameKey, this.settings.getHostName());
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
