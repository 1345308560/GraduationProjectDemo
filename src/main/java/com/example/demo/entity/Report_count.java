package com.example.demo.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name="report_count")
public class Report_count {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    private Integer uid;

    private Integer goods_report;

    private Integer post_report;

    private Integer activity_report;

    private Date create_at;

    private Date update_at;

    private Integer display;

    public Date getCreate_at() {
        return create_at;
    }

    public void setCreate_at(Date create_at) {
        this.create_at = create_at;
    }

    public void setUpdate_at(Date update_at) {
        this.update_at = update_at;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getActivity_report() {
        return activity_report;
    }

    public Integer getGoods_report() {
        return goods_report;
    }

    public Integer getPost_report() {
        return post_report;
    }

    public void setActivity_report(Integer activity_report) {
        this.activity_report = activity_report;
    }

    public void setGoods_report(Integer goods_report) {
        this.goods_report = goods_report;
    }

    public void setPost_report(Integer post_report) {
        this.post_report = post_report;
    }

    public Date getUpdate_at() {
        return update_at;
    }

    public void setDisplay(Integer display) {
        this.display = display;
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
