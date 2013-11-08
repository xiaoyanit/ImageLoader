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

import com.novoda.imageloader.core.bitmap.BitmapUtil;
import com.novoda.imageloader.core.cache.CacheManager;
import com.novoda.imageloader.core.file.FileManager;
import com.novoda.imageloader.core.loader.ConcurrentLoader;
import com.novoda.imageloader.core.network.NetworkManager;

import java.io.File;
import java.lang.ref.WeakReference;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.mockito.Mockito.*;

public class ImageManagerTest {

    public static final String IMAGE_URL = "http://king.com/img.png";
    public static final int WIDTH = 100;
    public static final int HEIGHT = 100;
    private LoaderSettings loaderSettings;
    private Context context;
    private ImageManager imageManager;

    @Before
    public void beforeEveryTest() {
        loaderSettings = mock(LoaderSettings.class);
        when(loaderSettings.isCleanOnSetup()).thenReturn(false);
        when(loaderSettings.getLoader()).thenReturn(new ConcurrentLoader(loaderSettings));
        context = mock(Context.class);
    }

    @Test(expected = RuntimeException.class)
    public void shouldComplainIfInternetPermissionIsNotSet() {
        disableManifestPermission(Manifest.permission.INTERNET);

        new ImageManager(context, loaderSettings);
    }

    @Test(expected = RuntimeException.class)
    public void shouldComplainIfWriteExternalStoragePermissionIsNotSet() {
        disableManifestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        new ImageManager(context, loaderSettings);
    }

    private void disableManifestPermission(String permission) {
        PackageManager pm = mock(PackageManager.class);
        when(pm.checkPermission(permission, null)).thenReturn(
                PackageManager.PERMISSION_DENIED);
        when(context.getPackageManager()).thenReturn(pm);
    }

    @Test
    public void shouldRegisterOnImageLoadedListener() {
        OnImageLoadedListener listener = mock(OnImageLoadedListener.class);
        imageManager = new ImageManager(loaderSettings);
        imageManager.setOnImageLoadedListener(listener);

        WeakReference listenerReference = new WeakReference<OnImageLoadedListener>(listener);

        System.gc();
        assertNotNull(listenerReference.get());
    }

    @Test
    public void shouldUnregisterOnImageLoadedListener() {
        setUpImageManager();
        OnImageLoadedListener listener = mock(OnImageLoadedListener.class);
        imageManager.setOnImageLoadedListener(listener);

        WeakReference listenerReference = new WeakReference<OnImageLoadedListener>(listener);
        listener = null;

        assertNotNull(listenerReference.get());
        System.gc();
        assertNull(listenerReference.get());

    }

    @Test
    public void testWhenCacheImageIsCalledTheCacheManagerPutsTheImageInCache() {
        CacheManager cache = mock(CacheManager.class);
        givenAnImageManagerWithAllSubManagers(cache);

        imageManager.cacheImage(IMAGE_URL, WIDTH, HEIGHT);

        verify(cache, atLeastOnce()).put(IMAGE_URL, null);
    }

    private void givenAnImageManagerWithAllSubManagers(CacheManager cache) {
        updateLoaderSettingsWithSubmanagers(cache);
        setUpImageManager();
    }

    private void updateLoaderSettingsWithSubmanagers(CacheManager cache) {
        FileManager fileManager = mock(FileManager.class);
        File file = mock(File.class);
        when(fileManager.getFile(IMAGE_URL, WIDTH, WIDTH)).thenReturn(file);

        final BitmapUtil bitmapUtil = mock(BitmapUtil.class);
        when(bitmapUtil.decodeFile(file, WIDTH, HEIGHT)).thenReturn(null);
        loaderSettings = new LoaderSettings() {
            public BitmapUtil getBitmapUtil() {
                return bitmapUtil;
            }
        };
        NetworkManager nm = mock(NetworkManager.class);

        loaderSettings.setNetworkManager(nm);
        loaderSettings.setFileManager(fileManager);
        loaderSettings.setCacheManager(cache);
    }

    private void setUpImageManager() {
        setValidImageManagerPermissions();
        imageManager = new ImageManager(context, loaderSettings);
    }

    private void setValidImageManagerPermissions() {
        PackageManager pm = mock(PackageManager.class);
        when(pm.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, null)).thenReturn(PackageManager.PERMISSION_GRANTED);
        when(pm.checkPermission(Manifest.permission.INTERNET, null)).thenReturn(PackageManager.PERMISSION_GRANTED);
        when(context.getPackageManager()).thenReturn(pm);
    }
}
