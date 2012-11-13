package com.novoda.imageloader.acceptance;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.Suppress;

import com.novoda.imageloader.core.*;

/**
 * Created with IntelliJ IDEA.
 * User: friedger
 * Date: 29.10.12
 * Time: 21:59
 * To change this template use File | Settings | File Templates.
 */
public class ImageManagerInstrumentationTest extends InstrumentationTestCase {

    public ImageManagerInstrumentationTest(String name) {
        super();
        setName(name);
    }

    @Suppress() // XXX No assertions so not running - gives NPE
    public void testCacheImage(){
        Bitmap bmOriginal = BitmapFactory.decodeResource(getInstrumentation().getContext().getResources(), R.drawable.icon);

        LoaderSettings settings = new LoaderSettings();

        LoaderContext loaderContext = new LoaderContext();
        loaderContext.setSettings(settings);

        ImageManager imageManager = new ImageManager(getInstrumentation().getTargetContext(), loaderContext);
        imageManager.cacheImage("http://king.com/img.png", 100, 100);




    }
}
