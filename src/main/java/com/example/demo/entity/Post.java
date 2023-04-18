package com.example.demo.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name="post")
public class Post {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    private String pid;//主贴编号

    private Integer sid;//版块编号

    private Integer uid;//发帖人编号

    private String title;//标题

    private String content;//内容

    private String img1;

    private String img2;

    private String img3;

    private Integer reply_count;//回复

    private Integer pclick_count;//点击

    private Integer likes;//点赞

    private Date create_at;

    private Date update_at;

    private Integer display;

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getUid() {
        return uid;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public Integer getPclick_count() {
        return pclick_count;
    }

    public Integer getReply_count() {
        return reply_count;
    }

    public Integer getSid() {
        return sid;
    }

    public String getContent() {
        return content;
    }

    public Integer getLikes() {
        return likes;
    }

    public String getImg1() {
        return img1;
    }

    public String getImg2() {
        return img2;
    }

    public void setCreate_at(Date create_at) {
        this.create_at = create_at;
    }

    public Date getUpdate_at() {
        return update_at;
    }

    public Date getCreate_at() {
        return create_at;
    }

    public void setUpdate_at(Date update_at) {
        this.update_at = update_at;
    }

    public String getImg3() {
        return img3;
    }

    public String getPid() {
        return pid;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setImg1(String img1) {
        this.img1 = img1;
    }

    public void setImg2(String img2) {
        this.img2 = img2;
    }

    public void setImg3(String img3) {
        this.img3 = img3;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public void setPclick_count(Integer pclick_count) {
        this.pclick_count = pclick_count;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }

    public void setReply_count(Integer reply_count) {
        this.reply_count = reply_count;
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
