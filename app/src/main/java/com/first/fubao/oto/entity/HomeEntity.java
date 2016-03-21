package com.first.fubao.oto.entity;

import java.util.List;

/**
 * @创建者：杨长福
 * @创建时间：2016/2/3
 * @描述：首页实体类
 */
public class HomeEntity {

    public HomeData data;// 返回的数据
    public String fhisign; //唯一标识
    public int status; //返回结果:1为成功0为失败2请先登录

    public class HomeData {
        public float XFC; // XFC价格
        public List<AdvertList> advert_list;
        public List<CateList> cate_list;
        public List<StoreList> store_list;
    }

    public class AdvertList {
        public int id; //广告id
        public String thumb; //广告图片
        public String url; //广告链接
    }

    public class CateList {
        public int cate_id;//分类id
        public String cate_name;//分类名称
        public String pic;//分类图片
    }

    public class StoreList {
        public String address;// 地址
        public String brokerage_value;//TODO
        public String consum_avg;//人均消费
        public String consum_sum;//总消费数
        public String credit_value;//信用值
        public String discount_value;//折扣
        public String distance;//距离
        public int fcate_id;//分类
        public String is_closing;//是否打烊
        public float lat_api;// 纬度
        public float lng_api;// 经度
        public float mark_all;//评分
        public String recommend_text;//推荐语
        public String store_disvalue;//TODO 商家折扣？
        public int store_id;//店铺id
        public String store_logo;//店铺logo
        public String store_name;//店铺名称
        public int top_id;//顶级分类id(扩展属性,目前是吃,以后可能有玩,乐等)
    }
}
