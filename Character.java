package engine;

public class Character extends AbstractEntity
{
	public Character(int xTemp, int yTemp, double widthTemp, double heightTemp, int velLimitX, int velLimitY, boolean collide, int gravityTemp, int typeTemp)
	{
		super(xTemp, yTemp, widthTemp, heightTemp, velLimitX, velLimitY, collide, gravityTemp, typeTemp);
	}
	
	@Override
	public String getEntityType()
	{
		return "Character";
	}
}
