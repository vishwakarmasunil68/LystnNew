package com.app.lystn.fragment.download;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.lystn.R;
import com.app.lystn.activity.HomeActivity;
import com.app.lystn.adapter.PlayListAdapter;
import com.app.lystn.fragmentcontroller.FragmentController;
import com.app.lystn.pojo.home.HomeContentPOJO;
import com.app.lystn.util.TagUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class PlayListFragment extends FragmentController {

    @BindView(R.id.rv_list)
    RecyclerView rv_list;
    @BindView(R.id.iv_delete)
    ImageView iv_delete;
    @BindView(R.id.iv_back)
    ImageView iv_back;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_play, container, false);
        setUpView(getActivity(), this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() instanceof HomeActivity) {
            HomeActivity homeActivity = (HomeActivity) getActivity();
            downloadPojos.addAll(homeActivity.getPlayListDbManager().readAllSongs());
        }

        attachGenericAdapter(rv_list);

        iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteDialog();
            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    public void deleteSong(boolean show) {
        if (show) {
            iv_delete.setVisibility(View.VISIBLE);
        } else {
            iv_delete.setVisibility(View.GONE);
        }
    }

    List<HomeContentPOJO> downloadPojos = new ArrayList<>();
    PlayListAdapter playListAdapter;

    public void attachGenericAdapter(RecyclerView rv) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(linearLayoutManager);
        playListAdapter = new PlayListAdapter(getActivity(), this, downloadPojos);
        rv.setAdapter(playListAdapter);
        rv.setNestedScrollingEnabled(false);
        rv.setItemAnimator(new DefaultItemAnimator());
    }

    public void showDeleteDialog() {
        final Dialog dialog1 = new Dialog(getActivity(), android.R.style.Theme_DeviceDefault_Light_Dialog);
        dialog1.setCancelable(true);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog1.setContentView(R.layout.dialog_delete_downloads);
        dialog1.show();
        dialog1.setCancelable(true);
        Window window = dialog1.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        TextView tv_cancel = dialog1.findViewById(R.id.tv_cancel);
        TextView tv_ok = dialog1.findViewById(R.id.tv_ok);

//        FrameLayout frame_dialog = dialog1.findViewById(R.id.frame_dialog);
//        animateFrame(frame_dialog);

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
            }
        });

        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
                deleteSongs();
//                dialogShutDown(is_stop);
            }
        });
    }

    public void deleteSongs() {
        if (getActivity() instanceof HomeActivity) {
            HomeActivity homeActivity = (HomeActivity) getActivity();
            for (int i = 0; i < downloadPojos.size(); i++) {
                HomeContentPOJO homeContentPOJO = downloadPojos.get(i);
                if (homeContentPOJO.isChecked()) {
                    boolean removed = homeActivity.getPlayListDbManager().removeSong(homeContentPOJO.getConId());
                    Log.d(TagUtils.getTag(), "removed:-" + removed + ",id:-" + homeContentPOJO.getConId());
                    if (removed) {
                        downloadPojos.remove(i);
                    }
                }
            }
        }
        playListAdapter.notifyDataSetChanged();
    }


}
