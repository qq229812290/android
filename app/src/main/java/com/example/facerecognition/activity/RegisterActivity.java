package com.example.facerecognition.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.facerecognition.R;
import com.example.facerecognition.bean.User;
import com.example.facerecognition.utils.JDBCUtils;

public class RegisterActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }
    public void register(View view){
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        String userName = username.getText().toString();
        String passWord = password.getText().toString();
        final User user = new User(userName,passWord);
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean isTrue = JDBCUtils.insertUser(user);
                if (isTrue){
                    Looper.prepare();
                    Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(RegisterActivity.this, EntranceActivity.class);
                    startActivity(intent);
                    Looper.loop();
                }else {
                    Looper.prepare();
                    Toast.makeText(RegisterActivity.this,"该用户已存在",Toast.LENGTH_LONG).show();
                    username.setText("");
                    password.setText("");
                    Looper.loop();
                }
            }
        }).start();
    }
}
