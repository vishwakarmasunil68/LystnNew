package com.app.lystn.fragment.home;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;
import com.app.lystn.R;
import com.app.lystn.activity.HomeActivity;
import com.app.lystn.adapter.SearchRankingAdapter;
import com.app.lystn.adapter.ViewPagerAdapter;
import com.app.lystn.fragment.search.AllSearchResultFragment;
import com.app.lystn.fragment.search.SearchArtistFragment;
import com.app.lystn.fragment.search.SearchEpisodesFragment;
import com.app.lystn.fragment.search.SearchPodcastsFragment;
import com.app.lystn.fragment.search.SearchRadioFragment;
import com.app.lystn.fragmentcontroller.FragmentController;
import com.app.lystn.pojo.artiste.PodcastDetailPOJO;
import com.app.lystn.pojo.artiste.PodcastEpisodeDetailsPOJO;
import com.app.lystn.pojo.home.HomeContentPOJO;
import com.app.lystn.util.Pref;
import com.app.lystn.util.StringUtils;
import com.app.lystn.util.TagUtils;
import com.app.lystn.util.ToastClass;
import com.app.lystn.webservice.ApiCallBase;
import com.app.lystn.webservice.WebServicesCallBack;
import com.app.lystn.webservice.WebServicesUrls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;

public class SearchFragment extends FragmentController {

    @BindView(R.id.rv_search_ranking)
    RecyclerView rv_search_ranking;
    @BindView(R.id.rv_popular)
    RecyclerView rv_popular;
    @BindView(R.id.ll_search_default)
    LinearLayout ll_search_default;
    @BindView(R.id.ll_search_layout)
    LinearLayout ll_search_layout;
    @BindView(R.id.ll_no_results)
    LinearLayout ll_no_results;
    @BindView(R.id.et_search)
    EditText et_search;
    @BindView(R.id.searchViewPager)
    ViewPager searchViewPager;

    @BindView(R.id.ll_all_results)
    LinearLayout ll_all_results;
    @BindView(R.id.ll_episodes)
    LinearLayout ll_episodes;
    @BindView(R.id.ll_artists)
    LinearLayout ll_artists;
    //    @BindView(R.id.ll_my_downloads)
//    LinearLayout ll_my_downloads;
    @BindView(R.id.ll_podcasts)
    LinearLayout ll_podcasts;
    @BindView(R.id.ll_radio)
    LinearLayout ll_radio;

    @BindView(R.id.tv_all_results)
    TextView tv_all_results;
    @BindView(R.id.tv_podcasts)
    TextView tv_podcasts;
    @BindView(R.id.tv_episodes)
    TextView tv_episodes;
    @BindView(R.id.tv_artists)
    TextView tv_artists;
    @BindView(R.id.tv_radio)
    TextView tv_radio;
    @BindView(R.id.ll_stt)
    LinearLayout ll_stt;
//    @BindView(R.id.tv_my_downloads)
//    TextView tv_my_downloads;

    List<LinearLayout> linearLayouts = new ArrayList<>();
    List<TextView> textViews = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_search, container, false);
        setUpView(getActivity(), this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        attachSearchAdapter();
        attachPopularAdapter();

        ll_search_default.setVisibility(View.VISIBLE);
        ll_search_layout.setVisibility(View.GONE);
        ll_no_results.setVisibility(View.GONE);

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (et_search.getText().toString().length() == 0) {
                    ll_search_default.setVisibility(View.VISIBLE);
                    ll_search_layout.setVisibility(View.GONE);
                    ll_no_results.setVisibility(View.GONE);
                }
//                else if (et_search.getText().toString().length() > 6) {
//                    ll_search_default.setVisibility(View.GONE);
//                    ll_search_layout.setVisibility(View.GONE);
//                    ll_no_results.setVisibility(View.VISIBLE);
//                }
                else {
                    search_count = search_count + 1;
                    ll_search_default.setVisibility(View.GONE);
                    ll_search_layout.setVisibility(View.VISIBLE);
                    ll_no_results.setVisibility(View.GONE);
                    callSearchAPI(search_count);
                }
            }
        });

        getSearchSuggestion();


//        textViews.add(tv_my_downloads);

