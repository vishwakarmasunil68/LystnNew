package com.app.lystn.fragmentcontroller;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.fragment.app.Fragment;

import com.app.lystn.progress.ProgressHUD;

import butterknife.ButterKnife;


public abstract class FragmentController extends Fragment {
    ProgressHUD mProgressHUD;
    public ActivityManager activityManager;
    public Fragment startingFragment;
    public int requestCode;


    public void setUpStartingFragment(Fragment startingFragment, int requestCode) {
        this.startingFragment = startingFragment;
        this.requestCode = requestCode;
    }

    public void setUpView(Activity activity, final Fragment fragment, View view) {
        activityManager = (ActivityManager) activity;
        ButterKnife.bind(fragment, view);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                Log.i(FragmentController.class.getSimpleName(), "keyCode: " + keyCode);
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
//                    Log.i(FragmentController.class.getSimpleName(), "onKey Back listener is working!!!");
                    FragmentController fragmentController = (FragmentController) fragment;
                    fragmentController.onBackPressed();
                    return true;
                }
                return false;
            }
        });
        hideKeyboard(view);
    }

    public void onBackPressed() {
        activityManager.onBackPressed();
    }

    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {

    }

    public void hideKeyboard(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activityManager.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void showProgressBar() {
        try {
            if (mProgressHUD == null) {
                mProgressHUD = ProgressHUD.show(getActivity(), "", true, true, null);
            } else {
                mProgressHUD.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dismissProgressBar() {
        if (mProgressHUD != null) {
            mProgressHUD.dismiss();
        }
    }
}
