package com.novoda.imageloader.core.util;

import java.io.File;

public class Settings {

  private static final long DEFAULT_EXPIRATION_PERIOD = 7l * 24l * 3600l * 1000l;
  private static final boolean DEFAULT_INCLUDE_QUERY_IN_HASH = true;
  private static final int DEFAULT_CONNECTION_TIMEOUT = 15 * 1000;
  private static final int DEFAULT_READ_TIMEOUT = 90 * 1000;
  
  private File cacheDir;
  private int connectionTimeout;
  private int readTimeout;
  private long expirationPeriod;
  private boolean isQueryIncludedInHash;
  
  public Settings() {
    this.setExpirationPeriod(DEFAULT_EXPIRATION_PERIOD);
    this.setQueryIncludedInHash(DEFAULT_INCLUDE_QUERY_IN_HASH);
    this.setConnectionTimeout(DEFAULT_CONNECTION_TIMEOUT);
    this.setReadTimeout(DEFAULT_READ_TIMEOUT);
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

}
