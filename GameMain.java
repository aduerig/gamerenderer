package engine;

import java.awt.*;

import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.*;
   
public class GameMain extends JFrame // main class for the game as a Swing application
{     
	// Define constants for the game
	static final int CANVAS_WIDTH = 800;    // width and height of the game screen
	static final int CANVAS_HEIGHT = 600;
	static final int UPDATE_RATE = 30;    // number of game update per second
	static final long UPDATE_PERIOD = 1000000000L / UPDATE_RATE;  // nanoseconds
   
	private Level level;
	private Level light;
	private double dropWidth, dropHeight;
	private int xOffset, yOffset, xPos, yPos;
	private static int blockSize;
	private int xStart;
	private int yStart;
	private int xFinal;
	private int yFinal;
	private int xVel;
	private int yVel;
	private int xMaxVel;
	private int yMaxVel;
	private int mouseX;
	private int mouseY;
	private int runRate;
	private int gravity;
	private int timer;
	private int entityCheck;
	private int checkNumber;
	private Vector<Image> images;
	private boolean keyDown, leftKeyDown, rightKeyDown, upKeyDown, downKeyDown, wKeyDown, aKeyDown, sKeyDown, dKeyDown, spaceKeyDown, leftClickDown, rightClickDown, scrollClickDown;
	private Character c1;
	private Player player;
	private BlockType blockType;
	private Point clickedTile, currentTile;
 
	// Enumeration for the states of the game.
	static enum State 
	{
		INITIALIZED, PLAYING, PAUSED, GAMEOVER, DESTROYED
	}
	static State state;   // current state of the game
	static enum Wallx
	{
		LEFT, RIGHT, NEITHER
	}
	static Wallx wallx;   // current state of the game
	static enum Wally
	{
		UP, DOWN, NEITHER
	}
	static Wally wally;   // current state of the game
   
	// Define instance variables for the game objects
	// ......
	// ......
   
	// Handle for the custom drawing panel
	
	private GameCanvas canvas;
   
	// Constructor to initialize the UI components and game objects
	
	public GameMain() 
	{
		// Initialize the game objects + variables//
		gameInit();
		
		//UI Components//
		canvas = new GameCanvas();
		canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
		this.setContentPane(canvas);
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = getSize();
		int xS = ((screenSize.width - frameSize.width) / 2) - CANVAS_WIDTH/2;
		int yS = ((screenSize.height - frameSize.height) / 2) - CANVAS_HEIGHT/2;
		this.setLocation(xS, yS); // set location of frame in center of screen
   
		// Other UI components such as button, score board, if any.
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.pack();
		this.setTitle("Blockcraft!");
		this.setVisible(true);
	}
	
	// Initialize all the game objects, run only once in the constructor of the main class.
	
	public void gameInit() 
	{
		//System.out.println("lol");
		level = new Level(1500, 1000);
		//level.generateLevel();
		//light = new Level(level.getLevelWidth(), level.getLevelHeight());
		//light.generateLight();
		
		
		gravity = 1;
		xPos = 0;
		yPos = 0;
		xMaxVel = 12;
		yMaxVel = 12;
		blockSize = 10;
		
		
		player = new Player((int)increment(level.getLevelWidth())/2 - blockSize*9, (int)increment(level.getLevelHeight())/2 - blockSize*2, 1, 1, xMaxVel, yMaxVel, false, 0, 1);
		c1 = new Character((int)increment(level.getLevelWidth())/2, (int)increment(level.getLevelHeight())/2 - blockSize*5, 5.7, 2.7, 4, 16, true, gravity, 2);
		xPos = player.getX();
		yPos = player.getY();
		level.addEntity(player);
		level.addEntity(c1);
		
		runRate = 1;
		
		dropWidth = .40;
		dropHeight = .40;
		
		entityCheck = 0;
		checkNumber = 0;
		
		xOffset = 0;
		yOffset = 0;
		xStart = 0;
		yStart = 0;
		xFinal = 0;
		yFinal = 0;
		xVel = 0;
		yVel = 0;
		timer = 0;
		
		mouseX = 0;
		mouseY = 0;
		
		clickedTile = new Point(0, 0);
		currentTile = new Point(0, 0);
		
		blockType = new BlockType();
		
		state = State.PLAYING;
		wallx = Wallx.NEITHER;
		wally = Wally.NEITHER;
		leftClickDown = false;
		rightClickDown = false;
		scrollClickDown = false;
		leftKeyDown = false;
		rightKeyDown = false;
		upKeyDown = false;
		downKeyDown = false;
		wKeyDown = false;
		aKeyDown = false;
		sKeyDown = false;
		dKeyDown = false;
		spaceKeyDown = false;
		keyDown = false;
		
		images = new Vector<Image>();
		
		gameStart();
	}

