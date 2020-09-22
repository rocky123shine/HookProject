package com.rocky.hookproject.plugin_package;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.rocky.hookproject.stander.ActivityInterface;

public class BaseActivity extends Activity implements ActivityInterface {

    public Activity appActivity;

    @Override
    public void insertAppContext(Activity activity) {
        appActivity = activity;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onStart() {

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onResume() {

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onPause() {

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onStop() {

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRestart() {

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onDestroy() {

    }

    @Override
    public ClassLoader getClassLoader() {
        return appActivity.getClassLoader();
    }

    @Override
    public Resources getResources() {
        return appActivity.getResources();
    }

    public void setContentView(int layoutResID) {
        appActivity.setContentView(layoutResID);
    }

    public <T extends View> T findViewById(int id) {
        return appActivity.findViewById(id);
    }

    public void startActivity(Intent intent) {
        Intent newIntent = new Intent();
        newIntent.putExtra("className", intent.getComponent().getClassName());
        appActivity.startActivity(newIntent);
    }

    public ComponentName startService(Intent service) {
        Intent newIntent = new Intent();
        newIntent.putExtra("className", service.getComponent().getClassName());

        return appActivity.startService(newIntent);
    }


    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        return appActivity.registerReceiver(receiver, filter);
    }

    public void sendBroadcast(Intent intent) {
        appActivity.sendBroadcast(intent);
    }
}
