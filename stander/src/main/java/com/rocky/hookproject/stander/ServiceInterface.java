package com.rocky.hookproject.stander;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

public interface ServiceInterface {

    void insertService(Service service);

    void onCreate();

    int onStartCommand(Intent intent, int flags, int startId);


    void onDestroy();
}
