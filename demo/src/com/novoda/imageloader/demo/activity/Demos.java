package com.novoda.imageloader.demo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.novoda.imageloader.demo.R;

/**
 * Nothing really interesting here just a dashboard.
 */
public class Demos extends Activity implements OnItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demos);
        ListView entries = (ListView) findViewById(R.id.demo_list);
        entries.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
        case 0:
            startActivity(new Intent(Demos.this, ImageLongList.class));
            break;
        case 1:
            startActivity(new Intent(Demos.this, ImageLongList.class).putExtra("animated", true));
            break;
        case 2:
            startActivity(new Intent(Demos.this, LongSmallImageList.class));
            break;
        case 3:
            startActivity(new Intent(Demos.this, LongSmallImageList.class).putExtra("animated", true));
            break;
        case 4:
            startActivity(new Intent(Demos.this, BigImages.class));
            break;
        case 5:
            startActivity(new Intent(Demos.this, BigImages.class).putExtra("animated", true));
            break;
        case 6:
            startActivity(new Intent(Demos.this, DirectLoading.class));
            break;
        case 7:
            startActivity(new Intent(Demos.this, DirectLoading.class).putExtra("animated", true));
            break;
        }
    }

}
