package com.app.lystn.adapter.search;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.app.lystn.R;
import com.app.lystn.activity.HomeActivity;
import com.app.lystn.fragment.search.SearchEpisodesFragment;
import com.app.lystn.pojo.artiste.PodcastEpisodeDetailsPOJO;
import com.app.lystn.util.ItemClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchPodcastEpisodeAdapter extends RecyclerView.Adapter<SearchPodcastEpisodeAdapter.ViewHolder> {

    private List<PodcastEpisodeDetailsPOJO> items;
    Activity activity;
    Fragment fragment;
    ItemClickListener itemClickListener;

    public SearchPodcastEpisodeAdapter(Activity activity, Fragment fragment, List<PodcastEpisodeDetailsPOJO> items) {
        this.items = items;
        this.activity = activity;
        this.fragment = fragment;
    }

    public void setItemAdapter(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_search_podcast_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        Glide.with(activity)
                .load(items.get(position).getImgLocalUri())
                .placeholder(R.drawable.ll_disc)
                .error(R.drawable.ll_disc)
                .dontAnimate()
                .into(holder.iv_podcast);

        holder.tv_title.setText(items.get(position).getTitle());
        holder.tv_description.setText(items.get(position).getDescription());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity instanceof HomeActivity) {
                    HomeActivity homeActivity = (HomeActivity) activity;
                    homeActivity.showPlayListFragment();
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.itemClicked(position);
                } else {
                    if (fragment != null && fragment instanceof SearchEpisodesFragment) {
                        SearchEpisodesFragment searchEpisodesFragment = (SearchEpisodesFragment) fragment;
                        searchEpisodesFragment.getEpisodeDetails(items.get(position).getEpisodeId(), position);
                    }
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

        @BindView(R.id.iv_podcast)
        ImageView iv_podcast;
        @BindView(R.id.tv_title)
        TextView tv_title;
        @BindView(R.id.tv_description)
        TextView tv_description;
        @BindView(R.id.ll_content)
        LinearLayout ll_content;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}