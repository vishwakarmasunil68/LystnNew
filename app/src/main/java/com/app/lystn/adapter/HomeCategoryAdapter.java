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
import com.app.lystn.pojo.home.HomeContentPOJO;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeCategoryAdapter extends RecyclerView.Adapter<HomeCategoryAdapter.ViewHolder> {

    private List<HomeContentPOJO> items;
    Activity activity;
    Fragment fragment;


    public HomeCategoryAdapter(Activity activity, Fragment fragment, List<HomeContentPOJO> items) {
        this.items = items;
        this.activity = activity;
        this.fragment = fragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_home_category_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        Glide.with(activity)
                .load(items.get(position).getImgIrl())
                .placeholder(R.drawable.ll_disc)
                .error(R.drawable.ll_disc)
                .dontAnimate()
                .into(holder.iv_item);

        holder.tv_cat.setText(items.get(position).getConName());

        holder.itemView.setTag(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_item)
        ImageView iv_item;
        @BindView(R.id.tv_cat)
        TextView tv_cat;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}