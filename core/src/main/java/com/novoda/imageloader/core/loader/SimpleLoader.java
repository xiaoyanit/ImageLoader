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
import android.widget.ImageView;

import com.novoda.imageloader.core.LoaderContext;
import com.novoda.imageloader.core.exception.ImageNotFoundException;
import com.novoda.imageloader.core.loader.util.BitmapDisplayer;
import com.novoda.imageloader.core.loader.util.SingleThreadedLoader;
import com.novoda.imageloader.core.model.ImageWrapper;

import java.io.File;

public class SimpleLoader implements Loader {

	private LoaderContext loaderContext;
	private SingleThreadedLoader singleThreadedLoader;

	public SimpleLoader(LoaderContext loaderContext) {
		this.loaderContext = loaderContext;
		this.singleThreadedLoader = new SingleThreadedLoader() {
			@Override
			protected Bitmap loadMissingBitmap(ImageWrapper iw) {
				return getBitmap(iw.getUrl(), iw.getWidth(), iw.getHeight());
			}

			@Override
			protected void onBitmapLoaded(ImageWrapper iw, Bitmap bmp) {
				new BitmapDisplayer(bmp, iw).runOnUiThread();
				SimpleLoader.this.loaderContext.getCache().put(iw.getUrl(), bmp);
			}
		};
	}

	@Override
	public void load(ImageView imageView) {
		ImageWrapper w = new ImageWrapper(imageView);

		try {
			Bitmap b = loaderContext.getCache().get(w.getUrl(), w.getWidth(), w.getHeight());
			if (b != null && !b.isRecycled()) {
				w.setBitmap(b);
				return;
			}
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
			singleThreadedLoader.push(w);
		} catch (ImageNotFoundException inf) {
			setResource(w, w.getNotFoundResourceId());
		} catch (Throwable t) {
			setResource(w, w.getNotFoundResourceId());
		}
	}

	private Bitmap getBitmap(String url, int width, int height) {
		if (url != null && url.length() >= 0) {
			File f = loaderContext.getFileManager().getFile(url);
			if (f.exists()) {
				Bitmap b = loaderContext.getBitmapUtil().decodeFileAndScale(f, width, height, loaderContext.getSettings().isAllowUpsampling());
				if (b != null && !b.isRecycled()) {
					return b;
				}
			}
			loaderContext.getNetworkManager().retrieveImage(url, f);
			return loaderContext.getBitmapUtil().decodeFileAndScale(f, width, height, loaderContext.getSettings().isAllowUpsampling());
		}
		return null;
	}

	private void setResource(ImageWrapper w, int resId) {
		Bitmap b = loaderContext.getResBitmapCache().get("" + resId, w.getWidth(), w.getHeight());
		if (b != null) {
			w.setBitmap(b);
			return;
		}
		b = loaderContext.getBitmapUtil().decodeResourceBitmapAndScale(w, resId, loaderContext.getSettings().isAllowUpsampling());
		loaderContext.getResBitmapCache().put("" + resId, b);
		w.setBitmap(b);
	}

}
