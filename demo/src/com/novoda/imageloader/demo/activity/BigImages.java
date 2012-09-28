package com.novoda.imageloader.demo.activity;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter.ViewBinder;
import com.novoda.imageloader.core.ImageManager;
import com.novoda.imageloader.core.OnImageLoadedListener;
import com.novoda.imageloader.core.model.ImageTag;
import com.novoda.imageloader.core.model.ImageTagFactory;
import com.novoda.imageloader.demo.DemoApplication;
import com.novoda.imageloader.demo.R;
import com.novoda.imageloader.demo.activity.base.SingleTableBaseListActivity;

/**
 * This is an example using really big images and see
 * how the image loader can keep up with the memory limitations of android.
 */
public class BigImages extends SingleTableBaseListActivity implements OnImageLoadedListener {

    private final static String TAG = DemoApplication.class.getSimpleName().toLowerCase();

    /**
     * TODO
     * Generally we can keep an instance of the
     * image loader and the imageTagFactory.
     */
    private ImageManager imageManager;
    private ImageTagFactory imageTagFactory;

    @Override
    protected String getTableName() {
        return BigImages.class.getSimpleName().toLowerCase();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_table_base_list_activity);

        imageManager = DemoApplication.getImageLoader();
        imageTagFactory = ImageTagFactory.getInstance(this, R.drawable.bg_img_loading);
        imageTagFactory.setErrorImageId(R.drawable.bg_img_notfound);
        setAdapter();
        initButtons();
    }

    @Override
    protected void onResume() {
        super.onResume();
        imageManager.setOnImageLoadedListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        imageManager.unRegisterOnImageLoadedListener(this);
    }

    /**
     * TODO
     * Generally you will have a binder where you have to set the image.
     * This is an example of using the imageManager to load
     */
    @Override
    protected ViewBinder getViewBinder() {
        return new ViewBinder() {

            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                String url = cursor.getString(columnIndex);
                ((ImageView) view).setTag(getTag(imageTagFactory, url));
                imageManager.getLoader().load((ImageView) view);
                return true;
            }
        };
    }

    @Override
    public void OnImageLoaded(ImageView imageView) {
        Log.v(TAG, "OnImageLoaded");
        Log.i(TAG, "ImageView URL : " + ((ImageTag) imageView.getTag()).getUrl());
    }
}
