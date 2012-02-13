package com.novoda.imageloader.core;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

import com.novoda.imageloader.core.cache.CacheManager;
import com.novoda.imageloader.core.cache.SoftMapCache;
import com.novoda.imageloader.core.file.BasicFileManager;
import com.novoda.imageloader.core.file.FileManager;
import com.novoda.imageloader.core.loader.ConcurrentLoader;
import com.novoda.imageloader.core.loader.Loader;
import com.novoda.imageloader.core.loader.SimpleLoader;
import com.novoda.imageloader.core.network.NetworkManager;
import com.novoda.imageloader.core.network.UrlNetworkLoader;

public class ImageManager {
	
	private LoaderContext loaderContext;
	private Loader loader;
	
	public ImageManager(Context context, LoaderSettings settings) {
		this.loaderContext = new LoaderContext();
		loaderContext.setSettings(settings);
		loaderContext.setFileManager(new BasicFileManager(settings));
		loaderContext.setNetworkManager(new UrlNetworkLoader(settings));
		loaderContext.setResBitmapCache(new SoftMapCache());
		CacheManager cacheManager = settings.getCacheManager();
		if(cacheManager == null) {
			cacheManager = new SoftMapCache();
		}
		loaderContext.setCache(cacheManager);
		if(settings.isUseAsyncTasks()) {
			this.loader = new ConcurrentLoader(loaderContext);
		} else {
			this.loader = new SimpleLoader(loaderContext);
		}
		verifyPermissions(context);
	}
	
	public Loader getLoader() {
	  return loader;
  }

	public FileManager getFileManager() {
	  return loaderContext.getFileManager();
  }

	public NetworkManager getNetworkManager() {
	  return loaderContext.getNetworkManager();
  }

	private void verifyPermissions(Context context) {
		verifyPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
		verifyPermission(context, Manifest.permission.INTERNET);
	}
	
	private void verifyPermission(Context c, String permission) {
		int p = c.getPackageManager().checkPermission(permission, c.getPackageName());
		if(p == PackageManager.PERMISSION_DENIED) {
			throw new RuntimeException("ImageLoader : please add the permission " 
					+ permission + " to the manifest");
		}
	}
	
}
