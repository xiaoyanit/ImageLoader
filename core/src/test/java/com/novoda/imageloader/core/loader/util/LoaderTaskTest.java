package com.novoda.imageloader.core.loader.util;

import android.content.Context;
import android.widget.ImageView;

import com.novoda.imageloader.core.LoaderSettings;
import com.novoda.imageloader.core.bitmap.BitmapUtil;
import com.novoda.imageloader.core.cache.CacheManager;
import com.novoda.imageloader.core.file.FileManager;
import com.novoda.imageloader.core.model.ImageTag;
import com.novoda.imageloader.core.model.ImageWrapper;
import com.novoda.imageloader.core.network.NetworkManager;

import java.io.File;

import org.junit.Test;

import static org.mockito.Mockito.*;

public class LoaderTaskTest {

    @Test
    public void testCacheImage() {

        File file = mock(File.class);
        FileManager fm = mock(FileManager.class);
        when(fm.getFile("http://king.com/img.png", 100, 100)).thenReturn(file);

        final BitmapUtil bmUtil = mock(BitmapUtil.class);
        when(bmUtil.decodeFile(file, 100, 100)).thenReturn(null);
        LoaderSettings loaderSettings = new LoaderSettings() {
            public BitmapUtil getBitmapUtil() {
                return bmUtil;
            }
        };

        NetworkManager nm = mock(NetworkManager.class);

        loaderSettings.setNetworkManager(nm);
        loaderSettings.setFileManager(fm);

        CacheManager cache = mock(CacheManager.class);
        loaderSettings.setCacheManager(cache);

        ImageView imageView = new ImageView(mock(Context.class));
        ImageTag imageTag = new ImageTag("http://king.com/img.png", 0, 0, 100, 100);
        imageView.setTag(imageTag);
        ImageWrapper imageWrapper = new ImageWrapper(imageView);

        LoaderTask task = new LoaderTask(imageWrapper, loaderSettings);
        task.doInBackground();

        // file decode failed, therefore nothing in cache
        verify(cache, atLeastOnce()).put("http://king.com/img.png", null);

    }
}
