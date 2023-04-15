package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name="report")
public class Report {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    private Integer uid1; //被举报账号

    private String reason;

    private Integer type;

    private Integer uid2;//举报人

    private Date create_at;

    private Date update_at;

    private Integer display;

    public Date getUpdate_at() {
        return update_at;
    }

    public void setUpdate_at(Date update_at) {
        this.update_at = update_at;
    }


    public void setCreate_at(Date create_at) {
        this.create_at = create_at;
    }

    public Date getCreate_at() {
        return create_at;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setDisplay(Integer display) {
        this.display = display;
    }

    public void setUid2(Integer uid2) {
        this.uid2 = uid2;
    }

    public void setUid1(Integer uid1) {
        this.uid1 = uid1;
    }

    public Integer getType() {
        return type;
    }

    public Integer getUid1() {
        return uid1;
    }

    public Integer getUid2() {
        return uid2;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Boolean getDisplay() {
        // 返回display的Boolean值
        return display == 1;
    }

    public void setDisplay(Boolean display) {
        // 将Boolean值转换为Integer值
        this.display = display ? 1 : 0;
    }

}

