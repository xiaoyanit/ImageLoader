package com.novoda.imageloader.demo.activity;

import com.novoda.imageloader.core.model.ImageTagFactory;
import com.novoda.imageloader.demo.DemoApplication;
import com.novoda.imageloader.demo.R;
import com.novoda.imageloader.demo.activity.base.SingleTableBaseListActivity;

public class ImageLongList extends SingleTableBaseListActivity {

	private static final int SIZE = 400;
	
	protected void prepareLoader() {
		imageManager = DemoApplication.getImageLoader();
		imageTagFactory = new ImageTagFactory(SIZE, SIZE, R.drawable.bg_img_loading);
		imageTagFactory.setErrorImageId(R.drawable.bg_img_notfound);
	}
	
	@Override
	protected String getTableName() {
		return ImageLongList.class.getSimpleName().toLowerCase();
	}

}