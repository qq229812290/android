package com.example.facerecognition.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.example.facerecognition.R;

/**
 * 启动页面
 *
 * 3秒钟后进入登录界面
 *
 * 启动界面根据苹果开发者文档可以理解为是用来让用户加快启动的
 * 而不是在上面显示你的广告和商标的（Android开发我们暂时没有找到相关的定义）
 */
public class SplashActivity extends BaseActivety {
 private  Handler handler=new Handler(){
     @Override
     public void handleMessage(@NonNull Message msg) {
         super.handleMessage(msg);
         next();
     }
 };
    private Intent intent;

    private void next() {
        Intent intent = null;
        intent = new Intent(this, GuideActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //去除状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
            }
        },3000);
    }
}
