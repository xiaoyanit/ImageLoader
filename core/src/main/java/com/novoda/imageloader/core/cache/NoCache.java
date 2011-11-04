package com.novoda.imageloader.core.cache;

import android.graphics.Bitmap;

public class NoCache implements BitmapCache {

  @Override
  public boolean hasBitmap(String url) {
    return false;
  }

  @Override
  public Bitmap get(String url) {
    return null;
  }

  @Override
  public void put(String url, Bitmap bmp) {
  }

  @Override
  public void clean() {
  }

}
