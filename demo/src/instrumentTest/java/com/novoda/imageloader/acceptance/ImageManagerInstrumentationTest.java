package com.novoda.imageloader.acceptance;

import android.test.InstrumentationTestCase;

import com.novoda.imageloader.core.ImageManager;
import com.novoda.imageloader.core.LoaderSettings;
import com.novoda.imageloader.core.cache.CacheManager;
import com.novoda.imageloader.core.cache.LruBitmapCache;

/**
 * Created with IntelliJ IDEA. User: friedger Date: 29.10.12 Time: 21:59 To change this template use File | Settings | File Templates.
 */
public class ImageManagerInstrumentationTest extends InstrumentationTestCase {

    public ImageManagerInstrumentationTest(String name) {
        super();
        setName(name);
    }

    public void testCacheImage() {
        CacheManager cache = new LruBitmapCache(getInstrumentation().getContext());
        LoaderSettings settings = new LoaderSettings.SettingsBuilder().withCacheManager(cache).build(getInstrumentation().getContext());
        ImageManager imageManager = new ImageManager(getInstrumentation().getTargetContext(), settings);

        String url = "http://imgur.com/5FirD.png";
        int width = 651;
        int height = 481;
        imageManager.cacheImage(url, width, height);

        assertTrue(imageManager.getCacheManager().get(url, width, height) != null);
    }
}