	//Run in separate thread to load all images//
	
	public void loadImages() 
	{
		String temp1;
		String temp2;
		String temp3;
		String path;
		for(int i = 0; i < 3; i++)
		{
			temp1 = "images//";
			temp2 = Integer.toString(i);
			temp3 = ".png";
			path = temp1 + temp2 + temp3;
	        File file = new File(path);
	        try
	        {
	        	images.add(ImageIO.read(file));
	        }
	        catch(Exception e)
	        {
	        	e.printStackTrace();
	        }
		}
	}
	
	public BufferedImage getImage(int blockTemp) 
	{
		return (BufferedImage) images.get(blockTemp);
	}
	
	public static double increment(double d)
	{
		double a = blockSize * d;
		return a;
	}
	public static double decrement(double numTemp)
	{
		double a = (double)numTemp/blockSize;
		return a;
	}
	
	// Shutdown the game, clean up code that runs only once.
	public void gameShutdown()
	{
		// 
	}
	
	
	// Run the game loop here.
	private void gameLoop() 
	{
		// Regenerate the game objects for a new game
		// ......
		//state = State.PLAYING;
		
		// Game loop
		long beginTime, timeTaken, timeLeft;
		while (true) 
		{
			
			beginTime = System.nanoTime();
			if (state == State.PLAYING)
			{
				gameUpdate();
			}
			repaint();
			
			
			// Delay timer to provide the necessary delay to meet the target rate
			timeTaken = System.nanoTime() - beginTime;
			timeLeft = (UPDATE_PERIOD - timeTaken) / 1000000L;  // in milliseconds
			if (timeLeft < 10) timeLeft = 10;   // set a minimum
			try 
			{
				// Provides the necessary delay and also yields control so that other thread can do work.
				Thread.sleep(timeLeft);
			}	 
			catch (InterruptedException ex)
			{ 
				//
			}
		}
   	}
	
	// Update the state and position of all the game objects,
	// detect collisions and provide responses.
	public void gameUpdate() 
	{ 
		mouseDraw();
		levelMove();
		entitiesMove();
		resolveCollisions();
		updateEntities();
		restrictLevelWalls();
		updateLight();
	}
	
	public void mouseDraw()
	{
		currentTile.x = (int)Math.floor(decrement(mouseX + xPos - CANVAS_WIDTH/2));
		currentTile.y = (int)Math.floor(decrement(mouseY + yPos - CANVAS_HEIGHT/2));
		
		if(clickedTile.x != currentTile.x || clickedTile.y != currentTile.y || level.getTileS(currentTile.x, currentTile.y) == -1)
		{
			timer = 0;
			clickedTile.x = (int)Math.floor(decrement(mouseX + xPos - CANVAS_WIDTH/2));
			clickedTile.y = (int)Math.floor(decrement(mouseY + yPos - CANVAS_HEIGHT/2));
		}
		if(leftClickDown == true)
		{
			timer++;
			if(timer >= (blockType.getHardness(level.getTileS(currentTile.x, currentTile.y))) && level.getTileS(currentTile.x, currentTile.y) != -1)
			{
				blockBreak();
			}
		}
		else
		{
			timer = 0;
		}
		
		if(rightClickDown == true)
		{
			blockSet();
			
		}
	}
	
	public void blockBreak()
	{
		Item itemDrop = new Item(0, (int)increment(clickedTile.y), dropWidth, dropHeight, 4, 16, true, gravity, level.getTileS(clickedTile.x, clickedTile.y));
		itemDrop.setX((int)(((Math.random() * (blockSize - increment(dropWidth))) + (int)increment(clickedTile.x))));
		itemDrop.setY((int)(((Math.random() * (blockSize - increment(dropHeight))) + (int)increment(clickedTile.y))));
		
		itemDrop.setVelX(Math.random()*4);
		itemDrop.setVelY(Math.random()*4);
		
		level.setTileS((int)Math.floor(decrement(mouseX + xPos - CANVAS_WIDTH/2)), (int)Math.floor(decrement(mouseY + yPos - CANVAS_HEIGHT/2)), -1);
		level.addEntity(itemDrop);
	}
	
