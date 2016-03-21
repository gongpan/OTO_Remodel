package com.first.fubao.oto.bdmap;

import android.content.Context;
import android.text.TextUtils;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.first.fubao.oto.MainActivity;
import com.first.fubao.oto.utils.Constants;
import com.first.fubao.oto.utils.Logger;
import com.first.fubao.oto.utils.UIUtils;

/**
 * @创建者：杨长福
 * @创建时间：2016/2/1
 * @描述：定位相关，定位服务类
 */
public class LocationService {

    private LocationClient client = null;
    private LocationClientOption mOption;
    private Object objLock = new Object();

    /***
     * @param locationContext
     */
    public LocationService(Context locationContext) {
        synchronized (objLock) {
            if (client == null) {
                client = new LocationClient(locationContext);
                client.setLocOption(getDefaultLocationClientOption());
            }
        }
    }

    /***
     * @param listener
     * @return
     */
    public boolean registerListener(BDLocationListener listener) {
        boolean isSuccess = false;
        if (listener != null) {
            client.registerLocationListener(listener);
            isSuccess = true;
        }
        return isSuccess;
    }

    public void unregisterListener(BDLocationListener listener) {
        if (listener != null) {
            client.unRegisterLocationListener(listener);
        }
    }

    /***
     * @param option
     * @return isSuccessSetOption
     */
    public boolean setLocationOption(LocationClientOption option) {
        boolean isSuccess = false;
        if (option != null) {
            if (client.isStarted())
                client.stop();
            mOption = option;
            client.setLocOption(option);
        }
        return isSuccess;
    }

    public LocationClientOption getOption() {
        return mOption;
    }

    public LocationClient getClient() {
        return client;
    }

    /***
     * @return DefaultLocationClientOption
     */
    public LocationClientOption getDefaultLocationClientOption() {
        if (mOption == null) {
            mOption = new LocationClientOption();
            mOption.setLocationMode(LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
            mOption.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
            mOption.setScanSpan(5000);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
            mOption.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
            mOption.setIsNeedLocationDescribe(true);//可选，设置是否需要地址描述
            mOption.setNeedDeviceDirect(true);//可选，设置是否需要设备方向结果
            mOption.setLocationNotify(false);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
            mOption.setIgnoreKillProcess(true);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
            mOption.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
            mOption.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
            mOption.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        }
        return mOption;
    }

    public void start() {
        synchronized (objLock) {
            if (client != null && !client.isStarted()) {
                mBDListener = new MainBDLocationListener();
                registerListener(mBDListener);
                client.start();
                Logger.e(Constants.DUBUG_TAG, "开始定位start");
            }
        }
    }

    public void stop() {
        synchronized (objLock) {
            if (client != null && client.isStarted()) {
                client.stop();
                unregisterListener(mBDListener); //注销掉监听
                mBDListener = null;
                Logger.e(Constants.DUBUG_TAG, "取消定位stop");
            }
        }
    }

    // ########################## 百度地图定位 start #########################
    private MainBDLocationListener mBDListener;
    private LocationEntity mLocationEntity;
    private int count = 0;

    public class MainBDLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            count++;
            Logger.e(Constants.DUBUG_TAG, "定位次数：" + count);
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                if (mLocationEntity == null) {
                    mLocationEntity = new LocationEntity();
                    mLocationEntity.setCity(location.getCity());
                    mLocationEntity.setCityCode(location.getCityCode());
                    mLocationEntity.setCountry(location.getCountry());
                    mLocationEntity.setCountryCode(location.getCountryCode());
                    mLocationEntity.setDistrict(location.getDistrict());
                    mLocationEntity.setLatitude(location.getLatitude());
                    mLocationEntity.setLongitude(location.getLongitude());
                    mLocationEntity.setProvince(location.getProvince());
                    mLocationEntity.setStreet(location.getStreet());
                    mLocationEntity.setStreetNumber(location.getStreetNumber());
                    mLocationEntity.setTime(location.getTime());
                }
                String msg = mLocationEntity.toString();
                Logger.e(Constants.DUBUG_TAG, msg);
                if (!TextUtils.isEmpty(mLocationEntity.getCity())) {
                    Logger.e(Constants.DUBUG_TAG, "定位成功");
                    // 定位成功后的回调
                    if (mOnLocalFinishListener != null) {
                        mOnLocalFinishListener.onLocalFinish(mLocationEntity);
                    }
                } else {
                    mLocationEntity = null;
                    Logger.e(Constants.DUBUG_TAG, "定位失败");
                    // 定位失败后的回调
                    if (mOnLocalFinishListener != null) {
                        mOnLocalFinishListener.onLocalFail();
                    }
                }
            } else {
                Logger.e(Constants.DUBUG_TAG, "定位失败");
                // 定位失败后的回调
                if (mOnLocalFinishListener != null) {
                    mOnLocalFinishListener.onLocalFail();
                }
            }
            stop();
        }
    }

    // -------------- 定位监听的接口回调 -------------
    private OnLocalFinishListener mOnLocalFinishListener;

    public void setOnLocalFinishListener(OnLocalFinishListener listener) {
        if (listener == null) {
            throw new RuntimeException("OnLocalFinishListener can not null");
        }
        this.mOnLocalFinishListener = listener;
    }

    public interface OnLocalFinishListener {
        void onLocalFinish(LocationEntity locationEntity);

        void onLocalFail();
    }
    // -------------- 定位监听的接口回调 -------------

    // ########################## 百度地图定位 end ###########################
}
