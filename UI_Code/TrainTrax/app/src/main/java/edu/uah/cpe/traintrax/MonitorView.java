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
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.WindowManager;

import com.traintrax.navigation.service.TrainNavigationServiceInterface;
import com.traintrax.navigation.trackswitch.SwitchState;

import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Handler;

/**
 * Class controls the train monitor display of the TrainMonitorView.
 */
public class MonitorView extends View {

    //get default values for TrackDiagram
    int xmax;
    int ymax;
    int xpixel;
    int ypixel;
    int num_shapes;
    int num_switches;
    int num_trains;


    public MonitorView(Context context) {
        super(context);
    }

    public MonitorView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MonitorView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    //Object responsible for changing the state of switches in a background thread
    /*private ChangeSwitchStateTask changeSwitchStateTask = new ChangeSwitchStateTask() {
        @Override
        protected void onPostExecute(ChangeSwitchStateTaskResult changeSwitchStateTaskResult) {
            super.onPostExecute(changeSwitchStateTaskResult);

            if(changeSwitchStateTaskResult.isSwitchStateChanged()) {
                //Update the switch state if successful

                TrackSwitchInfo Switch = SharedObjectSingleton.getInstance().getTrackSwitchInfo();

                Switch.setSwitchState(changeSwitchStateTaskResult.getSwitchNumber(), changeSwitchStateTaskResult.getAssignedSwitchState());

                //Request a redraw now that we know that the switch has successfully changed
                invalidate();
            }
            else{
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage(changeSwitchStateTaskResult.getFailureMessage());
                builder.show();
            }
        }
    }; */

    private ChangeSwitchStateTask CreateSwitchStateTask(){
        ChangeSwitchStateTask changeSwitchStateTask = new ChangeSwitchStateTask() {
            @Override
            protected void onPostExecute(ChangeSwitchStateTaskResult changeSwitchStateTaskResult) {
                super.onPostExecute(changeSwitchStateTaskResult);

                if(changeSwitchStateTaskResult.isSwitchStateChanged()) {
                    //Update the switch state if successful

                    TrackSwitchInfo Switch = SharedObjectSingleton.getInstance().getTrackSwitchInfo();

                    Switch.setSwitchState(changeSwitchStateTaskResult.getSwitchNumber(), changeSwitchStateTaskResult.getAssignedSwitchState());

                    //Request a redraw now that we know that the switch has successfully changed
                    invalidate();
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage(changeSwitchStateTaskResult.getFailureMessage());
                    builder.show();
                }
            }
        };

        return changeSwitchStateTask;
    }

    private final Semaphore binarySwitchSemaphore = new Semaphore(1, true);


