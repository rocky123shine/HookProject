package com.rocky.hookproject.plugin_package;

import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

public class TestService extends BaseService {
    public static final String TAG = TestService.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(1000);
                Log.d(TAG, "run: 插件run。。。。。");
            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
