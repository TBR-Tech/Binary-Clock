package com.example.tbrbinaryclockv0_2;
import java.util.Timer;
import java.util.TimerTask;

//import android.content.Context;
//import android.view.View;
import android.graphics.Canvas;

public class UpdateTime
{
//	long startTime = 0L;
//	long finishTime = 0L;
	DrawView drawing;
	Canvas canvas;
	long counter = 0;
	boolean twelve24Mode = false;	// 12/24 = 0/1
	Timer timer = new Timer(); 
	static boolean TimerIsStopped = false;
	
	public UpdateTime(DrawView draw)
    {
		drawing = draw;
		canvas = drawing.getCanvas();
    }

	//public void setupSchedule(DrawView viewDrawer)
	public void setupSchedule()
	{
		final long PERIOD = 500;
		//final UpdateBackground drawBackground = new UpdateBackground();
		//drawing = viewDrawer;
    //DrawView drawView;
//		Timer timer = new Timer(); 
    timer.schedule(new TimerTask() 
    {
    	@Override
    	public void run() 
    	{
    		if(!TimerIsStopped)
    		{
	    		drawing.postInvalidate();
	    		counter++;
	    		if(counter == (1000/PERIOD))
	    		{
	    			drawing.rollBkgdColor();
	      			drawing.updateTime(canvas, twelve24Mode);   			
	      			drawing.draw(canvas);
	      			counter = 0;
	    		}
    		}
//    		drawing.draw(canvas);
    	}    	
    }, 0L, PERIOD);
	}
	
	public void handleSchedule()
	{
		
	}
	
	public boolean getMilliseconds()
	{
		counter = System.currentTimeMillis();
		
		if((counter % 1000) == 0)
			return true;
		else
			return false;
	}

	public void Set1224Mode(boolean mode)
	{
		twelve24Mode = mode;
	}
	
	public boolean Get1224Mode()
	{
		return twelve24Mode;
	}
	
	public boolean isTimerStopped()
	{
		return TimerIsStopped; 
	}
	
	public void stopTimer()
	{
		TimerIsStopped = true;
	}
	
	public void startTimer()
	{
		TimerIsStopped = false;
	}
}