    @Override
    protected void onDraw(Canvas canvas) {

        //The value of the singleton changes once the task of reading track geometry data from the
        //network has been completed.
        //If you do not read the current value of the singleton, then it will have the initial value
        //(null) forever, even after the track data has been loaded.
        TrackDiagram TrackDig = SharedObjectSingleton.getInstance().getTrackDiagram();
        TrackSwitchInfo Switch = SharedObjectSingleton.getInstance().getTrackSwitchInfo();
        TrainPosInfo TrainPos = SharedObjectSingleton.getInstance().getTrainPosInfo();

        if (TrackDig == null)
            return;

        //get default values for TrackDiagram
        xmax = TrackDig.getXmax();
        ymax = TrackDig.getYmax();
        xpixel = TrackDig.getXpixel();
        ypixel = TrackDig.getYpixel();
        num_shapes = TrackDig.getNumShapes();
        num_switches = Switch.getNum_switches();
        List<TrainInfo> trainPositions = TrainPos.getTrains();
        num_trains = trainPositions.size();

        DisplayMetrics metrics = new DisplayMetrics();

        ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay().getMetrics(metrics);

        //From this, we can get the information required to size the display:

        int widthPixels = metrics.widthPixels;
        int heightPixels = metrics.heightPixels;

        xpixel = widthPixels - 125;
        ypixel = heightPixels - 250;

        int dontmoveflag = 0;

        Paint paint = new Paint();

        super.onDraw(canvas);

        canvas.drawPaint(paint);

        //Use Color.parseColor to define HTML colors
        paint.setColor(Color.parseColor("#CCCfff"));
        paint.setColor(Color.GREEN);

        //set paint style
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(SharedObjectSingleton.getInstance().getTrackPixelStrokeWidth());

        //Creates a path based on a collection of lines.
        Path testPath = new Path();

        List<TrackDiagram.ShapeCoordinate> tmp;
        tmp = TrackDig.coordinates;

        List<TrackDiagram.ShapeCoordinate> inactive_Shapes = new ArrayList<TrackDiagram.ShapeCoordinate>();

        for (int i = 0; i < num_shapes; i++) {
            dontmoveflag = 0;
            //testPath.reset();
            TrackDiagram.ShapeCoordinate tempShape_cord;
            tempShape_cord = tmp.get(i);

            int num_points = tempShape_cord.getnumCoords();

            //NOTE: This also could be done with edges of the polygons
            //To simply draw each line that the polygon has.
            for (int j = 0; j < num_points; j++) {
                float xcord = tempShape_cord.getXPosition(j);
                float ycord = tempShape_cord.getYPosition(j);
                String trackBlockName = tempShape_cord.getTrackBlockName(j);
                num_switches = Switch.getNum_switches();

                for (int z = 0; z < num_switches; z++) {

                    Boolean State = Switch.getPassState(z);
                    String bypassName = Switch.getBypassBlockName(z);
                    String passName = Switch.getPassBlockName(z);
                    String switchName = Switch.getswitchBlockName(z);


                    if ((trackBlockName.equals(passName) && State == false) ||
                            (trackBlockName.equals(bypassName) && State == true)) {
                        inactive_Shapes.add(tempShape_cord);
                        dontmoveflag = 1;
                        break;
                    }
                }

                xcord = (xcord / xmax) * xpixel;
                ycord = (ycord / ymax) * ypixel;
                //correct for y orientation of the track coordinate system
                ycord = (ypixel) - ycord;


                if (dontmoveflag == 0) {
                    if (j == 0) {
                        // testPath.reset();
                        testPath.moveTo(xcord, ycord);
                    } else {
                        testPath.lineTo(xcord, ycord);
                    }
                }
                //this is the last point in this list check to see if this
                // shape needs to be drawn a different color
                if (j + 1 == num_points) {
                    if (dontmoveflag == 1)
                        //add this shape to draw it a different color
                        inactive_Shapes.add(tempShape_cord);
                }

            }

            canvas.drawPath(testPath, paint);

        }

        paint.setColor(Color.RED);
        //Creates a path based on a collection of lines.
        Path inactivePath = new Path();

        inactivePath.reset();
        int num = inactive_Shapes.size();


        for (int i = 0; i < num; i++) {

            TrackDiagram.ShapeCoordinate tmpShape = inactive_Shapes.get(i);

            int size = tmpShape.getnumCoords();
            for (int j = 0; j < size; j++) {
                //Boolean moveFlag = moveToFlag.get(j);
                Float xcord = tmpShape.getXPosition(j);
                Float ycord = tmpShape.getYPosition(j);

                xcord = (xcord / xmax) * xpixel;
                ycord = (ycord / ymax) * ypixel;
                //correct for y orientation of the track coordinate system
                ycord = (ypixel) - ycord;

                if (j == 0) {
                    inactivePath.moveTo(xcord, ycord);
                } else {
                    inactivePath.lineTo(xcord, ycord);
                }
            }
        }

        canvas.drawPath(inactivePath, paint);

        num_switches = Switch.getNum_switches();

        for (int i = 0; i < num_switches; i++) {
            float xcord = Switch.getXPosition(i);
            float ycord = Switch.getYPosition(i);

            //x = (x/xmax) * screen resolution;
            // scale coordinates to resolution size
            xcord = (xcord / xmax) * xpixel;
            ycord = (ycord / ymax) * ypixel;

            //adjust orientation
            ycord = ypixel - ycord;

            Bitmap switchstate;

            Boolean state = Switch.getPassState(i);

            if (state == true) {
                switchstate = BitmapFactory.decodeResource(getResources(),
                        R.drawable.pass);
            } else {
                switchstate = BitmapFactory.decodeResource(getResources(),
                        R.drawable.bypass);
            }

            Bitmap resizedBitmap = Bitmap.createScaledBitmap(switchstate, 30, 30, false);
            canvas.drawBitmap(resizedBitmap, xcord, ycord, paint);
        }


        //draw train stuff
        for (int i = 0; i < num_trains; i++) {
            TrainInfo trainInfo = trainPositions.get(i);

            float xcord = trainInfo.getXposition();
            float ycord = trainInfo.getYposition();
            Double xvelocity = trainInfo.getXvelocity().doubleValue();
            Double yvelocity = trainInfo.getYvelocity().doubleValue();
            String trainID = trainInfo.getTrainID();

            //x = (x/xmax) * screen resolution;
            // scale coordinates to resolution size
            xcord = (xcord / xmax) * xpixel;
            ycord = (ycord / ymax) * ypixel;

            //adjust orientation
            ycord = ypixel - ycord;


            Bitmap train = BitmapFactory.decodeResource(getResources(),
                    R.drawable.train2);

            Bitmap resizedBitmap = Bitmap.createScaledBitmap(train, 30, 30, false);
            canvas.drawBitmap(resizedBitmap, xcord, ycord, paint);

            paint.setTextSize(20);

            //calculate speed and orientation
            Double Speed = (Math.sqrt(xvelocity*xvelocity + yvelocity*yvelocity));
            Double orientation = (Speed == 0) ? 0 : Math.asin((xvelocity/(Math.sqrt(xvelocity*xvelocity + yvelocity*yvelocity))));

            paint.setColor(Color.WHITE);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawText("Speed: "+ Speed, xcord, ycord + 50, paint);
            canvas.drawText("Orientation: " + orientation, xcord, ycord + 75, paint);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //The value of the singleton changes once the task of reading track geometry data from the
        //network has been completed.
        //If you do not read the current value of the singleton, then it will have the initial value
        //(null) forever, even after the track data has been loaded.
        TrackSwitchInfo Switch = SharedObjectSingleton.getInstance().getTrackSwitchInfo();

        if(Switch == null){
            //Immediately return if you do not have switch info.
            return false;
        }

        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:

                for (int i = 0; i < num_switches; i++) {

                    float posx = Switch.getXPosition(i) / xmax * xpixel;
                    float posy = Switch.getYPosition(i) / ymax * ypixel;

                    int xcord = ((int) posx);
                    int ycord = ((int) (ypixel - posy));

                    //subtract difference between the touched position and stored switch coordinate
                    int xthreshold = Math.abs(xcord - x);
                    int ythreshold = Math.abs(ycord - y);

                    //if it's within 30 pixels then process the click (may adjust this)
                    if (xthreshold < 30 && ythreshold < 30) {
                        Boolean isInPassState = Switch.getPassState(i);
                        final String switchName = Switch.getswitchName(i);

                        //toogle pass state
                        isInPassState = !isInPassState;
                        final SwitchState switchState = (isInPassState) ? SwitchState.Pass : SwitchState.ByPass;

                        /*Runnable controlSwitchRunnable = new Runnable() {
                            @Override
                            public void run() {

                                TrainNavigationServiceInterface trainNavigationServiceInterface = SharedObjectSingleton.getInstance().getTrainNavigationServiceInterface();

                                if(trainNavigationServiceInterface != null){
                                    try {

                                        TrackSwitchInfo Switch = SharedObjectSingleton.getInstance().getTrackSwitchInfo();

                                        Switch.setSwitchState(switchName, switchState);

                                        trainNavigationServiceInterface.SetSwitchState(switchName, switchState);
                                        invalidate();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        };

                        try {
                            controlSwitchRunnable.run();
                        }
                        catch (Exception exception){

                        } */

                        try {
                            binarySwitchSemaphore.acquire();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        try {
                            //Create a new task to change the switch
                            ChangeSwitchStateTask changeSwitchStateTask = CreateSwitchStateTask();

                            changeSwitchStateTask.execute(switchName, switchState.toString());
                        }
                        catch(Exception exception){
                            //Suppresses any error from attempting to control the switch
                           /* AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                            String errorMessage = exception.getMessage();
                            builder.setMessage("Unable to set Switch: " + errorMessage); */
                        }
                        binarySwitchSemaphore.release();

                        break;
                    }


                    // check and see if click event occurred in switch area
                }

        }
        return false;
    }
}
