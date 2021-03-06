package com.example.tbrbinaryclockv0_2;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.OutputStream;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

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
//		TimeDateBlock tdBlock = new TimeDateBlock();
		TimeDateBlock dvtdb = drawView.timeDateBlock;
		
		settings = context.getSharedPreferences(PREFS_NAME, 0);
		editor = settings.edit();
		
		//Boolean mode = dvtdb.isAmPmMode();
		editor.putBoolean("amPmMode", dvtdb.isAmPmMode());
		editor.putInt("OnColor",  drawView.getTimeOnColor());
		editor.putInt("OffColor",  drawView.getTimeOffColor());
		editor.putInt("Shape", dvtdb.getShape());
		
		int[] t = drawView.getTimeCenter();
		editor.putInt("TimeCenterX", t[0]);
		editor.putInt("TimeCenterY", t[1]);
		
		editor.commit();
	}
	
	public void GetSettings(Context context, DrawView drawView, TimeDateBlock tdBlock)
	{
		TimeDateBlock dvtdb = drawView.timeDateBlock;
		int[] timeCenter = {0,0};
		
		settings = context.getSharedPreferences(PREFS_NAME, 0);
		Boolean mode = settings.getBoolean("amPmMode",  dvtdb.isAmPmMode());
		dvtdb.setAmPmMode(mode);
		int onColor = settings.getInt("OnColor", -1 ^ 0xFF000000);
		int offColor = settings.getInt("OffColor", -1);
		timeCenter[0] = settings.getInt("TimeCenterX",  100000);
		timeCenter[1] = settings.getInt("TimeCenterY",  100000);
		drawView.setTimeCenter(timeCenter);
		//drawView.setTimeOnColor(onColor);
		//drawView.setTimeOffColor(offColor);
		//dvtdb.SetOffColor(offColor);
		dvtdb.setShape(settings.getInt("Shape", TimeDateBlock.CIRCLE));
		
//	  	  int height = drawView.getCanvas().getHeight();
//	  	  int width = drawView.getCanvas().getWidth();
//	  	  
//	  	  int w = width;
	  	  


	}
	
	public int[] getTimeCenter()
	{
		int[] timeCenter = {0,0};
		
		timeCenter[0] = settings.getInt("TimeCenterX", 100000);
		timeCenter[1] = settings.getInt("TimeCenterY", 100000);
		
		return timeCenter;
	}
	
	public int[] getDateCenter()
	{
		int[] dateCenter = {0,0};
		
		dateCenter[0] = settings.getInt("TimeCenterX", 100000);
		dateCenter[1] = settings.getInt("TimeCenterY", 100000);
		
		return dateCenter;
	}
	
}
//	public void WriteSettings(DrawView drawView, UpdateTime timer)
//	{
//		String output = "";
//		TimeDateBlock tdBlock = new TimeDateBlock();
//		//UpdateTime updateTime = new UpdateTime(drawView.getDrawView());
//		
//		BufferedOutputStream bos = null;
//		FileOutputStream fos = null;
//
//		try 
//		{	
//			String state = Environment.getExternalStorageState();
//			File file = new File(Environment.getExternalStorageDirectory() + File.separator + filename);
//			if(file.exists() == false)
//				file.createNewFile();
//			
//			//write the bytes in file
//			
//			// create FileOutputStream from filename
//		    fos = new FileOutputStream(Environment.getExternalStorageDirectory() + File.separator + filename);
//
//			// create BufferedOutputStream for FileOutputStream
//			bos = new BufferedOutputStream(fos);
//
//			// mode 12/24
//			// mode MD/DM
//			// shapes
//			int[] center = tdBlock.getCenter();
//			output += Integer.toString(center[0]) + ".";
//			output += Integer.toString(center[1]) + ".";
//			
//			center = tdBlock.getTimeBlockCenter();
//			output += Integer.toString(center[0]) + ".";
//			output += Integer.toString(center[1]) + ".";
//			
//			center = tdBlock.getDateBlockCenter();
//			output += Integer.toString(center[0]) + ".";
//			output += Integer.toString(center[1]) + ".";
//			
//			int parameter = tdBlock.getHeight();
//			output += Integer.toString(parameter) + ".";
//			
//			parameter = tdBlock.getWidth();
//			output += Integer.toString(parameter) + ".";
//			
//			parameter = tdBlock.GetOnColor();
//			output += Integer.toString(parameter) + ".";
//			
//			parameter = tdBlock.GetOffColor();
//			output += Integer.toString(parameter) + ".";
//			
//			parameter = tdBlock.getBorder();
//			output += Integer.toString(parameter) + ".";
//						
//			bos.write(output.getBytes());
//		}
//		catch (FileNotFoundException fnfe) 
//		{
//			String err;
//			err = fnfe.toString();
//			System.out.println("File not found" + fnfe);
//			
//		}
//		catch (IOException ioe) 
//		{
//			System.out.println("Error while writing to file" + ioe);
//		}
//		finally 
//		{
//			try 
//			{
//				if (bos != null) 
//				{
//					bos.flush();
//					bos.close();
//					fos.close();
//				}
//			}
//			catch (Exception e) 
//			{
//				System.out.println("Error while closing streams" + e);
//			}
//		}
//	}
//	
//	@SuppressWarnings("unused")
//	public void ReadSettings()
//	{
//		byte inputArray[] = new byte[1000];
//
//		FileInputStream fis;
//		BufferedInputStream bis;
//		// create FileOutputStream from filename
//		
//		try 
//		{	
//			fis = new FileInputStream(Environment.getExternalStorageDirectory() + File.separator + filename);
//			bis = new BufferedInputStream(fis);
//			bis.read(inputArray);
//			bis.close();
//			fis.close();
//			
//			String settingsIn = inputArray.toString();
//			String s = settingsIn;
//		}
//		catch (FileNotFoundException fnfe) 
//		{
//			System.out.println("File not found" + fnfe);
//		}
//		catch (IOException ioe) 
//		{
//			System.out.println("Error while writing to file" + ioe);
//		}
//		finally 
//		{
//			try 
//			{
////				if (bis != null) 
////				{
////					bis.close();
////				}
//			}
//			catch (Exception e) 
//			{
//				System.out.println("Error while closing streams" + e);
//			}
//		}
//
//	}
//}
