package com.first.fubao.oto.fragment;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.InputType;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.first.fubao.oto.MainActivity;
import com.first.fubao.oto.R;
import com.first.fubao.oto.activity.BusinessActivity;
import com.first.fubao.oto.adapter.BaseHolder;
import com.first.fubao.oto.adapter.HomeAreaGridAdapter;
import com.first.fubao.oto.adapter.HomeGridViewAdapter;
import com.first.fubao.oto.adapter.HomeListItemHolder;
import com.first.fubao.oto.adapter.NetworkImageHolderView;
import com.first.fubao.oto.adapter.SuperBaseAdapter;
import com.first.fubao.oto.bdmap.LocationEntity;
import com.first.fubao.oto.bdmap.LocationService;
import com.first.fubao.oto.entity.CityAreaEntity;
import com.first.fubao.oto.entity.CityIdEntiry;
import com.first.fubao.oto.entity.HomeEntity;
import com.first.fubao.oto.network.NetWorkUtils;
import com.first.fubao.oto.network.OnLoadMoreListener;
import com.first.fubao.oto.network.OnNetWorkListener;
import com.first.fubao.oto.utils.Constants;
import com.first.fubao.oto.utils.DisplayUtils;
import com.first.fubao.oto.utils.Logger;
import com.first.fubao.oto.utils.SPUtils;
import com.first.fubao.oto.utils.UIUtils;
import com.first.fubao.oto.widget.GridViewForScrollView;
import com.first.fubao.oto.widget.ListViewForScrollView;
import com.google.gson.Gson;
import com.squareup.okhttp.Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @创建者：杨长福
 * @创建时间：2016/2/14
 * @描述：首页TAB对应的Fragment
 */
