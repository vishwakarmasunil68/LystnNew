package com.app.lystn.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.app.lystn.util.UtilityFunction;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.app.lystn.R;
import com.app.lystn.adapter.ViewPagerAdapter;
import com.app.lystn.fragment.category.HomeCateogryPodcastFragment;
import com.app.lystn.fragment.download.DownloadListFragment;
import com.app.lystn.fragment.download.PlayListFragment;
import com.app.lystn.fragment.home.AllArtistFragment;
import com.app.lystn.fragment.home.AllGenreFragment;
import com.app.lystn.fragment.home.AllRadioFragment;
import com.app.lystn.fragment.home.ArtisteDetailFragment;
import com.app.lystn.fragment.home.CategoryTagFragment;
import com.app.lystn.fragment.home.GenreDetailFragment;
import com.app.lystn.fragment.home.HomeAllContentFragment;
import com.app.lystn.fragment.home.HomeFragment;
import com.app.lystn.fragment.home.LibraryFragment;
import com.app.lystn.fragment.home.ManageSubscriptionFragment;
import com.app.lystn.fragment.home.MeFragment;
import com.app.lystn.fragment.home.MeSubscriptionFragment;
import com.app.lystn.fragment.home.MusicPlayerFragment;
import com.app.lystn.fragment.home.SearchFragment;
import com.app.lystn.fragment.home.UpdateFragment;
import com.app.lystn.fragment.login.LoginSelectLanguageFragment;
import com.app.lystn.fragment.profile.ProfileFragment;
import com.app.lystn.fragmentcontroller.ActivityManager;
import com.app.lystn.pojo.DownloadPOJO;
import com.app.lystn.pojo.artiste.ArtisteDetailPOJO;
import com.app.lystn.pojo.artiste.PodcastDetailPOJO;
import com.app.lystn.pojo.artiste.PodcastEpisodeDetailsPOJO;
import com.app.lystn.pojo.home.HomeContentPOJO;
import com.app.lystn.pojo.home.HomePOJO;
import com.app.lystn.service.DownloadSongService;
import com.app.lystn.service.MediaService;
import com.app.lystn.util.DbManager;
import com.app.lystn.util.PlayListDbManager;
import com.app.lystn.util.Pref;
import com.app.lystn.util.StringUtils;
import com.app.lystn.util.TagUtils;
import com.app.lystn.util.ToastClass;
import com.app.lystn.webservice.ApiCallBase;
import com.app.lystn.webservice.WebServicesCallBack;
import com.app.lystn.webservice.WebServicesUrls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import p32929.androideasysql_library.Column;
import p32929.androideasysql_library.EasyDB;

public class HomeActivity extends ActivityManager implements View.OnClickListener {

    @BindView(R.id.ll_home)
    LinearLayout ll_home;
    @BindView(R.id.iv_home)
    ImageView iv_home;
    @BindView(R.id.tv_home)
    TextView tv_home;

    @BindView(R.id.ll_search)
    LinearLayout ll_search;
    @BindView(R.id.iv_search)
    ImageView iv_search;
    @BindView(R.id.tv_search)
    TextView tv_search;

    @BindView(R.id.ll_update)
    LinearLayout ll_update;
    @BindView(R.id.iv_update)
    ImageView iv_update;
    @BindView(R.id.tv_update)
    TextView tv_update;

    @BindView(R.id.ll_library)
    LinearLayout ll_library;
    @BindView(R.id.iv_library)
    ImageView iv_library;
    @BindView(R.id.tv_library)
    TextView tv_library;

    @BindView(R.id.ll_me)
    LinearLayout ll_me;
    @BindView(R.id.iv_me)
    ImageView iv_me;
    @BindView(R.id.tv_me)
    TextView tv_me;

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @BindView(R.id.frame_main)
    FrameLayout frame_main;

    List<LinearLayout> linearLayouts = new ArrayList<>();
    List<ImageView> imageViews = new ArrayList<>();
    List<TextView> textViews = new ArrayList<>();

    //    HomeContentPOJO homeContentPOJO;
    HomeFragment homeFragment;
    MeFragment mefrag;
    List<HomeContentPOJO> homeContentPOJOS = new ArrayList<>();
    int playingPosition = -1;

    List<String> artisteFollowUpList = new ArrayList<>();
    List<String> genreFollowUpList = new ArrayList<>();

    DbManager dbManager;
    PlayListDbManager playListDbManager;

