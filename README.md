snake-game
==========

A simple game using OpenGL ES containing elements from Snake

The player is in control of a snake.  The snake can move in any direction in the 2D plane, and the game does not end if it collides with itself.  The snake grows in length by collecting white squares.

The objective of the game is to collect green squares by encompassing them with your snake.  The player has to collect as many of these squares as possible within a given time period (yet to be implemented).

At the moment my primary focus is on collision detection.  The snake is a collection of line segments, and every frame I use a scanline algorithm to check if any one of these intersects another.  However, the algorithm in its current state is spotty at best and needs to be improved.
