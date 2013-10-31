package com.novoda.imageloader.acceptance;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.AbsListView;

import com.jayway.android.robotium.solo.Solo;
import com.novoda.imageloader.demo.activity.ImageLongList;
import com.novoda.imageloader.demo.R;

public class ImageLoaderDemoActivityTest extends ActivityInstrumentationTestCase2<ImageLongList> {
    private Solo solo;

    public ImageLoaderDemoActivityTest() {
        super("com.novoda.imageloader.demo", ImageLongList.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testOpenTheActivity() {
        assertNotNull(solo);
    }

    public void testScrollingDownTheList() {
        AbsListView list = (AbsListView) solo.getView(R.id.list_view);
        solo.scrollListToTop(list);
        
        boolean shouldScroll = true;
        while (shouldScroll) {
            shouldScroll = solo.scrollDownList(list);
        }
        
        assertEquals(list.getLastVisiblePosition(), list.getSelectedItemPosition());
    }
}

