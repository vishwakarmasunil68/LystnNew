package com.app.lystn.fragment.login;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.gson.Gson;
import com.app.lystn.R;
import com.app.lystn.activity.LoginActivity;
import com.app.lystn.adapter.GenreCategoryAdapter;
import com.app.lystn.fragmentcontroller.FragmentController;
import com.app.lystn.pojo.login.CategoryTagPOJO;
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

public class LoginTagFragment extends FragmentController {

//    @BindView(R.id.iv_login_mobile)
//    ImageView iv_login_mobile;
    @BindView(R.id.tv_skip)
    TextView tv_skip;
    @BindView(R.id.rv_genre_category)
    RecyclerView rv_genre_category;
    @BindView(R.id.iv_continue)
    ImageView iv_continue;

    List<CategoryTagPOJO> categoryTagPOJOS=new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.frag_login_tag,container,false);
        setUpView(getActivity(),this,view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        iv_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValid()){
                    postGenre();
                }
            }
        });

        tv_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity() instanceof LoginActivity){
                    Pref.SetBooleanPref(getActivity().getApplicationContext(), StringUtils.LOGIN_TAG,true);
                    LoginActivity loginActivity= (LoginActivity) getActivity();
                    loginActivity.skipFragment();
                }
            }
        });

        attachGenreCategoryAdapter();
        getGenre();
    }

    public boolean isValid(){

        for(int i=0;i<categoryTagPOJOS.size();i++){
            if(categoryTagPOJOS.get(i).isActive()){
                return true;
            }
        }

        return false;
    }

    public void getGenre() {

        JSONObject jsonObject = new JSONObject();

        showProgressBar();

        try {
            jsonObject.put("userId", Pref.GetStringPref(getActivity().getApplicationContext(),StringUtils.USER_ID,""));
            jsonObject.put("deviceId", "91");
            jsonObject.put("langCode", "en");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new ApiCallBase(getActivity(), new WebServicesCallBack() {
            @Override
            public void onGetMsg(String apicall, String response) {
                dismissProgressBar();
                categoryTagPOJOS.clear();
                try {
                    String object = new String(response);
                    JSONObject jsonObject = new JSONObject(object);
                    JSONObject responseObject = jsonObject.optJSONObject("response");
                    if (responseObject.optBoolean("status")) {

                        JSONArray jsonArray=responseObject.optJSONArray("categories");
                        for(int i=0;i<jsonArray.length();i++){
                            categoryTagPOJOS.add(new Gson().fromJson(jsonArray.optJSONObject(i).toString(),CategoryTagPOJO.class));
                        }

                    } else {
                        ToastClass.showShortToast(getActivity(), "Something went wrong");
                    }
                    Log.d(TagUtils.getTag(), jsonObject.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                genreCategoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onErrorMsg(String status_code, String response) {
                dismissProgressBar();
                ToastClass.showShortToast(getActivity().getApplicationContext(),"Server Down");
            }
        }, "POST_GENRE").makeApiCall(WebServicesUrls.GET_GENRE_CATEGORIES, jsonObject);
    }

    GenreCategoryAdapter genreCategoryAdapter;

    public void attachGenreCategoryAdapter() {
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getActivity().getApplicationContext());
        rv_genre_category.setLayoutManager(layoutManager);
        genreCategoryAdapter = new GenreCategoryAdapter(getActivity(), this, categoryTagPOJOS);
        layoutManager.setFlexDirection(FlexDirection.ROW);
        rv_genre_category.setAdapter(genreCategoryAdapter);
    }

    public void continueBtnEnabling(){
        boolean is_active=false;
        for(CategoryTagPOJO categoryTagPOJO:categoryTagPOJOS){
            if(categoryTagPOJO.isActive()){
                is_active=true;
            }
        }

        if(is_active){
            iv_continue.setImageResource(R.drawable.ic_continue_btn_enabled);
        }else{
            iv_continue.setImageResource(R.drawable.ic_continue_btn_disabled);
        }
    }

    public void postGenre() {

        JSONObject jsonObject = new JSONObject();

        showProgressBar();

        try {
            String category_ids="";

            List<CategoryTagPOJO> activeCategoryTagPOJOS=new ArrayList<>();

            for(int i=0;i<categoryTagPOJOS.size();i++){
                if(categoryTagPOJOS.get(i).isActive()){
                    activeCategoryTagPOJOS.add(categoryTagPOJOS.get(i));
                }
            }

            for(int i=0;i<activeCategoryTagPOJOS.size();i++){
                if((i+1)==activeCategoryTagPOJOS.size()){
                    category_ids+=activeCategoryTagPOJOS.get(i).getCatId();
                }else{
                    category_ids+=activeCategoryTagPOJOS.get(i).getCatId()+",";
                }
            }

            jsonObject.put("userId", Pref.GetStringPref(getActivity().getApplicationContext(),StringUtils.USER_ID,""));
            jsonObject.put("deviceId", "91");
            jsonObject.put("catId", category_ids);
            jsonObject.put("langCode", "en");

//            tv_tags.setText(category_ids);
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
                        if(getActivity() instanceof LoginActivity){
                            Pref.SetBooleanPref(getActivity().getApplicationContext(), StringUtils.LOGIN_TAG,true);
                            LoginActivity loginActivity= (LoginActivity) getActivity();
                            loginActivity.startHome();
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
        }, "POST_GENRE").makeApiCall(WebServicesUrls.POST_GENRE, jsonObject);
    }
}
