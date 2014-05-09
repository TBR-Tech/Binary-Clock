package com.example.tbrbinaryclockv0_2;

import android.content.Context;
import android.content.SharedPreferences;

public class Settings 
{
	//File filename = new File("TBRClockSettings.txt");
	String filename = "TBRClockSettings.txt";
	public static final String PREFS_NAME = "TBRClockPrefs";
	SharedPreferences settings;
	SharedPreferences.Editor editor;
	
	public Settings()
	{
		;
	}
	
	public void SetSettings(Context context, DrawView drawView, TimeDateBlock tdBlock)
	{
		settings = context.getSharedPreferences(PREFS_NAME, 0);
		editor = settings.edit();
		
		//Boolean mode = dvtdb.isAmPmMode();
		editor.putBoolean("amPmMode", drawView.amPmMode);
		editor.putInt("OnColor",  drawView.getTimeOnColor());
		editor.putInt("OffColor",  drawView.getTimeOffColor());
		editor.putInt("Shape", drawView.getShape());
		
		int[] t = drawView.getTimeCenter();
		editor.putInt("TimeCenterX", t[0]);
		editor.putInt("TimeCenterY", t[1]);
		t = drawView.getDateCenter();
		editor.putInt("DateCenterX", t[0]);
		editor.putInt("DateCenterY", t[1]);
		
		editor.commit();
	}
	
	public void GetSettings(Context context, DrawView drawView, TimeDateBlock tdBlock)
	{
		int[] timeCenter = {0,0};
		
		settings = context.getSharedPreferences(PREFS_NAME, 0);
		Boolean mode = settings.getBoolean("amPmMode",  drawView.amPmMode);
		drawView.setAmPmMode(mode);
		int onColor = settings.getInt("OnColor", -1 ^ 0xFF000000);
		int offColor = settings.getInt("OffColor", -1);
		timeCenter[0] = settings.getInt("TimeCenterX",  -1);
		timeCenter[1] = settings.getInt("TimeCenterY",  -1);
		drawView.setTimeCenter(timeCenter);
		drawView.setTimeOnColor(onColor);
		drawView.setTimeOffColor(offColor);
		//dvtdb.SetOffColor(offColor);
		drawView.setShape(settings.getInt("Shape", drawView.CIRCLE));		
	}
	
	public int[] getTimeCenter()
	{
		int[] timeCenter = {0,0};
		
		timeCenter[0] = settings.getInt("TimeCenterX", -1);
		timeCenter[1] = settings.getInt("TimeCenterY", -1);
		
		return timeCenter;
	}
	
	public int[] getDateCenter()
	{
		int[] dateCenter = {0,0};
		
		dateCenter[0] = settings.getInt("TimeCenterX", -1);
		dateCenter[1] = settings.getInt("TimeCenterY", -1);
		
		return dateCenter;
	}	
}
