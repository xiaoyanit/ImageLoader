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

import java.lang.ref.SoftReference;
import java.util.HashMap;

import android.graphics.Bitmap;

public class SoftMapCache implements CacheManager {

    private HashMap<String, SoftReference<Bitmap>> cache = new HashMap<String, SoftReference<Bitmap>>();

    @Override
    public boolean hasBitmap(String url) {
        if (!cache.containsKey(url)) {
            return false;
        }
        if (cache.get(url) == null) {
            return false;
        }
        return true;
    }

    @Override
    public Bitmap get(String url) {
        SoftReference<Bitmap> bmpr = cache.get(url);
        if (bmpr == null) {
            return null;
        }
        return bmpr.get();
    }

    @Override
    public void put(String url, Bitmap bmp) {
        cache.put(url, new SoftReference<Bitmap>(bmp));
    }

    @Override
    public void clean() {
        cache.clear();
    }

}
