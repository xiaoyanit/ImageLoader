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
package com.novoda.imageloader.core.model;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.novoda.imageloader.core.loader.util.BitmapDisplayer;

public class ImageWrapper {
 
	private String url;
	private int loadingResourceId;
	private int notFoundResourceId;
	private int width;
	private int height;
	private boolean isUseCacheOnly;
  private ImageView imageView;

  public ImageWrapper(ImageView imageView) {
    this.imageView = imageView;
    ImageTag tag = (ImageTag)imageView.getTag();
    if(tag == null) {
    	return;
    }
    this.url = tag.getUrl();
    this.loadingResourceId = tag.getLoadingResourceId();
    this.notFoundResourceId = tag.getNotFoundResourceId();
    this.isUseCacheOnly = tag.isUseOnlyCache();
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

	public boolean isUrlChanged() {
	  return !getUrl().equals(getCurrentUrl());
  }

	public boolean isUseCacheOnly() {
	  return isUseCacheOnly;
  }

}
