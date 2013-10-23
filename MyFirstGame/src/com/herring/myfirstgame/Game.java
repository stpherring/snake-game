package com.herring.myfirstgame;

public interface Game 
{
	public Input getInput();
	
	public FileIO getFileIO();
	
	public Audio getAudio();
	
	public void setScreen(Screen screen);
	
	public Screen getCurrentScreen();
	
	public Screen getStartScreen();
}
