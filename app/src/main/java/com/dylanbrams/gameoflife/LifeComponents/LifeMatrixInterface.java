package com.dylanbrams.gameoflife.LifeComponents;

import android.graphics.Bitmap;

/*
  Created by Dylan Brams on 11/10/2017.
  This is an interface that defines the interactions allowed by LifeMatrix objects.
 */

public interface LifeMatrixInterface {
    void FillMatrixFromRandomSeed(int RandomSeed);

    void SetupMatrix(int width, int height);

    boolean GetInitialized();
    int GetTick();
    int GetRandomSeed();
    // Get new fullscreen lifeBitmap
    Bitmap GetNewMatrixGraphic();

    // Calculate Tick.  Call CalcNewMatrix().
    void CalcNewTick();

    // Check equivalency of an input life matrix with the current life matrix.
    // Was not very necessary except for testing.
    boolean CheckEqualMatrix(LifeStatusEnum[][] MatrixIn);
}
