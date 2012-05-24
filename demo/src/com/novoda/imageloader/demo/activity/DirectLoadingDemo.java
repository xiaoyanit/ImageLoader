package com.novoda.imageloader.demo.activity;

import com.novoda.imageloader.core.util.DirectLoader;
import com.novoda.imageloader.demo.R;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

public class DirectLoadingDemo extends Activity {
    
    private ImageView imageView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.direct_loading);
        imageView = (ImageView)findViewById(R.id.direct_image);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        
        //Don't use this way this is just for testing
        new Thread() {
            public void run() {
                DirectLoader dl = new DirectLoader();
                Log.v("XXXX" , "directLoader resumed");
                Bitmap b = dl.download("http://www.asianweek.com/wp-content/uploads/2012/03/microsoft_logo11.jpg");
                Log.v("XXXX" , "bitmap created and isNull? " + (b == null));
                Log.v("XXXX" , "image setted");
                setImageView(b);
            };
        }.start();
    }
    
    public void setImageView(final Bitmap bitmap) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                imageView.setImageBitmap(bitmap);
            }
        });
    }

}
