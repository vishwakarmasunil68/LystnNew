package com.app.lystn.activity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.app.lystn.R;
import com.app.lystn.fragmentcontroller.ActivityManager;

import butterknife.ButterKnife;

public class LoginTagActivity extends ActivityManager {

//    @BindView(R.id.grid)
//    GridView grid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decor = getWindow().getDecorView();
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.activity_login_tag);
        ButterKnife.bind(this);

//        mGeoloTagGroup.setTags("111", "222", "333", "444", "555", "666", "777", "888", "999");
//        mGeoloTagGroup.setOnTagChangeListener(new GeoloTagGroup.OnTagChangeListener() {
//            @Override
//            public void onAppend(GeoloTagGroup geoloTagGroup, String tag) {
//                Log.d(TagUtils.getTag(), "onAppend() -- tag:" + tag);
//            }
//
//            @Override
//            public void onDelete(GeoloTagGroup geoloTagGroup, String tag) {
//                Log.d(TagUtils.getTag(), "onDelete() -- tag:" + tag);
//            }
//
//            @Override
//            public void onCheckedChanged(GeoloTagGroup geoloTagGroup, String tagStr, boolean isChecked) {
//                Log.d(TagUtils.getTag(), "onCheckedChanged() -- tagStr:" + tagStr);
//            }
//        });
//
//        mGeoloTagGroup.setOnTagClickListener(new GeoloTagGroup.OnTagClickListener() {
//            @Override
//            public void onTagClick(String tag) {
//                Log.d(TagUtils.getTag(), "onTagClick() -- tag:" + tag);
//            }
//        });

//        String[] web=new String[]{"abcdefs","abcdefs","abcdefs","abcdefs","abcdefs","abcdefs","abcdefs","abcdefs","abcdefs","abcdefs","abcdefs","abcdefs","abcdefs","abcdefs","abcdefs","abcdefs","abcdefs","abcdefs","abcdefs","abcdefs"};
//        CustomGrid adapter = new CustomGrid(LoginTagActivity.this, web);
//        grid=(GridView)findViewById(R.id.grid);
//        grid.setAdapter(adapter);
//        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//                Toast.makeText(LoginTagActivity.this, "You Clicked at " +web[+ position], Toast.LENGTH_SHORT).show();
//
//            }
//        });

    }
}
