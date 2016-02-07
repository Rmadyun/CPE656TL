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



public class MonitorView extends View {
    //set for 7 temp coordinates
    edu.uah.cpe.traintrax.TrackDiagram TrackDig = new edu.uah.cpe.traintrax.TrackDiagram(32);
    edu.uah.cpe.traintrax.TrackSwitch Switch = new edu.uah.cpe.traintrax.TrackSwitch(7);

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
        //paint.setStyle(Paint.Style.FILL);
        //paint.setColor(Color.WHITE);

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
        paint.setStrokeWidth(100);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.GREEN);
        Path path = new Path();

        // get default values for Track Diagram
        int xmax = TrackDig.getXmax();
        int ymax = TrackDig.getYmax();
        int xpixel = TrackDig.getXpixel();
        int ypixel = TrackDig.getYpixel();
        int num_coords = TrackDig.getnumCoords();


        for (int i = 0; i < num_coords; i++) {
                /* Get the X position and why position of the line */
            Float xcord = (Float) TrackDig.getXPosition(i);
            Float ycord = (Float) TrackDig.getYPosition(i);

            //x = (x/xmax) * screen resolution
            /* scale coordinates to resolution size */
            xcord = (xcord / xmax) * xpixel;
            ycord = (ycord / ymax) * ypixel;

            //set the initial path to the first point
            if (i == 0)
                path.moveTo(xcord, ycord);

            if (i > 0)
                path.lineTo(xcord, ycord);

        }

        canvas.drawPath(path, paint);

        //get default values for track switchs
        int num_switches = Switch.getNum_switches();

        for (int i = 0; i < num_switches; i++) {
            /* Get the X position and why position of the line */
            Float xcord = (Float) Switch.getXPosition(i);
            Float ycord = (Float) Switch.getYPosition(i);
            Boolean state = Switch.getPassState(i);

            //x = (x/xmax) * screen resolution
            /* scale coordinates to resolution size */
            xcord = (xcord / xmax) * xpixel;
            ycord = (ycord / ymax) * ypixel;

            int xrect = xcord.intValue();
            int yrect = ycord.intValue();

            Bitmap switchstate;

            if (state == false) {
                switchstate = BitmapFactory.decodeResource(getResources(),
                        R.drawable.bypass);
            }
            else
            {
                switchstate = BitmapFactory.decodeResource(getResources(),
                        R.drawable.pass);
            }
            //canvas.drawColor(Color.BLACK);
            //canvas.drawBitmap(MyBitmap, null, rectangle, null)
            canvas.drawBitmap(switchstate, null, new Rect(xrect, yrect, (xrect + 50), (yrect + 50)), null);
            //canvas.drawBitmap(switchstate, 250.0f, 250.0f, paint);


            //  for (Rect rect : rectangles) {
            //  canvas.drawRect(rect, paint);
            // }
        }
    }
}
