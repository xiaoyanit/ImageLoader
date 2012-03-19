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
package com.novoda.imageloader.core.cache;

import android.graphics.Bitmap;


public class LruBitmapCache implements CacheManager {

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
