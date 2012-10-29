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
package com.novoda.imageloader.core.network;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.novoda.imageloader.core.LoaderSettings;
import com.novoda.imageloader.core.exception.ImageNotFoundException;
import com.novoda.imageloader.core.file.util.FileUtil;

/**
 * Basic implementation of the NetworkManager using URL connection.
 */
public class UrlNetworkManager implements NetworkManager {

    private static final int TEMP_REDIRECT = 307;
    
    private FileUtil fileUtil;
    private LoaderSettings settings;
    private int manualRedirects;

    public UrlNetworkManager(LoaderSettings settings) {
        this(settings, new FileUtil());
    }

    public UrlNetworkManager(LoaderSettings settings, FileUtil fileUtil) {
        this.settings = settings;
        this.fileUtil = fileUtil;
    }

    @Override
    public void retrieveImage(String url, File f) {
        InputStream is = null;
        OutputStream os = null;
        HttpURLConnection conn = null;
        applyChangeonSdkVersion(settings.getSdkVersion());
        try {
            conn = openConnection(url);
            conn.setConnectTimeout(settings.getConnectionTimeout());
            conn.setReadTimeout(settings.getReadTimeout());
            if (conn.getResponseCode() == TEMP_REDIRECT) {
                redirectManually(f, conn);
            } else {
                is = conn.getInputStream();
                os = new FileOutputStream(f);
                fileUtil.copyStream(is, os);
            }
        } catch (FileNotFoundException fnfe) {
            throw new ImageNotFoundException();
        } catch (Throwable ex) {
            // TODO
        } finally {
            if (conn != null && settings.getDisconnectOnEveryCall()) {
                conn.disconnect();
            }
            fileUtil.closeSilently(is);
            fileUtil.closeSilently(os);
        }
    }

    public void redirectManually(File f, HttpURLConnection conn) {
        if (manualRedirects++ < 3) {
            retrieveImage(conn.getHeaderField("Location"), f);
        } else {
            manualRedirects = 0;
        }
    }

    @Override
    public InputStream retrieveInputStream(String url) {
        HttpURLConnection conn = null;
        try {
            conn = openConnection(url);
            conn.setConnectTimeout(settings.getConnectionTimeout());
            conn.setReadTimeout(settings.getReadTimeout());
            return conn.getInputStream();
        } catch (FileNotFoundException fnfe) {
            throw new ImageNotFoundException();
        } catch (Throwable ex) {
            return null;
        }
    }

    protected HttpURLConnection openConnection(String url) throws IOException {
        return (HttpURLConnection) new URL(url).openConnection();
    }

    private void applyChangeonSdkVersion(String sdkVersion) {
        if (Integer.parseInt(sdkVersion) < 8) {
            System.setProperty("http.keepAlive", "false");
        }
    }

}
