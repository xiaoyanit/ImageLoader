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

import com.novoda.imageloader.core.LoaderContext;
import com.novoda.imageloader.core.exception.ImageNotFoundException;
import com.novoda.imageloader.core.loader.util.LoaderTask;
import com.novoda.imageloader.core.model.ImageWrapper;

public class ConcurrentLoader implements Loader {

    private final LoaderContext loaderContext;

    public ConcurrentLoader(LoaderContext loaderContext) {
        this.loaderContext = loaderContext;
    }

    @Override
    public void load(ImageView imageView) {
        ImageWrapper w = new ImageWrapper(imageView);

        if (w.getUrl() == null) {
            Log.w("ImageLoader", "You should never call load if you don't set a ImageTag on the view");
            return;
        }


        if (checkConcurrentTasks(w.getUrl(), w.getLoaderTask())) {
            // only continue if a concurrent task has not yet been started

            try {

                // get bitmap from cache
                Bitmap b = loaderContext.getCache().get(w.getUrl(), w.getHeight(), w.getWidth());
                if (b != null && !b.isRecycled()) {
                    w.setBitmap(b);
                    return;
                }

                // get preview or loading image
                String thumbUrl = w.getPreviewUrl();
                if (thumbUrl != null) {
                    b = loaderContext.getCache().get(thumbUrl, w.getPreviewHeight(), w.getPreviewWidth());
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
                LoaderTask task = new LoaderTask(imageView, loaderContext);
                w.setLoaderTask(task);
                task.execute();

            } catch (ImageNotFoundException inf) {
                setResource(w, w.getNotFoundResourceId());
            } catch (Throwable t) {
                setResource(w, w.getNotFoundResourceId());
            }
        }
    }

    /**
     * checks whether a previous task is loading the same url
     *
     * @param url url of the image to be fetched
     * @param oldTask task that might already fetching an image, might be null
     * @return true if there is no other concurrent task running
     */

    private static boolean checkConcurrentTasks(String url, LoaderTask oldTask) {
        if (oldTask == null || (!url.equals(oldTask.getUrl()))){
            return false;
        } else {
            // task != null && url == task.getUrl
            // there is already a concurrent task fetching the image
        	oldTask.cancel(true);
        }

        return true;
    }

    private void setResource(ImageWrapper w, int resId) {
        Bitmap b = loaderContext.getResBitmapCache().get("" + resId, w.getWidth(), w.getHeight());
        if (b != null) {
            w.setBitmap(b);
            return;
        }
        b = loaderContext.getBitmapUtil().decodeResourceBitmapAndScale(w, resId, loaderContext.getSettings().isAllowUpsampling());
        loaderContext.getResBitmapCache().put(String.valueOf(resId), b);
        w.setBitmap(b);
    }

}
