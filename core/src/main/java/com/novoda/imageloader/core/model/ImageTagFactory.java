package com.novoda.imageloader.core.model;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

public class ImageTagFactory {
	
	private int width;
	private int height;
	private int defaultImageResId;
	private int errorImageResId;
	
	public ImageTagFactory(int width, int height, int defaultImageResId) {
		this.width = width;
		this.height = height;
		this.defaultImageResId = defaultImageResId;
		this.errorImageResId = defaultImageResId;
	}
	
	public ImageTagFactory(Context context, int defaultImageResId) {
		Display d = ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
  	DisplayMetrics dm = new DisplayMetrics();
  	d.getMetrics(dm);
  	this.width = d.getWidth(); 
  	this.height = d.getHeight();
  	this.defaultImageResId = defaultImageResId;
		this.errorImageResId = defaultImageResId;
  }
	
	public void setErrorImageId(int errorImageResId) {
		this.errorImageResId = errorImageResId;
	}
	
	public ImageTag build(String url) {
		return new ImageTag(url, defaultImageResId, errorImageResId, width, height);
	}

}
