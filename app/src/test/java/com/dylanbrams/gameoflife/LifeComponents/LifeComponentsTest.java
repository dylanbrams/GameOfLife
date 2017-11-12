package com.dylanbrams.gameoflife.LifeComponents;

/*
  Created by Dylan Brams on 11/10/2017.
  This is the unit test file for the LifeComponents package.
 */

import org.junit.runner.RunWith;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import static org.junit.Assert.*;
import android.util.Log;

@RunWith(PowerMockRunner.class)
@PrepareForTest({android.util.Log.class, android.graphics.Bitmap.class})
public class LifeComponentsTest {

    // Cases where death happens due to underpopulation.
    @Test
    public void UnitTest_CalculateLife_DeadLonely() throws Exception{
        short deadLonely = 1;
        PowerMockito.mockStatic(Log.class);
        PowerMockito.mockStatic(android.graphics.Bitmap.class);
        LifeMatrix LittleMatrix = new LifeMatrix(3, 3);
        LifeStatusEnum chkStatus = LittleMatrix.CalculateLife(deadLonely, LifeStatusEnum.AlivePoint);
        assertEquals(chkStatus, LifeStatusEnum.DeadPoint);
        LifeStatusEnum chkStatus2 = LittleMatrix.CalculateLife(deadLonely, LifeStatusEnum.DeadPoint);
        assertEquals(chkStatus, LifeStatusEnum.DeadPoint);
    }

    // Cases where death happens due to overcrowding.
    @Test
    public void UnitTest_CalculateLife_DeadCrowded() throws Exception{
        short deadCrowded = 4;
        PowerMockito.mockStatic(Log.class);
        PowerMockito.mockStatic(android.graphics.Bitmap.class);
        LifeMatrix LittleMatrix = new LifeMatrix(3, 3);
        LifeStatusEnum chkStatus = LittleMatrix.CalculateLife(deadCrowded, LifeStatusEnum.AlivePoint);
        assertEquals(chkStatus, LifeStatusEnum.DeadPoint);
        LifeStatusEnum chkStatus2 = LittleMatrix.CalculateLife(deadCrowded, LifeStatusEnum.AlivePoint);
        assertEquals(chkStatus2, LifeStatusEnum.DeadPoint);
    }

    // Cases where life happens due to the three possible scenarios.
    @Test
    public void UnitTest_CalculateLife_AliveHappy() throws Exception{
        short aliveHappyA = 2;
        short aliveHappyB = 3;
        PowerMockito.mockStatic(Log.class);
        PowerMockito.mockStatic(android.graphics.Bitmap.class);
        LifeMatrix LittleMatrix = new LifeMatrix(3, 3);
        LifeStatusEnum chkStatus = LittleMatrix.CalculateLife(aliveHappyA, LifeStatusEnum.AlivePoint);
        assertEquals(chkStatus, LifeStatusEnum.AlivePoint);
        LifeStatusEnum chkStatus2 = LittleMatrix.CalculateLife(aliveHappyB, LifeStatusEnum.AlivePoint);
        assertEquals(chkStatus2, LifeStatusEnum.AlivePoint);
        LifeStatusEnum chkStatus3 = LittleMatrix.CalculateLife(aliveHappyB, LifeStatusEnum.DeadPoint);
        assertEquals(chkStatus3, LifeStatusEnum.AlivePoint);
    }

    @Test
    public void UnitTest_CheckEqualMatrix_Pass(){
        PowerMockito.mockStatic(Log.class);
        PowerMockito.mockStatic(android.graphics.Bitmap.class);
        LifeStatusEnum BlinkerIn[][] = {{LifeStatusEnum.DeadPoint, LifeStatusEnum.AlivePoint, LifeStatusEnum.DeadPoint},
                                        {LifeStatusEnum.DeadPoint, LifeStatusEnum.AlivePoint, LifeStatusEnum.DeadPoint},
                                        {LifeStatusEnum.DeadPoint, LifeStatusEnum.AlivePoint, LifeStatusEnum.DeadPoint}};
        LifeMatrix LittleMatrix = new LifeMatrix(3, 3);
        LittleMatrix.SetLifeMatrix(BlinkerIn);
        assertTrue(LittleMatrix.checkEqualMatrix(BlinkerIn));
    }

