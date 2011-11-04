package com.novoda.imageloader.core;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

public interface ImageLoader {

  void load(ImageView imageView);
  
  void loadFromCacheOnly(ImageView imageView);

  Bitmap getBitmap(String url, int width, int height);

  void deleteFileCache(Context context);

  void reduceFileCache(Context context);

  void cleanCache();

  String getFilePath(String imageUrl);

}