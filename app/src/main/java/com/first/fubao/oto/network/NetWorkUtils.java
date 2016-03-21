package com.first.fubao.oto.network;

import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.GetBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @创建者：杨长福
 * @创建时间：2016/2/17
 * @描述：网络请求的工具类
 */
public class NetWorkUtils {

    public static void requestGet(String url, Map<String, String> params, final OnNetWorkListener listener) {

        GetBuilder builder = OkHttpUtils.get().url(url);

        // 添加请求参数
        Set<Map.Entry<String, String>> entrySet = params.entrySet();
        Iterator<Map.Entry<String, String>> iterator = entrySet.iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            String key = entry.getKey();
            String value = entry.getValue();
            builder.addParams(key, value);
        }

        builder.build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        listener.onFail(request, e);
                    }

                    @Override
                    public void onResponse(String response) {
                        listener.onSuccess(response);
                    }
                });
    }
}
