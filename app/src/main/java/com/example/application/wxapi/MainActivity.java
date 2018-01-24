package com.example.application.wxapi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.example.application.BuildConfig;
import com.example.application.R;
import wx.callback.WXActivity;

@WXActivity(BuildConfig.APPLICATION_ID)
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
