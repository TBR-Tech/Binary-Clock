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
import android.os.Vibrator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
//import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Canvas;
//import android.graphics.Color;

public class MainActivity extends Activity 
{
	DrawView drawView;
	UpdateTime timer;
	TimeDateBlock tdBlock;
	Canvas canvas;
	Context context;
  	Settings settings = new Settings();
  	Menu menu2;

	public enum Menus
	{
		SETCOLORS(0),
		SETSHAPES(1),
		TIMEMODE(2),
		DATEFORMAT(3),
		QUIT(4);
	
		private int menu;

	  Menus(int menu) 
	  {
	    this.menu = menu;
	  }

	  public int getMenu() { return menu;}
	}

//	private static enum SubMenus
//	{
//		SETCOLORS,
//		TWELVE,
//		TWENTYFOUR
//	}

  private final static int SETCOLORSMENU = 0;
  private final static int SETSHAPESMENU = SETCOLORSMENU + 1;
  private final static int SETLOCATIONMENU = SETSHAPESMENU + 1;
  private final static int SETSIZESMENU = SETLOCATIONMENU + 1;
  private final static int TIMEMODEMENU = SETSIZESMENU + 1;
  private final static int DATEFORMATMENU = TIMEMODEMENU + 1;
  private final static int QUITMENU = DATEFORMATMENU + 1;
  
  private final static int TWELVE = QUITMENU + 1;
  private final static int TWENTYFOUR = TWELVE + 1;
  private final static int DAYMONTH = TWENTYFOUR + 1;
  private final static int MONTHDAY = DAYMONTH + 1;
  private final static int RECTANGLE = MONTHDAY + 1;
  private final static int CIRCLE = RECTANGLE + 1;
  private final static int OVAL = CIRCLE + 1;
  private final static int XSTART = OVAL + 1;
  private final static int YSTART = XSTART + 1;
  private final static int FULLSCREEN = YSTART + 1;
  private final static int XSIZE = FULLSCREEN + 1;
  private final static int YSIZE = XSIZE + 1;

  Vibrator vibrator;
  Menu menu;
  
	@Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
      	drawView = new DrawView(this);
        timer = new UpdateTime(drawView);
        tdBlock = new TimeDateBlock();
        timer.setupSchedule();
        setContentView(drawView);
        drawView.setBkgdColor();
        context = this;
      	vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
      	settings.GetSettings(context, drawView, tdBlock); 
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
				drawView.timeDateBlocksDefaults.setShape(TimeDateBlock.CIRCLE);
				break;
	    	
	    	case R.id.SetShapeRectangle:
				drawView.timeDateBlocksDefaults.setShape(TimeDateBlock.RECTANGLE);
				break;
	    	
	    	case R.id.SetShapeOval:
				drawView.timeDateBlocksDefaults.setShape(TimeDateBlock.OVAL);
				break;
				
	    	case R.id.SizeWidth:
	    		break;
	    		
	    	case R.id.SizeHeight:
	    		break;
	    		   	
			case R.id.twelve24Mode:
				drawView.timeDateBlocksDefaults.setAmPmMode(true);
				break;
				
			case R.id.twenty_four_hour_mode:
			//case	TWENTYFOUR:
				drawView.timeDateBlocksDefaults.setAmPmMode(false);
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
    
    private void quitApp()
    {
		// write the persistent settings to file
    	//settings.WriteSettings(drawView, timer);
    	Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
    }
    
}
