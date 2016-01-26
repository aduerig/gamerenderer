package engine;

import javax.swing.JFrame;

public class AbstractEntity extends JFrame
{
	private int x, y, runRate, velLimitX, velLimitY, gravity, jumpHeight, type;
	private double velX, velY, width, height;
	private boolean collide, ground;
	public AbstractEntity(int xTemp, int yTemp, double widthTemp, double heightTemp, int velLimitXTemp, int velLimitYTemp, boolean collideTemp, int gravityTemp, int typeTemp)
	{
		x = xTemp;
		y = yTemp;
		width = widthTemp;
		height = heightTemp;
		velX = 0;
		velY = 0;
		velLimitX = velLimitXTemp;
		velLimitY = velLimitYTemp;
		runRate = 1;
		collide = collideTemp;
		gravity = gravityTemp;
		jumpHeight = 15;
		type = typeTemp;
	}
	
	public String getEntityType()
	{
		return "AbstractEntity";
	}
	
	public void setBlockType(int typeTemp)
	{
		type = typeTemp;
	}
	
	public int getImageType()
	{
		return type;
	}
	
	public boolean getCollide()
	{
		return collide;
	}
	
	public void setCollide(boolean collideTemp)
	{
		collide = collideTemp;
	}
	
	public boolean getGround()
	{
		return ground;
	}
	
	public void setGround(boolean groundTemp)
	{
		ground = groundTemp;
	}
	
	public int getJumpHeight()
	{
		return jumpHeight;
	}
	
	public void setJumpHeight(int jumpHeightTemp)
	{
		jumpHeight = jumpHeightTemp;
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
	
	public int getTileX()
	{
		return (int) GameMain.decrement(x);
	}
	
	public int getTileY()
	{
		return (int) GameMain.decrement(y);
	}
	
	public int getTileLastX()
	{
		return (int) GameMain.decrement(x + GameMain.increment(width - .01));
	}
	
	public int getTileLastY()
	{
		return (int) GameMain.decrement(y + GameMain.increment(height - .01));
	}
	
	public double getEntityWidth()
	{
		return width;
	}
	
	public double getEntityHeight()
	{
		return height;
	}
	
	public double getVelX()
	{
		return velX;
	}
	
	public double getVelY()
	{
		return velY;
	}
	
	public void setX(int xTemp)
	{
		x = xTemp;
	}
	
	public void setY(int yTemp)
	{
		y = yTemp;
	}
	
	public void addVelX()
	{
		velX += runRate;
	}
	
	public void subVelX()
	{
		velX -= runRate;
	}
	
	public void noX()
	{
		if(velX > 0)
		{
			velX -= runRate;
		}
		else if(velX < 0)
		{
			velX += runRate;
		}
	}
	
	public void addVelY()
	{
		velY += runRate;
	}
	
	public void subVelY()
	{
		velY -= runRate;
	}
	
	public void noY()
	{
		if(velY > 0)
		{
			velY -= runRate;
		}
		else if(velY < 0)
		{
			velY += runRate;
		}
	}
	
	public void setWidth(int widthTemp)
	{
		width = widthTemp;
	}
	
	public void setHeight(int heightTemp)
	{
		height = heightTemp;
	}
	
	public void setVelX(double tempVelX)
	{
		velX = tempVelX;
	}
	
	public void setVelY(double tempVelY)
	{
		velY = tempVelY;
	}
	
	public void limitVel()
	{
		if(velX >= velLimitX)
		{
			velX = velLimitX;
		}
		if(velX <= -velLimitX)
		{
			velX = -velLimitX;
		}
		if(velY >= velLimitY)
		{
			velY = velLimitY;
		}
		if(velY <= -velLimitY)
		{
			velY = -velLimitY;
		}
		
		if(getEntityType() == "Item")
		{
			if(velX > 0)
			{
				velX -= runRate;
			}
			else if(velX < 0)
			{
				velX += runRate;
			}
		}
	}
	
	public void addGravity()
	{
		setVelY(getVelY() + gravity);
	}
	
	public void update()
	{
		setX(getX() + (int)velX);
		setY(getY() + (int)velY);
	}
	
	public void initEntity()
	{
		//add entity to field
		//getX(); getY()
	}
}
