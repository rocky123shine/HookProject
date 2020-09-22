package com.rocky.hookproject;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.rocky.hookproject.stander.ActivityInterface;

import java.lang.reflect.Constructor;


public class ProxyActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String className = getIntent().getStringExtra("className");
        try {
            Class<?> pluginClass = getClassLoader().loadClass(className);
            Constructor<?> constructor = pluginClass.getConstructor(new Class[]{});
            Object pluginActivity = constructor.newInstance(new Object[]{});
            ActivityInterface activityInterface = (ActivityInterface) pluginActivity;
            activityInterface.insertAppContext(this);
            Bundle bundle = new Bundle();
            bundle.putString("name", "jajaj");
            activityInterface.onCreate(bundle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Resources getResources() {
        return PluginManager.getInstance(this).getResources();
    }

    @Override
    public ClassLoader getClassLoader() {
        return PluginManager.getInstance(this).getClassLoader();
    }

    @Override
    public void startActivity(Intent intent) {
        String className = intent.getStringExtra("className");
        Intent intent1 = new Intent(this, ProxyActivity.class);
        intent1.putExtra("className", className);
        super.startActivity(intent1);
    }


    @Override
    public ComponentName startService(Intent service) {
        String className = service.getStringExtra("className");
        Intent intent1 = new Intent(this, ProxyService.class);
        intent1.putExtra("className", className);
        return super.startService(intent1);
    }

    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        String pluginReceiverClassName = receiver.getClass().getName();

        return super.registerReceiver(new ProxyReceiver(pluginReceiverClassName), filter);
    }


    public void sendBroadcast(Intent intent) {
        super.sendBroadcast(intent);
    }
}
