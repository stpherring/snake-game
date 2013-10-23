package com.herring.myfirstgame;

import java.util.List;

public interface Input 
{
	// Since we only have touch input, this interface only includes those methods--
	// more are included if we add in accelerometer input or keyboard input
	public boolean isTouchDown(int pointer);
	
	public int getTouchX(int pointer);
	
	public int getTouchY(int pointer);
	
	public List<TouchEvent> getTouchEvents();
}
