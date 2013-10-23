package com.herring.myfirstgame;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import android.util.Log;

public class SnakeGame extends MyGame 
{
	public static final String TAG = "GLGameTest";
	GLGraphics glGraphics;
	
	public static int SCREEN_WIDTH;
	public static int SCREEN_HEIGHT;
	
	
	Vector2 snakePos = new Vector2();
	Vector2 touchPos = new Vector2();
	Vector2 snakeVelocity = new Vector2(0, 0);

	@Override
	public Screen getStartScreen() 
	{
		return new SnakeScreen(this);
	}
	
	class SnakeScreen extends Screen
	{
		FPSCounter fpsCounter;
		
		static final int NUM_SNAKE_ELEMENTS = 100;
		Vertices snakeModel;
		Vertices foodModel;
		Vertices captureModel;
		
		Snake snake;
		Food food;
		ArrayList<CapturableObject> captureList;
		
		SpatialHashGrid grid;
		
		public SnakeScreen(Game game)
		{
			super(game);
			glGraphics = ((MyGame)game).getGLGraphics();
			
			SCREEN_WIDTH = glGraphics.getWidth();
			SCREEN_HEIGHT = glGraphics.getHeight();
			grid = new SpatialHashGrid(SCREEN_WIDTH, SCREEN_HEIGHT, 32f);
			captureList = new ArrayList<CapturableObject>();
			
			snakeModel = new Vertices(glGraphics, NUM_SNAKE_ELEMENTS, 0, false, false);
			foodModel = new Vertices(glGraphics, 4, 0, false, false);
			captureModel = new Vertices(glGraphics, 4, 0, false, false);
			
			foodModel.setVertices(new float[]{ -16, -16,
												 16, -16,
												 16,  16,
												-16,  16  }, 0, 8);
			
			captureModel.setVertices(new float[]{ -16, -16,
						   					       16, -16,
											       16,  16,
											      -16,  16  }, 0, 8);
			
			float foodX = (float)(Math.random() * SCREEN_WIDTH);
			float foodY = (float)(Math.random() * SCREEN_HEIGHT);
			food = new Food(foodX, foodY, 32f, 32f);
			
			snake = new Snake(0, 0, 10f, 10f, 100);
			
			
			for(int i = 0; i < 100; i++)
			{
				newCapturable();
			}
			
			grid.insertStaticObject(food);
			
			fpsCounter = new FPSCounter();
		}

		@Override
		public void update(float deltaTime)
		{
			List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
			List<SnakeElement> elements = snake.getElements();
			int last = elements.size() - 1;
			
			SnakeElement lastElement;
			
			if(last > -1)
			{
				lastElement = elements.get(last);
			}
			else
			{
				lastElement = null;
			}
			
			int len = touchEvents.size();
			
			for(int i = 0; i < len; i++)
			{
				TouchEvent event = touchEvents.get(i);
				
				if(event == null)
				{
					break;
				}
				
				touchPos.x = (event.x);
				touchPos.y = (glGraphics.getHeight() - event.y);
				if(lastElement != null)
				{
					// This distance is not square-rootified
					float dist = touchPos.dist(lastElement.getX(), lastElement.getY());
					
					float MAX_LENGTH = 20;
					
					if(dist > MAX_LENGTH)
					{
						float x = touchPos.x - lastElement.getX();
						float y = touchPos.y - lastElement.getY();
						
						Vector2 newVec = new Vector2(x, y);
						float angle = newVec.angle();
						angle *= Vector2.TO_RADIANS;
						

						x = lastElement.getX() + (float)(MAX_LENGTH *
							Math.cos((double)angle));

						y = lastElement.getY() + (float)(MAX_LENGTH * 
								Math.sin((double)angle));
						
						touchPos = new Vector2(x, y);
					}
				}
				
				snake.addPoint(touchPos); 
			}
			
			snake.update(deltaTime);
			
			for(int i = 0; i < captureList.size(); i++)
			{
				captureList.get(i).update(deltaTime);
			}
		}

