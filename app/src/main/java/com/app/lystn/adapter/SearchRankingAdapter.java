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
import com.app.lystn.activity.HomeActivity;
import com.app.lystn.fragment.home.SearchFragment;
import com.app.lystn.pojo.home.HomeContentPOJO;
import com.app.lystn.util.StringUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchRankingAdapter extends RecyclerView.Adapter<SearchRankingAdapter.ViewHolder> {

    private List<HomeContentPOJO> items;
    Activity activity;
    Fragment fragment;


    public SearchRankingAdapter(Activity activity, Fragment fragment, List<HomeContentPOJO> items) {
        this.items = items;
        this.activity = activity;
        this.fragment = fragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_search_ranking_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        Glide.with(activity)
                .load(items.get(position).getImgIrl())
                .placeholder(R.drawable.ll_disc)
                .error(R.drawable.ll_disc)
                .dontAnimate()
                .into(holder.iv_search);

        holder.tv_search.setText(items.get(position).getConName());

        holder.tv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragment instanceof SearchFragment) {
                    SearchFragment searchFragment = (SearchFragment) fragment;
                    switch (items.get(position).getSearch_type()) {
                        case StringUtils.SEARCH_ARTISTE_CONTENT:
                            if (activity instanceof HomeActivity) {
                                HomeActivity homeActivity = (HomeActivity) activity;
                                homeActivity.showArtisteDetail(items.get(position).getConId());
                            }
                            break;
                        case StringUtils.SEARCH_EPISODE_CONTENT:
                            searchFragment.getEpisodeDetails(items, items.get(position).getConId(), position);
                            break;
                        case StringUtils.SEARCH_PODCAST_CONTENT:
                            if (activity instanceof HomeActivity) {
                                HomeActivity homeActivity = (HomeActivity) activity;
                                homeActivity.startPodcastFragment(items.get(position).getConId());
                            }
                            break;
                        case StringUtils.SEARCH_RADIOS_CONTENT:
                            if (activity instanceof HomeActivity) {
                                HomeActivity homeActivity = (HomeActivity) activity;
                                homeActivity.playAudio(items, position, "radio", null);
                            }
                            break;
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

        @BindView(R.id.iv_search)
        ImageView iv_search;
        @BindView(R.id.tv_search)
        TextView tv_search;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}