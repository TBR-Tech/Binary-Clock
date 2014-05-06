package com.example.tbrbinaryclockv0_2;

import java.util.Calendar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.view.ViewCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;

@SuppressLint({ "WrongCall", "SimpleDateFormat" }) 
public class DrawView extends View 
{
//region Rectangles
	Rect timeBorderRect = new Rect();
	Rect timeRect = new Rect();
	Rect dateRect = new Rect();
	Rect dateBorderRect = new Rect();

	Rect[] secondsOnes = 
	{
		new Rect(0,0,0,0),
		new Rect(0,0,0,0),
		new Rect(0,0,0,0),
		new Rect(0,0,0,0),
	};
	Rect[] secondsTens =
	{
		new Rect(0,0,0,0),
		new Rect(0,0,0,0),
		new Rect(0,0,0,0),
	};
	Rect[] minutesOnes = 
	{
		new Rect(0,0,0,0),
		new Rect(0,0,0,0),
		new Rect(0,0,0,0),
		new Rect(0,0,0,0),
	};
	Rect[] minutesTens =
	{
		new Rect(0,0,0,0),
		new Rect(0,0,0,0),
		new Rect(0,0,0,0),
	};
	Rect[] hoursOnes = 
	{
		new Rect(0,0,0,0),
		new Rect(0,0,0,0),
		new Rect(0,0,0,0),
		new Rect(0,0,0,0),
	};
	Rect[] hoursTens = 
	{
		new Rect(0,0,0,0),
		new Rect(0,0,0,0),
	};
	Rect[] monthOnes = 
	{
		new Rect(0,0,0,0),
		new Rect(0,0,0,0),
		new Rect(0,0,0,0),
		new Rect(0,0,0,0),
	};
	Rect monthTens = new Rect();	
	Rect[] dayOnes = 
	{
		new Rect(0,0,0,0),
		new Rect(0,0,0,0),
		new Rect(0,0,0,0),
		new Rect(0,0,0,0),
	};
	Rect[] dayTens = 
	{
		new Rect(0,0,0,0),
		new Rect(0,0,0,0),
		new Rect(0,0,0,0),
		new Rect(0,0,0,0),
	};
	Rect[] yearOnes = 
	{
		new Rect(0,0,0,0),
		new Rect(0,0,0,0),
		new Rect(0,0,0,0),
		new Rect(0,0,0,0),
	};
	Rect[] yearTens = 
	{
		new Rect(0,0,0,0),
		new Rect(0,0,0,0),
		new Rect(0,0,0,0),
		new Rect(0,0,0,0),
	};
//endregion Rectangles
	
	private int[] timeCenter = {-1,0};
	private int[] timeBlockSize = {0,0};
	private int[] dateCenter = {-1,0};
	private int[] dateBlockSize = {0,0};

	public int timeOnColor = 0xFFFF0000;  // red
	public int timeOffColor = 0xFF0000FF; // blue
	public int dateOnColor = 0xFFFF0000;  // red
	public int dateOffColor = 0xFF0000FF; // blue

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

	boolean monthDayDisplayMode = true;	// true = day/month, false = month/day
	boolean amPmMode = false;				// true = 12 hour, false = 24 hour
	boolean TimerIsStopped = false;
	
	int backgroundColor = 0;
	int TimeBackgroundColor = 0xFF0000;
	int TimeOnColor = 0x0000FF;
	private int borderWidth = 10;
	private int borderColor = 0xFF000000;
	public int padding = 16;

	int timeBlockWidth = 0;
	int timeBlockHeight = 0;
	int timeDisplayWidth = 0;
	int timeDisplayHeight = 0;
	
	int dateBlockWidth = 0;
	int dateBlockHeight = 0;
	int dateDisplayWidth = 0;
	int dateDisplayHeight = 0;
	
	private int viewWidth = 0;
	private int viewHeight = 0;
	
	UpdateTime updateTimeDefaults = new UpdateTime(this);
	TimeDateBlock timeDateBlock = new TimeDateBlock();
	Settings settings;
	
	private final int ORIENTATION_PORTRAIT = 1;
	@SuppressWarnings("unused")
	private final int ORIENTATION_LANDSCAPE = 2;
	int orientation = ORIENTATION_PORTRAIT;
	
