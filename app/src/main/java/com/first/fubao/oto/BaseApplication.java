package com.first.fubao.oto;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.baidu.mapapi.SDKInitializer;
import com.first.fubao.oto.bdmap.LocationService;
import com.first.fubao.oto.utils.FileUtils;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.squareup.okhttp.OkHttpClient;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @类名： BaseApplication
 * @创建者： 杨长福
 * @创建时间：2016-02-01
 * @描述：应用程序的入口
 */
public class BaseApplication extends Application {
	
	public static Thread mMainThread; // 主线程
	public static int mMainThreadId; // 主线程ID
	public static Looper mLooper; // looper
	public static Handler mMainHandler; // 主线程的 handler
	public static Context mContext; // 应用程序的上下文

	public static LocationService mLocationService;// 定位的服务类
    public static DisplayImageOptions mDefaultOptions;

    /**
	 * 在应用程序加载时，获取全局的信息
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		// 上下文
		mContext = this;

		// 主线程
		mMainThread = Thread.currentThread();

		// 主线程的id
		// long id = mMainThread.getId();
		mMainThreadId = android.os.Process.myTid();

		// 获取主线程的 looper
		mLooper = getMainLooper();

		// 在应用程序加载时，创建出一个全局的 handler
		mMainHandler = new Handler();

		//初始化百度地图--在使用SDK各组件之前初始化context信息
		SDKInitializer.initialize(getApplicationContext());
		mLocationService = new LocationService(getApplicationContext());

		initOkHttp();
        initImageLoader();
	}

	private void initOkHttp() {
		OkHttpClient client = OkHttpUtils.getInstance().getOkHttpClient();
		client.setConnectTimeout(10000, TimeUnit.MILLISECONDS);
		client.setReadTimeout(10000, TimeUnit.MILLISECONDS);
		client.setWriteTimeout(10000, TimeUnit.MILLISECONDS);
	}

    private void initImageLoader() {
        //网络图片例子,结合常用的图片缓存库UIL,你可以根据自己需求自己换其他网络图片库
        mDefaultOptions = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.ic_default_adimage)
                .showImageOnLoading(R.drawable.ic_default_adimage)
                .showImageOnFail(R.drawable.ic_default_adimage)
                .cacheInMemory(true).cacheOnDisk(true).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getApplicationContext()).defaultDisplayImageOptions(mDefaultOptions)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO).build();
        ImageLoader.getInstance().init(config);
    }
}
