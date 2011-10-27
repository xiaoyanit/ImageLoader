package com.novoda.imageloader.acceptance;

import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.ListView;

import com.jayway.android.robotium.solo.Solo;
import com.novoda.imageloader.demo.FromCacheOnly;

public class FromCacheOnlyTest extends ActivityInstrumentationTestCase2<FromCacheOnly> {

  private static final int NORMAL = 7;
  
  private Solo robotium;
  private ListView list;

  public FromCacheOnlyTest() {
    super("com.novoda.imageloader.demo", FromCacheOnly.class);
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    robotium = new Solo(getInstrumentation(), getActivity());
    list = getActivity().getListView();
    assertNotNull(robotium);
  }

  public void test_SHOULD_load_only_first_image() {
    Object object = list.getItemAtPosition(0);
   
    //TODO not sure what to do here for now
    //I need to check the images
  }

}
