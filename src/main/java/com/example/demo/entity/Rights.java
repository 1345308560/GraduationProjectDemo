package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name="rights")
public class Rights {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    private String uid;

    private Integer rights_buy;
    //1是有权限 0是没权限

    private Integer rights_sell;

    private Date create_at;

    private Date update_at;

    private Integer display;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Integer getRights_buy() {
        return rights_buy;
    }

    public void setRights_buy(Integer rights_buy) {
        this.rights_buy = rights_buy;
    }

    public Integer getRights_sell() {
        return rights_sell;
    }

    public void setRights_sell(Integer rights_sell) {
        this.rights_sell = rights_sell;
    }

    public Date getCreate_at() {
        return create_at;
    }

    public void setCreate_at(Date create_at) {
        this.create_at = create_at;
    }

    public Date getUpdate_at() {
        return update_at;
    }

    public void setUpdate_at(Date update_at) {
        this.update_at = update_at;
    }

    public Integer getDisplay() {
        return display;
    }

    public void setDisplay(Integer display) {
        this.display = display;
    }
}

