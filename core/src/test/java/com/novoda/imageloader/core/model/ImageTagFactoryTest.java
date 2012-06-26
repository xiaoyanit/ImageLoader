package com.novoda.imageloader.core.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import android.content.Context;
import android.view.Display;
import android.view.WindowManager;

public class ImageTagFactoryTest {
    
    private ImageTagFactory imageTagFactory;
    private int height = 12;
    private int width = 9;
    private int defaultResourceId = 1;
    private String url = "google.com";
    
    @Before
    public void beforeEachTest() {
        imageTagFactory = new ImageTagFactory(9, 12, 1);        
    }
    
    @Test
    public void shouldSetNormalPropertiesOnTheImageTag() {
        ImageTag imageTag = imageTagFactory.build(url);
        
        assertEquals(defaultResourceId, imageTag.getLoadingResourceId());
        assertEquals(defaultResourceId, imageTag.getNotFoundResourceId());
        assertEquals(height, imageTag.getHeight());
        assertEquals(width, imageTag.getWidth());
        assertEquals(url, imageTag.getUrl());
    }
    
    @Test
    public void shouldSetPreviewProperties() {
        int previewHeight = 1;
        int previewWidth = 2;
        imageTagFactory.usePreviewImage(previewWidth, previewHeight, true);
        ImageTag imageTag = imageTagFactory.build(url);
        
        assertEquals(previewHeight, imageTag.getPreviewHeight());
        assertEquals(previewWidth, imageTag.getPreviewWidth());
    }
    
    @Test
    public void shouldUseTheSameUrlForPreview() {
        imageTagFactory.usePreviewImage(1, 1, true);
        ImageTag imageTag = imageTagFactory.build(url);
        
        assertEquals(url, imageTag.getPreviewUrl());
    }
    
    @Test
    public void shouldNotUseTheSameUrlForPreview() {
        imageTagFactory.usePreviewImage(1, 1, false);
        ImageTag imageTag = imageTagFactory.build(url);
        
        assertNull(imageTag.getPreviewUrl());
    }
    
    @Test
    public void shouldUseDisplaySizes() {
        final Display display = mock(Display.class);
        when(display.getHeight()).thenReturn(21);
        when(display.getWidth()).thenReturn(12);
        imageTagFactory = new ImageTagFactory(null, 1) {
            @Override
            protected Display prepareDisplay(Context context) {
                return display;
            }
        };
        ImageTag imageTag = imageTagFactory.build(url);
        assertEquals(21, imageTag.getHeight());
        assertEquals(12, imageTag.getWidth());
    }

}
