package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity // This tells Hibernate to make a table out of this class
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    private String username;

    private String password;

    private String num;//学号

    private String phone;//手机号

    private String qq;//qq号

    private String addr;

    private Integer ban;

    private String token;

    private Date create_at;

    private Date update_at;

    private Integer display;

    private String uuid;

    public Boolean getDisplay() {
        // 返回display的Boolean值
        return display == 1;
    }

    public void setDisplay(Boolean display) {
        // 将Boolean值转换为Integer值
        this.display = display ? 1 : 0;
    }
}
