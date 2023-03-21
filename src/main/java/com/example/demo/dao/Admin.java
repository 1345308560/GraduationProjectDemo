package com.example.demo.dao;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class Admin {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer number;

    private String id;//手机号11位

    private String password;

    //private LocalDateTime created_at;

    //private LocalDateTime update_at;

    private boolean display;

    public Integer getNumber(){
        return this.number;
    }

    public void setNumber(Integer number){
        this.number=number;
    }

    public String getId(){
        return this.id;
    }

    public void setId(String id){
        this.id=id;
    }

    public String getPassword(){
        return this.password;
    }

    public void setPassword(String password){
        this.password=password;
    }



}
