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
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.novoda.imageloader.core.LoaderSettings;
import com.novoda.imageloader.core.exception.ImageNotFoundException;
import com.novoda.imageloader.core.file.util.FileUtil;

public class UrlNetworkLoader implements NetworkManager {

    private FileUtil fileUtil = new FileUtil();
    private LoaderSettings settings;

    public UrlNetworkLoader(LoaderSettings settings) {
        this.settings = settings;
    }

    @Override
    public void retrieveImage(String url, File f) {
        InputStream is = null;
        OutputStream os = null;
        HttpURLConnection conn = null;
        applyChangeonSdkVersion(settings.getSdkVersion());
        try {
            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setConnectTimeout(settings.getConnectionTimeout());
            conn.setReadTimeout(settings.getReadTimeout());
            is = conn.getInputStream();
            os = new FileOutputStream(f);
            fileUtil.copyStream(is, os);
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

    private void applyChangeonSdkVersion(String sdkVersion) {
        if (Integer.parseInt(sdkVersion) < 8) {
            System.setProperty("http.keepAlive", "false");
        }
    }

}
