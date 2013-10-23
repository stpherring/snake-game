package com.herring.myfirstgame;

import java.util.ArrayList;
import java.util.List;

public class Pool<T> 
{
	private final List<T> freeObjects;
	private final PoolObjectFactory<T> factory;
	private final int maxSize;
	
	public Pool(PoolObjectFactory<T> factory, int maxSize)
	{
		this.factory = factory;
		this.maxSize = maxSize;
		this.freeObjects = new ArrayList<T>(maxSize);
	}
	
	// newObject() returns either a brand new instance of the type T via factory.createObject
	// or returns a pooled instance
	public T newObject()
	{
		T object = null;
		
		if(freeObjects.isEmpty())
		{
			object = factory.createObject();
		}
		else
		{
			object = freeObjects.remove(freeObjects.size() - 1);
		}
		
		return object;
	}
	
	
	// This method lets us reinsert objects that we no longer use.  If the object cannot be inserted,
	// then it is picked up by the garbage collector
	public void free(T object)
	{
		if(freeObjects.size() < maxSize)
		{
			freeObjects.add(object);
		}
	}
}
