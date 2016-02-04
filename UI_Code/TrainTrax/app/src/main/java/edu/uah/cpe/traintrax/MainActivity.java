package edu.uah.cpe.traintrax;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.app.Activity;
import android.content.Context;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
;

public class MainActivity extends Activity {
    edu.uah.cpe.traintrax.TrackDiagram trackdata = new edu.uah.cpe.traintrax.TrackDiagram();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    } /* On calculate button clicked */

    public void onMonitorButtonClicked(View view) {
         //gets the coordinates for the track database in order to draw the track
        // on the screen
       trackdata.getCoordinates();
    }
    @Override
    public void setTitle(CharSequence title) {
        //you can override the other setTitle as well if you need in
        setTitle("Train Trax System");
    }



    }
