package com.herring.myfirstgame;

public class CapturableObject extends DynamicGameObject
{
	private float x, y;
	
	// Points for the bezier curve
	private float startx, starty;
	private float cpx1, cpy1;
	private float cpx2, cpy2;
	private float endx, endy;
	
	private float t;
	
	private float ANIMATION_TIME;
	
	public CapturableObject(float x, float y, float width, float height) 
	{
		super(x, y, width, height);
		
		ANIMATION_TIME = (float)(Math.random() * 30 + 10);
		
		this.x = x;
		this.y = y;
		
		startx = x;
		starty = y;
		
		cpx1 = (float)(Math.random() * SnakeGame.SCREEN_WIDTH);
		cpy1 = (float)(Math.random() * SnakeGame.SCREEN_HEIGHT);
		
		cpx2 = (float)(Math.random() * SnakeGame.SCREEN_WIDTH);
		cpy2 = (float)(Math.random() * SnakeGame.SCREEN_HEIGHT);
		
		int rand = (int) (Math.random() * 4);
		
		if(rand == 0)
		{
			endx = -30;
			endy = (float)(Math.random()  * SnakeGame.SCREEN_HEIGHT);
		}
		else if(rand == 1)
		{
			endx = SnakeGame.SCREEN_WIDTH + 30;
			endy = (float)(Math.random()  * SnakeGame.SCREEN_HEIGHT);
		}
		else if(rand == 2)
		{
			endx = (float)(Math.random()  * SnakeGame.SCREEN_WIDTH);
			endy = -30;
		}
		else
		{
			endx = (float)(Math.random()  * SnakeGame.SCREEN_WIDTH);
			endy = SnakeGame.SCREEN_HEIGHT + 30;
		}
		
	}
	
	public void update(float deltaTime)
	{
		t += deltaTime;
		
		if(ANIMATION_TIME - t > 0)
		{
			x = startx * ((ANIMATION_TIME - t) / ANIMATION_TIME) * ((ANIMATION_TIME - t) / ANIMATION_TIME) * ((ANIMATION_TIME - t) / ANIMATION_TIME) + 
				3 * cpx1 * ((ANIMATION_TIME - t) / ANIMATION_TIME) * ((ANIMATION_TIME - t) / ANIMATION_TIME) * (t/ ANIMATION_TIME) +
				3 * cpx2 * ((ANIMATION_TIME - t) / ANIMATION_TIME) * (t / ANIMATION_TIME) * (t / ANIMATION_TIME) +
				endx * (t / ANIMATION_TIME) * (t / ANIMATION_TIME) * (t / ANIMATION_TIME);
			
			y = starty * ((ANIMATION_TIME - t) / ANIMATION_TIME) * ((ANIMATION_TIME - t) / ANIMATION_TIME) * ((ANIMATION_TIME - t) / ANIMATION_TIME) + 
				3 * cpy1 * ((ANIMATION_TIME - t) / ANIMATION_TIME) * ((ANIMATION_TIME - t) / ANIMATION_TIME) * (t/ ANIMATION_TIME) +
				3 * cpy2 * ((ANIMATION_TIME - t) / ANIMATION_TIME) * (t / ANIMATION_TIME) * (t / ANIMATION_TIME) +
				endy * (t / ANIMATION_TIME) * (t / ANIMATION_TIME) * (t / ANIMATION_TIME);			
			
			position.x = x;
			position.y = y;
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
	
	public void setX(float x)
	{
		this.x = x;
	}
	
	public void setY(float y)
	{
		this.y = y;
	}

}
