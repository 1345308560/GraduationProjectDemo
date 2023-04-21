package com.example.demo.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity // This tells Hibernate to make a table out of this class
@Table(name = "usercomment")
public class Usercomment {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    private Integer uid1; //用户账号

    private Integer uid2; //评价账号

    private String comment;

    private Date create_at;

    private Date update_at;

    private Integer display;

    public Integer getId() {
        return id;
    }

    public void setUpdate_at(Date update_at) {
        this.update_at = update_at;
    }

    public Date getCreate_at() {
        return create_at;
    }

    public Date getUpdate_at() {
        return update_at;
    }

    public void setCreate_at(Date create_at) {
        this.create_at = create_at;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setDisplay(Integer display) {
        this.display = display;
    }

    public Integer getUid2() {
        return uid2;
    }

    public Integer getUid1() {
        return uid1;
    }

    public void setUid1(Integer uid1) {
        this.uid1 = uid1;
    }

    public void setUid2(Integer uid2) {
        this.uid2 = uid2;
    }

    public Integer getDisplay() {
        return display;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}

