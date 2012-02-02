package com.novoda.imageloader.core.file;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import com.novoda.imageloader.core.exception.ImageCopyException;
import com.novoda.imageloader.core.exception.ImageNotFoundException;

import android.content.Context;
import android.util.Log;

public class FileUtil {

  private static final String NOMEDIA_FILE_NAME = ".nomedia";
  private static final String DEFAULT_IMAGE_FOLDER_NAME = "/imagedata";
  private static final String TAG = "ImageLoader";  

  private static final int BUFFER_SIZE = 6*1024;
  
  public void retrieveImage(String url, File f, int connectionTimeout, int readTimeout) {
    InputStream is = null;
    OutputStream os = null;
    try {
    	URLConnection conn = new URL(url).openConnection();
    	conn.setConnectTimeout(connectionTimeout);
    	conn.setReadTimeout(readTimeout);
      is = conn.getInputStream();
      os = new FileOutputStream(f);
      copyStream(is, os);
      os.close();
    } catch (FileNotFoundException fnfe) {
      throw new ImageNotFoundException();
    } catch (Exception ex) {
      Log.e(TAG, "Unknown Exception while getting the image " + ex.getMessage(), ex);
    } finally {
      closeSilently(is);
      closeSilently(os);
    }
  }

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
      byte[] bytes = new byte[BUFFER_SIZE];
      for (;;) {
        int count = is.read(bytes, 0, BUFFER_SIZE);
        if (count == -1) {
          break;
        }
        os.write(bytes, 0, count);
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
    if(!isMounted()) {
      dir =  preparePhoneCacheDir(context);
    } else {
      dir = prepareExternalCacheDir(context);
    }
    addNomediaFile(dir);
    return dir;
  }
  
  public boolean isMounted() {
    if (android.os.Environment.getExternalStorageState().equals(
        android.os.Environment.MEDIA_MOUNTED)) {
      return true;
    }
    return false;
  }
  
  private File prepareExternalCacheDir(Context context) {
    String relativepath = context.getPackageName() + DEFAULT_IMAGE_FOLDER_NAME;
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
    } catch(Exception e) {
      //Don't care if doesn't save the file
    }
  }

}