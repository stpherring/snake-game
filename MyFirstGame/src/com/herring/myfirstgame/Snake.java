package com.herring.myfirstgame;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.TreeSet;

import android.util.Log;

public class Snake extends DynamicGameObject
{
	private static final String TAG = "Snake";
	List<SnakeElement> points;
	public int maxPoints;
	
	private float width, height;
	
	public Snake(float x, float y, float width, float height, int maxPoints)
	{
		super(x, y, width, height);
		points = new ArrayList<SnakeElement>();
		this.maxPoints = maxPoints;
		this.width = width;
		this.height = height;
	}
	
	public void update(float deltaTime)
	{
		for(int i = 0; i < points.size(); i++)
		{
			if(i < points.size() - 1)
			{
				points.get(i).update(deltaTime, points.get(i + 1));
			}
			else
			{
				points.get(i).update(deltaTime);
			}
		}
		
		if(points.size() != 0)
		{
			position.x = points.get(points.size() - 1).getX();
			position.y = points.get(points.size() - 1).getY();
			
			bounds = new Rectangle(position.x - width/2, position.y - height/2, width, height);
			getIntersection();	
		}
	}
	
	public void addPoint(Vector2 pos)
	{
		// If we haven't used all of our points, add to the list.  Otherwise, remove last point.
		if(points.size() < maxPoints)
		{
			points.add(new SnakeElement(pos, points.size() - 1));	
		}
		else
		{
			points.remove(0);
			for(int i = 0; i < points.size(); i++)
			{
				// This might mess me up later, so I'm making a note of it
				points.get(i).setPosition(i);
				if(i != 0)
				{
					points.get(i).addState(SnakeElement.ELEMENT_STATE.RIGHT);
				}
				else
				{
					points.get(i).removeState(SnakeElement.ELEMENT_STATE.RIGHT);
				}
				
				if(i != points.size() - 1)
				{
					points.get(i).addState(SnakeElement.ELEMENT_STATE.LEFT);
				}
				else
				{
					points.get(i).removeState(SnakeElement.ELEMENT_STATE.LEFT);
				}
			}
			points.add(new SnakeElement(pos, points.size() - 1));
		}
	}
	
	public List<SnakeElement> getElements()
	{
		return points;
	}
	
