package com.example.facerecognition.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtil {
    private  static  final String TAG ="TAG";
    private  static  final String KEY_LOGIN ="KEY_LOGIN";
    private static SharedPreferences mPreferences;
    private static SharedPreferences.Editor  mEditor;
    private static SharedPreferencesUtil mSharedPreferenceUtil;
    private final Context context;

//构造方法
    public SharedPreferencesUtil(Context context) {
        this.context = context.getApplicationContext();
        mPreferences=this.context.getSharedPreferences(TAG,context.MODE_PRIVATE);
        mEditor =mPreferences.edit();
    }
    /**android中没有java web 那样的高并发量，所以单例都可以简单实现
     * @param  context
     * @return
     * 单例简单实现，因为
     */
    public static SharedPreferencesUtil getInstance(Context context){
        if(mSharedPreferenceUtil==null){
            mSharedPreferenceUtil=new SharedPreferencesUtil(context);
        }
        return mSharedPreferenceUtil;
    }
    /**
     * 判断是否登录
     * @return
     */
    public boolean isLogin(){
        return  getBoolean(KEY_LOGIN,false);
    }
    /**
     * 更改登录状态
     * @param  value
     */
    public void setLogin (boolean value){
        putBoolean(KEY_LOGIN,value);
    }
    //私有方法
    private void put(String key,String value){
        mEditor.putString(key,value);
        mEditor.commit();
    }
    private void putBoolean(String key,boolean value){
        mEditor.putBoolean(key,value);
        mEditor.commit();
    }
    private String get(String key){
        return  mPreferences.getString(key, "");
    }
    private boolean getBoolean(String key,boolean defaultValue){
        return  mPreferences.getBoolean(key,defaultValue);
    }
    private void putInt(String key,int value){
        mEditor.putInt(key,value);
        mEditor.apply();
    }
    private int getInt(String key,int defaultValue){
        return mPreferences.getInt(key, defaultValue);
    }
    //end 私有方法
}