    @Test
    public void UnitTest_CheckEqualMatrix_Fail(){
        PowerMockito.mockStatic(Log.class);
        PowerMockito.mockStatic(android.graphics.Bitmap.class);
        LifeStatusEnum BlinkerIn[][] = {{LifeStatusEnum.DeadPoint, LifeStatusEnum.AlivePoint, LifeStatusEnum.DeadPoint},
                                        {LifeStatusEnum.DeadPoint, LifeStatusEnum.AlivePoint, LifeStatusEnum.DeadPoint},
                                        {LifeStatusEnum.DeadPoint, LifeStatusEnum.AlivePoint, LifeStatusEnum.DeadPoint}};
        LifeStatusEnum BlinkerOut[][] = {{LifeStatusEnum.DeadPoint, LifeStatusEnum.DeadPoint, LifeStatusEnum.DeadPoint},
                                         {LifeStatusEnum.AlivePoint, LifeStatusEnum.AlivePoint, LifeStatusEnum.AlivePoint},
                                         {LifeStatusEnum.DeadPoint, LifeStatusEnum.DeadPoint, LifeStatusEnum.DeadPoint}};
        LifeMatrix LittleMatrix = new LifeMatrix(3, 3);
        LittleMatrix.SetLifeMatrix(BlinkerIn);
        assertFalse(LittleMatrix.checkEqualMatrix(BlinkerOut));
    }

    @Test
    public void UnitTest_CheckEqualMatrix_FailSizeOne(){
        PowerMockito.mockStatic(Log.class);
        PowerMockito.mockStatic(android.graphics.Bitmap.class);
        LifeStatusEnum BlinkerIn[][] = {{LifeStatusEnum.DeadPoint, LifeStatusEnum.AlivePoint, LifeStatusEnum.DeadPoint},
                                        {LifeStatusEnum.DeadPoint, LifeStatusEnum.AlivePoint, LifeStatusEnum.DeadPoint},
                                        {LifeStatusEnum.DeadPoint, LifeStatusEnum.AlivePoint, LifeStatusEnum.DeadPoint}};
        LifeStatusEnum BlinkerOut[][] = {{LifeStatusEnum.DeadPoint, LifeStatusEnum.DeadPoint, LifeStatusEnum.DeadPoint}};
        LifeMatrix LittleMatrix = new LifeMatrix(3, 3);
        LittleMatrix.SetLifeMatrix(BlinkerIn);
        assertFalse(LittleMatrix.checkEqualMatrix(BlinkerOut));
    }

    @Test
    public void UnitTest_CheckEqualMatrix_FailSizeTwo(){
        PowerMockito.mockStatic(Log.class);
        PowerMockito.mockStatic(android.graphics.Bitmap.class);
        LifeStatusEnum BlinkerIn[][] = {{LifeStatusEnum.DeadPoint, LifeStatusEnum.AlivePoint, LifeStatusEnum.DeadPoint},
                                        {LifeStatusEnum.DeadPoint, LifeStatusEnum.AlivePoint, LifeStatusEnum.DeadPoint},
                                        {LifeStatusEnum.DeadPoint, LifeStatusEnum.AlivePoint, LifeStatusEnum.DeadPoint}};
        LifeStatusEnum BlinkerOut[][] = {{LifeStatusEnum.DeadPoint, LifeStatusEnum.DeadPoint, LifeStatusEnum.DeadPoint},
                                        {LifeStatusEnum.AlivePoint, LifeStatusEnum.AlivePoint},
                                        {LifeStatusEnum.DeadPoint, LifeStatusEnum.DeadPoint, LifeStatusEnum.DeadPoint}};
        LifeMatrix LittleMatrix = new LifeMatrix(3, 3);
        LittleMatrix.SetLifeMatrix(BlinkerIn);
        assertFalse(LittleMatrix.checkEqualMatrix(BlinkerOut));
    }

