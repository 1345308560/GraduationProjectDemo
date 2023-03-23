package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity // This tells Hibernate to make a table out of this class
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer number;

    private String id;

    private String password;

    private String name;

    private String addr;

}
