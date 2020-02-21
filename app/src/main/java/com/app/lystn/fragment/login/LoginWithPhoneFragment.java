package com.app.lystn.fragment.login;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hbb20.CountryCodePicker;
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

public class LoginWithPhoneFragment extends FragmentController {

    @BindView(R.id.iv_login_mobile)
    ImageView iv_login_mobile;
    @BindView(R.id.tv_skip)
    TextView tv_skip;
    @BindView(R.id.tv_get_otp)
    TextView tv_get_otp;
    @BindView(R.id.et_phone_no)
    EditText et_phone_no;
    @BindView(R.id.et_verification)
    EditText et_verification;
    @BindView(R.id.ccp)
    CountryCodePicker ccp;

    boolean otp_verified = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_login_phone, container, false);
        setUpView(getActivity(), this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        iv_login_mobile.setEnabled(true);
        iv_login_mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_phone_no.getText().toString().length()>=10){
                    if(et_verification.getText().toString().length()==4){
                        callOtpVerifiedAPI();
                    }else{
                        ToastClass.showShortToast(getActivity().getApplicationContext(),getResources().getString(R.string.enter_otp));
                    }
                }else{
                    ToastClass.showShortToast(getActivity().getApplicationContext(),getResources().getString(R.string.enter_mobile));
                }

            }
        });

        tv_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() instanceof LoginActivity) {
                    Pref.SetBooleanPref(getActivity().getApplicationContext(),StringUtils.LOGIN_MAIN,true);
                    LoginActivity loginActivity = (LoginActivity) getActivity();
                    loginActivity.startLoginTagFragment();
                }
            }
        });

        tv_get_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_phone_no.getText().toString().length() != 10) {
                    ToastClass.showShortToast(getActivity().getApplicationContext(), getString(R.string.enter_mobile));
                } else {
                    callGetOTPAPI(ccp.getSelectedCountryCode(),
                            et_phone_no.getText().toString(),
                            Pref.GetStringPref(getActivity().getApplicationContext(), StringUtils.DEVICE_ID, ""),
                            "en");
                }
            }
        });

        et_verification.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (et_verification.getText().toString().length() == 4) {
                    iv_login_mobile.setEnabled(true);
                    iv_login_mobile.setImageResource(R.drawable.ic_signin_num_enabled);
                } else {
                    iv_login_mobile.setEnabled(false);
                    iv_login_mobile.setImageResource(R.drawable.ic_signin_num_disabled);
                }
            }
        });
        et_phone_no.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (et_phone_no.getText().toString().length() >= 10) {
                    tv_get_otp.setEnabled(true);
                    tv_get_otp.setBackgroundResource(R.drawable.ic_get_activated);
                } else {
                    tv_get_otp.setEnabled(false);
                    tv_get_otp.setBackgroundResource(R.drawable.ic_get_deactivated);
                }
            }
        });
    }

    public void callGetOTPAPI(String isdCode, String mobileNo, String deviceId, String langCode) {
        JSONObject jsonObject = new JSONObject();

        showProgressBar();

        try {
            jsonObject.put("isdCode", isdCode);
            jsonObject.put("mobileNo", mobileNo);
            jsonObject.put("deviceId", deviceId);
            jsonObject.put("langCode", langCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new ApiCallBase(getActivity(), new WebServicesCallBack() {
            @Override
            public void onGetMsg(String apicall, String response) {
                dismissProgressBar();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject responseObject = jsonObject.optJSONObject("response");
                    if (responseObject.optBoolean("status")) {
                        et_verification.setEnabled(true);
                        makeCounter();
//                        tv_get_otp.setBackgroundResource(R.drawable.ic_get_activated);
                    } else {
                        et_verification.setEnabled(false);
//                        tv_get_otp.setBackgroundResource(R.drawable.ic_get_deactivated);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onErrorMsg(String status_code, String response) {
                dismissProgressBar();
                ToastClass.showShortToast(getActivity().getApplicationContext(),"Server Down");
            }
        }, "GET_OTP").makeApiCall(WebServicesUrls.GET_OTP, jsonObject);
    }

    public void makeCounter() {
        new CountDownTimer(120000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
//                Log.d(TagUtils.getTag(),"millis:-"+millisUntilFinished);
                tv_get_otp.setText(((int) (millisUntilFinished / 1000)) + "");
                tv_get_otp.setEnabled(false);
                tv_get_otp.setBackgroundResource(R.drawable.ic_get_deactivated);
            }

            @Override
            public void onFinish() {
                tv_get_otp.setText("GET");
                tv_get_otp.setEnabled(true);
                tv_get_otp.setBackgroundResource(R.drawable.ic_get_activated);
            }
        }.start();
    }

    public void callOtpVerifiedAPI() {
        JSONObject jsonObject = new JSONObject();

        showProgressBar();

        try {
            jsonObject.put("otp", et_verification.getText().toString());
            jsonObject.put("isdCode", "91");
            jsonObject.put("mobileNo", et_phone_no.getText().toString());
            jsonObject.put("deviceId", Pref.GetStringPref(getActivity().getApplicationContext(), StringUtils.DEVICE_ID, ""));
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
                        JSONObject profileJSON = responseObject.optJSONObject("profile");
                        Pref.SetStringPref(getActivity().getApplicationContext(), StringUtils.PROFILE_DATA, profileJSON.toString());
                        Pref.SetStringPref(getActivity().getApplicationContext(), StringUtils.USER_ID, profileJSON.optString("userId"));
                        Pref.SetBooleanPref(getActivity().getApplicationContext(), StringUtils.IS_LOGIN, true);
                        if (getActivity() instanceof LoginActivity) {
                            Pref.SetBooleanPref(getActivity().getApplicationContext(),StringUtils.LOGIN_MAIN,true);
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
        }, "GET_OTP").makeApiCall(WebServicesUrls.VALIDATE_OTP, jsonObject);
    }


}
