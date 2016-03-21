package com.first.fubao.oto.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.first.fubao.oto.R;
import com.first.fubao.oto.entity.BusinessEntity;
import com.first.fubao.oto.utils.UIUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @创建者：杨长福
 * @创建时间：2016/2/16
 * @描述：TODO
 */
public class BusinessListItemHolder extends BaseHolder<BusinessEntity.StoreData> {

    private ImageView mImageView;
    private TextView mTvStoreTitle;
    private RatingBar mRatingBar;
    private TextView mTvPerMoney;
    private TextView mTvZK;
    private TextView mTvPlace;
    private TextView mTvDistance;

    @Override
    protected View initView() {
        View view = View.inflate(UIUtils.getContext(), R.layout.item_business_listview, null);
        mImageView = (ImageView) view.findViewById(R.id.business_item_img);
        mTvStoreTitle = (TextView) view.findViewById(R.id.business_item_title);
        mRatingBar = (RatingBar) view.findViewById(R.id.business_item_ratingBar);
        mTvPerMoney = (TextView) view.findViewById(R.id.business_item_per_money_tv);
        mTvZK = (TextView) view.findViewById(R.id.business_item_zk);
        mTvPlace = (TextView) view.findViewById(R.id.business_item_place);
        mTvDistance = (TextView) view.findViewById(R.id.business_item_distance);
        return view;
    }

    @Override
    protected void refreshUI(BusinessEntity.StoreData data) {
        ImageLoader.getInstance().displayImage(data.store_logo, mImageView, UIUtils.getImageLoadOptions());
        mTvStoreTitle.setText(data.store_name);
        mRatingBar.setRating(data.mark_all);
        mTvPerMoney.setText(data.consum_avg);
        if (TextUtils.isEmpty(data.store_disvalue)) {
            mTvZK.setText("不打折");
        } else {
            mTvZK.setText(data.store_disvalue + "折");
        }
        mTvPlace.setText(data.address);
        mTvDistance.setText(data.distance);
    }
}
