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
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.baidu.aip.util.Base64Util;
import com.example.facerecognition.MyApplication;
import com.example.facerecognition.R;
import com.example.facerecognition.bean.User;
import com.example.facerecognition.utils.GsonUtils;
import com.example.facerecognition.utils.HttpUtil;
import com.example.facerecognition.utils.JDBCUtils;


import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FaceCollectionActivity extends AppCompatActivity {

    private MyApplication myApplication;
    private ImageView photo;
    private byte[] fileBuf;
    private Button upload;
    private RelativeLayout after;
    private String username;
    private Spinner spinner;
    private List<String> data;
    private ArrayAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_collection);
        myApplication = (MyApplication) getApplication();
        photo = findViewById(R.id.photo);
        upload = findViewById(R.id.upload);
        after = findViewById(R.id.after);
    }

    //相册选择
    public void select(View view){
        String[] permissions=new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        //进行sdcard的读写请求
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,permissions,1);
        }
        else{
            openGallery(); //打开相册，进行选择
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    openGallery();
                }
                else{
                    Toast.makeText(this,"读相册的操作被拒绝",Toast.LENGTH_LONG).show();
                }
        }
    }
    //打开相册,进行照片的选择
    private void openGallery(){
        Intent intent=new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                handleSelect(data);
        }
    }
    //选择后照片的读取工作
    private void handleSelect(Intent intent){
        Cursor cursor = null;
        Uri uri = intent.getData();
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            fileBuf=convertToBytes(inputStream);
            Bitmap bitmap = BitmapFactory.decodeByteArray(fileBuf, 0, fileBuf.length);
            photo.setImageBitmap(bitmap);
            showList();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //设置下拉列表
    public void showList(){
        Thread checkData = new Thread(new Runnable() {
            @Override
            public void run() {
                spinner=findViewById(R.id.spinner);
                data=new ArrayList<String>();
                ArrayList<User> userList = JDBCUtils.getAllUsers();
                for (User user : userList) {
                    data.add(user.getUsername());
                }
            }
        });
        checkData.start();
        while (true){
            if(!checkData.isAlive()){
                break;
            }
        }
        //2、未下来列表定义一个数组适配器
        adapter=new ArrayAdapter(FaceCollectionActivity.this,android.R.layout.simple_list_item_1,data);
        //3、为适配器设置下拉菜单的样式
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //4、将适配器配置到下拉列表上
        spinner.setAdapter(adapter);
        //5、给下拉菜单设置监听事件
        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                username = data.get(position);
                upload.setEnabled(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        after.setVisibility(View.VISIBLE);
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

    //人脸库上传的处理
    public void upload(View view) {
        final String userId = username;
        final String image = Base64Util.encode(fileBuf);
        final String groupId = "groupDemo";
        new Thread() {
            @Override
            public void run() {
                // 请求url
                String url = "https://aip.baidubce.com/rest/2.0/face/v3/faceset/user/add";
                try {
                    Map<String, Object> map = new HashMap<>();
                    map.put("image", image);
                    map.put("group_id", groupId);
                    map.put("user_id", userId);
                    map.put("image_type", "BASE64");
                    String param = GsonUtils.toJson(map);
                    // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
                    String accessToken = myApplication.ACCESSTOKEN;
                    HttpUtil.post(url, accessToken, "application/json", param);
                    Looper.prepare();
                    alertSet();
                    Looper.loop();
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        }.start();
    }


    public void alertSet(){
        new AlertDialog.Builder(FaceCollectionActivity.this)
                .setTitle("结果")
                .setMessage("上传成功")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(FaceCollectionActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                }).create().show();
    }
}
