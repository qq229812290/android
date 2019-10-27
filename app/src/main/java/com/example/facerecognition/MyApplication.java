package com.example.facerecognition;

import android.app.Application;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyApplication extends Application {

    public static String ACCESSTOKEN;
    public static String CLIENTID = "h2wY9SiooFDZ2b02lLVrNful";
    public static String CLIENTSECRET = "LkpOhRaS6m0zquGGC6QyLTWr92QYhbNe";
    @Override
    public void onCreate() {
        new Thread(networkTask).start();
        super.onCreate();
    }


    Runnable networkTask = new Runnable() {
        @Override
        public void run() {
            getAuthKey();
            System.out.println("------------"+ACCESSTOKEN);
        }
    };

    public void getAuthKey() {
        // 获取token地址
        String authHost = "https://aip.baidubce.com/oauth/2.0/token?";
        String getAccessTokenUrl = authHost
                // 1. grant_type为固定参数
                + "grant_type=client_credentials"
                // 2. 官网获取的 API Key
                + "&client_id=" + CLIENTID
                // 3. 官网获取的 Secret Key
                + "&client_secret=" + CLIENTSECRET;
        try {
            URL realUrl = new URL(getAccessTokenUrl);
            // 打开和URL之间的连接
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            // 定义 BufferedReader输入流来读取URL的响应
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String result = "";
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            JSONObject jsonObject = new JSONObject(result);
            ACCESSTOKEN = jsonObject.getString("access_token");
        } catch (Exception e) {
            System.err.printf("获取token失败！");
            e.printStackTrace(System.err);
        }
    }
}
