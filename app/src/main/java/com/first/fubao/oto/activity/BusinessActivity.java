package com.first.fubao.oto.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.first.fubao.oto.R;
import com.first.fubao.oto.controller.BusinessController;

/**
 * @创建者：杨长福
 * @创建时间：2016/3/7
 * @描述：商家TAB对应的Activity
 */
public class BusinessActivity extends Activity {

    private EditText mEtSearch;
    private ImageView mImgOpenMap;
    private BusinessController mController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewGroup rootView = (ViewGroup) View.inflate(this, R.layout.fragment_business_ui, null);
        setContentView(rootView);

        mController = new BusinessController(this);
        mController.startLoadData();

        ViewGroup topView = (ViewGroup) rootView.getChildAt(0);
        mEtSearch = (EditText) topView.findViewById(R.id.business_et_search);
        mImgOpenMap = (ImageView) topView.findViewById(R.id.business_iv_open_map);

        FrameLayout mContainerView = (FrameLayout) findViewById(R.id.fragment_business_content_container);
        mContainerView.addView(mController.getRootView());

        mEtSearch.setInputType(InputType.TYPE_NULL);

        initTopView();
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
