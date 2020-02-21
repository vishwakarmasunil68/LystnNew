package com.app.lystn.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.ixuea.android.downloader.DownloadService;
import com.ixuea.android.downloader.callback.DownloadListener;
import com.ixuea.android.downloader.callback.DownloadManager;
import com.ixuea.android.downloader.domain.DownloadInfo;
import com.ixuea.android.downloader.exception.DownloadException;
import com.app.lystn.R;
import com.app.lystn.pojo.DownloadPOJO;
import com.app.lystn.pojo.artiste.PodcastEpisodeDetailsPOJO;
import com.app.lystn.util.StringUtils;
import com.app.lystn.util.TagUtils;
import com.app.lystn.util.ToastClass;
import com.app.lystn.util.UtilityFunction;

import java.io.File;
import java.util.List;

import p32929.androideasysql_library.EasyDB;

public class DownloadSongService extends Service {

    DownloadManager downloadManager;
//    String conId;
//    String url;
//    String file_name;




    String download_type = "normal";
    List<PodcastEpisodeDetailsPOJO> podcastEpisodeDetailsPOJOS;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        downloadManager = DownloadService.getDownloadManager(getApplicationContext());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TagUtils.getTag(), "on start");
//        conId = intent.getStringExtra("conId");
//        url = intent.getStringExtra("url");
//
//        Log.d(TagUtils.getTag(), "onstart command");
//
//        file_name = UtilityFunction.createTestingDir(getApplicationContext()) + File.separator +
//                conId + ".mp3";
//
//        Log.d(TagUtils.getTag(), "file name:-" + file_name);
//
//        startDownload();
        try {
            download_type = intent.getStringExtra("download_type");
            String type = intent.getStringExtra("type");
            if (download_type.equalsIgnoreCase("list")) {
                 podcastEpisodeDetailsPOJOS=((DownloadPOJO)intent.getSerializableExtra("downloadPOJO")).getPodcastEpisodeDetailsPOJOS();
                for(PodcastEpisodeDetailsPOJO podcastEpisodeDetailsPOJO:podcastEpisodeDetailsPOJOS){
                    startDownload(type,podcastEpisodeDetailsPOJO);
                }
            } else {
                PodcastEpisodeDetailsPOJO podcastEpisodeDetailsPOJO = (PodcastEpisodeDetailsPOJO) intent.getSerializableExtra("podcast_episode");
                Log.d(TagUtils.getTag(), podcastEpisodeDetailsPOJO.toString());
                startDownload(type, podcastEpisodeDetailsPOJO);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return START_NOT_STICKY;
    }

    public void checkSonginDB() {
        EasyDB easyDB = EasyDB.init(getApplicationContext(), "Lystn").setTableName("downloads");
        Cursor res = easyDB.searchInColumn("ID", "data", -1); // Here we passed limit = -1. Thus it will return all the rows with the matched column values
        if (res != null) {
            while (res.moveToNext()) {
                String ID = res.getString(0); // Column 0 is the ID column
                String c1 = res.getString(1);
                String c2 = res.getString(2);
            }
        }
    }

    private void startDownload(String type, PodcastEpisodeDetailsPOJO podcastEpisodeDetailsPOJO) {

        String file_name = UtilityFunction.createTestingDir(getApplicationContext()) + File.separator + podcastEpisodeDetailsPOJO.getEpisodeId() + ".mp3";
        if (!new File(file_name).exists()) {
            final DownloadInfo[] downloadInfo = {new DownloadInfo.Builder().setUrl(podcastEpisodeDetailsPOJO.getStreamUri().trim())
                    .setPath(file_name)
                    .build()};

            final NotificationManager[] mNotifyManager = new NotificationManager[1];
            final NotificationCompat.Builder[] mBuilder = new NotificationCompat.Builder[1];
            int notificationId = (int) System.currentTimeMillis();

            downloadInfo[0].setDownloadListener(new DownloadListener() {

                @Override
                public void onStart() {
                    Log.d(TagUtils.getTag(), "Prepare downloading");

                    ToastClass.showShortToast(getApplicationContext(), "Download started");

                    mNotifyManager[0] = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


                    String channelId = "channel-01";
                    String channelName = "Channel Name";
                    int importance = NotificationManager.IMPORTANCE_HIGH;

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        NotificationChannel mChannel = new NotificationChannel(
                                channelId, channelName, importance);
                        mNotifyManager[0].createNotificationChannel(mChannel);
                    }

                    mBuilder[0] = new NotificationCompat.Builder(getApplicationContext(), channelId);
                    mBuilder[0].setContentTitle(podcastEpisodeDetailsPOJO.getTitle().trim())
                            .setContentText("Download in progress")
                            .setSmallIcon(R.mipmap.ic_launcher);
                }

                @Override
                public void onWaited() {
                    Log.d(TagUtils.getTag(), "Waiting");
                    Log.d(TagUtils.getTag(), "Pause");
                }

                @Override
                public void onPaused() {
                    Log.d(TagUtils.getTag(), "Continue");
                    Log.d(TagUtils.getTag(), "Paused");
                }

                @Override
                public void onDownloading(long progress, long size) {

                    mBuilder[0].setProgress((int) size, (int) progress, false);
                    mNotifyManager[0].notify(notificationId, mBuilder[0].build());

                    Log.d(TagUtils.getTag(), "progress:-" + progress + "/size:-" + size);
                    Log.d(TagUtils.getTag(), "Pause");
                }

                @Override
                public void onRemoved() {
                    Log.d(TagUtils.getTag(), "Download");
                    Log.d(TagUtils.getTag(), "");
                    downloadInfo[0] = null;
                }

                @Override
                public void onDownloadSuccess() {
                    ToastClass.showShortToast(getApplicationContext(), "Download Complete");
                    Log.d(TagUtils.getTag(), "Delete");
                    Log.d(TagUtils.getTag(), "Download success");
                    mBuilder[0].setContentText("Download completed")
                            // Removes the progress bar
                            .setProgress(0, 0, false);
                    mNotifyManager[0].notify(notificationId, mBuilder[0].build());

                    updatHomeActivity(StringUtils.DOWNLOAD_SONG_TYPE, podcastEpisodeDetailsPOJO);

                    podcastEpisodeDetailsPOJO.setService_downloaded(true);

                    if (download_type.equalsIgnoreCase("normal")) {
                        stopSelf();
                    } else {
                        if (checkAllDownloaded()) {
                            stopSelf();
                        }
                    }
                }

                @Override
                public void onDownloadFailed(DownloadException e) {
                    e.printStackTrace();
                    Log.d(TagUtils.getTag(), "Download fail:" + e.getMessage());
                }
            });

            downloadManager.download(downloadInfo[0]);
        } else {
            ToastClass.showShortToast(getApplicationContext(), "Download Complete");
            updatHomeActivity(StringUtils.DOWNLOAD_SONG_TYPE, podcastEpisodeDetailsPOJO);
            if (!download_type.equalsIgnoreCase("list")) {
                stopSelf();
            } else {
                if (checkAllDownloaded()) {
                    stopSelf();
                }
            }
        }
    }

    public boolean checkAllDownloaded() {
        for (PodcastEpisodeDetailsPOJO episodeDetailsPOJO : podcastEpisodeDetailsPOJOS) {
            if (!episodeDetailsPOJO.isService_downloaded()) {
                return false;
            }
        }
        return true;
    }


