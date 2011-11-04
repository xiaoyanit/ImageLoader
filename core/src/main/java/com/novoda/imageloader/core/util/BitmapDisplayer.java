package com.novoda.imageloader.core.util;

import android.graphics.Bitmap;

import com.novoda.imageloader.core.model.ImageWrapper;

public class BitmapDisplayer implements Runnable {
  private Bitmap bitmap;
  private ImageWrapper imageView;

  public BitmapDisplayer(Bitmap b, ImageWrapper i) {
    bitmap = b;
    imageView = i;
  }
  
  public void runOnUiThread() {
  	imageView.runOnUiThread(this);
  }

  @Override
  public void run() {
    if (bitmap == null) {
      return;
    }
    imageView.setBitmap(bitmap);
  }
}