	public void blockSet()
	{
		for(int i = 0; i < level.getEntityLength(); i++)
		{
			if(level.getEntity(i).getEntityType() != "Item")
			{
				checkNumber++;
				if(!(currentTile.x >= level.getEntity(i).getTileX() && currentTile.x <= level.getEntity(i).getTileLastX() && currentTile.y >= level.getEntity(i).getTileY() && currentTile.y <= level.getEntity(i).getTileLastY()))
				{
					entityCheck++;
				}
			}
			
		}
		if(entityCheck == checkNumber) 
		{
			timer = 0;
			level.setTileS((int)Math.floor(decrement(mouseX + xPos - CANVAS_WIDTH/2)), (int)Math.floor(decrement(mouseY + yPos - CANVAS_HEIGHT/2)), 0);
		}
		entityCheck = 0;
		checkNumber = 0;
	}
	
	public void levelMove()
	{
		if(leftKeyDown == true)
		{
			player.setVelX((int) (player.getVelX() - runRate));
			if(getStateX() == Wallx.NEITHER || (getStateX() == Wallx.RIGHT && (player.getX() + player.getVelX()) <= increment(level.getLevelWidth()) - CANVAS_WIDTH/2))
			{
				xVel -= runRate;
			}
		}
		else
		{
			if(getStateX() == Wallx.RIGHT && (player.getX() + player.getVelX()) <= increment(level.getLevelWidth()) - CANVAS_WIDTH/2)
			{
				xVel = (int) player.getVelX();
			}
		}
		
		if(rightKeyDown == true)
		{
			player.setVelX((int) (player.getVelX() + runRate));
			if(getStateX() == Wallx.NEITHER || (getStateX() == Wallx.LEFT && (player.getX() + player.getVelX()) >= CANVAS_WIDTH/2))
			{
				xVel += runRate;
			}
		}
		else
		{
			if(getStateX() == Wallx.LEFT && (player.getX() + player.getVelX()) >= CANVAS_WIDTH/2)
			{
				xVel = (int) player.getVelX();
			}
		}
		if(leftKeyDown == false && xVel < 0)
		{
			xVel += runRate;
		}
		else if(rightKeyDown == false && xVel > 0)
		{
			xVel -= runRate;
		}
		if(leftKeyDown == false && player.getVelX() < 0)
		{
			player.setVelX(player.getVelX() + runRate);
		}
		else if(rightKeyDown == false && player.getVelX() > 0)
		{
			player.setVelX(player.getVelX() - runRate);
		}
		
		if(xVel >= xMaxVel)
		{
			xVel = xMaxVel;
		}
		else if(xVel <= -xMaxVel)
		{
			xVel = -xMaxVel;
		}
		
		if(upKeyDown == true)
		{
			player.setVelY(player.getVelY() - runRate);
			if(getStateY() == Wally.NEITHER || (getStateY() == Wally.DOWN && (player.getY() + player.getVelY()) <= increment(level.getLevelHeight()) - CANVAS_HEIGHT/2))
			{
				yVel -= runRate;
			}
		}
		else
		{
			if(getStateY() == Wally.DOWN && (player.getY() + player.getVelY()) <= increment(level.getLevelHeight()) - CANVAS_HEIGHT/2)
			{
				yVel = (int) player.getVelY();
			}
		}
		if(downKeyDown == true)
		{
			player.setVelY(player.getVelY() + runRate);
			if(getStateY() == Wally.NEITHER || (getStateY() == Wally.UP && player.getY() >= CANVAS_HEIGHT/2))
			{
				yVel += runRate;
			}
		}
		else
		{
			if(getStateY() == Wally.UP && player.getY() >= CANVAS_HEIGHT/2)
			{
				yVel = (int) player.getVelY();
			}
		}
		
		if(upKeyDown == false && yVel < 0)
		{
			yVel += runRate;
		}
		else if(downKeyDown == false && yVel > 0)
		{
			yVel -= runRate;
		}
		if(upKeyDown == false && player.getVelY() < 0)
		{
			player.setVelY(player.getVelY() + runRate);
		}
		else if(downKeyDown == false && player.getVelY() > 0)
		{
			player.setVelY(player.getVelY() - runRate);
		}
		
		if(yVel >= yMaxVel)
		{
			yVel = yMaxVel;
		}
		else if(yVel <= -yMaxVel)
		{
			yVel = -yMaxVel;
		}
	}
	
