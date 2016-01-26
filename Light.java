package engine;
import javax.swing.JFrame;

public class Light extends JFrame
{
	private int x, y;
	private double width, height;
	public Light(int xTemp, int yTemp, double widthTemp, double heightTemp)
	{
		x = xTemp;
		y = yTemp;
		width = widthTemp;
		height = heightTemp;
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
	
	public void setX(int xTemp)
	{
		x = xTemp;
	}
	
	public void setY(int yTemp)
	{
		y = yTemp;
	}
	public void update()
	{
		//
	}
}
