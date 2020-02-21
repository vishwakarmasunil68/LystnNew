package com.app.lystn.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.app.lystn.R;
import com.app.lystn.fragmentcontroller.ActivityManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginMobileActivity extends ActivityManager {

    @BindView(R.id.iv_login_mobile)
    ImageView iv_login_mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decor = getWindow().getDecorView();
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.activity_login_mobile);
        ButterKnife.bind(this);

        iv_login_mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginMobileActivity.this,LoginTagActivity.class));
            }
        });
    }
}
