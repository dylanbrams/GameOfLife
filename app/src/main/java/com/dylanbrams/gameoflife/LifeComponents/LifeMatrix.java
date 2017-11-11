package com.dylanbrams.gameoflife.LifeComponents;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Created by Dylan Brams on 11/9/2017.
 * This class is an implementation of Conway's Game of Life.
 * Wikipedia page here: https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life
 * Essentially, this chunk of code operates a matrix that follows the following rules:
 * If a cell is alive and has 2 neighbors, it will be alive next tick.
 * If a cell has 3 neighbors, it will be alive next tick.
 * Otherwise, the cell is dead next tick.
 *
 * I have set up an enum, "LifeStatusEnum," instead of using raw values.  This was too much
 * complexity (which compounded) unless future versions need to include multiple levels of life.
 *
 * Note that some functions are 'protected' instead of 'private' so that Android Studio can test them
 * with unit testing.
 */


public class LifeMatrix implements LifeMatrixInterface {

    private int lmWidth;
    private int lmHeight;
    private boolean lmInitialized = false; // Whether or not the matrix is ready to tick.
    private LifeStatusEnum[][] lmMatrix;
    private int tick = 0;
    private int randomSeed = 0; // Random number seed for the matrix.
    private static final String DEBUG_TAG = "Debug, FieldGameField";

    public LifeMatrix (int width, int height){
        SetupMatrix(width, height);
    }

    public LifeMatrix (){
    }

    // Initialize the matrix to a particular size, set up that size in local vars.
    public void SetupMatrix(int width, int height){
        lmWidth = width;
        lmHeight = height;
        lmMatrix = new LifeStatusEnum[lmWidth][lmHeight];
    }

    // Various Get functions to maintain encapsulation for private variables.
    public boolean GetInitialized(){
        return lmInitialized;
    }
    public int GetTick() {return tick;}
    public int GetRandomSeed() {return randomSeed;}

    @Override
    // Fill the core matrix based on a random seed or no seed.  I chose a 1/3 average fill ratio.
    // Set isInitialized, because the matrix is now ready to tick.
    public void FillMatrixFromRandomSeed(int randomSeedIn) {
        Random generator;
        Integer mySeed = randomSeedIn;
        if (mySeed != 0)
            generator = new Random(mySeed);
        else {
            Random gimmieGen = new Random();
            mySeed = gimmieGen.nextInt();
            generator = new Random(mySeed);
        }
        for (int i = 0; i < lmWidth; i++){
            for (int j = 0; j < lmHeight; j++){
                int randnum = generator.nextInt();
                if (randnum % 3 == 0)
                    lmMatrix[i][j] = LifeStatusEnum.AlivePoint;
                else
                    lmMatrix[i][j] = LifeStatusEnum.DeadPoint;
            }
        }
        randomSeed = mySeed;
        lmInitialized = true;
    }

    // Get new fullscreen lifeBitmap, return it.
    @Override
    public Bitmap GetNewMatrixGraphic(){
        LifePrint lfPrinter = new LifePrint(lmMatrix);
        return lfPrinter.BMPPrint();
    }

    // Calculate Tick.  Call CalcNewMatrix().
    @Override
    public void CalcNewTick(){
        try {
            Log.d(DEBUG_TAG, "calling this.CalcNewMatrix");
            CalcNewMatrix();
            tick++;
        }
        catch (Exception ex){
            throw (ex);
        }
    }

    // Check equivalency of an input life matrix with the current life matrix.
    // Was not very necessary except for testing.
    @Override
    public boolean CheckEqualMatrix(LifeStatusEnum[][] MatrixIn){
        // Step one was always to check matrix sizes for equivalency.
        if (MatrixIn.length == lmMatrix.length && MatrixIn[0].length == lmMatrix[0].length)
        {
            for (int i = 0; i<lmWidth; i++)
            {
                if (MatrixIn[i].length == lmMatrix[i].length){
                    for (int j=0; j<lmHeight; j++) {
                        if (MatrixIn[i][j] != lmMatrix[i][j]){
                            return false;
                        }
                    }
                }
                else return false;
            }
        }
        else
            return false;
        return true;
    }

    // Simple set for primary matrix
    protected void SetLifeMatrix (LifeStatusEnum[][] lmMatrixIn){
        try {
            lmMatrix = lmMatrixIn;
        }
        catch (Exception ex) {
            throw (ex);
        }
    }

    // Simple get for the primary matrix
    protected LifeStatusEnum[][] GetLifeMatrix(){
        return lmMatrix;
    }

    // This class holds a column of the matrix.
    // Necessary for reasons to be explained later.
    private class MatrixColumn{
        public LifeStatusEnum[] myLine;
        public int myIndex;
        MatrixColumn(int IndexIn, LifeStatusEnum[] LineIn){
            myIndex = IndexIn;
            myLine = LineIn;
        }
    }

