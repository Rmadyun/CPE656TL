package edu.uah.cpe.traintrax;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;


    public class MyView extends View {
        edu.uah.cpe.traintrax.TrackDiagram TrackDig = new edu.uah.cpe.traintrax.TrackDiagram();

        //here need to get the ArrayLists from the Track Diagram model class
        //will use those to either add rectangles to the list or draw directly on the screen
        //from the array lists (both ways are done below)
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
            // TODO Auto-generated method stub

            super.onDraw(canvas);
            int x = getWidth();
            int y = getHeight();
            int radius;
            radius = 100;
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.WHITE);
            canvas.drawPaint(paint);

            //Add Rectangles method, will replace the coordinates in here with the ArrayLists once
            // the TrackDiagram model class works correctly

            //addrectangle(float left, float top, float right, float bottom)
            rectangles.add(new Rect(30, 50, 200, 1100));
            rectangles.add(new Rect(200, 50, 2200, 200));
            rectangles.add(new Rect(200, 950, 2200, 1100));
            rectangles.add(new Rect(2000, 50, 2200, 1100));;

            //Use Color.parseColor to define HTML colors
            paint.setColor(Color.parseColor("#CCCfff"));

             // optional method of just adding the rectangles directly from the coordinates

            //drawRect(float left, float top, float right, float bottom, Paint paint)

            /*canvas.drawRect(30, 50, 200, 1100, paint);
            canvas.drawRect(200, 50, 2200, 200, paint);
            canvas.drawRect(200, 950, 2200, 1100, paint);
            canvas.drawRect(2000, 50, 2200, 1100, paint); */



            for (Rect rect : rectangles) {
              canvas.drawRect(rect, paint);
            }
        }
    }

