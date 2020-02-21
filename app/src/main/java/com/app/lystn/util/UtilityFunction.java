package com.app.lystn.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;
import com.app.lystn.pojo.UserPOJO;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UtilityFunction {

    public static UserPOJO getUserPOJO(Context context) {
        try {
            UserPOJO userPOJO = new Gson().fromJson(Pref.GetStringPref(context, StringUtils.PROFILE_DATA, ""), UserPOJO.class);
            return userPOJO;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getAppDirectory(Context context) {
        PackageManager m = context.getPackageManager();
        String s = context.getPackageName();
        try {
            PackageInfo p = m.getPackageInfo(s, 0);
            s = p.applicationInfo.dataDir;
            Log.d(TagUtils.getTag(), "data directory:-" + s);
            return s;
        } catch (PackageManager.NameNotFoundException e) {
            Log.w("yourtag", "Error Package name not found ", e);
            return "";
        }
    }

    public static String createAppSongDir(Context context) {
        File file = new File(getAppDirectory(context) + File.separator + "downsong");
        if (!file.exists()) {
            file.mkdirs();
        }

        return file.getPath();
    }

    public static String createTestingDir(Context context) {
        File file = new File(Environment.getExternalStorageDirectory() + File.separator + "downsong");
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getPath();
    }

    public static String parseDT(String dt) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date d = simpleDateFormat.parse(dt);
            return new SimpleDateFormat("dd MMM, YYYY").format(d);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static String getMinSec(int current_time) {
//        int total_seconds = (int) (current_time / 1000);
//        int minutes = total_seconds / 60;
//        int seconds = total_seconds % 60;
//
//        String min = "";
//
//        if (minutes < 10) {
//            min = "0" + String.valueOf(minutes);
//        } else {
//            min = String.valueOf(minutes);
//        }
//
//        String sec = "";
//
//        if (seconds < 10) {
//            sec = "0" + String.valueOf(seconds);
//        } else {
//            sec = String.valueOf(seconds);
//        }
//
//        return min + " : " + sec;
        int timeInSeconds = (int) (current_time / 1000);
        int secondsLeft = timeInSeconds % 3600 % 60;
        int minutes = (int) Math.floor(timeInSeconds % 3600 / 60);
        int hours = (int) Math.floor(timeInSeconds / 3600);

        String HH = ((hours < 10) ? "0" : "") + hours;
        String MM = ((minutes < 10) ? "0" : "") + minutes;
        String SS = ((secondsLeft < 10) ? "0" : "") + secondsLeft;

        if (HH.equalsIgnoreCase("00")) {
            return MM + ":" + SS;
        }else{
            return HH + ":" + MM + ":" + SS;
        }
    }

    public static String convertSecToHHMMSS(int current_time) {
        int timeInSeconds = current_time;
        int secondsLeft = timeInSeconds % 3600 % 60;
        int minutes = (int) Math.floor(timeInSeconds % 3600 / 60);
        int hours = (int) Math.floor(timeInSeconds / 3600);

        String HH = ((hours < 10) ? "0" : "") + hours;
        String MM = ((minutes < 10) ? "0" : "") + minutes;
        String SS = ((secondsLeft < 10) ? "0" : "") + secondsLeft;

        if (HH.equalsIgnoreCase("00")) {
            return MM + ":" + SS;
        }else{
            return HH + ":" + MM + ":" + SS;
        }
    }

    private static char[] c = new char[]{'K', 'M', 'B', 'T'};

    public static String coolFormat(double n, int iteration) {
        double d = ((long) n / 100) / 10.0;
        boolean isRound = (d * 10) %10 == 0;//true if the decimal part is equal to 0 (then it's trimmed anyway)
        return (d < 1000? //this determines the class, i.e. 'k', 'm' etc
                ((d > 99.9 || isRound || (!isRound && d > 9.99)? //this decides whether to trim the decimals
                        (int) d * 10 / 10 : d + "" // (int) d * 10 / 10 drops the decimal
                ) + "" + c[iteration])
                : coolFormat(d, iteration+1));

    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

}
