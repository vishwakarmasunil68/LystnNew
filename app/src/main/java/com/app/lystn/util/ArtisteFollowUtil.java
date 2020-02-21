package com.app.lystn.util;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class ArtisteFollowUtil {

    public static void addFollowArtiste(Context context,String follow_id){
        List<String> artisteList=getArtisteList(context);
        artisteList.add(follow_id);
        saveArtisteList(context,artisteList);
    }

    public static boolean checkFollowArtisteExist(Context context,String follow_id){
        List<String> artisteList=getArtisteList(context);
        return artisteList.contains(follow_id);
    }

    public static void saveArtisteList(Context context,List<String> stringList){
        try{
            String data="";
            for(int i=0;i<stringList.size();i++){
                if((i+1)==stringList.size()){
                    data+=stringList.get(i);
                }else{
                    data+=stringList.get(i)+",";
                }
            }
            Pref.SetStringPref(context,StringUtils.ARTISTE_FOLLOW_UP,data);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static List<String> getArtisteList(Context context){
        try{
            String data=Pref.GetStringPref(context,StringUtils.ARTISTE_FOLLOW_UP,"");
            ArrayList<String> listdata = new ArrayList<String>();
//            JSONArray jArray = new JSONArray();
            if (!data.equalsIgnoreCase("")) {
                String[] data_arr=data.split(",");
                for (int i=0;i<data_arr.length;i++){
                    listdata.add(data_arr[i]);
                }
            }
            return listdata;
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static void removeArtisteFromList(Context context,String follow_id){
        try{
            List<String> artisteList=getArtisteList(context);
            artisteList.remove(follow_id);
            saveArtisteList(context,artisteList);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
