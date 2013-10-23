package com.herring.myfirstgame;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class SingleTouchHandler implements TouchHandler
{
	private static final String TAG = "SingleTouchHandler";
	
	boolean isTouched;
	int touchX;
	int touchY;
	Pool<TouchEvent> touchEventPool;
	List<TouchEvent> touchEvents = new ArrayList<TouchEvent>();
	public static List<TouchEvent> touchEventsBuffer = new ArrayList<TouchEvent>();
	float scaleX;
	float scaleY;
	
	ScheduledExecutorService mUpdater;
	
	public SingleTouchHandler(View view, float scaleX, float scaleY)
	{
		PoolObjectFactory<TouchEvent> factory = new PoolObjectFactory<TouchEvent>() {
		
			@Override
			public TouchEvent createObject()
			{
				return new TouchEvent();
			}
		};
		touchEventPool = new Pool<TouchEvent>(factory, 100);
		view.setOnTouchListener(this);
		
		this.scaleX = scaleX;
		this.scaleY = scaleY;
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) 
	{
		synchronized(this)
		{	
			TouchEvent touchEvent = touchEventPool.newObject();
			touchEvent.x = touchX = (int)(event.getX() * scaleX);
			touchEvent.y = touchY = (int)(event.getY() * scaleY);
			
			switch(event.getAction())
			{
				case MotionEvent.ACTION_HOVER_MOVE:
				case MotionEvent.ACTION_POINTER_INDEX_SHIFT:
				case MotionEvent.ACTION_DOWN:
					touchEvent.type = TouchEvent.TOUCH_DOWN;
					isTouched = true;
					startUpdating(touchEvent.x, touchEvent.y);
					break;
				case MotionEvent.ACTION_MOVE:
					touchEvent.type = TouchEvent.TOUCH_DRAGGED;
					isTouched = true;
					stopUpdating();
					startUpdating(touchEvent.x, touchEvent.y);
					break;
				case MotionEvent.ACTION_CANCEL:
				case MotionEvent.ACTION_UP:
					touchEvent.type = TouchEvent.TOUCH_UP;
					isTouched = false;
					stopUpdating();
					break;

			}
			
			touchEventsBuffer.add(touchEvent);
			
			return true;
		}
	}
	
	@Override
	public boolean isTouchDown(int pointer) 
	{
		synchronized(this)
		{
			if(pointer == 0)
			{
				return isTouched;
			}
			else
			{
				return false;
			}
		}
	}
	
	// This is to start the updater, sending points to the buffer.  This is important because it 
	// keeps the snake updated when the user isn't moving
	private void startUpdating(int x, int y)
	{
		mUpdater = Executors.newSingleThreadScheduledExecutor();
		mUpdater.scheduleAtFixedRate(new HoldTask(x, y), 15, 15, TimeUnit.MILLISECONDS);
	}
	
	private void stopUpdating()
	{
		mUpdater.shutdownNow();
	}
	
	@Override
	public int getTouchX(int pointer) 
	{
		synchronized(this)
		{
			return touchX;
		}
	}
	
	@Override
	public int getTouchY(int pointer) 
	{
		synchronized(this)
		{
			return touchY;
		}
	}
	
	@Override
	public List<TouchEvent> getTouchEvents() 
	{
		synchronized(this)
		{
			int len = touchEvents.size();
			for(int i = 0; i < len; i++)
			{
				touchEventPool.free(touchEvents.get(i));
			}
			touchEvents.clear();
			touchEvents.addAll(touchEventsBuffer);
			touchEventsBuffer.clear();
			return touchEvents;
		}
	}
	
	private class HoldTask implements Runnable
	{
		private int x, y;
		
		public HoldTask(int x, int y)
		{
			this.x = x;
			this.y = y;
		}
		
		@Override
		public void run() 
		{
			TouchEvent event = new TouchEvent();
			event.type = TouchEvent.TOUCH_DOWN;
			event.x = x;
			event.y = y;
			SingleTouchHandler.touchEventsBuffer.add(event);
		}
		
	}
}
