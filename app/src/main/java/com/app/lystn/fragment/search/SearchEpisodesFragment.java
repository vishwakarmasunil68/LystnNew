package com.app.lystn.fragment.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.app.lystn.R;
import com.app.lystn.activity.HomeActivity;
import com.app.lystn.adapter.search.SearchPodcastEpisodeAdapter;
import com.app.lystn.fragmentcontroller.FragmentController;
import com.app.lystn.pojo.artiste.PodcastDetailPOJO;
import com.app.lystn.pojo.artiste.PodcastEpisodeDetailsPOJO;
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

public class SearchEpisodesFragment extends FragmentController {

    @BindView(R.id.rv_results)
    RecyclerView rv_results;
    List<PodcastEpisodeDetailsPOJO> podcastEpisodeDetailsPOJOS;

    List<HomeContentPOJO> homeContentPOJOS=new ArrayList<>();


    public SearchEpisodesFragment(List<PodcastEpisodeDetailsPOJO> podcastEpisodeDetailsPOJOS){
        this.podcastEpisodeDetailsPOJOS=podcastEpisodeDetailsPOJOS;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.frag_podcasts,container,false);
        setUpView(getActivity(),this,view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        attachGenericAdapter(rv_results);

        for(PodcastEpisodeDetailsPOJO podcastEpisodeDetailsPOJO:podcastEpisodeDetailsPOJOS){

            HomeContentPOJO homeContentPOJO = new HomeContentPOJO();
            homeContentPOJO.setConId(podcastEpisodeDetailsPOJO.getEpisodeId());
            homeContentPOJO.setConName(podcastEpisodeDetailsPOJO.getTitle());
            homeContentPOJO.setImgIrl(podcastEpisodeDetailsPOJO.getImgLocalUri().toString());
            homeContentPOJO.setCotDeepLink(podcastEpisodeDetailsPOJO.getStreamUri());
            homeContentPOJO.setDescription(podcastEpisodeDetailsPOJO.getDescription());
            homeContentPOJO.setSubtitle(podcastEpisodeDetailsPOJO.getSubtitle());

            homeContentPOJOS.add(homeContentPOJO);
        }

    }


    public void attachGenericAdapter(RecyclerView rv) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(linearLayoutManager);
        SearchPodcastEpisodeAdapter searchResultAdapter = new SearchPodcastEpisodeAdapter(getActivity(), this, podcastEpisodeDetailsPOJOS);
        rv.setAdapter(searchResultAdapter);
        rv.setNestedScrollingEnabled(false);
        rv.setItemAnimator(new DefaultItemAnimator());
    }

    public void playAudio(int position,PodcastDetailPOJO podcastDetailPOJO) {
        if(getActivity() instanceof HomeActivity){
            HomeActivity homeActivity= (HomeActivity) getActivity();
            homeActivity.playAudio(homeContentPOJOS, position, "podcast", null);
        }
    }

    public void getEpisodeDetails(String episode_id,int position){
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
                    if(responseObject.optBoolean("status")){
                        JSONObject podcastJSONObject=responseObject.optJSONObject("podcast");
                        PodcastDetailPOJO podcastDetailPOJO=new Gson().fromJson(podcastJSONObject.optJSONObject("podcast_details").toString(),PodcastDetailPOJO.class);
                        playAudio(position,podcastDetailPOJO);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onErrorMsg(String status_code, String response) {
                dismissProgressBar();
                ToastClass.showShortToast(getActivity().getApplicationContext(),"Server Down");
            }
        }, "GET_EPISODE_DETAILS").makeApiCall(WebServicesUrls.GET_EPISODE_DETAILS, jsonObject);
    }
}
