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
package com.novoda.imageloader.core.model;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

public class ImageTagFactory {

    private int width;
    private int height;
    private int defaultImageResId;
    private int errorImageResId;
    private boolean useOnlyCache;
    private boolean saveThumbnail;

    public ImageTagFactory(int width, int height, int defaultImageResId) {
        this.width = width;
        this.height = height;
        this.defaultImageResId = defaultImageResId;
        this.errorImageResId = defaultImageResId;
    }

    public ImageTagFactory(Context context, int defaultImageResId) {
        Display d = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        d.getMetrics(dm);
        this.width = d.getWidth();
        this.height = d.getHeight();
        this.defaultImageResId = defaultImageResId;
        this.errorImageResId = defaultImageResId;
    }

    public void setErrorImageId(int errorImageResId) {
        this.errorImageResId = errorImageResId;
    }

    public void setUseOnlyCache(boolean useOnlyCache) {
        this.useOnlyCache = useOnlyCache;
    }

    public void setSaveThumbnail(boolean saveThumbnail) {
        this.saveThumbnail = saveThumbnail;
    }

    public ImageTag build(String url) {
        ImageTag it = new ImageTag(url, defaultImageResId, errorImageResId, width, height);
        it.setUseOnlyCache(useOnlyCache);
        it.setSaveThumbnail(saveThumbnail);
        return it;
    }

}
