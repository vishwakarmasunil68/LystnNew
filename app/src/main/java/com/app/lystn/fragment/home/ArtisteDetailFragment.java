package com.app.lystn.fragment.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.app.lystn.R;
import com.app.lystn.activity.HomeActivity;
import com.app.lystn.adapter.PodcastEpisodeAdapter;
import com.app.lystn.fragmentcontroller.FragmentController;
import com.app.lystn.pojo.artiste.PodcastDetailPOJO;
import com.app.lystn.pojo.artiste.PodcastEpisodeDetailsPOJO;
import com.app.lystn.pojo.artiste.PodcastPOJO;
import com.app.lystn.pojo.home.HomeContentPOJO;
import com.app.lystn.util.Pref;
import com.app.lystn.util.StringUtils;
import com.app.lystn.util.ToastClass;
import com.app.lystn.webservice.ApiCallBase;
import com.app.lystn.webservice.WebServicesCallBack;
import com.app.lystn.webservice.WebServicesUrls;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ArtisteDetailFragment extends FragmentController {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.iv_podcast_image)
    ImageView iv_podcast_image;
    @BindView(R.id.tv_podcast_name)
    TextView tv_podcast_name;
    @BindView(R.id.tv_podcast_description)
    TextView tv_podcast_description;
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.iv_favorited)
    ImageView iv_favorited;
    @BindView(R.id.tv_podcast_by)
    TextView tv_podcast_by;

    PodcastDetailPOJO podcastDetailPOJO;

    String conId;

    public ArtisteDetailFragment(String conId){
        this.conId=conId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_artiste_detail, container, false);
        setUpView(getActivity(), this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getPodcastDetails();

        attachGenericAdapter(recyclerView);

        changeFollowText();

        iv_favorited.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() instanceof HomeActivity) {
                    HomeActivity homeActivity = (HomeActivity) getActivity();
                    if (homeActivity.getArtisteFollowUpList().contains(conId)) {
                        homeActivity.showProgressBar();
                        JSONObject jsonObject = new JSONObject();

                        try {
                            jsonObject.put("userId", Pref.GetStringPref(getActivity().getApplicationContext(), StringUtils.USER_ID, ""));
                            jsonObject.put("deviceId", "91");
                            jsonObject.put("followType", "artiste");
                            jsonObject.put("followId", conId);
                            jsonObject.put("langCode", "en");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        new ApiCallBase(getActivity(), new WebServicesCallBack() {
                            @Override
                            public void onGetMsg(String apicall, String response) {
                                homeActivity.dismissProgressBar();
                                try {
                                    String object = new String(response);
                                    JSONObject jsonObject = new JSONObject(object);
                                    JSONObject responseObject = jsonObject.optJSONObject("response");
//                                    ArtisteFollowUtil.removeArtisteFromList(activity.getApplicationContext(),String.valueOf(items.get(position).getConId()));
//                                    homeActivity.getArtisteFollowUpList().remove(items.get(position).getConId());
                                    homeActivity.removeFollowUP(StringUtils.ARTISTE_FOLLOW_UP_STRING, conId);
//                                    Log.d(TagUtils.getTag(),"artiste list:-"+ArtisteFollowUtil.getArtisteList(activity.getApplicationContext()));

                                    changeFollowText();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onErrorMsg(String status_code, String response) {
                                ToastClass.showShortToast(getActivity().getApplicationContext(),"Server Down");
                                homeActivity.dismissProgressBar();
                            }
                        }, "UNFOLLOW_API").makeApiCall(WebServicesUrls.UNFOLLOW_API, jsonObject);
                    } else {
                        JSONObject jsonObject = new JSONObject();
                        homeActivity.showProgressBar();
                        try {
                            jsonObject.put("userId", Pref.GetStringPref(getActivity().getApplicationContext(), StringUtils.USER_ID, ""));
                            jsonObject.put("deviceId", "91");
                            jsonObject.put("followType", "artiste");
                            jsonObject.put("followId", conId);
                            jsonObject.put("langCode", "en");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        new ApiCallBase(getActivity(), new WebServicesCallBack() {
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

                                    homeActivity.addFollowUpData(StringUtils.ARTISTE_FOLLOW_UP_STRING, conId);

//                                    homeActivity.getArtisteFollowUpList().add(items.get(position).getConId());

                                    changeFollowText();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onErrorMsg(String status_code, String response) {
                                ToastClass.showShortToast(getActivity().getApplicationContext(),"Server Down");
                                homeActivity.dismissProgressBar();
                            }
                        }, "FOLLOW_API").makeApiCall(WebServicesUrls.FOLLOW_API, jsonObject);
                    }
                }
            }
        });
    }

    List<PodcastEpisodeDetailsPOJO> podcastEpisodeDetailsPOJOS = new ArrayList<>();
    List<HomeContentPOJO> homeContentPOJOS = new ArrayList<>();
    PodcastEpisodeAdapter podcastEpisodeAdapter;

    public void attachGenericAdapter(RecyclerView recyclerView) {
        GridLayoutManager linearLayoutManager = new GridLayoutManager(getActivity(), 4);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        podcastEpisodeAdapter = new PodcastEpisodeAdapter(getActivity(), this, podcastEpisodeDetailsPOJOS);
        recyclerView.setAdapter(podcastEpisodeAdapter);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public void changeFollowText() {
        if (getActivity() instanceof HomeActivity) {
            HomeActivity homeActivity = (HomeActivity) getActivity();
            if (homeActivity.getArtisteFollowUpList().contains(conId)) {
                iv_favorited.setImageResource(R.drawable.ic_following_new);
            } else {
                iv_favorited.setImageResource(R.drawable.ic_follow_new);
            }
        }
    }

    public void getPodcastDetails() {
        JSONObject jsonObject = new JSONObject();

        showProgressBar();

        try {
            jsonObject.put("userId", Pref.GetStringPref(getActivity().getApplicationContext(), StringUtils.USER_ID, ""));
            jsonObject.put("deviceId", Pref.GetStringPref(getActivity().getApplicationContext(), StringUtils.DEVICE_ID, ""));
//            jsonObject.put("podcastId", "525");
            jsonObject.put("podcastId", conId);
            jsonObject.put("langCode", "en");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new ApiCallBase(getActivity(), new WebServicesCallBack() {
            @Override
            public void onGetMsg(String apicall, String response) {
                dismissProgressBar();
                try {
                    String object = new String(response);
                    JSONObject jsonObject = new JSONObject(object);
                    JSONObject responseObject = jsonObject.optJSONObject("response");
                    if (responseObject.optBoolean("status")) {

                        PodcastPOJO podcastPOJO=new Gson().fromJson(responseObject.optJSONObject("podcast").toString(),PodcastPOJO.class);

                        podcastDetailPOJO=podcastPOJO.getPodcastDetailPOJO();

                        tv_podcast_name.setText(podcastPOJO.getPodcastDetailPOJO().getTitle());
                        tv_podcast_description.setText(podcastPOJO.getPodcastDetailPOJO().getDescription());

                        Glide.with(getActivity())
                                .load(podcastPOJO.getPodcastDetailPOJO().getImgLocalUri())
                                .placeholder(R.drawable.ll_disc)
                                .error(R.drawable.ll_disc)
                                .dontAnimate()
                                .into(iv_podcast_image);

                        tv_podcast_by.setText("Podcast by "+podcastPOJO.getPodcastDetailPOJO().getTitle());

                        podcastEpisodeDetailsPOJOS.addAll(podcastPOJO.getPodcastEpisodeDetailsPOJOS());
                        podcastEpisodeAdapter.notifyDataSetChanged();

                        for(PodcastEpisodeDetailsPOJO podcastEpisodeDetailsPOJO:podcastEpisodeDetailsPOJOS){
                            HomeContentPOJO homeContentPOJO=new HomeContentPOJO();

                            homeContentPOJO.setConId("");
                            homeContentPOJO.setConName(podcastEpisodeDetailsPOJO.getTitle());
                            homeContentPOJO.setImgIrl(podcastEpisodeDetailsPOJO.getImgLocalUri().toString());
                            homeContentPOJO.setCotDeepLink(podcastEpisodeDetailsPOJO.getStreamUri());
                            homeContentPOJO.setDescription(podcastEpisodeDetailsPOJO.getDescription());
                            homeContentPOJO.setSubtitle(podcastEpisodeDetailsPOJO.getSubtitle());

                            homeContentPOJOS.add(homeContentPOJO);

                        }

                    } else {
                        ToastClass.showShortToast(getActivity().getApplicationContext(), "Something went wrong");
                    }
//                    Log.d(TagUtils.getTag(), jsonObject.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onErrorMsg(String status_code, String response) {
                ToastClass.showShortToast(getActivity().getApplicationContext(),"Server Down");
                dismissProgressBar();
            }
        }, "GET_PODCAST_DETAILS").makeApiCall(WebServicesUrls.GET_PODCAST_DETAILS, jsonObject);
    }

    public void playEpisode(int index){
        if(getActivity() instanceof HomeActivity){
            HomeActivity homeActivity= (HomeActivity) getActivity();
            homeActivity.playAudio(homeContentPOJOS,index,"artiste",podcastDetailPOJO);
        }
    }

}
