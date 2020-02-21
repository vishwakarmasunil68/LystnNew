package com.app.lystn.fragment.profile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.app.lystn.R;
import com.app.lystn.activity.HomeActivity;
import com.app.lystn.activity.LoginActivity;
import com.app.lystn.fragmentcontroller.FragmentController;
import com.app.lystn.util.Pref;
import com.app.lystn.util.StringUtils;
import com.app.lystn.util.TagUtils;
import com.app.lystn.util.ToastClass;
import com.app.lystn.util.UtilityFunction;
import com.app.lystn.webservice.ApiCallBase;
import com.app.lystn.webservice.WebServicesCallBack;
import com.app.lystn.webservice.WebServicesUrls;
import com.app.lystn.webservice.WebUploadService;

import net.alhazmy13.mediapicker.Image.ImagePicker;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import p32929.androideasysql_library.EasyDB;

public class ProfileFragment extends FragmentController {

    @BindView(R.id.tv_first_name)
    EditText tv_first_name;
    @BindView(R.id.tv_last_name)
    EditText tv_last_name;
    @BindView(R.id.tv_email)
    EditText tv_email;
    @BindView(R.id.tv_mobile_num)
    EditText tv_mobile_num;
    @BindView(R.id.tv_gender)
    TextView tv_gender;
    @BindView(R.id.tv_profile_name)
    TextView tv_profile_name;
    @BindView(R.id.tv_profile_email)
    TextView tv_profile_email;
    @BindView(R.id.iv_logout)
    ImageView iv_logout;
    @BindView(R.id.iv_save)
    ImageView iv_save;
    @BindView(R.id.frame_profile)
    FrameLayout frame_profile;
    @BindView(R.id.ll_gender)
    LinearLayout ll_gender;
    @BindView(R.id.iv_back)
    ImageView iv_back;

    @BindView(R.id.iv_profile)
    ImageView iv_profile;

