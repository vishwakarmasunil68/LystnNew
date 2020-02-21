package com.app.lystn.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.an.deviceinfo.device.model.Device;
import com.an.deviceinfo.device.model.Network;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.app.lystn.R;
import com.app.lystn.fragment.login.LoginDefaultFragment;
import com.app.lystn.fragment.login.LoginSelectLanguageFragment;
import com.app.lystn.fragment.login.LoginTagFragment;
import com.app.lystn.fragment.login.LoginWithPhoneFragment;
import com.app.lystn.fragmentcontroller.ActivityManager;
import com.app.lystn.util.Pref;
import com.app.lystn.util.StringUtils;
import com.app.lystn.util.TagUtils;
import com.app.lystn.util.ToastClass;

import org.json.JSONObject;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Enumeration;

import butterknife.ButterKnife;

public class LoginActivity extends ActivityManager {

//    @BindView(R.id.iv_login_mobile)
//    ImageView iv_login_mobile;


    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decor = getWindow().getDecorView();
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        callbackManager = CallbackManager.Factory.create();

//        iv_login_mobile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(LoginActivity.this,LoginMobileActivity.class));
//            }
//        });

        Log.d(TagUtils.getTag(),"Login activity");
        if(Pref.GetBooleanPref(getApplicationContext(), StringUtils.LOGIN_MAIN,false)){
            startLoginTagFragment();
        }else if(Pref.GetBooleanPref(getApplicationContext(), StringUtils.LOGIN_LANGUAGE,false)){
            startLoginDefaultFragment();
        }else{
            startSelectLangageFragment();
        }


//        startLoginTagFragment();

        getAllPermissions();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        Log.d(TagUtils.getTag(), "login success");
                        callGraphAPI(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        // App code
                        Log.d(TagUtils.getTag(), "login cancel");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        Log.d(TagUtils.getTag(), "login exception:-"+exception.toString());
                    }
                });

    }

    public void callFacebookAPI() {
        LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile"));
    }

    public void callGraphAPI(AccessToken accessToken) {
        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        // Application code

                        if (object != null) {
                            Log.d(TagUtils.getTag(), "json data:-" + object.toString());
                            if(loginDefaultFragment!=null){
                                loginDefaultFragment.receiveFacebookData(object);
                            }
                        } else {
                            Log.d(TagUtils.getTag(), "null object received");
                            ToastClass.showShortToast(getApplicationContext(), "Facebook Login Failed");
                        }

//                        Log.d(TagUtils.getTag(),"graph Response:-"+response.ge)

                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,email");
        request.setParameters(parameters);
        request.executeAsync();
    }

//    @Override
//    public void onBackPressed() {
//        if(fragmentList.size()==0){
//
//        }
//        super.onBackPressed();
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void getAllPermissions() {
        String[] permissions = {
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.ACCESS_NETWORK_STATE

        };
        Permissions.check(this/*context*/, permissions, null/*rationale*/, null/*options*/, new PermissionHandler() {
            @Override
            public void onGranted() {
                // do your task.

                try {
                    Device device = new Device(LoginActivity.this);
                    Network network = new Network(LoginActivity.this);

                    Pref.SetStringPref(getApplicationContext(), StringUtils.DEVICE_MODEL, device.getModel());
                    Pref.SetStringPref(getApplicationContext(), StringUtils.DEVICE_BRAND, device.getBuildBrand());
                    Pref.SetStringPref(getApplicationContext(), StringUtils.DEVICE_ID, network.getIMSI());
                    Pref.SetStringPref(getApplicationContext(), StringUtils.BUILD_NUMBER, device.getBuildVersionCodeName());
                    Pref.SetStringPref(getApplicationContext(), StringUtils.VERSION_NO, "");
                    Pref.SetStringPref(getApplicationContext(), StringUtils.DEVICE_OS, device.getOsVersion());
                    Pref.SetStringPref(getApplicationContext(), StringUtils.IP_ADDRESS, getLocalIpAddress());
                }catch (Exception e){
                    e.printStackTrace();
                }


//                Log.d(TagUtils.getTag(),"model:-"+device.getModel());
//                Log.d(TagUtils.getTag(),"brand:-"+device.getBuildBrand());
//
//                Log.d(TagUtils.getTag(),"imei:-"+network.getIMEI());
//                Log.d(TagUtils.getTag(),"imsi:-"+network.getIMSI());
//                Log.d(TagUtils.getTag(),"osversion:-"+device.getOsVersion());
//                Log.d(TagUtils.getTag(),"build version:-"+device.getReleaseBuildVersion());
//                Log.d(TagUtils.getTag(),"build version code:-"+device.getBuildVersionCodeName());
//                Log.d(TagUtils.getTag(),"local ip:-"+getLocalIpAddress());
            }
        });
    }

    public String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String ip = Formatter.formatIpAddress(inetAddress.hashCode());
                        Log.i(TagUtils.getTag(), "***** IP=" + ip);
                        return ip;
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e(TagUtils.getTag(), ex.toString());
        }
        return null;
    }

    public void startSelectLangageFragment() {
        startFragment(R.id.frame_base, new LoginSelectLanguageFragment());
    }

    LoginDefaultFragment loginDefaultFragment;

    public void startLoginDefaultFragment() {
        startFragment(R.id.frame_base, loginDefaultFragment = new LoginDefaultFragment());
    }

    public void startLoginWithPhoneFragment() {
        startFragment(R.id.frame_base, new LoginWithPhoneFragment());
    }

    public void startLoginTagFragment() {
        startFragment(R.id.frame_base, new LoginTagFragment());
    }

    public void startHome() {
        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
    }

    public void skipFragment() {
        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
    }
}
