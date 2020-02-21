package com.app.lystn.fragment.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.lystn.util.UtilityFunction;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.app.lystn.R;
import com.app.lystn.activity.HomeActivity;
import com.app.lystn.adapter.QueueAdapter;
import com.app.lystn.adapter.RecommentRVAdapter;
import com.app.lystn.fragmentcontroller.FragmentController;
import com.app.lystn.pojo.artiste.PodcastDetailPOJO;
import com.app.lystn.pojo.artiste.PodcastEpisodeDetailsPOJO;
import com.app.lystn.pojo.home.HomeContentPOJO;
import com.app.lystn.util.Pref;
import com.app.lystn.util.StringUtils;
import com.app.lystn.util.TagUtils;
import com.app.lystn.util.ToastClass;
import com.app.lystn.webservice.ApiCallBase;
import com.app.lystn.webservice.DownloadCallback;
import com.app.lystn.webservice.DownloadSongManager;
import com.app.lystn.webservice.WebServicesCallBack;
import com.app.lystn.webservice.WebServicesUrls;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import jp.wasabeef.glide.transformations.BlurTransformation;

public class MusicPlayerFragment extends FragmentController {

    @BindView(R.id.seekBar)
    SeekBar seekBar;
    @BindView(R.id.iv_play)
    ImageView iv_play;
    @BindView(R.id.iv_player)
    ImageView iv_player;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_small_desc)
    TextView tv_small_desc;
    @BindView(R.id.ll_minimize)
    LinearLayout ll_minimize;
    @BindView(R.id.iv_next)
    ImageView iv_next;
    @BindView(R.id.iv_previous)
    ImageView iv_previous;
    @BindView(R.id.ll_queue)
    LinearLayout ll_queue;
    @BindView(R.id.iv_player_background)
    ImageView iv_player_background;
    @BindView(R.id.tv_description)
    TextView tv_description;
    @BindView(R.id.tv_current_time)
    TextView tv_current_time;
    @BindView(R.id.tv_total_duration)
    TextView tv_total_duration;
    @BindView(R.id.ic_download)
    ImageView ic_download;
    @BindView(R.id.iv_podcast_image)
    ImageView iv_podcast_image;
    @BindView(R.id.tv_podcast_name)
    TextView tv_podcast_name;
    @BindView(R.id.tv_podcast_copy_right)
    TextView tv_podcast_copy_right;
    @BindView(R.id.cv_podcast)
    CardView cv_podcast;
    @BindView(R.id.iv_favorited)
    ImageView iv_favorited;
    @BindView(R.id.ll_seekBar)
    LinearLayout ll_seekBar;
    @BindView(R.id.ll_previous)
    LinearLayout ll_previous;
    @BindView(R.id.ll_next)
    LinearLayout ll_next;
    @BindView(R.id.rv_queue_list)
    RecyclerView rv_queue_list;
    @BindView(R.id.coordinator)
    CoordinatorLayout coordinator;
    @BindView(R.id.frame_bottom_sheet)
    FrameLayout frame_bottom_sheet;
    @BindView(R.id.frame_rv)
    FrameLayout frame_rv;
    @BindView(R.id.iv_seek_forward)
    ImageView iv_seek_forward;
    @BindView(R.id.layout_content)
    View layout_content;
    @BindView(R.id.frame_drag)
    FrameLayout frame_drag;
    @BindView(R.id.rv_recommend)
    RecyclerView rv_recommend;
    @BindView(R.id.cv_recomment)
    CardView cv_recomment;
    BottomSheetBehavior bottomSheetBehavior;

    PodcastDetailPOJO podcastDetailPOJO;
    List<HomeContentPOJO> homeContentPOJOS;
    int position;

    boolean onRVTouch = false;
    boolean openQueue = false;

    public MusicPlayerFragment(List<HomeContentPOJO> homeContentPOJOS, int position, PodcastDetailPOJO podcastDetailPOJO, boolean openQueue) {
        this.homeContentPOJOS = homeContentPOJOS;
        this.position = position;
        this.podcastDetailPOJO = podcastDetailPOJO;
        this.openQueue = openQueue;
//        Log.d(TagUtils.getTag(),"homecontentpojos:-"+homeContentPOJOS.size());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_music_player, container, false);
        setUpView(getActivity(), this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        layout_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        attachQueueAdapter();
//        this.homeContentPOJOS.clear();
//        queueAdapter.notifyDataSetChanged();

        bottomSheetBehavior = BottomSheetBehavior.from((view.findViewById(R.id.frame_bottom_sheet)));
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                if (onRVTouch) {
                    if (i == BottomSheetBehavior.STATE_DRAGGING) {
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                }

            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });

        ll_queue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queueLogic();
            }
        });


        ll_minimize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