    // Calc the Matrix column-by-column and progressively add each column back into
    // the original matrix after calculation.  This is done for memory-compression purposes;
    // Android doesn't allow
    // for applications that use much memory, and full-screen bitmaps (and matricies representing
    // them) use copious amounts. I can't minimize the size of the bitmap memory, but this reduces
    // the footprint of the matrix when calculating it anew.
    private void CalcNewMatrix(){
        boolean[] FinishedLineTracker = new boolean[lmWidth];
        List<MatrixColumn> CompletedLines = new ArrayList<>();
        Log.d(DEBUG_TAG, "In CalcNewMatrix; beginning matrix calculation.");

        for (int i = 0; i < lmWidth; i++) {

            // Calc one line of the final matrix.
            if (i == 0) {
                CompletedLines.add(new MatrixColumn(i,
                        CalcLine(null, lmMatrix[i], lmMatrix[i + 1], lmHeight)));
            }
            else if (i == lmWidth-1)
                CompletedLines.add(new MatrixColumn(i,
                        CalcLine(lmMatrix[i-1], lmMatrix[i], null, lmHeight)));
            else
                CompletedLines.add(new MatrixColumn(i,
                        CalcLine(lmMatrix[i-1], lmMatrix[i], lmMatrix[i+1], lmHeight)));
            FinishedLineTracker[i] = true;
            // Iterate through the completed lines not yet in the matrix, put them in if the lines
            // before and after have been finished.
            for (Iterator<MatrixColumn> iterator = CompletedLines.iterator(); iterator.hasNext();) {
                MatrixColumn currentCol = iterator.next();
                if (CheckLineCalculated(FinishedLineTracker, currentCol.myIndex)) {
                    // Remove the current element from the iterator and the list.
                    lmMatrix[currentCol.myIndex] = currentCol.myLine;
                    iterator.remove();
                }
            }
        }
        Log.d(DEBUG_TAG, "Matrix calculations finished.");
    }

    // Return true if the lines that will need to refer to this line have been calculated.
    private boolean CheckLineCalculated(boolean[] LineCalculated, int currentline){
        boolean beforeCalced = false, afterCalced = false;
        if (currentline == 0)
            beforeCalced = true;
        else if (LineCalculated[currentline-1])
            beforeCalced = true;
        if (currentline == lmWidth-1)
            afterCalced = true;
        else if (LineCalculated[currentline+1])
            afterCalced = true;
        if (beforeCalced && afterCalced)
            return true;
        return false;
    }

    // Calculate status of each cell in a line.
    // This is done by assembly of a list of cells to count, and passing that list to CountLife
    protected LifeStatusEnum[] CalcLine(LifeStatusEnum[] LineBefore, LifeStatusEnum[] ThisLine,
                                      LifeStatusEnum[] LineAfter, int LineLength){
        LifeStatusEnum[] ReturnLine = new LifeStatusEnum[LineLength];
        // Note: the decision in this for loop could be broken into another function.
        // That said, this function seems already particle-ized enough to me.  I'm on the edge
        // about it.
        for (int j = 0; j < LineLength; j++) {
            short LifeCountIn = 0;
            List<LifeStatusEnum> StatusList = new ArrayList<>();
            if (LineBefore != null && j > 0)
                    StatusList.add(LineBefore[j-1]);
            if (LineBefore != null)
                StatusList.add(LineBefore[j]);
            if (LineBefore != null && j < LineLength - 1)
                StatusList.add(LineBefore[j+1]);
            if (j>0)
                StatusList.add(ThisLine[j-1]);
            if (j < LineLength - 1)
                StatusList.add(ThisLine[j+1]);
            if (LineAfter != null && j > 0)
                StatusList.add(LineAfter[j-1]);
            if (LineAfter != null)
                StatusList.add(LineAfter[j]);
            if (LineAfter != null && j < LineLength - 1)
                StatusList.add(LineAfter[j+1]);
            LifeCountIn = CountLife(StatusList);
            ReturnLine[j] = CalculateLife(LifeCountIn, ThisLine[j]);
        }
        return ReturnLine;
    }

    // Count the number of cells that are alive in a list.
    protected short CountLife (List<LifeStatusEnum> InputList){
        short returnTotal = 0;
        for (LifeStatusEnum LS : InputList) {
            if (LS == LifeStatusEnum.AlivePoint)
                returnTotal++;
        }
        return returnTotal;
    }

    // Check whether a cell is alive based upon the count of life around it, and current life.
    protected LifeStatusEnum CalculateLife(short LifeCountIn, LifeStatusEnum currentLife){
        if ((LifeCountIn == 2 && currentLife == LifeStatusEnum.AlivePoint) || LifeCountIn == 3)
            return LifeStatusEnum.AlivePoint;
        else
            return LifeStatusEnum.DeadPoint;
    }

}
