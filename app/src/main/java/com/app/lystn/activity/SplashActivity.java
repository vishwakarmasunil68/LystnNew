package com.app.lystn.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.app.lystn.R;
import com.app.lystn.util.Pref;
import com.app.lystn.util.StringUtils;
import com.app.lystn.util.TagUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decor = getWindow().getDecorView();
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.activity_splash);

        new CountDownTimer(3000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
//                Crashlytics.getInstance().crash();
                if (Pref.GetBooleanPref(getApplicationContext(), StringUtils.IS_LOGIN, false)) {
                    startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                    finish();
                } else {
                    Log.d(TagUtils.getTag(), "tag:-" + Pref.GetBooleanPref(getApplicationContext(), StringUtils.LOGIN_TAG, false));
                    Log.d(TagUtils.getTag(), "main:-" + Pref.GetBooleanPref(getApplicationContext(), StringUtils.LOGIN_MAIN, false));
                    Log.d(TagUtils.getTag(), "language:-" + Pref.GetBooleanPref(getApplicationContext(), StringUtils.LOGIN_LANGUAGE, false));
                    Log.d(TagUtils.getTag(), "walkthrough:-" + Pref.GetBooleanPref(getApplicationContext(), StringUtils.WALKTHORUGH_SKIPPED, false));
                    if (Pref.GetBooleanPref(getApplicationContext(), StringUtils.LOGIN_TAG, false)) {
                        startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                        finish();
                    } else if (Pref.GetBooleanPref(getApplicationContext(), StringUtils.LOGIN_MAIN, false)
                            || Pref.GetBooleanPref(getApplicationContext(), StringUtils.LOGIN_LANGUAGE, false)
                            || Pref.GetBooleanPref(getApplicationContext(), StringUtils.WALKTHORUGH_SKIPPED, false)) {
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                        finish();
                    } else {
                        startActivity(new Intent(SplashActivity.this, WelcomeActivity.class));
                        finish();
                    }


                }

            }
        }.start();

        getPackageInfo();
    }

    public void getPackageInfo() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.sundroid.lystn",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
//                Log.d(TagUtils.getTag(),"Keyhash:-"+ Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }
}
