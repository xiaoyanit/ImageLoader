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
import com.novoda.imageloader.core.network.UrlNetworkManager;

/**
 * ImageManager has the responsibility to provide a
 * simple and easy interface to access three fundamental part of the imageLoader
 * library : the FileManager, the NetworkManager, and the CacheManager.
 * An ImageManager instance can be instantiated at the application level and used 
 * statically across the application.
 * 
 * Manifest.permission.WRITE_EXTERNAL_STORAGE and Manifest.permission.INTERNET are 
 * currently necessary for the imageLoader library to work properly. 
 */
public class ImageManager {

    private LoaderContext loaderContext;
    private Loader loader;
    private CacheManager cacheManager;

    public ImageManager(Context context, LoaderSettings settings) {
        this.loaderContext = new LoaderContext();
        loaderContext.setSettings(settings);
        loaderContext.setFileManager(new BasicFileManager(settings));
        loaderContext.setNetworkManager(new UrlNetworkManager(settings));
        loaderContext.setResBitmapCache(new SoftMapCache());
        cacheManager = settings.getCacheManager();
        if (cacheManager == null) {
            cacheManager = new SoftMapCache();
        }
        loaderContext.setCache(cacheManager);
        setLoader(settings);
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
    
    public CacheManager getCacheManager() {
        return cacheManager;
    }

    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }
    
    protected void setLoader(LoaderSettings settings) {
        if (settings.isUseAsyncTasks()) {
            this.loader = new ConcurrentLoader(loaderContext);
        } else {
            this.loader = new SimpleLoader(loaderContext);
        }
    }

    private void verifyPermissions(Context context) {
        verifyPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        verifyPermission(context, Manifest.permission.INTERNET);
    }

    private void verifyPermission(Context c, String permission) {
        int p = c.getPackageManager().checkPermission(permission, c.getPackageName());
        if (p == PackageManager.PERMISSION_DENIED) {
            throw new RuntimeException("ImageLoader : please add the permission " + permission + " to the manifest");
        }
    }

}
