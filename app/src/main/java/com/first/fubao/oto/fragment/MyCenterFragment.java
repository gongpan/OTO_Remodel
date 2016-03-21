package com.first.fubao.oto.fragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.first.fubao.oto.R;
import com.first.fubao.oto.utils.UIUtils;

import java.util.Random;

/**
 * @创建者：杨长福
 * @创建时间：2016/1/26
 * @描述：个人中心TAB对应的Fragment
 */
public class MyCenterFragment extends BaseFragment {

    private ImageView mIvSetting;
    private FrameLayout mContentView;

    @Override
    protected View initView() {
        ViewGroup viewGroup = (ViewGroup) View.inflate(UIUtils.getContext(), R.layout.fragment_mycenter_ui, null);
        mContentView = (FrameLayout) viewGroup.findViewById(R.id.mycenter_content_container);
        mContentView.addView(addContentView());

        ViewGroup topView = (ViewGroup) viewGroup.getChildAt(0);
        mIvSetting = (ImageView) topView.findViewById(R.id.mycenter_img_setting);
        return viewGroup;
    }

    /**
     * 重新加载数据
     */
    @Override
    protected void refreshLoadData() {

    }

    @Override
    protected void startLoadData() {
        final LoadingUI.LoadedResult[] results = new LoadingUI.LoadedResult[]{LoadingUI.LoadedResult.EMPTY,
                LoadingUI.LoadedResult.ERROR, LoadingUI.LoadedResult.SUCCESS};
        final Random rdm = new Random();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                final LoadingUI.LoadedResult result = results[rdm.nextInt(results.length)];

                UIUtils.getMainHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        loadDataFinish(LoadingUI.LoadedResult.SUCCESS);
                    }
                });
            }
        }).start();
    }

    @Override
    protected View loadSuccessView() {
        ViewGroup contentView = (ViewGroup) View.inflate(UIUtils.getContext(), R.layout.fragment_mycenter_content, null);
        return contentView;
    }
}
