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

import com.bumptech.glide.Glide;
import com.app.lystn.R;
import com.app.lystn.activity.HomeActivity;
import com.app.lystn.pojo.CategoryPodcastPOJO;
import com.app.lystn.util.Pref;
import com.app.lystn.util.StringUtils;
import com.app.lystn.util.TagUtils;
import com.app.lystn.util.ToastClass;
import com.app.lystn.webservice.ApiCallBase;
import com.app.lystn.webservice.WebServicesCallBack;
import com.app.lystn.webservice.WebServicesUrls;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryPodcastAdapter extends RecyclerView.Adapter<CategoryPodcastAdapter.ViewHolder> {

    private List<CategoryPodcastPOJO> items;
    Activity activity;
    Fragment fragment;
    String type;


    public CategoryPodcastAdapter(Activity activity, Fragment fragment, List<CategoryPodcastPOJO> items, String type) {
        this.items = items;
        this.activity = activity;
        this.fragment = fragment;
        this.type=type;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_all_genre_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        Glide.with(activity)
                .load(items.get(position).getImgIrl())
                .placeholder(R.drawable.ll_square)
                .error(R.drawable.ll_square)
                .dontAnimate()
                .into(holder.iv_genre);

        holder.tv_genre.setText(items.get(position).getTitle());

        if (activity instanceof HomeActivity) {
            HomeActivity homeActivity = (HomeActivity) activity;
            if (homeActivity.getGenreFollowUpList().contains(items.get(position).getPodcastId())) {
                holder.tv_follow.setText("unfollow");
            } else {
                holder.tv_follow.setText("follow");
            }
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TagUtils.getTag(),"type:-"+type);
                if (type.equalsIgnoreCase("genre")) {
                    if (activity instanceof HomeActivity) {
                        HomeActivity homeActivity = (HomeActivity) activity;
                        homeActivity.startGenreDetailFragment(items.get(position).getPodcastId());
                    }
                } else if (type.equalsIgnoreCase("radio")) {
                    if (activity instanceof HomeActivity) {
//                        HomeActivity homeActivity = (HomeActivity) activity;
//                        homeActivity.startFragment(R.id.frame_main, new MusicPlayerFragment(items.get(position)));
                    }
                } else if (type.equalsIgnoreCase("artiste")) {
                    if (activity instanceof HomeActivity) {
                        HomeActivity homeActivity = (HomeActivity) activity;
                        homeActivity.showArtisteDetail(items.get(position).getPodcastId());
                    }
                }
            }
        });

        holder.ll_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(activity instanceof HomeActivity){
                    HomeActivity homeActivity= (HomeActivity) activity;
                    if(homeActivity.getGenreFollowUpList().contains(items.get(position).getPodcastId())){
                        homeActivity.showProgressBar();
                        JSONObject jsonObject = new JSONObject();

                        try {
                            jsonObject.put("userId", Pref.GetStringPref(activity.getApplicationContext(), StringUtils.USER_ID, ""));
                            jsonObject.put("deviceId", "91");
                            jsonObject.put("followType", "genre");
                            jsonObject.put("followId", items.get(position).getPodcastId());
                            jsonObject.put("langCode", "en");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        new ApiCallBase(activity, new WebServicesCallBack() {
                            @Override
                            public void onGetMsg(String apicall, String response) {
                                homeActivity.dismissProgressBar();
                                try {
                                    String object = new String(response);
                                    JSONObject jsonObject = new JSONObject(object);
                                    JSONObject responseObject = jsonObject.optJSONObject("response");
//                                    ArtisteFollowUtil.removeArtisteFromList(activity.getApplicationContext(),String.valueOf(items.get(position).getConId()));
                                    homeActivity.getGenreFollowUpList().remove(items.get(position).getPodcastId());
//                                    Log.d(TagUtils.getTag(),"artiste list:-"+ArtisteFollowUtil.getArtisteList(activity.getApplicationContext()));

                                    notifyDataSetChanged();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onErrorMsg(String status_code, String response) {
                                ToastClass.showShortToast(activity.getApplicationContext(),"Server Down");
                                homeActivity.dismissProgressBar();
                            }
                        }, "UNFOLLOW_API").makeApiCall(WebServicesUrls.UNFOLLOW_API, jsonObject);
                    }else{
                        JSONObject jsonObject = new JSONObject();
                        homeActivity.showProgressBar();
                        try {
                            jsonObject.put("userId", Pref.GetStringPref(activity.getApplicationContext(), StringUtils.USER_ID, ""));
                            jsonObject.put("deviceId", "91");
                            jsonObject.put("followType", "genre");
                            jsonObject.put("followId", items.get(position).getPodcastId());
                            jsonObject.put("langCode", "en");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        new ApiCallBase(activity, new WebServicesCallBack() {
                            @Override
                            public void onGetMsg(String apicall, String response) {
                                homeActivity.dismissProgressBar();
                                try {
                                    String object = new String(response);
                                    JSONObject jsonObject = new JSONObject(object);
                                    JSONObject responseObject = jsonObject.optJSONObject("response");
//                                    ArtisteFollowUtil.addFollowArtiste(activity.getApplicationContext(), String.valueOf(items.get(position).getConId()));
//                                    Log.d(TagUtils.getTag(), jsonObject.toString());
//                                    Log.d(TagUtils.getTag(),"artiste list:-"+ArtisteFollowUtil.getArtisteList(activity.getApplicationContext()));

                                    homeActivity.getGenreFollowUpList().add(items.get(position).getPodcastId());

                                    notifyDataSetChanged();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onErrorMsg(String status_code, String response) {
                                ToastClass.showShortToast(activity.getApplicationContext(),"Server Down");
                                homeActivity.dismissProgressBar();
                            }
                        }, "FOLLOW_API").makeApiCall(WebServicesUrls.FOLLOW_API, jsonObject);
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

        @BindView(R.id.iv_genre)
        ImageView iv_genre;
        @BindView(R.id.tv_genre)
        TextView tv_genre;
        @BindView(R.id.ll_follow)
        LinearLayout ll_follow;
        @BindView(R.id.tv_follow)
        TextView tv_follow;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}