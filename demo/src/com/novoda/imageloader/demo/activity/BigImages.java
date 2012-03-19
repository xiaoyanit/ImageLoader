package com.novoda.imageloader.demo.activity;

import com.novoda.imageloader.demo.activity.base.SingleTableBaseListActivity;

public class BigImages extends SingleTableBaseListActivity {

    @Override
    protected String getTableName() {
        return BigImages.class.getSimpleName().toLowerCase();
    }

}
