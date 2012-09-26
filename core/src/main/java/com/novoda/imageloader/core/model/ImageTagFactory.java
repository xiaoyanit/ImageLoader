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

    private int previewImageWidth;
    private int previewImageHeight;
    private int width;
    private int height;
    private int defaultImageResId;
    private int errorImageResId;
    private boolean useOnlyCache;
    private boolean saveThumbnail;
    private boolean useSameUrlForPreviewImage;

    public static ImageTagFactory newInstance() {
        return new ImageTagFactory();
    }

    private ImageTagFactory() {
    }

    public static ImageTagFactory newInstance(int width, int height, int defaultImageResId) {
        ImageTagFactory imageTagFactory = new ImageTagFactory();
        imageTagFactory.setInitialSizeParams(imageTagFactory, width, height);
        imageTagFactory.setInitialImageId(imageTagFactory, defaultImageResId);
        return imageTagFactory;
    }

    public static ImageTagFactory newInstance(Context context, int defaultImageResId) {
        ImageTagFactory imageTagFactory = new ImageTagFactory();
        Display display = imageTagFactory.prepareDisplay(context);
        imageTagFactory.setInitialSizeParams(imageTagFactory, display.getWidth(), display.getHeight());
        imageTagFactory.setInitialImageId(imageTagFactory, defaultImageResId);
        return imageTagFactory;
    }

    private ImageTagFactory setInitialSizeParams(ImageTagFactory imageTagFactory, int width, int height) {
        imageTagFactory.setWidth(width);
        imageTagFactory.setHeight(height);
        return imageTagFactory;
    }

    private ImageTagFactory setInitialImageId(ImageTagFactory imageTagFactory, int defaultImageResId) {
        imageTagFactory.setDefaultImageResId(defaultImageResId);
        imageTagFactory.setErrorImageId(defaultImageResId);
        return imageTagFactory;
    }

    public ImageTagFactory(Context context, int defaultImageResId) {
        Display d = prepareDisplay(context);
        this.width = d.getWidth();
        this.height = d.getHeight();
        this.defaultImageResId = defaultImageResId;
        this.errorImageResId = defaultImageResId;
    }

    protected Display prepareDisplay(Context context) {
        Display d = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        d.getMetrics(dm);
        return d;
    }

    public void setDefaultImageResId(int defaultImageResId) {
        this.defaultImageResId = defaultImageResId;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setErrorImageId(int errorImageResId) {
        this.errorImageResId = errorImageResId;
    }

    public void usePreviewImage(int previewImageWidth, int previewImageHeight, boolean useSameUrlForPreviewImage) {
        this.previewImageWidth = previewImageWidth;
        this.previewImageHeight = previewImageHeight;
        this.useSameUrlForPreviewImage = useSameUrlForPreviewImage;
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
        if(useSameUrlForPreviewImage) {
            it.setPreviewUrl(url);
        }
        it.setPreviewHeight(previewImageHeight);
        it.setPreviewWidth(previewImageWidth);
        return it;
    }

}
