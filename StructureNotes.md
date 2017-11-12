# Structure Notes

Basic Project Structure:


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