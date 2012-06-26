package com.novoda.imageloader.core.network;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import com.novoda.imageloader.core.LoaderSettings;
import com.novoda.imageloader.core.Util;
import com.novoda.imageloader.core.exception.ImageNotFoundException;
import com.novoda.imageloader.core.file.util.FileUtil;

public class UrlNetworlManagerTest {
    
    private UrlNetworkManager urlNetworkManager;
    private LoaderSettings loaderSettings;
    private HttpURLConnection httpURLConnection;
    private FileUtil fileUtil;
    private File cacheDir;
    private File imageFile;
    
    @Before
    public void beforeEachTest() throws IOException {
        loaderSettings = mock(LoaderSettings.class);
        fileUtil = mock(FileUtil.class);
        when(loaderSettings.getSdkVersion()).thenReturn("8");
        httpURLConnection = mock(HttpURLConnection.class);
        urlNetworkManager = new UrlNetworkManager(loaderSettings, fileUtil) {
            @Override
            protected HttpURLConnection openConnection(String url) throws IOException, MalformedURLException {
                return httpURLConnection;
            }
        };
        cacheDir = new File(Util.FOLDER_FOR_TEST_TMP_FILES);
        cacheDir.mkdirs();
        imageFile = new File(cacheDir.getAbsolutePath() + "/test.jpg");
        imageFile.createNewFile();
    }

    @After 
    public void afterEachTest() throws IOException {
        FileUtils.deleteDirectory(cacheDir);
    } 
    
    @Test(expected = ImageNotFoundException.class)
    public void shouldThrowFileNotFoundExecptionIfFileDoesNotExists() throws IOException {
        File imageFile = new File(cacheDir.getAbsolutePath() + "/");
        imageFile.createNewFile();
        urlNetworkManager.retrieveImage("http://king.com", imageFile);
    }
    
    @Test
    public void shouldExecuteCopyStream() {
        urlNetworkManager.retrieveImage("http://king.com", imageFile);
        verify(fileUtil).copyStream(any(InputStream.class), any(FileOutputStream.class));
    }
    
    @Test
    public void shouldAlwaysCallStreams() {
        urlNetworkManager.retrieveImage("http://king.com", imageFile);
        verify(fileUtil, atLeast(2)).closeSilently(any(Closeable.class));
    }
    
    @Test
    public void shouldCallDisconnectIfDefinedInSettings() {
        when(loaderSettings.getDisconnectOnEveryCall()).thenReturn(true);
        urlNetworkManager.retrieveImage("http://king.com", imageFile);
        verify(httpURLConnection).disconnect();
    }
    
    @Test
    public void shouldAvoidToDisconnectIfNotDefinedInSettings() {
        urlNetworkManager.retrieveImage("http://king.com", imageFile);
        verify(httpURLConnection, never()).disconnect();
    }
    
    @Test
    public void shouldFailGracefullyForUnknownExceptions() throws IOException {
        when(httpURLConnection.getInputStream()).thenThrow(new IOException());
        urlNetworkManager.retrieveImage("http://king.com", imageFile);
        verify(httpURLConnection, never()).disconnect();
    }

    @Test
    public void shouldSetKeepAliveSystemPropertyForApiLevelOlderThan8() {
        System.setProperty("http.keepAlive", "true");
        assertEquals("true", System.getProperty("http.keepAlive"));
        when(loaderSettings.getSdkVersion()).thenReturn("4");
        
        urlNetworkManager.retrieveImage("http://king.com", imageFile);
        assertEquals("false", System.getProperty("http.keepAlive"));
    }
    
    @Test
    public void shouldRetrieveInputStream() throws IOException {
        InputStream expected = mock(InputStream.class);
        when(httpURLConnection.getInputStream()).thenReturn(expected);
        InputStream actual = urlNetworkManager.retrieveInputStream("http://king.com");
        assertEquals(expected, actual);
    }
    
    @Test
    public void shouldRetrieveNullInputStreamForUnknownExceptions() throws IOException {
        when(httpURLConnection.getInputStream()).thenThrow(new IOException());
        InputStream actual = urlNetworkManager.retrieveInputStream("http://king.com");
        assertNull(actual);
    }
    
    @Test(expected = ImageNotFoundException.class)
    public void shouldThrowImageNotFoundExceptionForFileNotFoundException() throws IOException {
        when(httpURLConnection.getInputStream()).thenThrow(new FileNotFoundException());
        urlNetworkManager.retrieveInputStream("http://king.com");
    }
    
}
