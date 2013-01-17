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

import android.content.Context;
import android.graphics.Bitmap;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.novoda.imageloader.core.LoaderSettings;
import com.novoda.imageloader.core.OnImageLoadedListener;
import com.novoda.imageloader.core.exception.ImageNotFoundException;
import com.novoda.imageloader.core.model.ImageTag;
import com.novoda.imageloader.core.model.ImageWrapper;

import java.io.File;
import java.lang.ref.WeakReference;

public class LoaderTask extends AsyncTask<String, Void, Bitmap> {

    private final WeakReference<ImageView> imageViewReference;
    private final LoaderSettings loaderSettings;
    private final WeakReference<OnImageLoadedListener> onImageLoadedListener;

    private String url;
    private boolean saveScaledImage;
    private boolean useCacheOnly;
    private int width;
    private int height;
    private int notFoundResourceId;

    public LoaderTask(ImageView imageView, LoaderSettings loaderSettings) {
        this(imageView, loaderSettings, null);
    }

    public LoaderTask(ImageView imageView, LoaderSettings loaderSettings, WeakReference<OnImageLoadedListener> onImageLoadedListener) {
        this.imageViewReference = new WeakReference<ImageView>(imageView);
        this.loaderSettings = loaderSettings;
        this.onImageLoadedListener = onImageLoadedListener;
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
        ImageWrapper imageWrapper = setAndValidateTagInformation(imageView);

        if (url == null || url.length() <= 0 || url.equals("_url_error")) {
            return getNotFoundImage(imageWrapper.getContext());
        }

        if (hasImageViewUrlChanged(imageView)) {
            return null;
        }
        Bitmap b = loaderSettings.getCacheManager().get(url, width, height);
        if (b != null && !b.isRecycled()) {
            return b;
        }
        File imageFile = getImageFile(imageWrapper);
        if (!imageFile.exists()) {
            if (useCacheOnly) {
                return null;
            }
            try {
                loaderSettings.getNetworkManager().retrieveImage(url, imageFile);
            } catch (ImageNotFoundException inf) {
                return getNotFoundImage(imageWrapper.getContext());
            }
        }
        if (hasImageViewUrlChanged(imageView)) {
            return null;
        }
        return getImageFromFile(imageFile);
    }

    private Bitmap getImageFromFile(File imageFile) {
        Bitmap b;
        if (loaderSettings.isAlwaysUseOriginalSize()) {
            b = loaderSettings.getBitmapUtil().decodeFile(imageFile, width, height);
        } else {
            b = loaderSettings.getBitmapUtil().decodeFileAndScale(imageFile, width, height, loaderSettings.isAllowUpsampling());
        }

        if (b == null) {
            // decoding failed
            return b;
        }

        if (saveScaledImage) {
            saveScaledImage(imageFile, b);
        }
        loaderSettings.getCacheManager().put(url, b);
        return b;
    }

    private ImageWrapper setAndValidateTagInformation(ImageView imageView) {
        ImageWrapper imageWrapper = new ImageWrapper(imageView);
        url = imageWrapper.getUrl();
        width = imageWrapper.getWidth();
        height = imageWrapper.getHeight();
        notFoundResourceId = imageWrapper.getNotFoundResourceId();
        useCacheOnly = imageWrapper.isUseCacheOnly();
        return imageWrapper;
    }

    private void saveScaledImage(File imageFile, Bitmap b) {
        loaderSettings.getFileManager().saveBitmap(imageFile.getAbsolutePath(), b, width, height);
    }

    private File getImageFile(ImageWrapper imageWrapper) {
        File imageFile = null;
        if (imageWrapper.isSaveThumbnail()) {
            imageFile = loaderSettings.getFileManager().getFile(url, width, height);
        }
        if (imageFile == null || !imageFile.exists()) {
            imageFile = loaderSettings.getFileManager().getFile(url);
            if (imageWrapper.isSaveThumbnail()) {
                saveScaledImage = true;
            }
        }
        return imageFile;
    }

    private boolean hasImageViewUrlChanged(ImageView imageView) {
        if (url == null) {
            return false;
        } else {
            return !url.equals(new ImageWrapper(imageView).getCurrentUrl());
        }
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
        if (validateImageView(imageView)) {
            listenerCallback(imageView);
            stopExistingAnimation(imageView);
            imageView.setImageBitmap(bitmap);
            startImageViewAnimation(imageView);
        }
    }

    private void startImageViewAnimation(ImageView imageView) {
        Animation animation = ((ImageTag) imageView.getTag()).getAnimation();
        if (animation != null) {
            imageView.startAnimation(animation);
        }
    }

    private void stopExistingAnimation(ImageView imageView) {
        Animation old = imageView.getAnimation();
        if (old != null && !old.hasEnded()) {
            old.cancel();
        }
    }

    private boolean validateImageView(ImageView imageView) {
        if (imageView == null || hasImageViewUrlChanged(imageView) ||
                ((ImageTag) imageView.getTag()).getLoaderTask() != this) {
            return false;
        }
        return true;
    }

    private void listenerCallback(ImageView imageView) {
        if (onImageLoadedListener != null && onImageLoadedListener.get() != null) {
            onImageLoadedListener.get().onImageLoaded(imageView);
        }
    }

    private Bitmap getNotFoundImage(Context c) {
        String key = "resource" + notFoundResourceId + width + height;
        Bitmap b = loaderSettings.getResCacheManager().get(key, width, height);
        if (b != null) {
            return b;
        }
        if (loaderSettings.isAlwaysUseOriginalSize()) {
            b = loaderSettings.getBitmapUtil().decodeResourceBitmap(c, width, height, notFoundResourceId);
        } else {
            b = loaderSettings.getBitmapUtil().decodeResourceBitmapAndScale(c, width, height, notFoundResourceId, loaderSettings.isAllowUpsampling());
        }
        loaderSettings.getResCacheManager().put(key, b);
        return b;
    }

    public String getUrl() {
        return url;
    }

}
