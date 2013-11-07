package com.novoda.imageloader.acceptance;

import android.test.InstrumentationTestCase;

import com.novoda.imageloader.core.ImageManager;
import com.novoda.imageloader.core.LoaderSettings;

/**
 * Created with IntelliJ IDEA. User: friedger Date: 29.10.12 Time: 21:59 To change this template use File | Settings | File Templates.
 */
public class ImageManagerInstrumentationTest extends InstrumentationTestCase {

    public ImageManagerInstrumentationTest(String name) {
        super();
        setName(name);
    }

    public void testCacheImage() {
        LoaderSettings settings = new LoaderSettings.SettingsBuilder().build(getInstrumentation().getContext());
        ImageManager imageManager = new ImageManager(getInstrumentation().getTargetContext(), settings);

        String url = "http://imgur.com/5FirD.png";
        imageManager.cacheImage(url, 64, 64);

        assertTrue(imageManager.getCacheManager().get(url, 64, 64) != null);
    }
}
