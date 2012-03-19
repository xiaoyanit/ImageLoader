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

import com.novoda.imageloader.core.bitmap.BitmapUtil;
import com.novoda.imageloader.core.cache.CacheManager;
import com.novoda.imageloader.core.file.FileManager;
import com.novoda.imageloader.core.network.NetworkManager;

public class LoaderContext {
    private FileManager fileManager;
    private NetworkManager networkManager;
    private CacheManager cache;
    private CacheManager resBitmapCache;
    private LoaderSettings settings;
    private BitmapUtil bitmapUtil = new BitmapUtil();

    public FileManager getFileManager() {
        return fileManager;
    }

    public void setFileManager(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    public NetworkManager getNetworkManager() {
        return networkManager;
    }

    public void setNetworkManager(NetworkManager networkManager) {
        this.networkManager = networkManager;
    }

    public LoaderSettings getSettings() {
        return settings;
    }

    public void setSettings(LoaderSettings settings) {
        this.settings = settings;
    }

    public CacheManager getResBitmapCache() {
        return resBitmapCache;
    }

    public void setResBitmapCache(CacheManager resBitmapCache) {
        this.resBitmapCache = resBitmapCache;
    }

    public CacheManager getCache() {
        return cache;
    }

    public void setCache(CacheManager cache) {
        this.cache = cache;
    }

    public BitmapUtil getBitmapUtil() {
        return bitmapUtil;
    }
}