//        makeTabSelected(0);

        ll_stt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchTT();
            }
        });

    }

    public void launchTT() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hi speak something");
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1: {
                if (resultCode == Activity.RESULT_OK && null != data) {
                    ArrayList result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    Log.d(TagUtils.getTag(),"result:-"+result.get(0));
                    et_search.setText((String)result.get(0));
                }
                break;
            }
        }
    }

    SearchRankingAdapter searchRankingAdapter;

    public void attachSearchAdapter() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 2);
        rv_search_ranking.setHasFixedSize(true);
        rv_search_ranking.setLayoutManager(gridLayoutManager);
        searchRankingAdapter = new SearchRankingAdapter(getActivity(), this, ratingSearchContent);
        rv_search_ranking.setAdapter(searchRankingAdapter);
        rv_search_ranking.setNestedScrollingEnabled(false);
        rv_search_ranking.setItemAnimator(new DefaultItemAnimator());
    }

    SearchRankingAdapter popularSearchRankingAdapter;

    public void attachPopularAdapter() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 2);
        rv_popular.setHasFixedSize(true);
        rv_popular.setLayoutManager(gridLayoutManager);
        popularSearchRankingAdapter = new SearchRankingAdapter(getActivity(), this, popularSearchContent);
        rv_popular.setAdapter(popularSearchRankingAdapter);
        rv_popular.setNestedScrollingEnabled(false);
        rv_popular.setItemAnimator(new DefaultItemAnimator());
    }

