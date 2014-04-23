package com.example.tbrbinaryclockv0_2;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;
//import android.text.format.Time;

@SuppressLint({ "WrongCall", "SimpleDateFormat" }) 
public class DrawView extends View 
{
    Paint paint = new Paint();
    Canvas canvas2 = new Canvas();
    Context c;
    float x;
    float y;
	static int red = 0;
	static int green = 0;
	static int blue = 0xFF;
	static boolean redActive = true;
	static boolean greenActive = false;
	static boolean blueActive = false;
	int backgroundColor = 0xFF0000FF;
	boolean firstTime = true;
	boolean dayMonthDisplayMode = false;	// true = day/month, false = month/day
	boolean amPmMode = false;				// true = 12 hour, false = 24 hour

	UpdateTime updateTimeDefaults = new UpdateTime(this);
	TimeDateBlock timeDateBlocksDefaults = new TimeDateBlock();
		
	TimeDateBlock seconds_1 = new TimeDateBlock(440, 300, 80, 80, Color.GREEN, Color.BLACK);
	TimeDateBlock seconds_2 = new TimeDateBlock(440, 220, 80, 80, Color.GREEN, Color.BLACK);
	TimeDateBlock seconds_4 = new TimeDateBlock(440, 140, 80, 80, Color.GREEN, Color.BLACK);
	TimeDateBlock seconds_8 = new TimeDateBlock(440, 60, 80, 80, Color.GREEN, Color.BLACK);

	TimeDateBlock seconds_11 = new TimeDateBlock(360, 300, 80, 80, Color.GREEN, Color.BLACK);
	TimeDateBlock seconds_12 = new TimeDateBlock(360, 220, 80, 80, Color.GREEN, Color.BLACK);
	TimeDateBlock seconds_14 = new TimeDateBlock(360, 140, 80, 80, Color.GREEN, Color.BLACK);

	TimeDateBlock minutes_1 = new TimeDateBlock(280, 300, 80, 80, Color.GREEN, Color.BLACK);
	TimeDateBlock minutes_2 = new TimeDateBlock(280, 220, 80, 80, Color.GREEN, Color.BLACK);
	TimeDateBlock minutes_4 = new TimeDateBlock(280, 140, 80, 80, Color.GREEN, Color.BLACK);
	TimeDateBlock minutes_8 = new TimeDateBlock(280, 60, 80, 80, Color.GREEN, Color.BLACK);

	TimeDateBlock minutes_11 = new TimeDateBlock(200, 300, 80, 80, Color.GREEN, Color.BLACK);
	TimeDateBlock minutes_12 = new TimeDateBlock(200, 220, 80, 80, Color.GREEN, Color.BLACK);
	TimeDateBlock minutes_14 = new TimeDateBlock(200, 140, 80, 80, Color.GREEN, Color.BLACK);

	TimeDateBlock hours_1 = new TimeDateBlock(120, 300, 80, 80, Color.GREEN, Color.BLACK);
	TimeDateBlock hours_2 = new TimeDateBlock(120, 220, 80, 80, Color.GREEN, Color.BLACK);
	TimeDateBlock hours_4 = new TimeDateBlock(120, 140, 80, 80, Color.GREEN, Color.BLACK);
	TimeDateBlock hours_8 = new TimeDateBlock(120, 60, 80, 80, Color.GREEN, Color.BLACK);

	TimeDateBlock hours_11 = new TimeDateBlock(40, 300, 80, 80, Color.GREEN, Color.BLACK);
	TimeDateBlock hours_12 = new TimeDateBlock(40, 220, 80, 80, Color.GREEN, Color.BLACK);

	TimeDateBlock days_1 = new TimeDateBlock(280, 660, 80, 80, Color.GREEN, Color.BLACK);
	TimeDateBlock days_2 = new TimeDateBlock(280, 580, 80, 80, Color.GREEN, Color.BLACK);
	TimeDateBlock days_4 = new TimeDateBlock(280, 500, 80, 80, Color.GREEN, Color.BLACK);
	TimeDateBlock days_8 = new TimeDateBlock(280, 420, 80, 80, Color.GREEN, Color.BLACK);

	TimeDateBlock days_11 = new TimeDateBlock(200, 660, 80, 80, Color.GREEN, Color.BLACK);
	TimeDateBlock days_12 = new TimeDateBlock(200, 580, 80, 80, Color.GREEN, Color.BLACK);

	TimeDateBlock months_1 = new TimeDateBlock(120, 660, 80, 80, Color.GREEN, Color.BLACK);
	TimeDateBlock months_2 = new TimeDateBlock(120, 580, 80, 80, Color.GREEN, Color.BLACK);
	TimeDateBlock months_4 = new TimeDateBlock(120, 500, 80, 80, Color.GREEN, Color.BLACK);
	TimeDateBlock months_8 = new TimeDateBlock(120, 420, 80, 80, Color.GREEN, Color.BLACK);