public class HomePagerFragment extends BaseFragment implements OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    private View mPlaceContainer;
    private TextView mTvCity;
    private ImageView mIgvMorePlace;
    private EditText mEtSearch;
    private ImageView mIgvSys;

    private FrameLayout mContainerView;
    private ConvenientBanner mBanner;
    private TextView mXFCvalue;
    private GridViewForScrollView mGridView;
    private ListViewForScrollView mListView;
    private SwipeRefreshLayout mRefreshLayout;
    private LinearLayout mPopBgShadow;

    // 主页需要加载的数据
    private HomeEntity.HomeData mHomeData;

    // 当前城市区域列表数据
    private List<CityAreaEntity.CityAreaData> mCityAreaDatas;

    private PopupWindow mPopupWindow;

    private boolean isFirstLocal = true;
    private boolean isRefresh = false;

    private int mPage = 1;

    private String mCity = "";

    // 下拉城市列表控件
    private RelativeLayout mPopLayout;
    private GridView mAreaGrid;
    private RelativeLayout mChangeCityRl;
    private TextView mCurrentCity;
    private ViewGroup mTopView;
    private HomeAreaGridAdapter mHomeAreaGridAdapter;

    /**
     * 重新加载数据
     */
    @Override
    protected void refreshLoadData() {
        loadDataFinish(LoadingUI.LoadedResult.LOADING);
        isFirstLocal = true;
        UIUtils.getLocationService().start();
    }

    @Override
    protected View initView() {
        ViewGroup viewGroup = (ViewGroup) View.inflate(UIUtils.getContext(), R.layout.fragment_home_pager_ui, null);
        mPopBgShadow = (LinearLayout) viewGroup.findViewById(R.id.home_pop_bg_shadow);
        mContainerView = (FrameLayout) viewGroup.findViewById(R.id.home_content_container);
        mContainerView.addView(addContentView());

        mTopView = (ViewGroup) viewGroup.getChildAt(0);
        mPlaceContainer = mTopView.findViewById(R.id.home_place_container);
        mTvCity = (TextView) mTopView.findViewById(R.id.home_tv_city);
        mIgvMorePlace = (ImageView) mTopView.findViewById(R.id.home_img_more_place);
        mEtSearch = (EditText) mTopView.findViewById(R.id.home_et_search);
        mIgvSys = (ImageView) mTopView.findViewById(R.id.home_img_sys);
        mEtSearch.setInputType(InputType.TYPE_NULL);

        initTopViewEvent();

        return viewGroup;
    }

    private void initTopViewEvent() {
        // 选择城市区域
        mPlaceContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (TextUtils.isEmpty(mCity)) {
//                    return;
//                }
                showAreas();
            }
        });

        // 顶部搜索点击事件
        mEtSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO 处理顶部搜索点击事件
            }
        });

        // 顶部二维码扫描按钮点击事件
        mIgvSys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO 处理顶部二维码扫描按钮点击事件
            }
        });
    }

    /**
     * 获取当前城市的地区
     */
    private void loadCityArea(final boolean isFirstLoad) {

        if (TextUtils.isEmpty(mCity)) {
            return;
        }

        String url = Constants.BASE_SERVER + "plugins/appapi/index.php";
        Map<String, String> params = new HashMap<String, String>();
        params.put("app", "home");
        params.put("act", "get_area");
        params.put("city", mCity);

        NetWorkUtils.requestGet(url, params, new OnNetWorkListener<String>() {
            @Override
            public void onSuccess(String response) {
                Gson gson = new Gson();
                CityAreaEntity cityAreaEntity = gson.fromJson(response, CityAreaEntity.class);
                if (cityAreaEntity.status == 1) {
                    mCityAreaDatas = cityAreaEntity.data;
                    if (!isFirstLoad) {
                        if (mAreaGrid != null) {
                            mHomeAreaGridAdapter = new HomeAreaGridAdapter(mCityAreaDatas);
                            mAreaGrid.setAdapter(mHomeAreaGridAdapter);
                        }
                        if (mCurrentCity != null) {
                            mCurrentCity.setText(mCity);
                        }
                    }
                }
            }

            @Override
            public void onFail(Request request, Exception e) {
            }
        });
    }

    /**
     * 弹出城市区域列表
     */
    private void showAreas() {
        if (mCityAreaDatas == null) {
            loadCityArea(false);
        }
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
            onAreaPopupDismiss();
        } else {
            if (mPopupWindow == null) {
                mPopLayout = (RelativeLayout) View.inflate(UIUtils.getContext(), R.layout.home_popup_area, null);
                mAreaGrid = (GridView) mPopLayout.findViewById(R.id.home_pop_gridview);
                mChangeCityRl = (RelativeLayout) mPopLayout.findViewById(R.id.home_pop_change_city_rl);
                mCurrentCity = (TextView) mPopLayout.findViewById(R.id.home_pop_current_city);
                mPopupWindow = new PopupWindow(mPopLayout, DisplayUtils.getScreenW(getActivity()), ViewGroup.LayoutParams.WRAP_CONTENT);
            }
            mCurrentCity.setText(mCity);
            if (mHomeAreaGridAdapter == null) {
                mHomeAreaGridAdapter = new HomeAreaGridAdapter(mCityAreaDatas);
            }
            mAreaGrid.setAdapter(mHomeAreaGridAdapter);
            mAreaGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // TODO 处理城市列表的点击事件
                }
            });
            mChangeCityRl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO 点击切换城市
                }
            });
            // 设置点击其他部分时，收起PopupWindow，需要设置OutsideTouchable和背景
            mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.setBackgroundDrawable(new ColorDrawable());//记得设置背景--这设置为透明
            mPopupWindow.setAnimationStyle(R.style.popupAnimation);
            mPopupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
            mPopupWindow.setTouchable(true); // 设置popupwindow可点击
            mPopupWindow.setFocusable(true); // 获取焦点
            mPopupWindow.setTouchInterceptor(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                        mPopupWindow.dismiss();
                        onAreaPopupDismiss();
                        return true;
                    }
                    return false;
                }
            });
            mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    onAreaPopupDismiss();
                }
            });
            mPopupWindow.showAsDropDown(mTopView);
            onAreaPopupShow();
            mPopupWindow.update();
        }
    }

    /**
     * 当AreaPopup打开时调用
     */
    private void onAreaPopupShow() {
        mPopBgShadow.setVisibility(View.VISIBLE);
        rotatePlaceImg(true);
    }

    /**
     * 当AreaPopup关闭时调用
     */
    private void onAreaPopupDismiss() {
        mPopBgShadow.setVisibility(View.GONE);
        rotatePlaceImg(false);
    }

    /**
     * 弹出和收起PopupWindow时，箭头的动画切换
     *
     * @param isOpenPopup 是否是打开PopupWindow
     */
    private void rotatePlaceImg(boolean isOpenPopup) {

        if (isOpenPopup) {
            ObjectAnimator rotation1 = ObjectAnimator.ofFloat(mIgvMorePlace, "rotation", 0, 180);
            rotation1.setDuration(200);
            rotation1.start();
        } else {
            ObjectAnimator rotation2 = ObjectAnimator.ofFloat(mIgvMorePlace, "rotation", 180, 0);
            rotation2.setDuration(200);
            rotation2.start();
        }
    }

    @Override
    protected void startLoadData() {

        // 读取上一次定位的位置
        final String lastCity = SPUtils.getString(UIUtils.getContext(), Constants.SP_KEY_LAST_CITY);
        final String lastCityId = SPUtils.getString(UIUtils.getContext(), Constants.SP_KEY_LAST_CITY_ID);
        final String lastLng = SPUtils.getString(UIUtils.getContext(), Constants.SP_KEY_LAST_LNG);
        final String lastLat = SPUtils.getString(UIUtils.getContext(), Constants.SP_KEY_LAST_LAT);
        if (!TextUtils.isEmpty(lastCity)) {
            mTvCity.setText(lastCity);
            mCity = lastCity;
        } else {
            mTvCity.setText("正在定位");
        }

        // ------------ 监听定位的结果，根据当前定位加载数据 -----------------
        UIUtils.getLocationService().setOnLocalFinishListener(new LocationService.OnLocalFinishListener() {
            @Override
            public void onLocalFinish(final LocationEntity locationEntity) {
                if (isFirstLocal) {
                    loadDataOnLocationSuccess(locationEntity, lastCity, lastCityId, lastLng, lastLat);
                    isFirstLocal = false;
                }
            }

            @Override
            public void onLocalFail() {
                if (isFirstLocal) {
                    loadDataOnLocationFail(lastCity, lastCityId, lastLng, lastLat);
                    isFirstLocal = false;
                }
            }
        });

    }

    /**
     * 定位成功后的数据加载
     *
     * @param locationEntity
     * @param lastCity
     * @param lastCityId
     * @param lastLng
     * @param lastLat
     */
    private void loadDataOnLocationSuccess(LocationEntity locationEntity, final String lastCity, final String lastCityId, final String
            lastLng, final String lastLat) {

        // 获取当前位置的信息
        final String currentCity = locationEntity.getCity();
        final String currentLng = locationEntity.getLongitude() + "";
        final String currentLat = locationEntity.getLatitude() + "";

        if (!TextUtils.isEmpty(currentCity) && !TextUtils.isEmpty(currentLng) && !TextUtils.isEmpty(currentLat)) {
            // 将当前的位置信息保存到SP中
            SPUtils.setString(UIUtils.getContext(), Constants.SP_KEY_LAST_CITY, currentCity);
            SPUtils.setString(UIUtils.getContext(), Constants.SP_KEY_LAST_LNG, currentLng);
            SPUtils.setString(UIUtils.getContext(), Constants.SP_KEY_LAST_LAT, currentLat);

            // 判断是否切换了城市
            if (!TextUtils.isEmpty(lastCity) && !currentCity.equals(lastCity)) {
                // 是否切换到当前城市
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("位置变化");
                builder.setMessage("上一次定位城市与当前定位城市不一致，是否切换到当前城市？");
                builder.setPositiveButton("切换", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mTvCity.setText(currentCity);
                        mCity = currentCity;
                        requestCityId(currentCity, currentLng, currentLat);
                    }
                });
                builder.setNegativeButton("不切换", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 根据上一次定位的位置加载数据
                        if (!TextUtils.isEmpty(lastCityId)) {
                            requestDataFromNet(null, lastCityId, lastLng, lastLat, 1);
                        } else {
                            requestCityId(lastCity, lastLng, lastLat);
                        }
                    }
                });
                builder.create().show();
            } else {
                mTvCity.setText(currentCity);
                mCity = currentCity;
                // 城市未变化，直接根据当前位置请求数据
                if (!TextUtils.isEmpty(lastCityId)) {
                    requestDataFromNet(null, lastCityId, currentLng, currentLat, 1);
                } else {
                    requestCityId(currentCity, currentLng, currentLat);
                }
            }
        } else {
            loadDataOnLocationFail(lastCity, lastCityId, lastLng, lastLat);
        }
    }

    /**
     * 定位失败后的数据加载
     *
     * @param lastCity
     * @param lastCityId
     * @param lastLng
     * @param lastLat
     */
    private void loadDataOnLocationFail(String lastCity, String lastCityId, String lastLng, String lastLat) {
        if (!TextUtils.isEmpty(lastCity)) {
            // 如果之前有定位，则先展示上次位置的数据
            if (!TextUtils.isEmpty(lastCityId)) {
                requestDataFromNet(null, lastCityId, lastLng, lastLat, 1);
            } else {
                requestCityId(lastCity, lastLng, lastLat);
            }
//            Toast.makeText(UIUtils.getContext(), "定位失败，无法更新最新数据", Toast.LENGTH_SHORT).show();
        } else {
            // 如果没有定位过，则不处理
            mTvCity.setText("定位失败");
            Toast.makeText(UIUtils.getContext(), "定位失败，无法加载数据", Toast.LENGTH_SHORT).show();
            if (!isRefresh) loadDataFinish(LoadingUI.LoadedResult.ERROR);
            refreshFinish(false);
        }
    }

    /**
     * 根据城市名请求城市ID，在首页请求数据时需要使用到城市ID
     *
     * @param city
     * @param lng
     * @param lat
     */
    private void requestCityId(String city, final String lng, final String lat) {

        String url = Constants.BASE_SERVER + "plugins/appapi/index.php";
        Map<String, String> params = new HashMap<String, String>();

        // 添加请求参数
        params.put("app", "home");
        params.put("act", "get_area_id");
        params.put("city", city);//当前城市名称

        NetWorkUtils.requestGet(url, params, new OnNetWorkListener<String>() {
            @Override
            public void onSuccess(String response) {
                Logger.e(Constants.DUBUG_TAG, "请求城市ID成功：" + response);
                Gson gson = new Gson();
                CityIdEntiry cityIdEntiry = gson.fromJson(response, CityIdEntiry.class);
                if (cityIdEntiry.status == 1) {
                    String currentCityId = cityIdEntiry.data;
                    SPUtils.setString(UIUtils.getContext(), Constants.SP_KEY_LAST_CITY_ID, currentCityId);
                    requestDataFromNet(null, currentCityId, lng, lat, 1);
                } else {
                    Toast.makeText(UIUtils.getContext(), "请求不到城市ID", Toast.LENGTH_SHORT).show();
                    if (!isRefresh) loadDataFinish(LoadingUI.LoadedResult.EMPTY);
                    refreshFinish(false);
                }
            }

            @Override
            public void onFail(Request request, Exception e) {
                Logger.e(Constants.DUBUG_TAG, "请求城市ID失败：" + e.toString());
                if (!isRefresh) loadDataFinish(LoadingUI.LoadedResult.ERROR);
                refreshFinish(false);
            }
        });
    }

    /**
     * 首页，从网络请求数据
     *
     * @param cityId
     * @param lng
     * @param lat
     */
    private void requestDataFromNet(final OnLoadMoreListener listener, String cityId, String lng, String lat, int page) {

        String homeUrl = Constants.BASE_SERVER + "plugins/appapi/index.php";
        Map<String, String> params = new HashMap<String, String>();

        // 添加请求参数
        params.put("app", "home");//首页，定值
        params.put("ad_id", "14");//广告位id，暂为14
        params.put("top_id", "1");//顶级分类id,暂为1

        params.put("p", page + "");//底部list的页数
        params.put("psize", Constants.HOME_LIST_PAGE_SIZE + "");//底部list的每页数量
        params.put("sort", "1");//排序
        params.put("distance", "20");//距离(单位为KM)
        params.put("recommended", "1");//是否推荐(1是0否)

        params.put("lng", lng);//经度
        params.put("lat", lat);//纬度
        params.put("region_ids", cityId);//地区id(深圳为2765)

        NetWorkUtils.requestGet(homeUrl, params, new OnNetWorkListener<String>() {

            @Override
            public void onSuccess(String response) {
                Logger.e(Constants.DUBUG_TAG, "请求数据成功：" + response);
                Gson gson = new Gson();
                HomeEntity homeEntity = gson.fromJson(response, HomeEntity.class);
                if (homeEntity.status == 1) {
                    if (listener != null) {
                        listener.onSucess(homeEntity.data.store_list);
                    } else {
                        mHomeData = homeEntity.data;
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

    private int refreshCount = 0; // TODO 待删除

    @Override
    protected View loadSuccessView() {
        ViewGroup contentView = (ViewGroup) View.inflate(UIUtils.getContext(), R.layout.fragment_home_content, null);
        mBanner = (ConvenientBanner) contentView.findViewById(R.id.home_content_banner);
        mXFCvalue = (TextView) contentView.findViewById(R.id.home_content_xfc_tv_value);
        mGridView = (GridViewForScrollView) contentView.findViewById(R.id.home_content_gridview);
        mListView = (ListViewForScrollView) contentView.findViewById(R.id.home_content_listview);
        mRefreshLayout = (SwipeRefreshLayout) contentView.findViewById(R.id.home_swipe_container);
        initViewData();
        initViewEvent();
        loadCityArea(true);
        return contentView;
    }

    private void initViewEvent() {
        mRefreshLayout.setColorSchemeColors(0xffff0000, 0xff00ff00, 0xff0000ff);
        mRefreshLayout.setOnRefreshListener(this);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO 处理listView的Item点击事件
            }
        });

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO 处理分类TAB的点击事件--实现跳转
                Intent intent = new Intent(getActivity(), BusinessActivity.class);
                startActivity(intent);
            }
        });
        mGridView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return MotionEvent.ACTION_MOVE == event.getAction() ? true
                        : false;
            }
        });//禁止GridView分类TAB滑动
    }

    /**
     * 给主页面的view加载数据
     */
    private void initViewData() {
        Logger.e(Constants.DUBUG_TAG, "刷新界面次数：" + ++refreshCount);
        initBanner();
        mXFCvalue.setText(mHomeData.XFC + "");
        initGridView();
        initListView();
    }

    private void initListView() {
        List<HomeEntity.StoreList> store_list = mHomeData.store_list;
        mListView.setAdapter(new HomeListAdapter(store_list));
    }

    private void initGridView() {
        List<HomeEntity.CateList> cate_list = mHomeData.cate_list;
        mGridView.setAdapter(new HomeGridViewAdapter(cate_list));
    }

    private void initBanner() {
        List<String> imageUrls = getBanerImageUrl();
        mBanner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
            @Override
            public NetworkImageHolderView createHolder() {
                return new NetworkImageHolderView();
            }
        }, imageUrls)
                .setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused})
                .setOnItemClickListener(this);
