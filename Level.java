package engine;

import java.util.*;

public class Level 
{
	private double renderLength;
	private int fractalLength, numSections, timesFractal, fractalPoints, randomInit, fractalIteration, randomTemp, lastSectPos, lengthFractal, numBlocks;
	private ArrayList<int[]> sections;
	
	
	
	private int height, width, level[][], entityLength, indexTemp;
	private ArrayList<AbstractEntity> entityList;
	private ArrayList<AbstractEntity> entityLoadedList;
	private PerlinFractal perlin;
	private boolean a;
	BlockType blockType;
	
	public Level(int widthTemp, int heightTemp)
	{
		System.out.println("lol");
		perlin = new PerlinFractal();
		height = heightTemp;
		width = widthTemp;
		level = perlin.PerlinGen(widthTemp, heightTemp);
		
		entityList = new ArrayList<AbstractEntity>();
		entityLoadedList = new ArrayList<AbstractEntity>();
		indexTemp = 0;
		a = false;
		blockType = new BlockType();
		
		lastSectPos = 0;
		sections = new ArrayList<int[]>();
		randomInit = 300;
		
		/*lengthFractal = 100;
		timesFractal = findFractalNum(lengthFractal);
		numSections = (int)Math.ceil((double)width/(double)lengthFractal);
		numBlocks = numSections * lengthFractal;
		System.out.print(lengthFractal + " lengthFractal ");
		System.out.print(timesFractal + " fractalTimes ");
		System.out.print(numBlocks + " numBlocks ");
		System.out.print(numSections + " numSections");
		System.out.println();
		
		fractalPoints = numBlocks;
		fractalIteration = 3;
		
		for(int i = 0; i < numSections; i++)
		{
			sections.add(new int[fractalPoints]);
		}
		fractalLength = (fractalPoints - 1)/2;
		fractalLength /= 2;
		renderLength = numBlocks/fractalPoints;*/
	}
	
	public Level(int levelTemp[][])
	{
		width = levelTemp.length;
		height = levelTemp[0].length;
		level = levelTemp;
		entityList = new ArrayList<AbstractEntity>();
		entityLoadedList = new ArrayList<AbstractEntity>();
		indexTemp = 0;
		a = false;
		blockType = new BlockType();
	}
	
	
	public int findFractalNum(int block) 
	{
		int i = 3;
		int j = 0;
		while(true)
		{
			i = (i*2) -1;
			
			j++;
			if(i >= block)
			{
				lengthFractal = i;
				return j;
			}
		}
	}
	
	public void initSections()
	{
		for(int i = 0; i < numSections; i++)
		{
			sections.get(i)[0] = lastSectPos;
			sections.get(i)[(sections.get(i).length - 1)/2] = ((int) (Math.random() * randomInit)) + 80;
			randomTemp = sections.get(i)[(sections.get(i).length - 1)/2];
			sections.get(i)[sections.get(i).length - 1] = (int) (Math.random() * randomTemp/2);
			lastSectPos = sections.get(i)[sections.get(i).length - 1];
		}
	}
	
	public void fractal()
	{
		if(fractalLength > 0)
		{
			int temp = fractalIteration;
			for(int i = 0; i < numSections; i++)
			{
				fractalIteration = temp;
				for(int j = 1; j < fractalIteration; j += 2)
				{
					randomTemp = (sections.get(i)[((j - 1) * fractalLength)] + sections.get(i)[((j + 1) * fractalLength)])/2;
					sections.get(i)[(j * fractalLength)] = (int) ((Math.random() * randomTemp) + (randomTemp/4));
					fractalIteration += 1;
				}
			}
			fractalLength /= 2;
		}
	}
	
	public void generateLevel()
	{
		for(int i = 0; i < width; i++)
		{
			for(int j = 0; j < height/2; j++)
			{
				level[i][j] = -1;
			}
		}
		for(int i = 0; i < width; i++)
		{
			for(int j = height/2; j < height; j++)
			{
				level[i][j] = 0;
			}
		}
	}
	
	public void generateLight()
	{
		for(int i = 0; i < width; i++)
		{
			for(int j = 0; j < height; j++)
			{
				level[i][j] = 125;
			}
		}
	}
	
	public int[][] getLevel()
	{
		return level;
	}
	
	public int getTile(int xTemp, int yTemp)
	{
		return level[xTemp][yTemp];
	}
	
	public int getTileS(int xTemp, int yTemp)
	{
		if(xTemp > width - 1 || xTemp < 0 || yTemp > height -1 || yTemp < 0)
		{
			System.out.println("ERROR - Out of Bounds of Level: Could not get tile at: " + xTemp + ", " + yTemp);
			return 0;
		}
		return level[xTemp][yTemp];
	}

	public void setTile(int xTemp, int yTemp, int setTemp)
	{
		level[xTemp][yTemp] = setTemp;
	}
	
	public void setTileS(int xTemp, int yTemp, int setTemp)
	{
		if(xTemp > width - 1 || xTemp < 0 || yTemp > height - 1 || yTemp < 0)
		{
			System.out.println("ERROR - Out of Bounds of Level: Could not set tile at: " + xTemp + ", " + yTemp);
			return;
		}
		level[xTemp][yTemp] = setTemp;
	}
	
	public boolean getCollidableTile(int blockTemp)
	{
		a = blockType.checkCollidable(blockTemp);
		return a;
	}
	
	public int getLevelHeight()
	{
		return height;
	}
	
	public int getLevelWidth()
	{
		return width;
	}
	
	public int getEntityLength()
	{
		return entityList.size();
	}
	
	public void setEntityLength(int entityLengthTemp)
	{
		entityList.ensureCapacity(entityLengthTemp);
	}
	
	public void addEntity(AbstractEntity entityTemp)
	{
		entityList.add(entityTemp);
	}
	
	public void removeEntity(AbstractEntity entityTemp)
	{
		indexTemp = 0;
		for(int i = 0; i < entityList.size(); i++)
		{
			if(entityList.get(i).equals(entityTemp))
			{
				entityList.remove(i);
			}
		}
	}
	
	public List<AbstractEntity> getEntities()
	{
		return entityList;
	}
	
	public AbstractEntity getEntity(int tempIndex)
	{
		return entityList.get(tempIndex);
	}
	
}
//test