package com.novoda.imageloader.core.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import android.widget.ImageView;


public class ImageWrapperTest {
    
    private ImageWrapper imageWrapper;
    private ImageView imageView;
    private ImageTag imageTag;
    
    @Before
    public void beforeEachTest() {
        imageView = mock(ImageView.class);
        imageTag = mock(ImageTag.class);
        
    }
    
    @Test
    public void shouldNotFailIfImageTagIsNull() {
        imageWrapper = new ImageWrapper(imageView);
    }
    
    @Test
    public void shouldSetLoadingResourceId() {
        when(imageTag.getLoadingResourceId()).thenReturn(1);
        when(imageView.getTag()).thenReturn(imageTag);
        
        imageWrapper = new ImageWrapper(imageView);
        assertEquals(1, imageWrapper.getLoadingResourceId());
    }
    
    @Test
    public void shouldSetNotFoundResourceIdIfDefined() {
        when(imageTag.getNotFoundResourceId()).thenReturn(2);
        when(imageView.getTag()).thenReturn(imageTag);
        
        imageWrapper = new ImageWrapper(imageView);
        assertEquals(2, imageWrapper.getNotFoundResourceId());
    }
    
    @Test
    public void shouldReturnTrueIfCurrentUrlHasChanged() {
        when(imageTag.getUrl()).thenReturn("url1");
        when(imageView.getTag()).thenReturn(imageTag);
        
        imageWrapper = new ImageWrapper(imageView);
        when(imageTag.getUrl()).thenReturn("url2");
        
        assertTrue(imageWrapper.isUrlChanged());
    }
    
    @Test
    public void shouldReturnFalseIfCurrentUrlHasNotChanged() {
        when(imageTag.getUrl()).thenReturn("url1");
        when(imageView.getTag()).thenReturn(imageTag);
        
        imageWrapper = new ImageWrapper(imageView);
        when(imageTag.getUrl()).thenReturn("url1");
        
        assertFalse(imageWrapper.isUrlChanged());
    }
    
}
