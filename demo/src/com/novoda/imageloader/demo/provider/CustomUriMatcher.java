package com.novoda.imageloader.demo.provider;

import com.novoda.imageloader.demo.activity.BigImages;
import com.novoda.imageloader.demo.activity.FromCacheOnly;
import com.novoda.imageloader.demo.activity.ImageLongList;

import android.content.UriMatcher;
import android.net.Uri;

public class CustomUriMatcher extends UriMatcher {

  public static final String IMAGE_COLLECTION_TYPE = "vnd.android.cursor.dir/vnd.imageloader.demo." + ImageLongList.class.getSimpleName().toLowerCase();  
  public static final int IMAGE_INCOMING_COLLECTION = 20;
  
  public static final String IMAGE_BIGIMAGES_COLLECTION_TYPE = "vnd.android.cursor.dir/vnd.imageloader.demo." + BigImages.class.getSimpleName().toLowerCase();
  public static final int IMAGE_BIGIMAGES_INCOMING_COLLECTION = 40;
  
  public static final String IMAGE_FROMCACHEONLY_COLLECTION_TYPE = "vnd.android.cursor.dir/vnd.imageloader.demo." + FromCacheOnly.class.getSimpleName().toLowerCase();
  public static final int IMAGE_FROMCACHEONLY_INCOMING_COLLECTION = 60;

  public CustomUriMatcher(int code) {
    super(code);
    setUp();
  }

  public CustomUriMatcher() {
    super(UriMatcher.NO_MATCH);
    setUp();
  }

  public void setUp() {
    add(ImageLongList.class.getSimpleName().toLowerCase(), IMAGE_INCOMING_COLLECTION);
    add(BigImages.class.getSimpleName().toLowerCase(), IMAGE_BIGIMAGES_INCOMING_COLLECTION);
    add(FromCacheOnly.class.getSimpleName().toLowerCase(), IMAGE_FROMCACHEONLY_INCOMING_COLLECTION);
  }

  public void add(String path, int code) {
    super.addURI("com.novoda.imageloader.demo", path, code);
  }

  public static final String[] getIdSelectionArgumentsFromUri(Uri uri) {
    return new String[] { uri.getPathSegments().get(1) };
  }
}
