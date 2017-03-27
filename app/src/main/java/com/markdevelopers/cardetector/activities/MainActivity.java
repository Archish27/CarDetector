package com.markdevelopers.cardetector.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.markdevelopers.cardetector.R;

/**
 * Created by Archish on 3/26/2017.
 */

public class MainActivity extends AppCompatActivity {
    Button bScan, bAbout,bAll;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        bScan = (Button) findViewById(R.id.bScan);
        bAll = (Button) findViewById(R.id.bAll);
        bAbout = (Button) findViewById(R.id.bAbout);
        bScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent k = new Intent(MainActivity.this, CarDetectorActivity.class);
                startActivity(k);
            }
        });
        bAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent k = new Intent(MainActivity.this, InformationActivity.class);
                startActivity(k);
            }
        });
    }

}
