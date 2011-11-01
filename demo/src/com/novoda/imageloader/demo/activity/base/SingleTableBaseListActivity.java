package com.novoda.imageloader.demo.activity.base;

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
import com.novoda.imageloader.demo.DemoApplication;
import com.novoda.imageloader.demo.R;


public abstract class SingleTableBaseListActivity extends BaseListActivity {

	private static final String[] FROM = new String[] { "url" };
	private static final int[] TO = new int[] { R.id.list_item_image };

	// TODO add this to your class
	private ImageManager imageLoader;
	//

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.single_table_base_list_activity);
		// TODO add this to your class
		imageLoader = DemoApplication.getImageLoader();
		//
		setAdapter();
	}

	private ViewBinder getViewBinder() {
		return new ViewBinder() {
			@Override
			public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
				try {
					((ImageView) view).setTag(cursor.getString(columnIndex));
					load(imageLoader, view, cursor.getString(columnIndex));
				} catch (Exception e) {
					Log.e("ImageLoader", "exception : " + e.getMessage());
				}
				return true;
			}

		};
	}
	
	protected void load(ImageManager imageLoader, View view, String url) {
		// TODO add this to your class
		imageLoader.load(url, getApplicationContext(), (ImageView) view);
		//
	}

	private SimpleCursorAdapter initAdapter() {
		return new SimpleCursorAdapter(this, R.layout.image_item, getCursor(), FROM, TO);
	}

	private Cursor getCursor() {
		return managedQuery(Uri.parse("content://com.novoda.imageloader.demo/" + getTableName()), null, null, null, null);
	}

	protected abstract String getTableName();

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
