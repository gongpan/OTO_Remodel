package com.first.fubao.oto.entity;

import com.baidu.platform.comapi.map.E;

import java.util.List;

/**
 * @创建者：杨长福
 * @创建时间：2016/2/16
 * @描述：商家列表的实体类
 */
public class BusinessEntity {

    public List<StoreData> data;
    public String fhisign;
    public String msg;
    public int page_total;//分页总数
    public int status;
    public int total;//全部记录总数

    public class StoreData{
        public String address;// 店铺地址
        public String brokerage_value;// TODO
        public String consum_avg;// 人均消费
        public int consum_sum;//消费次数
        public String credit_value;//等级积分
        public String discount_value;//折扣
        public String distance;//距离
        public String fcate_id;// TODO
        public int is_closing;// 1已经打烊0正常营业
        public String lat_api;// 纬度
        public String lng_api;// 经度
        public float mark_all;//店铺总评分
        public String recommend_text;//推荐语
        public String store_disvalue;// TODO 商家折扣？
        public String store_id;//店铺id
        public String store_logo;//店铺logo--图片地址
        public String store_name;//店铺名称
        public String top_id;//顶级分类
    }
}
