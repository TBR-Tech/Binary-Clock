package com.example.tbrbinaryclockv0_2;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.R.string;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
//import android.text.format.Time;

@SuppressLint({ "WrongCall", "SimpleDateFormat" }) 
public class DrawView extends View 
{
    Paint paint = new Paint();
    Path path = new Path();
    Canvas canvas = new Canvas();
    Context context;
	static int red = 0;
	static int green = 0;
	static int blue = 0xFF;
	static boolean redActive = true;
	static boolean greenActive = false;
	static boolean blueActive = false;
	private static final float STROKE_WIDTH = 5f;
	private static final float HALF_STROKE_WIDTH = STROKE_WIDTH/2;

	boolean dayMonthDisplayMode = false;	// true = day/month, false = month/day
	boolean amPmMode = false;				// true = 12 hour, false = 24 hour
	boolean TimerIsStopped = false;
	
	private float lastTouchX;
	private float lastTouchY;
	private final RectF dirtyRect = new RectF();

	int backgroundColor = 0;
	int TimeBackgroundColor = 0xFF0000;
	int TimeOnColor = 0x0000FF;
	private int borderWidth = 10;
	private int borderColor = 0xFF000000;
	private int padding = 16;
	
	private int[] timeCenter = {100000,0};
	private int[] timeBlockSize = {0,0};
	private int[] dateCenter = {100000,0};
	private int[] dateBlockSize = {0,0};

	UpdateTime updateTimeDefaults = new UpdateTime(this);
	TimeDateBlock timeDateBlock = new TimeDateBlock();
	Settings settings;
	
	private final int ORIENTATION_PORTRAIT = 1;
	private final int ORIENTATION_LANDSCAPE = 2;

	public enum MotionMode {
		SELECT_AND_MOVE,
		DRAW_POLY,
		DRAW_OVAL,
		DRAW_RECTANGLE
	}
	
