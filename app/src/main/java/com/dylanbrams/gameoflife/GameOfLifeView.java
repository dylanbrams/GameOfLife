package com.dylanbrams.gameoflife;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.renderscript.RenderScript;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import com.dylanbrams.gameoflife.LifeComponents.LifeMatrixInterface;

/**
 * This view is the Android wrapper for the LifeComponents classes.
 *
 * It uses a LifeMatrixInterface
 * to process and graphically present a version of Conway's Game of Life.  No calculation is done
 * in this class; there is a timer tick that changes a local bitmap that becomes the background
 * asynchronously.
 *
 * Notes:
 * This view is extremely memory heavy, because it's in Java.  Full screen bitmap generation is
 * fairly slow on Android.
 * the interface must be created OUTSIDE the class, in the Activity running it.  This is due to
 * threading / Java issues.
 * There are also some portions of the view that are more complicated to edit while
 * inside this thread.
 * Extends SurfaceView, because it places a continually changing background behind the foreground.
 *
 * Functions:
 *  Constructor - Nothing special.  Starts the clock.
 *  Timer instance - Starts a thread that calls the interface's calcNewTick, then invalidates
 *      the current background
 *  OnDraw - Spawned when the current background is invalidated.  Replaces current background with
 *      a BitmapDrawable.  In the future, the background could be moved and zoomed.  I don't know
 *      how to do this.
 *  InitDrawMatrix - Draws the background after initiation of the program.
 *  StopCalculation - Stop all threads and calculation within them, including inside the matrix.
 *      This will interrupt ongoing calculations, so should not be used except upon destruction
 *      of the view.
 */

public class GameOfLifeView extends SurfaceView {
    private static final String DEBUG_TAG = "DebugTests, GOLView"; // Debug tag for logging
    private BitmapDrawable currentBackground = null; // the background of the View.
    volatile boolean background_updated = false; // Whether the background has been updated
        // NOTE THAT THIS ^^ is a performance check for the future.  If the ticks speed up Android
        // will not handle the calculations on my laptop (and therefore phone) so bitmaps shouldn't
        // be generated continuously.
    public LifeMatrixInterface thisLifeMatrixInterface;
    Timer timer = new Timer(false);
    Handler timerHandler = new Handler();
    private long startTimeMillis; // Initial system time when matrix starts calculating
    private static final int tick_length = 3000; // How long between calculations.  Playing with
                // this will cause GC issues.

    volatile boolean boolStop = false;
    // Create a timerTask.  It's a runnable that does one tick cycle calculation.
    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            timerHandler.post(new Runnable() {
                @Override
                public void run() {
                    Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
                    long runTimeCurrent = System.currentTimeMillis() - startTimeMillis;
                    Log.d(DEBUG_TAG, "Entering Calc New Tick.  Runtime: " +
                                     Long.toString(runTimeCurrent));
                    // This thread calculates a matrix graphic.
                    new Thread(new Runnable() {
                        public void run() {

                            if (!thisLifeMatrixInterface.getCalculationActive()) {
                                if (thisLifeMatrixInterface.getInitialized()) {
                                    thisLifeMatrixInterface.calcNewTick();
                                    Log.d(DEBUG_TAG,
                                            "calling thisLifeMatrixInterface.getNewMatrixGraphic");
                                    currentBackground = new BitmapDrawable(getResources(),
                                            thisLifeMatrixInterface.getNewMatrixGraphic());
                                    background_updated = false;
                                    postInvalidate(); // Put a redraw into the queue.
                                    Log.d(DEBUG_TAG, "Life bitmap ready for drawing. Calctime since thread start: " +
                                            Long.toString(System.currentTimeMillis() - startTimeMillis));
                                }
                            }
                            else
                                Log.d(DEBUG_TAG, "Calculation thread interrupted or overloaded.");
                        }
                    }).start();
                }
            });
        }
    };

    public void initializeLifeMatrixInterface(int widthIn, int heightIn, int randSeedIn){
        if (!thisLifeMatrixInterface.getInitialized()) {
            thisLifeMatrixInterface.SetupMatrix(widthIn, heightIn);
            thisLifeMatrixInterface.FillMatrixFromRandomSeed(randSeedIn);
        }
        else
            Log.d(DEBUG_TAG, "Attempt to re-initialize Life Matrix when it already existed.");
    }

    // Stop calculations being done by the processing threads.
    public void stopCalculation(){
        this.thisLifeMatrixInterface.setBoolStopWorking(true);
        timer.cancel();
        timer.purge();
    }
    // Simple constructor.
    public GameOfLifeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
        startTimeMillis = System.currentTimeMillis();
        timer.schedule(timerTask, 100, tick_length); // 1000 = 1 second.
    }

    // Android Generated.
    private void init(AttributeSet attrs, int defStyle) {
    }

    // Draws the canvas by updating the background.
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d(DEBUG_TAG, "Canvas onDraw");
        if (currentBackground != null) {
            setBackground(currentBackground);
            background_updated = true;
            // I'd like to implement this in the future.  It's more complicated than it looked.
            //TextView tickCount = findViewById(R.id.tvTickCount);
        }
        else if (!thisLifeMatrixInterface.getInitialized()){
            InitDrawMatrix();
        }
    }

    // Draws the matrix for the first time.
    private void InitDrawMatrix(){
        new Thread(new Runnable() {
            public void run() {
                Log.d(DEBUG_TAG, "Drawing Life Matrix for the first time.");
                currentBackground = new BitmapDrawable(getResources(),
                        thisLifeMatrixInterface.getNewMatrixGraphic());
            }
        }).start();
    }


}
