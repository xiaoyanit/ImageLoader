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

public class BitmapRetrieverTest {

    @Test
    public void testCacheImage() {

        File file = mock(File.class);
        when(file.exists()).thenReturn(true);
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

        ImageTag imageTag = new ImageTag("http://king.com/img.png", 0, 0, 100, 100);
        ImageView imageView = createImageView(imageTag);
        ImageWrapper imageWrapper = new ImageWrapper(imageView);

        BitmapRetriever retriever = new BitmapRetriever("http://king.com/img.png", file, 100, 100, 0, false, true, imageView, loaderSettings, mock(Context.class));
        retriever.getBitmap();

        // file decode failed, therefore nothing in cache
        verify(cache, atLeastOnce()).put("http://king.com/img.png", null);
        verify(file, atLeastOnce()).delete();

    }

    private ImageView createImageView(ImageTag tag){
        ImageView imageView = mock(ImageView.class);
        when(imageView.getTag()).thenReturn(tag);
        return imageView;
    }
}
