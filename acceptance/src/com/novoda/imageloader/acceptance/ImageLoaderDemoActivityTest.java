package com.novoda.imageloader.acceptance;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.ListView;
import com.jayway.android.robotium.solo.Solo;
import com.novoda.imageloader.demo.activity.ImageLongList;

public class ImageLoaderDemoActivityTest extends ActivityInstrumentationTestCase2<ImageLongList> {

	private static final int QUICK = 2;
	
	private Solo solo;
	private ListView list;

	public ImageLoaderDemoActivityTest() {
		super("com.novoda.imageloader.demo", ImageLongList.class);
	}
	
	public void testOpenTheActivity(){
		solo = new Solo(getInstrumentation(), getActivity());
		assertNotNull(solo);
	}

    public void testScrollingThroughList() {
        solo = new Solo(getInstrumentation(), getActivity());
        list = getActivity().getListView();

        for (int i = 0; i < Math.max(2, list.getCount()); i++) {
            solo.scrollDown();
        }

        assertEquals(list.getLastVisiblePosition(), list.getSelectedItemPosition());
    }

}
