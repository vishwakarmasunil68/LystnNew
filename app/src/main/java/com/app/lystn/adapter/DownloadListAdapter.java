package com.app.lystn.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.app.lystn.R;
import com.app.lystn.activity.HomeActivity;
import com.app.lystn.fragment.download.DownloadListFragment;
import com.app.lystn.pojo.home.HomeContentPOJO;
import com.app.lystn.util.Pref;
import com.app.lystn.util.StringUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DownloadListAdapter extends RecyclerView.Adapter<DownloadListAdapter.ViewHolder> {

    private List<HomeContentPOJO> items;
    Activity activity;
    Fragment fragment;
    boolean show_checks = false;


    public DownloadListAdapter(Activity activity, Fragment fragment, List<HomeContentPOJO> items) {
        this.items = items;
        this.activity = activity;
        this.fragment = fragment;
        setHasStableIds(true);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_download_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        Glide.with(activity)
                .load(items.get(position).getImgIrl())
                .placeholder(R.drawable.ll_square)
                .error(R.drawable.ll_square)
                .dontAnimate()
                .into(holder.iv_podcast);

        holder.tv_title.setText(items.get(position).getConName());
        holder.tv_description.setText(items.get(position).getDescription());

        holder.ll_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity instanceof HomeActivity) {
                    HomeActivity homeActivity = (HomeActivity) activity;
                    Pref.SetStringPref(activity.getApplicationContext(), StringUtils.NOTIFICAION_ALBUM_NAME, items.get(position).getConName());
                    homeActivity.playAudio(items, position, "download", null);
                }
            }
        });

        if (show_checks) {
            holder.ll_check.setVisibility(View.VISIBLE);
            if (items.get(position).isChecked()) {
                holder.iv_check.setImageResource(R.drawable.ic_check_circle);
            } else {
                holder.iv_check.setImageResource(R.drawable.ic_uncheck_circle);
            }
        } else {
            holder.ll_check.setVisibility(View.GONE);
        }

        holder.ll_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                items.get(position).setChecked(!items.get(position).isChecked());
                notifyDataSetChanged();
                boolean is_checked = false;
                for (HomeContentPOJO homeContentPOJO : items) {
                    if (homeContentPOJO.isChecked()) {
                        is_checked = true;
                    }
                }

                if (fragment instanceof DownloadListFragment) {
                    DownloadListFragment downloadListFragment = (DownloadListFragment) fragment;
                    downloadListFragment.deleteSong(is_checked);
                }

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity instanceof HomeActivity) {
                    HomeActivity homeActivity = (HomeActivity) activity;
                    Pref.SetStringPref(activity.getApplicationContext(), StringUtils.NOTIFICAION_ALBUM_NAME, items.get(position).getConName());
                    homeActivity.playAudio(items, position, "download", null);
                }
            }
        });

//        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                show_checks = true;
//                notifyDataSetChanged();
//                holder.ll_check.callOnClick();
//                return false;
//            }
//        });

        holder.frame_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_checks = true;
                notifyDataSetChanged();
                holder.ll_check.callOnClick();
                holder.swipeReveal.close(true);
            }
        });

        holder.ll_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity instanceof HomeActivity) {
                    HomeActivity homeActivity = (HomeActivity) activity;
                    Pref.SetStringPref(activity.getApplicationContext(), StringUtils.NOTIFICAION_ALBUM_NAME, items.get(position).getConName());
                    homeActivity.playAudio(items, position, "download", items.get(position).getPodcastDetailPOJO());
                }
            }
        });

        holder.ll_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity instanceof HomeActivity) {
                    HomeActivity homeActivity = (HomeActivity) activity;
                    Pref.SetStringPref(activity.getApplicationContext(), StringUtils.NOTIFICAION_ALBUM_NAME, items.get(position).getConName());
                    homeActivity.playAudio(items, position, "download", items.get(position).getPodcastDetailPOJO());
                }
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

        @BindView(R.id.iv_podcast)
        ImageView iv_podcast;
        @BindView(R.id.tv_title)
        TextView tv_title;
        @BindView(R.id.tv_description)
        TextView tv_description;
        @BindView(R.id.ll_check)
        LinearLayout ll_check;
        @BindView(R.id.iv_check)
        ImageView iv_check;
        @BindView(R.id.iv_play)
        ImageView iv_play;
        @BindView(R.id.ll_play)
        LinearLayout ll_play;
        @BindView(R.id.tv_delete)
        TextView tv_delete;
        @BindView(R.id.frame_delete)
        FrameLayout frame_delete;
        @BindView(R.id.swipeReveal)
        SwipeRevealLayout swipeReveal;
        @BindView(R.id.ll_image)
        LinearLayout ll_image;
        @BindView(R.id.ll_content)
        LinearLayout ll_content;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}