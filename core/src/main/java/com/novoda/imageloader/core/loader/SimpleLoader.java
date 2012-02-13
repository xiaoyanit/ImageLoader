package com.novoda.imageloader.core.loader;

import java.io.File;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.novoda.imageloader.core.LoaderContext;
import com.novoda.imageloader.core.exception.ImageNotFoundException;
import com.novoda.imageloader.core.loader.util.BitmapDisplayer;
import com.novoda.imageloader.core.loader.util.SingleThreadedLoader;
import com.novoda.imageloader.core.model.ImageWrapper;

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
			if (loaderContext.getCache().hasBitmap(w.getUrl())) {
				Bitmap b = loaderContext.getCache().get(w.getUrl());
				if (b != null) {
					imageView.setImageBitmap(b);
					return;
				}
			}
			setResource(w, w.getLoadingResourceId());
			if(w.isUseCacheOnly()) {
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
				Bitmap b = loaderContext.getBitmapUtil().decodeFileAndScale(f, width, height);
				if (b != null) {
					return b;
				}
			}
			loaderContext.getNetworkManager().retrieveImage(url, f);
			return loaderContext.getBitmapUtil().decodeFileAndScale(f, width, height);
		}
		return null;
	}
	
	private void setResource(ImageWrapper w, int resId) {
		String key = "resource" + resId + w.getHeight() + w.getHeight();
		if(loaderContext.getResBitmapCache().hasBitmap(key)) {
			Bitmap b = loaderContext.getResBitmapCache().get(key);
			if(b != null) {
				w.setBitmap(b);
				return;
			}
		}
		Bitmap b = loaderContext.getBitmapUtil().scaleResourceBitmap(w, resId);
		loaderContext.getResBitmapCache().put(key, b);
		w.setBitmap(b);
  }

}