    String profileImageUrl = "";
    String socialProfile = "";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_profile, container, false);
        setUpView(getActivity(), this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        iv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUserProfile();
            }
        });

        iv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clearSongDB();

                Pref.SetStringPref(getActivity().getApplicationContext(), StringUtils.ARTISTE_FOLLOW_UP_STRING, "");
                Pref.SetStringPref(getActivity().getApplicationContext(), StringUtils.GENRE_FOLLOW_UP_STRING, "");
                Pref.SetBooleanPref(getActivity().getApplicationContext(), StringUtils.IS_USER_PROFILE_LOADED, false);
                Pref.SetBooleanPref(getActivity().getApplicationContext(), StringUtils.IS_LOGIN, false);
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finishAffinity();
            }
        });

        frame_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });

        ll_gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogNotificationAction();
            }
        });

        getUserProfile();

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    BottomSheetDialog mBottomDialogNotificationAction;

    private void showDialogNotificationAction() {
        try {
            View sheetView = getActivity().getLayoutInflater().inflate(R.layout.dialog_bottom_gender, null);
            mBottomDialogNotificationAction = new BottomSheetDialog(getActivity());
            mBottomDialogNotificationAction.setContentView(sheetView);
            mBottomDialogNotificationAction.show();

            LinearLayout ll_female = mBottomDialogNotificationAction.findViewById(R.id.ll_female);
            LinearLayout ll_male = mBottomDialogNotificationAction.findViewById(R.id.ll_male);

            ll_female.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tv_gender.setText("Female");
                    mBottomDialogNotificationAction.dismiss();
                }
            });

            ll_male.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tv_gender.setText("Male");
                    mBottomDialogNotificationAction.dismiss();
                }
            });

            // Remove default white color background
            FrameLayout bottomSheet = (FrameLayout) mBottomDialogNotificationAction.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            bottomSheet.setBackground(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openImagePicker() {
        new ImagePicker.Builder(getActivity())
                .mode(ImagePicker.Mode.CAMERA_AND_GALLERY)
                .compressLevel(ImagePicker.ComperesLevel.MEDIUM)
                .directory(ImagePicker.Directory.DEFAULT)
                .extension(ImagePicker.Extension.PNG)
                .allowMultipleImages(false)
                .enableDebuggingMode(true)
                .build();
    }

    String file_path = "";

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ImagePicker.IMAGE_PICKER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            List<String> mPaths = data.getStringArrayListExtra(ImagePicker.EXTRA_IMAGE_PATH);
            if (mPaths.size() > 0) {
                file_path = mPaths.get(0);
                Log.d(TagUtils.getTag(), "file path:-" + file_path);
                Glide.with(getActivity())
                        .load(mPaths.get(0))
                        .placeholder(R.drawable.ic_user_default)
                        .error(R.drawable.ic_user_default)
                        .dontAnimate()
                        .into(iv_profile);
                uploadImageOnServer(file_path);
            }
        }
    }

    private void uploadImageOnServer(String image_path) {
        if (new File(image_path).exists()) {
            try {
                MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                reqEntity.addPart("userId", new StringBody(Pref.GetStringPref(getActivity().getApplicationContext(), StringUtils.USER_ID, "")));
                reqEntity.addPart("file", new FileBody(new File(image_path)));

                Map<String, String> headers = new HashMap<>();
                headers.put("API-KEY", "0ccdc8d9ab1659b139b690498f03873c");

                activityManager.showProgressBar();
                new WebUploadService(reqEntity, getActivity(), headers, new WebServicesCallBack() {
                    @Override
                    public void onGetMsg(String apicall, String response) {
                        activityManager.dismissProgressBar();
                        try {
                            String object = new String(response);
                            JSONObject jsonObject = new JSONObject(object);
                            JSONObject responseObject = jsonObject.optJSONObject("response");
                            if (responseObject.optBoolean("status")) {
                                ToastClass.showShortToast(getActivity().getApplicationContext(), "Profile Pic Uploaded");
//                                uploadProfileData(responseObject);
                                JSONObject profileJSObject = responseObject.optJSONObject("profile");
                                profileImageUrl = profileJSObject.optString("profileImgUrl");
                                socialProfile = profileJSObject.optString("socialProfileUrl");

                            } else {
                                ToastClass.showShortToast(getActivity().getApplicationContext(), "Something went wrong");
                            }
                            Log.d(TagUtils.getTag(), jsonObject.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onErrorMsg(String status_code, String response) {
                        activityManager.dismissProgressBar();
                        ToastClass.showShortToast(getActivity().getApplicationContext(), "Something went wrong");
                    }
                }, "UPDATE PROFILE PIC", false).execute(WebServicesUrls.UPLOAD_IMAGE_URL);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void clearSongDB() {
        EasyDB easyDB = EasyDB.init(getActivity(), "Lystn").setTableName("downloads");
        easyDB.deleteAllDataFromTable();


        Pref.SetBooleanPref(getActivity().getApplicationContext(), StringUtils.LOGIN_TAG, false);
        Pref.SetBooleanPref(getActivity().getApplicationContext(), StringUtils.LOGIN_MAIN, false);
        Pref.SetBooleanPref(getActivity().getApplicationContext(), StringUtils.LOGIN_LANGUAGE, false);
        Pref.SetBooleanPref(getActivity().getApplicationContext(), StringUtils.WALKTHORUGH_SKIPPED, false);
    }

    public void getUserProfile() {
        JSONObject jsonObject = new JSONObject();

        showProgressBar();

        try {
            jsonObject.put("userId", Pref.GetStringPref(getActivity().getApplicationContext(), StringUtils.USER_ID, ""));
            jsonObject.put("deviceId", Pref.GetStringPref(getActivity().getApplicationContext(), StringUtils.DEVICE_ID, ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new ApiCallBase(getActivity(), new WebServicesCallBack() {
            @Override
            public void onGetMsg(String apicall, String response) {
                dismissProgressBar();
//                homeContentPOJOS.clear();
                try {
                    String object = new String(response);
                    JSONObject jsonObject = new JSONObject(object);
                    JSONObject responseObject = jsonObject.optJSONObject("response");
                    if (responseObject.optBoolean("status")) {

                        uploadProfileData(responseObject);
                        Pref.SetStringPref(getActivity().getApplicationContext(), StringUtils.PROFILE_DATA, responseObject.optJSONObject("profile").toString());


                    } else {
                        ToastClass.showShortToast(getActivity().getApplicationContext(), "Something went wrong");
                    }
                    Log.d(TagUtils.getTag(), jsonObject.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onErrorMsg(String status_code, String response) {
                dismissProgressBar();
                ToastClass.showShortToast(getActivity().getApplicationContext(), "Server Down");
            }
        }, "GET_PROFILE").makeApiCall(WebServicesUrls.GET_PROFILE, jsonObject);
    }


    public void uploadProfileData(JSONObject responseObject) {
        JSONObject profileJSObject = responseObject.optJSONObject("profile");

        Log.d(TagUtils.getTag(), "first name:-" + profileJSObject.optString("firstname"));

        String full_name = "";

        if (profileJSObject.optString("firstname") != null
                && !(profileJSObject.optString("firstname").equalsIgnoreCase("null"))) {
            tv_first_name.setText(profileJSObject.optString("firstname"));
            full_name = profileJSObject.optString("firstname");
        }
        if (profileJSObject.optString("lastname") != null
                && !(profileJSObject.optString("lastname").equalsIgnoreCase("null"))) {
            tv_last_name.setText(profileJSObject.optString("lastname"));
            full_name += " " + profileJSObject.optString("lastname");
        }

        tv_profile_name.setText(full_name);


        if (profileJSObject.optString("email") != null
                && !(profileJSObject.optString("email").equalsIgnoreCase("null"))) {
            tv_email.setText(profileJSObject.optString("email"));

            tv_profile_email.setText(profileJSObject.optString("email"));
        }
        if (profileJSObject.optString("mobileNo") != null
                && !(profileJSObject.optString("mobileNo").equalsIgnoreCase("null"))) {
            tv_mobile_num.setText(profileJSObject.optString("mobileNo"));
        }
        if (profileJSObject.optString("name") != null
                && !(profileJSObject.optString("name").equalsIgnoreCase("null"))) {
            tv_profile_name.setText(profileJSObject.optString("name"));
        }

        profileImageUrl = profileJSObject.optString("profileImgUrl");
        socialProfile = profileJSObject.optString("socialProfileUrl");

        Glide.with(getActivity())
                .load(profileJSObject.optString("profileImgUrl"))
                .placeholder(R.drawable.ic_user_default)
                .error(R.drawable.ic_user_default)
                .dontAnimate()
                .into(iv_profile);


        if (getActivity() instanceof HomeActivity) {
            HomeActivity homeActivity = (HomeActivity) getActivity();
            JSONObject followJSONObject = responseObject.optJSONObject("followList");
            JSONArray artisteFollows = followJSONObject.optJSONArray("artisteFollows");
            String followString = "";
            for (int i = 0; i < artisteFollows.length(); i++) {
                if ((i + 1) == artisteFollows.length()) {
                    followString += artisteFollows.optString(i);
                } else {
                    followString += artisteFollows.optString(i) + ",";
                }
            }
            Pref.SetStringPref(getActivity().getApplicationContext(), StringUtils.ARTISTE_FOLLOW_UP_STRING, followString);

            if (followString.length() > 0) {
                homeActivity.setArtisteFollowUpList(Arrays.asList(followString.split(",")));
            }


            JSONArray genreFollows = followJSONObject.optJSONArray("genreFollows");
            String genreString = "";
            for (int i = 0; i < genreFollows.length(); i++) {
                if ((i + 1) == genreFollows.length()) {
                    genreString += genreFollows.optString(i);
                } else {
                    genreString += genreFollows.optString(i) + ",";
                }
            }
            Pref.SetStringPref(getActivity().getApplicationContext(), StringUtils.GENRE_FOLLOW_UP_STRING, genreString);

            if (genreString.length() > 0) {
                homeActivity.setGenreFollowUpList(Arrays.asList(genreString.split(",")));
            }

            Pref.SetBooleanPref(getActivity().getApplicationContext(), StringUtils.IS_USER_PROFILE_LOADED, true);
        }
    }

    public void setUserProfile() {

        if (TextUtils.isEmpty(tv_email.getText().toString())) {
            ToastClass.showShortToast(getActivity().getApplicationContext(), "Please enter email");
            return;
        }
        if (!UtilityFunction.isEmailValid(tv_email.getText().toString())) {
            ToastClass.showShortToast(getActivity().getApplicationContext(), "Please enter valid email address");
            return;
        }

        if (TextUtils.isEmpty(tv_first_name.getText().toString())) {
            ToastClass.showShortToast(getActivity().getApplicationContext(), "Please enter first name");
            return;
        }

        if (TextUtils.isEmpty(tv_last_name.getText().toString())) {
            ToastClass.showShortToast(getActivity().getApplicationContext(), "Please enter last name");
            return;
        }

        if (TextUtils.isEmpty(tv_email.getText().toString())) {
            ToastClass.showShortToast(getActivity().getApplicationContext(), "Please enter email");
            return;
        }

        if (TextUtils.isEmpty(tv_mobile_num.getText().toString())) {
            ToastClass.showShortToast(getActivity().getApplicationContext(), "Please enter mobile number");
            return;
        }

        JSONObject jsonObject = new JSONObject();

        showProgressBar();

//        String set_profile_url=WebServicesUrls.SET_PROFILE+Pref.GetStringPref(getActivity().getApplicationContext(), StringUtils.USER_ID, "");
        String set_profile_url = WebServicesUrls.SET_PROFILE;

        try {
            jsonObject.put("userId", Pref.GetStringPref(getActivity().getApplicationContext(), StringUtils.USER_ID, ""));
            jsonObject.put("email", tv_email.getText().toString());
            jsonObject.put("isdCode", "+91");
            jsonObject.put("mobileNo", tv_mobile_num.getText().toString());
            jsonObject.put("country", "IN");
            jsonObject.put("name", tv_first_name.getText().toString() + " " + tv_last_name.getText().toString());
            jsonObject.put("vip", "0");
            jsonObject.put("isLoggedin", "1");
            jsonObject.put("langCode", "en");
            jsonObject.put("firstname", tv_first_name.getText().toString());
            jsonObject.put("lastname", tv_last_name.getText().toString());
            jsonObject.put("profileImgUrl", profileImageUrl);
            jsonObject.put("socialProfileUrl", socialProfile);
            jsonObject.put("signup_source", "androidapp");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new ApiCallBase(getActivity(), new WebServicesCallBack() {
            @Override
            public void onGetMsg(String apicall, String response) {
                dismissProgressBar();
//                homeContentPOJOS.clear();
                try {
                    String object = new String(response);
                    JSONObject jsonObject = new JSONObject(object);
                    JSONObject responseObject = jsonObject.optJSONObject("response");
                    if (responseObject.optBoolean("status")) {
                        Pref.SetStringPref(getActivity().getApplicationContext(), StringUtils.PROFILE_DATA, responseObject.optJSONObject("profile").toString());
                        ToastClass.showShortToast(getActivity().getApplicationContext(),"Profile Updated");
                    } else {
                        ToastClass.showShortToast(getActivity().getApplicationContext(), "Something went wrong");
                    }
                    Log.d(TagUtils.getTag(), jsonObject.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onErrorMsg(String status_code, String response) {
                dismissProgressBar();
                ToastClass.showShortToast(getActivity().getApplicationContext(), "Server Down");
            }
        }, "SET_PROFILE").makeApiCall(set_profile_url, jsonObject);
    }

    @Override
    public void onBackPressed() {
        if(getActivity() instanceof HomeActivity){
            HomeActivity homeActivity= (HomeActivity) getActivity();
            homeActivity.refreshMeHeader();
        }
        super.onBackPressed();
    }

}
