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
import com.alibaba.fastjson.JSONObject;
import com.baidu.aip.util.Base64Util;
import com.example.facerecognition.MyApplication;
import com.example.facerecognition.R;
import com.example.facerecognition.utils.GsonUtils;
import com.example.facerecognition.utils.HttpUtil;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class FaceRecognitionActivity extends AppCompatActivity {
    private MyApplication myApplication;
    private ImageView photo;
    private byte[] fileBuf;
    private Button upload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_recognition);
        upload = findViewById(R.id.upload);
        myApplication = (MyApplication) getApplication();
        photo = findViewById(R.id.photo);

    }

    //相册选择
    public void select(View view) {
        String[] permissions = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        //进行sdcard的读写请求
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions, 1);
        } else {
            openGallery(); //打开相册，进行选择
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGallery();
                } else {
                    Toast.makeText(this, "读相册的操作被拒绝", Toast.LENGTH_LONG).show();
                }
        }
    }

    //打开相册,进行照片的选择
    private void openGallery() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                handleSelect(data);
        }
    }

    //选择后照片的读取工作
    private void handleSelect(Intent intent) {
        Cursor cursor = null;
        Uri uri = intent.getData();
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            fileBuf = convertToBytes(inputStream);
            Bitmap bitmap = BitmapFactory.decodeByteArray(fileBuf, 0, fileBuf.length);
            photo.setImageBitmap(bitmap);
            upload.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private byte[] convertToBytes(InputStream inputStream) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int len = 0;
        while ((len = inputStream.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        out.close();
        inputStream.close();
        return out.toByteArray();
    }

    //人脸库上传的处理
    public void upload(View view) {
        final String image = Base64Util.encode(fileBuf);
        final String groupId = "groupDemo";
        new Thread() {
            @Override
            public void run() {
                // 请求url
                String url = "https://aip.baidubce.com/rest/2.0/face/v3/search";
                try {
                    Map<String, Object> map = new HashMap<>();
                    map.put("image", image);
                    map.put("group_id_list", groupId);
                    map.put("image_type", "BASE64");
                    String param = GsonUtils.toJson(map);
                    // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
                    String accessToken = myApplication.ACCESSTOKEN;
                    String result = HttpUtil.post(url, accessToken, "application/json", param);
                    //json解析
                    JSONObject jsonObject = JSON.parseObject(result);
                    String resultStr = jsonObject.getString("result");
                    JSONObject resultObject = JSON.parseObject(resultStr);
                    Object object = resultObject.getJSONArray("user_list").get(0);
                    String str = JSON.toJSONString(object);
                    JSONObject jsonStr = JSON.parseObject(str);
                    String userName = jsonStr.getString("user_id");
                    String score = jsonStr.getString("score");
                    Looper.prepare();
                    alertSet(userName,score);
                    Looper.loop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void alertSet(String userName,String score){
        new AlertDialog.Builder(FaceRecognitionActivity.this)
                .setTitle("检测结果")
                .setMessage("姓名为：" + userName + "的同学，相似度为:" + score)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(FaceRecognitionActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                }).create().show();
    }
}