	public void entitiesMove()
	{
		if(wKeyDown == true)
		{
			//c1.subVelY();
		}
		else if(sKeyDown == true)
		{
			//c1.addVelY();
		}
		else
		{
			//c1.noY();
		}
		if(aKeyDown == true)
		{
			c1.subVelX();
		}
		else if(dKeyDown == true)
		{
			c1.addVelX();
		}
		else
		{
			c1.noX();
		}
		if(spaceKeyDown == true)
		{
			if(c1.getGround() == true)
			{
				c1.setVelY(-(c1.getJumpHeight()));
			}
		}
		for(int i = 0; i < level.getEntityLength(); i++)
		{
			level.getEntity(i).addGravity();
			level.getEntity(i).limitVel();
		}
	}
	
	public void resolveCollisions()
		{
			for(int i = 0; i < level.getEntityLength(); i++)
			{
				if(level.getEntity(i).getCollide() == true)
				{
					int widthX = (int)increment(level.getEntity(i).getEntityWidth());
					int heightY = (int)increment(level.getEntity(i).getEntityHeight());
					
					int entityLeftX = level.getEntity(i).getX();
					int entityRightX = level.getEntity(i).getX() + widthX;
					int entityUpY = level.getEntity(i).getY();
					int entityDownY = level.getEntity(i).getY() + heightY;
					
					int entityVelX = (int) level.getEntity(i).getVelX();
					int entityVelY = (int) level.getEntity(i).getVelY();
					
					int entityLeftTile = (int) Math.floor(decrement(entityLeftX)) - 1;
					int entityRightTile = (int) Math.floor(decrement(entityLeftX + widthX));
					int entityRightTileX = (int)increment((double)entityRightTile);
					int entityUpTile = (int) Math.floor(decrement(entityUpY)) - 1;
					int entityUpTileNextFrame = (int) Math.floor(decrement(entityUpY + entityVelY)) - 1;
					int entityDownTile = (int) Math.floor(decrement(entityUpY + heightY));
					int entityDownTileY = (int)increment((double)entityDownTile);
					
					int entityTile1 = (int) Math.floor(decrement(entityLeftX));
					int entityTile1NextFrame = (int) Math.floor(decrement(entityLeftX + entityVelX));
					int entityTileX = (int) increment(Math.floor(decrement(entityLeftX)));
					int entityTileRight = (int) Math.floor(decrement(entityLeftX + widthX - .1));
					int entityTileRightX = (int) increment(Math.floor(decrement(entityLeftX + widthX)));
					int entityTileRight2X = (int) increment(Math.floor(decrement(entityLeftX + widthX)) + 1);
					
					int entityTile2 = (int) Math.floor(decrement(entityUpY));
					int entityTileY = (int) increment(Math.floor(decrement(entityUpY)));
					int entityTileDown = (int) Math.floor(decrement(entityUpY + heightY - .1));
					int entityTileDownY = (int )increment(Math.floor(decrement(entityUpY + heightY)));
					int entityTileDown2Y = (int )increment(Math.floor(decrement(entityUpY + heightY)) + 1);
					
					int entityNextFrameLeftX = (int) entityVelX + entityLeftX;
					int entityNextFrameRightX = (int) (entityLeftX + level.getEntity(i).getVelX() + widthX);
					int entityNextFrameUpY = (int) entityVelY + entityUpY;
					int entityNextFrameDownY = (int) (entityUpY + level.getEntity(i).getVelY() + heightY);
					
					
					//Left//
					
					//System.out.println("Left: " + entityLeftTile);
					//System.out.println("Down: " + entityTileDownY);
					
					if(entityTileX > entityNextFrameLeftX)
					{
						if(level.getTile(entityLeftTile, entityTile2) == 0 || level.getTile(entityLeftTile, entityTileDown) == 0)
						{
							level.getEntity(i).setVelX(-(entityLeftX - entityTileX));
						}
					}
					
					for(int j = 1; j < Math.floor(level.getEntity(i).getEntityHeight() - .1) + 1; j++)
					{
						if(entityTileX > entityNextFrameLeftX)
						{
							if(level.getTile(entityLeftTile, entityTile2 + j) == 0)
							{
								level.getEntity(i).setVelX(-(entityLeftX - entityTileX));
							}
						}
					}
					
					//Right//
					
					if(entityTileRightX < entityNextFrameRightX)
					{
						if((level.getTile(entityRightTile, entityTile2) == 0 || level.getTile(entityRightTile, entityTileDown) == 0) && level.getEntity(i).getVelX() > 0)
						{
							level.getEntity(i).setVelX(entityRightTileX - entityRightX);
						}
					}
					if(entityTileRight2X < entityNextFrameRightX)
					{
						if(level.getTile(entityTileRight + 1, entityTile2) == 0 || level.getTile(entityTileRight + 1, entityDownTile) == 0)
						{
							level.getEntity(i).setVelX(entityTileRight2X - entityRightX);
						}
					}
					
					//Points placed at tile lengths to ensure entire entity is hit tested//
					
					for(int j = 1; j < Math.floor(level.getEntity(i).getEntityHeight() - .1) + 1; j++)
					{
						if(entityTileRightX < entityNextFrameRightX)
						{
							if(level.getTile(entityRightTile, entityTile2 + j) == 0)
							{
								level.getEntity(i).setVelX(entityRightTileX - entityRightX);
							}
						}
						if(entityTileRight2X < entityNextFrameRightX)
						{
							if(level.getTile(entityTileRight + 1, entityTile2 + j) == 0)
							{
								level.getEntity(i).setVelX(entityTileRight2X - entityRightX);
							}
						}
					}
					
					//Up//
					
					//System.out.println("current: " + entityUpTile);
					//System.out.println("next: " + entityUpTileNextFrame);
					
					
					if(entityTileY > entityNextFrameUpY)
					{
						if(level.getTile(entityTile1, entityUpTile) == 0 || level.getTile(entityTileRight, entityUpTile) == 0)
						{
							level.getEntity(i).setVelY(-(entityUpY - entityTileY));
						}
					}
					
					if(level.getTile(entityTile1NextFrame, entityUpTileNextFrame) == 0)
					{
						//level.getEntity(i).setVelY(-(entityUpY - entityTileYNextFrame));
					}
					
				
					
					for(int j = 1; j < Math.floor(level.getEntity(i).getEntityWidth() - .1) + 1; j++)
					{
						if(entityTileY > entityNextFrameUpY)
						{
							if(level.getTile(entityTile1 + j, entityUpTile) == 0)
							{
								level.getEntity(i).setVelY(-(entityUpY - entityTileY));
							}
						}
					}
					
					//Down//
					
					//System.out.println("Up: " + entityTileY);
					//System.out.println("Down: " + entityTileDownY);
					
					if(entityTileDownY < entityNextFrameDownY)
					{
						if(level.getTile(entityTile1, entityDownTile) == 0 || level.getTile(entityTileRight, entityDownTile) == 0)
						{
							level.getEntity(i).setVelY(entityDownTileY - entityDownY);
							level.getEntity(i).setGround(true);					
						}
						else
						{
							level.getEntity(i).setGround(false);
						}
					}
					else
					{
						level.getEntity(i).setGround(false);
					}
					if(entityTileDown2Y < entityNextFrameDownY)
					{
						if(level.getTile(entityTile1, entityTileDown + 1) == 0 || level.getTile(entityRightTile, entityTileDown + 1) == 0)
						{
							level.getEntity(i).setVelY(entityTileDown2Y - entityDownY);
						}
					}
					
					//Points placed at tile lengths to ensure entire entity is hit tested//
					
					for(int j = 1; j < Math.floor(level.getEntity(i).getEntityWidth() - .1) + 1; j++)
					{
						if(entityTileDownY < entityNextFrameDownY)
						{
							if(level.getTile(entityTile1 + j, entityDownTile) == 0)
							{
								level.getEntity(i).setVelY(entityDownTileY - entityDownY);
								level.getEntity(i).setGround(true);					
							}
							else
							{
								//level.getEntity(i).setGround(false);
							}
						}
						else
						{
							level.getEntity(i).setGround(false);
						}
						if(entityTileDown2Y < entityNextFrameDownY)
						{
							if(level.getTile(entityTile1 + j, entityTileDown + 1) == 0)
							{
								level.getEntity(i).setVelY(entityTileDown2Y - entityDownY);
							}
						}
					}
				}
			}
		}
	
