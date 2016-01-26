package engine;

import java.awt.*;

import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.*;

public class PerlinFractal
{
	private boolean keyDown, leftKeyDown, rightKeyDown, upKeyDown, downKeyDown, wKeyDown, aKeyDown, sKeyDown, dKeyDown, spaceKeyDown;
	private int xS, yS, numStepsX, numStepsY, xMin, yMin, noise, fractalSum, valueOffset;
	private int[][] level, levelGrad, fractal, random;
	private int kMaxVertices, xMaxVerticesZoom, yMaxVerticesZoom;
	private double tRemapSmoothstep, xInput, yInput, txDist, tyDist, frequency, amplitude, lacurity, gain, maxSum, turbulence;
	
	public  PerlinFractal()
	{
		System.out.println("lol");
		//initVars();
	}
	
	public void initVars(int width, int length)
	{
		
		
		frequency = 1;
		amplitude = 1;
		turbulence = .50;
		maxSum = 0;
		
		kMaxVertices = 512;
		numStepsX = width;
		numStepsY = length;
		lacurity = .48;
		gain = 2;
		fractalSum = 7;
		
		
		
	    random = new int[kMaxVertices][kMaxVertices];
	    level = new int[numStepsX][numStepsY];
	    levelGrad = new int[numStepsX][numStepsY];
	    fractal = new int[numStepsX][numStepsY];
	    
	    
	}
	
	public int[][] PerlinGen(int width, int length)
	{
		//System.out.println("lol");
		initVars(width, length);
		//System.out.println("lol");
		calcNoise();
	    levelSet();
	   
	    return level;
	}
	
	public void randomInts()
	{
		for(int i = 0; i < kMaxVertices; i++)
		{
			for(int j = 0; j < kMaxVertices; j++)
	    	{
	    		random[i][j] = (int) (Math.random()*255);
	    	}
		}
	}

	public double Smoothstep(double t)
	{
	    tRemapSmoothstep = t * t * ( 3 - 2 * t );
	    return tRemapSmoothstep;
	}
	
	public int Lerp(int a, int b, double t)
	{
	    return (int)(a*(1 - t) + b*t);
	}	
	
	public void calcNoise()
	{
		for(int l = 0; l < fractalSum; l++)
	    {
			randomInts();
			maxSum += amplitude;
			for(int i = 0; i < numStepsX; i++)
		    {
		    	for(int j = 0; j < numStepsY; j++)
			    {
			    	xInput = i / (double)(numStepsX - 1) * (kMaxVertices-1);
			    	yInput = j / (double)(numStepsY - 1) * (kMaxVertices-1);
			    	noise = (int) ((eval(xInput*frequency, yInput*frequency)*amplitude) + valueOffset);
			    	
			    	fractal[i][j] += (int) (noise);
			    }  
		    }
			frequency *= lacurity;
			amplitude *= gain;
	    }
		for(int i = 0; i < numStepsX; i++)
	    {
	    	for(int j = 0; j < numStepsY; j++)
		    {
		    	fractal[i][j] /= (maxSum);
		    }  
	    }
	}
	
	public int eval(double x, double y)
	{
		xMin = (int)x;
		yMin = (int)y;
		txDist = x - xMin;
		tyDist = y - yMin;
		txDist = Smoothstep(txDist);
		tyDist = Smoothstep(tyDist);
		int Lerp1 = Lerp(random[xMin][yMin], random[(xMin + 1)%kMaxVertices][yMin], txDist);
		int Lerp2 = Lerp(random[xMin][(yMin + 1)%kMaxVertices], random[(xMin + 1)%kMaxVertices][(yMin + 1)%kMaxVertices], txDist);
		int Lerp3 = Lerp(Lerp1, Lerp2, tyDist);
		return Lerp3;
	}
	
	public void levelSet()
	{
		for(int i = 0; i < numStepsX; i++)
		{
			for(int j = 0; j < numStepsY; j++)
			{
				//Set top half black, bottom half white//
				if(j < (numStepsY/2 + 1))
				{
					level[i][j] = -1;
				}
				else
				{
					level[i][j] = 0;
				}
			}
		}
		
		for(int i = 0; i < numStepsX; i++)
		{
			for(int j = 0; j < numStepsY; j++)
			{
				//Set top half black, bottom half white//
				if(j < (numStepsY/2 + 1))
				{
					levelGrad[i][j] = -1;
				}
				else
				{
					levelGrad[i][j] = 0;
				}
			}
		}
		
		//Caves(Direct Set)//
		
		for(int i = 0; i < numStepsX; i++)
		{
			for(int j = 0; j < numStepsY; j++)
			{
				
				if(fractal[i][j] > 127)
				{
					level[i][j] = -1;
				}
				if(fractal[i][j] < 128)
				{
					level[i][j] = 0;
				}
			}
		}
		
		//Landscape(HeightMap)//
		
		for(int i = 0; i < numStepsX; i++)
		{
			for(int j = 0; j < numStepsY*turbulence; j++)
			{

				/*double multiplier = (numStepsY*turbulence)/256;
				int offsetValue = (int) (((fractal[i][(int) (j + numStepsY*(turbulence/2))] + 1)*multiplier) - ((numStepsY*turbulence)*turbulence));

				level[i][(int) (j + (numStepsY*(turbulence/2)))] = levelGrad[i][(int) (j + (numStepsY*(turbulence/2))) + offsetValue];
			*/}
		}
	}
}

