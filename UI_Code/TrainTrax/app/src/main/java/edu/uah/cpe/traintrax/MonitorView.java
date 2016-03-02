package edu.uah.cpe.traintrax;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import java.util.ArrayList;
import java.util.List;


public class MonitorView extends View {
    //set for 7 temp coordinates
    TrackDiagram TrackDig = new TrackDiagram();
    TrackSwitchInfo Switch = new TrackSwitchInfo();

    public MonitorView(Context context) {
        super(context);
    }
    public MonitorView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MonitorView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub

        Paint paint = new Paint();

        super.onDraw(canvas);
        int x = getWidth();
        int y = getHeight();
        int radius;
        radius = 100;
        canvas.drawPaint(paint);

        //Use Color.parseColor to define HTML colors
        paint.setColor(Color.parseColor("#CCCfff"));
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

        for (int i = 0; i < num_shapes; i++)
        {
            //testPath.reset();
        TrackDiagram.ShapeCoordinate tempShape_cord;
            tempShape_cord = tmp.get(i);

           int num_points = tempShape_cord.getnumCoords();

            //NOTE: This also could be done with edges of the polygons
            //To simply draw each line that the polygon has.
            for(int j = 0; j < num_points; j++ ){
               float xcord = tempShape_cord.getXPosition(j);
                float ycord = tempShape_cord.getYPosition(j);

                xcord = (xcord / xmax) * xpixel;
                ycord = (ycord / ymax) * ypixel;

                if(j== 0){
                    testPath.moveTo(xcord, ycord);
                }
                else{
                    testPath.lineTo(xcord, ycord);
                }
            }

            canvas.drawPath(testPath, paint);
        }


       int num_switches = Switch.getNum_switches();

for (int i = 0; i < num_switches; i++)
        {
            float xcord = Switch.getXPosition(i);
            float ycord = Switch.getYPosition(i);

            //x = (x/xmax) * screen resolution;
            // scale coordinates to resolution size
            xcord = (xcord / xmax) * xpixel;
            ycord = (ycord / ymax) * ypixel;

            //need to figure out a way to size this icon better
            int xrect = ((int) xcord);
            int yrect = ((int) ycord);

            Bitmap switchstate;

            Boolean state = false;
            if (state == false) {
                switchstate = BitmapFactory.decodeResource(getResources(),
                        R.drawable.bypass);
            }
            else
            {
                switchstate = BitmapFactory.decodeResource(getResources(),
                        R.drawable.pass);
            }

            canvas.drawBitmap(switchstate, null, new Rect(xrect, yrect, (xrect + 50), (yrect + 50)), null);
            //canvas.drawBitmap(switchstate, 250.0f, 250.0f, paint)
        }
    }


}
