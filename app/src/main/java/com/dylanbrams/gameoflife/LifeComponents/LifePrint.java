package com.dylanbrams.gameoflife.LifeComponents;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.Timer;

/**
 * Created by ThinkPad on 11/9/2017.
 */

public class LifePrint {
    // Prints a Field matrix to a bitmap.
    private LifeStatusEnum[][] MatrixToPrint;
    private Bitmap BMPOutput;
    private int imageWidth = 0;
    private int imageHeight = 0;
    private static final String DEBUG_TAG = "DebugTests, FieldPrint";
    private static final String EX_TAG = "Exceptions, FieldPrint";

    LifePrint(LifeStatusEnum[][] MatrixIn) {
        // Constructor.  Takes a Matrix to convert into a Bitmap.
        imageWidth = MatrixIn.length;
        imageHeight = MatrixIn[0].length;
        MatrixToPrint = MatrixIn;
        BMPOutput = null;
    }

    // Converts the
    Bitmap BMPPrint() {
        int k = 0;
        int BMPArray[] = new int[imageWidth * imageHeight + 1];
        Timer myTimer = new Timer();
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < imageHeight; i++) {
            for (int j = 0; j < imageWidth; j++) {
                // False is not alive.
                if (MatrixToPrint[j][i] == LifeStatusEnum.AlivePoint )
                    BMPArray[k] = 0x000000;
                else
                    BMPArray[k] = 0xFFFFFF;
                k++;
            }

        }
        long millis = System.currentTimeMillis() - startTime;
        Log.d(DEBUG_TAG, "Put all lines into BMP array. Time taken, MS: " + Long.toString(millis));
        BMPOutput = CallBMPGenerate(BMPArray, imageWidth, imageHeight);
        millis = System.currentTimeMillis() - startTime;
        Log.d(DEBUG_TAG, "Created bitmap. Total BMPPrint Execution Time, MS: " +
                         Long.toString(millis));
        return BMPOutput;
    }


    private Bitmap CallBMPGenerate(int[] ArrayIn, int imageWidthIn, int imageHeightIn) {
        try {
            return Bitmap.createBitmap(ArrayIn, imageWidthIn, imageHeightIn, Bitmap.Config.RGB_565);
        } catch (Exception Ex) {
            Log.e(EX_TAG, "CallBMPGenerate EX:" + Ex.getMessage());
            return null;
        }

    }

}
