package com.first.fubao.oto.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.first.fubao.oto.R;
import com.first.fubao.oto.controller.BusinessController;
import com.first.fubao.oto.utils.UIUtils;

/**
 * @创建者：杨长福
 * @创建时间：2016/2/14
 * @描述：商家TAB对应的Fragment
 */
public class BusinessFragment extends Fragment {

    private EditText mEtSearch;
    private ImageView mImgOpenMap;
    private BusinessController mController;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mController = new BusinessController(getActivity());
        mController.startLoadData();

        ViewGroup viewGroup = (ViewGroup) View.inflate(UIUtils.getContext(), R.layout.fragment_business_ui, null);

        ViewGroup topView = (ViewGroup) viewGroup.getChildAt(0);
        mEtSearch = (EditText) topView.findViewById(R.id.business_et_search);
        mImgOpenMap = (ImageView) topView.findViewById(R.id.business_iv_open_map);

        FrameLayout mContainerView = (FrameLayout) viewGroup.findViewById(R.id.fragment_business_content_container);
        mContainerView.addView(mController.getRootView());

        mEtSearch.setInputType(InputType.TYPE_NULL);

        initTopView();

        return viewGroup;
    }

    private void initTopView(){
        // 处理顶部搜索栏的点击事件
        mEtSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO 处理顶部搜索栏的点击事件
            }
        });

        // 处理顶部地图按钮的点击事件
        mImgOpenMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO 处理顶部地图按钮的点击事件
            }
        });
    }

}
