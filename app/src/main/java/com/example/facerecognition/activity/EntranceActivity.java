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

public class EntranceActivity extends AppCompatActivity {
    private EditText username;
    private EditText password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrance);
    }
    public void register(View view){
        Intent intent = new Intent(EntranceActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
    public void login(View view){
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        String userName = username.getText().toString();
        String passWord = password.getText().toString();
        final User user = new User(userName,passWord);
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean isTure = JDBCUtils.getUserByUser(user);
                if (isTure){
                    Intent intent = new Intent(EntranceActivity.this, MainActivity.class);
                    startActivity(intent);
                }else {
                    Looper.prepare();
                    Toast.makeText(EntranceActivity.this,"用户名密码不正确",Toast.LENGTH_LONG).show();
                    username.setText("");
                    password.setText("");
                    Looper.loop();
                }
            }
        }).start();
    }
}
