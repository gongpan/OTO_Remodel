package com.first.fubao.oto.controller;

import android.app.Activity;
import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.first.fubao.oto.MainActivity;
import com.first.fubao.oto.R;
import com.first.fubao.oto.adapter.BaseHolder;
import com.first.fubao.oto.adapter.BusinessListItemHolder;
import com.first.fubao.oto.adapter.SuperBaseAdapter;
import com.first.fubao.oto.bdmap.LocationEntity;
import com.first.fubao.oto.bdmap.LocationService;
import com.first.fubao.oto.entity.BusinessEntity;
import com.first.fubao.oto.fragment.LoadingUI;
import com.first.fubao.oto.network.NetWorkUtils;
import com.first.fubao.oto.network.OnLoadMoreListener;
import com.first.fubao.oto.network.OnNetWorkListener;
import com.first.fubao.oto.utils.Constants;
import com.first.fubao.oto.utils.Logger;
import com.first.fubao.oto.utils.SPUtils;
import com.first.fubao.oto.utils.UIUtils;
import com.google.gson.Gson;
import com.squareup.okhttp.Request;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @创建者：杨长福
 * @创建时间：2016/3/7
 * @描述：商家TAB对应的Controller控制器
 */
public class BusinessController implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private Context mContext;
    private View mRootView;
    private FrameLayout mContainerView;
    private LoadingUI mLoadingUI;

    private ViewGroup mSiftTab1;
    private ViewGroup mSiftTab2;
    private ViewGroup mSiftTab3;
    private TextView mSiftTab1Tv;
    private TextView mSiftTab2Tv;
    private TextView mSiftTab3Tv;
    private ImageView mSiftTab1Img;
    private ImageView mSiftTab2Img;
    private ImageView mSiftTab3Img;

    private ListView mListView;
    private SwipeRefreshLayout mRefreshLayout;

    private int mPage = 1;
    private boolean isRefresh = false;
    private boolean isFirstLocal = true;
    private String mLng = "";
    private String mLat = "";

    List<BusinessEntity.StoreData> mStoreDatas = null;

    private Runnable mRefreshTask = null;

    public BusinessController(Context context) {
        this.mContext = context;
        this.mRootView = initView();
    }

    private View initView() {

        ViewGroup viewGroup = (ViewGroup) View.inflate(UIUtils.getContext(), R.layout.controller_business, null);

        ViewGroup siftView = (ViewGroup) viewGroup.getChildAt(0);
        mSiftTab1 = (ViewGroup) siftView.findViewById(R.id.business_sift_tab1);
        mSiftTab2 = (ViewGroup) siftView.findViewById(R.id.business_sift_tab2);
        mSiftTab3 = (ViewGroup) siftView.findViewById(R.id.business_sift_tab3);
        mSiftTab1Tv = (TextView) siftView.findViewById(R.id.business_sift_tab1_tv);
        mSiftTab2Tv = (TextView) siftView.findViewById(R.id.business_sift_tab2_tv);
        mSiftTab3Tv = (TextView) siftView.findViewById(R.id.business_sift_tab3_tv);
        mSiftTab1Img = (ImageView) siftView.findViewById(R.id.business_sift_tab1_img);
        mSiftTab2Img = (ImageView) siftView.findViewById(R.id.business_sift_tab2_img);
        mSiftTab3Img = (ImageView) siftView.findViewById(R.id.business_sift_tab3_img);

        mContainerView = (FrameLayout) viewGroup.findViewById(R.id.controller_business_content_container);
        mContainerView.addView(addContentView());

        initSiftView();
        return viewGroup;
    }

    private void initSiftView() {
        // 顶部筛选栏的点击事件
        mSiftTab1.setOnClickListener(this);
        mSiftTab2.setOnClickListener(this);
        mSiftTab3.setOnClickListener(this);
    }

    private View addContentView() {
        mLoadingUI = new LoadingUI(UIUtils.getContext()) {
            @Override
            public View onLoadSuccessView() {
                return loadSuccessView();
            }

            @Override
            protected void clickAfreshLoad() {
                refreshLoadData();
            }
        };
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mLoadingUI.setLayoutParams(params);
        return mLoadingUI;
    }

    /**
     * 重新加载的回调
     */
    protected void refreshLoadData() {
        loadDataFinish(LoadingUI.LoadedResult.LOADING);
        isFirstLocal = true;
        UIUtils.getLocationService().start();
    }

    /**
     * 加载数据结束时，必须调用该方法
     *
     * @param result
     */
    protected final void loadDataFinish(LoadingUI.LoadedResult result) {
        if (mLoadingUI != null) {
            mLoadingUI.onLoadDataFinish(result);
        }
    }

    /**
     * 初始化成功的View的回调
     *
     * @return
     */
    protected View loadSuccessView() {
        ViewGroup contentView = (ViewGroup) View.inflate(UIUtils.getContext(), R.layout.fragment_business_content, null);
        mListView = (ListView) contentView.findViewById(R.id.business_listView);
        mRefreshLayout = (SwipeRefreshLayout) contentView.findViewById(R.id.business_swipe_container);
        initSuccessViewData();
        initSuccessViewEvent();
        return contentView;
    }

    private void initSuccessViewData() {
        mListView.setAdapter(new BusinessListAdapter(mStoreDatas));
    }

    private void initSuccessViewEvent() {
        mRefreshLayout.setColorSchemeColors(0xffff0000, 0xff00ff00, 0xff0000ff);
        mRefreshLayout.setOnRefreshListener(this);
        Logger.e(Constants.DUBUG_TAG, "--------isRefresh: " + mRefreshLayout.isRefreshing());

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO 处理ListView的item的点击事件
            }
        });
    }

    /**
     * 刷新完成时调用 -- 用于下拉刷新
     */
    private void refreshFinish(boolean isRefreshFinish) {
        if (isRefresh) {
            isRefresh = false;
            mRefreshLayout.setRefreshing(false);
            if (isRefreshFinish) {
                mPage = 1;
                initSuccessViewData();
                Toast.makeText(UIUtils.getContext(), "刷新完成", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(UIUtils.getContext(), "刷新失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 开始加载数据的回调
     *
     * @return
     */
    public void startLoadData() {
        String lastLng = SPUtils.getString(UIUtils.getContext(), Constants.SP_KEY_LAST_LNG);
        String lastLat = SPUtils.getString(UIUtils.getContext(), Constants.SP_KEY_LAST_LAT);
        if (!TextUtils.isEmpty(lastLng) && !TextUtils.isEmpty(lastLat)) {
            mLng = lastLng;
            mLat = lastLat;
            requestDataFromNet(null, lastLng, lastLat, 1);
        } else {
            UIUtils.getLocationService().start();
        }

        UIUtils.getLocationService().setOnLocalFinishListener(new LocationService.OnLocalFinishListener() {

            @Override
            public void onLocalFinish(LocationEntity locationEntity) {
                if (isFirstLocal) {
                    String currentLng = locationEntity.getLongitude() + "";
                    String currentLat = locationEntity.getLatitude() + "";
                    if (!TextUtils.isEmpty(currentLng) && !TextUtils.isEmpty(currentLat)) {
                        mLng = currentLng;
                        mLat = currentLat;
                        requestDataFromNet(null, currentLng, currentLat, 1);
                    } else {
                        loadDataOnLocationFail();
                    }
                    isFirstLocal = false;
                }
            }

            @Override
            public void onLocalFail() {
                loadDataOnLocationFail();
            }
        });
    }

    /**
     * 定位失败时调用
     */
    private void loadDataOnLocationFail() {
        String lastLng = SPUtils.getString(UIUtils.getContext(), Constants.SP_KEY_LAST_LNG);
        String lastLat = SPUtils.getString(UIUtils.getContext(), Constants.SP_KEY_LAST_LAT);
        if (!TextUtils.isEmpty(lastLng) && !TextUtils.isEmpty(lastLat)) {
            mLng = lastLng;
            mLat = lastLat;
            requestDataFromNet(null, lastLng, lastLat, 1);
        } else {
            Toast.makeText(UIUtils.getContext(), "定位失败，无法加载数据", Toast.LENGTH_SHORT).show();
            if (!isRefresh) loadDataFinish(LoadingUI.LoadedResult.ERROR);
            refreshFinish(false);
        }
    }

    /**
     * 商家页面，从网络请求数据
     *
     * @param listener
     * @param lng
     * @param lat
     */
    private void requestDataFromNet(final OnLoadMoreListener listener, String lng, String lat, int page) {

        String url = Constants.BASE_SERVER + "plugins/appapi/index.php";
        Map<String, String> params = new HashMap<String, String>();
        params.put("app", "store");//商家，定值
        params.put("act", "get_store_list");

        params.put("p", page + "");//list的页数
        params.put("psize", Constants.PAGER_SIZE + "");//每页数量
        params.put("lng", lng);//经度
        params.put("lat", lat);//纬度

        NetWorkUtils.requestGet(url, params, new OnNetWorkListener<String>() {
            @Override
            public void onSuccess(String response) {
                Gson gson = new Gson();
                BusinessEntity businessEntity = gson.fromJson(response, BusinessEntity.class);
                if (businessEntity.status == 1) {
                    if (listener != null) {
                        listener.onSucess(businessEntity.data);
                    } else {
                        mStoreDatas = businessEntity.data;
                        if (!isRefresh) loadDataFinish(LoadingUI.LoadedResult.SUCCESS);
                        refreshFinish(true);
                    }
                } else {
                    if (listener != null) {
                        listener.onFail();
                    } else {
                        Toast.makeText(UIUtils.getContext(), "请求数据失败", Toast.LENGTH_SHORT).show();
                        if (!isRefresh) loadDataFinish(LoadingUI.LoadedResult.EMPTY);
                        refreshFinish(false);
                    }
                }
            }

            @Override
            public void onFail(Request request, Exception e) {
                if (listener != null) {
                    listener.onFail();
                } else {
                    Logger.e(Constants.DUBUG_TAG, "请求数据失败：" + e.toString());
                    if (!isRefresh) loadDataFinish(LoadingUI.LoadedResult.ERROR);
                    refreshFinish(false);
                }
            }
        });
    }


    /**
     * 下拉刷新的回调
     */
    @Override
    public void onRefresh() {
        isFirstLocal = true;
        isRefresh = true;
        UIUtils.getLocationService().start();

        if (mRefreshTask != null) {
            UIUtils.getMainHandler().removeCallbacks(mRefreshTask);
            mRefreshTask = null;
        }
        mRefreshTask = new Runnable() {
            @Override
            public void run() {
                if (mRefreshLayout.isRefreshing()) {
                    refreshFinish(false);
                }
            }
        };
        UIUtils.getMainHandler().postDelayed(mRefreshTask, Constants.REFRESH_TIME);
    }


    public void setData() {

    }

    public View getRootView() {
        return mRootView;
    }

    @Override
    public void onClick(View v) {
        int clickSift = 0;
        switch (v.getId()) {
            case R.id.business_sift_tab1:
                clickSift = 1;
                break;
            case R.id.business_sift_tab2:
                clickSift = 2;
                break;
            case R.id.business_sift_tab3:
                clickSift = 3;
                break;
        }
        // 切换筛选Tab的状态
        mSiftTab1Tv.setEnabled(!(clickSift == 1) || !mSiftTab1Tv.isEnabled());
        mSiftTab1Img.setEnabled(!(clickSift == 1) || !mSiftTab1Img.isEnabled());
        mSiftTab2Tv.setEnabled(!(clickSift == 2) || !mSiftTab2Tv.isEnabled());
        mSiftTab2Img.setEnabled(!(clickSift == 2) || !mSiftTab2Img.isEnabled());
        mSiftTab3Tv.setEnabled(!(clickSift == 3) || !mSiftTab3Tv.isEnabled());
        mSiftTab3Img.setEnabled(!(clickSift == 3) || !mSiftTab3Img.isEnabled());
    }


    /**
     * @创建者：杨长福
     * @创建时间：2016/2/16
     * @描述：ListView的Adapter
     */
    class BusinessListAdapter extends SuperBaseAdapter<BusinessEntity.StoreData> {

        public BusinessListAdapter(List<BusinessEntity.StoreData> datas) {
            super(datas);
        }

        @Override
        protected BaseHolder<BusinessEntity.StoreData> getItemHolder() {
            return new BusinessListItemHolder();
        }

        @Override
        protected void onLoadMore() throws Exception {

            requestDataFromNet(new OnLoadMoreListener<BusinessEntity.StoreData>() {
                @Override
                public void onSucess(List<BusinessEntity.StoreData> list) {
                    onLoadMoreFinish(list);
                }

                @Override
                public void onFail() {
                    onLoadMoreError();
                }
            }, mLng, mLat, ++mPage);
        }

        @Override
        protected boolean hasLoadMore() {
            return true;
        }

        @Override
        protected int getPageSize() {
            return Constants.PAGER_SIZE;
        }
    }
}
