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

public class HomeCircleAdapter extends RecyclerView.Adapter<HomeCircleAdapter.ViewHolder> {

    private List<HomeContentPOJO> items;
    Activity activity;
    Fragment fragment;
    String type;


    public HomeCircleAdapter(Activity activity, Fragment fragment, List<HomeContentPOJO> items,String type) {
        this.items = items;
        this.activity = activity;
        this.fragment = fragment;
        this.type=type;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_home_circle_item, parent, false);
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

        holder.tv_name.setText(items.get(position).getConName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(activity instanceof HomeActivity){
                    if(type.equalsIgnoreCase("artiste")){
                        HomeActivity homeActivity= (HomeActivity) activity;
                        homeActivity.showArtisteDetail(items.get(position).getConId());
                    }else{
                        HomeActivity homeActivity= (HomeActivity) activity;
                        homeActivity.showPlayListFragment();
                    }
                }
            }
        });



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(activity instanceof HomeActivity){
//                    HomeActivity homeActivity= (HomeActivity) activity;
//                    homeActivity.showPlayListFragment();
//                }

                if(type.equalsIgnoreCase("genre")){
                    if(activity instanceof HomeActivity){
                        HomeActivity homeActivity= (HomeActivity) activity;
                        homeActivity.startGenreDetailFragment(items.get(position).getConId());
                    }
                }else if(type.equalsIgnoreCase("radio")){
                    if(activity instanceof HomeActivity){
                        HomeActivity homeActivity= (HomeActivity) activity;
//                        homeActivity.startFragment(R.id.frame_main,new MusicPlayerFragment(items.get(position)));
                        homeActivity.playAudio(items,position,"radio",null);
                    }
                }else if(type.equalsIgnoreCase("artiste")){
                    if(activity instanceof HomeActivity){
                        HomeActivity homeActivity= (HomeActivity) activity;
                        homeActivity.showArtisteDetail(items.get(position).getConId());
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

        @BindView(R.id.iv_item)
        ImageView iv_item;
        @BindView(R.id.tv_name)
        TextView tv_name;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}