package com.novoda.imageloader.core.file;

import java.io.File;

import android.content.Context;

public interface FileManager {

	void delete(Context context);

	void clean(Context context);

	String getFilePath(String url);
	
	boolean exists(String path);

	File getFile(String url);

}