	public void updateEntities()
	{
		for(int i = 0; i < level.getEntityLength(); i++)
		{
			level.getEntity(i).update();
		}
		xPos += xVel;
		yPos += yVel;
	}
	
	public void restrictLevelWalls()
	{
		if(xPos >= (increment(level.getLevelWidth()) - (CANVAS_WIDTH/2)))
		{
			xPos = (int) (increment(level.getLevelWidth()) - (CANVAS_WIDTH/2));
		}
		if(yPos >= (increment(level.getLevelHeight()) - (CANVAS_HEIGHT/2)))
		{
			yPos = (int) (increment(level.getLevelHeight()) - (CANVAS_HEIGHT/2));
		}
		if(xPos <= (CANVAS_WIDTH/2))
		{
			xPos = (int) (CANVAS_WIDTH/2);
			xVel = 0;
		}
		if(yPos <= (CANVAS_HEIGHT/2))
		{
			yPos = (int) (CANVAS_HEIGHT/2);
			yVel = 0;
		}
		
		if(player.getX() <= 0)
		{
			player.setX(0);
			player.setVelX(0);
		}
		if((player.getX() + Math.ceil(increment(player.getEntityWidth()))) >= increment(level.getLevelWidth()))
		{
			player.setX((int) (increment(level.getLevelWidth()) - Math.ceil(increment(player.getEntityWidth()))));
		}
		if(player.getY() <= 0)
		{
			player.setY(0);
			player.setVelY(0);
		}
		if((player.getY() + Math.ceil(increment(player.getEntityHeight()))) >= increment(level.getLevelHeight()))
		{
			player.setY((int) (increment(level.getLevelHeight()) - Math.ceil(increment(player.getEntityHeight()))));
		}
	}
	
