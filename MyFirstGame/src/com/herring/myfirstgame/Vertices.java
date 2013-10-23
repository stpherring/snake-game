package com.herring.myfirstgame;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;


public class Vertices 
{
	// private static final String TAG = "Vertices";
	
	final GLGraphics glGraphics;
	private boolean hasColor;
	private boolean hasTexCoords;
	private int vertexSize;
	private FloatBuffer vertices;
	private ShortBuffer indices;
	
	public Vertices(GLGraphics glGraphics, int maxVertices, int maxIndices, boolean hasColor, boolean hasTexCoords)
	{
		this.glGraphics = glGraphics;
		this.hasColor = hasColor;
		this.hasTexCoords = hasTexCoords;
		this.vertexSize = (2 + (hasColor?4:0)) * 4;
		
		ByteBuffer buffer = ByteBuffer.allocateDirect(maxVertices * vertexSize * 20);
		buffer.order(ByteOrder.nativeOrder());
		vertices = buffer.asFloatBuffer();
		
		if(maxIndices > 0)
		{
			buffer = ByteBuffer.allocateDirect(maxIndices * Short.SIZE);
			buffer.order(ByteOrder.nativeOrder());
			indices = buffer.asShortBuffer();
		}
		else
		{
			indices = null;
		}
	}
	
	public void setVertices(float[] vertices, int offset, int length)
	{
		this.vertices.clear();
		this.vertices.put(vertices, offset, length);
		this.vertices.flip();
	}
	
	public void setIndices(short[] indices, int offset, int length)
	{
		this.indices.clear();
		this.indices.put(indices, offset, length);
		this.indices.flip();
	}
	
	public void bind()
	{
		GL10 gl = glGraphics.getGL10();
		
		gl.glEnable(GL10.GL_VERTEX_ARRAY);
		vertices.position(0);
		gl.glVertexPointer(2, GL10.GL_FLOAT, vertexSize, vertices);
		
		if(hasColor)
		{
			gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
			vertices.position(2);
			gl.glColorPointer(4, GL10.GL_FLOAT, vertexSize, vertices);
		}
		
		if(hasTexCoords)
		{
			gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			vertices.position(hasColor?6:2);
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, vertexSize, vertices);
		}
	}
	 
	public void draw(int primitiveType, int offset, int numVertices)
	{
		GL10 gl = glGraphics.getGL10();
			
		if(indices != null)
		{
			indices.position(offset);
			gl.glDrawElements(primitiveType, numVertices, GL10.GL_UNSIGNED_SHORT, indices);
		}
		else
		{
			gl.glDrawArrays(primitiveType, offset, numVertices);
		}
	}
	
	public void unbind()
	{
		GL10 gl = glGraphics.getGL10();
		
		if(hasTexCoords)
		{
			gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		}
		
		if(hasColor)
		{
			gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
		}
	}
}