//
//        // ------------- custom thumb
//        //----- using resources
//
//        seekBar.setThumb(new BitmapDrawable(BitmapFactory.decodeResource(
//                getActivity().getResources(), R.drawable.ic_seek_circle)));
//        //----- or using shape drawable

        iv_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() instanceof HomeActivity) {
                    HomeActivity homeActivity = (HomeActivity) getActivity();
                    homeActivity.playPausePlayer();
                }
            }
        });

        iv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Pref.GetStringPref(getActivity().getApplicationContext(), StringUtils.MEDIA_TYPE, "").equals("radio")) {
                    if (getActivity() instanceof HomeActivity) {
                        HomeActivity homeActivity = (HomeActivity) getActivity();
                        homeActivity.nextSong();
                    }
                }
            }
        });

        iv_seek_forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TagUtils.getTag(), "seeekbar progress:-" + seekBar.getProgress());
                if (getActivity() instanceof HomeActivity) {
                    HomeActivity homeActivity = (HomeActivity) getActivity();
                    homeActivity.seekMediaPlayer(seekBar.getProgress() + 15000);
                }
            }
        });

        iv_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Pref.GetStringPref(getActivity().getApplicationContext(), StringUtils.MEDIA_TYPE, "").equals("radio")) {
                    if (getActivity() instanceof HomeActivity) {
                        HomeActivity homeActivity = (HomeActivity) getActivity();
                        homeActivity.previousSong();
                    }
                }
            }
        });


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            //            boolean is_touched=false;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                Log.d(TagUtils.getTag(),"seekbar progress changed:-"+progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d(TagUtils.getTag(), "seekbar progress changed:-" + seekBar.getProgress());
                if (getActivity() instanceof HomeActivity) {
                    HomeActivity homeActivity = (HomeActivity) getActivity();
                    homeActivity.seekMediaPlayer(seekBar.getProgress());
                }
            }
        });

        if (podcastDetailPOJO != null) {
            changeFollowText(String.valueOf(podcastDetailPOJO.getPodcastId()));
        }

        iv_favorited.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (podcastDetailPOJO != null) {
                    if (getActivity() instanceof HomeActivity) {
                        HomeActivity homeActivity = (HomeActivity) getActivity();
                        if (homeActivity.getArtisteFollowUpList().contains(String.valueOf(podcastDetailPOJO.getPodcastId()))) {
                            homeActivity.showProgressBar();
                            JSONObject jsonObject = new JSONObject();

                            try {
                                jsonObject.put("userId", Pref.GetStringPref(getActivity().getApplicationContext(), StringUtils.USER_ID, ""));
                                jsonObject.put("deviceId", "91");
                                jsonObject.put("followType", "artiste");
                                jsonObject.put("followId", String.valueOf(podcastDetailPOJO.getPodcastId()));
                                jsonObject.put("langCode", "en");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            new ApiCallBase(getActivity(), new WebServicesCallBack() {
                                @Override
                                public void onGetMsg(String apicall, String response) {
                                    homeActivity.dismissProgressBar();
                                    try {
                                        String object = new String(response);
                                        JSONObject jsonObject = new JSONObject(object);
                                        JSONObject responseObject = jsonObject.optJSONObject("response");
//                                    ArtisteFollowUtil.removeArtisteFromList(activity.getApplicationContext(),String.valueOf(items.get(position).getConId()));
//                                    homeActivity.getArtisteFollowUpList().remove(items.get(position).getConId());
                                        homeActivity.removeFollowUP(StringUtils.ARTISTE_FOLLOW_UP_STRING, String.valueOf(podcastDetailPOJO.getPodcastId()));
//                                    Log.d(TagUtils.getTag(),"artiste list:-"+ArtisteFollowUtil.getArtisteList(activity.getApplicationContext()));

                                        changeFollowText(String.valueOf(podcastDetailPOJO.getPodcastId()));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onErrorMsg(String status_code, String response) {
                                    homeActivity.dismissProgressBar();
                                    ToastClass.showShortToast(getActivity().getApplicationContext(), "Server Down");
                                }
                            }, "UNFOLLOW_API").makeApiCall(WebServicesUrls.UNFOLLOW_API, jsonObject);
                        } else {
                            JSONObject jsonObject = new JSONObject();
                            homeActivity.showProgressBar();
                            try {
                                jsonObject.put("userId", Pref.GetStringPref(getActivity().getApplicationContext(), StringUtils.USER_ID, ""));
                                jsonObject.put("deviceId", "91");
                                jsonObject.put("followType", "artiste");
                                jsonObject.put("followId", String.valueOf(podcastDetailPOJO.getPodcastId()));
                                jsonObject.put("langCode", "en");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            new ApiCallBase(getActivity(), new WebServicesCallBack() {
                                @Override
                                public void onGetMsg(String apicall, String response) {
                                    homeActivity.dismissProgressBar();
                                    try {
                                        String object = new String(response);
                                        JSONObject jsonObject = new JSONObject(object);
                                        JSONObject responseObject = jsonObject.optJSONObject("response");
//                                    ArtisteFollowUtil.addFollowArtiste(activity.getApplicationContext(), String.valueOf(items.get(position).getConId()));
//                                    Log.d(TagUtils.getTag(), jsonObject.toString());
//                                    Log.d(TagUtils.getTag(),"artiste list:-"+ArtisteFollowUtil.getArtisteList(activity.getApplicationContext()));

                                        homeActivity.addFollowUpData(StringUtils.ARTISTE_FOLLOW_UP_STRING, String.valueOf(podcastDetailPOJO.getPodcastId()));

//                                    homeActivity.getArtisteFollowUpList().add(items.get(position).getConId());

                                        changeFollowText(String.valueOf(podcastDetailPOJO.getPodcastId()));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onErrorMsg(String status_code, String response) {
                                    ToastClass.showShortToast(getActivity().getApplicationContext(), "Server Down");
                                    homeActivity.dismissProgressBar();
                                }
                            }, "FOLLOW_API").makeApiCall(WebServicesUrls.FOLLOW_API, jsonObject);
                        }
                    }
                }
            }
        });
        attachRecommendAdapter();

        if (UtilityFunction.isNetworkConnected(getActivity().getApplicationContext())) {
            cv_recomment.setVisibility(View.VISIBLE);
        } else {
            cv_recomment.setVisibility(View.GONE);
        }
    }

    public void queueLogic() {
        Log.d(TagUtils.getTag(), "queue clicked");
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }

    public void openQueue() {
        Log.d(TagUtils.getTag(), "queue clicked");
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
        } else {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }

    public void attachRecommendAdapter() {

        List<String> recommend = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            recommend.add("");
        }

        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        rv_recommend.setHasFixedSize(true);
        rv_recommend.setLayoutManager(gridLayoutManager);
        RecommentRVAdapter homeArtistesAdapter = new RecommentRVAdapter(getActivity(), this, recommend);
        rv_recommend.setAdapter(homeArtistesAdapter);
        rv_recommend.setNestedScrollingEnabled(false);
        rv_recommend.setItemAnimator(new DefaultItemAnimator());
    }

    public void changeFollowText(String conId) {
        if (getActivity() instanceof HomeActivity) {
            HomeActivity homeActivity = (HomeActivity) getActivity();
            if (homeActivity.getArtisteFollowUpList().contains(conId)) {
                iv_favorited.setImageResource(R.drawable.ic_following_new);
            } else {
                iv_favorited.setImageResource(R.drawable.ic_follow_new);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (getActivity() instanceof HomeActivity) {
            HomeActivity homeActivity = (HomeActivity) getActivity();
            homeActivity.onMusicPlayerClosed();
        }
        super.onBackPressed();
//        Log.d(TagUtils.getTag(),"Music player back");
    }

    @Override
    public void onStart() {
        super.onStart();
        setHomeContentPOJO(position);
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }

    public void setHomeContentPOJO(int position) {
        Glide.with(getActivity())
                .load(homeContentPOJOS.get(position).getImgIrl())
                .placeholder(R.drawable.ll_square)
                .error(R.drawable.ll_square)
                .dontAnimate()
                .into(iv_player);

        for (HomeContentPOJO homeContentPOJO : homeContentPOJOS) {
            homeContentPOJO.setPlaying(false);
        }

        homeContentPOJOS.get(position).setPlaying(true);

        Glide.with(this).load(homeContentPOJOS.get(position).getImgIrl())
                .apply(RequestOptions.bitmapTransform(new BlurTransformation(25, 5)))
                .into(iv_player_background);

//        Glide.with(getActivity()).load(homeContentPOJO.getImgIrl())
//                .biu(new BlurTransformation(context))
//                .into((ImageView) findViewById(R.id.image));

        tv_name.setText(homeContentPOJOS.get(position).getConName());
        tv_name.setSelected(true);

        try {
//            Log.d(TagUtils.getTag(),"song pojo:-"+homeContentPOJOS.get(position).toString());
            tv_small_desc.setText("2019 following | " + UtilityFunction.coolFormat(((double) homeContentPOJOS.get(position).getPlayTimes()), 0) + " play");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (homeContentPOJOS.get(position).getDescription() != null && !(homeContentPOJOS.get(position).getDescription().equalsIgnoreCase(""))) {
            tv_description.setText(homeContentPOJOS.get(position).getDescription());
            tv_description.setVisibility(View.VISIBLE);
        } else {
            tv_description.setText("");
            tv_description.setVisibility(View.GONE);
        }

        if (Pref.GetStringPref(getActivity().getApplicationContext(), StringUtils.MEDIA_TYPE, "").equalsIgnoreCase("radio")) {
            ic_download.setVisibility(View.GONE);
            ll_seekBar.setVisibility(View.GONE);
            ll_next.setVisibility(View.GONE);
            frame_drag.setVisibility(View.GONE);
            ll_previous.setVisibility(View.GONE);
            ll_queue.setVisibility(View.GONE);
        } else {
            if (Pref.GetStringPref(getActivity().getApplicationContext(), StringUtils.MEDIA_TYPE, "").equalsIgnoreCase("download")) {
                ic_download.setVisibility(View.GONE);
            } else {
                if (getActivity() instanceof HomeActivity) {
                    HomeActivity homeActivity = (HomeActivity) getActivity();
                    if (homeActivity.getDbManager().checkSongInDB(homeContentPOJOS.get(position).getConId())) {
                        ic_download.setVisibility(View.GONE);
                    } else {
                        ic_download.setVisibility(View.VISIBLE);
                    }
                }
//                ic_download.setVisibility(View.VISIBLE);
            }
            frame_drag.setVisibility(View.VISIBLE);
            ll_seekBar.setVisibility(View.VISIBLE);
            ll_next.setVisibility(View.VISIBLE);
            ll_queue.setVisibility(View.VISIBLE);
            ll_previous.setVisibility(View.VISIBLE);
        }

        ic_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() instanceof HomeActivity) {
                    HomeActivity homeActivity = (HomeActivity) getActivity();

                    PodcastEpisodeDetailsPOJO podcastEpisodeDetailsPOJO = new PodcastEpisodeDetailsPOJO();
                    podcastEpisodeDetailsPOJO.setEpisodeId(homeContentPOJOS.get(position).getConId());
                    podcastEpisodeDetailsPOJO.setTitle(homeContentPOJOS.get(position).getConName());
                    podcastEpisodeDetailsPOJO.setImgLocalUri(homeContentPOJOS.get(position).getImgIrl());
                    podcastEpisodeDetailsPOJO.setImgRemoteUri(homeContentPOJOS.get(position).getImgIrl());
                    podcastEpisodeDetailsPOJO.setDescription(homeContentPOJOS.get(position).getDescription());
                    podcastEpisodeDetailsPOJO.setSubtitle(homeContentPOJOS.get(position).getSubtitle());
                    podcastEpisodeDetailsPOJO.setStreamUri(homeContentPOJOS.get(position).getCotDeepLink());

                    new DownloadSongManager(getActivity().getApplicationContext(),
                            homeActivity.getDbManager(),
                            new DownloadCallback() {
                                @Override
                                public void onSuccessDownload(String podcast_id) {
                                    Log.d(TagUtils.getTag(), "download complete:-" + podcast_id);
                                    ic_download.setVisibility(View.GONE);
                                }

                                @Override
                                public void onDownloadFailed(String error) {

                                }
                            }).downloadSong(podcastEpisodeDetailsPOJO, podcastDetailPOJO);
                }
            }
        });

        if (getActivity() instanceof HomeActivity) {
            HomeActivity homeActivity = (HomeActivity) getActivity();
            homeActivity.checkPlayerRunning();
        }
        if (podcastDetailPOJO != null) {
            cv_podcast.setVisibility(View.VISIBLE);
            Glide.with(getActivity())
                    .load(podcastDetailPOJO.getImgLocalUri())
                    .placeholder(R.drawable.ll_square)
                    .error(R.drawable.ll_square)
                    .dontAnimate()
                    .into(iv_podcast_image);

            tv_podcast_name.setText(podcastDetailPOJO.getTitle());
            tv_podcast_copy_right.setText(podcastDetailPOJO.getCopyright());
        } else {
            cv_podcast.setVisibility(View.GONE);
        }
        queueAdapter.notifyDataSetChanged();

        rv_queue_list.post(new Runnable() {
            @Override
            public void run() {
                rv_queue_list.scrollToPosition(position);
                // Here adapter.getItemCount()== child count
            }
        });

        frame_rv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(TagUtils.getTag(), "frame touch");
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                onRVTouch = true;
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.d(TagUtils.getTag(), "Action Down");
                        // touch down code
//                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        break;

                    case MotionEvent.ACTION_MOVE:
                        // touch move code
//                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        break;

                    case MotionEvent.ACTION_UP:
                        Log.d(TagUtils.getTag(), "Action UP");
                        onRVTouch = false;
                        // touch up code
                        break;
                }
                return false;
            }
        });

        rv_queue_list.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(TagUtils.getTag(), "rv_queue touch");
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                onRVTouch = true;
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.d(TagUtils.getTag(), "Action Down");
                        // touch down code
