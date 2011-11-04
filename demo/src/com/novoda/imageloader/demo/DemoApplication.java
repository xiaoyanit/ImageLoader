package com.novoda.imageloader.demo;

import android.app.Application;

import com.novoda.imageloader.core.ImageLoader;
import com.novoda.imageloader.core.cache.BitmapCache;
import com.novoda.imageloader.core.cache.LruBitmapCache;
import com.novoda.imageloader.core.loader.SingleThreadedImageLoader;
import com.novoda.imageloader.core.util.Settings;
import com.novoda.imageloader.core.util.SettingsBuilder;

public class DemoApplication extends Application {

  // TODO add this to your class
  private static ImageLoader imageLoader;
  private static ImageLoader thumbnailImageLoader;
  
  @Override
  public void onCreate() {
    super.onCreate();
    // TODO add this to your classs
    SettingsBuilder builder = new SettingsBuilder();
    Settings settings = builder.build(this);
    imageLoader = new SingleThreadedImageLoader(this, settings);
    thumbnailImageLoader = new SingleThreadedImageLoader(this, settings) {
    	@Override
    	protected BitmapCache createCache() {
    		return new LruBitmapCache(50);
    	}
    };
    //
  }

  // TODO add this to your class
  public static ImageLoader getImageLoader() {
    return imageLoader;
  }
  
  // TODO add this to your class
  public static ImageLoader getThumbnailImageLoader() {
    return thumbnailImageLoader;
  }
  
}
