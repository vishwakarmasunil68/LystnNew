package com.app.lystn.fragment.home;

import android.os.Bundle;
import android.util.Log;
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

import com.google.gson.Gson;
import com.app.lystn.R;
import com.app.lystn.adapter.ArtistAdapter;
import com.app.lystn.adapter.GenreAdapter;
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

public class HomeAllContentFragment extends FragmentController {

    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tv_title)
    TextView tv_title;

    HomePOJO homePOJO;
    String url = "";


    public HomeAllContentFragment(HomePOJO homePOJO) {
        this.homePOJO = homePOJO;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_all_content, container, false);
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

        tv_title.setText(homePOJO.getBkName());

//        switch (homePOJO.getBkType()) {
//            case "category":
//                break;
//            case "artiste":
//                url = WebServicesUrls.GET_ARTIST_BUCKET;
//                break;
//            case "genre":
//                url = WebServicesUrls.GET_GENRE_BUCKET;
//                break;
//            case "radio":
//                url = WebServicesUrls.GET_RADIO_BUCKET;
//                break;
//        }

        url= WebServicesUrls.GET_SEE_ALL_BUCKET;

        if (!url.equalsIgnoreCase("")) {
            getHomeContents(url);
        }
    }

    public void getHomeContents(String url) {
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

                        if (homePOJO.getShapeType().equalsIgnoreCase("circle")) {
                            attachCircleAdapter();
                        } else {
                            attachSquarAdapter();
                        }

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
                dismissProgressBar();
                ToastClass.showShortToast(getActivity().getApplicationContext(),"Server Down");

            }
        }, "GET_HOME_CONTENT").makeApiCall(url, jsonObject);
    }


    List<HomeContentPOJO> homeContentPOJOS = new ArrayList<>();

    public void attachSquarAdapter() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 4);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);
        GenreAdapter homeArtistesAdapter = new GenreAdapter(getActivity(), this, homeContentPOJOS,homePOJO.getBkType());
        recyclerView.setAdapter(homeArtistesAdapter);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public void attachCircleAdapter() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 4);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);
        ArtistAdapter homeArtistesAdapter = new ArtistAdapter(getActivity(), this, homeContentPOJOS,homePOJO.getBkType());
        recyclerView.setAdapter(homeArtistesAdapter);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }
}
