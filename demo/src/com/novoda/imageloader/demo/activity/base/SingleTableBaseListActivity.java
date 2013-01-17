package com.novoda.imageloader.demo.activity.base;

import android.app.ListActivity;
import android.database.Cursor;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.ViewBinder;

import com.novoda.imageloader.core.model.ImageTag;
import com.novoda.imageloader.core.model.ImageTagFactory;
import com.novoda.imageloader.core.util.AnimationHelper;
import com.novoda.imageloader.demo.R;

public abstract class SingleTableBaseListActivity extends ListActivity implements View.OnClickListener {

    private static final String[] FROM = new String[] { "url" };
    private static final int[] TO = new int[] { R.id.list_item_image };

    private boolean null_tag;
    private boolean null_url;

    private SimpleCursorAdapter adapter;

    protected abstract String getTableName();

    protected abstract ViewBinder getViewBinder();

    protected int getImageItem() {
        return R.layout.image_item;
    }

    protected void setAdapter() {
    	adapter = initAdapter();
        ViewBinder binder = getViewBinder();
        if (binder != null) {
            adapter.setViewBinder(binder);
        }
        getListView().setAdapter(adapter);
    }

    private SimpleCursorAdapter initAdapter() {
        return new SimpleCursorAdapter(this, getImageItem(), getCursor(), FROM, TO);
    }

    private Cursor getCursor() {
        return managedQuery(Uri.parse("content://com.novoda.imageloader.demo/" + getTableName()), null, null, null, null);
    }

    protected void initButtons() {
        Button button = (Button) this.findViewById(R.id.refresh_button);
        button.setOnClickListener(this);
        button = (Button) this.findViewById(R.id.null_tag_button);
        button.setOnClickListener(this);
        button = (Button) this.findViewById(R.id.null_url_button);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.refresh_button:
                null_tag = false;
                null_url = false;
                refreshData();
                break;
            case R.id.null_tag_button:
                null_tag = !null_tag;
                refreshData();
                break;
            case R.id.null_url_button:
                null_url = !null_url;
                refreshData();
                break;
        }
    }

    protected void setAnimationFromIntent(ImageTagFactory imageTagFactory) {
        if (getIntent().hasExtra("animated")) {
            imageTagFactory.setAnimation(getIntent().getIntExtra("animated", AnimationHelper.ANIMATION_DISABLED));
        }
    }

    protected void refreshData(){
        adapter.notifyDataSetChanged();
    }

    protected ImageTag buildTagWithButtonOptions(ImageTagFactory imageTagFactory, String url) {
        if (null_tag) {
            return null;
        }
        if (null_url) {
            return imageTagFactory.build(null, this);
        }
        return imageTagFactory.build(url, this);
    }

}
