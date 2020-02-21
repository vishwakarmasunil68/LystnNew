package com.app.lystn;

import android.app.Application;
import android.content.Context;

import com.danikula.videocache.HttpProxyCacheServer;

public class Lystn extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    private HttpProxyCacheServer proxy;

    public static HttpProxyCacheServer getProxy(Context context) {
        Lystn app = (Lystn) context.getApplicationContext();
        return app.proxy == null ? (app.proxy = app.newProxy()) : app.proxy;
    }

    private HttpProxyCacheServer newProxy() {
        return new HttpProxyCacheServer(this);
    }
//
//    private static HttpProxyCacheServer proxy;
//
//    public static HttpProxyCacheServer getProxy(Context context) {
//        Lystn app = (Lystn) context.getApplicationContext();
//        return app.proxy == null ? (app.proxy = app.newProxy()) : app.proxy;
//    }
//
//    private HttpProxyCacheServer newProxy() {
//        if (proxy != null) {
//            return proxy;
//        } else {
//            proxy = new HttpProxyCacheServer(this);
//            return proxy;
//        }
//    }
}