    boolean is_registered = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


//        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
//            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
//        }
//        if (Build.VERSION.SDK_INT >= 19) {
//            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//        }
        //make fully Android Transparent Status bar

        maketranslucentStatusBar();
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);


        dbManager = DbManager.initDB(getApplicationContext());
        playListDbManager = PlayListDbManager.initDB(getApplicationContext());

        linearLayouts.add(ll_home);
        linearLayouts.add(ll_search);
        linearLayouts.add(ll_update);
        linearLayouts.add(ll_library);
        linearLayouts.add(ll_me);

        imageViews.add(iv_home);
        imageViews.add(iv_search);
        imageViews.add(iv_update);
        imageViews.add(iv_library);
        imageViews.add(iv_me);

        textViews.add(tv_home);
        textViews.add(tv_search);
        textViews.add(tv_update);
        textViews.add(tv_library);
        textViews.add(tv_me);

        setupViewPager(viewPager);
        makeTabSelected(0);

        ll_home.setOnClickListener(this);
        ll_search.setOnClickListener(this);
        ll_update.setOnClickListener(this);
        ll_library.setOnClickListener(this);
        ll_me.setOnClickListener(this);


        checkUserProfileLoaded();

//        String appDir = UtilityFunction.getAppDirectory(getApplicationContext());
//        UtilityFunction.createAppSongDir(getApplicationContext());
//        listAllFiles(appDir);


//        initDB();

    }


    public DbManager getDbManager() {
        return dbManager;
    }

    public PlayListDbManager getPlayListDbManager() {
        return playListDbManager;
    }

    public void initDB() {
        //type:-  podcast song =1
        EasyDB easyDB = EasyDB.init(this, "Lystn") // "TEST" is the name of the DATABASE
                .setTableName("downloads")  // You can ignore this line if you want
//                .addColumn(new Column("id", new String[]{"text", "unique"}))
                .addColumn(new Column("type", new String[]{"text", "not null"}))
                .addColumn(new Column("pojo", new String[]{"text"}))
                .doneTableColumn();

        addsong();
    }

    public void addsong() {
        EasyDB easyDB = EasyDB.init(this, "Lystn").setTableName("downloads");
        boolean done = easyDB
                .addData("type", "adding data")
                .addData("pojo", "casdcsdac")
                .doneDataAdding();

        readAllData();
    }

    public void readAllData() {
        EasyDB easyDB = EasyDB.init(this, "Lystn").setTableName("downloads");
        Cursor res = easyDB.getAllData();
        while (res.moveToNext()) {
            Log.d(TagUtils.getTag(), "data reading:- col1:-" + res.getString(0) + ",col2:-" + res.getString(1) + ",col3:-" + res.getString(2));
        }
    }

