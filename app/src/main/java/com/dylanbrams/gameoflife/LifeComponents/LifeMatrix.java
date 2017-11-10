package com.dylanbrams.gameoflife.LifeComponents;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by ThinkPad on 11/9/2017.
 */

public class LifeMatrix {

    protected int lmWidth;
    protected int lmHeight;
    protected LifeStatusEnum[][] lmMatrix;
    private int tick = 0;
    private int[] lfCount = new int[3];
    private static final String DEBUG_TAG = "Debug, FieldGameField";
    private Bitmap lmBitmap;

    // Create Matrix
    public LifeMatrix (int width, int height){
        lmWidth = width;
        lmHeight = height;
        lmBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        lmMatrix = new LifeStatusEnum[lmWidth][lmHeight];
    }

    // Get new fullscreen lifeBitmap
    public Bitmap GetNewFieldGraphic(){
        LifePrint lfPrinter = new LifePrint(lmMatrix);
        Bitmap lfNewGraphic = lfPrinter.BMPPrint();
        return lfNewGraphic;
    }

    // Simple Set
    protected boolean SetLifeMatrix (LifeStatusEnum[][] lmMatrixIn){
        try {
            lmMatrix = lmMatrixIn;
            return false;
        }
        catch (Exception ex) {
            throw (ex);
        }
    }

    // Simple get
    protected LifeStatusEnum[][] GetLifeMatrix (){
        return lmMatrix;
    }

    // Calculate Tick
    public boolean CalcNewTick(){
        tick++;
        Log.d(DEBUG_TAG, "calling this.CalcNewMatrix");
        CalcNewMatrix();
        return false;
    }

    public boolean CheckEqualMatrix(LifeStatusEnum[][] MatrixIn ){
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

    // This class holds a column of the matrix.
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
    // for applications to use much memory, and full-screen bitmaps (and matricies representing
    // them) use copious amounts. I can't minimize the size of the bitmap memory, but this reduces
    // the footprint of the matrix.
    private void CalcNewMatrix(){
        boolean[] FinishedLineTracker = new boolean[lmWidth];
        List<MatrixColumn> CompletedLines = new ArrayList<MatrixColumn>();
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

    private boolean CheckLineCalculated(boolean[] LineCalculated, int currentline){
        boolean beforeCalced = false, afterCalced = false;
        if (currentline == 0)
            beforeCalced = true;
        else if (LineCalculated[currentline-1] == true)
            beforeCalced = true;
        if (currentline == lmWidth-1)
            afterCalced = true;
        else if (LineCalculated[currentline+1] == true)
            afterCalced = true;
        if (beforeCalced == true && afterCalced == true)
            return true;
        return false;
    }

    protected LifeStatusEnum[] CalcLine(LifeStatusEnum[] LineBefore, LifeStatusEnum[] ThisLine,
                                      LifeStatusEnum[] LineAfter, int LineLength){
        LifeStatusEnum[] ReturnLine = new LifeStatusEnum[LineLength];
        // Note: the decision in this for loop could be broken into another function.
        // That said, this function seems already particle-ized enough to me.  I'm on the edge
        // about it.
        for (int j = 0; j < LineLength; j++) {
            short LifeCountIn = 0;
            List<LifeStatusEnum> StatusList = new ArrayList<LifeStatusEnum>();
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

    protected short CountLife (List<LifeStatusEnum> InputList){
        short returnTotal = 0;
        for (LifeStatusEnum LS : InputList) {
            if (LS == LifeStatusEnum.AlivePoint)
                returnTotal++;
        }
        return returnTotal;
    }

    // Check whether a cell is alive based upon its input life counts.
    protected LifeStatusEnum CalculateLife(short LifeCountIn, LifeStatusEnum currentLife){
        if ((LifeCountIn == 2 && currentLife == LifeStatusEnum.AlivePoint) || LifeCountIn == 3)
            return LifeStatusEnum.AlivePoint;
        else
            return LifeStatusEnum.DeadPoint;
    }

}
