# GameOfLife

This is an implementation of Conway's Game of Life simulation.

It takes the screen size available to it and fills it with 1/3 live pixels, based upon a
user-input random number.  It then calculates the next tick repeatedly.

The user interface was not the point of the exercise, but ended up being a lot of the work.
  Some of the code written for it was inherited from elsewhere and will be used there eventually.

StructureNotes.md has a basic rundown of the project structure.

Please note that this is a program in Java, so it is fairly heavy in a few ways.

The development environment I have used is Android Studio 3.0; the version as of 11/12/2017

ToDo:
1. Unit Tests for the Bitmap generation functions.

1.5 Tests for Android platforms beyond the five I have locally installed (Nexus / Samsung).

1.75 Get a better code standards Linter and get that linter to work.

2. Figure out how to get a Tick Count to work.

3. Backgrounds on the overlay labels

4. Zooming in / out

5. Tapping on the screen

6. More randomization options

7. Performance enhancements (This is a memory elephant, and I want it to not be such)

8. Put an interface on top of GameOfLifeView