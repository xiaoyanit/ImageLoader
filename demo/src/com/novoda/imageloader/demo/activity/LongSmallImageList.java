package com.novoda.imageloader.demo.activity;

import com.novoda.imageloader.core.model.ImageTagFactory;
import com.novoda.imageloader.demo.DemoApplication;
import com.novoda.imageloader.demo.R;
import com.novoda.imageloader.demo.activity.base.SingleTableBaseListActivity;

public class LongSmallImageList extends SingleTableBaseListActivity {
	
	private static final int SIZE = 80;
	
	@Override
	protected int getImageItem() {
		return R.layout.small_image_item;
	}
	
	@Override
	protected void prepareLoader() {
		imageManager = DemoApplication.getThumbnailImageLoader();
		imageTagFactory = new ImageTagFactory(SIZE, SIZE, R.drawable.bg_img_loading);
		imageTagFactory.setErrorImageId(R.drawable.bg_img_notfound);
	}

	@Override
	protected String getTableName() {
		return LongSmallImageList.class.getSimpleName().toLowerCase();
	}

}