/**
 * Copyright 2012 Novoda Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.novoda.imageloader.core.bitmap;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

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
  	Bitmap b = null;
  	try {
  		b = BitmapFactory.decodeResource(c.getResources(), resourceId);
  		return scaleBitmap(b, iw.getWidth(), iw.getHeight());
  	} catch (final Throwable e) {
    	System.gc();
  	}
  	return null;
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
			Bitmap bitmap = null;
			FileInputStream fis = null;
      try {
      	int scale = evaluateScale(f, suggestedSize);
      	final BitmapFactory.Options options = new BitmapFactory.Options();
      	options.inSampleSize = scale;
      	options.inTempStorage = new byte[BUFFER_SIZE];
      	options.inPurgeable = true;
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

	private void closeSilently(Closeable c) {
		try {
      if (c != null) {
        c.close();
      }
    } catch (Exception e) {
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
