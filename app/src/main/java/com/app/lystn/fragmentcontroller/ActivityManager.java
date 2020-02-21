package com.app.lystn.fragmentcontroller;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.app.lystn.progress.ProgressHUD;

import java.util.ArrayList;
import java.util.List;

public class ActivityManager extends AppCompatActivity {
    public List<Fragment> fragmentList = new ArrayList<>();
    ProgressHUD mProgressHUD;
    public void startFragment(int id, Fragment fragment) {
        fragmentList.add(fragment);
        getSupportFragmentManager().beginTransaction()
                .add(id, fragment)
                .addToBackStack(fragment.getClass().getSimpleName())
                .commit();
    }

    //
    public void startFragmentForResult(int id, Fragment startingFragment, Fragment targetFragment, int requestCode) {
        fragmentList.add(targetFragment);
        getSupportFragmentManager().beginTransaction()
                .add(id, targetFragment)
                .addToBackStack(targetFragment.getClass().getSimpleName())
                .commit();

        FragmentController fragmentController = (FragmentController) targetFragment;
        fragmentController.setUpStartingFragment(startingFragment, requestCode);
    }

    public void popFragment() {
        fragmentList.remove(fragmentList.size() - 1);
        FragmentManager manager = getSupportFragmentManager();
        manager.popBackStack();
    }

    public void popBackResultFragment(Fragment startingFragment, int requestCode, int resultCode, Bundle data) {
        fragmentList.remove(fragmentList.size() - 1);
        FragmentManager manager = getSupportFragmentManager();
        manager.popBackStack();

        if (startingFragment != null) {
            FragmentController fragmentController = (FragmentController) startingFragment;
            fragmentController.onFragmentResult(requestCode, resultCode, data);
        }
    }

    public void clearAllFragments() {
        for (Fragment fragment : fragmentList) {
            FragmentManager manager = getSupportFragmentManager();
            manager.popBackStack();
        }

        fragmentList.clear();
    }

    public void focusKeyboard(EditText editText) {
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    public void hideKeyboard(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void showProgressBar(){
        try {
            if (mProgressHUD == null) {
                mProgressHUD = ProgressHUD.show(this, "", true, true, null);
            } else {
                mProgressHUD.show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void dismissProgressBar(){
        if(mProgressHUD!=null){
            mProgressHUD.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        if(fragmentList.size()>0) {
            fragmentList.remove(fragmentList.size() - 1);
        }
        super.onBackPressed();
//        Log.d(TagUtils.getTag(),"fragments size:-"+fragmentList.size());
    }
}
