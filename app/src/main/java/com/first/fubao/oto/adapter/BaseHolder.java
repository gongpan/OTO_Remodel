package com.first.fubao.oto.adapter;

import android.view.View;

/**
 * @创建者：杨长福
 * @创建时间：2016/2/15
 * @描述：TODO
 */
public abstract class BaseHolder<T> {

    protected View mRootView;
    protected T mData;

    public BaseHolder() {
        mRootView = initView();
    }

    /**
     * 初始化view
     */
    protected abstract View initView();

    /**
     * 设置数据
     */
    public void setData(T data) {
        this.mData = data;
        // 根据数据改变UI
        refreshUI(data);
    }

    protected abstract void refreshUI(T data);

    public View getRootView() {
        return mRootView;
    }
}