	TimeDateBlock months_11 = new TimeDateBlock(40, 660, 80, 80, Color.GREEN, Color.BLACK);

	TimeDateBlock years_1 = new TimeDateBlock(440, 660, 80, 80, Color.GREEN, Color.BLACK);
	TimeDateBlock years_2 = new TimeDateBlock(440, 580, 80, 80, Color.GREEN, Color.BLACK);
	TimeDateBlock years_4 = new TimeDateBlock(440, 500, 80, 80, Color.GREEN, Color.BLACK);
	TimeDateBlock years_8 = new TimeDateBlock(440, 420, 80, 80, Color.GREEN, Color.BLACK);

	TimeDateBlock years_11 = new TimeDateBlock(360, 660, 80, 80, Color.GREEN, Color.BLACK);
	TimeDateBlock years_12 = new TimeDateBlock(360, 580, 80, 80, Color.GREEN, Color.BLACK);
	TimeDateBlock years_14 = new TimeDateBlock(360, 500, 80, 80, Color.GREEN, Color.BLACK);
	TimeDateBlock years_18 = new TimeDateBlock(360, 420, 80, 80, Color.GREEN, Color.BLACK);

  public DrawView(Context context) 
  {
     super(context);
     c = context;
     paint.setColor(Color.GREEN);
     paint.setStyle(Paint.Style.FILL);
     paint.setAntiAlias(true);
     x = 200.0f;
     y = 200.0f;
  }

  @Override
  public void onDraw(Canvas canvas) 
  {
  	super.onDraw(canvas); 
  	 //canvas2 = canvas;
  }
  
  @Override
  public void draw(Canvas canvas)
  {
	canvas.drawColor(backgroundColor);
  	updateTime(canvas, updateTimeDefaults.Get1224Mode());
  }
    
  public void updateTime(Canvas canvas, boolean twelve24Mode)
	{
	  int hours;
	  
	  Calendar rightNow = Calendar.getInstance();
	  
	  if(timeDateBlocksDefaults.isAmPmMode() == true)
		  hours = rightNow.get(Calendar.HOUR);
	  else
		  hours = rightNow.get(Calendar.HOUR_OF_DAY);
	  int minutes = rightNow.get(Calendar.MINUTE);
	  int seconds = rightNow.get(Calendar.SECOND);
	  
	  int month = rightNow.get(Calendar.MONTH) + 1;
	  int day = rightNow.get(Calendar.DATE);
	  int year = rightNow.get(Calendar.YEAR);

	  setSecondsBlocks(canvas, seconds);
	  setMinutesBlocks(canvas, minutes);	  
	  setHoursBlocks(canvas, hours);	  	

	  if(dayMonthDisplayMode == true)
	  {
  	    setDaysBlocks(canvas, day);
	    setMonthsBlocks(canvas, month);
	  }
	  else
	  {
	    setMonthsBlocks(canvas, month);
		setDaysBlocks(canvas, day);
	  }
	  setYearsBlocks(canvas, year);	  
	}
	
	private void setTimeBlocks(TimeDateBlock block, int time, Canvas canvas, int blockNumber)
	{
		paint.setColor(block.GetOffColor());
		paint.setStrokeWidth(10);

		int border = 10;
		int width = timeDateBlocksDefaults.getWidth();
		int height = timeDateBlocksDefaults.getHeight();
		int[] center = timeDateBlocksDefaults.getCenter();
//		int left = block.GetCenter()[0] - (block.GetWidth()/2);
//		int right = block.GetCenter()[0] + (block.GetWidth()/2);
//		int top = block.GetCenter()[1] - (block.GetHeight()/2);
//		int bottom = block.GetCenter()[1] + (block.GetHeight()/2);		
		int left = center[0] - (width/2);
		int right = center[0] + (width/2);
		int top = center[1] - (height/2);
		int bottom = center[1] + (height/2);		

		int shape = timeDateBlocksDefaults.Shape;
		
		canvas.drawRect(left, top, right, bottom, paint);
		
	  if((time & blockNumber) == blockNumber)
	  {
		  paint.setColor(block.GetOnColor());

//			left = block.GetCenter()[0] - (block.GetWidth()/2) + border;
//			right = block.GetCenter()[0] + (block.GetWidth()/2) - border;
//			top = block.GetCenter()[1] - (block.GetHeight()/2) + border;
//			bottom = block.GetCenter()[1] + (block.GetHeight()/2) - border;
			left = center[0] - (width/2) + border;
			right = center[0] + (width/2) - border;
			top = center[1] - (height/2) + border;
			bottom = center[1] + (height/2) - border;
		  
			canvas.drawRect(left, top, right, bottom, paint);
	  }
	}
	
