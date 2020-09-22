package com.rocky.hookproject.stander;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

public interface ActivityInterface {
    void insertAppContext(Activity activity);

    void onCreate(@Nullable Bundle savedInstanceState);


    void onStart();


    void onResume();


    void onPause();


    void onStop();


    void onRestart();


    void onDestroy();
}
