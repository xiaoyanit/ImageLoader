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
    applayChangeonSdkVersion(settings.getSdkVersion());
    try {
    	conn = (HttpURLConnection)new URL(url).openConnection();
    	conn.setConnectTimeout(settings.getConnectionTimeout());
    	conn.setReadTimeout(settings.getReadTimeout());
      is = conn.getInputStream();
      os = new FileOutputStream(f);
      fileUtil.copyStream(is, os);
    } catch (FileNotFoundException fnfe) {
      throw new ImageNotFoundException();
    } catch (Exception ex) {
    	//TODO
    } finally {
    	if(conn != null && settings.getDisconnectOnEveryCall()) {
    		conn.disconnect();    		
    	}
    	fileUtil.closeSilently(is);
    	fileUtil.closeSilently(os);
    }
  }

  private void applayChangeonSdkVersion(String sdkVersion) {
  	if (Integer.parseInt(sdkVersion) < 8) {
      System.setProperty("http.keepAlive", "false");
    }
  }

}