	public void updateLight()
	{
		//
	}
	
	public void switchStateX(Wallx tempState)
	{
		if(wallx != tempState)
		{
			if(tempState == Wallx.NEITHER && getStateX() == Wallx.LEFT || tempState == Wallx.NEITHER && getStateX() == Wallx.RIGHT)
			{
				player.setX(xPos);
				xVel = (int) player.getVelX();
			}
			wallx = tempState;
		}
	}
	public void switchStateY(Wally tempState)
	{
		if(wally != tempState)
		{
			if(tempState == Wally.NEITHER && getStateY() == Wally.UP || tempState == Wally.NEITHER && getStateY() == Wally.DOWN)
			{
				player.setY(yPos);
				yVel = (int) player.getVelY();
			}
			wally = tempState;
		}
	}
	
	public Wallx getStateX()
	{
		return wallx;
	}
	
	public Wally getStateY()
	{
		return wally;
	}
	
	// Refresh the display. Called back via repaint(), which invoke the paintComponent().
   	private void gameDraw(Graphics2D g2d) 
   	{
   		switch (state) 
   		{
   			case INITIALIZED:
   		   		//
   				break;
   			case PLAYING:
   				if(player.getX() < CANVAS_WIDTH/2)
   				{
   					switchStateX(Wallx.LEFT);
   					
   					xStart = 0;
   					xFinal = (int)Math.round(decrement(CANVAS_WIDTH)) + 1;
   					xOffset = 0;
   				}
   				
   				else if(player.getX() > (increment(level.getLevelWidth())) - CANVAS_WIDTH/2)
   				{
   					switchStateX(Wallx.RIGHT);
   					
   					xStart = (int)(decrement((xPos)) - (Math.round((decrement(CANVAS_WIDTH))/2) + 1));
   					xFinal = level.getLevelWidth();
   					xOffset = blockSize;
   				}
   				else
   				{
   					switchStateX(Wallx.NEITHER);
   					
   					xStart = (int)(decrement((xPos)) - (Math.round((decrement(CANVAS_WIDTH))/2)));
   					xFinal = (int)(decrement((xPos)) + (Math.round((decrement(CANVAS_WIDTH))/2) + 1));
   					if(xFinal > level.getLevelWidth())
   					{
   						xFinal = level.getLevelWidth();
   					}
   					xOffset = (xPos % blockSize);
   				}
   				
   				
   				if(player.getY() < CANVAS_HEIGHT/2)
   				{
   					switchStateY(Wally.UP);
   					
   					yStart = 0;
   					yFinal = (int)Math.round(decrement(CANVAS_HEIGHT)) + 1;
   					yOffset = 0;
   				}
   				else if(player.getY() > (increment(level.getLevelHeight())) - CANVAS_HEIGHT/2)
   				{
   					switchStateY(Wally.DOWN);
   					
   					yStart = (int)(decrement((yPos)) - (Math.round((decrement(CANVAS_HEIGHT))/2) + 1));
   					yFinal = level.getLevelHeight();
   					yOffset = blockSize;
   				}
   				else
   				{
   					switchStateY(Wally.NEITHER);
   					
   					yStart = (int)(decrement((yPos)) - (Math.round((decrement(CANVAS_HEIGHT))/2)));
   					yFinal = (int)(decrement((yPos)) + (Math.round((decrement(CANVAS_HEIGHT))/2) + 1));
   					if(yFinal > level.getLevelHeight())
   					{
   						yFinal = level.getLevelHeight();
   					}
   					yOffset = (yPos % blockSize);
   				}
   				
   				//Render Tiles//
   				
   				for(int i = yStart; i < yFinal; i++)
   				{
   					for(int j = xStart; j < (xFinal); j++)
   					{
   						try
   			   			{
   							if(!(level.getTile(j, i) == -1))
   							{
   								g2d.drawImage(getImage(level.getTile(j, i)), (int)increment(j - xStart) - xOffset, (int)increment(i - yStart) - yOffset, blockSize, blockSize, this);
   							}
   						    //g2d.fillRect((int)increment(j - xStart) - xOffset, (int)increment(i - yStart) - yOffset, blockSize, blockSize);
   			   			}
   			   			catch (Exception e) 
   			   			{
   			   				e.printStackTrace();
   			   			}
   					}
   				}
   				
   				//Render Items//
   				
   				
   				
   				
   				//Render Entities (Except player)//
   				
   				for(int i = 1; i < level.getEntityLength(); i++)
   				{
   					g2d.drawImage(getImage(level.getEntity(i).getImageType()), (level.getEntity(i).getX() - xPos) + CANVAS_WIDTH/2, (level.getEntity(i).getY() - yPos) + CANVAS_HEIGHT/2, (int)increment(level.getEntity(i).getEntityWidth()), (int)increment(level.getEntity(i).getEntityHeight()), this);
   				}
   				
   				//Render Player//
   				
   				g2d.drawImage(getImage(1), (player.getX() - xPos) + CANVAS_WIDTH/2, (player.getY() - yPos) + CANVAS_HEIGHT/2, (int)increment(player.getEntityWidth()), (int)increment(player.getEntityHeight()), this);
   				break;
   			case PAUSED:
   				// ......
   				break;
   			case DESTROYED:
   				// ......
   				break;
   			case GAMEOVER:
   				// ......
   				break;
   		}
   	}
   	