	public final int RECTANGLE = 0;
	public final int CIRCLE = 1;
	public final int OVAL = 2;
	public final int TRIANGLE_UP = 3;
	public final int TRIANGLE_DOWN = 4;
	public final int TRIANGLE_LEFT = 5;
	public final int TRIANGLE_RIGHT = 6;
	
	private int shape = CIRCLE;
	
	UpdateTime timer;

  public DrawView(Context context, Settings settings) 
  {
     super(context);
     this.context = context;
     this.settings = settings;
     
     paint.setColor(Color.GREEN);
     paint.setStyle(Paint.Style.FILL);
     paint.setAntiAlias(true);     
     this.setSoundEffectsEnabled(true);
     playSoundEffect (SoundEffectConstants.CLICK);

     //setMode(MotionMode.DRAW_POLY);
     setOnTouchListener(listener);   
     
}

  final OnTouchListener listener = new OnTouchListener() 
  {
      boolean movingTimeBlock = false;
      boolean movingDateBlock = false;
      float startX = 0;
      float startY = 0;
      final int MOVE_TOLERANCE = 1;
      
      @Override
      public boolean onTouch(View v, MotionEvent event) 
      {
          // Log.d("jabagator", "onTouch: " + event);
          float eventX = event.getX();
          float eventY = event.getY();
          float dx = 0;
          float dy = 0;
          int left = 0;
          int right = 0;
          int top = 0;
          int bottom = 0;

          switch (event.getAction()) 
          {
            case MotionEvent.ACTION_DOWN:
              startX = eventX;
              startY = eventY;
              if((timeRect.left <= eventX) && (timeRect.right > eventX) &&
            	 (timeRect.top <= eventY) && (timeRect.bottom > eventY))
        	  {
        	  	movingTimeBlock = true;
        	  }
              // There is no end point yet, so don't waste cycles invalidating.
              return true;

           case MotionEvent.ACTION_UP:
        	   {
        		   movingTimeBlock = false;
        	   }
        	   break;
        	   
           case MotionEvent.ACTION_MOVE:
    		   dx = startX - eventX;
    		   dy = startY - eventY;
    		   int moveWidth = 0;
    		   int moveHeight = 0;
    		   moveWidth = getWidth();
    		   moveHeight = getHeight();
    		   
    		   if((Math.abs(dx) > MOVE_TOLERANCE) || (Math.abs(dy) > MOVE_TOLERANCE))
    		   {
        		   startX = eventX;
        		   startY = eventY;
	           	   if(movingTimeBlock == true)
	        	   {
	        		   if(((timeBorderRect.left + (int)dx) >= 0) &&
	        		      ((timeBorderRect.top - (int)dy) >= 0 ) &&
	        		      ((timeBorderRect.right + (int)dx) <= moveWidth) &&
	        		      ((timeBorderRect.bottom + (int)dy) <= moveHeight/2))
	        		   {
	        			   left = timeBorderRect.left + (int)dx;
	        			   //right = timeBorderRect.right + (int)dx;
	        			   top = timeBorderRect.top + (int)dy;
	        			   //bottom = timeBorderRect.bottom + (int)dy;
	        			   
	        			   initializeTimeDisplay(left+timeDisplayWidth/2, top+timeDisplayHeight/2, timeBlockHeight, timeBlockWidth);
	        		   }
//	        		   else
//	        		   {
//	        			   left = (timeBorderRect.left - (int)dx) >= 0?timeBorderRect.left - (int)dx:0;
//	        			   top = (timeBorderRect.top - (int)dx) >= 0?timeBorderRect.top - (int)dx:0;
//	        			   right = 0;
//	        			   bottom = 0;
//	        			   if(orientation == ORIENTATION_PORTRAIT)
//	        			   {
//	            			   right = (timeBorderRect.right + (int)dx) >= 0?timeBorderRect.right - (int)dx:getWidth();
//	            			   bottom = (timeBorderRect.bottom - (int)dx) >= 0?timeBorderRect.bottom - (int)dx:getHeight()/2;
//	        			   }
//	        			   else
//	        			   {
//	            			   right = (timeBorderRect.right + (int)dx) >= 0?timeBorderRect.right - (int)dx:getWidth()/2;
//	            			   bottom = (timeBorderRect.bottom - (int)dx) >= 0?timeBorderRect.bottom - (int)dx:getHeight();
//	        			   }
//	        			   initializeTimeDisplay((left+right)/2, (top+bottom)/2, timeBlockHeight, timeBlockWidth);
//	        		   }
	        	   }
	        	   else if(movingDateBlock == true)
	        	   {
	        		   
	        	   }
    		   }
          }
 	      invalidate();
    	  return true;
      }
  };

// if centerX,Y == -1, use h/w to get size
// else use centerX,Y
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

  
//if centerX or centerY == -1, set at default location with size determined by height and width
// if height or width == -1, set the location on centerx,y using default h/w
private void initializeTimeDisplay(int centerX, int centerY, int height, int width)
{
	int left = 0;
	int right = 0;
	int top = 0;
	int startTop = 0;
	int bottom = 0;
	int startBottom = 0;

	orientation = getScreenOrientation();
	
	if((viewWidth == 0) || (viewHeight == 0))
	{
		while((viewWidth == 0) || (viewHeight == 0))
		{
			viewWidth = getWidth();
			viewHeight = getHeight();
		}
	}
	
	if((width == -1) || (height == -1))	// set to size at x,y using h/w as big as possible
	{
		if(orientation == ORIENTATION_PORTRAIT)
		{
			timeCenter[0] = centerX;
			timeCenter[1] = centerY;
			
			if(centerX <= viewWidth/2)
				timeDisplayWidth = (centerX - padding - borderWidth) * 2;
			else
				timeDisplayWidth = (viewWidth - centerX - padding - borderWidth) * 2;
			
			timeBlockWidth = (timeDisplayWidth/6) - (timeDisplayWidth%6);

			if(centerY <= viewHeight/4)
				timeDisplayHeight = (centerY - padding - borderWidth) * 2;
			else
				timeDisplayWidth = (viewHeight/2 - centerY - padding - borderWidth) * 2;
			
			timeBlockHeight = (timeDisplayHeight/4) - (timeDisplayHeight%4);
		}
		else	// landscape
		{
			timeCenter[0] = centerX;
			timeCenter[1] = centerY;
			
			if(centerX <= getWidth()/4)
				timeDisplayWidth = (centerX - padding - borderWidth) * 2;
			else
				timeDisplayWidth = (viewWidth - centerX - padding - borderWidth) * 2;
			
			timeBlockWidth = (timeDisplayWidth/6) - (timeDisplayWidth%6);

			if(centerY <= viewHeight/2)
				timeDisplayHeight = (centerY - padding - borderWidth) * 2;
			else
				timeDisplayWidth = (viewHeight/2 - centerY - padding - borderWidth) * 2;
			
			timeBlockHeight = (timeDisplayHeight/4) - (timeDisplayHeight%4);
		}
		timeBlockSize[0] = timeBlockWidth;
		timeBlockSize[1] = timeBlockWidth;
	}	
	else if((centerX == -1) || (centerY == -1))	// set to h/w at default center
	{
		if(orientation == ORIENTATION_PORTRAIT)
		{
			timeCenter[0] = viewWidth/2;		
			timeCenter[1] = viewHeight/4;
			
			if((width*6) > (viewWidth - (2*(padding + borderWidth))))
				width = (viewWidth -  (2*(padding + borderWidth)))/6;

			timeDisplayWidth = width * 6;
			timeBlockWidth = width;

			if((height*4) > ((viewHeight/2) - (2*(padding + borderWidth))))
				height = ((viewHeight/2) - (2*(padding + borderWidth)))/4;

			timeDisplayHeight = height * 4;
			timeBlockHeight = height;
		}
		else	// landscape
		{
			timeCenter[0] = viewWidth/4;		
			timeCenter[1] = viewHeight/2;

			if((width*6) > ((viewWidth/2) - (2*(padding + borderWidth))))
				width = ((viewWidth/2) -  (2*(padding + borderWidth)))/6;

			timeDisplayWidth = width * 6;
			timeBlockWidth = width;

			if((height*4) > (viewHeight - (2*(padding + borderWidth))))
				height = (viewHeight - (2*(padding + borderWidth)))/4;

			timeDisplayHeight = height * 4;
			timeBlockHeight = height;
		}		
		timeBlockSize[0] = timeBlockWidth;
		timeBlockSize[1] = timeBlockHeight;
	}
	else	// set x,y with w,h
	{
		timeCenter[0] = centerX;
		timeCenter[1] = centerY;

		if(orientation == ORIENTATION_PORTRAIT)
		{
			if((width*6) > (viewWidth - (2*(borderWidth))))
				width = (viewWidth -  (2*(borderWidth)))/6;

			timeDisplayWidth = width * 6;
			timeBlockWidth = width;

			if((height*4) > ((viewHeight/2) - (2*(borderWidth))))
				height = ((viewHeight/2) - (2*(borderWidth)))/4;

			timeDisplayHeight = height * 4;
			timeBlockHeight = height;
			
		}
		else
		{
			if((width*6) > ((viewWidth/2) - (2*(borderWidth))))
				width = ((viewWidth/2) -  (2*(borderWidth)))/6;

			timeDisplayWidth = width * 6;
			timeBlockWidth = width;

			if((height*4) > (viewHeight - (2*(borderWidth))))
				height = (viewHeight - (2*(borderWidth)))/4;

			timeDisplayHeight = height * 4;
			timeBlockHeight = height;
		}		
		timeBlockSize[0] = timeBlockWidth;
		timeBlockSize[1] = timeBlockHeight;
	}

	// set the borderBlock
	left = timeCenter[0] - (timeDisplayWidth/2) - borderWidth;
	right = timeCenter[0] + (timeDisplayWidth/2) + borderWidth;
	top = timeCenter[1] - (timeDisplayHeight/2) - borderWidth;
	bottom = timeCenter[1] + (timeDisplayHeight/2) + borderWidth;
	timeBorderRect.set(left, top, right, bottom);

	// set the timeRect
	left = timeCenter[0] - (timeDisplayWidth/2);
	right = timeCenter[0] + (timeDisplayWidth/2);
	top = timeCenter[1] - (timeDisplayHeight/2);
	bottom = timeCenter[1] + (timeDisplayHeight/2);
	timeRect.set(left, top, right, bottom);

	// now set the time unit locations
	// set(l,t,r,b)
	left = timeCenter[0] + (timeDisplayWidth/2) - timeBlockWidth;
	right = timeCenter[0] + (timeDisplayWidth/2);
	top = timeCenter[1] + (timeDisplayHeight/2) - timeBlockHeight;
	startTop = top;
	bottom = timeCenter[1] + (timeDisplayHeight/2);
	startBottom = bottom;

	for(int i=0;i<4;i++)
	{
		secondsOnes[i].set(left, top, right, bottom);
		top -= timeBlockHeight;
		bottom -= timeBlockHeight;
	}

	left -= timeBlockWidth;
	right -= timeBlockWidth;
	top = startTop;
	bottom = startBottom;

	for(int i=0;i<3;i++)
	{
		secondsTens[i].set(left, top, right, bottom);
		top -= timeBlockHeight;
		bottom -= timeBlockHeight;
	}

	left -= timeBlockWidth;
	right -= timeBlockWidth;
	top = startTop;
	bottom = startBottom;

	for(int i=0;i<4;i++)
	{
		minutesOnes[i].set(left, top, right, bottom);
		top -= timeBlockHeight;
		bottom -= timeBlockHeight;
	}

	left -= timeBlockWidth;
	right -= timeBlockWidth;
	top = startTop;
	bottom = startBottom;

	for(int i=0;i<3;i++)
	{
		minutesTens[i].set(left, top, right, bottom);
		top -= timeBlockHeight;
		bottom -= timeBlockHeight;
	}

	left -= timeBlockWidth;
	right -= timeBlockWidth;
	top = startTop;
	bottom = startBottom;

	for(int i=0;i<4;i++)
	{
		hoursOnes[i].set(left, top, right, bottom);
		top -= timeBlockHeight;
		bottom -= timeBlockHeight;
	}

	left -= timeBlockWidth;
	right -= timeBlockWidth;
	top = startTop;
	bottom = startBottom;

	for(int i=0;i<2;i++)
	{
		hoursTens[i].set(left, top, right, bottom);
		top -= timeBlockHeight;
		bottom -= timeBlockHeight;
	}
}

private void initializeDateDisplay(int centerX, int centerY, int height, int width)
{
	int left = 0;
	int right = 0;
	int top = 0;
	int startTop = 0;
	int bottom = 0;
	int startBottom = 0;

	int orientation = getScreenOrientation();
	
	if((viewWidth == 0) || (viewHeight == 0))
	{
		while((viewWidth == 0) || (viewHeight == 0))
		{
			viewWidth = getWidth();
			viewHeight = getHeight();
		}
	}
	
	if((width == -1) || (height == -1))	// set to size at x,y using h/w as big as possible
	{
		if(orientation == ORIENTATION_PORTRAIT)
		{
			dateCenter[0] = centerX;
			dateCenter[1] = centerY;
			
			if(centerX <= viewWidth/2)
				dateDisplayWidth = (centerX - padding - borderWidth) * 2;
			else
				dateDisplayWidth = (viewWidth - centerX - padding - borderWidth) * 2;
			
			dateBlockWidth = (dateDisplayWidth/6) - (dateDisplayWidth%6);

			if(centerY <= viewHeight/4)
				dateDisplayHeight = (centerY - padding - borderWidth) * 2;
			else
				dateDisplayWidth = (viewHeight/2 - centerY - padding - borderWidth) * 2;
			
			dateBlockHeight = (dateDisplayHeight/4) - (dateDisplayHeight%4);
		}
		else	// landscape
		{
			dateCenter[0] = centerX;
			dateCenter[1] = centerY;
			
			if(centerX <= getWidth()/4)
				dateDisplayWidth = (centerX - padding - borderWidth) * 2;
			else
				dateDisplayWidth = (viewWidth - centerX - padding - borderWidth) * 2;
			
			dateBlockWidth = (dateDisplayWidth/6) - (dateDisplayWidth%6);

			if(centerY <= viewHeight/2)
				dateDisplayHeight = (centerY - padding - borderWidth) * 2;
			else
				dateDisplayWidth = (viewHeight/2 - centerY - padding - borderWidth) * 2;
			
			dateBlockHeight = (dateDisplayHeight/4) - (dateDisplayHeight%4);
		}
		dateBlockSize[0] = dateBlockWidth;
		dateBlockSize[1] = dateBlockWidth;
	}	
	else if((centerX == -1) || (centerY == -1))	// set to h/w at default center
	{
		if(orientation == ORIENTATION_PORTRAIT)
		{
			dateCenter[0] = viewWidth/2;		
			dateCenter[1] = (viewHeight*3)/4;
			
			if((width*6) > (viewWidth - (2*(padding + borderWidth))))
				width = (viewWidth -  (2*(padding + borderWidth)))/6;

			dateDisplayWidth = width * 6;
			dateBlockWidth = width;

			if((height*4) > ((viewHeight/2) - (2*(padding + borderWidth))))
				height = ((viewHeight/2) - (2*(padding + borderWidth)))/4;

			dateDisplayHeight = height * 4;
			dateBlockHeight = height;
		}
		else	// landscape
		{
			dateCenter[0] = (viewWidth*3)/4;		
			dateCenter[1] = viewHeight/2;

			if((width*6) > ((viewWidth/2) - (2*(padding + borderWidth))))
				width = ((viewWidth/2) -  (2*(padding + borderWidth)))/6;

			dateDisplayWidth = width * 6;
			dateBlockWidth = width;

			if((height*4) > (viewHeight - (2*(padding + borderWidth))))
				height = (viewHeight - (2*(padding + borderWidth)))/4;

			dateDisplayHeight = height * 4;
			dateBlockHeight = height;
		}		
		dateBlockSize[0] = timeBlockWidth;
		dateBlockSize[1] = timeBlockHeight;
	}
	else	// set x,y with w,h
	{
		
	}

	// set the borderBlock
	left = dateCenter[0] - (dateDisplayWidth/2) - borderWidth;
	right = dateCenter[0] + (dateDisplayWidth/2) + borderWidth;
	top = dateCenter[1] - (dateDisplayHeight/2) - borderWidth;
	bottom = dateCenter[1] + (dateDisplayHeight/2) + borderWidth;
	dateBorderRect.set(left, top, right, bottom);

	// set the dateRect
	left = dateCenter[0] - (dateDisplayWidth/2);
	right = dateCenter[0] + (dateDisplayWidth/2);
	top = dateCenter[1] - (dateDisplayHeight/2);
	bottom = dateCenter[1] + (dateDisplayHeight/2);
	dateRect.set(left, top, right, bottom);

	// now set the date unit locations
	// set(l,t,r,b)
	left = dateCenter[0] + (dateDisplayWidth/2) - dateBlockWidth;
	right = dateCenter[0] + (dateDisplayWidth/2);
	top = dateCenter[1] + (dateDisplayHeight/2) - dateBlockHeight;
	startTop = top;
	bottom = dateCenter[1] + (dateDisplayHeight/2);
	startBottom = bottom;

	for(int i=0;i<4;i++)
	{
		yearOnes[i].set(left, top, right, bottom);
		top -= dateBlockHeight;
		bottom -= dateBlockHeight;
	}

	left -= dateBlockWidth;
	right -= dateBlockWidth;
	top = startTop;
	bottom = startBottom;

	for(int i=0;i<4;i++)
	{
		yearTens[i].set(left, top, right, bottom);
		top -= dateBlockHeight;
		bottom -= dateBlockHeight;
	}

	left -= dateBlockWidth;
	right -= dateBlockWidth;
	top = startTop;
	bottom = startBottom;

	for(int i=0;i<4;i++)
	{
		dayOnes[i].set(left, top, right, bottom);
		top -= dateBlockHeight;
		bottom -= dateBlockHeight;
	}

	left -= dateBlockWidth;
	right -= dateBlockWidth;
	top = startTop;
	bottom = startBottom;

	for(int i=0;i<2;i++)
	{
		dayTens[i].set(left, top, right, bottom);
		top -= dateBlockHeight;
		bottom -= dateBlockHeight;
	}

	left -= dateBlockWidth;
	right -= dateBlockWidth;
	top = startTop;
	bottom = startBottom;

	for(int i=0;i<4;i++)
	{
		monthOnes[i].set(left, top, right, bottom);
		top -= dateBlockHeight;
		bottom -= dateBlockHeight;
	}
	
	left -= dateBlockWidth;
	right -= dateBlockWidth;
	top = startTop;
	bottom = startBottom;

	monthTens.set(left, top, right, bottom);

}

