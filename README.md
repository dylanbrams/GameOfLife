# GameOfLife

This is an implementation of Conway's Game of Life simulation.

It takes the screen size available to it and fills it with 1/3 live pixels, based upon a
user-input random number.  It then calculates the next tick repeatedly.

The user interface could use a ton of work.  

ToDo:
1. Unit Tests for the Bitmap generation functions.
1.5 Tests for Android platforms beyond the five I have locally installed (Nexus / Samsung).
2. Figure out how to get a Tick Count to work.
3. Backgrounds on the overlay labels
4. Zooming in / out
5. Tapping on the screen
6. More randomization options
7. Performance enhancements (This is a memory elephant, and I want it to not be such)