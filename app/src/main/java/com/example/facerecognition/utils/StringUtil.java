package com.example.facerecognition.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by smile on 03/03/2018.
 */

public class StringUtil {

    private static final String PHONE_REGEX = "^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}$";

    public static String number2Scale(double value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.toString();
    }

    public static boolean isPhone(String value) {
        return value.matches(PHONE_REGEX);
    }

    public static boolean isPassword(String value) {
        return value.length()>=6;
    }

    /**
     * 统计数据格式化，100，10万
     * @param count
     * @return
     */
    public static String formatCount(long count) {
        if (count >= 10000) {
            count=count/10000;
            return String.format("%d万",count);
        }
        return String.valueOf(count);
    }

    /**
     * <p> Checks if the String contains only unicode digits. A decimal point is not a unicode digit
     * and returns false. </p>
     *
     * <p> <code>null</code> will return <code>false</code>. An empty String ("") will return
     * <code>true</code>. </p>
     *
     * <pre>
     * StringUtils.isNumeric(null)   = false
     * StringUtils.isNumeric("")     = true
     * StringUtils.isNumeric("  ")   = false
     * StringUtils.isNumeric("123")  = true
     * StringUtils.isNumeric("12 3") = false
     * StringUtils.isNumeric("ab2c") = false
     * StringUtils.isNumeric("12-3") = false
     * StringUtils.isNumeric("12.3") = false
     * </pre>
     *
     * @param str the String to check, may be null
     * @return <code>true</code> if only contains digits, and is non-null
     */
    public static boolean isNumber(String str) {
        if (str == null) {
            return false;
        }
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            if (Character.isDigit(str.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }
}
