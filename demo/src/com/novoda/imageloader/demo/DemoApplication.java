package com.novoda.imageloader.demo;

import android.app.Application;

import com.novoda.imageloader.core.ImageManager;
import com.novoda.imageloader.core.LoaderSettings;
import com.novoda.imageloader.core.LoaderSettings.SettingsBuilder;
import com.novoda.imageloader.core.cache.LruBitmapCache;

public class DemoApplication extends Application {

    // TODO add this to your class
    private static ImageManager imageManager;
    private static ImageManager thumbnailImageLoader;

    @Override
    public void onCreate() {
        super.onCreate();
        // TODO add this to your classs
        LoaderSettings settings = new SettingsBuilder().withDisconnectOnEveryCall(true).build(this);
        imageManager = new ImageManager(this, settings);

        settings = new SettingsBuilder().withDisconnectOnEveryCall(true).withAsyncTasks(false)
                .withCacheManager(new LruBitmapCache(this)).build(this);
        thumbnailImageLoader = new ImageManager(this, settings);
        //
    }

    // TODO add this to your class
    public static ImageManager getImageLoader() {
        return imageManager;
    }

    // TODO add this to your class
    public static ImageManager getThumbnailImageLoader() {
        return thumbnailImageLoader;
    }

}
