package com.novoda.imageloader.demo.activity.base;

import android.app.ListActivity;
import android.database.Cursor;
import android.net.Uri;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.ViewBinder;

import com.novoda.imageloader.demo.R;

public abstract class SingleTableBaseListActivity extends ListActivity {

    private static final String[] FROM = new String[] { "url" };
    private static final int[] TO = new int[] { R.id.list_item_image };

    protected int getImageItem() {
        return R.layout.image_item;
    }
    
    protected SimpleCursorAdapter initAdapter() {
        return new SimpleCursorAdapter(this, getImageItem(), getCursor(), FROM, TO);
    }

    private Cursor getCursor() {
        return managedQuery(Uri.parse("content://com.novoda.imageloader.demo/" + getTableName()), null, null, null,
                null);
    }

    protected abstract String getTableName();

    protected void setAdapter() {
        SimpleCursorAdapter adapter = initAdapter();
        ListView lv = getListView();
        ViewBinder binder = getViewBinder();
        if (binder != null) {
            adapter.setViewBinder(binder);
        }
        lv.setAdapter(adapter);
    }

    protected abstract ViewBinder getViewBinder();

}