//    public void listAllFiles(String file_path) {
//        File file = new File(file_path);
//        if (file.exists()) {
//            String[] files = file.list();
//            Log.d(TagUtils.getTag(), "files length:-" + files.length);
//            for (String f : files) {
//                Log.d(TagUtils.getTag(), "inner file:-" + f);
//            }
//        }
//    }

    public void checkUserProfileLoaded() {

        artisteFollowUpList.clear();
        genreFollowUpList.clear();

        if (!Pref.GetBooleanPref(getApplicationContext(), StringUtils.IS_USER_PROFILE_LOADED, false)) {
            getUserProfile();
        } else {
            String artisteFollowString = Pref.GetStringPref(getApplicationContext(), StringUtils.ARTISTE_FOLLOW_UP_STRING, "");
            if (artisteFollowString.length() > 0) {
                artisteFollowUpList.addAll(Arrays.asList(artisteFollowString.split(",")));
            }

            String genreFollowString = Pref.GetStringPref(getApplicationContext(), StringUtils.GENRE_FOLLOW_UP_STRING, "");
            if (genreFollowString.length() > 0) {
                genreFollowUpList.addAll(Arrays.asList(genreFollowString.split(",")));
            }
        }
    }


    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(homeFragment = new HomeFragment(), "Monitor");
        adapter.addFrag(new SearchFragment(), "Monitor");
        adapter.addFrag(new UpdateFragment(), "Monitor");
        adapter.addFrag(new LibraryFragment(), "Monitor");
        adapter.addFrag(mefrag = new MeFragment(), "Monitor");

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(adapter.getCount());


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                makeTabSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void makeTabSelected(int position) {

        makeAllUnselected();

        textViews.get(position).setTextColor(Color.parseColor("#FF9C37"));

        switch (position) {
            case 0:
                iv_home.setImageResource(R.drawable.ic_home_selected);
                break;
            case 1:
                iv_search.setImageResource(R.drawable.ic_search_selected);
                break;
            case 2:
                iv_update.setImageResource(R.drawable.ic_update_selected);
                break;
            case 3:
                iv_library.setImageResource(R.drawable.ic_library_selected);
                break;
            case 4:
                iv_me.setImageResource(R.drawable.ic_me_selected);
                break;
        }
    }

    public void setViewPagerIndex(int position) {
        viewPager.setCurrentItem(position);
    }

    public void makeAllUnselected() {
        iv_home.setImageResource(R.drawable.ic_home_nonselected);
        iv_search.setImageResource(R.drawable.ic_search_nonselected);
        iv_update.setImageResource(R.drawable.ic_update_nonselected);
        iv_library.setImageResource(R.drawable.ic_library_nonselected);
        iv_me.setImageResource(R.drawable.ic_me_nonselected);

        for (int i = 0; i < textViews.size(); i++) {
            textViews.get(i).setTextColor(Color.parseColor("#BDBDBD"));
        }
    }

    @Override
    public void onClick(View v) {

        if (fragmentList.size() > 0) {
            for (int i = 0; i < fragmentList.size(); i++) {
                onBackPressed();
            }
        }
        switch (v.getId()) {
            case R.id.ll_home:
                viewPager.setCurrentItem(0);
                break;
            case R.id.ll_search:
                viewPager.setCurrentItem(1);
                break;
            case R.id.ll_update:
                viewPager.setCurrentItem(2);
                break;
            case R.id.ll_library:
                viewPager.setCurrentItem(3);
                break;
            case R.id.ll_me:
                viewPager.setCurrentItem(4);
                break;

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!is_registered) {
            Log.d(TagUtils.getTag(), "registering receiver");
            is_registered = true;
            getApplicationContext().registerReceiver(mMessageReceiver, new IntentFilter(StringUtils.UPDATE_HOME_ACTIVITY));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void startAllArtistFragment() {
        startFragment(R.id.frame_main, new AllArtistFragment());
    }

    public void startAllGenreFragment() {
        startFragment(R.id.frame_main, new AllGenreFragment());
    }

    public void showUnLockPremiumFragment() {
        startFragment(R.id.frame_main, new MeSubscriptionFragment());
    }

    public void startManageSubscription() {
        startFragment(R.id.frame_main, new ManageSubscriptionFragment());
    }

    public void startSelectLangageFragment() {
        startFragment(R.id.frame_main, new LoginSelectLanguageFragment());
    }

    MusicPlayerFragment musicPlayerFragment;
    PodcastDetailPOJO podcastDetailPOJO;

    public void startMusicPlayerFragment(boolean openQueue) {
        if (homeContentPOJOS != null && homeContentPOJOS.size() > 0 && playingPosition != -1) {
            makeTransparentStatusBar();
            startFragment(R.id.frame_home_activity, musicPlayerFragment = new MusicPlayerFragment(homeContentPOJOS, playingPosition, podcastDetailPOJO, openQueue));
            new CountDownTimer(2000, 1000) {

                public void onTick(long millisUntilFinished) {
                    ll_small_player.setEnabled(false);
                }

                public void onFinish() {
                    ll_small_player.setEnabled(true);
                }
            }.start();
        }
    }

    public void showDownloadFragment() {
        startFragment(R.id.frame_main, new DownloadListFragment());
    }

    public void showPlayListFragment() {
        startFragment(R.id.frame_main, new PlayListFragment());
    }

    public void showCategoryListFragment(String bk_id) {
        startFragment(R.id.frame_main, new CategoryTagFragment(bk_id));
    }

    public void showHomeAllContentFragment(HomePOJO homePOJO) {
        startFragment(R.id.frame_main, new HomeAllContentFragment(homePOJO));
    }

    public void showAllRadioContentFragment(HomePOJO homePOJO) {
        makeTransparentStatusBar();
        startFragment(R.id.frame_main, new AllRadioFragment(homePOJO));
    }


    public void nextSong() {
        if (homeContentPOJOS != null && homeContentPOJOS.size() > 0 && playingPosition != -1) {
            if ((homeContentPOJOS.size() - 1) > playingPosition) {
                playingPosition = playingPosition + 1;
                MusicPlayerFragment musicPlayerFragment = getMusicPlayerFragment();
                if (musicPlayerFragment != null) {
                    musicPlayerFragment.setHomeContentPOJO(playingPosition);
                    startPlayer(homeContentPOJOS.get(playingPosition));
                }
            }
        }
    }

    public void playQueueSelectedSong(int position) {
        if (homeContentPOJOS != null && homeContentPOJOS.size() > 0 && playingPosition != -1) {
//            if ((homeContentPOJOS.size() - 1) > position) {
            playingPosition = position;
            MusicPlayerFragment musicPlayerFragment = getMusicPlayerFragment();
            if (musicPlayerFragment != null) {
                musicPlayerFragment.setHomeContentPOJO(playingPosition);
                startPlayer(homeContentPOJOS.get(playingPosition));
//                }
            }
        }
    }


    public void previousSong() {
        Log.d(TagUtils.getTag(), "previous song");
        if (homeContentPOJOS != null && homeContentPOJOS.size() > 0 && playingPosition != -1) {
            if (playingPosition != 0) {
                playingPosition = playingPosition - 1;
                MusicPlayerFragment musicPlayerFragment = getMusicPlayerFragment();
                if (musicPlayerFragment != null) {
                    musicPlayerFragment.setHomeContentPOJO(playingPosition);
                    startPlayer(homeContentPOJOS.get(playingPosition));
                }
            }
        }
    }

    public void playAudio(List<HomeContentPOJO> homeContentPOJOS, int position, String type, PodcastDetailPOJO podcastDetailPOJO) {
        ll_small_player.setVisibility(View.VISIBLE);
        Pref.SetStringPref(getApplicationContext(), StringUtils.MEDIA_TYPE, type);
        this.homeContentPOJOS.clear();
        this.homeContentPOJOS.addAll(homeContentPOJOS);
        this.playingPosition = position;
        this.podcastDetailPOJO = podcastDetailPOJO;

        String podcast = "";
        if (podcastDetailPOJO != null) {
            podcast = new Gson().toJson(podcastDetailPOJO);
        }
        playListDbManager.savePlayedSong(homeContentPOJOS.get(position).getConId(), "podcast", new Gson().toJson(homeContentPOJOS.get(position)), podcast);


        if (podcastDetailPOJO != null) {
            Pref.SetStringPref(getApplicationContext(), StringUtils.NOTIFICAION_ALBUM_NAME, podcastDetailPOJO.getTitle().trim());
        }
        startMusicPlayerFragment(false);
        startPlayer(this.homeContentPOJOS.get(playingPosition));
    }

    public void startPlayer(HomeContentPOJO homeContentPOJO) {
        showProgressBar();
        if (!isMyServiceRunning(MediaService.class)) {
            Intent serviceIntent = new Intent(HomeActivity.this, MediaService.class);
//            serviceIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
            serviceIntent.putExtra(StringUtils.AUDIO_DATA, new Gson().toJson(homeContentPOJO));
            startService(serviceIntent);
        } else {
            playSongMessageToService(homeContentPOJO);
        }
    }

    public void setMusicPlayerFragmentPlayImage(boolean is_playing) {

//        if (fragmentList != null && fragmentList.size() > 0) {
//            for (int i = 0; i < fragmentList.size(); i++) {
//                Log.d(TagUtils.getTag(), "fragment:-" + fragmentList.get(i).getClass().getName());
//                if (fragmentList.get(i) instanceof MusicPlayerFragment) {
//                    MusicPlayerFragment musicPlayerFragment = (MusicPlayerFragment) fragmentList.get(i);
//
//                }
//            }
//        }

        MusicPlayerFragment musicPlayerFragment = getMusicPlayerFragment();
        if (musicPlayerFragment != null) {
            musicPlayerFragment.setPlayImage(is_playing);
        }
        homeFragment.setHomeContentPOJO(homeContentPOJOS.get(playingPosition));
//        setSmallPlayerHomeContent(homeContentPOJOS.get(playingPosition));
        setSmallPlayerListeners(homeContentPOJOS.get(playingPosition));
        playPauseMusic(is_playing);
//        homeFragment.playPauseMusic(is_playing);
    }

    public void playSongMessageToService(HomeContentPOJO homeContentPOJO) {
        try {
            Intent intent = new Intent(StringUtils.UPDATE_SERVICE);
            intent.putExtra("type", StringUtils.PLAY_SONG);
            intent.putExtra(StringUtils.AUDIO_DATA, homeContentPOJO);
            sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        android.app.ActivityManager manager = (android.app.ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (android.app.ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                String type = intent.getStringExtra("type");
//                Log.d(TagUtils.getTag(), "HomeType:-" + type);
                if (type.equalsIgnoreCase(StringUtils.DISMISS_PROGRESS_BAR)) {
                    dismissProgressBar();
                } else if (type.equalsIgnoreCase(StringUtils.MUSIC_PLAYING_STATUS)) {
                    Log.d(TagUtils.getTag(), "setting music Player status");
                    setMusicPlayerFragmentPlayImage(intent.getBooleanExtra("status", false));
                } else if (type.equalsIgnoreCase(StringUtils.NEXT_SONG)) {
                    nextSong();
                } else if (type.equalsIgnoreCase(StringUtils.PREVIOUS_SONG)) {
                    previousSong();
                } else if (type.equalsIgnoreCase(StringUtils.MEDIA_TIMINGS)) {
                    if (musicPlayerFragment != null) {
                        musicPlayerFragment.updateTimings(intent.getIntExtra(StringUtils.CURRENT_MEDIA_TIME, 0), intent.getIntExtra(StringUtils.MEDIA_DURATION, 0));
                    }
                    updateTimings(intent.getIntExtra(StringUtils.CURRENT_MEDIA_TIME, 0), intent.getIntExtra(StringUtils.MEDIA_DURATION, 0));
                } else if (type.equalsIgnoreCase(StringUtils.PLAY_COMPLETED)) {
                    nextSong();
                } else if (type.equalsIgnoreCase(StringUtils.SAVE_SONG_DB)) {
                    String podcast_id = intent.getStringExtra("podcast_id");
                    PodcastEpisodeDetailsPOJO podcastEpisodeDetailsPOJO = (PodcastEpisodeDetailsPOJO) intent.getSerializableExtra("pojo");
                    dbManager.saveDownloadedSong(podcastEpisodeDetailsPOJO.getEpisodeId(), "podcast", new Gson().toJson(podcastEpisodeDetailsPOJO), "");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public void playPausePlayer() {
        try {
            Intent intent = new Intent(StringUtils.UPDATE_SERVICE);
            intent.putExtra("type", StringUtils.PLAY_PAUSE_MEDIA);
            sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void checkPlayerRunning() {
        try {
            Intent intent = new Intent(StringUtils.UPDATE_SERVICE);
            intent.putExtra("type", StringUtils.CHECK_PLAYER);
            sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public MusicPlayerFragment getMusicPlayerFragment() {
        if (fragmentList != null && fragmentList.size() > 0) {
            for (int i = 0; i < fragmentList.size(); i++) {
                if (fragmentList.get(i) instanceof MusicPlayerFragment) {
                    MusicPlayerFragment musicPlayerFragment = (MusicPlayerFragment) fragmentList.get(i);
                    return musicPlayerFragment;
                }
            }
        }
        return null;
    }

    public void onMusicPlayerClosed() {
        Log.d(TagUtils.getTag(), "Music Player closed");
        maketranslucentStatusBar();
    }

    public void makeTransparentStatusBar() {
        Log.d(TagUtils.getTag(), "transparent background");
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        //make fully Android Transparent Status bar
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public void maketranslucentStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decor = getWindow().getDecorView();
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    public void showArtisteDetail(String conId) {
        JSONObject jsonObject = new JSONObject();

        showProgressBar();

        try {
            jsonObject.put("userId", Pref.GetStringPref(getApplicationContext(), StringUtils.USER_ID, ""));
            jsonObject.put("deviceId", Pref.GetStringPref(getApplicationContext(), StringUtils.DEVICE_ID, ""));
//            jsonObject.put("conId", "5268");
            jsonObject.put("conId", conId);
            jsonObject.put("langCode", "en");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new ApiCallBase(this, new WebServicesCallBack() {
            @Override
            public void onGetMsg(String apicall, String response) {
                dismissProgressBar();
//                homeContentPOJOS.clear();
                try {
                    String object = new String(response);
                    JSONObject jsonObject = new JSONObject(object);
                    JSONObject responseObject = jsonObject.optJSONObject("response");
                    if (responseObject.optBoolean("status")) {

                        JSONObject content = responseObject.optJSONObject("data").optJSONArray("contents").optJSONObject(0);
                        ArtisteDetailPOJO artisteDetailPOJO = new Gson().fromJson(content.toString(), ArtisteDetailPOJO.class);
                        startPodcastFragment(artisteDetailPOJO.getPodcastId());

                    } else {
                        ToastClass.showShortToast(getApplicationContext(), "Something went wrong");
                    }
                    Log.d(TagUtils.getTag(), jsonObject.toString());
                } catch (JSONException e) {
                    ToastClass.showShortToast(getApplicationContext(), "No Data Found");
                    ;
                    e.printStackTrace();
                }
            }

            @Override
            public void onErrorMsg(String status_code, String response) {
                dismissProgressBar();
                ToastClass.showShortToast(getApplicationContext(), "Server Down");
            }
        }, "GET_HOME_CONTENT").makeApiCall(WebServicesUrls.GET_ARTIST_BUCKET, jsonObject);
    }

    public void startPodcastFragment(String conId) {
        startFragment(R.id.frame_main, new ArtisteDetailFragment(conId));
    }

    public void startGenreDetailFragment(String conId) {
        startFragment(R.id.frame_main, new GenreDetailFragment(conId));
    }

    public void setHomeContentPOJOS(List<HomeContentPOJO> homeContentPOJOS) {

        if (homeContentPOJOS != null && homeContentPOJOS.size() > 0 && playingPosition != -1) {

            String playing_id = homeContentPOJOS.get(playingPosition).getConId();

            this.homeContentPOJOS.clear();
            this.homeContentPOJOS.addAll(homeContentPOJOS);

            for (int i = 0; i < homeContentPOJOS.size(); i++) {
                if (homeContentPOJOS.get(i).getConId().equalsIgnoreCase(playing_id)) {
                    this.playingPosition = i;
                }
            }
        }
    }


    public void getUserProfile() {
        JSONObject jsonObject = new JSONObject();

        showProgressBar();

        try {
            jsonObject.put("userId", Pref.GetStringPref(getApplicationContext(), StringUtils.USER_ID, ""));
            jsonObject.put("deviceId", Pref.GetStringPref(getApplicationContext(), StringUtils.DEVICE_ID, ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new ApiCallBase(this, new WebServicesCallBack() {
            @Override
            public void onGetMsg(String apicall, String response) {
                dismissProgressBar();
//                homeContentPOJOS.clear();
                try {
                    String object = new String(response);
                    JSONObject jsonObject = new JSONObject(object);
                    JSONObject responseObject = jsonObject.optJSONObject("response");
                    if (responseObject.optBoolean("status")) {
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
                        Pref.SetStringPref(getApplicationContext(), StringUtils.ARTISTE_FOLLOW_UP_STRING, followString);

                        if (followString.length() > 0) {
                            artisteFollowUpList.addAll(Arrays.asList(followString.split(",")));
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
                        Pref.SetStringPref(getApplicationContext(), StringUtils.GENRE_FOLLOW_UP_STRING, genreString);

                        if (genreString.length() > 0) {
                            genreFollowUpList.addAll(Arrays.asList(genreString.split(",")));
                        }

                        Pref.SetBooleanPref(getApplicationContext(), StringUtils.IS_USER_PROFILE_LOADED, true);

                    } else {
                        ToastClass.showShortToast(getApplicationContext(), "Something went wrong");
                    }
                    Log.d(TagUtils.getTag(), jsonObject.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onErrorMsg(String status_code, String response) {
                dismissProgressBar();
                ToastClass.showShortToast(getApplicationContext(), "Server Down");
            }
        }, "GET_PROFILE").makeApiCall(WebServicesUrls.GET_PROFILE, jsonObject);
    }

    public List<String> getArtisteFollowUpList() {
        return artisteFollowUpList;
    }

    public List<String> getGenreFollowUpList() {
        return genreFollowUpList;
    }

    public void setGenreFollowUpList(List<String> genreFollowUpList) {
        this.genreFollowUpList = genreFollowUpList;
    }

    public void setArtisteFollowUpList(List<String> artisteFollowUpList) {
        this.artisteFollowUpList = artisteFollowUpList;
    }

    public void addFollowUpData(String prefString, String followId) {
        String followUp = Pref.GetStringPref(getApplicationContext(), prefString, "");
        List<String> followList = new ArrayList<>();
        if (followUp.length() > 0) {
            followList.addAll(Arrays.asList(followUp.split(",")));
            followList.add(followId);
        } else {
            followList.add(followId);
        }
        String followString = "";
        for (int i = 0; i < followList.size(); i++) {
            if ((i + 1) == followList.size()) {
                followString += followList.get(i);
            } else {
                followString += followList.get(i) + ",";
            }
        }
        Pref.SetStringPref(getApplicationContext(), prefString, followString);
        if (prefString.equalsIgnoreCase(StringUtils.ARTISTE_FOLLOW_UP_STRING)) {
            artisteFollowUpList.add(followId);
        } else if (prefString.equalsIgnoreCase(StringUtils.GENRE_FOLLOW_UP_STRING)) {
            genreFollowUpList.add(followId);
        }
    }

    public void removeFollowUP(String prefString, String followId) {
        String followUp = Pref.GetStringPref(getApplicationContext(), prefString, "");
        List<String> followList = new ArrayList<>();
        if (followUp.length() > 0) {
            followList.addAll(Arrays.asList(followUp.split(",")));
            if (followList.contains(followId)) {
                followList.remove(followId);
            }
        }
        String followString = "";
        for (int i = 0; i < followList.size(); i++) {
            if ((i + 1) == followList.size()) {
                followString += followList.get(i);
            } else {
                followString += followList.get(i) + ",";
            }
        }
        Pref.SetStringPref(getApplicationContext(), prefString, followString);
        if (prefString.equalsIgnoreCase(StringUtils.ARTISTE_FOLLOW_UP_STRING)) {
            if (artisteFollowUpList.contains(followId)) {
                artisteFollowUpList.remove(followId);
            }
        } else if (prefString.equalsIgnoreCase(StringUtils.GENRE_FOLLOW_UP_STRING)) {
            if (genreFollowUpList.contains(followId)) {
                genreFollowUpList.remove(followId);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        is_registered = false;
        Log.d(TagUtils.getTag(), "destroying receiver");
        getApplicationContext().unregisterReceiver(mMessageReceiver);

        stopService(new Intent(HomeActivity.this, MediaService.class));
        if (dbManager != null) {
            dbManager.closeDB();
        }
        if (playListDbManager != null) {
            playListDbManager.closeDB();
        }
    }

    @Override
    public void onBackPressed() {
        if (fragmentList.size() == 0) {
            if (viewPager.getCurrentItem() == 0) {
                this.moveTaskToBack(true);
            } else {
                viewPager.setCurrentItem(0);
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Bundle bundle = intent.getExtras();

        if (intent != null && intent.getAction() != null) {
            if (intent.getAction().equals(MediaService.ACTION_SEEK)) {
                seekMediaPlayer();
            } else if (intent.getAction().equals(MediaService.ACTION_QUEUE)) {
                MusicPlayerFragment musicPlayerFragment = getMusicPlayerFragment();
                if (musicPlayerFragment != null) {
                    musicPlayerFragment.openQueue();
                } else {
                    startMusicPlayerFragment(true);
                }
            }
        }

        if (bundle != null) {
            for (String key : bundle.keySet()) {
                Log.e(TagUtils.getTag(), key + " : " + (bundle.get(key) != null ? bundle.get(key) : "NULL"));
            }
        }
    }

    //    @Override
//    protected void onNewIntent(Intent intent) {
//        HomeActivity.this.finish();
//
//        // -----Start New Dashboard  when notification is clicked----
//
//        Intent i = new Intent(HomeActivity.this, HomeActivity.class);
//
//        startActivity(i);
//
//        super.onNewIntent(intent);
//    }

    //    public void playEpisode(List<HomeContentPOJO> homeContentPOJOS, int index) {
//        playAudio(homeContentPOJOS, index, "genre");
//    }

    public void seekMediaPlayer() {
        Intent intent = new Intent(StringUtils.UPDATE_SERVICE);
        intent.putExtra("type", StringUtils.SEEK_PLAYER_ACTIVITY);
        sendBroadcast(intent);
    }

    public void seekMediaPlayer(int progress) {
        Intent intent = new Intent(StringUtils.UPDATE_SERVICE);
        intent.putExtra("type", StringUtils.SEEK_PLAYER_ACTIVITY);
        intent.putExtra(StringUtils.SEEK_PROGRESS, progress);
        sendBroadcast(intent);
    }

    @BindView(R.id.ll_small_player)
    LinearLayout ll_small_player;
    @BindView(R.id.iv_player)
    ImageView iv_player;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.iv_play)
    ImageView iv_play;
    @BindView(R.id.ll_play)
    LinearLayout ll_play;
    @BindView(R.id.tv_timings)
    TextView tv_timings;

    public void setSmallPlayerListeners(HomeContentPOJO homeContentPOJO) {

        Glide.with(this)
                .load(homeContentPOJO.getImgIrl())
                .placeholder(R.drawable.ll_square)
                .error(R.drawable.ll_square)
                .dontAnimate()
                .into(iv_player);

        if (Pref.GetStringPref(getApplicationContext(), StringUtils.MEDIA_TYPE, "").equalsIgnoreCase("radio")) {
            tv_timings.setVisibility(View.GONE);
        } else {
            tv_timings.setVisibility(View.VISIBLE);
        }

        tv_name.setText(homeContentPOJO.getConName());
        tv_name.setSelected(true);
//        tv_name.initialize(homeContentPOJO.getConName(), Color.parseColor("#000000"), false, false);

        ll_small_player.setVisibility(View.VISIBLE);

        ll_small_player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMusicPlayerFragment(false);
            }
        });

        iv_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_name.setText(homeContentPOJO.getConName());
                tv_name.setSelected(true);
//                tv_name.start();
//                tv_name.initialize(homeContentPOJO.getConName(), Color.parseColor("#000000"), false, false);
                playPausePlayer();
            }
        });

        ll_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_play.callOnClick();
            }
        });

    }

    public void playPauseMusic(boolean is_playing) {
        if (is_playing) {
            iv_play.setImageResource(R.drawable.ic_mini_mp_pause);
        } else {
            iv_play.setImageResource(R.drawable.ic_mini_mp_play);
        }
    }

    public void updateTimings(int current_time, int media_duration) {
        tv_timings.setText(UtilityFunction.getMinSec(current_time) + " / " + UtilityFunction.getMinSec(media_duration));
//        tv_name.setSelected(true);
//        tv_name.setFocusableInTouchMode(true);
//        tv_name.setFocusable(true);
    }

    //episode_id,uri,title

    public void downloadSong(HomeContentPOJO homeContentPOJO) {

        PodcastEpisodeDetailsPOJO podcastEpisodeDetailsPOJO = new PodcastEpisodeDetailsPOJO();
        podcastEpisodeDetailsPOJO.setEpisodeId(homeContentPOJO.getConId());
        podcastEpisodeDetailsPOJO.setStreamUri(homeContentPOJO.getCotDeepLink());
        podcastEpisodeDetailsPOJO.setDescription(homeContentPOJO.getDescription());
        podcastEpisodeDetailsPOJO.setTitle(homeContentPOJO.getConName());
        podcastEpisodeDetailsPOJO.setImgLocalUri(homeContentPOJO.getImgIrl());
        podcastEpisodeDetailsPOJO.setImgRemoteUri(homeContentPOJO.getImgIrl());

        Log.d(TagUtils.getTag(), "downloading song");
        Intent serviceIntent = new Intent(HomeActivity.this, DownloadSongService.class);
        serviceIntent.putExtra("download_type", "normal");
        serviceIntent.putExtra("type", "podcast");
        serviceIntent.putExtra("podcast_episode", podcastEpisodeDetailsPOJO);
        startService(serviceIntent);
    }

    public void downloadSong(PodcastEpisodeDetailsPOJO podcastEpisodeDetailsPOJO) {
        Log.d(TagUtils.getTag(), "downloading song");
        Intent serviceIntent = new Intent(HomeActivity.this, DownloadSongService.class);
        serviceIntent.putExtra("download_type", "normal");
        serviceIntent.putExtra("type", "podcast");
        serviceIntent.putExtra("podcast_episode", podcastEpisodeDetailsPOJO);
        startService(serviceIntent);
    }

    public void addsong(String type, String pojo) {
        EasyDB easyDB = EasyDB.init(this, "Lystn").setTableName("downloads");
//        easyDB.deleteAllDataFromTable();
        boolean done = easyDB
                .addData("type", type)
                .addData("pojo", pojo)
                .doneDataAdding();

        readAllData();
    }

    public void downloadSong(DownloadPOJO downloadPOJO) {
        Log.d(TagUtils.getTag(), "downloading song");
        Intent serviceIntent = new Intent(HomeActivity.this, DownloadSongService.class);
        serviceIntent.putExtra("download_type", "list");
        serviceIntent.putExtra("type", "podcast");
        serviceIntent.putExtra("downloadPOJO", downloadPOJO);
        startService(serviceIntent);
    }


    public void openCategoryPodcastFragment(String conId, String name) {
        startFragment(R.id.frame_main, new HomeCateogryPodcastFragment(conId, name));
    }

    ProfileFragment profileFragment;

    public void startProfileFragment() {
        startFragment(R.id.frame_main, profileFragment = new ProfileFragment());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (profileFragment != null) {
            profileFragment.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void refreshMeHeader() {
        if (mefrag!=null){
            mefrag.setProfile();
        }
    }
}
