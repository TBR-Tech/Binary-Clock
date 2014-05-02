package com.example.tbrbinaryclockv0_2;

//import com.example.TBRBinaryClock.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedInputStream;

import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
//import android.widget.TextView;
import android.widget.Toast;
//import android.graphics.Canvas;
//import android.graphics.Color;

public class MainActivity extends Activity 
{
	DrawView drawView;
	UpdateTime timer;
	TimeDateBlock tdBlock;
//	Canvas canvas;
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
//        setContentView(R.layout.activity_main);
      	drawView = new DrawView(this, settings);
        timer = new UpdateTime(drawView);
        tdBlock = new TimeDateBlock();
        timer.setupSchedule();
        setContentView(drawView);
        drawView.rollBkgdColor();
        context = this;
      	vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
//      	settings.SetSettings(context, drawView, tdBlock);
      	settings.GetSettings(context, drawView, tdBlock);       	
//      	PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
//      	wl  = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "My Tag");
//      	wl.acquire();
      }

	@Override
    protected void onPause() 
	{
	  super.onPause();
	  settings.SetSettings(context, drawView, tdBlock);	  
	}
	
	@Override
    protected void onStop() 
	{
	  super.onStop();
	  settings.SetSettings(context, drawView, tdBlock);
//   	  wl.release();    
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		settings.GetSettings(context, drawView, tdBlock);
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
				drawView.timeDateBlock.setShape(TimeDateBlock.CIRCLE);
				break;
	    	
	    	case R.id.SetShapeRectangle:
				drawView.timeDateBlock.setShape(TimeDateBlock.RECTANGLE);
				break;
	    	
	    	case R.id.SetShapeOval:
				drawView.timeDateBlock.setShape(TimeDateBlock.OVAL);
				break;
				
	    	case R.id.SizeWidth:
	    		timer.stopTimer();
	    		break;
	    		
	    	case R.id.SizeHeight:
	    		timer.startTimer();
	    		break;
	    		   	
			case R.id.twelve_hour_mode:
				drawView.timeDateBlock.setAmPmMode(true);
				break;
				
			case R.id.twenty_four_hour_mode:
			//case	TWENTYFOUR:
				drawView.timeDateBlock.setAmPmMode(false);
				break;
				
			case R.id.day_month_mode:
				tdBlock.setDayMonthDisplayMode(true);
				break;
				
			case R.id.month_day_mode:
				tdBlock.setDayMonthDisplayMode(false);
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
