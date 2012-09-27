package com.novoda.imageloader.demo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.bugsense.trace.BugSenseHandler;
import com.novoda.imageloader.demo.R;

/**
 * Nothing really interesting here just a dashboard.
 */
public class Demos extends Activity implements OnItemClickListener {

    private static final String API_KEY = "api key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BugSenseHandler.initAndStartSession(this, API_KEY);
        setContentView(R.layout.demos);
        ListView entries = (ListView) findViewById(R.id.demo_list);
        entries.setOnItemClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BugSenseHandler.closeSession(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
        case 0:
            startActivity(new Intent(Demos.this, ImageLongList.class));
            break;
        case 1:
            startActivity(new Intent(Demos.this, LongSmallImageList.class));
            break;
        case 2:
            startActivity(new Intent(Demos.this, BigImages.class));
            break;
        case 3:
            startActivity(new Intent(Demos.this, DirectLoading.class));
        }
    }

}
