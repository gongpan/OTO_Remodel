package com.first.fubao.oto.utils;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Process;

import com.first.fubao.oto.BaseApplication;
import com.first.fubao.oto.bdmap.LocationService;
import com.nostra13.universalimageloader.core.DisplayImageOptions;


/**
 * @类名： UIUtils
 * @创建者： 杨长福
 * @创建时间：2015-02-01
 * @描述：UI工具类
 */
public class UIUtils {
    /**
     * 获取获取整个应用的上下文
     *
     * @return
     */
    public static Context getContext() {
        return BaseApplication.mContext;
    }

    /**
     * 获取主线程的 handler
     *
     * @return
     */
    public static Handler getMainHandler() {
        return BaseApplication.mMainHandler;
    }

    /**
     * 获取主线程的线程ID
     *
     * @return
     */
    public static int getMainThreadId() {
        return BaseApplication.mMainThreadId;
    }

    /**
     * 使用Handler执行任务
     *
     * @param task
     */
    public static void post(Runnable task) {
        if (Process.myTid() == getMainThreadId()) {
            // 主线程中执行的
            task.run();
        } else {
            // 在主线程中运行
            getMainHandler().post(task);
        }
    }

    /**
     * 获取定位的服务类
     *
     * @return LocationService
     */
    public static LocationService getLocationService() {
        return BaseApplication.mLocationService;
    }

    public static String getPackageName() {
        return getContext().getPackageName();
    }

    public static Resources getResources() {
        return getContext().getResources();
    }

    public static String getString(int resId) {
        return getResources().getString(resId);
    }

    public static int getColor(int resId) {
        return getResources().getColor(resId);
    }

    public static DisplayImageOptions getImageLoadOptions() {
        return BaseApplication.mDefaultOptions;
    }
}
