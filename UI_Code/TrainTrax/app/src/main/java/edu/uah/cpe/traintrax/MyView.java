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

import java.util.ArrayList;


    public class MyView extends View {
        //set for 12 temp coordinates
        edu.uah.cpe.traintrax.TrackDiagram TrackDig = new edu.uah.cpe.traintrax.TrackDiagram(32);

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

            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.WHITE);

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

            //here need to get the ArrayLists from the Track Diagram model class
            // will use those to either add rectangles to the list or draw directly on the screen
            // from the array lists (both ways are done below)
            TrackDig.getCoordinates();

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
            int num_coords = TrackDig.getnumCoords();

            for (int i = 0; i < num_coords; i++ )
            {
                /* Get the X position and why position of the line */
                Float xcord = (Float) TrackDig.getXPosition(i);
                Float ycord = (Float) TrackDig.getYPosition(i);

                //x = (x/xmax) * screen resolution
                /* scale coordinates to resolution size */
                xcord = (xcord/xmax) * xpixel;
                ycord = (ycord/ymax) * ypixel;

            //set the initial path to the first point
            if (i == 0)
                path.moveTo(xcord, ycord);

            if (i > 0)
                path.lineTo(xcord, ycord);

            }

            canvas.drawPath(path, paint);


            /**************************temp hard codes for track diagram
            //x max = 80, y max = 90
            //nexus 10 = 2500 X 1600   21 inches by 14
             p.moveTo(960, 280);// 31, 16
             p.lineTo(780, 280); // 25, 16
             p.lineTo(625, 330); // 20, 19
             p.lineTo(500, 400); // 16, 23
             p.lineTo(430, 490); // 14, 28
             p.lineTo(430, 600); // 14, 34
             p.lineTo(530, 710); // 17, 40


            p.lineTo(625, 780); // 20, 44
            p.lineTo(710, 870); // 23, 49
            p.lineTo(870, 880); // 28, 50
            p.lineTo(1030, 810); // 33, 46
            p.lineTo(1180, 760); // 38, 43 */

          //  for (Rect rect : rectangles) {
            //  canvas.drawRect(rect, paint);
           // }


        }
    }

