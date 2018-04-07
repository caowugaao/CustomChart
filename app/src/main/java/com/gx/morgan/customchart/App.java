package com.gx.morgan.customchart;

import android.app.Application;
import android.util.Log;

/**
 * description：
 * <br>author：caowugao
 * <br>time：2018/4/7 15:46
 */
public class App extends Application{
    private static final String TAG = "App";
    @Override
    public void onCreate() {
        super.onCreate();
        SimpleCrashHandler.getInstance().addOnCrashListener(new SimpleCrashHandler.OnCrashListener() {
            @Override
            public void onCrash(Thread thread, Throwable t) {
                Log.e(TAG, "onCrash: thread="+thread.getName()+". msg="+t );
            }
        });
    }
}
