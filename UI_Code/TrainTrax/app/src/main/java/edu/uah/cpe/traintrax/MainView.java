package edu.uah.cpe.traintrax;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;


//import com.traintrax.navigation.database.library.AdjacentPoint;
//import com.traintrax.navigation.database.library.RepositoryEntry;
//import com.traintrax.navigation.database.library.TrackBlock;
//import com.traintrax.navigation.database.library.TrackPoint;
//import com.traintrax.navigation.database.rest.test.TestAdjacentPointRepository;
//import com.traintrax.navigation.database.rest.test.TestTrackBlockRepository;
//import com.traintrax.navigation.database.rest.test.TestTrackPointRepository;

import java.util.ArrayList;
import java.util.List;

public class MainView extends View {


    public MainView(Context context) {
        super(context);
    }

    public MainView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MainView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        //The value of the singleton changes once the task of reading track geometry data from the
        //network has been completed.
        //If you do not read the current value of the singleton, then it will have the initial value
        //(null) forever, even after the track data has been loaded.
        TrackDiagram TrackDig = SharedObjectSingleton.getInstance().getTrackDiagram();

        //return if TrackDig class has not been set yet
        if (TrackDig == null) {
            return;
        }

        int xmax = TrackDig.getXmax();
        int ymax = TrackDig.getYmax();
        int xpixel = TrackDig.getXpixel();
        int ypixel = TrackDig.getYpixel();
        int num_shapes = TrackDig.getNumShapes();


        DisplayMetrics metrics = new DisplayMetrics();

        ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay().getMetrics(metrics);

        //From this, we can get the information required to size the display:

        int widthPixels = metrics.widthPixels;
        int heightPixels = metrics.heightPixels;
        xpixel = widthPixels - 125;
        ypixel = heightPixels - 315;
        //#TODO remove test values for displaying on my android device
        //xpixel = 1920;
        //ypixel = 850;

        // TODO Auto-generated method stub

        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        canvas.drawPaint(paint);

        //Add Path, will replace the coordinates in here with the ArrayLists once
        // the TrackDiagram model class works correctly

        //set paint style
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(15);
        paint.setColor(Color.GRAY);

        //Creates a path based on a collection of lines.
        Path testPath = new Path();

        List<TrackDiagram.ShapeCoordinate> tmp;
        tmp = TrackDig.coordinates;

        for (int i = 0; i < num_shapes; i++) {
            //testPath.reset();
            TrackDiagram.ShapeCoordinate tempShape_cord;
            tempShape_cord = tmp.get(i);

            int num_points = tempShape_cord.getnumCoords();

            //NOTE: This also could be done with edges of the polygons
            //To simply draw each line that the polygon has.
            for (int j = 0; j < num_points; j++) {
                float xcord = tempShape_cord.getXPosition(j);
                float ycord = tempShape_cord.getYPosition(j);

                xcord = (xcord / xmax) * xpixel;
                ycord = (ycord / ymax) * ypixel;

                //adjust for different orientation in coordinate system
                ycord = ypixel - ycord;

                if (j == 0) {
                    testPath.moveTo(xcord, ycord);
                } else {
                    testPath.lineTo(xcord, ycord);
                }
            }

            canvas.drawPath(testPath, paint);
        }

        //draw legend
        Bitmap legend = BitmapFactory.decodeResource(getResources(),
                R.drawable.legend);

        Bitmap resizedBitmap = Bitmap.createScaledBitmap(legend, 450, 300, false);
        canvas.drawBitmap(resizedBitmap, (xpixel / 2) - 150, ypixel - 400, paint);

        //unable to get Track Digagram data from Train Navigation Database
          if (!TrackDig.GetNavData()) {

                // Display error message on screen letting them know default data is loaded

                paint.setTextSize(45);
                paint.setColor(Color.BLACK);
                paint.setStyle(Paint.Style.FILL);
                //canvas.drawText("Track Geometry Data Not Found!", 100, 200, paint);
                canvas.drawText("Error:  Unable to connect to Train Navigation Database, default track data loaded", 100, 200, paint);
             }
    }


}