//        AccordionTransformer transforemer = new AccordionTransformer();
//        mBanner.getViewPager().setPageTransformer(true, transforemer);
        mBanner.startTurning(4000);
    }

    private List<String> getBanerImageUrl() {
        List<HomeEntity.AdvertList> advert_list = mHomeData.advert_list;
        if (advert_list != null) {
            List<String> imageUrls = new ArrayList<String>();
            for (int i = 0; i < advert_list.size(); i++) {
                String imageUrl = advert_list.get(i).thumb;
                imageUrls.add(imageUrl);
            }
            return imageUrls;
        }
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mBanner != null) {
            mBanner.stopTurning();
        }
    }

    /**
     * 广告栏的点击事件处理
     *
     * @param position
     */
    @Override
    public void onItemClick(int position) {
        // TODO 广告栏的点击事件处理
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

    private Runnable mRefreshTask = null;

    /**
     * 刷新完成时调用
     */
    private void refreshFinish(boolean isRefreshFinish) {
        if (isRefresh) {
            isRefresh = false;
            mRefreshLayout.setRefreshing(false);
            if (isRefreshFinish) {
                mPage = 1;
                initViewData();
                Toast.makeText(UIUtils.getContext(), "刷新完成", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(UIUtils.getContext(), "刷新失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * @创建者：杨长福
     * @创建时间：2016/2/15
     * @描述：ListView的Adapter
     */
    class HomeListAdapter extends SuperBaseAdapter<HomeEntity.StoreList> {

        public HomeListAdapter(List<HomeEntity.StoreList> datas) {
            super(datas);
        }

        @Override
        protected BaseHolder<HomeEntity.StoreList> getItemHolder() {
            return new HomeListItemHolder();
        }

        @Override
        protected boolean hasLoadMore() {
            return true;
        }

        /**
         * 加载更多的回调
         *
         * @throws Exception
         */
        @Override
        protected void onLoadMore() throws Exception {

            String loadMoreCityId = SPUtils.getString(UIUtils.getContext(), Constants.SP_KEY_LAST_CITY_ID);
            String loadMoreLng = SPUtils.getString(UIUtils.getContext(), Constants.SP_KEY_LAST_LNG);
            String loadMoreLat = SPUtils.getString(UIUtils.getContext(), Constants.SP_KEY_LAST_LAT);
            requestDataFromNet(new OnLoadMoreListener<HomeEntity.StoreList>() {
                @Override
                public void onSucess(List<HomeEntity.StoreList> list) {
                    onLoadMoreFinish(list);
                }

                @Override
                public void onFail() {
                    onLoadMoreError();
                }
            }, loadMoreCityId, loadMoreLng, loadMoreLat, ++mPage);
        }

        @Override
        protected int getPageSize() {
            return Constants.HOME_LIST_PAGE_SIZE;
        }
    }
}
