package com.dylanbrams.gameoflife.LifeComponents;

/**
 * Created by ThinkPad on 11/10/2017.
 */

import org.junit.runner.RunWith;
import android.graphics.Bitmap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.ArrayList;

import android.graphics.Color;
import android.util.Log;


/**
 * Created by ThinkPad on 7/28/2017.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({android.util.Log.class, android.graphics.Bitmap.class})
public class LifeComponentsTest {

    @Test
    public void UnitTest_CalculateLife_DeadLonely() throws Exception{
        short deadLonely = 1;
        PowerMockito.mockStatic(Log.class);
        PowerMockito.mockStatic(android.graphics.Bitmap.class);
        LifeMatrix LittleMatrix = new LifeMatrix(3, 3);
        LifeStatusEnum chkStatus = LittleMatrix.CalculateLife(deadLonely);
        assertEquals(chkStatus, LifeStatusEnum.DeadPoint);
    }

    @Test
    public void UnitTest_CalculateLife_DeadCrowded() throws Exception{
        short deadCrowded = 4;
        PowerMockito.mockStatic(Log.class);
        PowerMockito.mockStatic(android.graphics.Bitmap.class);
        LifeMatrix LittleMatrix = new LifeMatrix(3, 3);
        LifeStatusEnum chkStatus = LittleMatrix.CalculateLife(deadCrowded);
        //assertEquals(chkStatus, LifeStatusEnum.DeadPoint);
    }

    @Test
    public void UnitTest_CalculateLife_AliveHappy() throws Exception{
        short aliveHappyA = 2;
        short aliveHappyB = 2;
        PowerMockito.mockStatic(Log.class);
        PowerMockito.mockStatic(android.graphics.Bitmap.class);
        LifeMatrix LittleMatrix = new LifeMatrix(3, 3);
        LifeStatusEnum chkStatus = LittleMatrix.CalculateLife(aliveHappyA);
        //assertEquals(chkStatus, LifeStatusEnum.DeadPoint);
        LifeStatusEnum chkStatus2 = LittleMatrix.CalculateLife(aliveHappyB);
        //assertEquals(chkStatus, LifeStatusEnum.DeadPoint);
    }
}
