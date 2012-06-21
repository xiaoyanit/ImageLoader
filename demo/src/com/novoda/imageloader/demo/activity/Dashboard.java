package com.novoda.imageloader.demo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.novoda.imageloader.demo.R;

public class Dashboard extends Activity implements OnClickListener {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboard);
		((Button)findViewById(R.id.long_image_list)).setOnClickListener(this);
        ((Button)findViewById(R.id.long_small_image_list)).setOnClickListener(this);
        ((Button)findViewById(R.id.from_cache_only)).setOnClickListener(this);
        ((Button)findViewById(R.id.big_images)).setOnClickListener(this);
        ((Button)findViewById(R.id.direct_loader)).setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		switch (id) {
		case R.id.long_image_list:
			startActivity(new Intent(Dashboard.this, ImageLongList.class));			
			break;
		case R.id.long_small_image_list:
			startActivity(new Intent(Dashboard.this, LongSmallImageList.class));			
			break;
		case R.id.from_cache_only:
			startActivity(new Intent(Dashboard.this, FromCacheOnly.class));			
			break;
		case R.id.big_images:
			startActivity(new Intent(Dashboard.this, BigImages.class));			
			break;
        case R.id.direct_loader:
            startActivity(new Intent(Dashboard.this, DirectLoading.class));
            break;
		}
	}
	
}
