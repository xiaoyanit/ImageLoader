package com.novoda.imageloader.core;

import java.io.File;

import android.content.Context;
import android.os.Build;

import com.novoda.imageloader.core.cache.CacheManager;
import com.novoda.imageloader.core.file.util.FileUtil;

public class LoaderSettings {

  private static final long DEFAULT_EXPIRATION_PERIOD = 7l * 24l * 3600l * 1000l;
  private static final boolean DEFAULT_INCLUDE_QUERY_IN_HASH = true;
  private static final int DEFAULT_CONNECTION_TIMEOUT = 10 * 1000;
  private static final int DEFAULT_READ_TIMEOUT = 10 * 1000;
  private static final boolean DEFAULT_DISCONNECT_ON_EVERY_CALL = false;
  private static final boolean DEFAULT_USE_ASYNC_TASKS = true;
  
  private File cacheDir;
  private int connectionTimeout;
  private int readTimeout;
  private long expirationPeriod;
  private boolean isQueryIncludedInHash;
	private boolean disconnectOnEveryCall;
	private String sdkVersion;
	private CacheManager cacheManager;
	private boolean useAsyncTasks;
  
  public LoaderSettings() {
    this.setExpirationPeriod(DEFAULT_EXPIRATION_PERIOD);
    this.setQueryIncludedInHash(DEFAULT_INCLUDE_QUERY_IN_HASH);
    this.setConnectionTimeout(DEFAULT_CONNECTION_TIMEOUT);
    this.setReadTimeout(DEFAULT_READ_TIMEOUT);
    this.setDisconnectOnEveryCall(DEFAULT_DISCONNECT_ON_EVERY_CALL);
    this.setUseAsyncTasks(DEFAULT_USE_ASYNC_TASKS);
  }

	public File getCacheDir() {
    return cacheDir;
  }

  public void setCacheDir(File cacheDir) {
    this.cacheDir = cacheDir;
  }

  public long getExpirationPeriod() {
    return expirationPeriod;
  }

  public void setExpirationPeriod(long expirationPeriod) {
    this.expirationPeriod = expirationPeriod;
  }

  public boolean isQueryIncludedInHash() {
    return isQueryIncludedInHash;
  }

  public void setQueryIncludedInHash(boolean isQueryIncludedInHash) {
    this.isQueryIncludedInHash = isQueryIncludedInHash;
  }

	public int getConnectionTimeout() {
	  return connectionTimeout;
  }

	public void setConnectionTimeout(int connectionTimeout) {
	  this.connectionTimeout = connectionTimeout;
  }
	
	public int getReadTimeout() {
	  return readTimeout;
  }

	public void setReadTimeout(int readTimeout) {
	  this.readTimeout = readTimeout;
  }

	public boolean getDisconnectOnEveryCall() {
	  return disconnectOnEveryCall;
  }
	
	public void setDisconnectOnEveryCall(boolean disconnectOnEveryCall) {
	  this.disconnectOnEveryCall = disconnectOnEveryCall;
  }

	public void setSdkVersion(String sdkVersion) {
	  this.sdkVersion = sdkVersion;
  }
	
	public String getSdkVersion() {
		return this.sdkVersion;
	}
	
	public CacheManager getCacheManager() {
	  return cacheManager;
  }

	public void setCacheManager(CacheManager cacheManager) {
	  this.cacheManager = cacheManager;
  }
	
	public boolean isUseAsyncTasks() {
	  return useAsyncTasks;
  }
	
	public void setUseAsyncTasks(boolean useAsyncTasks) {
		this.useAsyncTasks = useAsyncTasks;
	}

	public static class SettingsBuilder {
	  
	  private LoaderSettings settings;
	  
	  public SettingsBuilder() {
	    settings = new LoaderSettings();
	  }
	  
	  public SettingsBuilder withEnableQueryInHashGeneration(boolean enableQueryInHashGeneration) {
	    settings.setQueryIncludedInHash(enableQueryInHashGeneration);
	    return this;
	  }
	  
	  public SettingsBuilder withConnectionTimeout(int connectionTimeout) {
	    settings.setConnectionTimeout(connectionTimeout);
	    return this;
	  }
	  
	  public SettingsBuilder withReadTimeout(int readTimeout) {
	    settings.setReadTimeout(readTimeout);
	    return this;
	  }
	  
	  public SettingsBuilder withDisconnectOnEveryCall(boolean disconnectOnEveryCall) {
	    settings.setDisconnectOnEveryCall(disconnectOnEveryCall);
	    return this;
	  }
	  
	  public SettingsBuilder withCacheManager(CacheManager cacheManager) {
	    settings.setCacheManager(cacheManager);
	    return this;
	  }
	  
	  public SettingsBuilder withAsyncTasks(boolean useAsyncTasks) {
	    settings.setUseAsyncTasks(useAsyncTasks);
	    return this;
	  }

	  public LoaderSettings build(Context context){
	    settings.setCacheDir(new FileUtil().prepareCacheDirectory(context));
	    settings.setSdkVersion(Build.VERSION.SDK);
	    return settings;
	  }
	  
	}

}
