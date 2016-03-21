package com.first.fubao.oto.entity;

/**
 * @创建者：杨长福
 * @创建时间：2016/2/22
 * @描述：TODO
 */
public class MemberInfoEntity {

    public MenberInfoData data;
    public String fhisign;
    public String msg;
    public int status;

    public class MenberInfoData {
        public String address;//详细地址
        public String addressAreas;//省市区
        public String birthday;//生日
        public String consume_coupon;//TODO
        public String email;//邮箱
        public String gender;//性别0保密(未选)1男2女 TODO 实际直接返回结果
        public String level_name;//TODO
        public String nickname;//昵称
        public String order_percentage;//TODO
        public String phone_mob;//TODO
        public String portrait;//TODO
        public String qrcode;//TODO
        public String recommendcode;//TODO
        public String ry_token;//TODO
        public String user_id;//TODO
        public String user_name;//TODO
        public String xfc;//TODO
        //TODO 缺少"idcard": "", //身份证号码
    }
}