	public List<Vector2> getIntersection()
	{	
		
		List<Vector2> intersections = new ArrayList<Vector2>();
		
		HashMap<SnakeElement, List<LineSegment>> intersectionMap = new HashMap<SnakeElement, List<LineSegment>>();
		
		PriorityQueue<SnakeElement> queue = new PriorityQueue<SnakeElement>(points.size(), 
				new Comparator<SnakeElement>()
				{
					@Override
					public int compare(SnakeElement left, SnakeElement right) 
					{
						if(left.getX() < right.getX())
						{
							return 1;
						}
						if(left.getX() > right.getX())
						{
							return -1;
						}
						return 0;
					}
			
				});
		
		TreeSet<LineSegment> tree = new TreeSet<LineSegment>(new Comparator<LineSegment>()
				{

					@Override
					public int compare(LineSegment left, LineSegment right) 
					{
						if(left.getStart().getY() < right.getStart().getY())
						{
							return 1;
						}
						if(left.getStart().getY() > right.getStart().getY())
						{
							return -1;
						}
						return 0;
					}
			
				});
		
		for(int i = 0; i < points.size(); i++)
		{
			queue.add(points.get(i));
		}
		

		while(!queue.isEmpty())
		{
			SnakeElement element = queue.remove();
			
			int position = element.getPosition();
			
			// Element is start
			if(element.getState().contains(SnakeElement.ELEMENT_STATE.LEFT))
			{
				LineSegment line = null;
				
				// Get the line segment to look at
				
				if(position != 0 && points.get(position - 1).getX() >= element.getX())
				{
					line = new LineSegment(element, points.get(position - 1));
					tree.add(line);
					
				}
				if(position + 1 != points.size() && points.get(position + 1).getX() >= element.getX())
				{
					line = new LineSegment(element, points.get(position + 1));
					tree.add(line);
				}
				
				if(line != null)
				{
					// Get the segments directly above and below the current line segment
					
					LineSegment successor = tree.higher(line);
					LineSegment predecessor = tree.lower(line);
					
					// If the line above exists (might be a bug here)
					if(successor != null)
					{
						SnakeElement intersect = line.intersectsWith(successor);
						if(intersect != null)
						{
							queue.add(intersect);
							ArrayList<LineSegment> lines = new ArrayList<LineSegment>();
							lines.add(successor);
							lines.add(line);
							
							intersectionMap.put(intersect, lines);
						}
					}
					
					// If the line below exists (may be a bug here too)
					if(predecessor != null)
					{
						SnakeElement intersect = line.intersectsWith(predecessor);
						if(intersect != null)
						{
							queue.add(intersect);
							ArrayList<LineSegment> lines = new ArrayList<LineSegment>();
							lines.add(line);
							lines.add(predecessor);
							
							intersectionMap.put(intersect, lines);
						}
					}
					
					// If both lines exist (may be bugs)
					if(successor != null && predecessor != null)
					{
						SnakeElement intersect = predecessor.intersectsWith(successor);
						if(intersect != null)
						{
							queue.add(intersect);
							ArrayList<LineSegment> lines = new ArrayList<LineSegment>();
							lines.add(successor);
							lines.add(predecessor);
							
							intersectionMap.put(intersect, lines);
						}
					}
				}
			}
			
			
			// Element is end
			if(element.getState().contains(SnakeElement.ELEMENT_STATE.RIGHT))
			{
				/*
				 * Here we search the structure for the successor and the predecessor of the current element.
				 * Once we have them, we delete the current element from the tree.
				 * We then check to see if those two elements intersect.  If they do, insert the point into
				 * the queue.
				 */
				
				
				for(LineSegment segment : tree)
				{
					if(segment.end.getPosition() == element.getPosition())
					{
						LineSegment successor = tree.higher(segment);
						LineSegment predecessor = tree.lower(segment);
						
						tree.remove(segment);
						
						if(successor != null && predecessor != null)
						{
							SnakeElement intersection = successor.intersectsWith(predecessor);
							
							if(intersection != null)
							{
								queue.add(intersection);
								
								ArrayList<LineSegment> lines = new ArrayList<LineSegment>();
								lines.add(successor);
								lines.add(predecessor);
							
								intersectionMap.put(intersection, lines);
							}
						}
						
						break;
					}
				}
			}
			
			// Element is intersection
			else if(element.getState().contains(SnakeElement.ELEMENT_STATE.INTERSECTION))
			{
				intersections.add(new Vector2(element.getX(), element.getY()));
				
				List<LineSegment> lines = (List<LineSegment>) intersectionMap.get(element);
				if(lines.size() != 0)
				{
					LineSegment successor = tree.higher(lines.get(0));
					LineSegment predecessor = tree.lower(lines.get(1));
				
					if(successor != null)
					{
						SnakeElement intersect = successor.intersectsWith(lines.get(1));
						
						if(intersect != null)
						{
							queue.add(intersect);
							
							ArrayList<LineSegment> intersectLines = new ArrayList<LineSegment>();
							lines.add(successor);
							lines.add(lines.get(1));
							
							intersectionMap.put(intersect, intersectLines);
						}
					}
				
					if(predecessor != null)
					{
						SnakeElement intersect = predecessor.intersectsWith(lines.get(0));
					
						if(intersect != null)
						{
							queue.add(intersect);
							
							ArrayList<LineSegment> intersectLines = new ArrayList<LineSegment>();
							lines.add(lines.get(0));
							lines.add(predecessor);
							
							intersectionMap.put(intersect, intersectLines);
						}
					}		
				}
			}
		}
		

		if(intersections.size() != 0)
		{
			Log.d(TAG, "Intersections.get(0): (" + intersections.get(0).x + ", " + intersections.get(0).y + ")");
		}
		return intersections;
	}
}
