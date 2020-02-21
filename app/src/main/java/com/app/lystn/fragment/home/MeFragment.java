package com.app.lystn.fragment.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.app.lystn.R;
import com.app.lystn.activity.HomeActivity;
import com.app.lystn.activity.LoginActivity;
import com.app.lystn.fragmentcontroller.FragmentController;
import com.app.lystn.pojo.UserPOJO;
import com.app.lystn.util.Pref;
import com.app.lystn.util.StringUtils;
import com.app.lystn.util.TagUtils;
import com.app.lystn.util.UtilityFunction;

import butterknife.BindView;
import p32929.androideasysql_library.EasyDB;

public class MeFragment extends FragmentController {

    @BindView(R.id.iv_subscription)
    ImageView iv_subscription;
    @BindView(R.id.ll_subscription)
    LinearLayout ll_subscription;
    @BindView(R.id.ll_language)
    LinearLayout ll_language;
    @BindView(R.id.tv_user_name)
    TextView tv_user_name;
    @BindView(R.id.ll_logout)
    LinearLayout ll_logout;
    @BindView(R.id.ll_profile)
    LinearLayout ll_profile;
    @BindView(R.id.iv_profile)
    ImageView iv_profile;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_me, container, false);
        setUpView(getActivity(), this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        iv_subscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() instanceof HomeActivity) {
                    HomeActivity homeActivity = (HomeActivity) getActivity();
                    homeActivity.showUnLockPremiumFragment();
                }
            }
        });

        ll_subscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() instanceof HomeActivity) {
                    HomeActivity homeActivity = (HomeActivity) getActivity();
                    homeActivity.startManageSubscription();
                }
            }
        });

        ll_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() instanceof HomeActivity) {
                    HomeActivity homeActivity = (HomeActivity) getActivity();
                    homeActivity.startSelectLangageFragment();
                }
            }
        });



        ll_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clearSongDB();

                Pref.SetStringPref(getActivity().getApplicationContext(), StringUtils.ARTISTE_FOLLOW_UP_STRING, "");
                Pref.SetStringPref(getActivity().getApplicationContext(), StringUtils.GENRE_FOLLOW_UP_STRING, "");
                Pref.SetBooleanPref(getActivity().getApplicationContext(), StringUtils.IS_USER_PROFILE_LOADED, false);
                Pref.SetBooleanPref(getActivity().getApplicationContext(), StringUtils.IS_LOGIN, false);
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finishAffinity();
            }
        });

        ll_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity() instanceof HomeActivity){
                    HomeActivity homeActivity= (HomeActivity) getActivity();
                    homeActivity.startProfileFragment();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        setProfile();
    }

    public void setProfile(){
        Log.d(TagUtils.getTag(),"profile set");
        try {
            UserPOJO userPOJO = UtilityFunction.getUserPOJO(getActivity().getApplicationContext());
            if (userPOJO != null) {
                if(userPOJO.getEmail()!=null&&userPOJO.getEmail().length()>0){
                    tv_user_name.setText(userPOJO.getEmail());
                }else{
                    tv_user_name.setText(userPOJO.getMobileNo());
                }
                if(userPOJO.getProfileImgUrl()!=null
                        &&userPOJO.getProfileImgUrl().length()>0){
                    Glide.with(getActivity())
                            .load(userPOJO.getProfileImgUrl())
                            .placeholder(R.drawable.ic_user_default)
                            .error(R.drawable.ic_user_default)
                            .dontAnimate()
                            .into(iv_profile);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearSongDB() {
        EasyDB easyDB = EasyDB.init(getActivity(), "Lystn").setTableName("downloads");
        easyDB.deleteAllDataFromTable();


        Pref.SetBooleanPref(getActivity().getApplicationContext(),StringUtils.LOGIN_TAG,false);
        Pref.SetBooleanPref(getActivity().getApplicationContext(),StringUtils.LOGIN_MAIN,false);
        Pref.SetBooleanPref(getActivity().getApplicationContext(),StringUtils.LOGIN_LANGUAGE,false);
        Pref.SetBooleanPref(getActivity().getApplicationContext(),StringUtils.WALKTHORUGH_SKIPPED,false);

    }


}
