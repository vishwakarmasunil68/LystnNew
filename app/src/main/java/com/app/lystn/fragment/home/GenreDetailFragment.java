package com.app.lystn.fragment.home;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.app.lystn.R;
import com.app.lystn.activity.HomeActivity;
import com.app.lystn.adapter.ViewPagerAdapter;
import com.app.lystn.fragment.playlist.PodcastPlayListFragment;
import com.app.lystn.fragment.playlist.RecommendListFragment;
import com.app.lystn.fragmentcontroller.FragmentController;
import com.app.lystn.pojo.artiste.PodcastDetailPOJO;
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

public class GenreDetailFragment extends FragmentController {


    @BindView(R.id.iv_podcast_image)
    ImageView iv_podcast_image;
    @BindView(R.id.tv_podcast_name)
    TextView tv_podcast_name;
    @BindView(R.id.tv_podcast_description)
    TextView tv_podcast_description;
    @BindView(R.id.tv_podcast_copy_right)
    TextView tv_podcast_copy_right;
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.tv_frag_1)
    TextView tv_frag_1;
    @BindView(R.id.tv_frag_2)
    TextView tv_frag_2;
    @BindView(R.id.view_frag_1)
    View view_frag_1;
    @BindView(R.id.view_frag_2)
    View view_frag_2;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.btn_follow)
    Button btn_follow;
    @BindView(R.id.iv_favorited)
    ImageView iv_favorited;

    PodcastDetailPOJO podcastDetailPOJO;

    List<TextView> textViews = new ArrayList<>();
    List<View> views = new ArrayList<>();

    String conId;

    public GenreDetailFragment(String conId) {
        this.conId = conId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_genre_detail, container, false);
        setUpView(getActivity(), this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textViews.add(tv_frag_1);
        textViews.add(tv_frag_2);


        views.add(view_frag_1);
        views.add(view_frag_2);

        setupViewPager(viewPager);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getPodcastDetails();

        changeFollowText();

        iv_favorited.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_follow.callOnClick();
            }
        });

        btn_follow.setOnClickListener(new View.OnClickListener() {
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
                                homeActivity.dismissProgressBar();
                                ToastClass.showShortToast(getActivity().getApplicationContext(),"Server Down");
                            }
                        }, "FOLLOW_API").makeApiCall(WebServicesUrls.FOLLOW_API, jsonObject);
                    }
                }
            }
        });

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

                        PodcastPOJO podcastPOJO = new Gson().fromJson(responseObject.optJSONObject("podcast").toString(), PodcastPOJO.class);

                        podcastDetailPOJO = podcastPOJO.getPodcastDetailPOJO();

                        tv_podcast_name.setText(podcastPOJO.getPodcastDetailPOJO().getTitle());
//                        tv_podcast_description.setContent(podcastPOJO.getPodcastDetailPOJO().getDescription());
//                        tv_podcast_description.setTextMaxLength(150);
//                        tv_podcast_description.setSeeMoreText("More","Less");
//                        tv_podcast_description.setSeeMoreTextColor(Color.parseColor("#FF8C00"));
                        tv_podcast_description.setText(podcastPOJO.getPodcastDetailPOJO().getDescription());

                        tv_podcast_copy_right.setText(podcastPOJO.getPodcastDetailPOJO().getCopyright());

                        Glide.with(getActivity())
                                .load(podcastPOJO.getPodcastDetailPOJO().getImgLocalUri())
                                .placeholder(R.drawable.ll_disc)
                                .error(R.drawable.ll_disc)
                                .dontAnimate()
                                .into(iv_podcast_image);

//                        podcastEpisodeDetailsPOJOS.addAll(podcastPOJO.getPodcastEpisodeDetailsPOJOS());
                        podcastPlayListFragment.setPodcastList(podcastPOJO.getPodcastDetailPOJO(), podcastPOJO.getPodcastEpisodeDetailsPOJOS());
                        podcastPlayListFragment.setPhases(String.valueOf(podcastPOJO.getPodcastEpisodeDetailsPOJOS().size()));

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
                dismissProgressBar();
                ToastClass.showShortToast(getActivity().getApplicationContext(),"Server Down");
            }
        }, "GET_PODCAST_DETAILS").makeApiCall(WebServicesUrls.GET_PODCAST_DETAILS, jsonObject);
    }


    PodcastPlayListFragment podcastPlayListFragment;

    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFrag(podcastPlayListFragment = new PodcastPlayListFragment(), "Monitor");
        adapter.addFrag(new RecommendListFragment(), "Monitor");

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(adapter.getCount());


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                makeTabSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void makeTabSelected(int position) {
        for (int i = 0; i < views.size(); i++) {
            views.get(i).setVisibility(View.INVISIBLE);
        }
        views.get(position).setVisibility(View.VISIBLE);

        for (int i = 0; i < textViews.size(); i++) {
            textViews.get(i).setTextColor(Color.parseColor("#000000"));
        }
        textViews.get(position).setTextColor(Color.parseColor("#FFFF8C00"));
    }


    public void playEpisode(List<HomeContentPOJO> homeContentPOJOS, int index) {
        if (getActivity() instanceof HomeActivity) {
            HomeActivity homeActivity = (HomeActivity) getActivity();
            homeActivity.playAudio(homeContentPOJOS, index, "genre", podcastDetailPOJO);
        }
    }

}
