package com.example.tbrbinaryclockv0_2;

//import com.example.TBRBinaryClock.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends Activity 
{
	DrawView drawView;
	UpdateTime timer;
	Context context;
  	Settings settings = new Settings();
  	Menu menu2;
  	Vibrator vibrator;
  	Menu menu;
  	PowerManager.WakeLock wl;
  	
	@SuppressWarnings("deprecation")
	@Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
      	drawView = new DrawView(this, settings);
        timer = new UpdateTime(drawView);
        timer.setupSchedule();
        setContentView(drawView);
        drawView.rollBkgdColor();
        context = this;
      	vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
      	settings.GetSettings(context, drawView);       	
      }

	@Override
    protected void onPause() 
	{
	  super.onPause();
	  settings.SetSettings(context, drawView);	  
	}
	
	@Override
    protected void onStop() 
	{
	  super.onStop();
	  settings.SetSettings(context, drawView);
//   	  wl.release();    
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		settings.GetSettings(context, drawView);
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.main, menu);
	    //return true;
		return true;

    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
    	Context context = getApplicationContext();
    	int duration = Toast.LENGTH_SHORT;
    	CharSequence text;
    	Toast toast;
    	
		vibrator.vibrate(150);
		int itemId = item.getItemId();
    	    	
    	switch (item.getItemId()) 
    	{
	    	case R.id.SetOnColor:
	    		item.setIcon(R.drawable.ic_menu_greenicon);
	    		// get color picker value
	    		// set into timeDateBlock.OnColor
	    		break;
	    	
	    	case R.id.SetOffColor:
	    		// get color picker value
	    		// set into timeDateBlock.OffColor
	   		break;
	   		
	    	case R.id.SetShapeCircle:
				drawView.setShape(drawView.CIRCLE);
				break;
	    	
	    	case R.id.SetShapeRectangle:
				drawView.setShape(drawView.RECTANGLE);
				break;
	    	
	    	case R.id.SetShapeOval:
				drawView.setShape(drawView.OVAL);
				break;

	    	case R.id.SetShapeTriangleUp:
				drawView.setShape(drawView.TRIANGLE_UP);
				break;
				
	    	case R.id.SetShapeTriangleDown:
				drawView.setShape(drawView.TRIANGLE_DOWN);
				break;
				
	    	case R.id.SetShapeTriangleLeft:
				drawView.setShape(drawView.TRIANGLE_LEFT);
				break;
				
	    	case R.id.SetShapeTriangleRight:
				drawView.setShape(drawView.TRIANGLE_RIGHT);
				break;
				
	    	case R.id.TimeSizeWidth:
	    		// open numeric spinner
	    		// return value is new width
	    		NumberPicker timeBlockWidthPicker = new NumberPicker(this);
	    		int width = timeBlockWidthPicker.getValue();
	    		drawView.setTimeBlockWidth(width);
	    		break;
	    		
	    	case R.id.TimeSizeHeight:
//	    		drawView.setTimeBlockHeight(height);
//	    		break;
//	    		   	
//	    	case R.id.DateSizeWidth:
//	    		// open numeric spinner
//	    		// return value is new width
//	    		drawView.setDateBlockWidth(width);
//	    		break;
//	    		
//	    	case R.id.DateSizeHeight:
//	    		drawView.setDateBlockHeight(height);
//	    		break;
//	    		   	
			case R.id.twelve_hour_mode:
				drawView.setAmPmMode(true);
				break;
				
			case R.id.twenty_four_hour_mode:
			//case	TWENTYFOUR:
				drawView.setAmPmMode(false);
				break;
				
			case R.id.day_month_mode:
				drawView.setMonthDayMode(false);
				break;
				
			case R.id.month_day_mode:
				drawView.setMonthDayMode(true);
				break;
       	}
    	return true;   	
    }

    public UpdateTime getTimer()
    {
    	return timer;
    }
    
//    private void quitApp()
//    {
//		// write the persistent settings to file
//    	//settings.WriteSettings(drawView, timer);
//    	Intent intent = new Intent(Intent.ACTION_MAIN);
//		intent.addCategory(Intent.CATEGORY_HOME);
//		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		startActivity(intent);
//    }
    
}
