package com.novoda.imageloader.demo;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.ViewBinder;

import com.novoda.imageloader.core.ImageManager;

public class FromCacheOnly extends BaseListActivity {

	private static final String[] FROM = new String[] { "url" };
	private static final int[] TO = new int[] { R.id.list_item_image };
	private static final Uri URI = Uri.parse("content://com.novoda.imageloader.demo/fromcacheonly");

	// TODO add this to your class
	private ImageManager imageLoader;
	//

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.from_cache_only);
		// TODO add this to your class
		imageLoader = ImageLoaderDemoApplication.getImageLoader();
		//
		setAdapter();
	}

	private ViewBinder getViewBinder() {
		return new ViewBinder() {
			@Override
			public boolean setViewValue(View view, Cursor cursor,
					int columnIndex) {
				try {
					((ImageView) view).setTag(cursor.getString(columnIndex));
					// TODO add this to your class
					imageLoader.loadFromCacheOnly(cursor.getString(columnIndex),
							FromCacheOnly.this, (ImageView) view);
					//
				} catch (Exception e) {
					Log.e("ImageLoader", "exception : " + e.getMessage());
				}
				return true;
			}
		};
	}

	private SimpleCursorAdapter initAdapter() {
		return new SimpleCursorAdapter(this, R.layout.image_item, getCursor(), FROM, TO);
	}

	private Cursor getCursor() {
		return managedQuery(URI, null, null, null, null);
	}

	private void setAdapter() {
		SimpleCursorAdapter adapter = initAdapter();
		ListView lv = getListView();
		ViewBinder binder = getViewBinder();
		if (binder != null) {
			adapter.setViewBinder(binder);
		}
		lv.setAdapter(adapter);
	}

}
