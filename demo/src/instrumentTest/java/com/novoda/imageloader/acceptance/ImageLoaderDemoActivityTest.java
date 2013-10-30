package com.novoda.imageloader.acceptance;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.AbsListView;

import com.jayway.android.robotium.solo.Solo;
import com.novoda.imageloader.demo.activity.ImageLongList;

public class ImageLoaderDemoActivityTest extends ActivityInstrumentationTestCase2<ImageLongList> {

    private static final int LIST_INDEX = 0;
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

    public void testScrollingThroughList() {
        solo.waitForActivity(solo.getCurrentActivity().toString());
        AbsListView list = solo.getCurrentListViews().get(LIST_INDEX);
        for (int i = 0; i < Math.max(2, list.getCount()); i++) {
            solo.scrollDown();
        }
        assertEquals(list.getLastVisiblePosition(), list.getSelectedItemPosition());
    }

}