//    SearchPodcastsFragment searchPodcastsFragment;
//    SearchEpisodesFragment searchEpisodesFragment;
//    SearchArtistFragment searchArtistFragment;
//    SearchRadioFragment searchRadioFragment;
//
//    public void initFragments(){
//        searchPodcastsFragment=new SearchPodcastsFragment(new ArrayList<>());
//        searchEpisodesFragment=new SearchEpisodesFragment(new ArrayList<>());
//        searchArtistFragment=new SearchArtistFragment(new ArrayList<>());
//        searchRadioFragment=new SearchRadioFragment(new ArrayList<>());
//    }

    int search_count = 0;

    AllSearchResultFragment allSearchResultFragment;

    private void setupViewPager(List<HomeContentPOJO> podcastPOJOS, List<PodcastEpisodeDetailsPOJO> podcastEpisodeDetailsPOJOS, List<HomeContentPOJO> artistePojos, List<HomeContentPOJO> radioPOJOS) {

        makeVisibilityGone();
        linearLayouts.clear();
        textViews.clear();

        linearLayouts.add(ll_all_results);
        textViews.add(tv_all_results);


//        linearLayouts.add(ll_my_downloads);


        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFrag(allSearchResultFragment = new AllSearchResultFragment(podcastPOJOS, podcastEpisodeDetailsPOJOS, artistePojos, radioPOJOS), "Monitor");
//        allSearchResultFragment.setAllSearchAdapter(podcastPOJOS, podcastEpisodeDetailsPOJOS, artistePojos, radioPOJOS);
        if (podcastPOJOS.size() > 0) {
            linearLayouts.add(ll_podcasts);
            textViews.add(tv_podcasts);
            adapter.addFrag(new SearchPodcastsFragment(podcastPOJOS), "Monitor");
            ll_podcasts.setVisibility(View.VISIBLE);
        } else {
            ll_podcasts.setVisibility(View.GONE);
        }
        if (podcastEpisodeDetailsPOJOS.size() > 0) {
            linearLayouts.add(ll_episodes);
            textViews.add(tv_episodes);
            adapter.addFrag(new SearchEpisodesFragment(podcastEpisodeDetailsPOJOS), "Monitor");
            ll_episodes.setVisibility(View.VISIBLE);
        } else {
            ll_episodes.setVisibility(View.GONE);
        }
        if (artistePojos.size() > 0) {
            linearLayouts.add(ll_artists);
            textViews.add(tv_artists);
            adapter.addFrag(new SearchArtistFragment(artistePojos), "Monitor");
            ll_artists.setVisibility(View.VISIBLE);
        } else {
            ll_artists.setVisibility(View.GONE);
        }
        if (radioPOJOS.size() > 0) {
            linearLayouts.add(ll_radio);
            textViews.add(tv_radio);
            adapter.addFrag(new SearchRadioFragment(radioPOJOS), "Monitor");
            ll_radio.setVisibility(View.VISIBLE);
        } else {
            ll_radio.setVisibility(View.GONE);
        }
//        adapter.addFrag(new SearchMyDownloadFragment(), "Monitor");

        searchViewPager.setAdapter(adapter);
        searchViewPager.setOffscreenPageLimit(adapter.getCount());


        searchViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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

        makeVisibilityVisible();
//        makeTabSelected(0);
    }

    public void makeVisibilityGone() {
        for (int i = 0; i < linearLayouts.size(); i++) {
            linearLayouts.get(i).setVisibility(View.GONE);
        }
    }

    public void makeVisibilityVisible() {
        for (int i = 0; i < linearLayouts.size(); i++) {
            linearLayouts.get(i).setVisibility(View.VISIBLE);
        }
    }

    public void makeTabSelected(int position) {
        for (int i = 0; i < linearLayouts.size(); i++) {
            linearLayouts.get(i).setBackgroundResource(0);
        }

        for (int i = 0; i < textViews.size(); i++) {
            textViews.get(i).setTextColor(Color.parseColor("#757575"));
        }

        linearLayouts.get(position).setBackgroundResource(R.drawable.ic_tab_selected);

        for (int i = 0; i < linearLayouts.size(); i++) {
            int finalI = i;
            linearLayouts.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    searchViewPager.setCurrentItem(finalI);
                }
            });
        }

        textViews.get(position).setTextColor(Color.parseColor("#FFFFFF"));
    }

    List<PodcastEpisodeDetailsPOJO> podcastEpisodeDetailsPOJOS = new ArrayList<>();
    List<HomeContentPOJO> podcastPOJOS = new ArrayList<>();
    List<HomeContentPOJO> artistePojos = new ArrayList<>();
    List<HomeContentPOJO> radioPOJOS = new ArrayList<>();

    public void callSearchAPI(int count) {
        JSONObject jsonObject = new JSONObject();

//        showProgressBar();

        try {
            jsonObject.put("userId", Pref.GetStringPref(getActivity().getApplicationContext(), StringUtils.USER_ID, ""));
            jsonObject.put("deviceId", Pref.GetStringPref(getActivity().getApplicationContext(), StringUtils.DEVICE_ID, ""));
            jsonObject.put("langCode", "en");
            jsonObject.put("search_str", et_search.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new ApiCallBase(getActivity(), new WebServicesCallBack() {
            @Override
            public void onGetMsg(String apicall, String response) {
//                dismissProgressBar();
//                homeContentPOJOS.clear();
                parseSearchDataBackground(response, count);
            }

            @Override
            public void onErrorMsg(String status_code, String response) {
//                dismissProgressBar();
                ToastClass.showShortToast(getActivity().getApplicationContext(), "Server Down");
            }
        }, "GET_SEARCH_RESULT").makeApiCall(WebServicesUrls.GET_SEARCH_RESULT, jsonObject);
    }

    public void parseSearchDataBackground(String response, int count) {
        if (count == search_count) {
            podcastPOJOS.clear();
            podcastEpisodeDetailsPOJOS.clear();
            artistePojos.clear();
            radioPOJOS.clear();
            new AsyncTask<Void, Void, Void>() {

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                }

                @Override
                protected Void doInBackground(Void... voids) {
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
                        } else {
                            ToastClass.showShortToast(getActivity().getApplicationContext(), "Something went wrong");
                        }
                        Log.d(TagUtils.getTag(), jsonObject.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    setupViewPager(podcastPOJOS, podcastEpisodeDetailsPOJOS, artistePojos, radioPOJOS);
                    et_search.requestFocus();
                }
            }.execute();
        }
    }


    List<HomeContentPOJO> ratingSearchContent = new ArrayList<>();
    List<HomeContentPOJO> popularSearchContent = new ArrayList<>();

    public void getSearchSuggestion() {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("userId", Pref.GetStringPref(getActivity().getApplicationContext(), StringUtils.USER_ID, ""));
            jsonObject.put("deviceId", Pref.GetStringPref(getActivity().getApplicationContext(), StringUtils.DEVICE_ID, ""));
            jsonObject.put("langCode", "en");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new ApiCallBase(getActivity(), new WebServicesCallBack() {
            @Override
            public void onGetMsg(String apicall, String response) {


                try {
                    String object = new String(response);
                    JSONObject jsonObject = new JSONObject(object);
                    JSONObject responseObject = jsonObject.optJSONObject("response");
                    if (responseObject.optBoolean("status")) {

                        JSONObject result = responseObject.optJSONObject("result");

                        JSONObject result_rankingJSON = result.optJSONObject("result_ranking");
                        try {
                            JSONArray podcastRankingJSONArray = result_rankingJSON.optJSONObject("podcasts_bucket").optJSONArray("contents");
                            for (int i = 0; i < podcastRankingJSONArray.length(); i++) {
                                HomeContentPOJO homeContentPOJO = new Gson().fromJson(podcastRankingJSONArray.optJSONObject(i).toString(), HomeContentPOJO.class);
                                homeContentPOJO.setSearch_type(StringUtils.SEARCH_PODCAST_CONTENT);
                                ratingSearchContent.add(homeContentPOJO);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        try {
                            JSONArray podcastRankingJSONArray = result_rankingJSON.optJSONObject("episodes_bucket").optJSONArray("contents");
                            for (int i = 0; i < podcastRankingJSONArray.length(); i++) {
                                HomeContentPOJO homeContentPOJO = new Gson().fromJson(podcastRankingJSONArray.optJSONObject(i).toString(), HomeContentPOJO.class);
                                homeContentPOJO.setSearch_type(StringUtils.SEARCH_EPISODE_CONTENT);
                                ratingSearchContent.add(homeContentPOJO);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        try {
                            JSONArray podcastRankingJSONArray = result_rankingJSON.optJSONObject("radios_bucket").optJSONArray("contents");
                            for (int i = 0; i < podcastRankingJSONArray.length(); i++) {
                                HomeContentPOJO homeContentPOJO = new Gson().fromJson(podcastRankingJSONArray.optJSONObject(i).toString(), HomeContentPOJO.class);
                                homeContentPOJO.setSearch_type(StringUtils.SEARCH_RADIOS_CONTENT);
                                ratingSearchContent.add(homeContentPOJO);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        try {
                            JSONArray podcastRankingJSONArray = result_rankingJSON.optJSONObject("artistes_bucket").optJSONArray("contents");
                            for (int i = 0; i < podcastRankingJSONArray.length(); i++) {
                                HomeContentPOJO homeContentPOJO = new Gson().fromJson(podcastRankingJSONArray.optJSONObject(i).toString(), HomeContentPOJO.class);
                                homeContentPOJO.setSearch_type(StringUtils.SEARCH_ARTISTE_CONTENT);
                                ratingSearchContent.add(homeContentPOJO);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        JSONObject result_popularJSON = result.optJSONObject("result_ranking");

                        try {
                            JSONArray podcastRankingJSONArray = result_popularJSON.optJSONObject("podcasts_bucket").optJSONArray("contents");
                            for (int i = 0; i < podcastRankingJSONArray.length(); i++) {
                                HomeContentPOJO homeContentPOJO = new Gson().fromJson(podcastRankingJSONArray.optJSONObject(i).toString(), HomeContentPOJO.class);
                                homeContentPOJO.setSearch_type(StringUtils.SEARCH_PODCAST_CONTENT);
                                popularSearchContent.add(homeContentPOJO);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        try {
                            JSONArray podcastRankingJSONArray = result_popularJSON.optJSONObject("episodes_bucket").optJSONArray("contents");
                            for (int i = 0; i < podcastRankingJSONArray.length(); i++) {
                                HomeContentPOJO homeContentPOJO = new Gson().fromJson(podcastRankingJSONArray.optJSONObject(i).toString(), HomeContentPOJO.class);
                                homeContentPOJO.setSearch_type(StringUtils.SEARCH_EPISODE_CONTENT);
                                popularSearchContent.add(homeContentPOJO);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        try {
                            JSONArray podcastRankingJSONArray = result_popularJSON.optJSONObject("radios_bucket").optJSONArray("contents");
                            for (int i = 0; i < podcastRankingJSONArray.length(); i++) {
                                HomeContentPOJO homeContentPOJO = new Gson().fromJson(podcastRankingJSONArray.optJSONObject(i).toString(), HomeContentPOJO.class);
                                homeContentPOJO.setSearch_type(StringUtils.SEARCH_RADIOS_CONTENT);
                                popularSearchContent.add(homeContentPOJO);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        try {
                            JSONArray podcastRankingJSONArray = result_popularJSON.optJSONObject("artistes_bucket").optJSONArray("contents");
                            for (int i = 0; i < podcastRankingJSONArray.length(); i++) {
                                HomeContentPOJO homeContentPOJO = new Gson().fromJson(podcastRankingJSONArray.optJSONObject(i).toString(), HomeContentPOJO.class);
                                homeContentPOJO.setSearch_type(StringUtils.SEARCH_ARTISTE_CONTENT);
                                popularSearchContent.add(homeContentPOJO);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        popularSearchRankingAdapter.notifyDataSetChanged();
                        searchRankingAdapter.notifyDataSetChanged();

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
//                dismissProgressBar();
                ToastClass.showShortToast(getActivity().getApplicationContext(), "Server Down");
            }
        }, "GET_SEARCH_RESULT").makeApiCall(WebServicesUrls.GET_SEARCH_SUGGESTION, jsonObject);
    }

    public void playAudio(List<HomeContentPOJO> homeContentPOJOS, int position, PodcastDetailPOJO podcastDetailPOJO) {
        if (getActivity() instanceof HomeActivity) {
            HomeActivity homeActivity = (HomeActivity) getActivity();
            homeActivity.playAudio(homeContentPOJOS, position, "podcast", null);
        }
    }

    public void getEpisodeDetails(List<HomeContentPOJO> homeContentPOJOS, String episode_id, int position) {
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
                        playAudio(homeContentPOJOS, position, podcastDetailPOJO);
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
}
