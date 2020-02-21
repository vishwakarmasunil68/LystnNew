package com.app.lystn.fragment.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.lystn.R;
import com.app.lystn.activity.LoginActivity;
import com.app.lystn.adapter.LanguageSelectAdapter;
import com.app.lystn.fragmentcontroller.FragmentController;
import com.app.lystn.pojo.LanguagePOJO;
import com.app.lystn.util.Pref;
import com.app.lystn.util.StringUtils;
import com.app.lystn.util.ToastClass;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class LoginSelectLanguageFragment extends FragmentController {

    @BindView(R.id.rv_select_language)
    RecyclerView rv_select_language;
    @BindView(R.id.iv_continue)
    ImageView iv_continue;
    @BindView(R.id.tv_skip)
    TextView tv_skip;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.frag_login_select_language,container,false);
        setUpView(getActivity(),this,view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        languagePOJOS.add(new LanguagePOJO(R.drawable.icon_country_usa,"English",false,"en"));
        languagePOJOS.add(new LanguagePOJO(R.drawable.icon_country_india,"Hindi",false,"hi"));
        languagePOJOS.add(new LanguagePOJO(R.drawable.icon_country_fran,"Francis",false,"fr"));
        languagePOJOS.add(new LanguagePOJO(R.drawable.icon_country_china,"Chinese",false,"ch"));
        languagePOJOS.add(new LanguagePOJO(R.drawable.icon_country_vie,"Vitenamese",false,"vi"));
        languagePOJOS.add(new LanguagePOJO(R.drawable.icon_country_japan,"Japenese",false,"ja"));
        languagePOJOS.add(new LanguagePOJO(R.drawable.icon_country_deutsch,"Deutsch",false,"dtch"));

        attachAdapter();

        tv_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity() instanceof LoginActivity){
                    Pref.SetBooleanPref(getActivity().getApplicationContext(), StringUtils.LOGIN_LANGUAGE,true);
                    LoginActivity loginActivity= (LoginActivity) getActivity();
                    loginActivity.startLoginDefaultFragment();
                }
            }
        });

        iv_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkSelected()==-1){
                    ToastClass.showShortToast(getActivity().getApplicationContext(),"Please Select Language");
                }else{
//                    Pref.SetStringPref(getActivity(), StringUtils.USER_PREFER_LANGUAGE,languagePOJOS.get(checkSelected()).getLanguage_code());
                    Pref.SetStringPref(getActivity(), StringUtils.USER_PREFER_LANGUAGE,"en");
                    Pref.SetBooleanPref(getActivity().getApplicationContext(), StringUtils.LOGIN_LANGUAGE,true);
                    if(getActivity() instanceof LoginActivity){
                        LoginActivity loginActivity= (LoginActivity) getActivity();
                        loginActivity.startLoginDefaultFragment();
                    }
                }
            }
        });
    }

    private int checkSelected(){
        for(int i=0;i<languagePOJOS.size();i++){
            if(languagePOJOS.get(i).isSelected()){
                return i;
            }
        }
        return -1;
    }

    public void checkContinueVisibility(){
        boolean is_selected=false;
        for(LanguagePOJO languagePOJO:languagePOJOS){
            if(languagePOJO.isSelected()){
                is_selected=true;
            }
        }
        if(is_selected){
            iv_continue.setImageResource(R.drawable.ic_continue_btn_enabled);
        }else{
            iv_continue.setImageResource(R.drawable.ic_continue_btn_disabled);
        }
    }

    LanguageSelectAdapter languageSelectAdapter;
    List<LanguagePOJO> languagePOJOS = new ArrayList<>();

    public void attachAdapter() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv_select_language.setHasFixedSize(true);
        rv_select_language.setLayoutManager(linearLayoutManager);
        languageSelectAdapter = new LanguageSelectAdapter(getActivity(), this, languagePOJOS);
        rv_select_language.setAdapter(languageSelectAdapter);
        rv_select_language.setNestedScrollingEnabled(false);
        rv_select_language.setItemAnimator(new DefaultItemAnimator());
    }
}
