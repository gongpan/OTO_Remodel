package com.first.fubao.oto;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.first.fubao.oto.bdmap.LocationEntity;
import com.first.fubao.oto.bdmap.LocationService;
import com.first.fubao.oto.fragment.BusinessFragment;
import com.first.fubao.oto.fragment.HomePagerFragment;
import com.first.fubao.oto.fragment.MyCenterFragment;
import com.first.fubao.oto.utils.Constants;
import com.first.fubao.oto.utils.Logger;
import com.first.fubao.oto.utils.UIUtils;

/**
 * @创建者：杨长福
 * @创建时间：2016/2/1
 * @描述：主页面
 */
public class MainActivity extends Activity implements View.OnClickListener {

    private LinearLayout mTabContainer;
    private RelativeLayout mTabHomePager;
    private RelativeLayout mTabBusiness;
    private RelativeLayout mTabMyCenter;
    private ImageView mHomePagerIgv;
    private ImageView mBusinessIgv;
    private ImageView mMyCenterIgv;

    private TextView mHomePagerTv;
    private TextView mBusinessTv;
    private TextView mMyCenterTv;

    private HomePagerFragment mHomePagerFragment;
    private BusinessFragment mBusinessFragment;
    private MyCenterFragment mMyCenterFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initEvent();
        selectPager(0);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 开启定位
        Logger.e(Constants.DUBUG_TAG, "MainActivity onStart -------------- ");
        UIUtils.getLocationService().start();
    }

    private void initView() {
        mTabContainer = (LinearLayout) findViewById(R.id.main_tab_container);

        mTabHomePager = (RelativeLayout) findViewById(R.id.main_tab_homePager);
        mTabBusiness = (RelativeLayout) findViewById(R.id.main_tab_business);
        mTabMyCenter = (RelativeLayout) findViewById(R.id.main_tab_mycenter);

        mHomePagerIgv = (ImageView) findViewById(R.id.main_tab_homePager_iv);
        mBusinessIgv = (ImageView) findViewById(R.id.main_tab_business_iv);
        mMyCenterIgv = (ImageView) findViewById(R.id.main_tab_mycenter_iv);

        mHomePagerTv = (TextView) findViewById(R.id.main_tab_homePager_tv);
        mBusinessTv = (TextView) findViewById(R.id.main_tab_business_tv);
        mMyCenterTv = (TextView) findViewById(R.id.main_tab_mycenter_tv);
    }

    private void initEvent() {
        mTabHomePager.setOnClickListener(this);
        mTabBusiness.setOnClickListener(this);
        mTabMyCenter.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int mCurrentTab = 0;
        switch (v.getId()) {
            case R.id.main_tab_homePager:
                mCurrentTab = 0;
                break;
            case R.id.main_tab_business:
                mCurrentTab = 1;
                break;
            case R.id.main_tab_mycenter:
                mCurrentTab = 2;
                break;
        }
        selectPager(mCurrentTab);
    }

    /**
     * Fragment之间的切换
     *
     * @param position
     */
    private void selectPager(int position) {
        // 底部Tab状态切换
        mHomePagerIgv.setEnabled(!(position == 0));
        mHomePagerTv.setEnabled(!(position == 0));
        mBusinessIgv.setEnabled(!(position == 1));
        mBusinessTv.setEnabled(!(position == 1));
        mMyCenterIgv.setEnabled(!(position == 2));
        mMyCenterTv.setEnabled(!(position == 2));

        // 切换Fragment
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        hideAllFragment(transaction);
        switch (position) {
            case 0:
                if (mHomePagerFragment == null) {
                    mHomePagerFragment = new HomePagerFragment();
                    transaction.add(R.id.main_fl_container, mHomePagerFragment, "HomePagerFragment");
                } else {
                    transaction.show(mHomePagerFragment);
                }
                break;
            case 1:
                if (mBusinessFragment == null) {
                    mBusinessFragment = new BusinessFragment();
                    transaction.add(R.id.main_fl_container, mBusinessFragment, "BusinessFragment");
                } else {
                    transaction.show(mBusinessFragment);
                }
                break;
            case 2:
                if (mMyCenterFragment == null) {
                    mMyCenterFragment = new MyCenterFragment();
                    transaction.add(R.id.main_fl_container, mMyCenterFragment, "MyCenterFragment");
                } else {
                    transaction.show(mMyCenterFragment);
                }
                break;
        }
        transaction.commit();
    }

    /**
     * 隐藏所有的Fragment
     *
     * @param transaction
     */
    private void hideAllFragment(FragmentTransaction transaction) {
        if (mHomePagerFragment != null && !mHomePagerFragment.isHidden()) {
            transaction.hide(mHomePagerFragment);
        }
        if (mBusinessFragment != null && !mBusinessFragment.isHidden()) {
            transaction.hide(mBusinessFragment);
        }
        if (mMyCenterFragment != null && !mMyCenterFragment.isHidden()) {
            transaction.hide(mMyCenterFragment);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UIUtils.getLocationService().stop();
    }
}
