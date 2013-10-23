package com.herring.myfirstgame;

public class Vector2 
{
	public static float TO_RADIANS = (1/180f) * (float)Math.PI;
	public static float TO_DEGREES = (1/(float)Math.PI) * 180;
	
	public float x, y;
	
	public Vector2()
	{
		
	}
	
	public Vector2(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	public Vector2(Vector2 copy)
	{
		this.x = copy.x;
		this.y = copy.y;
	}
	
	public Vector2 copy()
	{
		return new Vector2(x, y);
	}
	
	public Vector2 set(Vector2 copy)
	{
		this.x = copy.x;
		this.y = copy.y;
		return this;
	}
	
	public Vector2 add(float x, float y)
	{
		this.x += x;
		this.y += y;
		
		return this;
	}

	public Vector2 add(Vector2 source)
	{
		this.x += source.x;
		this.y += source.y;
		
		return this;
	}
	
	public Vector2 sub(float x, float y)
	{
		this.x -= x;
		this.y -= y;
		
		return this;
	}
	
	public Vector2 sub(Vector2 source)
	{
		this.x += source.x;
		this.y += source.y;
		
		return this;
	}
	
	public Vector2 mul(float scalar)
	{
		this.x *= scalar;
		this.y *= scalar;
		
		return this;
	}
	
	public float len()
	{
		return (float)Math.sqrt(x * x + y * y);
	}
	
	public Vector2 nor()
	{
		float len = len();
		
		if(len != 0)
		{
			this.x /= len;
			this.y /= len;
		}
		
		return this;
	}
	
	public float angle()
	{
		float angle = (float)Math.atan2(y, x) * TO_DEGREES;
		if(angle < 0)
		{
			angle += 360;
		}
		
		return angle;
	}
	
	public Vector2 rotate(float angle)
	{
		float rad = angle * TO_RADIANS;
		float cos = (float) Math.cos(rad);
		float sin = (float)Math.sin(rad);
		
		float newX = this.x * cos - this.y * sin;
		float newY = this.x * sin + this.y * cos;
		
		this.x = newX;
		this.y = newY;
		
		return this;
		
	}
	
	public float dist(Vector2 other)
	{
		float distX = this.x - other.x;
		float distY = this.y - other.y;
		
		return distX * distX + distY * distY;
	}
	
	public float dist(float x, float y)
	{
		float distX = this.x - x;
		float distY = this.y - y;
		
		return (float)Math.sqrt(distX * distX + distY * distY);
	}
	
	public Vector2 dot(Vector2 vec)
	{
		Vector2 ret = new Vector2(vec.x * x, vec.y * y);
		
		return ret;
	}
	
	public float cross(Vector2 vec)
	{
		return (x * vec.y) - (y * vec.x);
	}
}
