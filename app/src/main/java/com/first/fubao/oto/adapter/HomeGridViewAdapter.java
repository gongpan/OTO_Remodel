package com.first.fubao.oto.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.first.fubao.oto.R;
import com.first.fubao.oto.entity.HomeEntity;
import com.first.fubao.oto.utils.UIUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * @创建者：杨长福
 * @创建时间：2016/2/15
 * @描述：主页面GridView的Adapter
 */
public class HomeGridViewAdapter extends BaseAdapter {

    private List<HomeEntity.CateList> cate_list;

    public HomeGridViewAdapter(List<HomeEntity.CateList> cate_list){
        this.cate_list = cate_list;
    }

    @Override
    public int getCount() {
        if (cate_list != null) {
            return cate_list.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (cate_list != null) {
            return cate_list.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = View.inflate(UIUtils.getContext(), R.layout.item_home_gridview_tab, null);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.home_grid_img);
        TextView textView = (TextView) convertView.findViewById(R.id.home_grid_txt);

        HomeEntity.CateList cateList = cate_list.get(position);
        ImageLoader.getInstance().displayImage(cateList.pic, imageView, UIUtils.getImageLoadOptions());
        textView.setText(cateList.cate_name);
        return convertView;
    }
}
