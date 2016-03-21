package com.first.fubao.oto.network;

import com.squareup.okhttp.Request;

/**
 * @创建者：杨长福
 * @创建时间：2016/2/17
 * @描述：TODO
 */
public interface OnNetWorkListener<T> {

    void onSuccess(T response);

    void onFail(Request request, Exception e);
}
