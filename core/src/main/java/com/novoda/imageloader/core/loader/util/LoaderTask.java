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
package com.novoda.imageloader.core.loader.util;

import java.io.File;
import java.lang.ref.WeakReference;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.novoda.imageloader.core.LoaderContext;
import com.novoda.imageloader.core.exception.ImageNotFoundException;
import com.novoda.imageloader.core.model.ImageWrapper;

public class LoaderTask extends AsyncTask<String, Void, Bitmap> {

    private WeakReference<ImageView> imageViewReference;
    private LoaderContext loaderContext;
    private String url;

    public LoaderTask(ImageView imageView, LoaderContext loaderContext) {
        this.imageViewReference = new WeakReference<ImageView>(imageView);
        this.loaderContext = loaderContext;
    }

    @Override
    protected Bitmap doInBackground(String... arg0) {
        if (imageViewReference == null) {
            return null;
        }
        ImageView imageView = imageViewReference.get();
        if (imageView == null) {
            return null;
        }
        ImageWrapper w = new ImageWrapper(imageView);
        url = w.getUrl();
        if (url == null || url.length() <= 0) {
            return null;
        }
        int width = w.getWidth();
        int height = w.getHeight();
        if (!url.equals(new ImageWrapper(imageView).getCurrentUrl())) {
            return null;
        }
        File f = loaderContext.getFileManager().getFile(url);
        if (f.exists()) {
            if (!url.equals(new ImageWrapper(imageView).getCurrentUrl())) {
                return null;
            }
            Bitmap b = loaderContext.getBitmapUtil().decodeFileAndScale(f, width, height);
            if (b != null) {
                return b;
            }
        }
        try {
            loaderContext.getNetworkManager().retrieveImage(url, f);
        } catch (ImageNotFoundException inf) {
            return getResource(w, w.getNotFoundResourceId());
        }
        if (!url.equals(new ImageWrapper(imageView).getCurrentUrl())) {
            return null;
        }
        return loaderContext.getBitmapUtil().decodeFileAndScale(f, width, height);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (bitmap == null) {
            return;
        }
        if (isCancelled()) {
            bitmap = null;
            return;
        }
        if (imageViewReference == null) {
            return;
        }
        ImageView imageView = imageViewReference.get();
        if (imageView == null) {
            return;
        }
        if (!url.equals(new ImageWrapper(imageView).getCurrentUrl())) {
            return;
        }
        imageView.setImageBitmap(bitmap);
    }

    private Bitmap getResource(ImageWrapper w, int resId) {
        String key = "resource" + resId + w.getHeight() + w.getHeight();
        if (loaderContext.getResBitmapCache().hasBitmap(key)) {
            Bitmap b = loaderContext.getResBitmapCache().get(key);
            if (b != null) {
                return b;
            }
        }
        Bitmap b = loaderContext.getBitmapUtil().scaleResourceBitmap(w, resId);
        loaderContext.getResBitmapCache().put(key, b);
        return b;
    }

}
