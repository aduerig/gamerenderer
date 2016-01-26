package engine;

public class Player extends AbstractEntity
{
	public Player(int xTemp, int yTemp, double widthTemp, double heightTemp, int velLimitX, int velLimitY, boolean collide, int gravityTemp, int typeTemp)
	{
		super(xTemp, yTemp, widthTemp, heightTemp, velLimitX, velLimitY, collide, gravityTemp, typeTemp);
	}
	
	@Override
	public String getEntityType()
	{
		return "Player";
	}
}