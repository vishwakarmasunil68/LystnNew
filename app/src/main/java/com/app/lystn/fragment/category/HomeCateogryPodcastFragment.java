package com.app.lystn.fragment.category;

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
import com.app.lystn.adapter.CategoryPodcastAdapter;
import com.app.lystn.fragmentcontroller.FragmentController;
import com.app.lystn.pojo.CategoryPodcastPOJO;
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

import butterknife.BindView;

public class HomeCateogryPodcastFragment extends FragmentController {

    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tv_title)
    TextView tv_title;

    String conId;
    String url = "";
    String bkName;

    String bkType = "";


    public HomeCateogryPodcastFragment(String conId, String bkName) {
        this.conId = conId;
        this.bkName = bkName;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_all_categories, container, false);
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

        tv_title.setText(bkName);

        url = WebServicesUrls.GET_CATEGORY_BUCKET;

        if (!url.equalsIgnoreCase("")) {
            getHomeContents(url);
        }
    }

    public void getHomeContents(String url) {
        JSONObject jsonObject = new JSONObject();

        showProgressBar();

        try {
            jsonObject.put("userId", Pref.GetStringPref(getActivity().getApplicationContext(), StringUtils.USER_ID, ""));
            jsonObject.put("deviceId", Pref.GetStringPref(getActivity().getApplicationContext(), StringUtils.DEVICE_ID, ""));
            jsonObject.put("conId", conId);
            jsonObject.put("langCode", "1");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new ApiCallBase(getActivity(), new WebServicesCallBack() {
            @Override
            public void onGetMsg(String apicall, String response) {
                dismissProgressBar();
                categoryPodcastPOJOS.clear();
                try {
                    String object = new String(response);
                    JSONObject jsonObject = new JSONObject(object);
                    JSONObject responseObject = jsonObject.optJSONObject("response");
                    if (responseObject.optBoolean("status")) {
//                        if(getActivity() instanceof LoginActivity){


                        JSONObject dataJSON = responseObject.optJSONObject("data");
                        bkType = dataJSON.optString("bkType");
                        JSONArray podcastJSONAray = dataJSON.optJSONArray("contents");

                        for (int i = 0; i < podcastJSONAray.length(); i++) {
                            CategoryPodcastPOJO categoryPodcastPOJO = new Gson().fromJson(podcastJSONAray.optJSONObject(i).toString(), CategoryPodcastPOJO.class);
                            categoryPodcastPOJOS.add(categoryPodcastPOJO);
                        }

                        attachSquarAdapter();

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


    List<CategoryPodcastPOJO> categoryPodcastPOJOS = new ArrayList<>();

    public void attachSquarAdapter() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 4);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);
        CategoryPodcastAdapter homeArtistesAdapter = new CategoryPodcastAdapter(getActivity(), this, categoryPodcastPOJOS, bkType);
        recyclerView.setAdapter(homeArtistesAdapter);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

}
