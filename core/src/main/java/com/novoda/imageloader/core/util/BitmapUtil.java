package com.novoda.imageloader.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.novoda.imageloader.core.file.FileUtil;
import com.novoda.imageloader.core.model.ImageWrapper;

public class BitmapUtil {

  private static final String TAG = "ImageLoader";

  public Bitmap decodeFileAndScale(File f, int width, int height) {
      updateLastModifiedForCache(f);
      int suggestedSize = height;
      if (width > height) {
      	suggestedSize = height;
      }
      Bitmap unscaledBitmap = decodeFile(f, suggestedSize);
      if(unscaledBitmap == null) {
      	return null;
      }
      return scaleBitmap(unscaledBitmap, width, height);
  }

	public Bitmap scaleResourceBitmap(ImageWrapper iw, int resourceId) {
  	Context c = iw.getContext();
  	Bitmap i = BitmapFactory.decodeResource(c.getResources(), resourceId);
    return scaleBitmap(i, iw.getWidth(), iw.getHeight());
  }

  public Bitmap scaleBitmap(Bitmap b, int width, int height) {
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
    Bitmap scaled = Bitmap.createScaledBitmap(b, finalWidth, finalHeight, true);
    recycle(b);
    return scaled;
  }

  private void recycle(Bitmap scaled) {
  	try {
  		scaled.recycle();
  	} catch (Exception e) {
  		//
  	}
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
    	closeSilently(fis);
    	
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
      	fis  = new FileInputStream(f);
        bitmap = BitmapFactory.decodeStream(fis, null, o2);
      } catch (final Throwable e) {
        System.gc();
      } finally {
      	closeSilently(fis);
      }
      return bitmap;
		} catch (FileNotFoundException e) {
			Log.e(TAG, e.getMessage(), e);
			return null;
		} finally {
			closeSilently(fis);
		}
  }

	private void closeSilently(FileInputStream fis) {
	  new FileUtil().closeSilently(fis);
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
