package com.app.lystn.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.app.lystn.R;
import com.app.lystn.activity.HomeActivity;
import com.app.lystn.fragment.home.MusicPlayerFragment;
import com.app.lystn.pojo.home.HomeContentPOJO;
import com.app.lystn.util.Pref;
import com.app.lystn.util.StringUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QueueAdapter extends RecyclerView.Adapter<QueueAdapter.ViewHolder> {

    private List<HomeContentPOJO> items;
    Activity activity;
    Fragment fragment;


    public QueueAdapter(Activity activity, Fragment fragment, List<HomeContentPOJO> items) {
        this.items = items;
        this.activity = activity;
        this.fragment = fragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_queue_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        Glide.with(activity)
                .load(items.get(position).getImgIrl())
                .placeholder(R.drawable.ll_square)
                .error(R.drawable.ll_square)
                .dontAnimate()
                .into(holder.iv_radio);

        holder.tv_title.setText(items.get(position).getConName().trim());

        if(Pref.GetStringPref(activity.getApplicationContext(), StringUtils.MEDIA_TYPE, "").equalsIgnoreCase("radio")){
            holder.iv_download.setVisibility(View.GONE);
        }else{
//            holder.iv_download.setVisibility(View.VISIBLE);
            if (activity instanceof HomeActivity) {
                HomeActivity homeActivity = (HomeActivity) activity;
                if (homeActivity.getDbManager().checkSongInDB(items.get(position).getConId())) {
                    holder.iv_download.setVisibility(View.GONE);
                } else {
                    holder.iv_download.setVisibility(View.VISIBLE);
                }
            }
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (activity instanceof HomeActivity) {
//                    HomeActivity homeActivity = (HomeActivity) activity;
//                    homeActivity.playQueueSelectedSong(position);
//                }
                if (fragment != null && fragment instanceof MusicPlayerFragment) {
                    MusicPlayerFragment musicPlayerFragment = (MusicPlayerFragment) fragment;
                    musicPlayerFragment.playQueueSelectedSong(position);
                }
            }
        });



        holder.iv_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity instanceof HomeActivity) {
                    HomeActivity homeActivity = (HomeActivity) activity;
//                    homeActivity.downloadSong(items.get(position).getEpisodeId(), items.get(position).getStreamUri());
                    homeActivity.downloadSong(items.get(position));
                }
                holder.iv_download.setVisibility(View.GONE);
            }
        });

        if (items.get(position).isPlaying()) {
            holder.iv_playpause.setImageResource(R.drawable.ic_white_pause);
            holder.tv_title.setTextColor(Color.parseColor("#FFA100"));
        } else {
            holder.iv_playpause.setImageResource(R.drawable.ic_white_play);
            holder.tv_title.setTextColor(Color.parseColor("#000000"));
        }

//        holder.tv_description.setText(items.get(position).get);

        holder.itemView.setTag(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_radio)
        ImageView iv_radio;
        @BindView(R.id.tv_title)
        TextView tv_title;
        @BindView(R.id.tv_description)
        TextView tv_description;
        @BindView(R.id.iv_download)
        ImageView iv_download;
        @BindView(R.id.frame_radio)
        FrameLayout frame_radio;
        @BindView(R.id.iv_playpause)
        ImageView iv_playpause;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}