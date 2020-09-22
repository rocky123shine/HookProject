package com.rocky.hookproject;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.rocky.hookproject.stander.ActivityInterface;
import com.rocky.hookproject.stander.ServiceInterface;

import java.lang.reflect.Constructor;

public class ProxyService  extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String className = intent.getStringExtra("className");
        try {
            Class<?> pluginClass = PluginManager.getInstance(this).getClassLoader().loadClass(className);
            Constructor<?> constructor = pluginClass.getConstructor(new Class[]{});
            Object pluginService = constructor.newInstance(new Object[]{});
            ServiceInterface activityInterface = (ServiceInterface) pluginService;
            activityInterface.insertService(this);

            activityInterface.onStartCommand(intent,flags,startId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
