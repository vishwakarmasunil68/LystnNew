package com.app.lystn.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.app.lystn.R;
import com.app.lystn.fragment.home.ArtisteDetailFragment;
import com.app.lystn.pojo.artiste.PodcastEpisodeDetailsPOJO;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PodcastEpisodeAdapter extends RecyclerView.Adapter<PodcastEpisodeAdapter.ViewHolder> {

    private List<PodcastEpisodeDetailsPOJO> items;
    Activity activity;
    Fragment fragment;


    public PodcastEpisodeAdapter(Activity activity, Fragment fragment, List<PodcastEpisodeDetailsPOJO> items) {
        this.items = items;
        this.activity = activity;
        this.fragment = fragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_podcast_episode_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.tv_episode_name.setText(items.get(position).getTitle());
        Glide.with(activity)
                .load(items.get(position).getImgLocalUri())
                .placeholder(R.drawable.ll_disc)
                .error(R.drawable.ll_disc)
                .dontAnimate()
                .into(holder.iv_episode);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fragment !=null && fragment instanceof ArtisteDetailFragment){
                    ArtisteDetailFragment artisteDetailFragment= (ArtisteDetailFragment) fragment;
                    artisteDetailFragment.playEpisode(position);
                }
            }
        });

        holder.itemView.setTag(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_episode)
        ImageView iv_episode;
        @BindView(R.id.tv_episode_name)
        TextView tv_episode_name;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}