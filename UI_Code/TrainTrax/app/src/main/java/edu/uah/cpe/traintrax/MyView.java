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
        edu.uah.cpe.traintrax.TrackDiagram TrackDig = new edu.uah.cpe.traintrax.TrackDiagram();

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
                //draw error diagram
                // <ImageView
                // android:layout_width="fill_parent"
                // android:layout_height="wrap_content"
                //android:src="@drawable/image_name" />

                //ImageView iv = (ImageView)findViewById(v);
                //iv.setImageResource(R.drawable.trackgeo);

                //Drawable d = getResources().getDrawable(R.drawable.trackgeo);
                //d.setBounds(left, top, right, bottom);
                //d.draw(canvas);
                //canvas.drawPicture(R.drawable.trackgeo);



            Bitmap error_screen = BitmapFactory.decodeResource(getResources(),
                    R.drawable.trackgeo);


           /* final int maxSize = 400;
            int outWidth;
            int outHeight;
            int inWidth = error_screen.getWidth();
            int inHeight = error_screen.getHeight();
            if (inWidth > inHeight) {
                outWidth = maxSize;
                outHeight = (inHeight * maxSize) / inWidth;
            } else {
                outHeight = maxSize;
                outWidth = (inWidth * maxSize) / inHeight;

                Bitmap resizedBitmap = Bitmap.createScaledBitmap(error_screen, outWidth, outHeight, false); */

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

            //startX = 20;
            //startY = 100;
            //stopX = 140;
            //stopY = 30;

            paint.setStrokeWidth(2000);
            int startx = 50;
            int starty = 90;
            int endx = 150;
            int endy = 360;
           // canvas.drawLine(startx, starty, endx, endy, paint);
            canvas.drawLine(44, 27, 41, 22, paint);
            canvas.drawLine(36, 18, 41, 22, paint);
           /* canvas.drawLine(41, 22, 36, 18, paint);
            canvas.drawLine(36, 18, 31, 16, paint);
            canvas.drawLine(31, 16, 25, 16, paint);
            canvas.drawLine(25, 16, 20, 19, paint);
            canvas.drawLine(20, 19, 16, 23, paint);
            canvas.drawLine(16, 23, 14, 28, paint);
            canvas.drawLine(14, 28, 14, 34, paint);
            canvas.drawLine(14, 34, 17, 40, paint);
            canvas.drawLine(17, 40, 20, 44, paint);
            canvas.drawLine(20, 44, 23, 49, paint);
            canvas.drawLine(23, 49, 28, 50, paint);
            canvas.drawLine(28, 50, 33, 46, paint);
            canvas.drawLine(33, 46, 38, 43, paint);
            canvas.drawLine(38, 43, 42, 39, paint);
            canvas.drawLine(42, 39, 44, 33, paint);
            canvas.drawLine(44, 33, 44, 27, paint); */




          //  for (Rect rect : rectangles) {
            //  canvas.drawRect(rect, paint);
           // }
        }
    }