//    public void initDB() {
//        //type:-  podcast song =1
//        EasyDB easyDB = EasyDB.init(this, "Lystn") // "TEST" is the name of the DATABASE
//                .setTableName("downloads")  // You can ignore this line if you want
////                .addColumn(new Column("id", new String[]{"text", "unique"}))
//                .addColumn(new Column("type", new String[]{"text", "not null"}))
//                .addColumn(new Column("pojo", new String[]{"text"}))
//                .doneTableColumn();
//
//        addsong();
//    }
//
//    public void addsong(String type, String pojo) {
//        EasyDB easyDB = EasyDB.init(this, "Lystn").setTableName("downloads");
//        boolean done = easyDB
//                .addData("type", type)
//                .addData("pojo", pojo)
//                .doneDataAdding();
//
//        readAllData();
//    }

    public void updatHomeActivity(String type, PodcastEpisodeDetailsPOJO
            podcastEpisodeDetailsPOJO) {
        try {
            Intent intent = new Intent(StringUtils.UPDATE_HOME_ACTIVITY);
            intent.putExtra("type", StringUtils.SAVE_SONG_DB);
            intent.putExtra("podcast_id", podcastEpisodeDetailsPOJO.getEpisodeId());
            intent.putExtra("pojo", podcastEpisodeDetailsPOJO);
            sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//
//    public void readAllData() {
//        EasyDB easyDB = EasyDB.init(this, "Lystn").setTableName("downloads");
//        Cursor res = easyDB.getAllData();
//        while (res.moveToNext()) {
//            Log.d(TagUtils.getTag(), "data reading:- col1:-" + res.getString(0) + ",col2:-" + res.getString(1) + ",col3:-" + res.getString(2));
//        }
//    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d(TagUtils.getTag(), "on destroy service");
    }
}
