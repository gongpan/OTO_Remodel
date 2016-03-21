package com.first.fubao.oto.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.first.fubao.oto.utils.Constants;
import com.first.fubao.oto.utils.Logger;
import com.first.fubao.oto.utils.UIUtils;

/**
 * @创建者：杨长福
 * @创建时间：2016/1/26
 * @描述：Fragment的
 */
public abstract class BaseFragment extends Fragment {

    protected LoadingUI mLoadingUI;
    protected View mRootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = initView();
        Logger.e(Constants.DUBUG_TAG, "BaseFragment onCreateView ---- ");
        return mRootView;
    }

    /**
     * 初始化rootView
     * @return
     */
    protected abstract View initView();

    protected View addContentView() {
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
     * 重新加载数据时调用
     */
    protected abstract void refreshLoadData();

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Logger.e(Constants.DUBUG_TAG, "BaseFragment onActivityCreated ========== ");
        startLoadData();
    }

    /**
     * 开始加载数据是调用
     *
     * @return
     */
    protected abstract void startLoadData();

    /**
     * 初始化成功的View时调用
     *
     * @return
     */
    protected abstract View loadSuccessView();

    /**
     * 子类加载数据结束时，必须调用该方法
     *
     * @param result
     */
    protected final void loadDataFinish(LoadingUI.LoadedResult result) {
        if (mLoadingUI != null) {
            mLoadingUI.onLoadDataFinish(result);
        }
    }
}
