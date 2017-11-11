package com.dylanbrams.gameoflife;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import com.dylanbrams.gameoflife.LifeComponents.LifeMatrixInterface;

/**
 * This view is the Android wrapper for the LifeComponents classes.  It uses a LifeMatrixInterface
 * to create a
 */
public class GameOfLifeView extends SurfaceView {
    private static final String DEBUG_TAG = "DebugTests, GOLView";
    private BitmapDrawable currentBackground = null;
    volatile boolean background_updated = false;
    LifeMatrixInterface thisLifeMatrixInterface;
    Timer timer = new Timer(false);
    Handler timerHandler = new Handler();
    private long startTimeMillis;
    private static final int tick_length = 3000;

    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            timerHandler.post(new Runnable() {
                @Override
                public void run() {
                    long runTimeCurrent = System.currentTimeMillis() - startTimeMillis;
                    Log.d(DEBUG_TAG, "Entering Calc New Tick.  Runtime: " +
                                     Long.toString(runTimeCurrent));
                    new Thread(new Runnable() {
                        public void run() {
                            if (thisLifeMatrixInterface.GetInitialized()) {
                                thisLifeMatrixInterface.CalcNewTick();
                                Log.d(DEBUG_TAG, "calling thisGameField.GetNewMatrixGraphic");
                                currentBackground = new BitmapDrawable(getResources(),
                                        thisLifeMatrixInterface.GetNewMatrixGraphic());
                                background_updated = false;
                            }
                        }
                    }).start();
                    postInvalidate();
                    Log.d(DEBUG_TAG, "Game field drawn with bitmap on background.");
                }
            });
        }
    };


    public GameOfLifeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
        startTimeMillis = System.currentTimeMillis();
        timer.schedule(timerTask, 3000, tick_length); // 1000 = 1 second.
    }

    // Android Generated.
    private void init(AttributeSet attrs, int defStyle) {
    }

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
        else if (!thisLifeMatrixInterface.GetInitialized()){
            InitDrawMatrix();
        }
    }

    private void InitDrawMatrix(){
        new Thread(new Runnable() {
            public void run() {
                Log.d(DEBUG_TAG, "Drawing Life Matrix for the first time.");
                currentBackground = new BitmapDrawable(getResources(),
                        thisLifeMatrixInterface.GetNewMatrixGraphic());
            }
        }).start();
    }
}
