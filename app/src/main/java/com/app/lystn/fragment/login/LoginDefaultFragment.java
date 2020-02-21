package com.app.lystn.fragment.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.app.lystn.R;
import com.app.lystn.activity.LoginActivity;
import com.app.lystn.fragmentcontroller.FragmentController;
import com.app.lystn.util.Pref;
import com.app.lystn.util.StringUtils;
import com.app.lystn.util.TagUtils;
import com.app.lystn.util.ToastClass;
import com.app.lystn.webservice.ApiCallBase;
import com.app.lystn.webservice.WebServicesCallBack;
import com.app.lystn.webservice.WebServicesUrls;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;

public class LoginDefaultFragment extends FragmentController implements GoogleApiClient.OnConnectionFailedListener {

    @BindView(R.id.iv_login_mobile)
    ImageView iv_login_mobile;
    @BindView(R.id.tv_skip)
    TextView tv_skip;
    @BindView(R.id.ll_facebook)
    LinearLayout ll_facebook;
    @BindView(R.id.ll_google)
    LinearLayout ll_google;

    SignInButton signInButton;
    private GoogleApiClient googleApiClient;
    private static final int RC_SIGN_IN = 1;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_login_default, container, false);
        setUpView(getActivity(), this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        iv_login_mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() instanceof LoginActivity) {
                    LoginActivity loginActivity = (LoginActivity) getActivity();
                    loginActivity.startLoginWithPhoneFragment();
                }
            }
        });

        tv_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (getActivity() instanceof LoginActivity) {
//                    LoginActivity loginActivity = (LoginActivity) getActivity();
//                    loginActivity.skipFragment();
//                }
                if (getActivity() instanceof LoginActivity) {
                    Pref.SetBooleanPref(getActivity().getApplicationContext(), StringUtils.LOGIN_MAIN,true);
                    LoginActivity loginActivity = (LoginActivity) getActivity();
                    loginActivity.startLoginTagFragment();
//                            loginActivity.startHome();
                }
            }
        });

        ll_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() instanceof LoginActivity) {
                    LoginActivity loginActivity = (LoginActivity) getActivity();
                    loginActivity.callFacebookAPI();
                }
            }
        });

        ll_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent, RC_SIGN_IN);
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity(), this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }

    public void receiveFacebookData(JSONObject jsonObject) {
        Log.d(TagUtils.getTag(), "facebook data:-" + jsonObject.toString());
        try {
            String id = jsonObject.optString("id");
            String name = jsonObject.optString("name");
            String email = jsonObject.optString("email");

            postSocialProfile(
                    id,
                    "FB",
                    Pref.GetStringPref(getActivity().getApplicationContext(), StringUtils.DEVICE_ID, ""),
                    "IN",
                    "en",
                    "https://www.facebook.com/" + id,
                    email,
                    "FB_source"
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        ToastClass.showShortToast(getActivity().getApplicationContext(), "Connection Failed");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            Log.d(TagUtils.getTag(), "Name:-" + account.getDisplayName());
            Log.d(TagUtils.getTag(), "id:-" + account.getId());
            Log.d(TagUtils.getTag(), "token:-" + account.getIdToken());
            Log.d(TagUtils.getTag(), "Email:-" + account.getEmail());
            Log.d(TagUtils.getTag(), "profile_pic:-" + account.getPhotoUrl());
            Log.d(TagUtils.getTag(), "profileurl:-" + "https://plus.google.com/" + account.getId());


            postSocialProfile(
                    account.getIdToken(),
                    "Google",
                    Pref.GetStringPref(getActivity().getApplicationContext(), StringUtils.DEVICE_ID, ""),
                    "IN",
                    "en",
                    "https://plus.google.com/" + account.getId(),
                    account.getEmail(),
                    "Google_source"
            );

        } else {
            Toast.makeText(getActivity().getApplicationContext(), "Sign in cancel", Toast.LENGTH_LONG).show();
        }
    }

    public void postSocialProfile(String access_token, String login_medium, String device_id, String country, String language,
                                  String social_profile_url, String email, String signup_source) {

        JSONObject jsonObject = new JSONObject();

        showProgressBar();

        try {
            jsonObject.put("accessToken", access_token);
            jsonObject.put("loginMedium", login_medium);
            jsonObject.put("deviceID", device_id);
            jsonObject.put("country", country);
            jsonObject.put("langCode", language);
            jsonObject.put("socialProfileURL", social_profile_url);
            jsonObject.put("loginMedium", social_profile_url);
            jsonObject.put("email", email);
            jsonObject.put("signup_source", "androidapp");
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
                        JSONObject profileJSON = responseObject.optJSONObject("profile");
                        Pref.SetStringPref(getActivity().getApplicationContext(), StringUtils.PROFILE_DATA, profileJSON.toString());
                        Pref.SetStringPref(getActivity().getApplicationContext(), StringUtils.USER_ID, profileJSON.optString("userId"));
                        Pref.SetBooleanPref(getActivity().getApplicationContext(), StringUtils.IS_LOGIN, true);
                        if (getActivity() instanceof LoginActivity) {
                            Pref.SetBooleanPref(getActivity().getApplicationContext(), StringUtils.LOGIN_MAIN,true);
                            LoginActivity loginActivity = (LoginActivity) getActivity();
                            loginActivity.startLoginTagFragment();
//                            loginActivity.startHome();
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
        }, "STORE_SOCIAL_PROFILE").makeApiCall(WebServicesUrls.STORE_SOCIAL_PROFILE, jsonObject);
    }
}
