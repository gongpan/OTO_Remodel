package com.first.fubao.oto.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.first.fubao.oto.R;
import com.first.fubao.oto.entity.CityAreaEntity;
import com.first.fubao.oto.utils.UIUtils;

import java.util.List;

/**
 * @创建者：杨长福
 * @创建时间：2016/2/18
 * @描述：TODO
 */
public class HomeAreaGridAdapter extends BaseAdapter {

    private final List<CityAreaEntity.CityAreaData> mDatas;

    public HomeAreaGridAdapter(List<CityAreaEntity.CityAreaData> datas){
        this.mDatas = datas;
    }

    @Override
    public int getCount() {
        if (mDatas != null) {
            return mDatas.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (mDatas != null) {
            return mDatas.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = View.inflate(UIUtils.getContext(), R.layout.item_home_gridview_area, null);
        TextView tv = (TextView) convertView.findViewById(R.id.item_home_grid_area_tv);

        CityAreaEntity.CityAreaData areaData = mDatas.get(position);
        tv.setText(areaData.region_name);

        return convertView;
    }
}
