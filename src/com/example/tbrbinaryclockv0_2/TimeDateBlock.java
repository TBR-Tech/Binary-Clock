package com.example.tbrbinaryclockv0_2;
import android.content.Context;
import android.graphics.Color;

public class TimeDateBlock
{
	public final static int RECTANGLE = 0;
	public final static int CIRCLE = RECTANGLE + 1;
	public final static int OVAL = CIRCLE + 1;
	
	int[] Center = {0,0};
	int[] TimeBlockCenter = {0,0};
	int[] DateBlockCenter = {0,0};
	int Height = 80;
	int Width = 80;
	int Border = 0;
	int OnColor = Color.WHITE;
	int OffColor = Color.BLACK;
	int Shape = CIRCLE;
	boolean amPmMode = false;	// 12/24 = 0/1
	boolean dayMonthDisplayMode = false;	// true = day/month, false = month/day
	
	public TimeDateBlock(int centerX, int centerY, int height, int width, int onColor, int offColor)
	{
		Center[0] = centerX;
		Center[1] = centerY;
		Height = height;
		Width = width;
		OnColor = onColor;
		OffColor = offColor;
	}
	
	public TimeDateBlock()	// this just 
	{
		//this.Center = Center;
		//this.Shape = Shape;
	}
	
	public int getShape()
	{
		return this.Shape;
	}

	public void setShape(int shape)
	{
		this.Shape = shape;
	}

	public int[] getCenter()
	{
		return this.Center;
	}

	public void setCenter(int[] center)
	{
		this.Center = center;
	}

	public int getWidth()
	{
		return this.Width;
	}

	public void setWidth(int width)
	{
		this.Width = width;
	}

	public int getHeight()
	{
		return this.Height;
	}

	public void setHeight(int height)
	{
		this.Height = height;
	}

	public void SetCenter(int x, int y)
	{
		Center[0] = x;
		Center[1] = y;
	}
	
	public int getBorder()
	{
		return this.Border;
	}

	public void setBorder(int border)
	{
		this.Border = border;
	}

	public int[] GetCenter()
	{
		return this.Center;
	}
	
	public void SetHeight(int height)
	{
		Height = height;
	}
	
	public int GetHeight()
	{
		return Height;
	}
	
	public void SetWidth(int width)
	{
		Width = width;
	}
	
	public int GetWidth()
	{
		return Width;
	}
	
	public void SetOnColor(int color)
	{
		OnColor = color;
	}
	
	public int GetOnColor()
	{
		return OnColor;
	}
	
	public void SetOffColor(int color)
	{
		OffColor = color;
	}
	
	public int GetOffColor()
	{
		return OffColor;
	}

	public int[] getTimeBlockCenter() {
		return TimeBlockCenter;
	}

	public void setTimeBlockCenter(int[] timeBlockCenter) {
		TimeBlockCenter = timeBlockCenter;
	}

	public int[] getDateBlockCenter() {
		return DateBlockCenter;
	}

	public void setDateBlockCenter(int[] dateBlockCenter) {
		DateBlockCenter = dateBlockCenter;
	}

	public boolean isAmPmMode() 
	{
		return this.amPmMode;
	}

	public void setAmPmMode(boolean amPmMode) 
	{
		this.amPmMode = amPmMode;
	}

	public boolean isDayMonthDisplayMode() {
		return dayMonthDisplayMode;
	}

	public void setDayMonthDisplayMode(boolean dayMonthDisplayMode) 
	{
		this.dayMonthDisplayMode = dayMonthDisplayMode;
	}
	
	public boolean getDayMonthDisplayMode(boolean dayMonthDisplayMode) 
	{
		return this.dayMonthDisplayMode;
	}
	
}
