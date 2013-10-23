package com.herring.myfirstgame;

public class Food extends GameObject
{
	private float x, y;
	
	public Food(float x, float y, float width, float height)
	{	
		super(x, y, width, height);

		this.x = x;
		this.y = y;
	}
	
	public void update(float deltaTime)
	{
		
	}
	
	public float getX()
	{
		return x;
	}
	
	public float getY()
	{
		return y;
	}
	
	public void setX(float x)
	{
		this.x = x;
	}
	
	public void setY(float y)
	{
		this.y = y;
	}
}
