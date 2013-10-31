package com.novoda.imageloader.acceptance;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.AbsListView;

import com.jayway.android.robotium.solo.Solo;
import com.novoda.imageloader.demo.R;
import com.novoda.imageloader.demo.activity.ImageLongList;

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

    public void testScrollingDownTheEntireList() {
        AbsListView list = (AbsListView) solo.getView(R.id.list_view);
        solo.scrollListToTop(list);

        boolean shouldScroll = true;
        while (shouldScroll) {
            shouldScroll = solo.scrollDownList(list);
        }

        int lastVisiblePosition = list.getLastVisiblePosition();
        int selectedPosition = list.getSelectedItemPosition();

        Log.d("IMAGELOADER", "adapter count: " + list.getAdapter().getCount());
        Log.d("IMAGELOADER", "lastVis: " + lastVisiblePosition);
        Log.d("IMAGELOADER", "selected: " + selectedPosition);
        assertEquals(list.getLastVisiblePosition(), list.getSelectedItemPosition());
    }
}

