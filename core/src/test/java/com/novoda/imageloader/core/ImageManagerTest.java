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
package com.novoda.imageloader.core;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

public class ImageManagerTest {
    
    private LoaderSettings loaderSettings;
    private Context context;
    
    @Before
    public void beforeEveryTest() {
        loaderSettings = mock(LoaderSettings.class);
        when(loaderSettings.isCleanOnSetup()).thenReturn(false);
        context = mock(Context.class);
    }
    
    @Test(expected = RuntimeException.class)
    public void shouldComplaingIfInternetPermissionIsNotSet() {
        PackageManager pm = mock(PackageManager.class);
        when(pm.checkPermission(Manifest.permission.INTERNET, null))
            .thenReturn(PackageManager.PERMISSION_DENIED);
        when(context.getPackageManager()).thenReturn(pm);
        
        new ImageManager(context, loaderSettings) {
            protected void setLoader(LoaderSettings settings) {};
        };
    }
    
    @Test(expected = RuntimeException.class)
    public void shouldComplaingIfWriteExternalStoragePermissionIsNotSet() {
        PackageManager pm = mock(PackageManager.class);
        when(pm.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, null))
            .thenReturn(PackageManager.PERMISSION_DENIED);
        when(context.getPackageManager()).thenReturn(pm);
        
        new ImageManager(context, loaderSettings) {
            protected void setLoader(LoaderSettings settings) {};
        };
    }

}
