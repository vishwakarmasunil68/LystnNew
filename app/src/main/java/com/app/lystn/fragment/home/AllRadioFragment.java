package com.app.lystn.fragment.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.app.lystn.R;
import com.app.lystn.activity.HomeActivity;
import com.app.lystn.adapter.RadioAdapter;
import com.app.lystn.fragmentcontroller.FragmentController;
import com.app.lystn.pojo.home.HomeContentPOJO;
import com.app.lystn.pojo.home.HomePOJO;
import com.app.lystn.util.Pref;
import com.app.lystn.util.StringUtils;
import com.app.lystn.util.TagUtils;
import com.app.lystn.util.ToastClass;
import com.app.lystn.webservice.ApiCallBase;
import com.app.lystn.webservice.WebServicesCallBack;
import com.app.lystn.webservice.WebServicesUrls;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class AllRadioFragment extends FragmentController {

    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.rv_radio)
    RecyclerView rv_radio;
    @BindView(R.id.iv_radio_play_all)
    ImageView iv_radio_play_all;

    HomePOJO homePOJO;

    List<HomeContentPOJO> homeContentPOJOS = new ArrayList<>();

    public AllRadioFragment(HomePOJO homePOJO){
        this.homePOJO=homePOJO;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.frag_all_radio,container,false);
        setUpView(getActivity(),this,view);
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

        iv_radio_play_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity() instanceof HomeActivity){
                    HomeActivity homeActivity= (HomeActivity) getActivity();
                    homeActivity.playAudio(homeContentPOJOS,0,"radio",null);
                }
            }
        });

        attachArtistAdapter();
        getRadioData();

    }

    @Override
    public void onDestroy() {
        if (getActivity() instanceof HomeActivity) {
            HomeActivity homeActivity = (HomeActivity) getActivity();
            homeActivity.onMusicPlayerClosed();
        }
        super.onDestroy();
    }

    public void getRadioData() {
//        JSONObject jsonObject = new JSONObject();
//
//        showProgressBar();
//
//        try {
//            jsonObject.put("userId", Pref.GetStringPref(getActivity().getApplicationContext(), StringUtils.USER_ID, ""));
//            jsonObject.put("deviceId", Pref.GetStringPref(getActivity().getApplicationContext(), StringUtils.DEVICE_ID, ""));
//            jsonObject.put("langCode", "en");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        new ApiCallBase(getActivity(), new WebServicesCallBack() {
//            @Override
//            public void onGetMsg(String apicall, String response) {
//                dismissProgressBar();
//                radioPojos.clear();
//                try {
//                    String object = new String(response);
//                    JSONObject jsonObject = new JSONObject(object);
//                    JSONObject responseObject = jsonObject.optJSONObject("response");
//                    if (responseObject.optBoolean("status")) {
////                        if(getActivity() instanceof LoginActivity){
//
//                        HomePOJO homePOJO=new Gson().fromJson(responseObject.optJSONObject("data").toString(),HomePOJO.class);
//                        for(HomeContentPOJO homeContentPOJO:homePOJO.getHomeContentPOJOS()){
//                            radioPojos.add(homeContentPOJO);
//                        }
//
//                        queueAdapter.notifyDataSetChanged();
//
//                    } else {
//                        ToastClass.showShortToast(getActivity(), "Something went wrong");
//                    }
//                    Log.d(TagUtils.getTag(), jsonObject.toString());
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onErrorMsg(String status_code, String response) {
//                dismissProgressBar();
//            }
//        }, "GET_HOME").makeApiCall(WebServicesUrls.GET_RADIO_BUCKET, jsonObject);

        JSONObject jsonObject = new JSONObject();

        showProgressBar();

        try {
            jsonObject.put("userId", Pref.GetStringPref(getActivity().getApplicationContext(), StringUtils.USER_ID, ""));
            jsonObject.put("isdCode", "91");
            jsonObject.put("mobileNo", "9873738969");
            jsonObject.put("deviceId", Pref.GetStringPref(getActivity().getApplicationContext(), StringUtils.DEVICE_ID, ""));
            jsonObject.put("conId", "1");
            jsonObject.put("langCode", "en");
            jsonObject.put("bkId", homePOJO.getBkId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new ApiCallBase(getActivity(), new WebServicesCallBack() {
            @Override
            public void onGetMsg(String apicall, String response) {
                dismissProgressBar();
                homeContentPOJOS.clear();
                try {
                    String object = new String(response);
                    JSONObject jsonObject = new JSONObject(object);
                    JSONObject responseObject = jsonObject.optJSONObject("response");
                    if (responseObject.optBoolean("status")) {
//                        if(getActivity() instanceof LoginActivity){

                        HomePOJO homePOJO = new Gson().fromJson(responseObject.optJSONObject("data").toString(), HomePOJO.class);
                        for (HomeContentPOJO homeContentPOJO : homePOJO.getHomeContentPOJOS()) {
                            homeContentPOJOS.add(homeContentPOJO);
                        }

                        radioAdapter.notifyDataSetChanged();

                    } else {
                        ToastClass.showShortToast(getActivity(), "Something went wrong");
                    }
                    Log.d(TagUtils.getTag(), jsonObject.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onErrorMsg(String status_code, String response) {
                ToastClass.showShortToast(getActivity().getApplicationContext(),"Server Down");
                dismissProgressBar();
            }
        }, "GET_HOME_CONTENT").makeApiCall(WebServicesUrls.GET_SEE_ALL_BUCKET, jsonObject);
    }

    RadioAdapter radioAdapter;

    public void attachArtistAdapter() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv_radio.setHasFixedSize(true);
        rv_radio.setLayoutManager(linearLayoutManager);
        radioAdapter = new RadioAdapter(getActivity(), this, homeContentPOJOS);
        rv_radio.setAdapter(radioAdapter);
        rv_radio.setNestedScrollingEnabled(false);
        rv_radio.setItemAnimator(new DefaultItemAnimator());
    }
}
