package com.novoda.imageloader.core.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import android.graphics.Bitmap;

import com.novoda.imageloader.core.bitmap.BitmapUtil;
import com.novoda.imageloader.core.network.NetworkManager;

//@RunWith(PowerMockRunner.class)
//@PrepareForTest({ Bitmap.class })
public class DirectLoaderTest {
    
    private DirectLoader directLoader;
    private NetworkManager networkManager;
    private BitmapUtil bitmapUtil;
    
    @Before
    public void beforeEachTest() {
        networkManager = Mockito.mock(NetworkManager.class);
        bitmapUtil = Mockito.mock(BitmapUtil.class);
        directLoader = new DirectLoader(networkManager, bitmapUtil);
    }
    
    @Test
    public void shouldReturnNullIfUrlIsNull() {
        assertNull(directLoader.download(null));
    }
    
    @Test
    public void shouldReturnNullIfUrlIsEmpty() {
        assertNull(directLoader.download(""));
    }
    
    @Test
    public void shouldReturnNullIfIsNotPossibleToGetAnInputStreamFromNetwrokResource() {
        String url = "http://www.google.com";
        Mockito.when(networkManager.retrieveInputStream(url)).thenReturn(null);
        assertNull(directLoader.download(url));
    }
    
//    @Test
//    public void shouldReturnBitmapFromHttpResource() {
//        String url = "http://www.google.com";
//        InputStream is = Mockito.mock(InputStream.class);
//        Mockito.when(networkManager.retrieveInputStream(url)).thenReturn(is);
//        Bitmap expectedBitmap = PowerMockito.mock(Bitmap.class);
//        PowerMockito.when(bitmapUtil.decodeInputStream(is)).thenReturn(expectedBitmap);        
//        Bitmap actualBitmap = directLoader.download(url);
//        assertEquals(expectedBitmap, actualBitmap);
//    }

}
