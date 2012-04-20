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
package com.novoda.imageloader.core.file;

import java.io.File;
import java.io.FileOutputStream;

import com.novoda.imageloader.core.LoaderSettings;
import com.novoda.imageloader.core.network.UrlUtil;
import com.novoda.imageloader.core.service.CacheCleaner;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

public class BasicFileManager implements FileManager {

    private LoaderSettings settings;

    public BasicFileManager(LoaderSettings settings) {
        this.settings = settings;
    }

    @Override
    public void delete(Context context) {
        sendCacheCleanUpBroadcast(context, 0);
    }

    @Override
    public void clean(Context context) {
        long expirationPeriod = settings.getExpirationPeriod();
        sendCacheCleanUpBroadcast(context, expirationPeriod);
    }

    @Override
    public String getFilePath(String imageUrl) {
        File f = getFile(imageUrl);
        if (f.exists()) {
            return f.getAbsolutePath();
        }
        return null;
    }

    @Override
    public void saveBitmap(String fileName, Bitmap b, int width, int height) {
        try {
            FileOutputStream out = new FileOutputStream(fileName + "-" + width + "x" + height);
            b.compress(Bitmap.CompressFormat.PNG, 90, out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendCacheCleanUpBroadcast(Context context, long expirationPeriod) {
        String path = settings.getCacheDir().getAbsolutePath();
        Intent i = CacheCleaner.getCleanCacheIntent(path, expirationPeriod);
        i.setPackage(context.getPackageName());
        context.startService(i);
    }

    @Override
    public File getFile(String url) {
        url = processUrl(url);
        String filename = String.valueOf(url.hashCode());
        return new File(settings.getCacheDir(), filename);
    }

    @Override
    public File getFile(String url, int width, int height) {
        url = processUrl(url);
        String filename = String.valueOf(url.hashCode()) + "-" + width + "x" + height;
        return new File(settings.getCacheDir(), filename);
    }

    private String processUrl(String url) {
        if (!settings.isQueryIncludedInHash()) {
            return url;
        }
        return new UrlUtil().removeQuery(url);
    }

}
