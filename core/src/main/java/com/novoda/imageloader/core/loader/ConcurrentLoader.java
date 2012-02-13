package com.novoda.imageloader.core.loader;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.novoda.imageloader.core.LoaderContext;
import com.novoda.imageloader.core.exception.ImageNotFoundException;
import com.novoda.imageloader.core.loader.util.LoaderTask;
import com.novoda.imageloader.core.model.ImageWrapper;

public class ConcurrentLoader implements Loader {
	
	private LoaderContext loaderContext;
	
	public ConcurrentLoader(LoaderContext loaderContext) {
		this.loaderContext = loaderContext;
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
  		new LoaderTask(imageView, loaderContext).execute();
		} catch (ImageNotFoundException inf) {
			setResource(w, w.getNotFoundResourceId());
		} catch (Throwable t) {
			setResource(w, w.getNotFoundResourceId());
		}
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
