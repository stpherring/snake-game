package com.herring.myfirstgame;

public class OverlapTester 
{
	public static boolean overlapRectangles(Rectangle r1, Rectangle r2)
	{
		if(r1.lowerLeft.x < r2.lowerLeft.x + r2.width &&
		   r1.lowerLeft.x + r1.width > r2.lowerLeft.x &&
		   r1.lowerLeft.y < r2.lowerLeft.y + r2.height &&
		   r1.lowerLeft.y + r1.height > r2.lowerLeft.y)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