	private void setTransparentTimeBlocks(TimeDateBlock block, int time, Canvas canvas, int blockNumber)
	{
		paint.setColor(backgroundColor);
		paint.setStrokeWidth(10);

		int border = 10;
		int width = timeDateBlocksDefaults.getWidth();
		int height = timeDateBlocksDefaults.getHeight();
		int[] center = timeDateBlocksDefaults.getCenter();
//		int left = block.GetCenter()[0] - (block.GetWidth()/2);
//		int right = block.GetCenter()[0] + (block.GetWidth()/2);
//		int top = block.GetCenter()[1] - (block.GetHeight()/2);
//		int bottom = block.GetCenter()[1] + (block.GetHeight()/2);		
		int left = block.GetCenter()[0] - (width/2);
		int right = block.GetCenter()[0] + (width/2);
		int top = block.GetCenter()[1] - (height/2);
		int bottom = block.GetCenter()[1] + (height/2);		
		int sizeX = right - left;
		int sizeY = bottom - top;
		
		int shape = timeDateBlocksDefaults.getShape();
		
		switch(shape)
		{
			case TimeDateBlock.RECTANGLE:
				canvas.drawRect(left, top, right, bottom, paint);
				break;
				
			case TimeDateBlock.CIRCLE:
				canvas.drawCircle(block.GetCenter()[0], block.GetCenter()[1], (sizeX > sizeY)?sizeY/2:sizeX/2, paint);
				break;

			case TimeDateBlock.OVAL:
				RectF oval = new RectF(left, top, right, bottom);
				canvas.drawOval(oval, paint);
				break;
				
				default:break;
		}
		
//		public void drawOval (RectF oval, Paint paint)  
//		RectF is class for drawing rectangle...whose constructor is defined as following...
//
//		RectF(x,y,x+width,y+height); 		
	  if((time & blockNumber) == blockNumber)
	  {
		  paint.setColor(backgroundColor ^ 0x00FFFFFF);

//		left = block.GetCenter()[0] - (block.GetWidth()/2) + border;
//		right = block.GetCenter()[0] + (block.GetWidth()/2) - border;
//		top = block.GetCenter()[1] - (block.GetHeight()/2) + border;
//		bottom = block.GetCenter()[1] + (block.GetHeight()/2) - border;
		left = block.GetCenter()[0] - (width/2) + border;
		right = block.GetCenter()[0] + (width/2) - border;
		top = block.GetCenter()[1] - (height/2) + border;
		bottom = block.GetCenter()[1] + (height/2) - border;
		sizeX = right - left;
		sizeY = bottom - top;
	  
		switch(shape)
		{
			case TimeDateBlock.RECTANGLE:
				canvas.drawRect(left, top, right, bottom, paint);
				break;
				
			case TimeDateBlock.CIRCLE:
				canvas.drawCircle(block.GetCenter()[0], block.GetCenter()[1], (sizeX > sizeY)?sizeY/2:sizeX/2, paint);
				break;

			case TimeDateBlock.OVAL:
				RectF oval = new RectF(left, top, right, bottom);
				canvas.drawOval(oval, paint);
				break;
				
				default:break;
		}		
	  }
	}
	
	private void setSecondsBlocks(Canvas canvas, int seconds)
	{
		int secondOnes = seconds%10;
		int secondsTens = seconds/10;
		
		setTransparentTimeBlocks(seconds_1, secondOnes, canvas, 1);
		setTransparentTimeBlocks(seconds_2, secondOnes, canvas, 2);
		setTransparentTimeBlocks(seconds_4, secondOnes, canvas, 4);
		setTransparentTimeBlocks(seconds_8, secondOnes, canvas, 8);
		
		setTransparentTimeBlocks(seconds_11, secondsTens, canvas, 1);
		setTransparentTimeBlocks(seconds_12, secondsTens, canvas, 2);
		setTransparentTimeBlocks(seconds_14, secondsTens, canvas, 4);
	}
	
	public void setMinutesBlocks(Canvas canvas, int minutes)
	{
		//static int oldMinutes = minutes;
		
		int minuteOnes = minutes%10;
		int minuteTens = minutes/10;
		
		setTransparentTimeBlocks(minutes_1, minuteOnes, canvas, 1);
		setTransparentTimeBlocks(minutes_2, minuteOnes, canvas, 2);
		setTransparentTimeBlocks(minutes_4, minuteOnes, canvas, 4);
		setTransparentTimeBlocks(minutes_8, minuteOnes, canvas, 8);
		
		setTransparentTimeBlocks(minutes_11, minuteTens, canvas, 1);
		setTransparentTimeBlocks(minutes_12, minuteTens, canvas, 2);
		setTransparentTimeBlocks(minutes_14, minuteTens, canvas, 4);
	}
  
