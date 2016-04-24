package edu.uah.cpe.traintrax;

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

        TrackDiagram TrackDig = SharedObjectSingleton.getInstance().getTrackDiagram();
        // get default values for Track Diagram

        //nothing to do if null this may need to be tweaked #TODO
        //what does TrackDig null mean?

        if (TrackDig == null) {
            return;
        }

        //no track diagram data set yet

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
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);


        if (TrackDig != null) {
            if (!TrackDig.GetNavData()) {

                /* Display error image and message on screen */

                Bitmap error_screen = BitmapFactory.decodeResource(getResources(),
                        R.drawable.trackgeo);
                canvas.drawBitmap(error_screen, null, new Rect(0, 50, 2300, 1100), null);
                canvas.drawPaint(paint);
                paint.setColor(Color.BLACK);
                paint.setTextSize(100);
                canvas.drawText("Track Geometry Data Not Found!", 350, 1200, paint);
                return;
            }
        }

        super.onDraw(canvas);

        canvas.drawPaint(paint);

        //Add Path, will replace the coordinates in here with the ArrayLists once
        // the TrackDiagram model class works correctly

        //Use Color.parseColor to define HTML colors
        paint.setColor(Color.parseColor("#CCCfff"));

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
    }
}

