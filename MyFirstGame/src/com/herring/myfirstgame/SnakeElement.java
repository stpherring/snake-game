package com.herring.myfirstgame;

import java.util.HashSet;

public class SnakeElement 
{
	private float x, y, dirX, dirY, width, height;
	
	public enum ELEMENT_STATE
	{
		LEFT, RIGHT, INTERSECTION;
	}
	
	private HashSet<ELEMENT_STATE> states;
	
	private int position;
	
	public SnakeElement(Vector2 pos, int position)
	{
		x = pos.x;
		y = pos.y;
		this.width = SnakeGame.SCREEN_WIDTH;
		this.height = SnakeGame.SCREEN_HEIGHT;
		this.position = position;
		
		states = new HashSet<ELEMENT_STATE>();
	}
	
	public void update(float deltaTime)
	{
		x = x + dirX * deltaTime;
		y = y + dirY * deltaTime;
		
		if(x < 0)
		{
			dirX *= -1;
			x = 0;
		}
		
		if(x > width)
		{
			dirX *= -1;
			x = width;
		}
		
		if(y < 0)
		{
			dirY *= -1;
			y = 0;
		}
		
		if(y > height)
		{
			dirY *= -1;
			y = height;
		}
	}
	public void update(float deltaTime, SnakeElement next)
	{
		x = next.getX() + dirX * deltaTime;
		y = next.getY() + dirY * deltaTime;
		
		if(x < 0)
		{
			dirX *= -1;
			x = 0;
		}
		
		if(x > width)
		{
			dirX *= -1;
			x = width;
		}
		
		if(y < 0)
		{
			dirY *= -1;
			y = 0;
		}
		
		if(y > height)
		{
			dirY *= -1;
			y = height;
		}
	}
	
	public float getX()
	{
		return x;
	}
	
	public float getY()
	{
		return y;
	}
	
	public int getPosition()
	{
		return position;
	}
	
	public void setPosition(int position)
	{
		this.position = position;
	}
	
	public HashSet<ELEMENT_STATE> getState()
	{
		return states;
	}
	
	public void addState(ELEMENT_STATE state)
	{
		states.add(state);
	}
	
	public void removeState(ELEMENT_STATE state)
	{
		states.remove(state);
	}
}
