package com.rocky.hookproject.plugin_package;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class PluginActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plugin);
        Toast.makeText(appActivity, savedInstanceState.getString("name"), Toast.LENGTH_SHORT).show();
        findViewById(R.id.btn_jump).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(appActivity, TestActivity.class));
            }
        });
        findViewById(R.id.btn_start_service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startService(new Intent(appActivity, TestService.class));
            }
        });

        findViewById(R.id.btn_register_receiver).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentFilter filter = new IntentFilter();
                filter.addAction("com.rocky.receiver");
                registerReceiver(new MyReceiverqqqqq(), filter);
            }
        });

        findViewById(R.id.btn_send_receiver).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("com.rocky.receiver");
                sendBroadcast(intent);
            }
        });
    }
}
