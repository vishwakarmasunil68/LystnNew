package com.app.lystn.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.viewpager.widget.ViewPager;

import com.app.lystn.R;
import com.app.lystn.adapter.ViewPagerAdapter;
import com.app.lystn.fragment.Welcome1Fragment;
import com.app.lystn.fragment.Welcome2Fragment;
import com.app.lystn.fragment.Welcome3Fragment;
import com.app.lystn.fragmentcontroller.ActivityManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WelcomeActivity extends ActivityManager {

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    int page_selected=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decor = getWindow().getDecorView();
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        setupViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new Welcome1Fragment(), "Monitor");
        adapter.addFrag(new Welcome2Fragment(), "Monitor");
        adapter.addFrag(new Welcome3Fragment(), "Monitor");
//        adapter.addFrag(new Welcome2Fragment(), "Device");

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(adapter.getCount());


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                page_selected=position;
//                switch (position) {
//                    case 0:
//                        iv_slider_1.setImageResource(R.drawable.ic_slider_filled);
//                        iv_slider_2.setImageResource(R.drawable.ic_slider_unfilled);
//                        break;
//                    case 1:
//                        iv_slider_1.setImageResource(R.drawable.ic_slider_unfilled);
//                        iv_slider_2.setImageResource(R.drawable.ic_slider_filled);
//                        break;
//                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    public void showNext(){
        if(viewPager.getCurrentItem()!=2){
            viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
        }else{
            startActivity(new Intent(WelcomeActivity.this,LoginActivity.class));
            finish();
        }
    }

    public void skipToLogin() {
        startActivity(new Intent(WelcomeActivity.this,LoginActivity.class));
        finish();
    }
}
