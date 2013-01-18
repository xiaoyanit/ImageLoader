package com.novoda.imageloader.acceptance;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.AbsListView;

import com.jayway.android.robotium.solo.Solo;
import com.novoda.imageloader.demo.activity.ImageLongList;

public class ImageLoaderDemoActivityTest extends ActivityInstrumentationTestCase2<ImageLongList> {

	private Solo solo;
	private AbsListView list;

	public ImageLoaderDemoActivityTest() {
		super("com.novoda.imageloader.demo", ImageLongList.class);
	}

	public void testOpenTheActivity() {
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
