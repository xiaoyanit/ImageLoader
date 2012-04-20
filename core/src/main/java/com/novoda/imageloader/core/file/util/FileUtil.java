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
package com.novoda.imageloader.core.file.util;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.util.Log;

import com.novoda.imageloader.core.exception.ImageCopyException;

public class FileUtil {

    private static final String ANDROID_ROOT = "/Android/data/";
    private static final String NOMEDIA_FILE_NAME = ".nomedia";
    private static final String DEFAULT_IMAGE_FOLDER_NAME = "/cache/images";
    private static final String TAG = "ImageLoader";

    private static final int BUFFER_SIZE = 60 * 1024;
    private static final byte[] BUFFER = new byte[BUFFER_SIZE];

    public void closeSilently(Closeable c) {
        try {
            if (c != null) {
                c.close();
            }
        } catch (Exception e) {
            Log.e(TAG, "Problem closing stream " + e.getMessage());
        }
    }

    public void copyStream(InputStream is, OutputStream os) {
        try {
            while (true) {
                synchronized (BUFFER) {
                    int amountRead = is.read(BUFFER);
                    if (amountRead == -1) {
                        break;
                    }
                    os.write(BUFFER, 0, amountRead);
                }
            }
        } catch (Exception ex) {
            Log.e(TAG, "Exception : ", ex);
        }
    }

    public boolean deleteFileCache(String cacheDirFullPath) {
        return reduceFileCache(cacheDirFullPath, -1);
    }

    public boolean reduceFileCache(String cacheDirFullPath, long expirationPeriod) {
        File cacheDir = new File(cacheDirFullPath);
        if (cacheDir.isDirectory()) {
            File[] children = cacheDir.listFiles();
            long lastModifiedThreashold = System.currentTimeMillis() - expirationPeriod;
            for (File f : children) {
                if (f.lastModified() < lastModifiedThreashold) {
                    f.delete();
                }
            }
        }
        return true;
    }

    public void copy(File src, File dst) throws ImageCopyException {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new FileInputStream(src);
            out = new FileOutputStream(dst);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
        } catch (IOException e) {
            throw new ImageCopyException(e);
        } finally {
            closeSilently(out);
            closeSilently(in);
        }
    }

    public File prepareCacheDirectory(Context context) {
        File dir = null;
        if (!isMounted()) {
            dir = preparePhoneCacheDir(context);
        } else {
            dir = prepareExternalCacheDir(context);
        }
        addNomediaFile(dir);
        return dir;
    }

    public boolean isMounted() {
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }

    private File prepareExternalCacheDir(Context context) {
        String relativepath = ANDROID_ROOT + context.getPackageName() + DEFAULT_IMAGE_FOLDER_NAME;
        File file = new File(android.os.Environment.getExternalStorageDirectory(), relativepath);
        if (!file.isDirectory()) {
            file.mkdirs();
        }
        return file;
    }

    private File preparePhoneCacheDir(Context context) {
        return context.getCacheDir();
    }

    private void addNomediaFile(File dir) {
        try {
            new File(dir, NOMEDIA_FILE_NAME).createNewFile();
        } catch (Exception e) {
            // Don't care if doesn't save the file
        }
    }

}