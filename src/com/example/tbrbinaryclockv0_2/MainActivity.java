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
      SubMenu setColorsMenu = menu.addSubMenu(SETCOLORSMENU, SETCOLORSMENU, 0, "Set Colors");

      SubMenu setShapesMenu = menu.addSubMenu(SETSHAPESMENU, SETSHAPESMENU, 0, "Set Shapes");
      setShapesMenu.add(SETSHAPESMENU, RECTANGLE, 0, "Rectangle");
      setShapesMenu.add(SETSHAPESMENU, CIRCLE, 1, "Circle");
      setShapesMenu.add(SETSHAPESMENU, OVAL, 2, "Oval");
      
      SubMenu setLocationMenu = menu.addSubMenu(SETLOCATIONMENU, SETLOCATIONMENU, 0, "Set Location");
      setLocationMenu.add(SETLOCATIONMENU, XSTART, 0, "Starting X Location");
      setLocationMenu.add(SETLOCATIONMENU, YSTART, 1, "Starting Y Location");
      setLocationMenu.add(SETLOCATIONMENU, FULLSCREEN, 2, "Full Screen");
      
      SubMenu setSizesMenu = menu.addSubMenu(SETSIZESMENU, SETSIZESMENU, 0, "Set Size");
      setSizesMenu.add(SETSIZESMENU, XSIZE, 0, "Set Width");
      setSizesMenu.add(SETSIZESMENU, YSIZE, 1, "Set Height");
      
      SubMenu modeMenu = menu.addSubMenu(TIMEMODEMENU, TIMEMODEMENU, 0, "12/24 Hour Mode");
      modeMenu.add(TIMEMODEMENU, TWELVE, 0, "12 hour mode (AM/PM)");
      modeMenu.add(TIMEMODEMENU, TWENTYFOUR, 1, "24 hour mode");
            
      SubMenu dateFormatMenu = menu.addSubMenu(DATEFORMATMENU, DATEFORMATMENU, 0, "Format Date");
      dateFormatMenu.add(DATEFORMATMENU, DAYMONTH, 0, "Day/Month");
      dateFormatMenu.add(DATEFORMATMENU, MONTHDAY, 1, "Month/Day");
      
      SubMenu quitMenu = menu.addSubMenu(QUITMENU, QUITMENU, 0, "Quit App");
      
      //menu.setGroupCheckable(Menus.TIMEMODE.ordinal(), true, true);
 
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
    	    	
    	switch (item.getItemId()) 
    	{
    		case SETCOLORSMENU:	// Set Colors
			// get the color picker
  			 text = "Set Colors not implemented yet\n\rDue in v0.3";
					toast = Toast.makeText(context, text, duration);
					toast.show();
					break;
					
			case RECTANGLE:	// Set Shapes
				drawView.timeDateBlocksDefaults.setShape(TimeDateBlock.RECTANGLE);
				break;
				
			case CIRCLE:	// Set Shapes
				drawView.timeDateBlocksDefaults.setShape(TimeDateBlock.CIRCLE);
				break;
				
			case OVAL:	// Set Shapes
				drawView.timeDateBlocksDefaults.setShape(TimeDateBlock.OVAL);
				break;
			
			case XSTART:
				break;
			
			case YSTART:
				break;
			
			case FULLSCREEN:
				break;
			
			case XSIZE:
				drawView.timeDateBlocksDefaults.setWidth(80);
				break;
			
			case YSIZE:
				drawView.timeDateBlocksDefaults.setHeight(40);
				break;
			
			case	TWELVE:	// 12/24 Hour Mode
				//settings.GetSettings(context, drawView, tdBlock);
				drawView.timeDateBlocksDefaults.setAmPmMode(true);
				//settings.SetSettings(context, drawView, tdBlock);
				break;
    			
    		case	TWENTYFOUR:
    			drawView.timeDateBlocksDefaults.setAmPmMode(false);
    			break;
    			
    		case DAYMONTH:	// Date Format
    			tdBlock.setDayMonthDisplayMode(true);
    			break;
    			
    		case MONTHDAY:	// Date Format
    			tdBlock.setDayMonthDisplayMode(false);
    			break;
    			
    		case QUITMENU:	// Quit
    			quitApp();
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
