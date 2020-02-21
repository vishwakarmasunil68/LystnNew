package com.app.lystn.util;

import android.content.Context;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class GenreFollowUtil {

    public static void addFollowGenre(Context context,String follow_id){
        List<String> genreList=getGenreList(context);
        genreList.add(follow_id);
        saveGenreList(context,genreList);
    }
    
    public static boolean checkFollowGenreExist(Context context,String follow_id){
        List<String> genreList=getGenreList(context);
        return genreList.contains(follow_id);
    }
    
    public static void saveGenreList(Context context,List<String> stringList){
        try{
            String data="";
            for(int i=0;i<stringList.size();i++){
                if((i+1)==stringList.size()){
                    data+=stringList.get(i);
                }else{
                    data+=stringList.get(i)+",";
                }
            }
            Pref.SetStringPref(context,StringUtils.GENRE_FOLLOW_UP,data);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public static List<String> getGenreList(Context context){
        try{
            ArrayList<String> listdata = new ArrayList<String>();
            JSONArray jArray = new JSONArray(Pref.GetStringPref(context,StringUtils.GENRE_FOLLOW_UP,""));
            if (jArray != null) {
                for (int i=0;i<jArray.length();i++){
                    listdata.add(jArray.getString(i));
                }
            }
            return listdata;
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

}
