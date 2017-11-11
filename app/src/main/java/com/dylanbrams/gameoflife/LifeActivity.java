package com.dylanbrams.gameoflife;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.dylanbrams.gameoflife.LifeComponents.LifeMatrix;

/*
    Created by Dylan Brams, 11/11/2017
    This activity holds the GameOfLife View.  It has no interactivity.
    The only interesting thing it does is fish out the size of the window Android has created for
    the GameOfLife view contained inside of it, then set up the Game of Life View and LifeMatrix
    (limited by the interface) required by its child.
    It does this with a listener, because things which should not be interesting sometimes are in Java.
 */
public class LifeActivity extends AppCompatActivity {
    private GameOfLifeView localLifeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Integer randSeed = getIntent().getIntExtra("RANDOM_SEED", 0);
        setContentView(R.layout.activity_life);
        localLifeView = findViewById(R.id.localLifeView);
        // create a LifeMatrixInterface for the child View to use.  It's easier to do this here.
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
                    TextView tvRandSeed = findViewById(R.id.tvRandomSeed);
                    localLifeView.thisLifeMatrixInterface.SetupMatrix(width, height);
                    localLifeView.thisLifeMatrixInterface.FillMatrixFromRandomSeed(randSeed);
                    tvRandSeed.setText(((Integer)localLifeView
                            .thisLifeMatrixInterface.GetRandomSeed()).toString());
                }
                return true;
            }
        });
    }
}
