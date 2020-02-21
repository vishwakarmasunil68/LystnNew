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
import com.app.lystn.pojo.home.HomeContentPOJO;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RadioAdapter extends RecyclerView.Adapter<RadioAdapter.ViewHolder> {

    private List<HomeContentPOJO> items;
    Activity activity;
    Fragment fragment;


    public RadioAdapter(Activity activity, Fragment fragment, List<HomeContentPOJO> items) {
        this.items = items;
        this.activity = activity;
        this.fragment = fragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_all_radio_item, parent, false);
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

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(activity instanceof HomeActivity){
                    HomeActivity homeActivity= (HomeActivity) activity;
                    homeActivity.playAudio(items,position,"radio",null);
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

        @BindView(R.id.iv_radio)
        ImageView iv_radio;
        @BindView(R.id.tv_title)
        TextView tv_title;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}