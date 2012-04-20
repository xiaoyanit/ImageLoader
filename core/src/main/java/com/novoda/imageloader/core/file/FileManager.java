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
package com.novoda.imageloader.core.file;

import java.io.File;

import android.content.Context;
import android.graphics.Bitmap;

public interface FileManager {

    void delete(Context context);

    void clean(Context context);

    String getFilePath(String url);

    File getFile(String url);

    void saveBitmap(String fileName, Bitmap b, int width, int height);

    File getFile(String url, int width, int height);

}
