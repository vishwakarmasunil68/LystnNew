package com.app.lystn.service;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.app.lystn.R;

public class Constants {
    public interface ACTION {
        public static String MAIN_ACTION = "com.app.lystn.action.main";
        public static String INIT_ACTION = "com.app.lystn.action.init";
        public static String PREV_ACTION = "com.app.lystn.action.prev";
        public static String PLAY_ACTION = "com.app.lystn.action.play";
        public static String NEXT_ACTION = "com.app.lystn.action.next";
        public static String STARTFOREGROUND_ACTION = "com.app.lystn.action.startforeground";
        public static String STOPFOREGROUND_ACTION = "com.app.lystn.action.stopforeground";

    }

    public interface NOTIFICATION_ID {
        public static int FOREGROUND_SERVICE = 101;
    }

    public static Bitmap getDefaultAlbumArt(Context context) {
        Bitmap bm = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        try {
            bm = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.ic_launcher_background, options);
        } catch (Error ee) {
        } catch (Exception e) {
        }
        return bm;
    }
}
