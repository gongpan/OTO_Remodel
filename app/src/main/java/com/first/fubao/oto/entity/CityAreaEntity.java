package com.first.fubao.oto.entity;

import java.util.List;

/**
 * @创建者：杨长福
 * @创建时间：2016/2/18
 * @描述：城市地区的实体类
 */
public class CityAreaEntity {

    public List<CityAreaData> data;
    public String fhisign;
    public String msg;
    public int status;

    public class CityAreaData {
        public int region_id;//id
        public String region_name;//名称
    }
}
