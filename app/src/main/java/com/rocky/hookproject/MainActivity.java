package com.rocky.hookproject;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.util.Objects;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_static_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PluginManager.getInstance(MainActivity.this).parseApkAction();
            }
        });
        findViewById(R.id.btn_static_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("com.qqqqq");
                sendBroadcast(intent);
            }
        });
    }

    public void load(View view) {

        PluginManager.getInstance(this).loadPlugin();

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void start(View view) {

        try {

            File file = new File(Environment.getExternalStorageDirectory() + File.separator + "plugin_package-debug.apk");
            String pluginPath = file.getAbsolutePath();

            PackageManager packageManager = getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageArchiveInfo(pluginPath, PackageManager.GET_ACTIVITIES);

            ActivityInfo activityInfo = packageInfo.activities[0];
            Intent intent = new Intent(this, ProxyActivity.class);
            intent.putExtra("className", activityInfo.name);
            startActivity(intent);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}