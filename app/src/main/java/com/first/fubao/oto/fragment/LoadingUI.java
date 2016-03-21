package com.first.fubao.oto.fragment;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.FrameLayout;

import com.first.fubao.oto.R;

/**
 * @创建者：杨长福
 * @创建时间：2016/1/26
 * @描述：负责界面切换的管理类
 */
public abstract class LoadingUI extends FrameLayout {

    private static final int STATE_LOADING = 0; // 加载状态
    private static final int STATE_ERROR = 1; // 失败状态
    private static final int STATE_EMPTY = 2; // 空状态
    private static final int STATE_SUCCESS = 3; // 成功状态

    private int mCurrentState = STATE_LOADING;// 默认没有状态

    private View mLoadingView;
    private View mErrorView;
    private View mEmptyView;
    private View mSuccessView;

    public LoadingUI(Context context) {
        this(context, null);
    }

    public LoadingUI(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        if (mLoadingView == null) {
            mLoadingView = View.inflate(getContext(), R.layout.pager_loading, null);
            addView(mLoadingView);
        }
        if (mErrorView == null) {
            mErrorView = View.inflate(getContext(), R.layout.pager_error, null);
            Button mButton = (Button) mErrorView.findViewById(R.id.error_btn_retry);
            mButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickAfreshLoad();
                }
            });
            addView(mErrorView);
        }

        if (mEmptyView == null) {
            mEmptyView = View.inflate(getContext(), R.layout.pager_empty, null);
            addView(mEmptyView);
        }

        updateUI();
    }

    /**
     * 点击重新加载数据
     */
    protected abstract void clickAfreshLoad();

    // 根据状态更新UI
    public void updateUI() {
        mLoadingView.setVisibility(mCurrentState == STATE_LOADING ? View.VISIBLE : View.GONE);
        mErrorView.setVisibility(mCurrentState == STATE_ERROR ? View.VISIBLE : View.GONE);
        mEmptyView.setVisibility(mCurrentState == STATE_EMPTY ? View.VISIBLE : View.GONE);

        // 成功的Viwe是否显示
        if (mCurrentState == STATE_SUCCESS && mSuccessView == null) {
            mSuccessView = onLoadSuccessView();
            if (mSuccessView == null) {
                throw new RuntimeException("onLoadSuccessView() method can not return null");
            }
            // ##################################
            ViewParent parent = mSuccessView.getParent();
            if (parent != null && parent instanceof ViewGroup)
            {
                ((ViewGroup) parent).removeView(mSuccessView);
            }
            // ##################################
            addView(mSuccessView);
        }
        if (mSuccessView != null) {
            mSuccessView.setVisibility(mCurrentState == STATE_SUCCESS ? View.VISIBLE : View.GONE);
        }
    }

    public void onLoadDataFinish(LoadedResult result) {
        if (result == null) {
            throw new RuntimeException("onLoadingData() method can not return null");
        }
        mCurrentState = result.getState();
        updateUI();
    }

    // 让具体的操作者返回指定的SuccessView
    public abstract View onLoadSuccessView();

    public enum LoadedResult {
        ERROR(STATE_ERROR), SUCCESS(STATE_SUCCESS), EMPTY(STATE_EMPTY), LOADING(STATE_LOADING);
        private int state;

        private LoadedResult(int state) {
            this.state = state;
        }

        public int getState() {
            return state;
        }
    }
}
