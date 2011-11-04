package com.novoda.imageloader.core.util;

import android.content.Context;

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

  public Settings build(Context context){
    settings.setCacheDir(new FileUtil().prepareCacheDirectory(context));
    return settings;
  }
  
}