   	public void mousePressed()
	{
		clickedTile.x = (int)Math.floor(decrement(mouseX + xPos - CANVAS_WIDTH/2));
		clickedTile.y = (int)Math.floor(decrement(mouseY + yPos - CANVAS_HEIGHT/2));
	}
   	
   	public void gameMouseReleased(MouseEvent e) 
   	{
   		if (e.getButton() == MouseEvent.NOBUTTON)
        {
   			//
        }
   		else if (e.getButton() == MouseEvent.BUTTON1) 
   		{
   			leftClickDown = false;
        }
   		else if (e.getButton() == MouseEvent.BUTTON2) 
   		{
   			scrollClickDown = false;
        }
   		else if (e.getButton() == MouseEvent.BUTTON3) 
   		{
   			rightClickDown = false;
   		}
   	}
   	public void gameMousePressed(MouseEvent e) 
   	{
   		if (e.getButton() == MouseEvent.NOBUTTON)
        {
   			//
        }
   		else if (e.getButton() == MouseEvent.BUTTON1) 
   		{
   			leftClickDown = true;
   			mousePressed();
        }
   		else if (e.getButton() == MouseEvent.BUTTON2) 
   		{
   			scrollClickDown = true;
        }
   		else if (e.getButton() == MouseEvent.BUTTON3) 
   		{
   			rightClickDown = true;
   		}
   	}
   	public void gameMouseMoved(MouseEvent e) 
   	{
   		mouseX = e.getX();
   		mouseY = e.getY();
   	}
   	public void gameMouseDragged(MouseEvent e) 
   	{
   		mouseX = e.getX();
   		mouseY = e.getY();
   	}
   	
   	
   	
   	// Process a key-pressed event. Update the current state.
   	public void gameKeyPressed(int keyCode)
   	{
   		keyDown = true;
   		switch (keyCode)
   		{
   			case KeyEvent.VK_UP:
   				upKeyDown = true;
   				break;
   			case KeyEvent.VK_DOWN:
   				downKeyDown = true;
   				break;
   			case KeyEvent.VK_LEFT:
   				leftKeyDown = true;
   				break;
   			case KeyEvent.VK_RIGHT:
   				rightKeyDown = true;
   				break;
   			case KeyEvent.VK_W:
   				wKeyDown = true;
   				break;
   			case KeyEvent.VK_A:
   				aKeyDown = true;
   				break;
   			case KeyEvent.VK_S:
   				sKeyDown = true;
   				break;
   			case KeyEvent.VK_D:
   				dKeyDown = true;
   				break;
   			case KeyEvent.VK_SPACE:
   				spaceKeyDown = true;
   				break;
   		}
   	}
   
