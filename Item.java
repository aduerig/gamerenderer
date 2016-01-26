package engine;

public class Item extends AbstractEntity
{
	public Item(int xTemp, int yTemp, double widthTemp, double heightTemp, int velLimitX, int velLimitY, boolean collide, int gravityTemp, int typeTemp)
	{
		super(xTemp, yTemp, widthTemp, heightTemp, velLimitX, velLimitY, collide, gravityTemp, typeTemp);
	}
	
	@Override
	public String getEntityType()
	{
		return "Item";
	}
}
