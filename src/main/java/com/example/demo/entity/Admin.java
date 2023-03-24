package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="admin")
public class Admin {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer number;

    private String id;//手机号11位

    private String password;

    //private LocalDateTime created_at;

    //private LocalDateTime update_at;

    private Boolean display;


}
