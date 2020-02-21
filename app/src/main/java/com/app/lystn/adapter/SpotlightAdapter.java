package com.app.lystn.adapter;

import android.app.Activity;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.app.lystn.activity.HomeActivity;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou;
import com.squareup.picasso.Picasso;
import com.app.lystn.R;
import com.app.lystn.pojo.home.HomeContentPOJO;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SpotlightAdapter extends RecyclerView.Adapter<SpotlightAdapter.ViewHolder> {

    private List<HomeContentPOJO> items;
    Activity activity;
    Fragment fragment;


    public SpotlightAdapter(Activity activity, Fragment fragment, List<HomeContentPOJO> items) {
        this.items = items;
        this.activity = activity;
        this.fragment = fragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_spotlight_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

//        Glide.with(activity)
//                .load(items.get(position).getImgIrl())
//                .placeholder(R.drawable.ic_spotlight)
//                .error(R.drawable.ic_spotlight)
//                .skipMemoryCache(true)
//                .dontAnimate()
//                .into(holder.iv_item);

        if (items.get(position).getImgIrl().contains(".svg")) {
//            SvgLoader.pluck()
//                    .with(activity)
//                    .setPlaceHolder(R.drawable.ic_spotlight, R.drawable.ic_spotlight)
//                    .load(items.get(position).getImgIrl(), holder.iv_item);
//            GlideToVectorYou.justLoadImage(activity, Uri.parse(items.get(position).getImgIrl()), holder.iv_item);
            RequestBuilder<PictureDrawable> requestBuilder = GlideToVectorYou
                    .init()
                    .with(activity)
                    .getRequestBuilder();

            requestBuilder
                    .load(Uri.parse(items.get(position).getImgIrl()))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .apply(new RequestOptions()
                            .fitCenter())
                    .into(holder.iv_item);
        } else {
            Picasso.get().load(items.get(position).getImgIrl()).into(holder.iv_item);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    String url = items.get(position).getCotDeepLink().replaceAll("lystn://", "");
                    String type = url.split("/")[0];
                    if (type.equalsIgnoreCase("radio")) {
//                        String radio_id = url.split("/")[1];
//                        if (activity instanceof HomeActivity) {
//                            HomeActivity homeActivity = (HomeActivity) activity;
//                            homeActivity.playAudio(items, position, "radio",null);
//                        }
                    } else if (type.equalsIgnoreCase("genre")) {
                        String genre_id = url.split("/")[1];
                        if (activity instanceof HomeActivity) {
                            HomeActivity homeActivity = (HomeActivity) activity;
                            homeActivity.startGenreDetailFragment(genre_id);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
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

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}