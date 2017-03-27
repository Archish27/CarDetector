package com.markdevelopers.cardetector.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.markdevelopers.cardetector.R;
import com.markdevelopers.cardetector.common.Config;
import com.markdevelopers.cardetector.data.remote.models.Information;
import com.squareup.picasso.Picasso;

import uk.co.senab.photoview.PhotoView;


/**
 * Created by Archish on 2/24/2017.
 */

public class ImageViewActivity extends AppCompatActivity {
    PhotoView ivImage;
    Information information;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imageview);
        Intent i = getIntent();
        information = i.getParcelableExtra("Information");
        initViews();
    }

    private void initViews() {
        ivImage = (PhotoView) findViewById(R.id.ivImage);
        Picasso.with(getApplicationContext()).load(Config.BASE_URL + information.getImage()).resize(512, 512).into(ivImage);
    }
}
