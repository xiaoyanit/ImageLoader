package com.novoda.imageloader.core.cache;

import android.graphics.Bitmap;

import com.android.camera.gallery.LruCache;

public class LruBitmapCache implements BitmapCache {

  private LruCache<String, Bitmap> cache;

  public LruBitmapCache(int capacity) {
  	cache = new LruCache<String, Bitmap>(capacity);
  }
  
  @Override
  public boolean hasBitmap(String url) {
    return cache.hasKey(url);
  }

  @Override
  public Bitmap get(String url) {
    return cache.get(url);
  }

  @Override
  public void put(String url, Bitmap bmp) {
    cache.put(url, bmp);
  }

  @Override
  public void clean() {
    cache.clear();
  }
  
}
