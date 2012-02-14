package com.novoda.imageloader.core.bitmap;

import java.io.File;
import java.io.FileInputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.novoda.imageloader.core.file.util.FileUtil;
import com.novoda.imageloader.core.model.ImageWrapper;

public class BitmapUtil {

  private static final int BUFFER_SIZE = 64 * 1024;

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
    Bitmap scaled = null;
    try {
    	scaled = Bitmap.createScaledBitmap(b, finalWidth, finalHeight, true);
  	} catch (final Throwable e) {
    	System.gc();
  	}
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
    	int scale = evaluateScale(f, suggestedSize);
      final BitmapFactory.Options options = new BitmapFactory.Options();
      options.inSampleSize = scale;
      options.inTempStorage = new byte[BUFFER_SIZE];
      options.inPurgeable = true;
      Bitmap bitmap = null;
      FileInputStream fis = null;
      try {
      	fis  = new FileInputStream(f);
        bitmap = BitmapFactory.decodeStream(fis, null, options);
      } catch (final Throwable e) {
        System.gc();
      } finally {
      	closeSilently(fis);
      }
      return bitmap;
  }

	private int evaluateScale(File f, int suggestedSize) {
	  final BitmapFactory.Options o = new BitmapFactory.Options();
	  o.inJustDecodeBounds = true;
	  decodeFileToPopulateOptions(f, o);
	  return calculateScale(suggestedSize, o.outWidth, o.outHeight);
  }

	private void decodeFileToPopulateOptions(File f, final BitmapFactory.Options o) {
	  FileInputStream fis = null;
	  try {
	  	fis  = new FileInputStream(f);
	  	BitmapFactory.decodeStream(fis, null, o);
	  	closeSilently(fis);
	  } catch (final Throwable e) {
	    System.gc();
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
