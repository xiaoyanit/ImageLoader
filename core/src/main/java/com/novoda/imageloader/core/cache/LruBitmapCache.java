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

import java.lang.reflect.Method;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;

public class LruBitmapCache implements CacheManager {

    public static final int DEFAULT_MEMORY_CACHE_PERCENTAGE = 25;
    private static final int DEFAULT_MEMORY_CAPACITY_FOR_DEVICES_OLDER_THAN_API_LEVEL_4 = 12;
    private LruCache<String, Bitmap> cache;
    private int capacity;

    /**
     * @param context
     * @param percentageOfMemoryForCache 1-80
     */
    public LruBitmapCache(Context context, int percentageOfMemoryForCache) {
        int memClass = 0;
        ActivityManager am = ((ActivityManager) context.getSystemService(
                Context.ACTIVITY_SERVICE));
        try {
            Method m = ActivityManager.class.getMethod("getMemoryClass");
            memClass = (Integer)m.invoke(am);
        } catch (Exception e) {
        }
        if(memClass == 0) {
            memClass = DEFAULT_MEMORY_CAPACITY_FOR_DEVICES_OLDER_THAN_API_LEVEL_4;
        }
        if(percentageOfMemoryForCache < 0) {
            percentageOfMemoryForCache = 0;
        }
        if(percentageOfMemoryForCache > 81) {
            percentageOfMemoryForCache = 80;
        }
        this.capacity = (1024 *1024*(memClass * percentageOfMemoryForCache))/100;
        reset();
    }
    
    public LruBitmapCache(Context context) {
        this(context, DEFAULT_MEMORY_CACHE_PERCENTAGE);
    }

    private void reset() {
        cache = new LruCache<String, Bitmap>(capacity) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getRowBytes()*bitmap.getHeight();
            }
        };
    }

    @Override
    public Bitmap get(String url, int width, int height) {
        int i = width;
        if(width < height) {
            i = height;
        }
        return cache.get(url + i);
    }

    @Override
    public void put(String url, Bitmap bmp) {
        int i = bmp.getWidth();
        if(bmp.getWidth() < bmp.getHeight()) {
            i = bmp.getHeight();
        }
        cache.put(url + i, bmp);
    }

    @Override
    public void clean() {
        reset();
    }

}
