package com.piggy.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Rect;
import android.os.Build.VERSION;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.MeasureSpec;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ScreenShootUtil {
	public static Activity gActivity = null;
	
	public static Bitmap getScreenShootBitmap()
	{
		if (null == gActivity)
			return null;
		
		DisplayMetrics dm = new DisplayMetrics();
		gActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels;
		int height = dm.heightPixels;
		Bitmap resultBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		
		Rect frame = new Rect();
		gActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		int statusBarHeight = frame.top;
		
        View decorview = gActivity.getWindow().getDecorView();
        decorview.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
        decorview.layout(0, 0, decorview.getMeasuredWidth(), decorview.getMeasuredHeight());
        decorview.setDrawingCacheEnabled(true);    
        decorview.buildDrawingCache();
        resultBitmap = decorview.getDrawingCache(); 

		Bitmap tempBitmap = Bitmap.createBitmap(resultBitmap, 0, 0, width, height);
		decorview.destroyDrawingCache();
		
		return tempBitmap;
	}
	
	public static boolean saveScreenShootToPngFile(String path, String fileName)
	{
		Bitmap bitmap = getScreenShootBitmap();
		if (null != bitmap)
		{
        	try
        	{
        		File file = new File(path + File.separator + fileName);
        		file.createNewFile();
        		FileOutputStream fOut = new FileOutputStream(file);
        		bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            	fOut.flush();
            	fOut.close();
            	return true;
        	}
        	catch (FileNotFoundException e)
        	{
        		e.printStackTrace();
        	} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	@SuppressLint("NewApi")
	public static Bitmap getFrostedGlassBitmap(Activity activity, Bitmap srcBitmap)
	{
		Bitmap resultBitmap = null;
		
		if (VERSION.SDK_INT <= 16)
			return null;
		
		resultBitmap = srcBitmap.copy(srcBitmap.getConfig(), true);
		RenderScript rs = RenderScript.create(activity);
		Allocation input = Allocation.createFromBitmap(rs, srcBitmap, 
				Allocation.MipmapControl.MIPMAP_NONE,
				Allocation.USAGE_SCRIPT);
		Allocation output = Allocation.createTyped(rs, input.getType());
		ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
		script.setRadius(5);
		script.setInput(input);
		script.forEach(output);
		output.copyTo(resultBitmap);
		
		return resultBitmap;
	}
	
}
