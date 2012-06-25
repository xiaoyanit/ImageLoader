package com.novoda.imageloader.core.util;

import java.io.InputStream;

import android.graphics.Bitmap;

import com.novoda.imageloader.core.LoaderSettings;
import com.novoda.imageloader.core.bitmap.BitmapUtil;
import com.novoda.imageloader.core.network.NetworkManager;
import com.novoda.imageloader.core.network.UrlNetworkLoader;

/**
 * Direct loader make use of the NetworkManager and the BitmapUtil
 * to provide a direct way to get a Bitmap given a http url.
 */
public class DirectLoader {

    private NetworkManager networkManager;
    private BitmapUtil bitmapUtil;
    
    public DirectLoader() {
        networkManager = new UrlNetworkLoader(new LoaderSettings());
        bitmapUtil = new BitmapUtil();
    }
    
    public Bitmap download(String url) {
        if(url == null) {
           return null; 
        }
        if(url.length() == 0) {
            return null; 
        }
        InputStream is = networkManager.retrieveInputStream(url);
        if(is == null) {
            return null;
        }
        return bitmapUtil.decodeInputStream(is);
    }
}
