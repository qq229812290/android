package com.example.facerecognition.activity;

import androidx.appcompat.app.AppCompatActivity;

import com.example.facerecognition.utils.SharedPreferencesUtil;


public class BaseActivety extends AppCompatActivity {
    /**
     * 访问修饰符改为protected
     */
protected SharedPreferencesUtil sp;
/**
 * 重写了setContentView方法
 * 因为在子类调用了setContentView设置布局
 * @param layoutResID
 */
    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        SharedPreferencesUtil sp = SharedPreferencesUtil.getInstance(getApplicationContext());
    }

}
