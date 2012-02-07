package com.novoda.imageloader.core.util;

import android.content.Context;
import android.os.Build;

import com.novoda.imageloader.core.file.FileUtil;

public class SettingsBuilder {
  
  private Settings settings;
  
  public SettingsBuilder() {
    settings = new Settings();
  }
  
  public SettingsBuilder enableQueryInHashGeneration(boolean enableQueryInHashGeneration) {
    settings.setQueryIncludedInHash(enableQueryInHashGeneration);
    return this;
  }
  
  public SettingsBuilder connectionTimeout(int connectionTimeout) {
    settings.setConnectionTimeout(connectionTimeout);
    return this;
  }
  
  public SettingsBuilder readTimeout(int readTimeout) {
    settings.setReadTimeout(readTimeout);
    return this;
  }
  
  public SettingsBuilder disconnectOnEveryCall(boolean disconnectOnEveryCall) {
    settings.setDisconnectOnEveryCall(disconnectOnEveryCall);
    return this;
  }

  public Settings build(Context context){
    settings.setCacheDir(new FileUtil().prepareCacheDirectory(context));
    settings.setSdkVersion(Build.VERSION.SDK);
    return settings;
  }
  
}
