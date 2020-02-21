package com.app.lystn.webservice;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.gson.Gson;
import com.ixuea.android.downloader.DownloadService;
import com.ixuea.android.downloader.callback.DownloadListener;
import com.ixuea.android.downloader.domain.DownloadInfo;
import com.ixuea.android.downloader.exception.DownloadException;
import com.app.lystn.R;
import com.app.lystn.pojo.artiste.PodcastDetailPOJO;
import com.app.lystn.pojo.artiste.PodcastEpisodeDetailsPOJO;
import com.app.lystn.util.DbManager;
import com.app.lystn.util.TagUtils;
import com.app.lystn.util.ToastClass;
import com.app.lystn.util.UtilityFunction;

import java.io.File;

public class DownloadSongManager {

    Context context;
    DbManager dbManager;
    Object object;

    com.ixuea.android.downloader.callback.DownloadManager downloadManager;

    public DownloadSongManager(Context context, DbManager dbManager, Object object) {
        this.context = context;
        this.dbManager=dbManager;
        this.object=object;
        downloadManager = DownloadService.getDownloadManager(context);
    }

    public void downloadSong(PodcastEpisodeDetailsPOJO podcastEpisodeDetailsPOJO, PodcastDetailPOJO podcastDetailPOJO){
        String file_name = UtilityFunction.createTestingDir(context) + File.separator + podcastEpisodeDetailsPOJO.getEpisodeId() + ".mp3";
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

                    ToastClass.showShortToast(context, "Download started");

                    mNotifyManager[0] = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


                    String channelId = "channel-01";
                    String channelName = "Channel Name";
                    int importance = NotificationManager.IMPORTANCE_HIGH;

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        NotificationChannel mChannel = new NotificationChannel(
                                channelId, channelName, importance);
                        mNotifyManager[0].createNotificationChannel(mChannel);
                    }

                    mBuilder[0] = new NotificationCompat.Builder(context, channelId);
                    mBuilder[0].setContentTitle(podcastEpisodeDetailsPOJO.getTitle().trim())
                            .setContentText("Download in progress")
                            .setSmallIcon(R.mipmap.ic_launcher);
                }

                @Override
                public void onWaited() {
                }

                @Override
                public void onPaused() {
                }

                @Override
                public void onDownloading(long progress, long size) {

                    mBuilder[0].setProgress((int) size, (int) progress, false);
                    mNotifyManager[0].notify(notificationId, mBuilder[0].build());

                    Log.d(TagUtils.getTag(), "progress:-" + progress + "/size:-" + size);
                }

                @Override
                public void onRemoved() {
                    downloadInfo[0] = null;
                }

                @Override
                public void onDownloadSuccess() {
                    ToastClass.showShortToast(context, "Download Complete");
                    mBuilder[0].setContentText("Download completed")
                            // Removes the progress bar
                            .setProgress(0, 0, false);
                    mNotifyManager[0].notify(notificationId, mBuilder[0].build());


                    saveSongToDB(podcastEpisodeDetailsPOJO,podcastDetailPOJO);

                    podcastEpisodeDetailsPOJO.setService_downloaded(true);
                    if (object != null) {
                        DownloadCallback mcallback = (DownloadCallback) object;
                        mcallback.onSuccessDownload(podcastEpisodeDetailsPOJO.getEpisodeId());
                    } else {
                        ToastClass.showShortToast(context, "Something went wrong");
                    }
                }

                @Override
                public void onDownloadFailed(DownloadException e) {
                    e.printStackTrace();
                    Log.d(TagUtils.getTag(), "Download fail:" + e.getMessage());
                    if (object != null) {
                        DownloadCallback mcallback = (DownloadCallback) object;
                        mcallback.onDownloadFailed("Downloading Failed");
                    } else {
                        ToastClass.showShortToast(context, "Something went wrong");
                    }
                }
            });

            downloadManager.download(downloadInfo[0]);
        } else {
            saveSongToDB(podcastEpisodeDetailsPOJO,podcastDetailPOJO);
            ToastClass.showShortToast(context, "Download Complete");
            if (object != null) {
                DownloadCallback mcallback = (DownloadCallback) object;
                mcallback.onSuccessDownload(podcastEpisodeDetailsPOJO.getEpisodeId());
            } else {
                ToastClass.showShortToast(context, "Something went wrong");
            }
        }
    }

    public void saveSongToDB(PodcastEpisodeDetailsPOJO podcastEpisodeDetailsPOJO,PodcastDetailPOJO podcastDetailPOJO){

        String podcast="";
        if(podcastDetailPOJO!=null){
            podcast=new Gson().toJson(podcastDetailPOJO);
        }

        dbManager.saveDownloadedSong(podcastEpisodeDetailsPOJO.getEpisodeId(), "podcast", new Gson().toJson(podcastEpisodeDetailsPOJO),
                podcast);
    }

}
