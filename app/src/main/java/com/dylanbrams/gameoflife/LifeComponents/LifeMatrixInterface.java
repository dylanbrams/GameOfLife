package com.dylanbrams.gameoflife.LifeComponents;

import android.graphics.Bitmap;

/*
  Created by Dylan Brams on 11/10/2017.
  This is an interface that defines the interactions allowed by LifeMatrix objects.
 */

public interface LifeMatrixInterface {
    void FillMatrixFromRandomSeed(int RandomSeed);

    void SetupMatrix(int width, int height);

    boolean getCalculationActive();
    void setBoolStopWorking(boolean stopWorkingIn);
    boolean getInitialized();
    int getTick();
    int getRandomSeed();
    // Get new fullscreen lifeBitmap
    Bitmap getNewMatrixGraphic();

    // Calculate Tick.  Call CalcNewMatrix().
    void calcNewTick();

    // Check equivalency of an input life matrix with the current life matrix.
    // Was not very necessary except for testing.
    boolean checkEqualMatrix(LifeStatusEnum[][] MatrixIn);
}
