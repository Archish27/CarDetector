package com.markdevelopers.cardetector.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.markdevelopers.cardetector.R;
import com.markdevelopers.cardetector.common.BaseActivity;
import com.markdevelopers.cardetector.data.remote.models.Information;
import com.markdevelopers.cardetector.data.remote.models.InformationWrapper;

import java.util.ArrayList;

/**
 * Created by Archish on 3/26/2017.
 */

public class InformationActivity extends BaseActivity implements InformationContract.InformationView, InformationAdapter.LikeItemUpdateListener {
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView rvInfo;
    InformationPresenter informationPresenter;
    FrameLayout flLoad;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        initViews();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    private void initViews() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        rvInfo = (RecyclerView) findViewById(R.id.rvInfo);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.rvRefresh);
        flLoad = (FrameLayout) findViewById(R.id.flLoad);
        informationPresenter = new InformationPresenter(this);
        rvInfo.setHasFixedSize(true);
        rvInfo.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                informationPresenter.getData();
            }
        });
        informationPresenter.getData();
    }

    @Override
    public void onResponse(InformationWrapper informationWrapper) {
        if (swipeRefreshLayout.isRefreshing())
            swipeRefreshLayout.setRefreshing(false);
        ArrayList<Information> informations = new ArrayList<>();
        for (int i = 0; i < informationWrapper.data.size(); i++) {
            informations.add(new Information(informationWrapper.data.get(i).getData()
                    , informationWrapper.data.get(i).getState()
                    , informationWrapper.data.get(i).getName()
                    , informationWrapper.data.get(i).getImage()
                    , informationWrapper.data.get(i).getTime()));
        }
        InformationAdapter informationAdapter = new InformationAdapter(informations, this);
        rvInfo.setAdapter(informationAdapter);
        flLoad.setVisibility(View.GONE);
        swipeRefreshLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemCardClicked(Information information) {
        Intent i = new Intent(InformationActivity.this, ImageViewActivity.class);
        i.putExtra("Information", information);
        startActivity(i);
    }

    @Override
    public void onNetworkException(Throwable e) {
        super.onNetworkException(e);
    }
}
