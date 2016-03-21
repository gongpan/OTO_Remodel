package com.first.fubao.oto.network;

import java.util.List;

/**
 * @创建者：杨长福
 * @创建时间：2016/2/17
 * @描述：TODO
 */
public interface OnLoadMoreListener<T> {

    void onSucess(List<T> list);

    void onFail();
}
