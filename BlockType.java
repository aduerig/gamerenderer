package engine;

public class BlockType 
{
	private int[] collidable = new int[]
	{
		0, 1
	};
	
	private int[] hardness = new int[]
	{
		0, 20, 100, 100, 100, 100
	};
	
	public void BlockType()
	{
		//
	}
	
	public boolean checkCollidable(int blockTemp)
	{
		for(int i = 0; i < collidable.length; i++)
		{
			System.out.println(i);
			if(blockTemp == collidable[i])
			{
				return true;
			}
		}
		return false;
	}
	
	public int getHardness(int blockTemp)
	{
		return hardness[blockTemp+1];
	}
}
