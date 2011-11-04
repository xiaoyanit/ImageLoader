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
  	FileInputStream fis = null;
    try {
      f.setLastModified(System.currentTimeMillis());
      fis = new FileInputStream(f);
      Bitmap unscaledBitmap = BitmapFactory.decodeStream(fis);
      return scaleBitmap(unscaledBitmap, width, height);
    } catch (FileNotFoundException e) {
      Log.e(TAG, e.getMessage(), e);
    } finally {
    	new FileUtil().closeSilently(fis);
    }
    return null;
  }
  
  public Bitmap scaleResourceBitmap(ImageWrapper iw, int resourceId) {
  	Log.v("XXX", "scaleResourceBitmap");
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
    return Bitmap.createScaledBitmap(b, finalWidth, finalHeight, true);
  }

}
