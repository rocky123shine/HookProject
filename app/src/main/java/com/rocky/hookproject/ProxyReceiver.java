package com.rocky.hookproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.rocky.hookproject.stander.ReceiverInterface;
import com.rocky.hookproject.stander.ServiceInterface;

public class ProxyReceiver extends BroadcastReceiver {
    private String pluginClassName;
    public static final String TAG = ProxyReceiver.class.getSimpleName();

    public ProxyReceiver(String pluginReceiverClassName) {
        pluginClassName = pluginReceiverClassName;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Log.d(TAG, "onReceive: " + pluginClassName);
            Class aClass = PluginManager.getInstance(context).getClassLoader()
                    .loadClass(pluginClassName);
            ReceiverInterface receiverInterface =
                    (ReceiverInterface) aClass.newInstance();
            receiverInterface.onReceive(context, intent);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
