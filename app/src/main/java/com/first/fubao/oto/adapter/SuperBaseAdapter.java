package com.first.fubao.oto.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.first.fubao.oto.utils.Constants;
import com.first.fubao.oto.utils.UIUtils;

import java.util.List;

/**
 * @创建者：杨长福
 * @创建时间：2016/2/15
 * @描述：自定义ListView的公共Adapter
 */
public abstract class SuperBaseAdapter<T> extends BaseAdapter {

    // 标记不能断层，必须从0开始
    private static final int TYPE_LOAD_MORE = 0;
    private static final int TYPE_NORMAL = 1;

    // 记录LoadMoreHolder的状态
    private int mState = LoadMoreHolder.STATE_CLICK_LOAD;

    private List<T> mDatas;

    private LoadMoreHolder mMoreHolder;

    public SuperBaseAdapter(List<T> datas) {
        this.mDatas = datas;
    }

    @Override
    public int getCount() {
        if (mDatas != null) {
            return mDatas.size() + 1;
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

    // 获得position对应的item的类型
    @Override
    public int getItemViewType(int position) {
        // 如果是最后一条数据
        if (position == getCount() - 1) {
            // 加载更多的类型
            return TYPE_LOAD_MORE;
        }
        // 普通类型
        return TYPE_NORMAL;
    }

    // listView的item显示的种类有几种
    @Override
    public int getViewTypeCount() {
        // 有加载更多的item
        return super.getViewTypeCount() + 1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // ### 一、加载View ##########################
        BaseHolder holder = null;
        if (convertView == null) {
            // ------- 没有复用 ------
            // 1. 创建holder
            if (getItemViewType(position) == TYPE_LOAD_MORE) {
                // 加载更多的item
                holder = getLoadMoreHolder();
            } else {
                // 普通item
                holder = getItemHolder();// 去实现某一个holders
            }
            // 2. 加载布局，初始化view
            convertView = holder.getRootView();
            // 3. 设置标记
            convertView.setTag(holder);
        } else {
            // ------ 复用 ------
            holder = (BaseHolder) convertView.getTag();
        }

        // ####### 二、给view加载数据###################
        if (getItemViewType(position) == TYPE_LOAD_MORE) {
            // 加载更多，给加载更多的View去加载数据
            if (hasLoadMore()) {
                // 有加载更多的功能
                performLoadMore(mState);
            } else {
                // 没有加载更多的功能
                mMoreHolder.setData(LoadMoreHolder.STATE_EMPTY);// 没有加载更多
            }
        } else {
            // 获取数据
            T data = mDatas.get(position);
            // 给view设置数据
            holder.setData(data);
        }

        return convertView;
    }

    private void performLoadMore(int state) {

        // 1. 加载更多的显示
        mMoreHolder.setData(state);

        mMoreHolder.setOnLoadMoreClickListener(new LoadMoreHolder.OnLoadMoreClickListener() {

            @Override
            public void onLoadMoreClick(View view) {

                // 2. 显示正在加载
                mMoreHolder.setData(LoadMoreHolder.STATE_LOADING);
                mState = LoadMoreHolder.STATE_LOADING;

                // 如果正在加载中，不往下执行 TODO

                // 3. 去加载数据--->List
                try {
                    onLoadMore();
                } catch (Exception e) {
                    e.printStackTrace();
                    // 加载数据错误:
                    mMoreHolder.setData(LoadMoreHolder.STATE_ERROR);
                    mState = LoadMoreHolder.STATE_ERROR;
                }
            }
        });

    }

    /**
     * 是否有加载更多的功能,如果孩子不要加载更多，那么就复写
     *
     * @return 默认true
     */
    protected boolean hasLoadMore() {
        return true;
    }

    private LoadMoreHolder getLoadMoreHolder() {
        if (mMoreHolder == null) {
            mMoreHolder = new LoadMoreHolder();
        }
        return mMoreHolder;
    }

    protected abstract BaseHolder<T> getItemHolder();

    protected void onLoadMore() throws Exception {}

    protected void onLoadMoreFinish(List<T> loadMoreDatas){

        if (loadMoreDatas == null || loadMoreDatas.size() == 0) {
            // 说明服务器没有更多数据
            mMoreHolder.setData(LoadMoreHolder.STATE_EMPTY);
            mState = LoadMoreHolder.STATE_EMPTY;
        } else {
            int size = loadMoreDatas.size();
            if (size < getPageSize()) {
                // 服务器也没有数据
                mMoreHolder.setData(LoadMoreHolder.STATE_EMPTY);
                mState = LoadMoreHolder.STATE_EMPTY;
            } else {
                mMoreHolder
                        .setData(LoadMoreHolder.STATE_CLICK_LOAD);
                mState = LoadMoreHolder.STATE_CLICK_LOAD;
            }
            // 3. List添加到 mDatas
            mDatas.addAll(loadMoreDatas);
            // 4. notifydatasetChange
            notifyDataSetChanged();
        }
    }

    protected void onLoadMoreError(){
        // 加载数据错误:
        mMoreHolder.setData(LoadMoreHolder.STATE_ERROR);
        mState = LoadMoreHolder.STATE_ERROR;
    }

    protected int getPageSize(){
        return Constants.PAGER_SIZE;
    }
}
