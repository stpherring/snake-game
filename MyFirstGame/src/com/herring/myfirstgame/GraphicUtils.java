package com.herring.myfirstgame;

import android.opengl.GLES20;
import android.util.Log;

public class GraphicUtils 
{
	private final static String vertexShaderCode =
		    "uniform mat4 uMVPMatrix;" +
			"attribute vec4 vPosition;" +
		    "void main() {" +
		    "  gl_Position = vPosition;" +
		    "}";

	private final static String fragmentShaderCode =
	    "precision mediump float;" +
	    "uniform vec4 vColor;" +
	    "void main() {" +
	    "  gl_FragColor = vColor;" +
	    "}";
	
	private static final String TAG = "GraphicUtils";
	
	private static int mCurrentProgram;
	
	public static int createProgram()
	{
		int program = GLES20.glCreateProgram();
		int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
		GLES20.glAttachShader(program, vertexShader);
		
		int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);
		GLES20.glAttachShader(program, fragmentShader);
		
		GLES20.glLinkProgram(program);
		int[] linkStatus = new int[1];
		
		GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0);
		
		if(linkStatus[0] != GLES20.GL_TRUE)
		{
			Log.e(TAG, "Could not link program: " + GLES20.glGetProgramInfoLog(program));
			GLES20.glDeleteProgram(program);
			program = 0;
		}
		
		mCurrentProgram = program;
		
		return program;
	}
	
	private static int loadShader(int shaderType, String source)
	{
		int shader = GLES20.glCreateShader(shaderType);
		
		GLES20.glShaderSource(shader, source);
		checkGlError("glShaderSource");
		
		GLES20.glCompileShader(shader);
		checkGlError("glCompileShader");
		
		return shader;
	}
	
	public static void checkGlError(String op)
	{
		int error;
		while((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR)
		{
			String errorMessage = GLES20.glGetProgramInfoLog(mCurrentProgram);
			Log.e(TAG, op + ": glError " + error + " " + errorMessage);
			
		}
	}
}