	public void setMonthDayMode(boolean monthDayOrder)
	{
		if(monthDayOrder == monthDayDisplayMode)
			return;
		else
		{
			monthDayDisplayMode = monthDayOrder;
			if(monthDayOrder == false) // new order is day/month
			{
				monthTens.set(dayTens[0]);
				for(int i=0;i<4;i++)
				{
					monthOnes[i].set(dayOnes[i]);
				}
				dayTens[0].set(monthTens.left-(2*dateBlockWidth), monthTens.top, monthTens.right-(2*dateBlockWidth), monthTens.bottom);
				dayTens[1].set(dayTens[0].left, dayTens[0].top-dateBlockHeight, dayTens[0].right, dayTens[0].bottom-dateBlockHeight);
				dayOnes[0].set(dayTens[0].left+dateBlockWidth, dayTens[0].top, dayTens[0].right+dateBlockWidth, dayTens[0].bottom);
				dayOnes[1].set(dayOnes[0].left, dayOnes[0].top-dateBlockHeight, dayOnes[0].right, dayOnes[0].bottom-dateBlockHeight);
				dayOnes[2].set(dayOnes[1].left, dayOnes[1].top-dateBlockHeight, dayOnes[1].right, dayOnes[1].bottom-dateBlockHeight);
				dayOnes[3].set(dayOnes[2].left, dayOnes[2].top-dateBlockHeight, dayOnes[2].right, dayOnes[2].bottom-dateBlockHeight);
			}
			else	// new order is month/day
			{
				dayTens[0].set(monthTens);
				dayTens[1].set(monthTens.left, monthTens.top-dateBlockHeight, monthTens.right, monthTens.bottom-dateBlockHeight);
				for(int i=0;i<4;i++)
				{
					dayOnes[i].set(monthOnes[i]);
				}
				monthTens.set(dayTens[0].left-(2*dateBlockWidth), dayTens[0].top, dayTens[0].right-(2*dateBlockWidth), dayTens[0].bottom);
				monthOnes[0].set(monthTens.left+dateBlockWidth, monthTens.top, monthTens.right+dateBlockWidth, monthTens.bottom);
				monthOnes[1].set(monthOnes[0].left, monthOnes[0].top-dateBlockHeight, monthOnes[0].right, monthOnes[0].bottom-dateBlockHeight);
				monthOnes[2].set(monthOnes[1].left, monthOnes[1].top-dateBlockHeight, monthOnes[1].right, monthOnes[1].bottom-dateBlockHeight);
				monthOnes[3].set(monthOnes[2].left, monthOnes[2].top-dateBlockHeight, monthOnes[2].right, monthOnes[2].bottom-dateBlockHeight);
			}
		}
	}

public void updateTime(Canvas canvas, boolean twelve24Mode)
{
	int hours;
	
	viewWidth = getWidth();
	viewHeight = getHeight();

	if((timeCenter[0] == -1) || (timeBlockSize[0] == 0))
	  initializeTimeDisplay(-1,-1,60,60);
	if((dateCenter[0] == -1) || (dateBlockSize[0] == 0))
	  initializeDateDisplay(-1,-1,80,80);

	Calendar rightNow = Calendar.getInstance();
	
	hours = rightNow.get(Calendar.HOUR_OF_DAY);
	if((hours > 12) && (getAmPmMode() == true))
		hours -= 12;
	else if((hours == 0) && (getAmPmMode() == true))
		hours = 12;
		
	int minutes = rightNow.get(Calendar.MINUTE);
	int seconds = rightNow.get(Calendar.SECOND);

	showTime(canvas, hours, minutes, seconds);

	int month = rightNow.get(Calendar.MONTH) + 1;
	int day = rightNow.get(Calendar.DATE);
	int year = rightNow.get(Calendar.YEAR)%100;

	showDate(canvas, month, day, year);
}

private void showTime(Canvas canvas, int hours, int minutes, int seconds)
{
	Paint paint = new Paint();
	paint.setColor(borderColor);
	canvas.drawRect(timeBorderRect, paint);
	paint.setColor(timeOffColor);
	canvas.drawRect(timeRect, paint);
	
	Paint paintOnColor = new Paint();
	paintOnColor.setColor(timeOnColor);
	Paint paintOffColor = new Paint();
	paintOffColor.setColor(timeOffColor);
	
	int secondOnes = seconds%10;
	int secondTens = seconds/10;
	int minuteOnes = minutes%10;
	int minuteTens = minutes/10;
	int hourOnes = hours%10;
	int hourTens = hours/10;

	for(int i=0;i<4;i++)
	{
		if((secondOnes & 1) == 1) paint = paintOnColor;
		else paint = paintOffColor;
		secondOnes >>= 1;
		drawShape(canvas, secondsOnes[i], paint);
	}
	for(int i=0;i<3;i++)
	{
		if((secondTens & 1) == 1) paint = paintOnColor;
		else paint = paintOffColor;
		secondTens >>= 1;
		drawShape(canvas, secondsTens[i], paint);
	}
	for(int i=0;i<4;i++)
	{
		if((minuteOnes & 1) == 1) paint = paintOnColor;
		else paint = paintOffColor;
		minuteOnes >>= 1;
		drawShape(canvas, minutesOnes[i], paint);
	}
	for(int i=0;i<3;i++)
	{
		if((minuteTens & 1) == 1) paint = paintOnColor;
		else paint = paintOffColor;
		minuteTens >>= 1;
		drawShape(canvas, minutesTens[i], paint);
	}
	for(int i=0;i<4;i++)
	{
		if((hourOnes & 1) == 1) 
			paint = paintOnColor;
		else 
			paint = paintOffColor;
		hourOnes >>= 1;
		drawShape(canvas, hoursOnes[i], paint);
	}
	for(int i=0;i<2;i++)
	{
		if((hourTens & 1) == 1) paint = paintOnColor;
		else paint = paintOffColor;
		hourTens >>= 1;
		drawShape(canvas, hoursTens[i], paint);
	}
}

private void showDate(Canvas canvas, int month, int day, int year)
{
	Paint paint = new Paint();
	paint.setColor(borderColor);
	canvas.drawRect(dateBorderRect, paint);
	paint.setColor(dateOffColor);
	canvas.drawRect(dateRect, paint);

	Paint paintOnColor = new Paint();
	paintOnColor.setColor(dateOnColor);
	Paint paintOffColor = new Paint();
	paintOffColor.setColor(dateOffColor);

	int dayOne = day%10;
	int dayTen = day/10;
	int monthOne = month%10;
	int monthTen = month/10;
	int yearOne = year%10;
	int yearTen = year/10;
	
	for(int i=0;i<4;i++)
	{
		if((monthOne & 1) == 1) paint = paintOnColor;
		else paint = paintOffColor;
		monthOne >>= 1;
		drawShape(canvas, monthOnes[i], paint);
	}

	if(monthTen >= 10) paint = paintOnColor;
	else paint = paintOffColor;
	drawShape(canvas, monthTens, paint);

	for(int i=0;i<4;i++)
	{
		if((dayOne & 1) == 1) paint = paintOnColor;
		else paint = paintOffColor;
		dayOne >>= 1;
		drawShape(canvas, dayOnes[i], paint);
	}
	for(int i=0;i<2;i++)
	{
		if((dayTen & 1) == 1) paint = paintOnColor;
		else paint = paintOffColor;
		dayTen >>= 1;
		drawShape(canvas, dayTens[i], paint);
	}
	for(int i=0;i<4;i++)
	{
		if((yearOne & 1) == 1) paint = paintOnColor;
		else paint = paintOffColor;
		yearOne >>= 1;
		drawShape(canvas, yearOnes[i], paint);
	}
	for(int i=0;i<4;i++)
	{
		if((yearTen & 1) == 1) paint = paintOnColor;
		else paint = paintOffColor;
		yearTen >>= 1;
		drawShape(canvas, yearTens[i], paint);
	}
}

private void drawShape(Canvas canvas, Rect rect, Paint paint)
{
	paint.setStyle(Style.FILL);
	Path path = new Path();
	RectF rectf = new RectF();
	
	switch(shape)
	{
		case RECTANGLE:
			rectf.set(rect);
			canvas.drawRoundRect(rectf, rectf.width()/4, rectf.height()/4, paint);
			break;

		case CIRCLE:
			canvas.drawCircle(rect.centerX(), rect.centerY(), (timeBlockWidth > timeBlockHeight)?rect.height()/2:rect.width()/2, paint);
			break;

		case OVAL:
			rectf.set(rect);
			canvas.drawOval(rectf, paint);
			break;
			
		case TRIANGLE_UP:
			path.reset(); // only needed when reusing this path for a new build
			path.moveTo(rect.left, rect.bottom); // used for first point
			path.lineTo(rect.right, rect.bottom);
			path.lineTo((rect.left+rect.right)/2, rect.top);
			path.close();
			canvas.drawPath(path, paint);
			break;
			
		case TRIANGLE_DOWN:
			path.reset(); // only needed when reusing this path for a new build
			path.moveTo(rect.left, rect.top); // used for first point
			path.lineTo(rect.right, rect.top);
			path.lineTo((rect.left+rect.right)/2, rect.bottom);
			path.close();
			canvas.drawPath(path, paint);
			break;
			
		case TRIANGLE_LEFT:
			path.reset(); // only needed when reusing this path for a new build
			path.moveTo(rect.right, rect.top); // used for first point
			path.lineTo(rect.right, rect.bottom);
			path.lineTo(rect.left, (rect.bottom+rect.top)/2);
			path.close();
			canvas.drawPath(path, paint);
			break;
			
		case TRIANGLE_RIGHT:
			path.reset(); // only needed when reusing this path for a new build
			path.moveTo(rect.left, rect.top); // used for first point
			path.lineTo(rect.left, rect.bottom);
			path.lineTo(rect.right, (rect.bottom + rect.top)/2);
			path.close();
			canvas.drawPath(path, paint);
			break;
								
		default:break;
	}
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
	
	public void rollBkgdColor(int increment)
	{
		backgroundColor += increment;
	}
	
	public void rollBkgdColor()
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
	
	public int getShape()
	{
		return shape;
	}
	
	public void setShape(int shape)
	{
		this.shape = shape;
	}
	
	public boolean getAmPmMode()
	{
		return amPmMode;
	}
	
	public void setAmPmMode(boolean mode)
	{
		amPmMode = mode;
	}
	
	public void setTimer(UpdateTime timer)
	{
		this.timer = timer;
	}
	
	public void setTimeBlockWidth(int width)
	{
		timeBlockWidth = width;
	}
	
	public void setDateBlockWidth(int width)
	{
		dateBlockWidth = width;
	}
	
	public void setTimeBlockHeight(int height)
	{
		timeBlockHeight = height;
	}
	
	public void setDateBlockHeight(int height)
	{
		dateBlockHeight = height;
	}
	
}
