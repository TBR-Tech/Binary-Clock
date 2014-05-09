package com.example.tbrbinaryclockv0_2;

//import com.example.TBRBinaryClock.R;

import java.awt.font.NumericShaper;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedInputStream;

import android.opengl.Visibility;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SubMenu;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
//import android.widget.TextView;
import android.widget.Toast;
//import android.graphics.Canvas;
//import android.graphics.Color;
import android.view.MotionEvent;

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
  	NumberPicker heightPicker;
  	NumberPicker widthPicker;
  	
	@SuppressWarnings("deprecation")
	@Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
      	try
      	{
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
	      	
	      	String[] nums = new String[81];
	
	      	for(int i=0; i<nums.length; i++)
	      	   nums[i] = Integer.toString(i);
	      	
	      	heightPicker = new NumberPicker(this);
	      	heightPicker.setMaxValue(80);
	      	heightPicker.setMinValue(10);
	      	heightPicker.setWrapSelectorWheel(false);
	      	heightPicker.setDisplayedValues(nums);
	      	
	      	widthPicker = new NumberPicker(this);
	      	widthPicker.setMaxValue(80);
	      	widthPicker.setMinValue(10);
	      	widthPicker.setWrapSelectorWheel(false);
	      	widthPicker.setDisplayedValues(nums);
      	}
      	catch(Exception e)
      	{
      		int i = 0;
      	}
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
				
	    	case R.id.SetTimeWidth:
	    		showTimeWidthPickerDialog(context);
	    		break;
	    		
	    	case R.id.SetTimeHeight:
	    		showTimeHeightPickerDialog(context);
	    		break;
	    		   	
	    	case R.id.SetDateWidth:
	    		showDateWidthPickerDialog(context);
	    		break;
	    		
	    	case R.id.SetDateHeight:
	    		showDateHeightPickerDialog(context);
	    		break;
	    		   	
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
    
    private void showTimeHeightPickerDialog(Context context)
    {
        final Dialog d = new Dialog(MainActivity.this);
        d.setTitle("Set Time Block Height");
        d.setContentView(R.layout.time_height_dialog);
        Button setButton = (Button) d.findViewById(R.id.set_time_height_button);
        Button cancelButton = (Button) d.findViewById(R.id.cancel_time_height_button);
        
        final NumberPicker timeHeightPicker = (NumberPicker) d.findViewById(R.id.TimeHeightPicker);
        timeHeightPicker.setMinValue(10);
        timeHeightPicker.setMaxValue(80);
        timeHeightPicker.setValue(drawView.timeBlockHeight);
        timeHeightPicker.setWrapSelectorWheel(false);

        d.show();
        
        setButton.setOnClickListener(new View.OnClickListener() 
        {
			
			@Override
			public void onClick(View v) 
			{
				int height = timeHeightPicker.getValue();
				//drawView.setTimeBlockHeight(timeHeightPicker.getValue());
				drawView.setTimeBlockHeight(height);
				drawView.initializeTimeDisplay(drawView.timeCenter[0], drawView.timeCenter[1], height, drawView.timeBlockWidth);
				//d.dismiss();				
			}
		});
        {
        
        cancelButton.setOnClickListener(new View.OnClickListener()
        {
			@Override
			public void onClick(View v) 
			{
				d.dismiss();
			}
         });
        }
    }

    public void showTimeWidthPickerDialog(Context context)
    {
        final Dialog d = new Dialog(MainActivity.this);
        d.setTitle("Set Time Block Width");
        d.setContentView(R.layout.time_width_dialog);
        Button setButton = (Button) d.findViewById(R.id.set_time_width_button);
        Button cancelButton = (Button) d.findViewById(R.id.cancel_time_width_button);
        
        final NumberPicker timeWidthPicker = (NumberPicker) d.findViewById(R.id.TimeWidthPicker);
        timeWidthPicker.setMinValue(10);
        timeWidthPicker.setMaxValue(80);
        timeWidthPicker.setValue(drawView.timeBlockWidth);
        timeWidthPicker.setWrapSelectorWheel(false);

        d.show();
        
        setButton.setOnClickListener(new View.OnClickListener() 
        {
			
			@Override
			public void onClick(View v) 
			{
				int width = timeWidthPicker.getValue();
				//drawView.setTimeBlockHeight(timeHeightPicker.getValue());
				drawView.setTimeBlockWidth(width);
				drawView.initializeTimeDisplay(drawView.timeCenter[0], drawView.timeCenter[1], drawView.timeBlockHeight, width);
				d.dismiss();				
			}
		});
        {
        
        cancelButton.setOnClickListener(new View.OnClickListener()
        {
			@Override
			public void onClick(View v) 
			{
				d.dismiss();
			}
         });
        }
    }
    
    public void showDateHeightPickerDialog(Context context)
    {
        final Dialog d = new Dialog(MainActivity.this);
        d.setTitle("Set Date Block");
        d.setContentView(R.layout.date_size_dialog);
        Button setButton = (Button) d.findViewById(R.id.set_date_height_button);
        Button cancelButton = (Button) d.findViewById(R.id.cancel_date_height_button);
        
        TextView tvY = (TextView) d.findViewById(R.id.labelY);
        tvY.setText(R.string.set_size_height);
        
        final NumberPicker dateHeightPicker = (NumberPicker) d.findViewById(R.id.HeightPicker);
        dateHeightPicker.setMinValue(10);
        dateHeightPicker.setMaxValue(80);
        dateHeightPicker.setValue(drawView.dateBlockHeight);
        dateHeightPicker.setWrapSelectorWheel(false);

        final NumberPicker dateWidthPicker = (NumberPicker) d.findViewById(R.id.WidthPicker);
        dateHeightPicker.setMinValue(10);
        dateHeightPicker.setMaxValue(80);
        dateHeightPicker.setValue(drawView.dateBlockWidth);
        dateHeightPicker.setWrapSelectorWheel(false);

        d.show();
        
        setButton.setOnClickListener(new View.OnClickListener() 
        {
			
			@Override
			public void onClick(View v) 
			{
				int height = dateHeightPicker.getValue();
				//int width = dateWidthPicker.getValue();
				//drawView.setTimeBlockHeight(timeHeightPicker.getValue());
				drawView.setDateBlockHeight(height);
				//drawView.setDateBlockWidth(width);
				//drawView.initializeTimeDisplay(drawView.timeCenter[0], drawView.timeCenter[1], height, width);
				d.dismiss();				
			}
		});
        {
        
        cancelButton.setOnClickListener(new View.OnClickListener()
        {
			@Override
			public void onClick(View v) 
			{
				d.dismiss();
			}
         });
        }
    }

    public void showDateWidthPickerDialog(Context context)
    {
    	
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
