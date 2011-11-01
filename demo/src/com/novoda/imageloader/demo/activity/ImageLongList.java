package com.novoda.imageloader.demo.activity;

import com.novoda.imageloader.demo.activity.base.SingleTableBaseListActivity;

public class ImageLongList extends SingleTableBaseListActivity {

	@Override
	protected String getTableName() {
		return ImageLongList.class.getSimpleName().toLowerCase();
	}

}