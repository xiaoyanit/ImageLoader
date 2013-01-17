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
package com.novoda.imageloader.core.loader;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import com.novoda.imageloader.core.LoaderSettings;
import com.novoda.imageloader.core.OnImageLoadedListener;
import com.novoda.imageloader.core.exception.ImageNotFoundException;
import com.novoda.imageloader.core.loader.util.LoaderTask;
import com.novoda.imageloader.core.model.ImageWrapper;

import java.lang.ref.WeakReference;

public class ConcurrentLoader implements Loader {

	private final LoaderSettings loaderSettings;

    private WeakReference<OnImageLoadedListener> onImageLoadedListener;

    public ConcurrentLoader(LoaderSettings loaderSettings) {
        this.loaderSettings = loaderSettings;
    }

	@Override
	public void load(ImageView imageView) {
		ImageWrapper w = new ImageWrapper(imageView);

		if (w.getUrl() == null) {
			Log.w("ImageLoader", "You should never call load if you don't set a ImageTag on the view");
			return;
		}

		if (!isTaskAlreadyRunning(w)) {
			// only continue if a concurrent task has not yet been started

			try {
                if (isBitmapAlreadyInCache(w)) {
                    return;
                }

				// get preview or loading image
				String thumbUrl = w.getPreviewUrl();
				if (thumbUrl != null) {
					Bitmap b = loaderSettings.getCacheManager().get(thumbUrl, w.getPreviewHeight(), w.getPreviewWidth());
					if (b != null && !b.isRecycled()) {
						w.setBitmap(b);
					} else {
						setResource(w, w.getLoadingResourceId());
					}
				} else {
					setResource(w, w.getLoadingResourceId());
				}

				if (w.isUseCacheOnly()) {
					return;
				}

				// spin off a new task for this url
                LoaderTask task = createTask(imageView);
				w.setLoaderTask(task);
				task.execute();

			} catch (ImageNotFoundException inf) {
				setResource(w, w.getNotFoundResourceId());
			} catch (Throwable t) {
				setResource(w, w.getNotFoundResourceId());
			}
		}
	}

    private boolean isBitmapAlreadyInCache(ImageWrapper w) {
        Bitmap b = loaderSettings.getCacheManager().get(w.getUrl(), w.getHeight(), w.getWidth());
        if (b != null && !b.isRecycled()) {
            w.setBitmap(b);
            return true;
        }
        return false;
    }

    private LoaderTask createTask(ImageView imageView) {
        return onImageLoadedListener == null ? new LoaderTask(imageView, loaderSettings) :
                new LoaderTask(imageView, loaderSettings, onImageLoadedListener);
    }

    @Override
    public void setLoadListener(WeakReference<OnImageLoadedListener> onImageLoadedListener) {
        this.onImageLoadedListener = onImageLoadedListener;
    }

    /**
	 * checks whether a previous task is loading the same url
	 *
	 * @param imageWrapper
	 *            url of the image to be fetched
	 *            task that might already fetching an image, might be null
	 * @return false if there is no other concurrent task running
	 */

    private static boolean isTaskAlreadyRunning(ImageWrapper imageWrapper) {
        LoaderTask oldTask = imageWrapper.getLoaderTask();
        if (oldTask == null) {
            return false;
        }

        String url = imageWrapper.getUrl();
        if ((!url.equals(oldTask.getUrl()))) {
            return true;
        }
        // task != null && url == task.getUrl
        // there is already a concurrent task fetching the image
        oldTask.cancel(true);

        return false;
    }

	private void setResource(ImageWrapper w, int resId) {
		Bitmap b = loaderSettings.getResCacheManager().get("" + resId, w.getWidth(), w.getHeight());
		if (b != null) {
			w.setBitmap(b);
			return;
		}
		b = loaderSettings.getBitmapUtil().decodeResourceBitmapAndScale(w, resId, loaderSettings.isAllowUpsampling());
		loaderSettings.getResCacheManager().put(String.valueOf(resId), b);
		w.setBitmap(b);
	}

}
