package com.novoda.imageloader.demo.activity;

import android.view.View;
import android.widget.ImageView;

import com.novoda.imageloader.demo.activity.base.SingleTableBaseListActivity;

public class FromCacheOnly extends SingleTableBaseListActivity {

	@Override
	protected String getTableName() {
		return FromCacheOnly.class.getSimpleName().toLowerCase();
	}
	
	protected void load(View view) {
		// TODO add this to your class
		imageLoader.loadFromCacheOnly((ImageView) view);
		//
	}

}
