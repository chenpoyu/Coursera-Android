package com.test.poyuchen.dailyselfie;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

/**
 * Created by poyuchen on 17/4/12.
 */
public class ImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imageview_main);
        Bitmap myBitmap = BitmapFactory.decodeFile(getIntent().getStringExtra("imagePath"));
        ImageView myImage = (ImageView) findViewById(R.id.image_view);
        myImage.setImageBitmap(myBitmap);
    }


}
