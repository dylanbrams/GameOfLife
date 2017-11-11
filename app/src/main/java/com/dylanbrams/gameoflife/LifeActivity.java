package com.dylanbrams.gameoflife;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewTreeObserver;

import com.dylanbrams.gameoflife.LifeComponents.LifeMatrix;

public class LifeActivity extends AppCompatActivity {
    private static final int tick_length = 1500;
    private GameOfLifeView localLifeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_life);
        localLifeView = findViewById(R.id.localLifeView);
        localLifeView.thisLifeMatrixInterface = new LifeMatrix();
        // After the size is determined, before the drawing happens, get the size and plug it into
        // the Life Matrix.
        final ViewTreeObserver obs = localLifeView.getViewTreeObserver();
        obs.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw () {
                //Log.d("Debug", "onPreDraw tv height is " + tv.getHeight()); // bad for performance, remove on production
                int height = localLifeView.getHeight();
                int width = localLifeView.getWidth();
                if (!localLifeView.thisLifeMatrixInterface.GetInitialized()) {
                    localLifeView.thisLifeMatrixInterface.SetupMatrix(width, height);

                }
                return true;
            }
        });
    }
}