		@Override
		public void present(float deltaTime) 
		{
			fpsCounter.logFrame();
			 
			GL10 gl = glGraphics.getGL10();
			
			gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
			
			gl.glMatrixMode(GL10.GL_MODELVIEW);
				
			/*
			 ******************************************************************************
			 * Draw food 																  *
			 ******************************************************************************
			 */
			
			gl.glColor4f(1, 1, 1, 1);
			
			foodModel.bind();
			
			List<GameObject> colliders = grid.getPotentialColliders(snake);
			
			for(int i = 0; i < colliders.size(); i++)
			{
				GameObject collider = colliders.get(i);
				
				
				if(OverlapTester.overlapRectangles(snake.bounds, collider.bounds))
				{
					
					Log.d(TAG, "Collision!");
					grid.removeObject(collider);
					
					float foodX = (float)(Math.random() * SCREEN_WIDTH);
					float foodY = (float)(Math.random() * SCREEN_HEIGHT);
					
					food = new Food(foodX, foodY, 32f, 32f);
					grid.insertStaticObject(food);
					
					snake.maxPoints += 10;
				}
			}
			
			
			gl.glTranslatef(food.getX(), food.getY(), 0);
			gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, 4);
			
			foodModel.unbind();
			
			
			/*
			 ******************************************************************************
			 * Draw capturable objects 																  *
			 ******************************************************************************
			 */
			
			gl.glColor4f(0, 1, 0, 1);
			
			captureModel.bind();
			
			for(int i = 0; i < captureList.size(); i++)
			{
				CapturableObject object = captureList.get(i);
				gl.glLoadIdentity();
				gl.glTranslatef(object.getX(), object.getY(), 0);
				
				captureModel.draw(GL10.GL_TRIANGLE_FAN, 0, 4);
				gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, 4);
				
			}
			captureModel.unbind();
			
			/*
			 ******************************************************************************
			 * Draw snake 																  *
			 ******************************************************************************
			 */
			
			gl.glColor4f(1, 0, 0, 1);
			
			snakeModel.bind();
			
			List<SnakeElement> elements = snake.getElements();

			float[] vertices = new float[elements.size() * 2];
			
			for(int i = 0; i < elements.size(); i++)
			{
				vertices[2 * i] = elements.get(i).getX();
				vertices[2 * i + 1] = elements.get(i).getY();
			}
			
			snakeModel.setVertices(vertices, 0, elements.size() * 2);
			
			gl.glLoadIdentity();
			snakeModel.draw(GL10.GL_LINE_STRIP, 0, elements.size());
			
			snakeModel.unbind();
		}
		

		@Override
		public void pause() 
		{
			
		}

		@Override
		public void resume() 
		{
			GL10 gl = glGraphics.getGL10();
			
			gl.glViewport(0, 0, glGraphics.getWidth(), glGraphics.getHeight());
			gl.glMatrixMode(GL10.GL_PROJECTION);
			gl.glLoadIdentity();
			gl.glOrthof(0, glGraphics.getWidth(), 0, glGraphics.getHeight(), 1, -1);
		}

		@Override
		public void dispose() 
		{
			
		}
		
		private void newCapturable()
		{
			int rand = (int) (Math.random() * 4);
			
			float startx;
			float starty;
			
			if(rand == 0)
			{
				startx = -30;
				starty = (float)(Math.random()  * SnakeGame.SCREEN_HEIGHT);
			}
			else if(rand == 1)
			{
				startx = SnakeGame.SCREEN_WIDTH + 30;
				starty = (float)(Math.random()  * SnakeGame.SCREEN_HEIGHT);
			}
			else if(rand == 2)
			{
				startx = (float)(Math.random()  * SnakeGame.SCREEN_WIDTH);
				starty = -30;
			}
			else
			{
				startx = (float)(Math.random()  * SnakeGame.SCREEN_WIDTH);
				starty = SnakeGame.SCREEN_HEIGHT + 30;
			}
			captureList.add(new CapturableObject(startx, starty, 32f, 32f));
		}
		
	}


}
