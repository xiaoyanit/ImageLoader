package com.novoda.imageloader.core.file;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.novoda.imageloader.core.LoaderSettings;
import com.novoda.imageloader.core.Util;

public class BasicFileManagerTest {

    private BasicFileManager basicFileManager;
    private LoaderSettings settings;
    private File cacheDir;
       
    @Before
    public void beforeEachTest() {
        settings = Mockito.mock(LoaderSettings.class);
        basicFileManager = new BasicFileManager(settings) {
            @Override
            public void cleanOldFiles() {
                //overriding to tests
            }
        };
        cacheDir = new File(Util.FOLDER_FOR_TEST_TMP_FILES);
        cacheDir.mkdirs();
    }
    
    @After 
    public void afterEachTest() throws IOException {
        FileUtils.deleteDirectory(cacheDir);
    } 
    
    @Test
    public void shouldDistinguishBetweenUrlWithQueryIfIsQueryIncludedInHashIsTrue() {
        Mockito.when(settings.getCacheDir()).thenReturn(cacheDir);
        Mockito.when(settings.isQueryIncludedInHash()).thenReturn(true);
        
        String filePath1 = basicFileManager.getFile("http://googl.com?param=1").getAbsolutePath();
        String filePath2 = basicFileManager.getFile("http://googl.com?param=2").getAbsolutePath();
        
        assertFalse(filePath2.equalsIgnoreCase(filePath1));
    }
    
    @Test
    public void shouldNotDistinguishBetweenUrlWithQueryIfIsQueryIncludedInHashIsFalse() {
        Mockito.when(settings.getCacheDir()).thenReturn(cacheDir);
        Mockito.when(settings.isQueryIncludedInHash()).thenReturn(false);
        
        String filePath1 = basicFileManager.getFile("http://googl.com?param=1").getAbsolutePath();
        String filePath2 = basicFileManager.getFile("http://googl.com?param=2").getAbsolutePath();
        
        assertTrue(filePath2.equalsIgnoreCase(filePath1));
    }
    
}
