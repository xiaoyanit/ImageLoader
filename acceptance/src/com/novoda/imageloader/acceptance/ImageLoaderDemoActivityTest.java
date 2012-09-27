package com.novoda.imageloader.acceptance;

import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.widget.ListView;
import com.jayway.android.robotium.solo.Solo;
import com.novoda.imageloader.demo.activity.BigImages;

public class ImageLoaderDemoActivityTest extends ActivityInstrumentationTestCase2<BigImages> {

	private static final int QUICK = 2;
	
	private Solo solo;
	private ListView list;

	public ImageLoaderDemoActivityTest() {
		super("com.novoda.imageloader.demo", BigImages.class);
	}
	
	public void testOpenTheActivity(){
		solo = new Solo(getInstrumentation(), getActivity());
		assertNotNull(solo);
	}
	
	//TODO work in progress
	@UiThreadTest
	public void IGNORE_testLoadingImage() {
		solo = new Solo(getInstrumentation(), getActivity());
      list = getActivity().getListView();
		int i = 0;
		try {
			while (true) {
				list.setSelection(i);
				i++;
			}
		} catch (Exception e) {
			assertFalse("" + e.getMessage(), i == 0);
		}
		try {
			while (true) {
				list.setSelection(i);
				i--;
			}
		} catch (Exception e) {
			assertTrue("" + e.getMessage(), i > 0);
		}
	}

	private boolean isListAtTheTop() {
		return list.getFirstVisiblePosition() == 0;
	}

	private boolean isListAtTheBottom(){
		return list.getLastVisiblePosition() == (list.getCount() - 1);
	}

	public void startNextActivity(){
//		btn = getActivity().getString(R.string.)
//		robotium.clickOnButton(btn);
	}

    public void testScrollingThroughList() {
        solo = new Solo(getInstrumentation(), getActivity());
        list = getActivity().getListView();
        for (int i = 0; i < list.getCount(); i++) {
            solo.scrollDown();
        }

        assertEquals(list.getCount() -1, list.getSelectedItemPosition());
    }

}
