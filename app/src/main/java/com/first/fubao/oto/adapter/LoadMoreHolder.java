package com.first.fubao.oto.adapter;

import android.view.View;

import com.first.fubao.oto.R;
import com.first.fubao.oto.utils.UIUtils;

/**
 * @创建者：杨长福
 * @创建时间：2016/2/15
 * @描述：TODO
 */
public class LoadMoreHolder extends BaseHolder<Integer> {

    public static final int STATE_CLICK_LOAD = 0;    // 加载中
    public static final int STATE_LOADING = 1;    // 加载中
    public static final int STATE_ERROR = 2;    // 加载失败
    public static final int STATE_EMPTY = 3;    // 没有加载

    private View mClickLoadView;
    private View mLoadingView;
    private View mErrorView;
    private View mEmptyView;

    @Override
    protected View initView() {
        View view = View.inflate(UIUtils.getContext(), R.layout.item_load_more, null);
        mClickLoadView = view.findViewById(R.id.item_loadmore_click_load);
        mLoadingView = view.findViewById(R.id.item_loadmore_loading);
        mErrorView = view.findViewById(R.id.item_loadmore_error);
        mEmptyView = view.findViewById(R.id.item_loadmore_empty);

        mClickLoadView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mListener.onLoadMoreClick(v);
//                refreshUI(STATE_LOADING);
            }
        });

        mErrorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onLoadMoreClick(v);
//                refreshUI(STATE_LOADING);
            }
        });

        return view;
    }

    @Override
    protected void refreshUI(Integer data) {
        // 根据data判断是否显示那一个
        switch (data) {
            case STATE_CLICK_LOAD:
                mClickLoadView.setVisibility(View.VISIBLE);
                mLoadingView.setVisibility(View.GONE);
                mErrorView.setVisibility(View.GONE);
                mEmptyView.setVisibility(View.GONE);
                break;
            case STATE_LOADING:
                mClickLoadView.setVisibility(View.GONE);
                mLoadingView.setVisibility(View.VISIBLE);
                mErrorView.setVisibility(View.GONE);
                mEmptyView.setVisibility(View.GONE);
                break;
            case STATE_ERROR:
                mClickLoadView.setVisibility(View.GONE);
                mLoadingView.setVisibility(View.GONE);
                mErrorView.setVisibility(View.VISIBLE);
                mEmptyView.setVisibility(View.GONE);
                break;
            case STATE_EMPTY:
                mClickLoadView.setVisibility(View.GONE);
                mLoadingView.setVisibility(View.GONE);
                mErrorView.setVisibility(View.GONE);
                mEmptyView.setVisibility(View.VISIBLE);
                break;
        }
    }

    private OnLoadMoreClickListener mListener;

    public void setOnLoadMoreClickListener(OnLoadMoreClickListener listener) {
        if (listener == null) {
            throw new RuntimeException("OnLoadMoreClickListener can not null");
        }
        this.mListener = listener;
    }

    public interface OnLoadMoreClickListener{
        void onLoadMoreClick(View view);
    }
}
