package com.rocky.hookproject.plugin_package;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.rocky.hookproject.stander.ReceiverInterface;

public class MyReceiverqqqqq extends BroadcastReceiver implements ReceiverInterface {
      @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "插件内广播接收者", Toast.LENGTH_SHORT).show();
    }
}
