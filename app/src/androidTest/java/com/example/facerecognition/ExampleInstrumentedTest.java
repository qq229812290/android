package com.example.facerecognition;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        assertEquals("com.example.facerecognition", appContext.getPackageName());
    }

    @Test
    public void jsonFast(){
        String json = "{\"error_code\":0,\"error_msg\":\"SUCCESS\",\"log_id\":6584153505058,\"timestamp\":1572166649,\"cached\":0,\"result\":{\"face_token\":\"dbd6161fd7a2b26748a8d6a498b6d408\",\"user_list\":[{\"group_id\":\"groupDemo\",\"user_id\":\"SCARLETT\",\"user_info\":\"\",\"score\":100}]}}";
        JSONObject jsonObject = JSON.parseObject(json);
        String resultStr = jsonObject.getString("result");
        JSONObject resultObject = JSON.parseObject(resultStr);
        Object object =  resultObject.getJSONArray("user_list").get(0);
        String str = JSON.toJSONString(object);
        JSONObject jsonStr = JSON.parseObject(str);
        String userID = jsonStr.getString("user_id");
        System.out.println(userID);
    }



}
