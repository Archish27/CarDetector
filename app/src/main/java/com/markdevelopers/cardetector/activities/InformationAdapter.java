package com.markdevelopers.cardetector.activities;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.markdevelopers.cardetector.R;
import com.markdevelopers.cardetector.common.Config;
import com.markdevelopers.cardetector.data.remote.models.Information;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * Created by Archish on 12/19/2016.
 */

public class InformationAdapter extends RecyclerView.Adapter<InformationAdapter.InfoViewHolder> {

    ArrayList<Information> data;
    private LikeItemUpdateListener commander;

    public InformationAdapter(ArrayList<Information> data, LikeItemUpdateListener commander) {
        this.data = data;
        this.commander = commander;
    }

    public interface LikeItemUpdateListener {

        void onItemCardClicked(Information information);
    }

    @Override
    public InformationAdapter.InfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_information, parent, false);
        InfoViewHolder holder = new InfoViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(final InformationAdapter.InfoViewHolder holder, final int position) {
        holder.tvName.setText(data.get(position).getName());
        holder.tvDescription.setText(data.get(position).getData());
        holder.tvLocation.setText(data.get(position).getState());
        holder.tvTime.setText(data.get(position).getTime());
        Picasso.with(holder.ivImage.getContext()).load(Config.BASE_URL + data.get(position).getImage()).centerCrop().resize(256,256).into(holder.ivImage, new Callback() {
            @Override
            public void onSuccess() {
                holder.pgBar.setVisibility(View.GONE);
                holder.ivImage.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError() {

            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class InfoViewHolder extends RecyclerView.ViewHolder {
        ProgressBar pgBar;
        TextView tvDescription, tvName, tvLocation, tvTime;
        ImageView ivImage;
        LinearLayout cdNewsFeed;


        public InfoViewHolder(final View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            pgBar = (ProgressBar) itemView.findViewById(R.id.pgBar);
            tvDescription = (TextView) itemView.findViewById(R.id.tvData);
            tvLocation = (TextView) itemView.findViewById(R.id.tvState);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
            ivImage = (ImageView) itemView.findViewById(R.id.ivImage);
            cdNewsFeed = (LinearLayout) itemView.findViewById(R.id.cdNewsFeed);
            cdNewsFeed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (commander != null) {
                        commander.onItemCardClicked(data.get(getAdapterPosition()));
                    }
                }
            });
        }

    }


}