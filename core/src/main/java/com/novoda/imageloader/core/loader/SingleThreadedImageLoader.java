package com.novoda.imageloader.core.loader;

import java.io.File;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import com.novoda.imageloader.core.ImageLoader;
import com.novoda.imageloader.core.cache.BitmapCache;
import com.novoda.imageloader.core.cache.SoftMapCache;
import com.novoda.imageloader.core.exception.ImageNotFoundException;
import com.novoda.imageloader.core.file.FileUtil;
import com.novoda.imageloader.core.model.ImageWrapper;
import com.novoda.imageloader.core.service.CacheCleaner;
import com.novoda.imageloader.core.util.BitmapDisplayer;
import com.novoda.imageloader.core.util.BitmapUtil;
import com.novoda.imageloader.core.util.Settings;
import com.novoda.imageloader.core.util.UrlUtil;

public class SingleThreadedImageLoader implements ImageLoader {

	private static final String TAG = "ImageLoader";

	private UrlUtil urlUtil = new UrlUtil();
	private BitmapUtil bitmapUtil = new BitmapUtil();
	private SingleThreadedLoader singleThreadedLoader;
	private Settings settings;
	private BitmapCache cache;
	private BitmapCache resBitmapCache = new SoftMapCache();

	public SingleThreadedImageLoader(Context c, Settings settings) {
		this.settings = settings;
		this.cache = createCache();
		this.singleThreadedLoader = new SingleThreadedLoader() {
			@Override
      protected Bitmap loadMissingBitmap(ImageWrapper iw) {
	      return getBitmap(iw.getUrl(), iw.getWidth(), iw.getHeight());
      }
			@Override
      protected void onBitmapLoaded(ImageWrapper iw, Bitmap bmp) {
	      new BitmapDisplayer(bmp, iw).runOnUiThread();
	      cache.put(iw.getUrl(), bmp);
      }
		};
		verifyPermission(c, Manifest.permission.WRITE_EXTERNAL_STORAGE);
		verifyPermission(c, Manifest.permission.INTERNET);
	}

	private void verifyPermission(Context c, String permission) {
		int p = c.getPackageManager().checkPermission(permission, c.getPackageName());
		if(p == PackageManager.PERMISSION_DENIED) {
			throw new RuntimeException("ImageLoader : please add the permission " 
						+ permission + " to the manifest");
		}
  }

	@Override
	public void load(ImageView imageView) {
		ImageWrapper w = new ImageWrapper(imageView);
		try {
			if (cache.hasBitmap(w.getUrl())) {
				Bitmap b = cache.get(w.getUrl());
				if (b != null) {
					imageView.setImageBitmap(b);
					return;
				}
			}
			setResource(w, w.getLoadingResourceId());
			singleThreadedLoader.push(w);
		} catch (ImageNotFoundException inf) {
			setResource(w, w.getNotFoundResourceId());
		} catch (Throwable t) {
			setResource(w, w.getNotFoundResourceId());
		}
	}

	private void setResource(ImageWrapper w, int resId) {
		String key = "resource" + resId + w.getHeight() + w.getHeight();
		if(resBitmapCache.hasBitmap(key)) {
			Bitmap b = resBitmapCache.get(key);
			if(b != null) {
				w.setBitmap(b);
				return;
			}
		}
		Bitmap b = bitmapUtil.scaleResourceBitmap(w, resId);
		resBitmapCache.put(key, b);
		w.setBitmap(b);
  }

	@Override
	public void loadFromCacheOnly(ImageView imageView) {
		ImageWrapper w = new ImageWrapper(imageView);
		try {
			if (cache.hasBitmap(w.getUrl())) {
				Bitmap b = cache.get(w.getUrl());
				if (b != null) {
					imageView.setImageBitmap(b);
					return;
				}
				return;
			}
			setResource(w, w.getLoadingResourceId());
		} catch (Throwable t) {
			Log.e(TAG, t.getMessage(), t);
		}
	}

	@Override
	public String getFilePath(String imageUrl) {
		File f = getFile(imageUrl);
		if (f.exists()) {
			return f.getAbsolutePath();
		}
		return null;
	}

	@Override
	public Bitmap getBitmap(String url, int width, int height) {
		if (url != null && url.length() >= 0) {
			File f = getFile(url);
			if (f.exists()) {
				Bitmap b = bitmapUtil.decodeFileAndScale(f, width, height);
				if (b != null) {
					return b;
				}
			}
			new FileUtil().retrieveImage(url, f);
			return bitmapUtil.decodeFileAndScale(f, width, height);
		}
		return null;
	}
	
	@Override
	public void deleteFileCache(Context context) {
		sendCacheCleanUpBroadcast(context, 0);
	}

	@Override
	public void reduceFileCache(Context context) {
		long expirationPeriod = settings.getExpirationPeriod();
		sendCacheCleanUpBroadcast(context, expirationPeriod);
	}

	@Override
	public void cleanCache() {
		cache.clean();
	}

	protected String processUrl(String url) {
		if (!settings.isQueryIncludedInHash()) {
			return url;
		}
		return urlUtil.removeQuery(url);
	}

	protected BitmapCache createCache() {
		return new SoftMapCache();
	}

	private void sendCacheCleanUpBroadcast(Context context, long expirationPeriod) {
		String path = settings.getCacheDir().getAbsolutePath();
		Intent i = CacheCleaner.getCleanCacheIntent(path, expirationPeriod);
		i.setPackage(context.getPackageName());
		context.startService(i);
	}
	
	private File getFile(String url) {
		url = processUrl(url);
		String filename = String.valueOf(url.hashCode());
		return new File(settings.getCacheDir(), filename);
	}

}