  public DrawView(Context context, Settings settings) 
  {
     super(context);
     this.context = context;
     this.settings = settings;
     
     paint.setColor(Color.GREEN);
     paint.setStyle(Paint.Style.FILL);
     paint.setAntiAlias(true);     
     playSoundEffect (SoundEffectConstants.CLICK);
     setMode(MotionMode.DRAW_POLY);
     }

  
    final OnTouchListener selectionAndMoveListener = new OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent event) {
          return false;
      }
  };
  final OnTouchListener drawRectangleListener = new OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent event) {
          return false;
      }
  };
  final OnTouchListener drawOvalListener = new OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent event) {
          return false;
      }
  };
  
  final OnTouchListener drawPolyLineListener = new OnTouchListener() {
      
      @Override
      public boolean onTouch(View v, MotionEvent event) 
      {
          // Log.d("jabagator", "onTouch: " + event);
          float eventX = event.getX();
          float eventY = event.getY();

          switch (event.getAction()) 
          {
            case MotionEvent.ACTION_DOWN:
              path.moveTo(eventX, eventY);
              lastTouchX = eventX;
              lastTouchY = eventY;
              // There is no end point yet, so don't waste cycles invalidating.
              return true;

            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
              // Start tracking the dirty region.
              resetDirtyRect(eventX, eventY);

              // When the hardware tracks events faster than they are delivered, the
              // event will contain a history of those skipped points.
              int historySize = event.getHistorySize();
              for (int i = 0; i < historySize; i++) 
              {
                float historicalX = event.getHistoricalX(i);
                float historicalY = event.getHistoricalY(i);
                expandDirtyRect(historicalX, historicalY);
                path.lineTo(historicalX, historicalY);
              }

              // After replaying history, connect the line to the touch point.
              path.lineTo(eventX, eventY);
              break;

            default:
              //Log.d("jabagator", "Unknown touch event  " + event.toString());
              return false;
          }

          // Include half the stroke width to avoid clipping.
          invalidate(
              (int) (dirtyRect.left - HALF_STROKE_WIDTH),
              (int) (dirtyRect.top - HALF_STROKE_WIDTH),
              (int) (dirtyRect.right + HALF_STROKE_WIDTH),
              (int) (dirtyRect.bottom + HALF_STROKE_WIDTH));
          
          lastTouchX = eventX;
          lastTouchY = eventY;

          return true;
      }
      
        /**
         * Called when replaying history to ensure the dirty region includes all
         * points.
         */
        private void expandDirtyRect(float historicalX, float historicalY) 
        {
          if (historicalX < dirtyRect.left) 
          {
            dirtyRect.left = historicalX;
          } 
          else if (historicalX > dirtyRect.right) 
          {
            dirtyRect.right = historicalX;
          }
          if (historicalY < dirtyRect.top) 
          {
            dirtyRect.top = historicalY;
          } 
          else if (historicalY > dirtyRect.bottom) 
          {
            dirtyRect.bottom = historicalY;
          }
        }

        /**
         * Resets the dirty region when the motion event occurs.
         */
        private void resetDirtyRect(float eventX, float eventY) {

          // The lastTouchX and lastTouchY were set when the ACTION_DOWN
          // motion event occurred.
          dirtyRect.left = Math.min(lastTouchX, eventX);
          dirtyRect.right = Math.max(lastTouchX, eventX);
          dirtyRect.top = Math.min(lastTouchY, eventY);
          dirtyRect.bottom = Math.max(lastTouchY, eventY);
        }
  };

  public DrawView(Context context, AttributeSet attrs) {
      super(context, attrs);

      paint.setAntiAlias(true);
      paint.setColor(Color.WHITE);
      paint.setStyle(Paint.Style.STROKE);
      paint.setStrokeJoin(Paint.Join.ROUND);
      paint.setStrokeWidth(STROKE_WIDTH);
      
      setMode(MotionMode.DRAW_POLY);
  }
  
  public void clear() {
      path.reset();

      // Repaints the entire view.
      invalidate();
  }

  /**
   * Sets the DrawingView into one of several modes, such
   * as "select" mode (e.g., for moving or resizing objects), 
   * or "Draw polyline" (smooth curve), "draw rectangle", etc.
   * Can be called from our constructor as well as from the toolbar, etc.
   * MotionMode is JabaGator's view mode enumeration.
   */
  private void setMode(MotionMode motionMode) {
      switch(motionMode) {
      case SELECT_AND_MOVE:
          setOnTouchListener(selectionAndMoveListener);
          break;
      case DRAW_POLY:
          setOnTouchListener(drawPolyLineListener);
          break;
      case DRAW_RECTANGLE:
          setOnTouchListener(drawRectangleListener);
          break;
      case DRAW_OVAL:
          setOnTouchListener(drawOvalListener);
          break;
      default:
          throw new IllegalStateException("Unknown MotionMode " + motionMode);
      }
  }

  
  private void initializeTimeBlock(int height, int width)
  {
	  int h = height;
	  int w = width;
	 
//	  int h = getResources().getConfiguration().screenHeightDp;
//	  int w = getResources().getConfiguration().screenWidthDp;
	  int rotation = getScreenOrientation();

	  if(rotation == ORIENTATION_PORTRAIT)
	  {
		  timeCenter[0] = width/2;
		  timeCenter[1] = height/4;
		  
		  int timeBlockWidth = width - (2*(borderWidth + padding));
		  timeBlockWidth -= timeBlockWidth % 6;
		  
		  int timeBlockHeight = (height/2) - (2*(borderWidth + padding));
		  timeBlockHeight -= timeBlockHeight % 4;
		  
		  timeBlockSize[0] = timeBlockWidth;
		  timeBlockSize[1] = timeBlockHeight;
	  }
	  else
	  {
		  timeCenter[0] = width/4;
		  timeCenter[1] = height/2;
	  }
  
  }
  
  private void initializeDateBlock(int height, int width)
  {
	  
  }
  
  public int[] getTimeCenter()
  {
	  return timeCenter;
  }
  
  public void setTimeCenter(int[] timeCenterLocation)
  {
	  timeCenter[0] = timeCenterLocation[0];
	  timeCenter[1] = timeCenterLocation[1];
  }
    
  public int[] getDateCenter()
  {
	  return dateCenter;
  }
    
  @Override
  public void onDraw(Canvas canvas) 
  {
  	super.onDraw(canvas); 
  	canvas.drawPath(path, paint);
  }
  
  private int getScreenOrientation()
  {    
	  return getResources().getConfiguration().orientation;
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
	  
	  if(timeDateBlock.isAmPmMode() == true)
		  hours = rightNow.get(Calendar.HOUR);
	  else
		  hours = rightNow.get(Calendar.HOUR_OF_DAY);
	  int minutes = rightNow.get(Calendar.MINUTE);
	  int seconds = rightNow.get(Calendar.SECOND);
	  
	  int month = rightNow.get(Calendar.MONTH) + 1;
	  int day = rightNow.get(Calendar.DATE);
	  int year = rightNow.get(Calendar.YEAR)%100;
	  
	  int height = getHeight();
	  int width = getWidth();
	  
	  if((height == 0) || (width == 0))
		  return;
		  
	  if((timeCenter[0] == 100000) || (timeBlockSize[0] == 0))
		  initializeTimeBlock(height,width);
	  if((dateCenter[0] == 100000) || (dateBlockSize[0] == 0))
		  initializeDateBlock(height,width);
	  
	  int timeLeft = timeCenter[0] - timeBlockSize[0]/2;
	  int timeRight = timeCenter[0] + timeBlockSize[0]/2;
	  int timeTop = timeCenter[1] - timeBlockSize[1]/2;
	  int timeBottom = timeCenter[1] + timeBlockSize[1]/2;

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
	  String timerOrientation;
		int rotation = getScreenOrientation();
		if(rotation == ORIENTATION_PORTRAIT)
		{
			timerOrientation = "Portrait";
		}
		else
		{
			timerOrientation = "Landscape";
		}
		
	  int color = 0xFF000000 | TimeBackgroundColor;

	  paint.setColor(borderColor);
	  
	  paint.setTextSize(70);
	  canvas.drawRect(timeLeft, timeTop, timeRight, timeBottom, paint);
	  color = 0xFF000000 | TimeOnColor;
	  paint.setColor(color);
	  canvas.drawRect(timeLeft+borderWidth, timeTop+borderWidth, timeRight-borderWidth, timeBottom-borderWidth, paint);
	  canvas.drawText(timerOrientation,  200, 200, paint);

	}
	
	private void setTimeBlocks(TimeDateBlock block, int time, Canvas canvas, int blockNumber)
	{
		paint.setColor(block.GetOffColor());
		paint.setStrokeWidth(10);

		int border = 10;
		int width = timeDateBlock.getWidth();
		int height = timeDateBlock.getHeight();
		int[] center = timeDateBlock.getCenter();
//		int left = block.GetCenter()[0] - (block.GetWidth()/2);
//		int right = block.GetCenter()[0] + (block.GetWidth()/2);
//		int top = block.GetCenter()[1] - (block.GetHeight()/2);
//		int bottom = block.GetCenter()[1] + (block.GetHeight()/2);		
		int left = center[0] - (width/2);
		int right = center[0] + (width/2);
		int top = center[1] - (height/2);
		int bottom = center[1] + (height/2);		

		int shape = timeDateBlock.Shape;
		
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

		int rotation = getScreenOrientation();
		if(rotation == ORIENTATION_PORTRAIT)
		{
			
		}
		else
		{
			
		}
		
		int border = 10;
		int width = timeDateBlock.getWidth();
		int height = timeDateBlock.getHeight();
		int[] center = timeDateBlock.getCenter();
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
		
		int shape = timeDateBlock.getShape();
		
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
		
		setTransparentTimeBlocks(timeDateBlock, secondOnes, canvas, 1);
		setTransparentTimeBlocks(timeDateBlock, secondOnes, canvas, 2);
		setTransparentTimeBlocks(timeDateBlock, secondOnes, canvas, 4);
		setTransparentTimeBlocks(timeDateBlock, secondOnes, canvas, 8);
		
		setTransparentTimeBlocks(timeDateBlock, secondsTens, canvas, 1);
		setTransparentTimeBlocks(timeDateBlock, secondsTens, canvas, 2);
		setTransparentTimeBlocks(timeDateBlock, secondsTens, canvas, 4);
	}
	
	public void setMinutesBlocks(Canvas canvas, int minutes)
	{
		//static int oldMinutes = minutes;
		
		int minuteOnes = minutes%10;
		int minuteTens = minutes/10;
		
		setTransparentTimeBlocks(timeDateBlock, minuteOnes, canvas, 1);
		setTransparentTimeBlocks(timeDateBlock, minuteOnes, canvas, 2);
		setTransparentTimeBlocks(timeDateBlock, minuteOnes, canvas, 4);
		setTransparentTimeBlocks(timeDateBlock, minuteOnes, canvas, 8);
		
		setTransparentTimeBlocks(timeDateBlock, minuteTens, canvas, 1);
		setTransparentTimeBlocks(timeDateBlock, minuteTens, canvas, 2);
		setTransparentTimeBlocks(timeDateBlock, minuteTens, canvas, 4);
	}
  
	public void setHoursBlocks(Canvas canvas, int hours)
	{
		int hourOnes = hours%10;
		int hourTens = hours/10;
		
		setTransparentTimeBlocks(timeDateBlock, hourOnes, canvas, 1);
		setTransparentTimeBlocks(timeDateBlock, hourOnes, canvas, 2);
		setTransparentTimeBlocks(timeDateBlock, hourOnes, canvas, 4);
		setTransparentTimeBlocks(timeDateBlock, hourOnes, canvas, 8);
		
		setTransparentTimeBlocks(timeDateBlock, hourTens, canvas, 1);
		setTransparentTimeBlocks(timeDateBlock, hourTens, canvas, 2);
		
		if(timeDateBlock.isAmPmMode() == true)
		{
		  paint.setColor(0xFF000000);
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
		
		setTransparentTimeBlocks(timeDateBlock, dayOnes, canvas, 1);
		setTransparentTimeBlocks(timeDateBlock, dayOnes, canvas, 2);
		setTransparentTimeBlocks(timeDateBlock, dayOnes, canvas, 4);
		setTransparentTimeBlocks(timeDateBlock, dayOnes, canvas, 8);
		
		setTransparentTimeBlocks(timeDateBlock, dayTens, canvas, 1);
		setTransparentTimeBlocks(timeDateBlock, dayTens, canvas, 2);
	}
	
	public void setMonthsBlocks(Canvas canvas, int month)
	{
		int monthOnes = month%10;
		int monthTens = month/10;
		
		setTransparentTimeBlocks(timeDateBlock, monthOnes, canvas, 1);
		setTransparentTimeBlocks(timeDateBlock, monthOnes, canvas, 2);
		setTransparentTimeBlocks(timeDateBlock, monthOnes, canvas, 4);
		setTransparentTimeBlocks(timeDateBlock, monthOnes, canvas, 8);
		
		setTransparentTimeBlocks(timeDateBlock, monthTens, canvas, 1);
	}
  
	public void setYearsBlocks(Canvas canvas, int year)
	{
		int yearOnes = year%10;
		int yearTens = year/10;
		
		setTransparentTimeBlocks(timeDateBlock, yearOnes, canvas, 1);
		setTransparentTimeBlocks(timeDateBlock, yearOnes, canvas, 2);
		setTransparentTimeBlocks(timeDateBlock, yearOnes, canvas, 4);
		setTransparentTimeBlocks(timeDateBlock, yearOnes, canvas, 8);
		
		setTransparentTimeBlocks(timeDateBlock, yearTens, canvas, 1);
		setTransparentTimeBlocks(timeDateBlock, yearTens, canvas, 2);
		setTransparentTimeBlocks(timeDateBlock, yearTens, canvas, 4);
		setTransparentTimeBlocks(timeDateBlock, yearTens, canvas, 8);
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
	
	public void rollBkgdColor()
	{	
		int relativeChange = 255;
			
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
		backgroundColor = 0x80000000 + ((red << 16) + (green << 8) + blue);
	}
	
	public Canvas getCanvas()
	{
		return this.canvas;
	}
	
	public void setCanvas(Canvas canvas)
	{
		this.canvas = canvas;
	}
	
	public DrawView getDrawView()
	{
		return this;
	}
	
	public void setTimeOffColor(int color)
	{
		this.TimeBackgroundColor = color;
	}
	
	public int getTimeOffColor()
	{
		return this.TimeBackgroundColor;
	}
	
	public void setTimeOnColor(int color)
	{
		this.TimeOnColor = color;
	}
	
	public int getTimeOnColor()
	{
		return this.TimeOnColor;
	}
	
}