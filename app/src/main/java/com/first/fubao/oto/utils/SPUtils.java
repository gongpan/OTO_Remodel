package com.first.fubao.oto.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * @创建者：杨长福
 * @创建时间：2016/2/14
 * @描述：SP的工具类
 */
public class SPUtils {

    private final static String SP_NAME = "oto-sp";// sp文件名
    private static SharedPreferences mPreferences;

    private static SharedPreferences getSP(Context context) {
        if (mPreferences == null) {
            mPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        return mPreferences;
    }

    /**
     * 通过SP获取boolean类型的数据，默认为false
     *
     * @param context : 上下文
     * @param key     : 存储的key
     * @return
     */
    public static boolean getBoolean(Context context, String key) {
        SharedPreferences sp = getSP(context);
        return sp.getBoolean(key, false);
    }

    /**
     * 通过SP获取boolean类型的数据，默认为defValue
     *
     * @param context  ：上下文
     * @param key      ：存储的key
     * @param defValue ：默认值
     * @return
     */
    public static boolean getBoolean(Context context, String key, boolean defValue) {
        SharedPreferences sp = getSP(context);
        return sp.getBoolean(key, defValue);
    }

    /**
     * 设置boolean的缓存数据
     *
     * @param context ：上下文
     * @param key     ：缓存对应的key
     * @param value   ：缓存对应的值
     */
    public static void setBoolean(Context context, String key, boolean value) {
        SharedPreferences sp = getSP(context);
        Editor edit = sp.edit();
        edit.putBoolean(key, value);
        edit.commit();
    }

    /**
     * 通过SP获取String类型的数据，默认为null
     *
     * @param context : 上下文
     * @param key     : 存储的key
     * @return
     */
    public static String getString(Context context, String key) {
        SharedPreferences sp = getSP(context);
        return sp.getString(key, null);
    }

    /**
     * 通过SP获取String类型的数据，默认为defValue
     *
     * @param context  ：上下文
     * @param key      ：存储的key
     * @param defValue ：默认值
     * @return
     */
    public static String getString(Context context, String key, String defValue) {
        SharedPreferences sp = getSP(context);
        return sp.getString(key, defValue);
    }

    /**
     * 设置String的缓存数据
     *
     * @param context ：上下文
     * @param key     ：缓存对应的key
     * @param value   ：缓存对应的值
     */
    public static void setString(Context context, String key, String value) {
        SharedPreferences sp = getSP(context);
        Editor edit = sp.edit();
        edit.putString(key, value);
        edit.commit();
    }

    /**
     * 通过SP获取long类型的数据，默认为-1
     *
     * @param context : 上下文
     * @param key     : 存储的key
     * @return
     */
    public static Long getLong(Context context, String key) {
        SharedPreferences sp = getSP(context);
        return sp.getLong(key, -1);
    }

    /**
     * 通过SP获取long类型的数据，默认为defValue
     *
     * @param context  ：上下文
     * @param key      ：存储的key
     * @param defValue ：默认值
     * @return
     */
    public static long getLong(Context context, String key, long defValue) {
        SharedPreferences sp = getSP(context);
        return sp.getLong(key, defValue);
    }

    /**
     * 设置String的缓存数据
     *
     * @param context ：上下文
     * @param key     ：缓存对应的key
     * @param value   ：缓存对应的值
     */
    public static void setLong(Context context, String key, long value) {
        SharedPreferences sp = getSP(context);
        Editor edit = sp.edit();
        edit.putLong(key, value);
        edit.commit();
    }
}
