package com.novoda.imageloader.demo.activity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter.ViewBinder;

import com.novoda.imageloader.core.ImageManager;
import com.novoda.imageloader.core.model.ImageTagFactory;
import com.novoda.imageloader.demo.DemoApplication;
import com.novoda.imageloader.demo.R;
import com.novoda.imageloader.demo.activity.base.SingleTableBaseListActivity;

import java.util.Locale;

/**
 * Example of setting a specific image size. Not that you can ask the imageLoader to store the small image as files. In this way you don't need to scale images
 * every time
 */
public class ImageLongList extends SingleTableBaseListActivity {

	private static final int SIZE = 400;

	/**
	 * TODO Generally we can keep an instance of the image loader and the imageTagFactory.
	 */
	private ImageManager imageManager;
	private ImageTagFactory imageTagFactory;

	@Override
	protected String getTableName() {
		return ImageLongList.class.getSimpleName().toLowerCase(Locale.UK);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.single_table_base_list_activity);

        /**
         * TODO Need to prepare imageLoader and imageTagFactory
         */
        initImageLoader();

        setAdapter();
		initButtons();
	}

    private void initImageLoader() {
        imageManager = DemoApplication.getImageLoader();
        imageTagFactory = ImageTagFactory.newInstance(SIZE, SIZE, R.drawable.bg_img_loading);
        imageTagFactory.setErrorImageId(R.drawable.bg_img_notfound);
        imageTagFactory.setSaveThumbnail(true);
        setAnimationFromIntent(imageTagFactory);
    }

    /**
	 * TODO Generally you will have a binder where you have to set the image. This is an example of using the imageManager to load
	 */
	@Override
	protected ViewBinder getViewBinder() {
		return new ViewBinder() {
			@Override
			public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
				String url = cursor.getString(columnIndex);
				((ImageView) view).setTag(buildTagWithButtonOptions(imageTagFactory, url));
				imageManager.getLoader().load((ImageView) view);
				return true;
			}

		};
	}

}