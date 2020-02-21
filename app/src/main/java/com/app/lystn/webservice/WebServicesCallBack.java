package com.app.lystn.webservice;

/**
 * Created by sunil on 29-12-2016.
 */

public interface WebServicesCallBack {
    public void onGetMsg(String apicall, String response);
    public void onErrorMsg(String status_code,String response);
}
