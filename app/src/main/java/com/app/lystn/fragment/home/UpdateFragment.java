package com.app.lystn.fragment.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.lystn.R;
import com.app.lystn.activity.HomeActivity;
import com.app.lystn.adapter.UpdateAdapter;
import com.app.lystn.adapter.search.SearchPodcastAdapter;
import com.app.lystn.adapter.search.SearchPodcastEpisodeAdapter;
import com.app.lystn.fragmentcontroller.FragmentController;
import com.app.lystn.pojo.artiste.PodcastDetailPOJO;
import com.app.lystn.pojo.artiste.PodcastEpisodeDetailsPOJO;
import com.app.lystn.pojo.home.HomeContentPOJO;
import com.app.lystn.util.ItemClickListener;
import com.app.lystn.util.Pref;
import com.app.lystn.util.StringUtils;
import com.app.lystn.util.TagUtils;
import com.app.lystn.util.ToastClass;
import com.app.lystn.webservice.ApiCallBase;
import com.app.lystn.webservice.WebServicesCallBack;
import com.app.lystn.webservice.WebServicesUrls;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class UpdateFragment extends FragmentController {

    @BindView(R.id.ll_radios)
    LinearLayout ll_radios;
    @BindView(R.id.tv_more_radios)
    TextView tv_more_radios;
    @BindView(R.id.rv_radios)
    RecyclerView rv_radios;

    @BindView(R.id.ll_artists)
    LinearLayout ll_artists;
    @BindView(R.id.tv_more_artist)
    TextView tv_more_artist;
    @BindView(R.id.rv_artists)
    RecyclerView rv_artists;

    @BindView(R.id.ll_episodes)
    LinearLayout ll_episodes;
    @BindView(R.id.tv_more_episodes)
    TextView tv_more_episodes;
    @BindView(R.id.rv_episodes)
    RecyclerView rv_episodes;

    @BindView(R.id.ll_podcasts)
    LinearLayout ll_podcasts;
    @BindView(R.id.tv_more_podcast)
    TextView tv_more_podcast;
    @BindView(R.id.rv_podcasts)
    RecyclerView rv_podcasts;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.frag_update,container,false);
        setUpView(getActivity(),this,view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        callUpdatesAPI();


    }

    public void setValues() {
        try {
            if (podcastPOJOS.size() > 0) {
                ll_podcasts.setVisibility(View.VISIBLE);
            } else {
                ll_podcasts.setVisibility(View.GONE);
            }


            if (podcastEpisodeDetailsPOJOS.size() > 0) {
                ll_episodes.setVisibility(View.VISIBLE);
            } else {
                ll_episodes.setVisibility(View.GONE);
            }

            for (PodcastEpisodeDetailsPOJO podcastEpisodeDetailsPOJO : podcastEpisodeDetailsPOJOS) {

                HomeContentPOJO homeContentPOJO = new HomeContentPOJO();
                homeContentPOJO.setConId(podcastEpisodeDetailsPOJO.getEpisodeId());
                homeContentPOJO.setConName(podcastEpisodeDetailsPOJO.getTitle());
                homeContentPOJO.setImgIrl(podcastEpisodeDetailsPOJO.getImgLocalUri().toString());
                homeContentPOJO.setCotDeepLink(podcastEpisodeDetailsPOJO.getStreamUri());
                homeContentPOJO.setDescription(podcastEpisodeDetailsPOJO.getDescription());
                homeContentPOJO.setSubtitle(podcastEpisodeDetailsPOJO.getSubtitle());

                podcasthomeContentPOJOS.add(homeContentPOJO);
            }

            if (artistePojos.size() > 0) {
                ll_artists.setVisibility(View.VISIBLE);
            } else {
                ll_artists.setVisibility(View.GONE);
            }

            if (radioPOJOS.size() > 0) {
                ll_radios.setVisibility(View.VISIBLE);
            } else {
                ll_radios.setVisibility(View.GONE);
            }

//            searchPodcastAdapter.notifyDataSetChanged();
//            searchPodcastEpisodeAdapter.notifyDataSetChanged();
//            searchArtistAdapter.notifyDataSetChanged();
//            searchRadioAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    List<PodcastEpisodeDetailsPOJO> podcastEpisodeDetailsPOJOS = new ArrayList<>();
    List<HomeContentPOJO> podcastPOJOS = new ArrayList<>();
    List<HomeContentPOJO> artistePojos = new ArrayList<>();
    List<HomeContentPOJO> radioPOJOS = new ArrayList<>();

    List<HomeContentPOJO> podcasthomeContentPOJOS = new ArrayList<>();

    public void callUpdatesAPI() {
        JSONObject jsonObject = new JSONObject();
        new ApiCallBase(getActivity(), new WebServicesCallBack() {
            @Override
            public void onGetMsg(String apicall, String response) {
                try {
                    String object = new String(response);
                    JSONObject jsonObject = new JSONObject(object);
                    JSONObject responseObject = jsonObject.optJSONObject("response");
                    if (responseObject.optBoolean("status")) {

                        JSONObject result = responseObject.optJSONObject("result");

                        JSONObject summaryBucket = result.optJSONObject("summaryBucket");
                        if (summaryBucket.optInt("podcasts_count") > 0) {
                            JSONArray podcastBucketJSONArray = result.optJSONObject("podcasts_bucket").optJSONArray("contents");
                            for (int i = 0; i < podcastBucketJSONArray.length(); i++) {
                                podcastPOJOS.add(new Gson().fromJson(podcastBucketJSONArray.optJSONObject(i).toString(), HomeContentPOJO.class));
                            }
                        }

                        if (summaryBucket.optInt("episodes_count") > 0) {
                            JSONArray episodeBucketJSONArray = result.optJSONObject("episodes_bucket").optJSONArray("contents");
                            for (int i = 0; i < episodeBucketJSONArray.length(); i++) {
                                podcastEpisodeDetailsPOJOS.add(new Gson().fromJson(episodeBucketJSONArray.optJSONObject(i).toString(), PodcastEpisodeDetailsPOJO.class));
                            }
                        }

                        if (summaryBucket.optInt("artiste_count") > 0) {
                            JSONArray episodeBucketJSONArray = result.optJSONObject("artistes_bucket").optJSONArray("contents");
                            for (int i = 0; i < episodeBucketJSONArray.length(); i++) {
                                artistePojos.add(new Gson().fromJson(episodeBucketJSONArray.optJSONObject(i).toString(), HomeContentPOJO.class));
                            }
                        }

                        if (summaryBucket.optInt("radios_count") > 0) {
                            JSONArray episodeBucketJSONArray = result.optJSONObject("radios_bucket").optJSONArray("contents");
                            for (int i = 0; i < episodeBucketJSONArray.length(); i++) {
                                radioPOJOS.add(new Gson().fromJson(episodeBucketJSONArray.optJSONObject(i).toString(), HomeContentPOJO.class));
                            }
                        }

                        setValues();

                        attachPodcastAdapter();
                        attachPodcastEpisodeAdapter();
                        attachArtistAdapter();
                        attachRadioAdapter();
                    } else {
                        ToastClass.showShortToast(getActivity().getApplicationContext(), "Something went wrong");
                    }
                    Log.d(TagUtils.getTag(), jsonObject.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onErrorMsg(String status_code, String response) {
                ToastClass.showShortToast(getActivity().getApplicationContext(), "Server Down");
            }
        }, "GET_UPDATES").makeApiCall(WebServicesUrls.GET_UPDATES, jsonObject);
    }

    SearchPodcastAdapter searchPodcastAdapter;

    public void attachPodcastAdapter() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv_podcasts.setHasFixedSize(true);
        rv_podcasts.setLayoutManager(linearLayoutManager);
        searchPodcastAdapter = new SearchPodcastAdapter(getActivity(), this, podcastPOJOS);
        rv_podcasts.setAdapter(searchPodcastAdapter);
        rv_podcasts.setNestedScrollingEnabled(false);
        rv_podcasts.setItemAnimator(new DefaultItemAnimator());
        searchPodcastAdapter.setItemAdapter(new ItemClickListener() {
            @Override
            public void itemClicked(int position) {
                openPodcastPage(podcastPOJOS.get(position).getConId());
            }
        });
    }

    public void openPodcastPage(String id) {
        if (getActivity() instanceof HomeActivity) {
            HomeActivity homeActivity = (HomeActivity) getActivity();
            homeActivity.startPodcastFragment(id);
        }
    }

    SearchPodcastEpisodeAdapter searchPodcastEpisodeAdapter;

    public void attachPodcastEpisodeAdapter() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv_episodes.setHasFixedSize(true);
        rv_episodes.setLayoutManager(linearLayoutManager);
        searchPodcastEpisodeAdapter = new SearchPodcastEpisodeAdapter(getActivity(), this, podcastEpisodeDetailsPOJOS);
        rv_episodes.setAdapter(searchPodcastEpisodeAdapter);
        rv_episodes.setNestedScrollingEnabled(false);
        rv_episodes.setItemAnimator(new DefaultItemAnimator());
        searchPodcastEpisodeAdapter.setItemAdapter(new ItemClickListener() {
            @Override
            public void itemClicked(int position) {
                getEpisodeDetails(podcastEpisodeDetailsPOJOS.get(position).getEpisodeId(),position);
            }
        });
    }

    public void playAudio(List<HomeContentPOJO> homeContentPOJOS, int position, PodcastDetailPOJO podcastDetailPOJO) {
        if (getActivity() instanceof HomeActivity) {
            HomeActivity homeActivity = (HomeActivity) getActivity();
            homeActivity.playAudio(homeContentPOJOS, position, "podcast", null);
        }
    }

    public void getEpisodeDetails(String episode_id, int position) {
        showProgressBar();
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("userId", Pref.GetStringPref(getActivity().getApplicationContext(), StringUtils.USER_ID, ""));
            jsonObject.put("deviceId", "91");
            jsonObject.put("episode_id", episode_id);
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
                        JSONObject podcastJSONObject = responseObject.optJSONObject("podcast");
                        PodcastDetailPOJO podcastDetailPOJO = new Gson().fromJson(podcastJSONObject.optJSONObject("podcast_details").toString(), PodcastDetailPOJO.class);
                        playAudio(podcasthomeContentPOJOS, position, podcastDetailPOJO);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onErrorMsg(String status_code, String response) {
                dismissProgressBar();
                ToastClass.showShortToast(getActivity().getApplicationContext(), "Server Down");
            }
        }, "GET_EPISODE_DETAILS").makeApiCall(WebServicesUrls.GET_EPISODE_DETAILS, jsonObject);
    }

    SearchPodcastAdapter searchArtistAdapter;

    public void attachArtistAdapter() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv_artists.setHasFixedSize(true);
        rv_artists.setLayoutManager(linearLayoutManager);
        searchArtistAdapter = new SearchPodcastAdapter(getActivity(), this, artistePojos);
        rv_artists.setAdapter(searchArtistAdapter);
        rv_artists.setNestedScrollingEnabled(false);
        rv_artists.setItemAnimator(new DefaultItemAnimator());
        searchArtistAdapter.setItemAdapter(new ItemClickListener() {
            @Override
            public void itemClicked(int position) {
                openArtistePage(artistePojos.get(position).getConId());
            }
        });
    }

    public void openArtistePage(String conId) {
        if (getActivity() instanceof HomeActivity) {
            HomeActivity homeActivity = (HomeActivity) getActivity();
            homeActivity.showArtisteDetail(conId);
        }
    }

    SearchPodcastAdapter searchRadioAdapter;

    public void attachRadioAdapter() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv_radios.setHasFixedSize(true);
        rv_radios.setLayoutManager(linearLayoutManager);
        searchRadioAdapter = new SearchPodcastAdapter(getActivity(), this, radioPOJOS);
        rv_radios.setAdapter(searchRadioAdapter);
        rv_radios.setNestedScrollingEnabled(false);
        rv_radios.setItemAnimator(new DefaultItemAnimator());
        searchRadioAdapter.setItemAdapter(new ItemClickListener() {
            @Override
            public void itemClicked(int position) {
                openRadioPlayer(radioPOJOS,radioPOJOS.get(position).getConId(),position);
            }
        });
    }

    public void openRadioPlayer(List<HomeContentPOJO> homeContentPOJOS, String conId, int position) {
        if (getActivity() instanceof HomeActivity) {
            HomeActivity homeActivity = (HomeActivity) getActivity();
            homeActivity.playAudio(homeContentPOJOS, position, "radio", null);
        }
    }


}
