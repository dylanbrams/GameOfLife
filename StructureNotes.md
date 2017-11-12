# Structure Notes

**Basic Project File Structure:**


Primary Codebase: app/src/main/java/com/dylanbrams/gameoflife

-- InitMenuActivity: Main menu interface

-- LifeActivity: Activity containing GameOfLifeView

-- GameOfLifeView: View implementing LifeComponents through interface

/LifeComponents

-- LifeMatrixInterface: Interface to LifeMatrix

-- LifeMatrix: Primary matrix for calculation

-- LifePrint: LifeMatrix Printing class (to bitmap)

-- LifeStatusEnum: An enum representing LifeMatrix states (Alive or Dead)


Unit Tests: app/src/test/java/com/dylanbrams/gameoflife/

/LifeComponents

-- LifeComponentsTest.java

There are no instrumentation tests.  For this product to continue towards
being, 'professional acceptable,' there would need to be instrumentation
tests.

**Code Structure:**

InitMenuActivity (Parent / Primary activity)
|
LifeActivity (Child activity) 
|
GameOfLifeView (View)
|
LifeInterface
|
LifeMatrix (Class containing matrix)
|
LifePrint (Class returning a Bitmap)

_Standard Tick Activity Description_ 

Note: The application this code was inherited from was designed to do heavier
calculation per matrix member.  Therefore, steps 9-14 seem poorly designed.
The division, however, was the beginning of multithreading the calculations
being applied to the matrix.

1. Timer Tick - Open New Thread.
2. Timer Tick - Set thread low priority so as to not interfere with GUI
3. Timer Tick - Check whether there is currently a calculation active
4. Timer Tick - Call calcNewTick
5. calcNewTick - call CalcNewMatrix
6. CalcNewMatrix - create array to track finished lines
7. CalcNewMatrix - create list of finished lines
8. CalcNewMatrix - Call CalcLine to calculate a post-tick line
9. CalcLine - Iterate through line, aggregate meaningful squares for each line 
member, pass to CalcLife with original value
10. CalcLife - Decide whether square will be alive next, return decision
11. CalcLine - Save all decisions to line, return to CalcNewMatrix
12. CalcNewMatrix - Mark line as finished in finished array, add line to list
13. CalcNewMatrix - Iterate through finished array, call CheckLineCalculated for each line.
14. CheckLineCalculated - Determine whether line is calculated, return to CalcNewMatrix
15. CalcNewMatrix - Eventually, when all line calculations are completed, combine into a new matrix. Return.
16. calcNewTick - increment Tick
17. Timer Tick - Set currentBackground to a BitmapDrawable pulled from getNewMatrixGraphic
18. getNewMatrixGraphic - Pass through to LifePrint; instantiate and call BMPPrint.
19. LifePrint.BMPPrint - Convert matrix to array of black and white colors.
20. LifePrint.BMPPrint - Call CallBMPGenerate - Push into android Bitmap System call.
21. Bitmap generated returns all thay way back to Timer Tick, assigned to CurrentBackground
22. Timer Tick - set backgroundUpdated to false
23. Timer Tick - call postInvalidate indicating to android that the view needs a redraw.

ASYNCHRONOUS
24. Set the background of the canvas to currentBackground.

