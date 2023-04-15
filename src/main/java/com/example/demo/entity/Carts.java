package com.example.demo.entity;


import com.alibaba.fastjson.JSONObject;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@Table(name="carts")
public class Carts {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    private String orders_id;

    private JSONObject json_value;

    private Date create_at;

    private Date update_at;

    private Integer display;

    private String uuid;

    public void setJson_value(JSONObject json_value) {
        this.json_value = json_value;
    }

    public JSONObject getJson_value() {
        return json_value;
    }

    public void setOrders_id(String orders_id) {
        this.orders_id = orders_id;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public String getOrders_id() {
        return orders_id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setDisplay(Integer display) {
        this.display = display;
    }

    public Integer getId() {
        return id;
    }

    public Date getCreate_at() {
        return create_at;
    }

    public void setCreate_at(Date create_at) {
        this.create_at = create_at;
    }

    public Integer getDisplay() {
        return display;
    }

    public void setUpdate_at(Date update_at) {
        this.update_at = update_at;
    }

    public Date getUpdate_at() {
        return update_at;
    }

}
