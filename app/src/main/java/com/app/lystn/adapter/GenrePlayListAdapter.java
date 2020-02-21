package com.app.lystn.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.app.lystn.R;
import com.app.lystn.activity.HomeActivity;
import com.app.lystn.fragment.playlist.PodcastPlayListFragment;
import com.app.lystn.pojo.artiste.PodcastDetailPOJO;
import com.app.lystn.pojo.artiste.PodcastEpisodeDetailsPOJO;
import com.app.lystn.util.TagUtils;
import com.app.lystn.util.UtilityFunction;
import com.app.lystn.webservice.DownloadCallback;
import com.app.lystn.webservice.DownloadSongManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GenrePlayListAdapter extends RecyclerView.Adapter<GenrePlayListAdapter.ViewHolder> {

    private List<PodcastEpisodeDetailsPOJO> items;
    Activity activity;
    Fragment fragment;


    public GenrePlayListAdapter(Activity activity, Fragment fragment, List<PodcastEpisodeDetailsPOJO> items) {
        setHasStableIds(true);
        this.items = items;
        this.activity = activity;
        this.fragment = fragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_genre_playlist_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.tv_episode_name.setText(items.get(position).getTitle());
        holder.tv_date.setText(UtilityFunction.parseDT(items.get(position).getAddedOn()));
        holder.tv_duration.setText(String.valueOf(UtilityFunction.convertSecToHHMMSS(items.get(position).getDuration())));
        holder.tv_sequence.setText(String.valueOf(UtilityFunction.coolFormat(((double) items.get(position).getPlaybackCount()), 0)));
//        Glide.with(activity)
//                .load(items.get(position).getImgLocalUri())
//                .placeholder(R.drawable.ll_disc)
//                .error(R.drawable.ll_disc)
//                .dontAnimate()
//                .into(holder.iv_episode);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragment != null && fragment instanceof PodcastPlayListFragment) {
                    PodcastPlayListFragment podcastPlayListFragment = (PodcastPlayListFragment) fragment;
                    podcastPlayListFragment.playAudio(position);
                }

            }
        });
        if (activity instanceof HomeActivity) {
            HomeActivity homeActivity = (HomeActivity) activity;
            if (homeActivity.getDbManager().checkSongInDB(items.get(position).getEpisodeId())) {
                items.get(position).setDownloaded(true);
                holder.iv_download.setVisibility(View.GONE);
            } else {
                items.get(position).setDownloaded(false);
                holder.iv_download.setVisibility(View.VISIBLE);
            }
        }

        holder.ll_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.iv_download.callOnClick();
            }
        });

        holder.iv_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if()
                if (activity instanceof HomeActivity) {

                    PodcastDetailPOJO podcastDetailPOJO = null;

                    if (fragment instanceof PodcastPlayListFragment) {
                        PodcastPlayListFragment podcastPlayListFragment = (PodcastPlayListFragment) fragment;
                        podcastDetailPOJO = podcastPlayListFragment.getPodcastDetailPOJO();
                    }

                    HomeActivity homeActivity = (HomeActivity) activity;
                    Log.d(TagUtils.getTag(), "download complete:-" + items.get(position).getEpisodeId());
                    holder.iv_download.setVisibility(View.GONE);
                    new DownloadSongManager(activity.getApplicationContext(),
                            homeActivity.getDbManager(),
                            new DownloadCallback() {
                                @Override
                                public void onSuccessDownload(String podcast_id) {
                                    Log.d(TagUtils.getTag(), "download complete:-" + podcast_id);
                                    notifyDataSetChanged();
                                }

                                @Override
                                public void onDownloadFailed(String error) {
                                    Log.d(TagUtils.getTag(), "download failed");
                                    holder.iv_download.setVisibility(View.VISIBLE);
                                }
                            }).downloadSong(items.get(position), podcastDetailPOJO);
                }

//                if (activity instanceof HomeActivity) {
//                    HomeActivity homeActivity = (HomeActivity) activity;
////                    homeActivity.downloadSong(items.get(position).getEpisodeId(), items.get(position).getStreamUri());
//                    homeActivity.downloadSong(items.get(position));
//                }
//                holder.iv_download.setVisibility(View.GONE);
            }
        });

        holder.itemView.setTag(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        //        @BindView(R.id.iv_episode)
//        ImageView iv_episode;
        @BindView(R.id.tv_episode_name)
        TextView tv_episode_name;
        @BindView(R.id.tv_date)
        TextView tv_date;
        @BindView(R.id.tv_duration)
        TextView tv_duration;
        @BindView(R.id.tv_sequence)
        TextView tv_sequence;
        @BindView(R.id.iv_download)
        ImageView iv_download;
        @BindView(R.id.ll_download)
        LinearLayout ll_download;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}