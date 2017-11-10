package com.dylanbrams.gameoflife;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

import com.dylanbrams.gameoflife.LifeComponents.LifeMatrix;

/**
 * TODO: document your custom view class.
 */
public class GameOfLifeView extends View {
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
    LifeMatrix thisLifeMatrix = null;
    Timer timer = new Timer(false);
    Handler timerHandler = new Handler();
    private long startTimeMillis;
    private short LineColor;
    private static final int tick_length = 1000;
    private boolean lineDrawn = false;

    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            timerHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (playing == true) {

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

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.GameOfLifeView, defStyle, 0);

        mExampleString = a.getString(
                R.styleable.GameOfLifeView_exampleString);
        mExampleColor = a.getColor(
                R.styleable.GameOfLifeView_exampleColor,
                mExampleColor);
        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
        // values that should fall on pixel boundaries.
        mExampleDimension = a.getDimension(
                R.styleable.GameOfLifeView_exampleDimension,
                mExampleDimension);

        if (a.hasValue(R.styleable.GameOfLifeView_exampleDrawable)) {
            mExampleDrawable = a.getDrawable(
                    R.styleable.GameOfLifeView_exampleDrawable);
            mExampleDrawable.setCallback(this);
        }

        a.recycle();

        // Set up a default TextPaint object
        mTextPaint = new TextPaint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.LEFT);

        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements();
    }

    private void invalidateTextPaintAndMeasurements() {
        mTextPaint.setTextSize(mExampleDimension);
        mTextPaint.setColor(mExampleColor);
        mTextWidth = mTextPaint.measureText(mExampleString);

        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        mTextHeight = fontMetrics.bottom;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // TODO: consider storing these as member variables to reduce
        // allocations per draw cycle.
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;

        // Draw the text.
        canvas.drawText(mExampleString,
                paddingLeft + (contentWidth - mTextWidth) / 2,
                paddingTop + (contentHeight + mTextHeight) / 2,
                mTextPaint);

        // Draw the example drawable on top of the text.
        if (mExampleDrawable != null) {
            mExampleDrawable.setBounds(paddingLeft, paddingTop,
                    paddingLeft + contentWidth, paddingTop + contentHeight);
            mExampleDrawable.draw(canvas);
        }
    }

    /**
     * Gets the example string attribute value.
     *
     * @return The example string attribute value.
     */
    public String getExampleString() {
        return mExampleString;
    }

    /**
     * Sets the view's example string attribute value. In the example view, this string
     * is the text to draw.
     *
     * @param exampleString The example string attribute value to use.
     */
    public void setExampleString(String exampleString) {
        mExampleString = exampleString;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example color attribute value.
     *
     * @return The example color attribute value.
     */
    public int getExampleColor() {
        return mExampleColor;
    }

    /**
     * Sets the view's example color attribute value. In the example view, this color
     * is the font color.
     *
     * @param exampleColor The example color attribute value to use.
     */
    public void setExampleColor(int exampleColor) {
        mExampleColor = exampleColor;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example dimension attribute value.
     *
     * @return The example dimension attribute value.
     */
    public float getExampleDimension() {
        return mExampleDimension;
    }

    /**
     * Sets the view's example dimension attribute value. In the example view, this dimension
     * is the font size.
     *
     * @param exampleDimension The example dimension attribute value to use.
     */
    public void setExampleDimension(float exampleDimension) {
        mExampleDimension = exampleDimension;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example drawable attribute value.
     *
     * @return The example drawable attribute value.
     */
    public Drawable getExampleDrawable() {
        return mExampleDrawable;
    }

    /**
     * Sets the view's example drawable attribute value. In the example view, this drawable is
     * drawn above the text.
     *
     * @param exampleDrawable The example drawable attribute value to use.
     */
    public void setExampleDrawable(Drawable exampleDrawable) {
        mExampleDrawable = exampleDrawable;
    }
}
