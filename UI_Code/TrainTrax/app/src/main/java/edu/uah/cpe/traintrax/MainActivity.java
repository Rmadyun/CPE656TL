package edu.uah.cpe.traintrax;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

;import com.traintrax.navigation.service.rest.client.RemoteTrainNavigationService;

public class MainActivity extends AppCompatActivity {

    final String module = "MainActivity";
    final Context context = this;
    final MainActivity activity = this;

    String hostName = "10.0.2.2"; //"192.168.1.104";//"10.0.2.2";
    Integer port = 8182;

    private RetrieveTrackGeometryTask retrieveTrackGeometryTask = new RetrieveTrackGeometryTask(){
        @Override
        protected void onPostExecute(TrackGeometry trackGeometry) {
            super.onPostExecute(trackGeometry);
            SharedObjectSingleton.getInstance().setTrackDiagram(new TrackDiagram(trackGeometry));
            SharedObjectSingleton.getInstance().setTrackSwitchInfo(new TrackSwitchInfo(trackGeometry));

            //Find the main diagram view associated with the view
            View diagramView = activity.findViewById(R.id.myview);

            if(diagramView != null) {

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

        SharedObjectSingleton.getInstance().setTrainNavigationServiceInterface(new RemoteTrainNavigationService(hostName, 8183));

        retrieveTrackGeometryTask.execute(hostName, port.toString());

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
    }

    public void onMonitorButtonClicked(View view) {
         //gets the coordinates for the track database in order to draw the track
        // on the screen
        Intent i = new Intent(context, TrainMonitorActivity.class);
        //i.putExtra("courseArg", itemValue);
        startActivityForResult(i, 1);
    }
}
