package com.example.facerecognition.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.example.facerecognition.R;
import com.example.facerecognition.utils.SharedPreferencesUtil;

public class GuideActivity extends BaseActivety implements View.OnClickListener {

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        //去除状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Button bt_login_or_register = findViewById(R.id.bt_login_or_register);
        Button bt_enter = findViewById(R.id.bt_enter);
        bt_login_or_register.setOnClickListener(this);
        bt_enter.setOnClickListener(this);
        //把sp创建成一个实例变量这样在之后的Login方法中才能访问到快捷键：
        sp = SharedPreferencesUtil.getInstance(getApplicationContext());
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_login_or_register:
                bt_login_or_register();
                break;
            case R.id.bt_enter:
                bt_enter();
                break;
        }
    }


    private  void bt_login_or_register(){
        intent = new Intent(this, EntranceActivity.class);
        startActivity(intent);
        finish();
    }
    private  void bt_enter(){
        intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}

