package com.example.demo.entity;


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


    private BigDecimal price_count;

    private Integer state;//订单状态  0 未完成  1 已完成

    //卖家
    private Integer uid1;

    //买家
    private Integer uid2;

    private Date create_at;

    private Date deal_at;

    private Integer display;

    public Integer getDisplay() {
        return display;
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

}