//                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        break;

                    case MotionEvent.ACTION_MOVE:
                        // touch move code
//                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        break;

                    case MotionEvent.ACTION_UP:
                        Log.d(TagUtils.getTag(), "Action UP");
                        onRVTouch = false;
                        // touch up code
                        break;
                }
                return false;
            }
        });

        if (openQueue) {
            openQueue();
        }
    }

    public void setPlayImage(boolean is_playing) {
        Log.d(TagUtils.getTag(), "is_playing:-" + is_playing);
        if (is_playing) {
            iv_play.setImageResource(R.drawable.ic_pause);
        } else {
            iv_play.setImageResource(R.drawable.ic_play);
        }
    }

    public void updateTimings(int current_time, int media_duration) {
        seekBar.setMax(media_duration);
        seekBar.setProgress(current_time);
        String minSec = UtilityFunction.getMinSec(current_time);
//        Log.d(TagUtils.getTag(), "min sec:-" + minSec);
        tv_current_time.setText(minSec);
        tv_total_duration.setText(UtilityFunction.getMinSec(media_duration));
    }


    QueueAdapter queueAdapter;

    public void attachQueueAdapter() {
        Log.d(TagUtils.getTag(), "content pojos length:-" + homeContentPOJOS.size());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv_queue_list.setHasFixedSize(true);
        rv_queue_list.setLayoutManager(linearLayoutManager);
        queueAdapter = new QueueAdapter(getActivity(), this, homeContentPOJOS);
        rv_queue_list.setAdapter(queueAdapter);
        rv_queue_list.setNestedScrollingEnabled(false);
        rv_queue_list.setItemAnimator(new DefaultItemAnimator());
    }

    public void playQueueSelectedSong(int position) {
        if (getActivity() instanceof HomeActivity) {
            for (HomeContentPOJO homeContentPOJO : homeContentPOJOS) {
                homeContentPOJO.setPlaying(false);
            }
            this.homeContentPOJOS.get(position).setPlaying(true);
            HomeActivity homeActivity = (HomeActivity) getActivity();
            homeActivity.playQueueSelectedSong(position);
        }
    }
}
