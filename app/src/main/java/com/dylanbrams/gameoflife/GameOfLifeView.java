package com.dylanbrams.gameoflife;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

import com.dylanbrams.gameoflife.LifeComponents.LifeMatrixInterface;
import com.dylanbrams.gameoflife.LifeComponents.LifeMatrix;

/**
 * TODO: document your custom view class.
 */
public class GameOfLifeView extends SurfaceView {
    private String mExampleString; // TODO: use a default from R.string...
    private int mExampleColor = Color.RED; // TODO: use a default from R.color...
    private float mExampleDimension = 0; // TODO: use a default from R.dimen...
    private Drawable mExampleDrawable;

    private TextPaint mTextPaint;
    private float mTextWidth;
    private float mTextHeight;
    private Context myContext;
    private static final String DEBUG_TAG = "DebugTests, FieldView";
    private int lmWidth;
    private int lmHeight;
    private Bitmap currentBackground = null;
    volatile boolean playing = true;
    volatile boolean background_updated = false;
    Thread gameThread = null;
    LifeMatrixInterface thisLifeMatrix = null;
    Timer timer = new Timer(false);
    Handler timerHandler = new Handler();
    private long startTimeMillis;
    private short LineColor;
    private static final int tick_length = 1500;
    private boolean lineDrawn = false;

    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            timerHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (playing) {

                        long runTimeCurrent = System.currentTimeMillis() - startTimeMillis;
                        Log.d(DEBUG_TAG, "Entering Calc New Tick.  Runtime: " +
                                         Long.toString(runTimeCurrent));
                        new Thread(new Runnable() {
                            public void run() {
                                thisLifeMatrix.CalcNewTick();
                                Log.d(DEBUG_TAG, "calling thisGameField.GetNewMatrixGraphic");
                                currentBackground = thisLifeMatrix.GetNewMatrixGraphic();
                                background_updated = false;
                            }
                        }).start();
                        //BitmapDrawable d = new BitmapDrawable(getResources(), currentBackground);
                        //setBackground(d);
                        invalidate();
                        Log.d(DEBUG_TAG, "Game field drawn with bitmap on background.");
                    }
                }
            });
        }
    };

    public GameOfLifeView(Context context) {
        super(context);
        init(null, 0);
        startTimeMillis = System.currentTimeMillis();
        timer.schedule(timerTask, 3000, tick_length); // 1000 = 1 second.
    }

    public GameOfLifeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public GameOfLifeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }


    // Android Generated.
    private void init(AttributeSet attrs, int defStyle) {
      // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.GameOfLifeView, defStyle, 0);

        a.recycle();
        int myHeight;

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d(DEBUG_TAG, "Canvas onDraw");
        // TODO: consider storing these as member variables to reduce
        // allocations per draw cycle.
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();
        if (currentBackground != null) {
            setBackground(new BitmapDrawable(getResources(), currentBackground));
            background_updated = true;
        }

        final int contentWidth = getWidth() - paddingLeft - paddingRight;
        final int contentHeight = getHeight() - paddingTop - paddingBottom;
        // First-run initialization and drawing of the matrix.  Yes, this should be elsewhere.
        if (thisLifeMatrix == null)
            InitAndDrawMatrix(contentWidth, contentHeight);


    }

    private void InitAndDrawMatrix(final int contentWidth, final int contentHeight){
        new Thread(new Runnable() {
            public void run() {
                thisLifeMatrix = new LifeMatrix(contentWidth, contentHeight);
                Log.d(DEBUG_TAG, "Initializing Life Matrix");
                thisLifeMatrix.FillMatrixFromRandomSeed(0);
                currentBackground = thisLifeMatrix.GetNewMatrixGraphic();
                setBackground(new BitmapDrawable(getResources(), currentBackground));
            }
        }).start();
    }
}
