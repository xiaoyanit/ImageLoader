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
import android.graphics.Bitmap;
import android.test.mock.MockContext;
import android.widget.ImageView;
import com.novoda.imageloader.core.bitmap.BitmapUtil;
import com.novoda.imageloader.core.cache.CacheManager;
import com.novoda.imageloader.core.file.FileManager;
import com.novoda.imageloader.core.file.util.FileUtil;
import com.novoda.imageloader.core.network.NetworkManager;
import com.novoda.imageloader.core.network.UrlNetworkManager;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.mockito.Mockito.*;

public class ImageManagerTest {

    private LoaderSettings loaderSettings;
    private Context context;
    private ImageManager imageManager;

    @Before
    public void beforeEveryTest() {
        loaderSettings = mock(LoaderSettings.class);
        when(loaderSettings.isCleanOnSetup()).thenReturn(false);
        context = mock(Context.class);
    }

    @Test(expected = RuntimeException.class)
    public void shouldComplaingIfInternetPermissionIsNotSet() {
        disableManifestPermission(Manifest.permission.INTERNET);

        new ImageManager(context, loaderSettings) {
            protected void setLoader(LoaderSettings settings) {
            };
        };
    }

    @Test(expected = RuntimeException.class)
    public void shouldComplainIfWriteExternalStoragePermissionIsNotSet() {
        disableManifestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        new ImageManager(context, loaderSettings) {
            protected void setLoader(LoaderSettings settings) {
            };
        };
    }

    private void disableManifestPermission(String permission) {
        PackageManager pm = mock(PackageManager.class);
        when(pm.checkPermission(permission, null)).thenReturn(
                PackageManager.PERMISSION_DENIED);
        when(context.getPackageManager()).thenReturn(pm);
    }

    @Test
    public void shouldRegisterOnImageLoadedListener() {
        setUpImageManager();
        OnImageLoadedListener listener = createOnImageLoadedListener();
        imageManager.setOnImageLoadedListener(listener);

        WeakReference listenerReference = new WeakReference<OnImageLoadedListener>(listener);

        System.gc();
        assertNotNull(listenerReference.get());
    }

    @Test
    public void shouldUnregisterOnImageLoadedListener() {
        setUpImageManager();
        OnImageLoadedListener listener = createOnImageLoadedListener();
        imageManager.setOnImageLoadedListener(listener);

        WeakReference listenerReference = new WeakReference<OnImageLoadedListener>(listener);
        listener = null;

        assertNotNull(listenerReference.get());
        System.gc();
        assertNull(listenerReference.get());

    }

    @Test
    public void testCacheImage(){
        setValidImageManagerPermissions();

        File file = mock(File.class);
        FileManager fm = mock(FileManager.class);
        when(fm.getFile("http://king.com/img.png", 100, 100)).thenReturn(file);

        final BitmapUtil bmUtil = mock(BitmapUtil.class);
        when(bmUtil.decodeFile(file, 100, 100)).thenReturn(null);
        LoaderContext loaderContext = new LoaderContext(){
            public BitmapUtil getBitmapUtil(){
                return bmUtil;
            }
        };

        NetworkManager nm = mock(NetworkManager.class);

        loaderContext.setNetworkManager(nm);
        loaderContext.setFileManager(fm);


        CacheManager cache = mock(CacheManager.class);
        loaderContext.setCache(cache);
        loaderContext.setSettings(new LoaderSettings());

        imageManager = new ImageManager(context, loaderContext);

        imageManager.cacheImage("http://king.com/img.png", 100, 100);
        // file decode failed, therefore nothing in cache
        verify(cache, never()).put("", null);

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

    private OnImageLoadedListener createOnImageLoadedListener() {
        OnImageLoadedListener listener = new OnImageLoadedListener() {
            @Override
            public void onImageLoaded(ImageView imageView) {
            }
        };
        return listener;
    }


}
