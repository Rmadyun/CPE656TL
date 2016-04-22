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
import android.view.LayoutInflater;
import android.view.View;
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

public class MyView extends View {

    private ArrayList<Rect> rectangles = new ArrayList<Rect>();

    public MyView(Context context) {
        super(context);
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        TrackDiagram TrackDig = SharedObjectSingleton.getInstance().getTrackDiagram();

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);

        if (TrackDig != null) {
            if (!TrackDig.GetNavData()) {

                /* Display error image and message on screen */

                Bitmap error_screen = BitmapFactory.decodeResource(getResources(),
                        R.drawable.trackgeo);
                //canvas.drawColor(Color.BLACK);
                //canvas.drawBitmap(MyBitmap, null, rectangle, null)
                canvas.drawBitmap(error_screen, null, new Rect(0, 50, 2300, 1100), null);
                canvas.drawPaint(paint);
                paint.setColor(Color.BLACK);
                paint.setTextSize(100);
                canvas.drawText("Track Geometry Data Not Found!", 350, 1200, paint);


                return;
            }

            super.onDraw(canvas);
            int x = getWidth();
            int y = getHeight();
            int radius;
            radius = 100;
            canvas.drawPaint(paint);

            //Add Path, will replace the coordinates in here with the ArrayLists once
            // the TrackDiagram model class works correctly

            //Use Color.parseColor to define HTML colors
            paint.setColor(Color.parseColor("#CCCfff"));

            // optional method of just adding the rectangles directly from the coordinates


            paint.setStrokeWidth(100);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.GREEN);
            Path path = new Path();

            // get default values for Track Diagram
            int xmax = TrackDig.getXmax();
            int ymax = TrackDig.getYmax();
            int xpixel = TrackDig.getXpixel();
            int ypixel = TrackDig.getYpixel();
            int num_shapes = TrackDig.getNumShapes();

            //set paint style
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(15);

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

                    if (j == 0) {
                        testPath.moveTo(xcord, ycord);
                    } else {
                        testPath.lineTo(xcord, ycord);
                    }
                }

                canvas.drawPath(testPath, paint);
            }
        }
    }
}

