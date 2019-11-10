package com.example.facerecognition.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by smile on 05/03/2018.
 */

public class ToastUtil {
    /**
     * 唯一的toast
     */
    private static Toast mToast = null;

    public static void showSortToast(Context context, String message) {
        showToast(context, message, Toast.LENGTH_SHORT);
    }

    public static void showSortToast(Context context, int resId) {
        showToast(context, context.getResources().getString(resId), Toast.LENGTH_SHORT);
    }

    public static void showToast(Context context, String message, int time) {
        if (mToast != null) {
        } else {
            mToast = Toast.makeText(context.getApplicationContext(), message, time);
        }
        mToast.setText(message);
        mToast.show();
    }


}
