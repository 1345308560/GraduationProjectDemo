package com.example.demo.entity;


import com.alibaba.fastjson.JSONObject;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@Table(name="orders")
public class Orders {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    private String orders_id;//订单编号


    private JSONObject json_value;

    private BigDecimal price_count;

    private Integer state;//订单状态  0 未完成  1 已完成

    //卖家
    private Integer uid1;

    //买家
    private Integer uid2;

    private Date create_at;

    private Date deal_at;

    private Integer display;

    private  String uuid;

    public Boolean getDisplay() {
        // 返回display的Boolean值
        return display == 1;
    }

    public void setDisplay(Boolean display) {
        // 将Boolean值转换为Integer值
        this.display = display ? 1 : 0;
    }

    public void setCreate_at(Date create_at) {
        this.create_at = create_at;
    }

    public Date getCreate_at() {
        return create_at;
    }

    public void setDisplay(Integer display) {
        this.display = display;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }


    public Date getDeal_at() {
        return deal_at;
    }


    public Integer getUid1() {
        return uid1;
    }

    public Integer getUid2() {
        return uid2;
    }

    public void setDeal_at(Date deal_at) {
        this.deal_at = deal_at;
    }


    public void setUid1(Integer uid1) {
        this.uid1 = uid1;
    }

    public void setUid2(Integer uid2) {
        this.uid2 = uid2;
    }

    public String getOrders_id() {
        return orders_id;
    }

    public void setOrders_id(String orders_id) {
        this.orders_id = orders_id;
    }

    public void setPrice_count(BigDecimal price_count) {
        this.price_count = price_count;
    }

    public BigDecimal getPrice_count() {
        return price_count;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public JSONObject getJson_value() {
        return json_value;
    }

    public String getUuid() {
        return uuid;
    }

    public void setJson_value(JSONObject json_value) {
        this.json_value = json_value;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
