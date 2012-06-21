package com.novoda.imageloader.demo.activity;

import com.novoda.imageloader.core.model.ImageTagFactory;
import com.novoda.imageloader.demo.DemoApplication;
import com.novoda.imageloader.demo.R;
import com.novoda.imageloader.demo.activity.base.SingleTableBaseListActivity;

public class FromCacheOnly extends SingleTableBaseListActivity {

    @Override
    protected String getTableName() {
        return FromCacheOnly.class.getSimpleName().toLowerCase();
    }

    protected void prepareLoader() {
        imageManager = DemoApplication.getImageLoader();
        imageTagFactory = new ImageTagFactory(this, R.drawable.bg_img_loading);
        imageTagFactory.setErrorImageId(R.drawable.bg_img_notfound);
        imageTagFactory.setUseOnlyCache(true);
    }
}
