package com.example.facerecognition.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;

import com.example.facerecognition.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void turnToFaceCollection(View view) {
        Intent intent = new Intent(MainActivity.this, FaceCollectionActivity.class);
        startActivity(intent);
    }

    public void turnToFaceRecognition(View view) {
        Intent intent = new Intent(MainActivity.this, FaceRecognitionActivity.class);
        startActivity(intent);
    }

    public void turnToFaceContrast(View view) {
        Intent intent = new Intent(MainActivity.this, FaceContrastActivity.class);
        startActivity(intent);
    }
}