    @Test
    public void UnitTest_CalcNewTick_Blinker() throws Exception{
        PowerMockito.mockStatic(Log.class);
        PowerMockito.mockStatic(android.graphics.Bitmap.class);
        LifeStatusEnum BlinkerIn[][] = {{LifeStatusEnum.DeadPoint, LifeStatusEnum.AlivePoint, LifeStatusEnum.DeadPoint},
                                       {LifeStatusEnum.DeadPoint, LifeStatusEnum.AlivePoint, LifeStatusEnum.DeadPoint},
                                       {LifeStatusEnum.DeadPoint, LifeStatusEnum.AlivePoint, LifeStatusEnum.DeadPoint}};
        LifeStatusEnum BlinkerOut[][] = {{LifeStatusEnum.DeadPoint, LifeStatusEnum.DeadPoint, LifeStatusEnum.DeadPoint},
                                        {LifeStatusEnum.AlivePoint, LifeStatusEnum.AlivePoint, LifeStatusEnum.AlivePoint},
                                        {LifeStatusEnum.DeadPoint, LifeStatusEnum.DeadPoint, LifeStatusEnum.DeadPoint}};
        LifeMatrix LittleMatrix = new LifeMatrix(3, 3);
        LittleMatrix.SetLifeMatrix(BlinkerIn);
        LittleMatrix.calcNewTick();
        assertTrue(LittleMatrix.checkEqualMatrix(BlinkerOut));
    }

    @Test
    public void UnitTest_CalcNewTick_Block() throws Exception{
        PowerMockito.mockStatic(Log.class);
        PowerMockito.mockStatic(android.graphics.Bitmap.class);
        LifeStatusEnum BlockStatic[][] = {
                {LifeStatusEnum.DeadPoint, LifeStatusEnum.DeadPoint, LifeStatusEnum.DeadPoint, LifeStatusEnum.DeadPoint},
                {LifeStatusEnum.DeadPoint, LifeStatusEnum.AlivePoint, LifeStatusEnum.AlivePoint, LifeStatusEnum.DeadPoint},
                {LifeStatusEnum.DeadPoint, LifeStatusEnum.AlivePoint, LifeStatusEnum.AlivePoint, LifeStatusEnum.DeadPoint},
                {LifeStatusEnum.DeadPoint, LifeStatusEnum.DeadPoint, LifeStatusEnum.DeadPoint, LifeStatusEnum.DeadPoint}};
        LifeMatrix LittleMatrix = new LifeMatrix(4, 4);
        LittleMatrix.SetLifeMatrix(BlockStatic);
        LittleMatrix.calcNewTick();
        assertTrue(LittleMatrix.checkEqualMatrix(BlockStatic));
    }

    @Test
    public void UnitTest_GenerateRandomWithSeed() throws Exception{
        PowerMockito.mockStatic(Log.class);
        PowerMockito.mockStatic(android.graphics.Bitmap.class);
        int RandNum = 12345; // Idiots and luggage and such.
        LifeStatusEnum RandomExpected[][] = {{LifeStatusEnum.DeadPoint, LifeStatusEnum.DeadPoint, LifeStatusEnum.DeadPoint},
                                        {LifeStatusEnum.DeadPoint, LifeStatusEnum.DeadPoint, LifeStatusEnum.AlivePoint},
                                        {LifeStatusEnum.AlivePoint, LifeStatusEnum.AlivePoint, LifeStatusEnum.AlivePoint}};
        LifeMatrixInterface LittleMatrix = new LifeMatrix(3, 3);
        LittleMatrix.FillMatrixFromRandomSeed(RandNum);
        assertTrue(LittleMatrix.checkEqualMatrix(RandomExpected));
    }
}
