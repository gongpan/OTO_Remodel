package com.first.fubao.oto.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.first.fubao.oto.R;
import com.first.fubao.oto.entity.HomeEntity;
import com.first.fubao.oto.utils.UIUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @创建者：杨长福
 * @创建时间：2016/2/15
 * @描述：TODO
 */
public class HomeListItemHolder extends BaseHolder<HomeEntity.StoreList> {

    private ImageView mImageView;
    private TextView mTxtZK;
    private TextView mTxtTitle;
    private RatingBar mRatingBar;
    private TextView mTxtIntroduce;
    private TextView mTxtMoney;
    private TextView mTxtPlace;

    @Override
    protected View initView() {
        View view = View.inflate(UIUtils.getContext(), R.layout.item_home_listview, null);
        mImageView = (ImageView) view.findViewById(R.id.home_list_main_img);
        mTxtZK = (TextView) view.findViewById(R.id.home_list_txt_zk);
        mTxtTitle = (TextView) view.findViewById(R.id.home_list_item_title);
        mRatingBar = (RatingBar) view.findViewById(R.id.home_list_ratingBar);
        mTxtIntroduce = (TextView) view.findViewById(R.id.home_list_item_introduce);
        mTxtMoney = (TextView) view.findViewById(R.id.home_list_item_money);
        mTxtPlace = (TextView) view.findViewById(R.id.home_list_place);
        return view;
    }

    @Override
    protected void refreshUI(HomeEntity.StoreList data) {
        ImageLoader.getInstance().displayImage(data.store_logo, mImageView, UIUtils.getImageLoadOptions());
        if (!TextUtils.isEmpty(data.store_disvalue)) {
            mTxtZK.setText(data.store_disvalue + "折");
        } else {
            mTxtZK.setText("不打折");
        }
        mTxtTitle.setText(data.store_name);
        mRatingBar.setRating(data.mark_all);
        mTxtIntroduce.setText(data.recommend_text);
        mTxtMoney.setText(data.consum_avg);
        mTxtPlace.setText(data.address);
    }
}
