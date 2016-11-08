package com.krislq.we;

import android.app.Application;

import com.growingio.android.sdk.collection.GrowingIO;

/**
 * Created by kris on 2016/11/7 11:36.
 * Packaged in WithoutEmbedding:com.krislq.we.
 */

public class WeApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(WeLifecycleCallBacks.getInstance());
//        GrowingIO.startTracing(this,"9d9b91f0c31cb5e7");
//        GrowingIO.setScheme("growing.751f0625f3b3d8af");
    }
}
