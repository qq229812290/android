package com.example.facerecognition.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baidu.aip.util.Base64Util;
import com.example.facerecognition.MyApplication;
import com.example.facerecognition.R;
import com.example.facerecognition.utils.GsonUtils;
import com.example.facerecognition.utils.HttpUtil;
import com.mysql.jdbc.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FaceContrastActivity extends AppCompatActivity {
    private MyApplication myApplication;
    private ImageView photo1;
    private ImageView photo2;
    private Button select2;
    private Button upload;
    private byte[] fileBuf1;
    private byte[] fileBuf2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_contrast);
        myApplication = (MyApplication) getApplication();
        photo1 = findViewById(R.id.photo1);
        photo2 = findViewById(R.id.photo2);
        select2 = findViewById(R.id.select2);
        upload = findViewById(R.id.upload);

    }
    //相册选择
    public void select(View view,int code){
        String[] permissions=new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        //进行sdcard的读写请求
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,permissions,code);
        }
        else{
            openGallery(code); //打开相册，进行选择
        }
    }
    @Override
    public void onRequestPermissionsResult(int code, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(code, permissions, grantResults);
        switch (code){
            case 1:
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    openGallery(1);
                }
                else{
                    Toast.makeText(this,"读相册的操作被拒绝",Toast.LENGTH_LONG).show();
                }
                break;
            case 2:
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    openGallery(2);
                }
                else{
                    Toast.makeText(this,"读相册的操作被拒绝",Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
    //打开相册,进行照片的选择
    private void openGallery(int code){
        Intent intent=new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,code);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                handleSelect(data,1);
                break;
             case 2:
                handleSelect(data,2);
                break;
        }
    }
    //选择后照片的读取工作
    private void handleSelect(Intent intent,int code){
        Uri uri = intent.getData();
        switch (code){
            case 1:
                try {
                    InputStream inputStream = getContentResolver().openInputStream(uri);
                    fileBuf1=convertToBytes(inputStream);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(fileBuf1, 0, fileBuf1.length);
                    photo1.setImageBitmap(bitmap);
                    select2.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    InputStream inputStream = getContentResolver().openInputStream(uri);
                    fileBuf2=convertToBytes(inputStream);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(fileBuf2, 0, fileBuf2.length);
                    photo2.setImageBitmap(bitmap);
                    upload.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }

    }
    private byte[] convertToBytes(InputStream inputStream) throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int len = 0;
        while ((len = inputStream.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        out.close();
        inputStream.close();
        return  out.toByteArray();
    }
    //第一次选择
    public void select1(View view) {
        select(view,1);
    }
    //第二次选择
    public void select2(View view) {
        select(view,2);
    }


    public void upload(View view) {
        final String image1 = Base64Util.encode(fileBuf1);
        final String image2 = Base64Util.encode(fileBuf2);
        Map<String, Object> map1 = new HashMap<>();
        map1.put("image", image1);
        map1.put("image_type", "BASE64");

        Map<String, Object> map2 = new HashMap<>();
        map2.put("image", image2);
        map2.put("image_type", "BASE64");

        final List<Map<String,Object>> listSum = new ArrayList<>();
        listSum.add(map1);
        listSum.add(map2);
        final String param = GsonUtils.toJson(listSum);
        new Thread() {
            @Override
            public void run() {
                // 请求url
                String url = "https://aip.baidubce.com/rest/2.0/face/v3/match";
                try {
                    // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
                    String accessToken = myApplication.ACCESSTOKEN;
                    String result = HttpUtil.post(url, accessToken, "application/json", param);
                    //json解析
                    JSONObject jsonObject = JSON.parseObject(result);
                    String resultStr = jsonObject.getString("result");
                    JSONObject resultObject = JSON.parseObject(resultStr);
                    String score = resultObject.getString("score");
                    Looper.prepare();
                    alertSet(score);
                    Looper.loop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void alertSet(String alertMes) {
        new AlertDialog.Builder(FaceContrastActivity.this)
                .setTitle("检测结果")
                .setMessage("两张脸的相似度得分为："+alertMes)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(FaceContrastActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                }).create().show();
    }

}
