package com.novoda.imageloader.demo.activity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter.ViewBinder;
import com.novoda.imageloader.core.ImageManager;
import com.novoda.imageloader.core.model.ImageTagFactory;
import com.novoda.imageloader.demo.DemoApplication;
import com.novoda.imageloader.demo.R;
import com.novoda.imageloader.demo.activity.base.SingleTableBaseListActivity;

/**
 * Example of setting a specific image size.
 * Not that you can ask the imageLoader to store the small image as files.
 * In this way you don't need to scale images every time
 */
public class ImageLongList extends SingleTableBaseListActivity {

    private static final int SIZE = 400;

    private Animation fadeInAnimation;
    private Boolean isAnimated = false;
    /**
     * TODO
     * Generally we can keep an instance of the 
     * image loader and the imageTagFactory.
     */
    private ImageManager imageManager;
    private ImageTagFactory imageTagFactory;

    @Override
    protected String getTableName() {
        return ImageLongList.class.getSimpleName().toLowerCase();
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_table_base_list_activity);

        if (getIntent().hasExtra("animated")) {
            isAnimated = true;
            fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        }

        /**
         * TODO
         * Need to prepare imageLoader and imageTagFactory
         */
        imageManager = DemoApplication.getImageLoader();
        imageTagFactory = ImageTagFactory.newInstance(SIZE, SIZE, R.drawable.bg_img_loading);
        imageTagFactory.setErrorImageId(R.drawable.bg_img_notfound);
        imageTagFactory.setSaveThumbnail(true);
        imageTagFactory.setAnimation(fadeInAnimation);

        setAdapter();
        initButtons();
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
                ((ImageView) view).setTag(imageTagFactory.build(url));
                imageManager.getLoader().load((ImageView) view);

                return true;
            }

        };
    }

}