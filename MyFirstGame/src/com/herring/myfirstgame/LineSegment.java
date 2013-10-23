package com.herring.myfirstgame;

import com.herring.myfirstgame.SnakeElement.ELEMENT_STATE;

public class LineSegment 
{
	public SnakeElement start, end;
	
	public LineSegment(SnakeElement start, SnakeElement end)
	{
		this.start = start;
		this.end = end;
	}
	
	public SnakeElement getStart()
	{
		return start;
	}
	
	public SnakeElement getEnd()
	{
		return end;
	}
	
	public SnakeElement intersectsWith(LineSegment segment)
	{
		SnakeElement intersection = null;
		
		/*
		 *  p + tr = q + us (r and s are vectors)
		 *  
		 *  t = (q - p) x s / (r x s)
		 *  
		 *  p + tr gives our point of intersection
		 */
		
		Vector2 p = new Vector2(start.getX(), start.getY());
		Vector2 q = new Vector2(segment.getStart().getX(), segment.getStart().getY());
		
		Vector2 r = new Vector2(end.getX() - start.getX(), end.getY() - start.getY());
		Vector2 s = new Vector2(segment.getEnd().getX() - segment.getStart().getX(), segment.getEnd().getY() - segment.getStart().getY());
		
		float t = 0;
		if(r.cross(s) != 0)
		{
			t = (q.sub(p).cross(s))/(r.cross(s));
		}
		else
		{
			return null;
		}
		
		// If the intersection is beyond the bounds of the line segment
		if(t >= 1 || t <= 0)
		{
			return null;
		}
		
		Vector2 result = p.add(r.mul(t));
		
		intersection = new SnakeElement(result, -1);
		intersection.addState(ELEMENT_STATE.INTERSECTION);
		
		return intersection;
	}

}