   	// Process a key-released event.
   	public void gameKeyReleased(int keyCode) 
   	{
   		keyDown = false;
   		switch (keyCode)
   		{
   			case KeyEvent.VK_UP:
   				upKeyDown = false;
   				break;
   			case KeyEvent.VK_DOWN:
   				downKeyDown = false;
   				break;
   			case KeyEvent.VK_LEFT:
   				leftKeyDown = false;
   				break;
   			case KeyEvent.VK_RIGHT:
   				rightKeyDown = false;
   				break;
   			case KeyEvent.VK_W:
   				wKeyDown = false;
   				break;
   			case KeyEvent.VK_A:
   				aKeyDown = false;
   				break;
   			case KeyEvent.VK_S:
   				sKeyDown = false;
   				break;
   			case KeyEvent.VK_D:
   				dKeyDown = false;
   				break;
   			case KeyEvent.VK_SPACE:
   				spaceKeyDown = false;
   				break;
   		}
   	}
   
   	// Process a key-typed event.
   	public void gameKeyTyped(char keyChar) 
   	{
	   
   	}
   	
   	// Custom drawing panel, written as an inner class.
   	class GameCanvas extends JPanel implements KeyListener, MouseListener, MouseMotionListener
   	{
   		boolean paintAll;
   		int xPos, yPos;
   		Image img;
   		
   		// Constructor
   		public GameCanvas() 
   		{
   			setFocusable(true);  // so that can receive key-events
   			requestFocus();
   			addKeyListener(this);
   			addMouseListener(this);
   			addMouseMotionListener(this);
   		}
   
   		// Override paintComponent to do custom drawing.
   		// Called back by repaint().
   		@Override
   		public void paintComponent(Graphics g) 
   		{
   			Graphics2D g2d = (Graphics2D)g;
   			super.paintComponent(g2d);   // paint background
   	   		setBackground(Color.BLACK);  // may use an image for background
   	   
   	   		// Draw the game objects
   	   		gameDraw(g2d);
   		}

   		@Override
   		public void mouseDragged(MouseEvent e)
   		{
   			gameMouseDragged(e);
   		}
   		
   		@Override
   		public void mouseMoved(MouseEvent e)
   		{
   			gameMouseMoved(e);
   		}
   		
   		@Override
   		public void mouseEntered(MouseEvent e)
   		{
   			//gameMouseEntered(e);
   		}
   		
   		@Override
   		public void mouseExited(MouseEvent e)
   		{
   			//gameMouseExited(e);
   		}
   		
   		@Override
   		public void mouseClicked(MouseEvent e)
   		{
   			//gameMouseClicked(e);
   		}
   		
   		@Override
   		public void mousePressed(MouseEvent e)
   		{
   			gameMousePressed(e);
   		}
   		
   		@Override
   		public void mouseReleased(MouseEvent e)
   		{
   			gameMouseReleased(e);
   		}
   		
   		// KeyEvent handlers
   		@Override
   		public void keyPressed(KeyEvent e) 
   		{
   			gameKeyPressed(e.getKeyCode());
   		}
      
   		@Override
   		public void keyReleased(KeyEvent e) 
   		{
   			gameKeyReleased(e.getKeyCode());
   		}
   
   		@Override
   		public void keyTyped(KeyEvent e) 
   		{
   			gameKeyTyped(e.getKeyChar());
   		}
   	}
   	
    // To start and re-start the game.
	public void gameStart() 
	{ 
		// Create a new thread
		Thread gameThread =  new Thread() 
		{
			// Override run() to provide the running behavior of this thread.
			@Override
			public void run() 
			{
				gameLoop();
			}
		};
		Thread workerLoadThread =  new Thread() 
		{
			// Override run() to provide the running behavior of this thread.
			@Override
			public void run() 
			{
				loadImages();
			}
		};
		// Start the thread. start() calls run(), which in turn calls gameLoop().
		workerLoadThread.start();
		try
		{
			workerLoadThread.join();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		gameThread.start();
	}
	
	
	
   	// main
   	public static void main(String[] args) 
   	{
   		// Use the event dispatch thread to build the UI for thread-safety.
   		SwingUtilities.invokeLater(new Runnable() 
   		{
   			@Override
   			public void run() 
   			{
   				new GameMain();
   			}
   		});
   	}
}
