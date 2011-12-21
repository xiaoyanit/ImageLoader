package com.novoda.imageloader.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.novoda.imageloader.core.Settings;
import com.novoda.imageloader.core.file.FileUtil;

public class BitmapUtil {

  private static final String TAG = "ImageLoader";

  public Bitmap decodeFileAndScale(File f, boolean scale, Settings settings) {
    try {
    	updateLastModifiedForCache(f);
      int suggestedSize = settings.getImageHeight();
      if (settings.getImageWidth() > settings.getImageHeight()) {
      	suggestedSize = settings.getImageHeight();
      }
      Bitmap unscaledBitmap = decodeFile(f, suggestedSize);
      return scaleBitmap(unscaledBitmap, settings.getImageWidth(), settings.getImageHeight());
    } catch (Exception e) {
      Log.e(TAG, e.getMessage(), e);
    }
    return null;
  }

  private Bitmap scaleBitmap(Bitmap b, int width, int height) {
  	if(b == null) {
  		return null;
  	}
    int imageHeight = b.getHeight();
    int imageWidth = b.getWidth();
    int finalWidth = width;
    int finalHeight = height;
    if (imageHeight > imageWidth) {
      float factor = ((float) height) / ((float) imageHeight);
      finalHeight = new Float(imageHeight * factor).intValue();
      finalWidth = new Float(imageWidth * factor).intValue();
    } else {
      float factor = ((float) width) / ((float) imageWidth);
      finalHeight = new Float(imageHeight * factor).intValue();
      finalWidth = new Float(imageWidth * factor).intValue();
    }
    Bitmap scaledBitmap = Bitmap.createScaledBitmap(b, finalWidth, finalHeight, true);
    return scaledBitmap;
  }

  public Bitmap decodeDefaultAndScaleBitmap(Context context, Settings settings) {
    return decodeResourceAndScale(context, settings, settings.getDefaultImageId());
  }
  
  public Bitmap decodeNotFoundAndScaleBitmap(Context context, Settings settings) {
    return decodeResourceAndScale(context, settings, settings.getNotFoundImageId());
  }

  private Bitmap decodeResourceAndScale(Context context, Settings settings, int resourceId) {
    Bitmap image = BitmapFactory.decodeResource(context.getResources(), resourceId);
    return scaleBitmap(image, settings.getImageWidth(), settings.getImageHeight());
  }
  
  private void updateLastModifiedForCache(File f) {
	  f.setLastModified(System.currentTimeMillis());
  }

	private Bitmap decodeFile(File f, int suggestedSize) {
		FileInputStream fis = null;
    try {
    	final BitmapFactory.Options o = new BitmapFactory.Options();
    	o.inJustDecodeBounds = true;
    	fis  = new FileInputStream(f);
    	BitmapFactory.decodeStream(fis, null, o);
    	
    	final int requiredSize = suggestedSize;
    	int widthTmp = o.outWidth, heightTmp = o.outHeight;
    	int scale = calculateScale(requiredSize, widthTmp, heightTmp);
    	
      // decode with inSampleSize
      final BitmapFactory.Options o2 = new BitmapFactory.Options();
      o2.inSampleSize = scale;
      o2.inTempStorage = new byte[64 * 1024];
      o2.inPurgeable = true;
      Bitmap bitmap = null;
      try {
        bitmap =
          BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
      } catch (final Throwable e) {
        System.gc();
      }
      return bitmap;
		} catch (FileNotFoundException e) {
			Log.e(TAG, e.getMessage(), e);
			return null;
		} finally {
			new FileUtil().closeSilently(fis);
		}
  }

	private int calculateScale(final int requiredSize, int widthTmp, int heightTmp) {
	  int scale = 1;
	  while (true) {
	  	if ((widthTmp / 2) < requiredSize
	  			|| (heightTmp / 2) < requiredSize) {
	  			break;
	  	}
	  	widthTmp /= 2;
	  	heightTmp /= 2;
	  	scale *= 2;
	  }
	  return scale;
  }

}
