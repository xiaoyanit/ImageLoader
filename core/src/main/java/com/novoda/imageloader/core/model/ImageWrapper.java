package com.novoda.imageloader.core.model;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.novoda.imageloader.core.util.BitmapDisplayer;

public class ImageWrapper {
 
	private String url;
	private int loadingResourceId;
	private int notFoundResourceId;
	private int width;
	private int height;
  private ImageView imageView;

  public ImageWrapper(ImageView imageView) {
    this.imageView = imageView;
    ImageTag tag = (ImageTag)imageView.getTag();
    this.url = tag.getUrl();
    this.loadingResourceId = tag.getLoadingResourceId();
    this.notFoundResourceId = tag.getNotFoundResourceId();
    this.height = tag.getHeight();
    this.width = tag.getWidth();
    if(notFoundResourceId == 0) {
    	this.notFoundResourceId = tag.getLoadingResourceId();
    }
  }

	public String getCurrentUrl() {
		ImageTag tag = (ImageTag)imageView.getTag();
	  return tag.getUrl();
  }
	
	public String getUrl() {
	  return url;
  }
	
	public int getWidth() {
	  return width;
  }
	
	public int getHeight() {
	  return height;
  }
	
	public void runOnUiThread(BitmapDisplayer displayer) {
		Activity a = (Activity) imageView.getContext();
    a.runOnUiThread(displayer);
	}
	
	public Context getContext() {
		return (Activity) imageView.getContext();
	}

	public void setBitmap(Bitmap bitmap) {
		imageView.setImageBitmap(bitmap);
  }

	public boolean isCorrectUrl(String url) {
	  return url.equals(getUrl());
  }

	public int getLoadingResourceId() {
	  return loadingResourceId;
  }
	
	public int getNotFoundResourceId() {
	  return notFoundResourceId;
  }

}
