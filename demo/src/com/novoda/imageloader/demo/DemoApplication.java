package com.novoda.imageloader.demo;

import android.app.Application;

import com.novoda.imageloader.core.ImageManager;
import com.novoda.imageloader.core.LoaderSettings;
import com.novoda.imageloader.core.LoaderSettings.SettingsBuilder;
import com.novoda.imageloader.core.cache.LruBitmapCache;

public class DemoApplication extends Application {

    /*
     * TODO Initialise the image manager in your application.
     * You can have more than one if you need different settings.
     */
    private static ImageManager imageManager;
    private static ImageManager thumbnailImageLoader;

    @Override
    public void onCreate() {
        super.onCreate();
        // TODO Initialise your image manager settings.
        LoaderSettings settings = new SettingsBuilder().withDisconnectOnEveryCall(true).build(this);
        imageManager = new ImageManager(this, settings);

        settings = new SettingsBuilder().withDisconnectOnEveryCall(true).withAsyncTasks(false)
                .withCacheManager(new LruBitmapCache(this)).build(this);
        thumbnailImageLoader = new ImageManager(this, settings);
    }

    // TODO Create a method to access your image manager.
    public static ImageManager getImageLoader() {
        return imageManager;
    }

    public static ImageManager getThumbnailImageLoader() {
        return thumbnailImageLoader;
    }

}