	public void setHoursBlocks(Canvas canvas, int hours)
	{
		int hourOnes = hours%10;
		int hourTens = hours/10;
		
		setTransparentTimeBlocks(hours_1, hourOnes, canvas, 1);
		setTransparentTimeBlocks(hours_2, hourOnes, canvas, 2);
		setTransparentTimeBlocks(hours_4, hourOnes, canvas, 4);
		setTransparentTimeBlocks(hours_8, hourOnes, canvas, 8);
		
		setTransparentTimeBlocks(hours_11, hourTens, canvas, 1);
		setTransparentTimeBlocks(hours_12, hourTens, canvas, 2);
		
		if(timeDateBlocksDefaults.isAmPmMode() == true)
		{
		  paint.setColor(~backgroundColor);
	  	  paint.setTextSize(100);
	  	  if(hours < 12)
	  		  canvas.drawText("A",  20, 80, paint);
	  	  else
	  		  canvas.drawText("P",  20, 80, paint);
		}
	}
  
	private void setDaysBlocks(Canvas canvas, int day)
	{
		int dayOnes = day%10;
		int dayTens = day/10;
		
		setTransparentTimeBlocks(days_1, dayOnes, canvas, 1);
		setTransparentTimeBlocks(days_2, dayOnes, canvas, 2);
		setTransparentTimeBlocks(days_4, dayOnes, canvas, 4);
		setTransparentTimeBlocks(days_8, dayOnes, canvas, 8);
		
		setTransparentTimeBlocks(days_11, dayTens, canvas, 1);
		setTransparentTimeBlocks(days_12, dayTens, canvas, 2);
	}
	
	public void setMonthsBlocks(Canvas canvas, int month)
	{
		int monthOnes = month%10;
		int monthTens = month/10;
		
		setTransparentTimeBlocks(months_1, monthOnes, canvas, 1);
		setTransparentTimeBlocks(months_2, monthOnes, canvas, 2);
		setTransparentTimeBlocks(months_4, monthOnes, canvas, 4);
		setTransparentTimeBlocks(months_8, monthOnes, canvas, 8);
		
		setTransparentTimeBlocks(months_11, monthTens, canvas, 1);
	}
  
	public void setYearsBlocks(Canvas canvas, int year)
	{
		int yearOnes = year%10;
		int yearTens = year/10;
		
		setTransparentTimeBlocks(years_1, yearOnes, canvas, 1);
		setTransparentTimeBlocks(years_2, yearOnes, canvas, 2);
		setTransparentTimeBlocks(years_4, yearOnes, canvas, 4);
		setTransparentTimeBlocks(years_8, yearOnes, canvas, 8);
		
		setTransparentTimeBlocks(years_11, yearTens, canvas, 1);
		setTransparentTimeBlocks(years_12, yearTens, canvas, 2);
		setTransparentTimeBlocks(years_14, yearTens, canvas, 4);
		setTransparentTimeBlocks(years_18, yearTens, canvas, 8);
	}
  
  
	public void setBackgroundColor(int bgColor)
	{
		blue = bgColor & 0xFF;
		bgColor >>= 8;
		green = bgColor & 0xFF;
		bgColor >>= 8;
		red = bgColor & 0xFF;
	}
	
	public int getBackgroundColor()
	{
		return backgroundColor;
	}
	
	public void setBkgdColor()
	{	
		int relativeChange = 5;
		
  	if(redActive)
		{
			red += relativeChange;
			blue -= relativeChange;
			if(red == 0xFF)
			{
				redActive = false;
				greenActive = true;
			}
		}
		else if(greenActive)
		{
			green += relativeChange;
			red -= relativeChange;
			if(green == 0xFF)
			{
				greenActive = false;
				blueActive = true;
			}
		}
		else // blueActive = true
		{
			blue += relativeChange;
			green -=relativeChange;
			if(blue == 0xFF)
			{
				blueActive = false;
				redActive = true;
			}			
		}
		backgroundColor = 0xFF000000 + ((red << 16) + (green << 8) + blue);
    //canvas2.drawRect(33, 60, 77, 77, paint);
	//	setBackgroundColor(backgroundColor);
		canvas2.drawColor(backgroundColor);
		//draw(canvas2);
	}
	
	public Canvas getCanvas()
	{
		return canvas2;
	}
	
	public DrawView getDrawView()
	{
		return this;
	}
	
}