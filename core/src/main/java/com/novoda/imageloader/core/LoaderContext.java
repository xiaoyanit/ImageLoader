package com.novoda.imageloader.core;

import com.novoda.imageloader.core.bitmap.BitmapUtil;
import com.novoda.imageloader.core.cache.CacheManager;
import com.novoda.imageloader.core.file.FileManager;
import com.novoda.imageloader.core.network.NetworkManager;

public class LoaderContext {
	private FileManager fileManager;
	private NetworkManager networkManager;
	private CacheManager cache;
	private CacheManager resBitmapCache;
	private LoaderSettings settings;
	private BitmapUtil bitmapUtil = new BitmapUtil();
	
	public FileManager getFileManager() {
    return fileManager;
  }
	public void setFileManager(FileManager fileManager) {
    this.fileManager = fileManager;
  }
	public NetworkManager getNetworkManager() {
    return networkManager;
  }
	public void setNetworkManager(NetworkManager networkManager) {
    this.networkManager = networkManager;
  }
	public LoaderSettings getSettings() {
    return settings;
  }
	public void setSettings(LoaderSettings settings) {
    this.settings = settings;
  }
	public CacheManager getResBitmapCache() {
	  return resBitmapCache;
  }
	public void setResBitmapCache(CacheManager resBitmapCache) {
	  this.resBitmapCache = resBitmapCache;
  }
	public CacheManager getCache() {
	  return cache;
  }
	public void setCache(CacheManager cache) {
	  this.cache = cache;
  }
	
	public BitmapUtil getBitmapUtil() {
	  return bitmapUtil;
  }
}
