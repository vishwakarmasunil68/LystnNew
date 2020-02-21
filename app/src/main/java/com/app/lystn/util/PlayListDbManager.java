package com.app.lystn.util;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.google.gson.Gson;
import com.app.lystn.pojo.artiste.PodcastDetailPOJO;
import com.app.lystn.pojo.home.HomeContentPOJO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import p32929.androideasysql_library.Column;
import p32929.androideasysql_library.EasyDB;

public class PlayListDbManager {

    Context context;
    EasyDB easyDB;

    private PlayListDbManager(Context context) {
        this.context = context;
    }

    public static PlayListDbManager initDB(Context context) {
        PlayListDbManager dbManager = new PlayListDbManager(context);
        dbManager.easyDB = initTable(context);
        return dbManager;
    }

    public static EasyDB initTable(Context context) {
        EasyDB easyDB = EasyDB.init(context, "Lystnplaylist")
                .setTableName("playlist")
                .addColumn(new Column("podcast_id", new String[]{"text", "unique"}))
                .addColumn(new Column("type", new String[]{"text", "not null"}))
                .addColumn(new Column("homecontent", new String[]{"text", "not null"}))
                .addColumn(new Column("podcast", new String[]{"text", "not null"}))
                .doneTableColumn();
        return easyDB;
    }

    public void savePlayedSong(String id, String type, String episode_pojo, String podcast) {
        if (checkSongInDB(id)) {
            removeSong(id);
        }
        boolean done = easyDB
                .addData("podcast_id", id)
                .addData("type", type)
                .addData("homecontent", episode_pojo)
                .addData("podcast", podcast)
                .doneDataAdding();

        Log.d(TagUtils.getTag(), "song saved in db" + done);
    }

    public boolean checkSongInDB(String podcast_id) {
        Cursor res = easyDB.searchInColumn("podcast_id", podcast_id, -1); // Here we passed limit = -1. Thus it will return all the rows with the matched column values
        if (res != null) {
            while (res.moveToNext()) {
                return true;
            }
        }
        return false;
    }

    public List<HomeContentPOJO> readAllSongs() {
        Cursor res = easyDB.getAllData();
        List<HomeContentPOJO> homeContentPOJOS = new ArrayList<>();
        while (res.moveToNext()) {
//            Log.d(TagUtils.getTag(), "data reading:- col1:-" + res.getString(0) + ",col2:-" + res.getString(1) + ",col3:-" + res.getString(2));
            HomeContentPOJO homeContentPOJO = new Gson().fromJson(res.getString(3), HomeContentPOJO.class);
            if (res.getString(4).length() > 0) {
                PodcastDetailPOJO podcastPOJO = new Gson().fromJson(res.getString(4), PodcastDetailPOJO.class);
                homeContentPOJO.setPodcastDetailPOJO(podcastPOJO);
            }
            homeContentPOJOS.add(homeContentPOJO);
        }
        Collections.reverse(homeContentPOJOS);
        return homeContentPOJOS;
    }

    public boolean removeSong(String podcast_id) {
        boolean deleted = easyDB.deleteRow("podcast_id", podcast_id);
        return deleted;
    }

    public void closeDB() {
        easyDB.close();
    }


}
