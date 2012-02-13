package com.novoda.imageloader.core.model;

public class ImageTag {

	private String url;
	private int loadingResourceId;
	private int notFoundResourceId;
	private int height;
	private int width;
	private boolean useOnlyCache;
	
	public ImageTag(String url, int loadingResourceId, int notFoundResourceId, int width, int height) {
		this.url = url;
		this.loadingResourceId = loadingResourceId;
		this.notFoundResourceId = notFoundResourceId;
		this.width = width;
		this.height = height;
	}

	public String getUrl() {
	  return url;
  }

	public int getNotFoundResourceId() {
	  return notFoundResourceId;
  }

	public int getLoadingResourceId() {
	  return loadingResourceId;
  }

	public int getHeight() {
	  return height;
  }
	
	public int getWidth() {
	  return width;
  }

	public boolean isUseOnlyCache() {
	  return useOnlyCache;
  }

	public void setUseOnlyCache(boolean useOnlyCache) {
	  this.useOnlyCache = useOnlyCache;
  }
	
}
